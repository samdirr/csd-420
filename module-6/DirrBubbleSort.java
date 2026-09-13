/*
 * Name: Sam Dirr
 * Date: September 13, 2026
 * Assignment: Module 6.2
 * Purpose: Demonstrate generic bubble-sort methods that use the Comparable
 *          and Comparator interfaces, and test both sorting approaches.
 */

import java.util.Arrays;
import java.util.Comparator;

public class DirrBubbleSort {

    public static void main(String[] args) {
        Integer[] comparableNumbers = {42, 7, 19, 7, 3, 25};
        String[] comparableWords = {"pear", "apple", "orange", "banana"};
        Integer[] descendingNumbers = {12, 4, 20, 4, 9, 1};
        String[] wordsByLength = {"elephant", "cat", "hippo", "ant"};
        Integer[] alreadySorted = {1, 2, 3, 4, 5};
        Integer[] emptyArray = {};

        runComparableTest(
                "Comparable integers",
                comparableNumbers,
                new Integer[]{3, 7, 7, 19, 25, 42});

        runComparableTest(
                "Comparable strings",
                comparableWords,
                new String[]{"apple", "banana", "orange", "pear"});

        runComparatorTest(
                "Comparator integers (descending)",
                descendingNumbers,
                new Integer[]{20, 12, 9, 4, 4, 1},
                Comparator.reverseOrder());

        runComparatorTest(
                "Comparator strings (length, then alphabetically)",
                wordsByLength,
                new String[]{"ant", "cat", "hippo", "elephant"},
                Comparator.comparingInt(String::length)
                        .thenComparing(Comparator.naturalOrder()));

        runComparableTest(
                "Already sorted array",
                alreadySorted,
                new Integer[]{1, 2, 3, 4, 5});

        runComparableTest(
                "Empty array",
                emptyArray,
                new Integer[]{});

        System.out.println("All bubble-sort tests passed.");
    }

    /**
     * Sorts an array into natural ascending order. Each element must implement
     * Comparable so that it can be compared with another element of its type.
     *
     * @param array the array to sort
     * @param <E>   the type of elements in the array
     */
    public static <E extends Comparable<? super E>> void bubbleSort(E[] array) {
        boolean swapped;

        for (int unsortedEnd = array.length - 1; unsortedEnd > 0; unsortedEnd--) {
            swapped = false;

            for (int current = 0; current < unsortedEnd; current++) {
                if (array[current].compareTo(array[current + 1]) > 0) {
                    swap(array, current, current + 1);
                    swapped = true;
                }
            }

            // If a complete pass makes no swaps, the array is already sorted.
            if (!swapped) {
                break;
            }
        }
    }

    /**
     * Sorts an array according to the ordering supplied by a Comparator.
     *
     * @param array      the array to sort
     * @param comparator the object that defines the desired element order
     * @param <E>        the type of elements in the array
     */
    public static <E> void bubbleSort(E[] array,
                                      Comparator<? super E> comparator) {
        boolean swapped;

        for (int unsortedEnd = array.length - 1; unsortedEnd > 0; unsortedEnd--) {
            swapped = false;

            for (int current = 0; current < unsortedEnd; current++) {
                if (comparator.compare(array[current], array[current + 1]) > 0) {
                    swap(array, current, current + 1);
                    swapped = true;
                }
            }

            // If a complete pass makes no swaps, the array is already sorted.
            if (!swapped) {
                break;
            }
        }
    }

    /**
     * Exchanges two elements in an array.
     *
     * @param array       the array containing the elements
     * @param firstIndex  the index of the first element
     * @param secondIndex the index of the second element
     * @param <E>         the type of elements in the array
     */
    private static <E> void swap(E[] array, int firstIndex, int secondIndex) {
        E temporaryValue = array[firstIndex];
        array[firstIndex] = array[secondIndex];
        array[secondIndex] = temporaryValue;
    }

    /**
     * Runs and verifies a test of the Comparable bubble-sort method.
     */
    private static <E extends Comparable<? super E>> void runComparableTest(
            String testName, E[] values, E[] expected) {
        System.out.println(testName + " before: " + Arrays.toString(values));
        bubbleSort(values);
        verifyResult(testName, values, expected);
    }

    /**
     * Runs and verifies a test of the Comparator bubble-sort method.
     */
    private static <E> void runComparatorTest(String testName, E[] values,
                                               E[] expected,
                                               Comparator<? super E> comparator) {
        System.out.println(testName + " before: " + Arrays.toString(values));
        bubbleSort(values, comparator);
        verifyResult(testName, values, expected);
    }

    /**
     * Confirms that a test produced the expected array and reports its result.
     */
    private static <E> void verifyResult(String testName, E[] actual,
                                         E[] expected) {
        System.out.println(testName + " after:  " + Arrays.toString(actual));

        if (!Arrays.equals(actual, expected)) {
            throw new AssertionError(testName + " failed. Expected "
                    + Arrays.toString(expected) + " but received "
                    + Arrays.toString(actual));
        }

        System.out.println(testName + " passed.\n");
    }
}
