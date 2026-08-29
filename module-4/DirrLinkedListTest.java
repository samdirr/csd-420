/*
 * Name: Sam Dirr
 * Date: August 29, 2026
 * Assignment: Module 4.2
 * Purpose: Compare the time required to traverse LinkedLists containing
 *          50,000 and 500,000 integers by using an Iterator and get(index).
 *
 * Results:
 * With 50,000 integers, Iterator traversal took 5.755 milliseconds and
 * get(index) traversal took 1,383.419 milliseconds, making get(index) about
 * 240 times slower. With 500,000 integers, Iterator traversal took 8.337
 * milliseconds and get(index) traversal took 424,139.560 milliseconds, or
 * approximately 7 minutes and 4 seconds. For the larger list, get(index) was
 * about 50,877 times slower than the Iterator. These times can vary by computer
 * and JVM execution.
 *
 * The Iterator was much faster, especially when the list grew from 50,000 to
 * 500,000 elements. An Iterator moves directly from one node to the next, so
 * its traversal time grows linearly. In contrast, each LinkedList get(index)
 * call must walk from one end of the list toward the requested position.
 * Repeating that operation for every index produces approximately quadratic
 * growth. Increasing the list size by a factor of ten therefore caused a much
 * larger increase in get(index) time than in Iterator time.
 */

import java.util.Iterator;
import java.util.LinkedList;

public class DirrLinkedListTest {

    public static void main(String[] args) {
        testTraversal(50_000);
        testTraversal(500_000);
    }

    /**
     * Creates a LinkedList of the requested size, times both traversal methods,
     * and checks that both methods visit every value correctly.
     *
     * @param listSize the number of sequential integers to store in the list
     */
    public static void testTraversal(int listSize) {
        LinkedList<Integer> numbers = new LinkedList<>();

        for (int i = 0; i < listSize; i++) {
            numbers.add(i);
        }

        if (numbers.size() != listSize) {
            throw new AssertionError("Incorrect list size: " + numbers.size());
        }

        long iteratorStart = System.nanoTime();
        long iteratorSum = traverseWithIterator(numbers);
        long iteratorEnd = System.nanoTime();

        long getStart = System.nanoTime();
        long getSum = traverseWithGet(numbers);
        long getEnd = System.nanoTime();

        long expectedSum = (long) listSize * (listSize - 1) / 2;

        if (iteratorSum != expectedSum || getSum != expectedSum) {
            throw new AssertionError("Traversal produced an incorrect sum.");
        }

        double iteratorMilliseconds = (iteratorEnd - iteratorStart) / 1_000_000.0;
        double getMilliseconds = (getEnd - getStart) / 1_000_000.0;

        System.out.printf("List size: %,d%n", listSize);
        System.out.printf("Iterator traversal: %,.3f milliseconds%n",
                iteratorMilliseconds);
        System.out.printf("get(index) traversal: %,.3f milliseconds%n",
                getMilliseconds);
        System.out.printf("get(index) was approximately %,.2f times slower.%n",
                getMilliseconds / iteratorMilliseconds);
        System.out.printf("Correctness test passed. Sum: %,d%n%n", expectedSum);
    }

    /**
     * Traverses a LinkedList with an Iterator and returns the sum of its values.
     *
     * @param list the LinkedList to traverse
     * @return the sum of all integers in the list
     */
    public static long traverseWithIterator(LinkedList<Integer> list) {
        long sum = 0;
        Iterator<Integer> iterator = list.iterator();

        while (iterator.hasNext()) {
            sum += iterator.next();
        }

        return sum;
    }

    /**
     * Traverses a LinkedList with get(index) and returns the sum of its values.
     *
     * @param list the LinkedList to traverse
     * @return the sum of all integers in the list
     */
    public static long traverseWithGet(LinkedList<Integer> list) {
        long sum = 0;

        for (int i = 0; i < list.size(); i++) {
            sum += list.get(i);
        }

        return sum;
    }
}
