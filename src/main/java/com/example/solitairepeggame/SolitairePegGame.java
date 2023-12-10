package com.example.solitairepeggame;

import javafx.application.Application;
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
    private Stage primaryStage;
    private final Text instructionText = new Text("Choose Starting Peg");

    @Override
    public void start(Stage stage) throws IOException {
        primaryStage = stage;
        createGame();
    }

    /**
     * This method is in charge of creating everything related to the game
     * it created a grid for the buttons, as well as a vbox to hold everything in
     * it sets up all 15 buttons and handles the clicks and hovers for all of them
     */
    private void createGame() {
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
                Image occupiedPeg = new Image("file:sprites/occupiedPeg.png");
                ImageView occupiedView = createCircleImageView(occupiedPeg);

                Image hoverPeg = new Image("file:sprites/hoverPeg.png");
                ImageView hoverView = createCircleImageView(hoverPeg);

                Image emptyPeg = new Image("file:sprites/emptyImage.png");
                ImageView emptyView = createCircleImageView(emptyPeg);

                Image selectedPeg = new Image("file:sprites/selectedPeg.png");
                Image highlightPeg = new Image("file:sprites/highlightedPeg.png");

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
                    if (!gameStarted[0]) {
                        peg.setGraphic(emptyView);
                    }
                });
                peg.setOnMouseExited(e -> {
                    if(peg.isOccupied() && !highlightedPeg[0] || !gameStarted[0]) {
                        peg.setGraphic(occupiedView);
                    }
                });

                // for playing of the game
                peg.setOnAction(e -> {
                    handlePegClick(peg, emptyPeg, occupiedPeg, selectedPeg, highlightPeg);
                });
            }

            grid.add(rowWrapper, 0, i);
        }

        vbox.getChildren().add(grid);

        BackgroundFill backgroundFill = new BackgroundFill(Color.BROWN, null, null);
        Background background = new Background(backgroundFill);
        vbox.setBackground(background);

        Scene scene = new Scene(vbox, 800, 600);
        primaryStage.setTitle("Solitaire Peg Game");
        primaryStage.setScene(scene);
        primaryStage.show();
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
     *      if it hasn't been chosen, you make the peg unoccupied
     *      if it has been chosen, the game started
     *          once the game is started a user can highlight a peg, it will only highlight if
     *          it is able to make a jump
     *          you can also unselect the highlighted peg to change your mind
     *          once you jump a peg, you can select another peg to jump and repeat until done
     *
     * @param peg the peg that was clicked
     * @param emptyPeg Image for if the peg is empty
     * @param occupiedPeg Image for if the peg is occupied
     * @param selectedPeg Image for if the peg is selected
     * @param highlightPeg Image for if the peg is highlighted
     */
    private void handlePegClick(Peg peg, Image emptyPeg, Image occupiedPeg, Image selectedPeg, Image highlightPeg) {
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
                    peg.setHighlight(false);
                    peg.setGraphic(createCircleImageView(occupiedPeg));
                    peg.setOccupied(true);

                    // original peg
                    clickedPeg[0].setGraphic(createCircleImageView(emptyPeg));
                    clickedPeg[0].setOccupied(false);

                    // jumped peg
                    jumpPeg(peg, createCircleImageView(emptyPeg));

                    highlightedPeg[0] = false;

                    unhighlightPeg(emptyPeg);
                }
                else if (clickedPeg[0] == peg) { // if the same peg is clicked, unhighlight
                    peg.setGraphic(createCircleImageView(occupiedPeg));
                    highlightedPeg[0] = false;
                    unhighlightPeg(emptyPeg);
                }
            }
            else {
                if (peg.isOccupied()) {
                    if (highlightPossibleMoves(peg, highlightPeg)) {
                        peg.setGraphic(createCircleImageView(selectedPeg));
                    }
                    clickedPeg[0] = peg;
                }
            }
        }
        if (!checkPossibleMoves() && gameStarted[0] && primaryStage.isShowing()) {
            showEnd(false);
        }
    }

    /**
     * This simple method is used after each run through
     * it checks for any left over highlighted pegs and changes them back to normal
     * useful for if there are multiple moves that you can do that were highlighted
     * @param emptyPeg the empty peg view
     */
    private void unhighlightPeg(Image emptyPeg) {
        for (int i = 0; i < 5; i++) {
            for (int k = 0; k < i + 1; k++) {
                if(board[i][k].isHighlight()) {
                    board[i][k].setGraphic(createCircleImageView(emptyPeg));
                    board[i][k].setHighlight(false);
                }
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
            showEnd(true);
        }
    }

    /**
     * This method creates an alert for when the game is over
     *      either by winning or losing (changes the text
     * it prompts the user with buttons that prompt the user to either close the program or
     * restart and play again
     */
    private void showEnd(boolean win) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("");
        alert.setHeaderText("");

        Label contentLabel = new Label("");
        if (win) {
            contentLabel.setText("Congratulations! You won!");
        }
        else {
            contentLabel.setText("No Possible Moves :(");
        }
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
            }
            else if (buttonType == closeButtonType) {
                primaryStage.close();
            }
        });
    }

    /**
     * This method will highlight possible moves for a pressed peg
     * it calls each 6 directions needed and changes the image if they return true
     *
     * @param peg the peg that was pressed
     * @param highlightPeg Image for if the peg is highlighted
     */
    private boolean highlightPossibleMoves(Peg peg, Image highlightPeg) {
        if (checkUpJump(peg)) {
            board[peg.getI() - 2][peg.getK()].setGraphic(createCircleImageView(highlightPeg));
            board[peg.getI() - 2][peg.getK()].setHighlight(true);
            highlightedPeg[0] = true;
        }
        if (checkUpLeftJump(peg)) {
            board[peg.getI() - 2][peg.getK() - 2].setGraphic(createCircleImageView(highlightPeg));
            board[peg.getI() - 2][peg.getK() - 2].setHighlight(true);
            highlightedPeg[0] = true;
        }
        if (checkDownJump(peg)) {
            board[peg.getI() + 2][peg.getK()].setGraphic(createCircleImageView(highlightPeg));
            board[peg.getI() + 2][peg.getK()].setHighlight(true);
            highlightedPeg[0] = true;
        }
        if (checkDownRightJump(peg)) {
            board[peg.getI() + 2][peg.getK() + 2].setGraphic(createCircleImageView(highlightPeg));
            board[peg.getI() + 2][peg.getK() + 2].setHighlight(true);
            highlightedPeg[0] = true;
        }
        if (checkLeftJump(peg)) {
            board[peg.getI()][peg.getK() - 2].setGraphic(createCircleImageView(highlightPeg));
            board[peg.getI()][peg.getK() - 2].setHighlight(true);
            highlightedPeg[0] = true;
        }
        if (checkRightJump(peg)) {
            board[peg.getI()][peg.getK() + 2].setGraphic(createCircleImageView(highlightPeg));
            board[peg.getI()][peg.getK() + 2].setHighlight(true);
            highlightedPeg[0] = true;
        }
        return highlightedPeg[0];
    }

    /**
     * This method checks to see if there is a move that is remaining
     * if there is a remaining move, possibleMove will turn true and be returned
     * if there isn't a remaining move, it will return false
     * @return true if there is a remaining possible move
     */
    private boolean checkPossibleMoves() {
        for (int i = 0; i < 5; i++) {
            for (int k = 0; k < i + 1; k++) {
                if (board[i][k].isOccupied()) {
                    if (checkUpJump(board[i][k]) || checkUpLeftJump(board[i][k])
                    || checkDownJump(board[i][k]) || checkDownRightJump(board[i][k])
                    || checkLeftJump(board[i][k]) || checkRightJump(board[i][k])) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * This method checks to see if the peg selected can jump a peg up and to the right
     * first check is the boundaries
     * second check is if it has a peg to jump
     * third check is if the area that it would jump to is open
     *
     * @param peg the peg that was pressed on
     * @return true if the peg is able to jump the specified peg
     */
    private boolean checkUpJump(Peg peg) {
        if (peg.getI() - 2 >= 0 && peg.getK() < board[peg.getI() - 2].length) {
            if (board[peg.getI() - 1][peg.getK()].isOccupied()){
                if (!board[peg.getI() - 2][peg.getK()].isOccupied()) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * This method checks to see if the peg selected can jump a peg from up and
     * to the left
     * first check is the boundaries
     * second check is if it has a peg to jump
     * third check is if the area that it would jump to is open
     *
     * @param peg the peg that was pressed on
     * @return true if the peg is able to jump the specified peg
     */
    private boolean checkUpLeftJump(Peg peg) {
        if (peg.getI() - 2 >= 0 && peg.getK() - 2 >= 0) {
            if(board[peg.getI() - 1][peg.getK() - 1].isOccupied()) {
                if (!board[peg.getI() - 2][peg.getK() - 2].isOccupied()) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * This method checks to see if the peg selected can jump a peg from down and
     * to the left
     * first check is the boundaries
     * second check is if it has a peg to jump
     * third check is if the area that it would jump to is open
     *
     * @param peg the peg that was pressed on
     * @return true if the peg is able to jump the specified peg
     */
    private boolean checkDownJump(Peg peg) {
        if (peg.getI() + 2 < 5 && peg.getK() < board[peg.getI() + 2].length) {
            if(board[peg.getI() + 1][peg.getK()].isOccupied()) {
                if (!board[peg.getI() + 2][peg.getK()].isOccupied()) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * This method checks to see if the peg selected can jump a peg from down
     * and to the right
     * first check is the boundaries
     * second check is if it has a peg to jump
     * third check is if the area that it would jump to is open
     *
     * @param peg the peg that was pressed on
     * @return true if the peg is able to jump the specified peg
     */
    private boolean checkDownRightJump(Peg peg) {
        if (peg.getI() + 2 < 5 && peg.getK() + 2 < board[peg.getI() + 2].length) {
            if (board[peg.getI() + 1][peg.getK() + 1].isOccupied()) {
                if (!board[peg.getI() + 2][peg.getK() + 2].isOccupied()) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * This method checks to see if the peg selected can jump a peg from the left
     * first check is the boundaries
     * second check is if it has a peg to jump
     * third check is if the area that it would jump to is open
     *
     * @param peg the peg that was pressed on
     * @return true if the peg is able to jump the specified peg
     */
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

    /**
     * This method checks to see if the peg selected can jump a peg from the right
     * first check is the boundaries
     * second check is if it has a peg to jump
     * third check is if the area that it would jump to is open
     *
     * @param peg the peg that was pressed on
     * @return true if the peg is able to jump the specified peg
     */
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
