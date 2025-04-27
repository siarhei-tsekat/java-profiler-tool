package app_agent;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Counter {
    public static final HashMap<String, Integer> stringConstructorCalls = new HashMap<>();

    public static synchronized void increment(String location) {
        stringConstructorCalls.put(location, stringConstructorCalls.getOrDefault(location, 0) + 1);
    }

    public static void printToFile(String path) {
        try (FileWriter writer = new FileWriter(path)) {

            for (Map.Entry<String, Integer> entry : stringConstructorCalls.entrySet()) {
                writer.write(entry.getKey() + " -> " + entry.getValue());
                writer.write(System.lineSeparator());
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
