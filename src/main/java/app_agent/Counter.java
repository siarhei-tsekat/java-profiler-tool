package app_agent;

import java.io.FileWriter;
import java.io.IOException;

public class Counter {
    public static int stringConstructorCalls = 0;

    public static void print() {
        System.out.println("String constructor calls: " + stringConstructorCalls);
    }

    public static void printToFile(String path) {
        try (FileWriter writer = new FileWriter(path)) {
            writer.write(stringConstructorCalls + "\n");
            System.out.println("String constructor calls written to " + path);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
