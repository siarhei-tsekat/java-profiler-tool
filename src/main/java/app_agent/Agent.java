package app_agent;

import java.lang.instrument.Instrumentation;

public class Agent {
    public static void premain(String args, Instrumentation inst) {
        System.out.println("Agent started with args: [" + args + "].");

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
//        inst.addTransformer(new StringConstructorTransformer(), true);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            Counter.printToFile("F:/profiling/string_calls_report.txt");
        }));

        System.out.println("Agent finished.");
    }
}



