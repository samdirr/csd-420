/*
 * Module 11 - Gson array serialization and deserialization
 * Student: Samuel Dirr
 * Course: CSD420 Advanced Java Programming
 *
 * Source: Google Gson contributors. (n.d.).
 * Gson user guide, Array Examples.
 * https://google.github.io/gson/UserGuide.html#array-examples
 *
 * Adaptation: The guide's array example is wrapped in a runnable
 * class with printed results and a round-trip check.
 * This example is separate from the paper.
 *
 * Original Gson project copyright Google; Apache License 2.0.
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Requires JDK 8+ and the JAR files from gson-jars.zip.
 * Run these commands from the folder containing this file
 * and the extracted jars folder.
 *
 * Compile:
 * javac -cp "jars/*" GsonArrayExample.java
 *
 * Run on macOS/Linux:
 * java -cp ".:jars/*" GsonArrayExample
 *
 * Run on Windows:
 * java -cp ".;jars/*" GsonArrayExample
 *
 * Expected output:
 * JSON: [1,2,3,4,5]
 * Restored array: [1, 2, 3, 4, 5]
 * Round trip matches: true
 */

import com.google.gson.Gson;
import java.util.Arrays;

/** Demonstrates the array example from the official Gson user guide. */
public class GsonArrayExample {
    /** Converts a Java array to JSON and restores the array from JSON. */
    public static void main(String[] args) {
        Gson gson = new Gson();
        int[] values = {1, 2, 3, 4, 5};

        // Serialization converts the Java array to JSON text.
        String json = gson.toJson(values);
        System.out.println("JSON: " + json);

        // Deserialization uses int[].class to identify the target type.
        int[] restored = gson.fromJson(json, int[].class);
        System.out.println("Restored array: " + Arrays.toString(restored));
        System.out.println("Round trip matches: " + Arrays.equals(values, restored));
    }
}
