    /*
        TODO: fix the highlightPossibleMoves() and the check for in bounds
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
        private final Text instructionText = new Text("Choose Starting Peg");

        @Override
        public void start(Stage stage) throws IOException {
            GridPane grid = new GridPane();
            grid.setAlignment(Pos.CENTER);

            VBox vbox = new VBox();
            vbox.setAlignment(Pos.CENTER);

            Text titleText = new Text("Solitare Peg Game");
            titleText.setFont(Font.font("Open Sans", FontWeight.BOLD, 32));

            instructionText.setFont(Font.font("Open Sans", FontWeight.BOLD, 16));
            vbox.getChildren().add(titleText);
            vbox.getChildren().add(instructionText);

            for (int i = 0; i < 5; i++) {
                board[i] = new Peg[i + 1];

                HBox rowWrapper = new HBox();
                rowWrapper.setAlignment(Pos.CENTER);

                for (int k = 0; k < i + 1; k++) {
                    Image occupiedPeg = new Image("file:sprites/redButton.png");
                    ImageView occupiedView = createCircleImageView(occupiedPeg);

                    Image hover = new Image("file:sprites/greenButton.png");
                    ImageView hoverView = createCircleImageView(hover);

                    Image emptyPeg = new Image("file:sprites/greyButton.jpeg");
                    ImageView emptyView = createCircleImageView(emptyPeg);

                    Peg peg = new Peg(i, k);
                    peg.setGraphic(occupiedView);
                    peg.setBackground(null);
                    peg.setShape(new Circle(50));
                    peg.setMaxSize(100, 100);

                    rowWrapper.getChildren().add(peg);

                    board[i][k] = peg;

                    // for hovering
                    peg.setOnMouseEntered(e -> {
                        if(peg.isOccupied()) {
                            peg.setGraphic(hoverView);
                        }
                    });
                    peg.setOnMouseExited(e -> {
                        if(peg.isOccupied()) {
                            peg.setGraphic(occupiedView);
                        }
                    });

                    // for playing of the game
                    peg.setOnAction(e -> {
                        handlePegClick(peg, emptyView, occupiedView, hoverView);
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

        private void handlePegClick(Peg peg, ImageView emptyView, ImageView occupiedView, ImageView hoverView) {
            if (!gameStarted[0]) {
                instructionText.setText("");
                peg.setGraphic(emptyView);
                peg.setOccupied(false);
                gameStarted[0] = true;
            }
            else { // play game
                if (peg.isOccupied()) {
                    highlightPossibleMoves(peg, emptyView, occupiedView, hoverView);
                }
            }
        }

        private void highlightPossibleMoves(Peg peg, ImageView emptyView, ImageView occupiedView, ImageView hoverView) {
            if (checkUpJump(peg)) {
                board[peg.getI() - 2][peg.getK()].setGraphic(hoverView);
            }
        }

        private boolean checkUpJump(Peg peg) {
            if (peg.getI() + 2 < board.length && peg.getK() < board[peg.getI() + 2].length) { // bounds check
                if (!board[peg.getI() + 2][peg.getK()].isOccupied()) {
                    return true;
                }
            }
            return false;
        }

        public static void main(String[] args) {
            launch();
        }
    }
