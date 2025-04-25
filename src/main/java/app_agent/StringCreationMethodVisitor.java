package app_agent;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import static org.objectweb.asm.Opcodes.GETSTATIC;

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
    }

    // used to emit type-specific JVM bytecode instructions—that is, instructions that operate on a class or interface type.
    // opcode: One of NEW, ANEWARRAY, CHECKCAST, or INSTANCEOF
    // desc: The internal name of the class (e.g., java/lang/String), or the type of the array component.

    @Override
    public void visitTypeInsn(int opcode, String desc) {
        switch (opcode) {
            case Opcodes.NEW:                   // Create new object
                visitAllocateBefore(desc);
                mv.visitTypeInsn(opcode, desc);
                visitAllocateAfter(desc);
                break;
            case Opcodes.ANEWARRAY:             // Create new array of reference type
                String arrayDesc = desc.startsWith("[") ? "[" + desc : "L" + desc + ";";
                visitAllocateArrayBefore(arrayDesc);
                mv.visitTypeInsn(opcode, desc);
                visitAllocateArrayAfter(arrayDesc);
                break;
//            case Opcodes.CHECKCAST:           // Cast object to specific reference type
//                System.out.println("Casting to " + desc);
//                break;
//            case Opcodes.INSTANCEOF:          // Check if object is of given type
//                System.out.println("Checking instanceof " + desc);
//                break;
            default:
                mv.visitTypeInsn(opcode, desc);
        }
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
        mv.visitMethodInsn(Opcodes.INVOKESTATIC,
                "app_agent/Counter",
                "increment",
                "(Ljava/lang/String;)V",
                false
        );



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
