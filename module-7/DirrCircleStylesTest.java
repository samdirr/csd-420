/*
 * Name: Sam Dirr
 * Date: September 20, 2026
 * Assignment: CSD 420 Module 7 Assignment 7.2
 * Purpose: Verify the circle count and computed external CSS styles.
 */

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

public class DirrCircleStylesTest {
    /** Runs checks on the JavaFX thread and propagates failures to the caller. */
    public static void main(String[] args) throws Exception {
        CountDownLatch finished = new CountDownLatch(1);
        AtomicReference<Throwable> failure = new AtomicReference<>();
        Platform.startup(() -> { });
        Platform.runLater(() -> {
            try {
                runTests();
            } catch (Throwable error) {
                failure.set(error);
            } finally {
                finished.countDown();
            }
        });
        try {
            if (!finished.await(20, TimeUnit.SECONDS)) {
                throw new AssertionError("JavaFX tests timed out.");
            }
            if (failure.get() != null) {
                throw new AssertionError("Circle style tests failed.", failure.get());
            }
            System.out.println("All circle style tests passed.");
        } finally {
            Platform.exit();
        }
    }

    /** Checks computed paints after CSS has actually been applied. */
    private static void runTests() {
        Scene scene = DirrCircleStyles.createScene();
        HBox root = (HBox) scene.getRoot();
        root.applyCss();
        root.layout();
        check(root.getChildren().size() == 4, "Exactly four circles");
        check(scene.getStylesheets().size() == 1, "External stylesheet loaded");
        Color[] expected = {Color.WHITE, Color.WHITE, Color.RED, Color.GREEN};
        for (int index = 0; index < expected.length; index++) {
            check(root.getChildren().get(index) instanceof Circle,
                    "Node " + (index + 1) + " is a circle");
            Circle circle = (Circle) root.getChildren().get(index);
            check(circle.getStyleClass().contains("plaincircle"),
                    "Circle " + (index + 1) + " uses the shared class");
            check(expected[index].equals(circle.getFill()),
                    "Circle " + (index + 1) + " has the expected fill");
            check(Color.BLACK.equals(circle.getStroke()),
                    "Circle " + (index + 1) + " has a black outline");
        }
        check("redcircle".equals(root.getChildren().get(2).getId()), "Red ID assigned");
        check("greencircle".equals(root.getChildren().get(3).getId()), "Green ID assigned");
    }

    /** Throws even when Java assertions are disabled. */
    private static void check(boolean condition, String description) {
        if (!condition) {
            throw new AssertionError(description);
        }
        System.out.println("PASS: " + description);
    }
}
