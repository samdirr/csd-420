/*
 * Name: Sam Dirr
 * Date: September 20, 2026
 * Assignment: CSD 420 Module 8 Assignment 8.2
 * Purpose: Generate random characters on three cooperating worker threads.
 */
import java.util.Objects;
import java.util.concurrent.Semaphore;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Consumer;

public final class SamCharacterGenerator {
    public static final String LETTERS = "abcdefghijklmnopqrstuvwxyz";
    public static final String DIGITS = "0123456789";
    public static final String SYMBOLS = "!@#$%&*";
    private final int count;
    private final Consumer<Character> output;
    private final Semaphore[] turns = {new Semaphore(1), new Semaphore(0), new Semaphore(0)};
    private final Thread[] workers = new Thread[3];
    private boolean started;

    /** Creates a generator; output receives one character at a time. */
    public SamCharacterGenerator(int count, Consumer<Character> output) {
        if (count <= 0) {
            throw new IllegalArgumentException("Character count must be positive.");
        }
        this.count = count;
        this.output = Objects.requireNonNull(output, "output");
    }

    /** Selects one random character from the requested nonempty alphabet. */
    public static char randomCharacter(String alphabet) {
        Objects.requireNonNull(alphabet, "alphabet");
        if (alphabet.isEmpty()) {
            throw new IllegalArgumentException("Alphabet must not be empty.");
        }
        return alphabet.charAt(ThreadLocalRandom.current().nextInt(alphabet.length()));
    }

    /** Starts exactly three workers, once per generator. */
    public synchronized void start() {
        if (started) {
            throw new IllegalStateException("This generator has already started.");
        }
        started = true;
        String[] alphabets = {LETTERS, DIGITS, SYMBOLS};
        String[] names = {"Letters", "Digits", "Symbols"};
        for (int index = 0; index < workers.length; index++) {
            final int workerIndex = index;
            workers[index] = new Thread(() -> produce(workerIndex, alphabets[workerIndex]), names[index]);
            workers[index].setDaemon(true);
        }
        for (Thread worker : workers) {
            worker.start();
        }
    }

    /** Alternating turns guarantee that the three character sets stay mixed. */
    private void produce(int index, String alphabet) {
        try {
            for (int generated = 0; generated < count; generated++) {
                turns[index].acquire();
                if (Thread.currentThread().isInterrupted()) {
                    return;
                }
                // Deliver immediately, before generating the next character.
                output.accept(randomCharacter(alphabet));
                turns[(index + 1) % workers.length].release();
            }
        } catch (InterruptedException interrupted) {
            Thread.currentThread().interrupt();
        } catch (RuntimeException | Error failure) {
            cancel();
            throw failure;
        }
    }

    /** Interrupts all workers, including workers waiting for their turn. */
    public synchronized void cancel() {
        for (Thread worker : workers) {
            if (worker != null) {
                worker.interrupt();
            }
        }
    }

    /** Waits up to the given time; call from a test or background thread only. */
    public boolean awaitCompletion(long milliseconds) throws InterruptedException {
        if (milliseconds < 0) {
            throw new IllegalArgumentException("Timeout must not be negative.");
        }
        Thread[] snapshot;
        synchronized (this) {
            if (!started) {
                throw new IllegalStateException("Start the generator first.");
            }
            snapshot = workers.clone();
        }
        long deadline = System.nanoTime() + java.util.concurrent.TimeUnit.MILLISECONDS.toNanos(milliseconds);
        for (Thread worker : snapshot) {
            long remaining = deadline - System.nanoTime();
            if (remaining > 0) {
                java.util.concurrent.TimeUnit.NANOSECONDS.timedJoin(worker, remaining);
            }
            if (worker.isAlive()) {
                return false;
            }
        }
        return true;
    }
}
