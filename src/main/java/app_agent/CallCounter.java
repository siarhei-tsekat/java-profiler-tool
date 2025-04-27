package app_agent;

import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class CallCounter {

    // Key: method where `new String()` was called
    // Value: metadata containing count + call chain
    private static final ConcurrentHashMap<String, CallInfo> stringConstructorCalls = new ConcurrentHashMap<>();

    public static void recordStringConstructor(String methodLocation, String callerLocation) {
        stringConstructorCalls.compute(methodLocation, (key, info) -> {
            if (info == null) {
                info = new CallInfo();
            }
            info.count.incrementAndGet();
            info.callers.putIfAbsent(callerLocation, new AtomicInteger(0));
            info.callers.get(callerLocation).incrementAndGet();
            return info;
        });
    }

    public static void dump() {
        try (FileWriter writer = new FileWriter("F:/profiling/string_constructor_stats.txt")) {
            writer.write("=== new String() Calls ===\n");
            for (var entry : stringConstructorCalls.entrySet()) {
                String method = entry.getKey();
                CallInfo info = entry.getValue();
                writer.write(method + " : " + info.count + " calls\n");

                for (var callerEntry : info.callers.entrySet()) {
                    writer.write("    <- " + callerEntry.getKey() + " : " + callerEntry.getValue() + "\n");
                }

                writer.write("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static {
        Runtime.getRuntime().addShutdownHook(new Thread(CallCounter::dump));
    }

    private static class CallInfo {
        AtomicInteger count = new AtomicInteger();
        ConcurrentHashMap<String, AtomicInteger> callers = new ConcurrentHashMap<>();
    }
}
