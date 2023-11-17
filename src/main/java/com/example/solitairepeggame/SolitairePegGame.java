/*
    TODO: fix the hovering of when a boardLogic[][] spot is set equal to 0
 */

package com.example.solitairepeggame;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;

public class SolitairePegGame extends Application {

    private final Peg[][] board = new Peg[5][];

    private final boolean[] gameStarted = {false};

    @Override
    public void start(Stage stage) throws IOException {
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);

        VBox vbox = new VBox();
        vbox.setAlignment(Pos.CENTER);

        Text titleText = new Text("Solitare Peg Game");
        titleText.setFont(Font.font("Open Sans", FontWeight.BOLD, 32));

        Text instructionText = new Text("Choose Starting Peg");
        instructionText.setFont(Font.font("Open Sans", FontWeight.BOLD, 16));
        vbox.getChildren().add(titleText);
        vbox.getChildren().add(instructionText);

        for (int i = 0; i < 5; i++) {
            board[i] = new Peg[i + 1];

            HBox rowWrapper = new HBox();
            rowWrapper.setAlignment(Pos.CENTER);

            for (int k = 0; k < i + 1; k++) {
                Image occupiedPeg = new Image("file:sprites/redButton.png");
                ImageView occupiedPegView = createCircleImageView(occupiedPeg);

                Image hover = new Image("file:sprites/greenButton.png");
                ImageView hoverImage = createCircleImageView(hover);

                Image emptyPeg = new Image("file:sprites/greyButton.jpeg");
                ImageView emptyPegView = createCircleImageView(emptyPeg);

                Peg peg = new Peg(i, k);
                peg.setGraphic(occupiedPegView);
                peg.setBackground(null);
                peg.setShape(new Circle(50));
                peg.setMaxSize(100, 100);

                rowWrapper.getChildren().add(peg);

                board[i][k] = peg;

                // for hovering
                peg.setOnMouseEntered(e -> {
                    if(peg.isOccupied()) {
                        peg.setGraphic(hoverImage);
                    }
                });
                peg.setOnMouseExited(e -> {
                    if(peg.isOccupied()) {
                        peg.setGraphic(occupiedPegView);
                    }
                });

                // for playing of the game
                int row = i;
                int col = k;
                peg.setOnAction(e -> {
                    handlePegClick(row, col, emptyPegView, instructionText);
                });
            }

            grid.add(rowWrapper, 0, i);
        }

        vbox.getChildren().add(grid);

        Scene scene = new Scene(vbox, 800, 600);
        stage.setTitle("Solitaire Peg Game");
        stage.setScene(scene);
        stage.show();
    }

    private ImageView createCircleImageView(Image image) {
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(100);
        imageView.setFitHeight(100);

        Circle clip = new Circle(imageView.getFitWidth() / 2,
                imageView.getFitHeight() / 2, imageView.getFitWidth() / 2);
        imageView.setClip(clip);

        return imageView;
    }

    private void handlePegClick(int row, int col, ImageView emptyPegView, Text instructionText) {
        if (!gameStarted[0]) {
            instructionText.setText("");
            board[row][col].setGraphic(emptyPegView);
            board[row][col].setOccupied(false);
            gameStarted[0] = true;
        }
        else { // play game

        }
    }

    public static void main(String[] args) {
        launch();
    }
}
