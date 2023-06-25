package me.julie.minesweeper.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;
import me.julie.minesweeper.Main;
import me.julie.minesweeper.model.Coord;

import java.util.Random;

public class GameController {
    private int minesLeft;
    private int cols;
    private int rows;
    Coord[][] mineGrid;
    @FXML
    private Button newGameButton;
    @FXML
    private Label minesLeftLabel;
    @FXML
    private GridPane displayBoard;
    @FXML
    private Label rightLabel;

    /**
     * initializes minesweeper game
     */
    @FXML
    public void initialize() {
        rightLabel.setText("--");
        newGameButton.setOnAction(e -> handleNewGame());
        minesLeft = 0;
        run();
    }

    /**
     * starts the minesweeper game
     */
    public void run() {
        displayBoardDimensions();
        placeMines();
        makeGrid();
        restOfGrid();
    }

    /**
     * starts a new game
     */
    private void handleNewGame() {
        minesLeft = 0;
        rightLabel.setText("--");
        resetBoard();
    }

    /**
     * Asks for displayBoard dimensions
     */
    private void displayBoardDimensions() {
        Dialog<GridPane> dialog = new Dialog<>();
        dialog.setTitle("Game Setup");
        Label rowsLabel = new Label("Num of rows:");
        Label colsLabel = new Label("Num of cols:");
        TextField rowsInput = new TextField();
        rowsInput.setMaxWidth(30);
        TextField colsInput = new TextField();
        colsInput.setMaxWidth(30);
        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.add(rowsLabel, 0, 0);
        gridPane.add(colsLabel, 0, 1);
        gridPane.add(rowsInput, 1, 0);
        gridPane.add(colsInput, 1, 1);
        VBox vBox = new VBox();
        vBox.getChildren().addAll(new Label("Board dimensions must be at least 8 x 8"), gridPane);
        vBox.setSpacing(10);

        dialog.getDialogPane().setContent(vBox);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.setResultConverter(param -> {
            if (param == ButtonType.OK) {
                if (Integer.parseInt(rowsInput.getText()) < 8 || Integer.parseInt(colsInput.getText()) < 8) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setHeaderText("Invalid input");
                    alert.setContentText("Enter valid minesweeper dimensions");
                    alert.showAndWait();
                    alert.close();
                    displayBoardDimensions();
                }
                mineGrid = new Coord[Integer.parseInt(colsInput.getText())][Integer.parseInt(rowsInput.getText())];
                cols = Integer.parseInt(colsInput.getText());
                rows = Integer.parseInt(rowsInput.getText());
                return new GridPane();
            } else {
                return null;
            }
        });
        dialog.showAndWait();
    }

    /**
     * resets the board display to the default
     */
    public void resetBoard() {
        displayBoard.getChildren().clear();
        displayBoard.getColumnConstraints().clear();
        displayBoard.getRowConstraints().clear();
        run();
    }

    /**
     * place the mines randomly
     */
    public void placeMines() {
        Random random = new Random();
        for (int i = 0; i < cols; i++) {
            for (int j = 0; j < rows; j++) {
                boolean mine = random.nextBoolean();
                mineGrid[i][j] = new Coord(i, j, mine, 0, false, false);
                if (mine) {
                    minesLeft++;
                }
            }
        }
    }

    /**
     * make the minesweeper grid
     */
    public void makeGrid() {
        for (int i = 8; i < cols; i++) {
            ColumnConstraints columnConstraints = new ColumnConstraints();
            columnConstraints.setHgrow(Priority.NEVER);
            columnConstraints.setMinWidth(30);
            displayBoard.getColumnConstraints().add(columnConstraints);
        }

        for (int i = 8; i < rows; i++) {
            RowConstraints rowConstraints = new RowConstraints();
            rowConstraints.setVgrow(Priority.NEVER);
            rowConstraints.setMinHeight(30);
            displayBoard.getRowConstraints().add(rowConstraints);
        }

        for (int i = 0; i < cols; i++) {
            for (int j = 0; j < rows; j++) {
                Coord coord = new Coord(i, j, false, 0, false, false);
                mineGrid[i][j] = coord;
                Button button = new Button(" ");
                button.setMinWidth(30);
                button.setMinHeight(30);
                button.setOnMouseClicked(e -> {
                    if (e.getButton().equals(MouseButton.PRIMARY)) {
                        handleLeftClick(coord, button);
                    } else {
                        handleRightClick(coord, button);
                    }
                });
                displayBoard.add(button, i, j);
            }
        }
        Main.getInstance().getStage().sizeToScene();
        displayBoard.setGridLinesVisible(true);
    }

    /**
     * Handles mouse left click.
     * Uncovers coord
     *
     * @param coord button coordinate
     */
    private void handleLeftClick(Coord coord, Button button) {
        if (coord.getUncovered()) { // already uncovered coord
            return;
        }
        int col = coord.getCol();
        int row = coord.getRow();
        if (mineGrid[col][row].getMine()) {
            lostGame();
        }
        coord.setUncovered(true);
        button.setText(String.valueOf(coord.getNum()));
        // if coord.getNum() is 0, uncover surrounding 0s

        displayBoard.add(button, col, row);
        checkEndGame();
    }

    /**
     * Handles mouse right click.
     * Places flag
     *
     * @param coord button coordinate
     */
    private void handleRightClick(Coord coord, Button button) {
        int col = coord.getCol();
        int row = coord.getRow();
        if (coord.getFlagged()) { // already flagged- remove flag
            button.setText("");
            coord.setFlagged(false);
            coord.setUncovered(false);
            minesLeft++;
            minesLeftLabel.setText(String.valueOf(minesLeft));
        } else {
            button.setText("X");
            coord.setFlagged(true);
            coord.setUncovered(true);
        }

        if (minesLeft == 0) {
            checkEndGame();
        } else {
            minesLeft--;
            minesLeftLabel.setText(String.valueOf(minesLeft));
        }
        displayBoard.add(button, col, row);
    }

    /**
     * sets numbers/empty cells around mines
     */
    public void restOfGrid() {
        for (int i = 0; i < cols; i++) {
            for (int j = 0; j < rows; j++) {
                int numOfMines = 0;
                if (mineGrid[i][j].getMine()) {
                    continue;
                }
                Coord left = null;
                Coord right = null;
                Coord top = null;
                Coord bottom = null;
                if (i != 0) {
                    left = mineGrid[i - 1][j];
                }
                if (i != cols - 1) {
                    right = mineGrid[i + 1][j];
                }
                if (j != 0) {
                    top = mineGrid[i][j - 1];
                }
                if (j != rows - 1) {
                    bottom = mineGrid[i][j + 1];
                }

                if (i == 0) { // first column, no left
                    if (j == 0) { // top left corner
                        if (right.getMine()) { // right
                            numOfMines++;
                        }
                        if (bottom.getMine()) { // bottom
                            numOfMines++;
                        }
                        if (mineGrid[i + 1][j + 1].getMine()) { // bottom-right
                            numOfMines++;
                        }
                    } else if (j == rows - 1) { // bottom left corner
                        if (right.getMine()) { // right
                            numOfMines++;
                        }
                        if (top.getMine()) { // top
                            numOfMines++;
                        }
                        if (mineGrid[i + 1][j - 1].getMine()) { // top-right
                            numOfMines++;
                        }
                    } else {
                        if (right.getMine()) { // right
                            numOfMines++;
                        }
                        if (top.getMine()) { // top
                            numOfMines++;
                        }
                        if (bottom.getMine()) { // bottom
                            numOfMines++;
                        }
                        if (mineGrid[i + 1][j - 1].getMine()) { // top-right
                            numOfMines++;
                        }
                        if (mineGrid[i + 1][j + 1].getMine()) { // bottom-right
                            numOfMines++;
                        }
                    }
                } else if (i == cols - 1) { // last column, no right
                    if (j == 0) { // top right corner
                        if (left.getMine()) { // left
                            numOfMines++;
                        }
                        if (bottom.getMine()) { // bottom
                            numOfMines++;
                        }
                        if (mineGrid[i - 1][j + 1].getMine()) { // bottom-left
                            numOfMines++;
                        }
                    } else if (j == rows - 1) { // bottom right corner
                        if (top.getMine()) { // top
                            numOfMines++;
                        }
                        if (left.getMine()) { // left
                            numOfMines++;
                        }
                        if (mineGrid[i - 1][j - 1].getMine()) { // top-left
                            numOfMines++;
                        }
                    } else {
                        if (left.getMine()) { // left
                            numOfMines++;
                        }
                        if (top.getMine()) { // top
                            numOfMines++;
                        }
                        if (bottom.getMine()) { // bottom
                            numOfMines++;
                        }
                        if (mineGrid[i - 1][j - 1].getMine()) { // top-left
                            numOfMines++;
                        }
                        if (mineGrid[i - 1][j + 1].getMine()) { // bottom-left
                            numOfMines++;
                        }
                    }
                } else if (j == 0) { // first row
                    if (left.getMine()) { // left
                        numOfMines++;
                    }
                    if (bottom.getMine()) { // bottom
                        numOfMines++;
                    }
                    if (right.getMine()) { // right
                        numOfMines++;
                    }
                    if (mineGrid[i - 1][j + 1].getMine()) { // bottom-left
                        numOfMines++;
                    }
                    if (mineGrid[i + 1][j + 1].getMine()) { // bottom-right
                        numOfMines++;
                    }
                } else if (j == rows - 1) { // last row
                    if (left.getMine()) { // left
                        numOfMines++;
                    }
                    if (top.getMine()) { // top
                        numOfMines++;
                    }
                    if (right.getMine()) { // right
                        numOfMines++;
                    }
                    if (mineGrid[i - 1][j - 1].getMine()) { // top-left
                        numOfMines++;
                    }
                    if (mineGrid[i + 1][j - 1].getMine()) { // top-right
                        numOfMines++;
                    }
                } else { // middle coords
                    if (left.getMine()) { // left
                        numOfMines++;
                    }
                    if (bottom.getMine()) { // bottom
                        numOfMines++;
                    }
                    if (right.getMine()) { // right
                        numOfMines++;
                    }
                    if (top.getMine()) { // top
                        numOfMines++;
                    }
                    if (mineGrid[i - 1][j + 1].getMine()) { // bottom-left
                        numOfMines++;
                    }
                    if (mineGrid[i + 1][j + 1].getMine()) { // bottom-right
                        numOfMines++;
                    }
                    if (mineGrid[i - 1][j - 1].getMine()) { // top-left
                        numOfMines++;
                    }
                    if (mineGrid[i + 1][j - 1].getMine()) { // top-right
                        numOfMines++;
                    }
                }
                mineGrid[i][j] = new Coord(i, j, false, numOfMines, false, false);
            }
        }
        minesLeftLabel.setText(String.valueOf(minesLeft));
    }

    /**
     * game over dialog (lost)
     */
    private void lostGame() {
        Dialog<Integer> dialog = new Dialog<>();
        dialog.setTitle("Game Over");
        dialog.getDialogPane().setContent(new Label("You hit a mine!"));
        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.setResultConverter(param -> {
            if (param == ButtonType.OK) {
                return 0;
            } else {
                return null;
            }
        });
        dialog.showAndWait();
        rightLabel.setText("--   Click [New] to play again");
    }

    /**
     * game over dialog (won)
     */
    private void wonGame() {
        Dialog<Integer> dialog = new Dialog<>();
        dialog.setTitle("Game Over");
        dialog.getDialogPane().setContent(new Label("You won!"));
        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.setResultConverter(param -> {
            if (param == ButtonType.OK) {
                return 0;
            } else {
                return null;
            }
        });
        dialog.showAndWait();
        rightLabel.setText("--   Click [New] to play again");
    }

    /**
     * checks if the game is over
     */
    private void checkEndGame() {
        for (int i = 0; i < cols; i++) {
            for (int j = 0; j < rows; j++) {
                if (!mineGrid[i][j].getUncovered()) {
                    return;
                }
            }
        }
        wonGame();
    }
}