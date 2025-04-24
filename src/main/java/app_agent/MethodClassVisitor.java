package app_agent;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class MethodClassVisitor extends ClassVisitor {
    // Opcodes.ASM9  – Java 15+

    public MethodClassVisitor(ClassWriter cv) {
        super(Opcodes.ASM9, cv);
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        MethodVisitor mv = super.visitMethod(access, name, descriptor, signature, exceptions);
        System.out.println("Visiting method: " + name);
        return new StringCreationMethodVisitor(mv);
    }
}
