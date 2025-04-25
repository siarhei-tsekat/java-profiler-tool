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

            System.out.println("Transforming class [" + className + "]");

            ClassReader reader = new ClassReader(classfileBuffer);
            ClassWriter writer = new ClassWriter(reader, ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
            ClassVisitor classVisitor = new MethodClassVisitor(writer, className);

            reader.accept(classVisitor, ClassReader.EXPAND_FRAMES);

            return writer.toByteArray();
        }
        catch (Exception e) {
            System.out.println("Error: " + e);
            return null;
        }
    }
}
