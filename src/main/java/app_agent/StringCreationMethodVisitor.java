package app_agent;

import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import static org.objectweb.asm.Opcodes.GETSTATIC;
import static org.objectweb.asm.Opcodes.INVOKESTATIC;

public class StringCreationMethodVisitor extends MethodVisitor {
    private final String methodName;
    private final String descriptor;
    private final String signature;
    private final String className;

    // Opcodes.ASM9  – Java 15+
    public StringCreationMethodVisitor(MethodVisitor mv, int access, String methodName, String descriptor, String signature, String[] exceptions, String className) {
        super(Opcodes.ASM9, mv);
        this.methodName = methodName;
        this.signature = signature;
        this.descriptor = descriptor;
        this.className = className;
    }

    // Used to insert a simple (zero-operand) instruction into a method. Use it to generate bytecode instructions inside a method body.
    // opcode: The opcode of the instruction to be visited (usually a constant from Opcodes class, like Opcodes.RETURN, Opcodes.POP, etc.)
    // Opcodes.RETURN	Return void
    // Opcodes.IRETURN	Return int
    // Opcodes.ARETURN	Return object reference
    // Opcodes.POP	Pop the top stack value
    // Opcodes.DUP	Duplicate top of stack
    // Opcodes.NOP	Do nothing

    @Override
    public void visitInsn(int opcode) {
//        if (opcode >= Opcodes.IRETURN && opcode <= Opcodes.RETURN) {
//            super.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
//            super.visitLdcInsn("Method is returning!");
//            super.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);
//        }
        super.visitInsn(opcode);
    }

