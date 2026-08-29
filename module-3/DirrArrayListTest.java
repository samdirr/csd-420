/*
 * Name: Sam Dirr
 * Date: August 29, 2026
 * Assignment: Module 3.2
 * Purpose: Fill an ArrayList with 50 random integers from 1 to 20 and return
 *          a new ArrayList containing the original values without duplicates.
 */

import java.util.ArrayList;
import java.util.Random;

public class DirrArrayListTest {

    public static void main(String[] args) {
        ArrayList<Integer> originalList = new ArrayList<>();
        Random random = new Random();

        for (int i = 0; i < 50; i++) {
            originalList.add(random.nextInt(20) + 1);
        }

        ArrayList<Integer> uniqueList = removeDuplicates(originalList);

        System.out.println("Original ArrayList:");
        System.out.println(originalList);
        System.out.println("\nArrayList with duplicates removed:");
        System.out.println(uniqueList);
    }

    /**
     * Returns a new ArrayList containing each value from the original list
     * only once, in the order in which the value first appeared.
     *
     * @param list the ArrayList whose duplicate values will be removed
     * @param <E> the type of elements stored in the ArrayList
     * @return a new ArrayList containing no duplicate values
     */
    public static <E> ArrayList<E> removeDuplicates(ArrayList<E> list) {
        ArrayList<E> uniqueList = new ArrayList<>();

        for (E value : list) {
            if (!uniqueList.contains(value)) {
                uniqueList.add(value);
            }
        }

        return uniqueList;
    }
}
