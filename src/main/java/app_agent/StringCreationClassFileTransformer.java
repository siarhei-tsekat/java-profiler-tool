package app_agent;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

public class StringCreationClassFileTransformer implements ClassFileTransformer {

    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {

        if (!className.contains("application/")) {
            return null;
        }

        try {

            System.out.println("Transforming class: " + className + ". ");

            ClassReader reader = new ClassReader(classfileBuffer);
            ClassWriter writer = new ClassWriter(reader, ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
            ClassVisitor classVisitor = new MethodClassVisitor(writer);
//        ClassVisitor classVisitor = new ClassVisitor(Opcodes.ASM9, writer) {
//
//            @Override
//            public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
//                System.out.println("Hello tsekot");
//                super.visit(version, access, name, signature, superName, interfaces);
//            }
//
//            @Override
//            public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
//                System.out.println("?????????");
//                MethodVisitor mv = super.visitMethod(access, name, descriptor, signature, exceptions);
//                System.out.println("!!!!!!!!!!!!!!!!");
//                if ("<init>".equals(name)) {
//                    return new MethodVisitor(Opcodes.ASM9, mv) {
//                        @Override
//                        public void visitCode() {
//                            mv.visitCode();
//                            mv.visitFieldInsn(Opcodes.GETSTATIC, "app_agent/Counter", "stringConstructorCalls", "I");
//                            mv.visitInsn(Opcodes.ICONST_1);
//                            mv.visitInsn(Opcodes.IADD);
//                            mv.visitFieldInsn(Opcodes.PUTSTATIC, "app_agent/Counter", "stringConstructorCalls", "I");
//                        }
//                    };
//                }
//                return mv;
//            }
//        };

            // Modify the class using ASM
            reader.accept(classVisitor, ClassReader.EXPAND_FRAMES);

            // Return modified class bytes
            return writer.toByteArray();
        }
        catch (Exception e) {
            System.out.println("Error: " + e);
            return null;
        }
    }
}