    // marks the start of the method's bytecode instructions.
    @Override
    public void visitCode() {
//        super.visitCode();
//        super.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
//        super.visitLdcInsn("Method started " + name);
//        super.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);

        // Inject: StackTraceElement[] stack = Thread.currentThread().getStackTrace();
//        mv.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/Thread", "currentThread", "()Ljava/lang/Thread;", false);
//        mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/Thread", "getStackTrace", "()[Ljava/lang/StackTraceElement;", false);
//        mv.visitVarInsn(Opcodes.ASTORE, 1); // store in local variable 1 (assuming 0 is 'this')
//
//
//        for (int i = 3; i < 6; i++) {
//            Label skip = new Label();
//
//            mv.visitVarInsn(Opcodes.ALOAD, 1);                 // stack
//            mv.visitInsn(Opcodes.ARRAYLENGTH);                 // stack.length
//            mv.visitIntInsn(Opcodes.BIPUSH, i + 1);                // push 4
//            mv.visitJumpInsn(Opcodes.IF_ICMPLT, skip);         // if length < 4 -> skip
//
//
//            mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
//            mv.visitTypeInsn(Opcodes.NEW, "java/lang/StringBuilder");
//            mv.visitInsn(Opcodes.DUP);
//            mv.visitLdcInsn("Caller #" + (i - 2) + ": ");
//            mv.visitMethodInsn(Opcodes.INVOKESPECIAL, "java/lang/StringBuilder", "<init>", "(Ljava/lang/String;)V", false);
//            mv.visitVarInsn(Opcodes.ALOAD, 1);
//            mv.visitIntInsn(Opcodes.BIPUSH, i);
//            mv.visitInsn(Opcodes.AALOAD);
//            mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(Ljava/lang/Object;)Ljava/lang/StringBuilder;", false);
//            mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/StringBuilder", "toString", "()Ljava/lang/String;", false);
//            mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);
//
//            mv.visitLabel(skip);
//        }

        // StackTraceElement[] stack = Thread.currentThread().getStackTrace();
//        mv.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/Thread", "currentThread", "()Ljava/lang/Thread;", false);
//        mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/Thread", "getStackTrace", "()[Ljava/lang/StackTraceElement;", false);
//        mv.visitVarInsn(Opcodes.ASTORE, 1); // local var 1 = stack
//
//        // if (stack.length > 2) -> print current method
//        Label skipCurrent = new Label();
//        mv.visitVarInsn(Opcodes.ALOAD, 1);
//        mv.visitInsn(Opcodes.ARRAYLENGTH);
//        mv.visitIntInsn(Opcodes.BIPUSH, 3);
//        mv.visitJumpInsn(Opcodes.IF_ICMPLT, skipCurrent);
//
//        mv.visitFieldInsn(Opcodes.GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
//        mv.visitTypeInsn(Opcodes.NEW, "java/lang/StringBuilder");
//        mv.visitInsn(Opcodes.DUP);
//        mv.visitLdcInsn("Current method: ");
//        mv.visitMethodInsn(Opcodes.INVOKESPECIAL, "java/lang/StringBuilder", "<init>", "(Ljava/lang/String;)V", false);
//        mv.visitVarInsn(Opcodes.ALOAD, 1);
//        mv.visitIntInsn(Opcodes.BIPUSH, 2);
//        mv.visitInsn(Opcodes.AALOAD);
//        mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(Ljava/lang/Object;)Ljava/lang/StringBuilder;", false);
//        mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/StringBuilder", "toString", "()Ljava/lang/String;", false);
//        mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);
//
//        mv.visitLabel(skipCurrent);
//
//        // if (stack.length > 3) -> print parent caller
//        Label skipCaller = new Label();
//        mv.visitVarInsn(Opcodes.ALOAD, 1);
//        mv.visitInsn(Opcodes.ARRAYLENGTH);
//        mv.visitIntInsn(Opcodes.BIPUSH, 4);
//        mv.visitJumpInsn(Opcodes.IF_ICMPLT, skipCaller);
//
//        mv.visitFieldInsn(Opcodes.GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
//        mv.visitTypeInsn(Opcodes.NEW, "java/lang/StringBuilder");
//        mv.visitInsn(Opcodes.DUP);
//        mv.visitLdcInsn("Called from: ");
//        mv.visitMethodInsn(Opcodes.INVOKESPECIAL, "java/lang/StringBuilder", "<init>", "(Ljava/lang/String;)V", false);
//        mv.visitVarInsn(Opcodes.ALOAD, 1);
//        mv.visitIntInsn(Opcodes.BIPUSH, 3);
//        mv.visitInsn(Opcodes.AALOAD);
//        mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(Ljava/lang/Object;)Ljava/lang/StringBuilder;", false);
//        mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/StringBuilder", "toString", "()Ljava/lang/String;", false);
//        mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);
//
//        mv.visitLabel(skipCaller);
    }

    // used to emit type-specific JVM bytecode instructions—that is, instructions that operate on a class or interface type.
    // opcode: One of NEW, ANEWARRAY, CHECKCAST, or INSTANCEOF
    // desc: The internal name of the class (e.g., java/lang/String), or the type of the array component.

    @Override
    public void visitTypeInsn(int opcode, String desc) {
//        switch (opcode) {
//            case Opcodes.NEW:                   // Create new object
//                visitAllocateBefore(desc);
//                mv.visitTypeInsn(opcode, desc);
//                visitAllocateAfter(desc);
//                break;
//            case Opcodes.ANEWARRAY:             // Create new array of reference type
//                String arrayDesc = desc.startsWith("[") ? "[" + desc : "L" + desc + ";";
//                visitAllocateArrayBefore(arrayDesc);
//                mv.visitTypeInsn(opcode, desc);
//                visitAllocateArrayAfter(arrayDesc);
//                break;
////            case Opcodes.CHECKCAST:           // Cast object to specific reference type
////                System.out.println("Casting to " + desc);
////                break;
////            case Opcodes.INSTANCEOF:          // Check if object is of given type
////                System.out.println("Checking instanceof " + desc);
////                break;
//            default:
//                mv.visitTypeInsn(opcode, desc);
//        }


    }

