package app_agent;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

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
        return new StringCreationMethodVisitor(mv, access, name, descriptor, signature, exceptions, className);
    }
}
