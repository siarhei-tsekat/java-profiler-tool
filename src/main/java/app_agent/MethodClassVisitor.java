package app_agent;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.AdviceAdapter;

public class MethodClassVisitor extends ClassVisitor {

    private final String className;

    // Opcodes.ASM9  – Java 15+
    public MethodClassVisitor(ClassWriter cv, String className) {
        super(Opcodes.ASM9, cv);
        this.className = className;
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        super.visit(version, access, name, signature, superName, interfaces);
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        MethodVisitor mv = super.visitMethod(access, name, descriptor, signature, exceptions);

        return new AdviceAdapter(Opcodes.ASM9, mv, access, name, descriptor) {
            @Override
            public void visitMethodInsn(int opcode, String owner, String name, String descriptor, boolean isInterface) {
                super.visitMethodInsn(opcode, owner, name, descriptor, isInterface);
                if (opcode == INVOKESPECIAL && owner.equals("java/lang/String") && name.equals("<init>")) {
                    injectCounter();
                }
            }

            private void injectCounter() {
                // StackTraceElement[] stack = Thread.currentThread().getStackTrace();
                visitMethodInsn(INVOKESTATIC, "java/lang/Thread", "currentThread", "()Ljava/lang/Thread;", false);
                visitMethodInsn(INVOKEVIRTUAL, "java/lang/Thread", "getStackTrace", "()[Ljava/lang/StackTraceElement;", false);

                int stackVar = newLocal(Type.getType("[Ljava/lang/StackTraceElement;"));
                visitVarInsn(ASTORE, stackVar);

                Label skip = new Label();

                // if (stack.length < 4) return;
                visitVarInsn(ALOAD, stackVar);
                visitInsn(ARRAYLENGTH);
                visitIntInsn(BIPUSH, 4);
                visitJumpInsn(IF_ICMPLT, skip);

                // String current = stack[2].toString();
                visitVarInsn(ALOAD, stackVar);
                visitIntInsn(BIPUSH, 2);
                visitInsn(AALOAD);
                visitMethodInsn(INVOKEVIRTUAL, "java/lang/Object", "toString", "()Ljava/lang/String;", false);
                int currentMethodVar = newLocal(Type.getType(String.class));
                visitVarInsn(ASTORE, currentMethodVar);

                // String caller = stack[3].toString();
                visitVarInsn(ALOAD, stackVar);
                visitIntInsn(BIPUSH, 3);
                visitInsn(AALOAD);
                visitMethodInsn(INVOKEVIRTUAL, "java/lang/Object", "toString", "()Ljava/lang/String;", false);
                int callerVar = newLocal(Type.getType(String.class));
                visitVarInsn(ASTORE, callerVar);

                // CallCounter.recordStringConstructor(current, caller);
                visitVarInsn(ALOAD, currentMethodVar);
                visitVarInsn(ALOAD, callerVar);
                visitMethodInsn(INVOKESTATIC, "app_agent/CallCounter", "recordStringConstructor", "(Ljava/lang/String;Ljava/lang/String;)V", false);

                visitLabel(skip);
            }
        };
//        return new StringCreationMethodVisitor(mv, access, name, descriptor, signature, exceptions, className);
    }
}