    @Override
    public void visitMethodInsn(int opcode, String owner, String name, String descriptor, boolean isInterface) {
        super.visitMethodInsn(opcode, owner, name, descriptor, isInterface);
        // Detect: new String(); aka invokespecial java/lang/String.<init>
        if (opcode == Opcodes.INVOKESPECIAL && owner.equals("java/lang/String") && name.equals("<init>")) {
            injectRecordStringConstructor();
        }
    }

    private void injectRecordStringConstructor() {
        // Thread.currentThread().getStackTrace()
        mv.visitMethodInsn(INVOKESTATIC, "java/lang/Thread", "currentThread",
                "()Ljava/lang/Thread;", false);
        mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/Thread", "getStackTrace",
                "()[Ljava/lang/StackTraceElement;", false);
        mv.visitVarInsn(Opcodes.ASTORE, 1); // local var 1 = stack

        Label skip = new Label();

        mv.visitVarInsn(Opcodes.ALOAD, 1);
        mv.visitInsn(Opcodes.ARRAYLENGTH);
        mv.visitIntInsn(Opcodes.BIPUSH, 4);
        mv.visitJumpInsn(Opcodes.IF_ICMPLT, skip);

        mv.visitVarInsn(Opcodes.ALOAD, 1);
        mv.visitIntInsn(Opcodes.BIPUSH, 2);
        mv.visitInsn(Opcodes.AALOAD);
        mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/Object", "toString", "()Ljava/lang/String;", false);
        mv.visitVarInsn(Opcodes.ASTORE, 2);

        mv.visitVarInsn(Opcodes.ALOAD, 1);
        mv.visitIntInsn(Opcodes.BIPUSH, 3);
        mv.visitInsn(Opcodes.AALOAD);
        mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/Object", "toString", "()Ljava/lang/String;", false);
        mv.visitVarInsn(Opcodes.ASTORE, 3);

        mv.visitVarInsn(Opcodes.ALOAD, 2);
        mv.visitVarInsn(Opcodes.ALOAD, 3);
        mv.visitMethodInsn(INVOKESTATIC, "app_agent/CallCounter", "recordStringConstructor",
                "(Ljava/lang/String;Ljava/lang/String;)V", false);

        mv.visitLabel(skip);
    }

    private void visitAllocateAfter(String desc) {

    }

    private void visitAllocateBefore(String desc) {
        mv.visitCode();

        // This will generate bytecode that calls Counter.increment("com.example.Main::myMethod()V").

        // In ASM, you use visitLdcInsn to insert an LDC(Load Constant) instruction into a method.
        // This puts the string "location" onto the stack at that point in the method's bytecode.

        String location = className.replace('/', '.') + "::" + methodName + descriptor; // com.example.Main::<init>()
        mv.visitLdcInsn(location);
        mv.visitMethodInsn(INVOKESTATIC, "app_agent/Counter", "increment", "(Ljava/lang/String;)V", false);



//        mv.visitFieldInsn(GETSTATIC, "app_agent/Counter", "stringConstructorCalls", "I");
//        mv.visitInsn(Opcodes.ICONST_1);
//        mv.visitInsn(Opcodes.IADD);
//        mv.visitFieldInsn(Opcodes.PUTSTATIC, "app_agent/Counter", "stringConstructorCalls", "I");
    }

    private void visitAllocateArrayAfter(String arrayDesc) {

    }

    private void visitAllocateArrayBefore(String desc) {
//        mv.dup();
//        pushAllocationPoint(desc);
//        Type type = Type.getType(desc);
//        Type elementType = type.getElementType();
//        String name = elementType.getSort() == Type.OBJECT || elementType.getSort() == Type.ARRAY ? "object" : elementType.getClassName();
//        mv.visitMethodInsn(Opcodes.INVOKESTATIC, context.getAprofOpsImplementation(), name + "AllocateArraySize", TransformerUtil.INT_INT_VOID, false);
    }
}
