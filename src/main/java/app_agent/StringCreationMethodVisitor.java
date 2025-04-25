package app_agent;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import static org.objectweb.asm.Opcodes.GETSTATIC;
import static org.objectweb.asm.Opcodes.INVOKEVIRTUAL;

public
class StringCreationMethodVisitor extends MethodVisitor {
    String name;

    // Opcodes.ASM9  – Java 15+
    public StringCreationMethodVisitor(
            MethodVisitor mv, int access, String name,
            String descriptor, String signature, String[] exceptions
    ) {
        super(Opcodes.ASM9, mv);
        this.name = name;
    }

    @Override
    public void visitInsn(int opcode) {
        if (opcode >= Opcodes.IRETURN && opcode <= Opcodes.RETURN) {
            super.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
            super.visitLdcInsn("Method is returning!");
            super.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);
        }
        super.visitInsn(opcode);
    }

    @Override
    public void visitCode() {
        super.visitCode();

        System.out.println("Method started!");

        super.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
        super.visitLdcInsn("Method started " + name);
        super.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);
    }

    @Override
    public void visitTypeInsn(int opcode, String desc) {
        switch (opcode) {
            case Opcodes.NEW:
                //System.out.println("NEW instance of " + desc);
                visitAllocateBefore(desc);
                mv.visitTypeInsn(opcode, desc);
                visitAllocateAfter(desc);
                break;
            case Opcodes.ANEWARRAY:
                //System.out.println("Creating an array of desc " + desc);
                String arrayDesc = desc.startsWith("[") ? "[" + desc : "L" + desc + ";";
                visitAllocateArrayBefore(arrayDesc);
                mv.visitTypeInsn(opcode, desc);
                visitAllocateArrayAfter(arrayDesc);
                break;
//            case Opcodes.CHECKCAST:
//                System.out.println("Casting to " + desc);
//                break;
//            case Opcodes.INSTANCEOF:
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
        mv.visitFieldInsn(GETSTATIC, "app_agent/Counter", "stringConstructorCalls", "I");
        mv.visitInsn(Opcodes.ICONST_1);
        mv.visitInsn(Opcodes.IADD);
        mv.visitFieldInsn(Opcodes.PUTSTATIC, "app_agent/Counter", "stringConstructorCalls", "I");
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
