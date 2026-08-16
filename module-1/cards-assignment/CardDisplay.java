/*
 * Sam Dirr
 * CSD 420 - Advanced Java Programming
 * Module 1.3 Assignment
 * August 16, 2026
 *
 * This JavaFX application randomly displays four unique playing cards.
 * Selecting the Refresh Cards button displays a different set of four cards.
 */

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CardDisplay extends Application {
    private static final int DECK_SIZE = 52;
    private static final int DISPLAY_COUNT = 4;
    private static final double CARD_WIDTH = 150;

    private final HBox cardBox = new HBox(16);
    private List<Integer> displayedCards = List.of();

    @Override
    public void start(Stage stage) {
        cardBox.setAlignment(Pos.CENTER);

        Button refreshButton = new Button("Refresh Cards");

        // Lambda expression refreshes the display when the button is selected.
        refreshButton.setOnAction(event -> displayRandomCards());

        HBox buttonBox = new HBox(refreshButton);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setPadding(new Insets(0, 0, 20, 0));

        BorderPane root = new BorderPane();
        root.setPadding(new Insets(20));
        root.setCenter(cardBox);
        root.setBottom(buttonBox);

        displayRandomCards();

        Scene scene = new Scene(root, 720, 310);
        stage.setTitle("Random Playing Cards");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    /** Selects and displays four unique cards that differ from the current set. */
    private void displayRandomCards() {
        List<Integer> deck = new ArrayList<>();
        for (int cardNumber = 1; cardNumber <= DECK_SIZE; cardNumber++) {
            deck.add(cardNumber);
        }

        // Prevent cards in the current display from appearing immediately again.
        deck.removeAll(displayedCards);
        Collections.shuffle(deck);
        List<Integer> newCards = new ArrayList<>(deck.subList(0, DISPLAY_COUNT));

        displayedCards = newCards;
        cardBox.getChildren().clear();

        for (int cardNumber : displayedCards) {
            File imageFile = new File("cards/" + cardNumber + ".png");
            Image image = new Image(imageFile.toURI().toString());
            ImageView imageView = new ImageView(image);
            imageView.setFitWidth(CARD_WIDTH);
            imageView.setPreserveRatio(true);
            cardBox.getChildren().add(imageView);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
