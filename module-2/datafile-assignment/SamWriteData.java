/*
 * Name: Sam Dirr
 * Date: August 23, 2026
 * Assignment: Module 2.2
 * Purpose: Generate arrays of five random integers and five random doubles,
 *          then append both arrays to a binary data file.
 */

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Random;

public class SamWriteData {
    private static final String FILE_NAME = "sam datafile.dat";
    private static final int ARRAY_SIZE = 5;

    public static void main(String[] args) {
        Random random = new Random();
        int[] integerValues = new int[ARRAY_SIZE];
        double[] doubleValues = new double[ARRAY_SIZE];

        // Fill both arrays with random values from 0 (inclusive) to 100 (exclusive).
        for (int index = 0; index < ARRAY_SIZE; index++) {
            integerValues[index] = random.nextInt(100);
            doubleValues[index] = random.nextDouble() * 100;
        }

        // The true argument opens the file in append mode and creates it if needed.
        try (DataOutputStream output = new DataOutputStream(
                new BufferedOutputStream(new FileOutputStream(FILE_NAME, true)))) {

            for (int value : integerValues) {
                output.writeInt(value);
            }

            for (double value : doubleValues) {
                output.writeDouble(value);
            }

            System.out.println("The following values were appended to " + FILE_NAME + ":");
            System.out.println("Integers: " + Arrays.toString(integerValues));
            System.out.println("Doubles:  " + Arrays.toString(doubleValues));
        } catch (IOException exception) {
            System.out.println("Unable to write to " + FILE_NAME + ".");
            exception.printStackTrace();
        }
    }
}
