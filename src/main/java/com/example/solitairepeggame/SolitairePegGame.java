    /*
        TODO:
            possibly having a amtOfJumps() return the remaining amount of jumps
                if returns 0, then end game with remaining pegs since no jumps are possible
                have it return after each "turn" / peg click
            possibly add where if you click the highlighted peg, it unhighlights them
     */

    package com.example.solitairepeggame;

    import javafx.application.Application;
    import javafx.application.Platform;
    import javafx.geometry.Pos;
    import javafx.scene.Scene;
    import javafx.scene.control.Alert;
    import javafx.scene.control.ButtonType;
    import javafx.scene.control.Label;
    import javafx.scene.effect.DropShadow;
    import javafx.scene.image.Image;
    import javafx.scene.image.ImageView;
    import javafx.scene.layout.*;
    import javafx.scene.paint.Color;
    import javafx.scene.shape.Circle;
    import javafx.scene.text.Font;
    import javafx.scene.text.FontWeight;
    import javafx.scene.text.Text;
    import javafx.stage.Stage;

    import java.io.IOException;

    public class SolitairePegGame extends Application {

        private final Peg[][] board = new Peg[5][];

        private final boolean[] gameStarted = {false};
        private final boolean[] highlightedPeg = {false};

        private final Peg[] clickedPeg = new Peg[1];
        private final int[] count = {0};
        private final Text instructionText = new Text("Choose Starting Peg");

        @Override
        public void start(Stage stage) throws IOException {
            createGame();
        }

        /**
         * This method is in charge of creating everything related to the game
         * it created a grid for the buttons, as well as a vbox to hold everything in
         * it sets up all 15 buttons and handles the clicks and hovers for all of them
         */
        private void createGame() {
            Stage stage = new Stage();
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

                    Image hoverPeg = new Image("file:sprites/greenButton.png");
                    ImageView hoverView = createCircleImageView(hoverPeg);

                    Image emptyPeg = new Image("file:sprites/greyButton.jpeg");
                    ImageView emptyView = createCircleImageView(emptyPeg);

                    Peg peg = new Peg(i, k);
                    count[0]++;
                    peg.setGraphic(occupiedView);
                    peg.setBackground(null);
                    peg.setShape(new Circle(50));
                    peg.setMaxSize(100, 100);

                    rowWrapper.getChildren().add(peg);

                    board[i][k] = peg;

                    // for hovering
                    peg.setOnMouseEntered(e -> {
                        if(peg.isOccupied() && !highlightedPeg[0]) {
                            peg.setGraphic(hoverView);
                        }
                    });
                    peg.setOnMouseExited(e -> {
                        if(peg.isOccupied() && !highlightedPeg[0]) {
                            peg.setGraphic(occupiedView);
                        }
                    });

                    // for playing of the game
                    peg.setOnAction(e -> {
                        handlePegClick(peg, emptyPeg, occupiedPeg, hoverPeg);
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
         * This method makes the images into circles
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
         * @param emptyPeg Image for if the peg is empty
         * @param occupiedPeg Image for if the peg is occupied
         * @param hoverPeg Image for if the peg is being hovered
         */
        private void handlePegClick(Peg peg, Image emptyPeg, Image occupiedPeg, Image hoverPeg) {
            if (!gameStarted[0]) {
                instructionText.setText("");
                peg.setGraphic(createCircleImageView(emptyPeg));
                peg.setOccupied(false);
                gameStarted[0] = true;
                count[0]--;
            }
            else { // play game
                if (highlightedPeg[0]) {
                    if (peg.isHighlight()) {
                        // clicked peg
                        peg.setGraphic(createCircleImageView(occupiedPeg));
                        peg.setOccupied(true);

                        // original peg
                        clickedPeg[0].setGraphic(createCircleImageView(emptyPeg));
                        clickedPeg[0].setOccupied(false);

                        // jumped peg
                        jumpPeg(peg, createCircleImageView(emptyPeg));

                        highlightedPeg[0] = false;
                    }
                }
                else {
                    highlightPossibleMoves(peg, emptyPeg, occupiedPeg, hoverPeg);
                    clickedPeg[0] = peg;
                    peg.setGraphic(createCircleImageView(hoverPeg));
                }
            }
        }

        /**
         * This method removes the peg that was jumped and sets its graphic to empty
         * as well as sets the occupied variable to false
         *
         * @param peg the original peg that was clicked
         * @param emptyView the empty image for the pegs graphic
         */
        private void jumpPeg(Peg peg, ImageView emptyView) {
            Peg jumpedPeg = board[(peg.getI() + clickedPeg[0].getI()) / 2][(peg.getK() + clickedPeg[0].getK()) / 2];
            jumpedPeg.setGraphic(emptyView);
            jumpedPeg.setOccupied(false);

            count[0]--;
            if (count[0] == 1) {
                showWin();
            }
        }

        /**
         * This method creates an alert for when the user wins (is left with one peg)
         * and it also has buttons that prompt the user to either close the program or
         * restart and play again
         */
        private void showWin() {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("");
            alert.setHeaderText("");

            Label contentLabel = new Label("Congratulations! You won!");
            Font customFont = Font.font("Fredo", 32);
            contentLabel.setTextFill(Color.WHITE);
            contentLabel.setFont(customFont);
            DropShadow dropShadow = new DropShadow(10, Color.BLACK);
            contentLabel.setEffect(dropShadow);
            alert.setGraphic(contentLabel);

            BackgroundFill backgroundFill = new BackgroundFill(Color.BROWN, null, null);
            Background background = new Background(backgroundFill);
            alert.getDialogPane().setBackground(background);

            alert.getDialogPane().setMinSize(400, 110);
            alert.getDialogPane().setMaxSize(400, 110);

            ButtonType restartButtonType = new ButtonType("Restart");
            ButtonType closeButtonType = new ButtonType("Close");
            alert.getButtonTypes().setAll(restartButtonType, closeButtonType);

            alert.showAndWait().ifPresent(buttonType -> {
                if (buttonType == restartButtonType) {
                    alert.close();

                    // reset variables after each game
                    count[0] = 0;
                    gameStarted[0] = false;
                    instructionText.setText("Choose Starting Peg");
                    createGame();
                } else if (buttonType == closeButtonType) {
                    Platform.exit();
                }
            });
        }

        /**
         * This method will highlight possible moves for a pressed peg
         * it calls each 6 directions needed and changes the image if they return true
         *
         * @param peg the peg that was pressed
         * @param emptyPeg Image for if the peg is empty
         * @param occupiedPeg Image for if the peg is occupied
         * @param hoverPeg Image for if the peg is being hovered
         */
        private void highlightPossibleMoves(Peg peg, Image emptyPeg, Image occupiedPeg, Image hoverPeg) {
            if (checkUpJump(peg)) {
                board[peg.getI() - 2][peg.getK()].setGraphic(createCircleImageView(hoverPeg));
                board[peg.getI() - 2][peg.getK()].setHighlight(true);
                highlightedPeg[0] = true;
            }
            if (checkUpLeftJump(peg)) {
                board[peg.getI() - 2][peg.getK() - 2].setGraphic(createCircleImageView(hoverPeg));
                board[peg.getI() - 2][peg.getK() - 2].setHighlight(true);
                highlightedPeg[0] = true;
            }
            if (checkDownJump(peg)) {
                board[peg.getI() + 2][peg.getK()].setGraphic(createCircleImageView(hoverPeg));
                board[peg.getI() + 2][peg.getK()].setHighlight(true);
                highlightedPeg[0] = true;
            }
            if (checkDownRightJump(peg)) {
                board[peg.getI() + 2][peg.getK() + 2].setGraphic(createCircleImageView(hoverPeg));
                board[peg.getI() + 2][peg.getK() + 2].setHighlight(true);
                highlightedPeg[0] = true;
            }
            if (checkLeftJump(peg)) {
                board[peg.getI()][peg.getK() - 2].setGraphic(createCircleImageView(hoverPeg));
                board[peg.getI()][peg.getK() - 2].setHighlight(true);
                highlightedPeg[0] = true;
            }
            if (checkRightJump(peg)) {
                board[peg.getI()][peg.getK() + 2].setGraphic(createCircleImageView(hoverPeg));
                board[peg.getI()][peg.getK() + 2].setHighlight(true);
                highlightedPeg[0] = true;
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
            if (peg.getI() - 2 >= 0 && peg.getK() - 2 >= 0) {
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
            if (peg.getK() + 2 < board[peg.getI()].length) {
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
