    /*
        TODO:
            possibly have a which turn variable..?
                variable to tell whether you highlight a peg or remove it, not sure how
                    to handle this yet
                could be boolean or integer and use % 2
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

            Text titleText = new Text("Solitaire Peg Game");
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


        /**
         * This method is in charge of making the images into circles
         *
         * @param image the image we want to make a circle
         * @return the circular image
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


        /**
         * This method handles the peg click, it has an if else statement that decides
         * if the game has started or not depending on if the user has chosen a starting peg
         *      if it hasn't been chosen, you make the peg occupied unoccupied
         *      if it has been chosen, is highlights the moves, & TODO finish this
         *
         * @param peg the peg that was clicked
         * @param emptyView ImageView for if the peg is empty
         * @param occupiedView ImageView for if the peg is occupied
         * @param hoverView ImageView for if the peg is being hovered
         */
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

        /**
         * This method will highlight possible moves for a pressed peg
         * it calls each 6 directions needed and changes the image if they return true
         *
         * @param peg the peg that was pressed
         * @param emptyView ImageView for if the peg is empty
         * @param occupiedView ImageView for if the peg is occupied
         * @param hoverView ImageView for if the peg is being hovered
         */
        private void highlightPossibleMoves(Peg peg, ImageView emptyView, ImageView occupiedView, ImageView hoverView) {
            if (checkUpJump(peg)) {
                board[peg.getI() - 2][peg.getK()].setGraphic(hoverView);
            }
            if (checkUpLeftJump(peg)) {
                board[peg.getI() - 2][peg.getK() - 2].setGraphic(hoverView);
            }
            if (checkDownJump(peg)) {
                board[peg.getI() + 2][peg.getK()].setGraphic(hoverView);
            }
            if (checkDownRightJump(peg)) {
                board[peg.getI() + 2][peg.getK() + 2].setGraphic(hoverView);
            }
            if (checkLeftJump(peg)) {
                board[peg.getI()][peg.getK() - 2].setGraphic(hoverView);
            }
            if (checkRightJump(peg)) {
                board[peg.getI()][peg.getK() + 2].setGraphic(hoverView);
            }
        }

        /**
         * These method checks to see if the peg selected can jump a peg from a
         * certain direction
         * first check is the boundaries
         * second check is if it has a peg to jump
         * third check is if the area that it would jump to is open
         *
         * @param peg the peg that was pressed on
         * @return true if the peg is able to jump the specified peg
         */
        private boolean checkUpJump(Peg peg) { // up and to the right
            if (peg.getI() - 2 >= 0 && peg.getK() < board[peg.getI() - 2].length) {
                if (board[peg.getI() - 1][peg.getK()].isOccupied()){
                    if (!board[peg.getI() - 2][peg.getK()].isOccupied()) {
                        return true;
                    }
                }
            }
            return false;
        }
        private boolean checkUpLeftJump(Peg peg) { // up and to the left
            if (peg.getI() - 2 >= 0 && peg.getK() - 2 > board[peg.getI() - 2].length) {
                if(board[peg.getI() - 1][peg.getK() - 1].isOccupied()) {
                    if (!board[peg.getI() - 2][peg.getK() - 2].isOccupied()) {
                        return true;
                    }
                }
            }
            return false;
        }
        private boolean checkDownJump(Peg peg) { // down and to the left
            if (peg.getI() + 2 < 5 && peg.getK() < board[peg.getI() + 2].length) {
                if(board[peg.getI() + 1][peg.getK()].isOccupied()) {
                    if (!board[peg.getI() + 2][peg.getK()].isOccupied()) {
                        return true;
                    }
                }
            }
            return false;
        }
        private boolean checkDownRightJump(Peg peg) { // down and to the right
            if (peg.getI() + 2 < 5 && peg.getK() + 2 < board[peg.getI() + 2].length) {
                if (board[peg.getI() + 1][peg.getK() + 1].isOccupied()) {
                    if (!board[peg.getI() + 2][peg.getK() + 2].isOccupied()) {
                        return true;
                    }
                }
            }
            return false;
        }
        private boolean checkLeftJump(Peg peg) {
            if (peg.getK() - 2 >= 0) {
                if (board[peg.getI()][peg.getK() - 1].isOccupied()) {
                    if (!board[peg.getI()][peg.getK() - 2].isOccupied()) {
                        return true;
                    }
                }
            }
            return false;
        }
        private boolean checkRightJump(Peg peg) {
            if (peg.getK() + 2 < 5) {
                if (board[peg.getI()][peg.getK() + 1].isOccupied()) {
                    if (!board[peg.getI()][peg.getK() + 2].isOccupied()) {
                        return true;
                    }
                }
            }
            return false;
        }

        public static void main(String[] args) {
            launch();
        }
    }
