package app_agent;

import java.lang.instrument.Instrumentation;

/**
 *  High-Level Overview: ASM + ClassLoader + JVM at Runtime
 *  1. ClassLoader: The Gatekeeper
 *      The ClassLoader is responsible for loading Java classes into the JVM at runtime.
 *      By default, the JVM uses a hierarchy of class loaders to load classes (e.g., bootstrap, system class loader, custom class loaders).
 *      Classes are loaded from .class files (either from disk, network, or other sources) as bytecode into the JVM's memory.
 *
 *  2. Bytecode: The Core of ASM
 *      Bytecode is the compiled representation of a Java class.
 *      Each .class file is made of raw bytecode, which is interpreted and executed by the JVM. This bytecode contains:
 *          Constants
 *          Fields and methods
 *          Instructions (the actual JVM opcodes like aload, ldc, invokevirtual, etc.)
 *    ASM operates directly on this bytecode:
 *      ASM reads the bytecode, analyzes it, modifies it, and can even generate entirely new bytecode.
 *
 *  3. Class Instrumentation / Modification via ASM
 *      When you're using ASM to instrument or modify classes at runtime, this is what happens:
 *      Steps in Dynamic Class Manipulation:
 * Intercept Class Loading:
 *      If you're using ASM in conjunction with a custom ClassLoader (or Java Agent), you'll intercept class loading to manipulate the bytecode before it gets loaded into the JVM.
 *      You can do this using a ClassLoader or Java Instrumentation Agent.
 *      java.lang.instrument API allows modifying classes during runtime before they are loaded.
 * ASM Reads the Bytecode:
 *      ASM uses ClassReader to read a .class file into memory and analyze the bytecode.
 *      ClassReader takes the bytecode and breaks it down into its components, such as methods, fields, and the constant pool.
 * Modify the Bytecode:
 *      ASM can be used to transform or instrument specific methods or fields.
 *      Add new methods, change existing methods, insert logging statements, or even replace a class entirely.
 *      This is done by visiting each part of the class (fields, methods, instructions) using visitor classes like ClassVisitor, MethodVisitor, FieldVisitor, and InsnList.
 * Write Modified Bytecode:
 *      Once ASM finishes modifying the class, the modified bytecode is passed to a ClassWriter, which then rewrites it into a new bytecode format.
 * Load the Modified Class:
 *       If you're using a custom ClassLoader, it can then load the modified class using defineClass() method or by returning the modified bytes from the agent.
 *       If using a Java Agent, the JVM will dynamically load the transformed bytecode into the running application.
 *
 *
 * 4. Java Agent for Dynamic Instrumentation
 *      A Java Agent can be used to modify classes before they are loaded by the JVM. It's typically used for profiling, debugging, or instrumenting third-party libraries.
 *      Using Instrumentation API:
 *      The Instrumentation interface in Java allows agents to modify classes dynamically while the JVM is running.
 *
 * 5. JVM Loads the Modified Class
 *      After ASM modifies the bytecode, and the bytecode is loaded via a ClassLoader or Java Agent, the JVM treats it just like any other class:
 *      Instance creation, method calls, field accesses, etc., will work normally.
 *      The modified code is now part of the running application.
 *
 *
 *   Summary: How ASM and JVM Work Together at Runtime
 *
 *      JVM ClassLoader is responsible for loading .class files (bytecode) into memory.
 *      ASM intercepts and manipulates this bytecode (either directly through a custom ClassLoader or with a Java Agent).
 *      Modified Bytecode is then loaded into the JVM.
 *      The JVM executes the modified classes as normal, with new or altered behavior (method call logging, performance monitoring, etc.).
 * */

/**
 * Javaagent представляет из себя программу, которая запускается перед запуском основной программы.
 * java: -javaagent:myagent.jar -jar myjar.jar
 * Javaagent реализует пакет java.lang.instrument, который позволяет производить инструментацию загружаемого класса в jrm.
 * Получая экземпляр класса, javaagent регистрирует инструментатор (JavaTransformer jTransformer) в jrm, который будет запускаться каждый раз, когда classloader будет загружать исполняемый класс в java машину
 * */

/**
 *  Key building blocks in ASM:
 *  Thing               | What it does                                  | Typical use
 *
 *  ClassReader         | Reads a .class file into ASM’s model          | Read bytecode
 *  ClassWriter         | Writes a new .class file                      | Output after modification
 *  ClassVisitor        | Visits a class                                | Modify / add fields, methods
 *  MethodVisitor       | Visits a method                               | Change method bodies
 *  FieldVisitor        | Visits a field                                | Change fields
 * */

/**
 * Minimize Direct Opcode Use if Possible
 *
 * Use AdviceAdapter (from org.objectweb.asm.commons) when instrumenting methods.
 * It wraps method logic, makes stack management easier (e.g., auto-handles constructor super() calls).
 *
 *  Use Correct API Versions
 * Always pass Opcodes.ASM9 (or newer) to constructors.
 * Different JVM versions may require slight opcode changes.
 *
 *  Know Method Signatures (Descriptors)
 * Method signatures are compact and weird:
 * Java                             | JVM descriptor
 * void foo()                       | ()V
 * int foo(String s)                | (Ljava/lang/String;)I
 * String foo(int x, float y)       | (IF)Ljava/lang/String;
 *
 * Use helper methods like Type.getMethodDescriptor() to avoid mistakes.
 *
 * Use CheckClassAdapter or CheckClassVisitor
 * These validate your generated classes before you load them.
 * Catches stack errors, bad instructions, wrong types.
 * CheckClassAdapter.verify(new ClassReader(bytes), false, new PrintWriter(System.err));
 *
 * Don’t Forget Stack Size and Local Variables
 * When generating methods manually, always properly calculate:
 * max stack size
 * max locals
 * Or set ClassWriter.COMPUTE_FRAMES | COMPUTE_MAXS flags to auto-compute them.
 *
 * ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS);
 *
 * */

