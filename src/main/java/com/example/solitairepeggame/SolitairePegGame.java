package com.example.solitairepeggame;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import java.io.IOException;

public class SolitairePegGame extends Application {

    private final Button[][] board = new Button[5][];

    /*
        boardLogic:
            1 = full
            0 = empty
     */
    private final int[][] boardLogic = new int[5][];

    private final boolean[] gameStarted = {false};

    @Override
    public void start(Stage stage) throws IOException {
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);


        for (int i = 0; i < 5; i++) {
            board[i] = new Button[i + 1];
            boardLogic[i] = new int[i+1];

            HBox rowWrapper = new HBox();
            rowWrapper.setAlignment(Pos.CENTER);

            for (int k = 0; k < i + 1; k++) {
                Image occupiedPeg = new Image("file:sprites/redButton.png");
                ImageView occupiedPegView = createCircleImageView(occupiedPeg);

                Image hover = new Image("file:sprites/greenButton.png");
                ImageView hoverImage = createCircleImageView(hover);

                Image emptyPeg = new Image("file:sprites/greyButton.jpeg");
                ImageView emptyPegView = createCircleImageView(emptyPeg);

                Button button = new Button("");
                button.setGraphic(occupiedPegView);
                button.setBackground(null);
                button.setShape(new Circle(50));
                button.setMaxSize(100, 100);

                rowWrapper.getChildren().add(button);

                board[i][k] = button;
                boardLogic[i][k] = 1;

                // for hovering
                button.setOnMouseEntered(e -> {
                    button.setGraphic(hoverImage);
                });
                button.setOnMouseExited(e -> {
                    button.setGraphic(occupiedPegView);
                });

                // for playing of the game
                button.setOnAction(e -> {
                    if (!gameStarted[0]) { // choose starting peg

                        gameStarted[0] = true;
                    }
                    else {





                    }
                });
            }

            grid.add(rowWrapper, 0, i);
        }

        Scene scene = new Scene(grid, 800, 600);
        stage.setTitle("Solitaire Peg Game");
        stage.setScene(scene);
        stage.show();
    }

    /*
        This method takes in the image and then makes it into a circle
     */
    private ImageView createCircleImageView(Image image) {
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(100);
        imageView.setFitHeight(100);

        Circle clip = new Circle(imageView.getFitWidth() / 2,
                imageView.getFitHeight() / 2, imageView.getFitWidth() / 2);
        imageView.setClip(clip);

        return imageView;
    }

    public static void main(String[] args) {
        launch();
    }
}
