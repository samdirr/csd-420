/*
 * Name: Sam Dirr
 * Date: September 20, 2026
 * Assignment: CSD 420 Module 7 Assignment 7.2
 * Purpose: Display four circles styled by an external JavaFX CSS file.
 */

import java.net.URL;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

public class DirrCircleStyles extends Application {
    /** Builds the same scene used by the application and its tests. */
    public static Scene createScene() {
        HBox root = new HBox(20);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(25));

        for (int index = 0; index < 4; index++) {
            Circle circle = new Circle(45);
            // All circles inherit the white fill and black outline.
            circle.getStyleClass().add("plaincircle");
            if (index == 2) {
                circle.setId("redcircle");
            } else if (index == 3) {
                circle.setId("greencircle");
            }
            root.getChildren().add(circle);
        }

        Scene scene = new Scene(root, 500, 180);
        URL stylesheet = DirrCircleStyles.class.getResource("mystyle.css");
        if (stylesheet == null) {
            throw new IllegalStateException(
                    "Missing mystyle.css: place it beside the compiled classes on the classpath.");
        }
        scene.getStylesheets().add(stylesheet.toExternalForm());
        return scene;
    }

    /** Opens the application window. */
    @Override
    public void start(Stage stage) {
        stage.setTitle("Sam Dirr - CSD 420 Assignment 7.2");
        stage.setScene(createScene());
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
