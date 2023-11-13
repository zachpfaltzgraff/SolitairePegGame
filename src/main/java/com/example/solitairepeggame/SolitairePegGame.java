package com.example.solitairepeggame;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;

public class SolitairePegGame extends Application {

    private final Button[][] board = new Button[5][];

    @Override
    public void start(Stage stage) throws IOException {
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);

        for (int i = 0; i < 5; i++) {
            board[i] = new Button[i + 1];
            HBox rowWrapper = new HBox();
            rowWrapper.setAlignment(Pos.CENTER);
            for (int k = 0; k < i + 1; k++) {
                // base image
                Image begin = new Image("file:sprites/redButton.png");
                ImageView beginView = new ImageView(begin);
                beginView.setFitWidth(100);
                beginView.setFitHeight(100);

                // hover Image
                Image hover = new Image("file:sprites/greenButton.png");
                ImageView hoverImage = new ImageView(hover);
                hoverImage.setFitWidth(100);
                hoverImage.setFitHeight(100);

                Button button = new Button("");
                button.setGraphic(beginView);
                button.setBackground(null);

                rowWrapper.getChildren().add(button);

                board[i][k] = button;

                // for hovering
                button.setOnMouseEntered(e -> {
                    button.setGraphic(hoverImage);
                });
                button.setOnMouseExited(e -> {
                    button.setGraphic(beginView);
                });

                button.setOnAction(e -> {
                    System.out.println("testing");
                });
            }

            grid.add(rowWrapper, 0, i);
        }

        Scene scene = new Scene(grid, 800, 600);
        stage.setTitle("Solitaire Peg Game");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