/**
 * The constant pool in Java is super important — it's basically like a table of constants that a .class file needs at runtime.
 *
 * Think of it like this:
 * When you compile Java code (javac), the compiler collects all the constants — like strings ("Hello"), class names (java/lang/Object), method names (toString), numbers (42), etc.
 * These constants are stored in a big array called the constant pool inside the .class file.
 * Bytecode instructions (like ldc, invokestatic, etc.) will reference entries in this table by index.
 *
 *More technically:
 * The constant pool sits near the top of every .class file.
 * It's an array (starts at index 1; index 0 is invalid on purpose).
 * Each entry has a tag (type) + data.
 * Types of entries:
 * Utf8 (strings like method names, class names)
 * Integer, Float, Long, Double (numbers)
 * Class (refers to a Utf8 string containing the class name)
 * String (refers to a Utf8 string)
 * Fieldref, Methodref, InterfaceMethodref (reference other classes and names)
 * NameAndType (combines a name and a type descriptor)
 * */


public class Agent {
    public static void premain(String args, Instrumentation inst) {
        System.out.println("Agent started with args: [" + args + "]");

        /**
         *
         true: The transformer will be invoked both when classes are loaded and when they are retransformed using retransformClasses(Class<?>... classes).
         false: The transformer is only invoked during initial class loading, not during retransformation.
         Retransformation refers to changing the bytecode of already loaded classes without unloading them.
         This can be triggered using the Instrumentation.retransformClasses(Class<?>... classes) method, provided the JVM supports retransformation.
         * */
        boolean canRetransform = false;

        /**
         JVM support required: The JVM must support class retransformation. Use
         * */
//        boolean retransformClassesSupported = inst.isRetransformClassesSupported();

        inst.addTransformer(new StringCreationClassFileTransformer(), canRetransform);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            Counter.printToFile("F:/profiling/string_calls_report.txt");
        }));

        System.out.println("Agent finished");
    }
}

/**  Top JVM Opcodes (for ASM + general JVM understanding)
 *
 * Opcode       	                    Meaning     	                                        When you use it
 *
 * aload	                            Load reference from local variable      	            Load this, method args, objects
 * astore	                            Store reference into local variable	                    Save an object into a local variable
 * iload	                            Load int from local variable	                        Load an int
 * istore	                            Store int into local variable	                        Save an int
 * ldc	                                Push constant from constant pool	                    Push a string, int, float, class constant
 * getstatic	                        Get static field	                                    System.out, constants
 * putstatic	                        Set static field	                                    Update static fields
 * getfield	                            Get instance field	                                    Load this.field
 * putfield	                            Set instance field	                                    Set this.field = ...
 * invokevirtual	                    Call instance method	                                object.toString(), System.out.println()
 * invokespecial	                    Call constructor / private method / super method	    super(), privateMethod()
 * invokestatic	                        Call static method	                                    Math.abs(), utility methods
 * new	                                Create new object	                                    new String(), new ArrayList()
 * dup	                                Duplicate top value on stack	                        Prepare for object initialization (after new)
 * pop	                                Pop top value off the stack	                            Clean up
 * return	                            Return void from method	                                Used for void methods
 * ireturn, areturn, freturn, etc.	    Return value from method	                            Different types: int, reference, float, etc.
 * ifnonnull, ifnull	                Branch if ref is (non)null	                            Null checks
 * ifeq, ifne	                        Branch based on int (== 0 / != 0)	                    Conditionals
 * goto	                                Unconditional jump	                                    Loops, manual control flow
 * */

/** Java-to-ASM Cheat Sheet
 *
 *  Java code	                    ASM Opcodes	                                                        Notes
 *  int a = 10;	                    ldc 10 → istore 1	                                                Push 10, store into local var 1
 *  String s = "Hi";	            ldc "Hi" → astore 1                                                 Push "Hi", store into local var 1
 *  return;	                        return	                                                            Void return
 *  return x; (int)	                iload x → ireturn	                                                Load int, return
 *  return obj;	                    aload x → areturn	                                                Load object, return
 *  System.out.println("Hello");	getstatic System.out → ldc "Hello" → invokevirtual println	        Stack: [System.out, "Hello"] then call println
 *  if (a == 0)	                    iload a → ifeq label	                                            Branch if equal to zero
 *  if (obj == null)	            aload obj → ifnull label	                                        Branch if null
 *  while (true)	                label: ... goto label	                                            Infinite loop
 *  new String()	                new java/lang/String → dup → invokespecial <init>	                dup is critical!
 * */

/**
 *   Important Patterns to Remember
 *
 *   Common Pattern	                Opcodes
 *
 *   Create object	                new + dup + invokespecial
 *   Method call (instance)	        aload (obj) + args + invokevirtual
 *   Method call (static)	        args + invokestatic
 *   Field read/write	            getfield, putfield, getstatic, putstatic
 *   If-null check	                aload + ifnull or ifnonnull
 *   Basic branching	            ifeq, ifne, goto
 * */


