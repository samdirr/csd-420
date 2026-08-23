/*
 * Name: Sam Dirr
 * Date: August 23, 2026
 * Assignment: Module 2.2
 * Purpose: Read and display every set of integers and doubles stored in the
 *          binary data file created by SamWriteData.
 */

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;

public class SamReadData {
    private static final String FILE_NAME = "sam datafile.dat";
    private static final int ARRAY_SIZE = 5;

    public static void main(String[] args) {
        int setNumber = 1;

        try (DataInputStream input = new DataInputStream(
                new BufferedInputStream(new FileInputStream(FILE_NAME)))) {

            while (true) {
                int[] integerValues = new int[ARRAY_SIZE];
                double[] doubleValues = new double[ARRAY_SIZE];

                try {
                    for (int index = 0; index < ARRAY_SIZE; index++) {
                        integerValues[index] = input.readInt();
                    }

                    for (int index = 0; index < ARRAY_SIZE; index++) {
                        doubleValues[index] = input.readDouble();
                    }
                } catch (EOFException exception) {
                    break;
                }

                System.out.println("Data set " + setNumber + ":");
                System.out.println("Integers: " + Arrays.toString(integerValues));
                System.out.println("Doubles:  " + Arrays.toString(doubleValues));
                System.out.println();
                setNumber++;
            }

            if (setNumber == 1) {
                System.out.println(FILE_NAME + " does not contain any complete data sets.");
            }
        } catch (FileNotFoundException exception) {
            System.out.println(FILE_NAME + " was not found. Run SamWriteData first.");
        } catch (IOException exception) {
            System.out.println("Unable to read from " + FILE_NAME + ".");
            exception.printStackTrace();
        }
    }
}
