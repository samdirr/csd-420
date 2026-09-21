/*
 * Name: Sam Dirr
 * Date: September 20, 2026
 * Assignment: CSD 420 Module 8 Assignment 8.2
 * Purpose: Test random selection, thread output, counts, ordering,
 *          completion, cancellation, and invalid arguments without a GUI.
 */
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class SamThreeThreadsTest {
    public static void main(String[] args) throws Exception {
        testRandomCharacter();
        testOutput();
        testCancellation();
        testInvalidArguments();
        System.out.println("All thread tests passed.");
    }

    private static void testRandomCharacter() {
        for (String alphabet : new String[]{SamCharacterGenerator.LETTERS,
                SamCharacterGenerator.DIGITS, SamCharacterGenerator.SYMBOLS}) {
            for (int index = 0; index < 10_000; index++) {
                check(alphabet.indexOf(SamCharacterGenerator.randomCharacter(alphabet)) >= 0,
                        "Random character outside alphabet");
            }
        }
        check(SamCharacterGenerator.randomCharacter("x") == 'x', "Single-character alphabet");
        System.out.println("PASS: Random selections stay in each alphabet.");
    }

    private static void testOutput() throws Exception {
        StringBuilder text = new StringBuilder();
        Set<String> threads = new HashSet<>();
        SamCharacterGenerator generator = new SamCharacterGenerator(10_000, character -> {
            text.append(character);
            threads.add(Thread.currentThread().getName());
        });
        generator.start();
        try {
            check(generator.awaitCompletion(20_000), "Workers did not finish");
            check(text.length() == 30_000, "Expected 30,000 characters");
            String[] alphabets = {SamCharacterGenerator.LETTERS,
                    SamCharacterGenerator.DIGITS, SamCharacterGenerator.SYMBOLS};
            int[] counts = new int[3];
            for (int index = 0; index < text.length(); index++) {
                int category = index % 3;
                check(alphabets[category].indexOf(text.charAt(index)) >= 0,
                        "Wrong character category at " + index);
                counts[category]++;
            }
            for (int count : counts) {
                check(count == 10_000, "Expected 10,000 per category");
            }
            check(threads.equals(Set.of("Letters", "Digits", "Symbols")), "Expected three worker threads");
            expect(IllegalStateException.class, generator::start);
            System.out.println("PASS: Three workers deliver 30,000 individual, interleaved characters.");
            System.out.println("PASS: Exactly 10,000 letters, 10,000 digits, and 10,000 symbols.");
            System.out.println("Sample: " + text.substring(0, 60));
        } finally {
            generator.cancel();
        }
    }

    private static void testCancellation() throws Exception {
        CountDownLatch entered = new CountDownLatch(1);
        CountDownLatch blocked = new CountDownLatch(1);
        SamCharacterGenerator generator = new SamCharacterGenerator(10_000, character -> {
            entered.countDown();
            try {
                blocked.await();
            } catch (InterruptedException interrupted) {
                Thread.currentThread().interrupt();
            }
        });
        generator.start();
        try {
            check(entered.await(5, TimeUnit.SECONDS), "Worker did not start");
            check(!generator.awaitCompletion(10), "Blocked generator should time out");
            generator.cancel();
            check(generator.awaitCompletion(5_000), "Cancelled workers did not terminate");
            System.out.println("PASS: Timeout and cancellation release all workers.");
        } finally {
            generator.cancel();
            blocked.countDown();
        }
    }

    private static void testInvalidArguments() {
        expect(IllegalArgumentException.class, () -> new SamCharacterGenerator(0, c -> { }));
        expect(IllegalArgumentException.class, () -> new SamCharacterGenerator(-1, c -> { }));
        expect(NullPointerException.class, () -> new SamCharacterGenerator(1, null));
        expect(IllegalArgumentException.class, () -> SamCharacterGenerator.randomCharacter(""));
        expect(NullPointerException.class, () -> SamCharacterGenerator.randomCharacter(null));
        SamCharacterGenerator generator = new SamCharacterGenerator(1, c -> { });
        generator.cancel();
        expect(IllegalStateException.class, () -> generator.awaitCompletion(1));
        expect(IllegalArgumentException.class, () -> generator.awaitCompletion(-1));
        System.out.println("PASS: Invalid inputs and lifecycle calls are rejected.");
    }

    private static void check(boolean condition, String message) {
        if (!condition) throw new AssertionError(message);
    }

    private static void expect(Class<? extends Throwable> expected, CheckedAction action) {
        try {
            action.run();
        } catch (Throwable error) {
            if (expected.isInstance(error)) return;
            throw new AssertionError("Unexpected exception", error);
        }
        throw new AssertionError("Expected " + expected.getSimpleName());
    }

    @FunctionalInterface
    private interface CheckedAction {
        void run() throws Exception;
    }
}
