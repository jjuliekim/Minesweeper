package me.julie.minesweeper.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import me.julie.minesweeper.Main;
import me.julie.minesweeper.model.Coord;

import java.util.Random;

public class GameController {
    private int totalMines;
    private int minesLeft;
    private int cols;
    private int rows;
    Coord[][] mineGrid;
    boolean firstMove;
    @FXML
    private Button newGameButton;
    @FXML
    private Label minesLeftLabel;
    @FXML
    private GridPane displayBoard;
    @FXML
    private VBox mainVbox;
    private final Random random = new Random();

    /**
     * initializes minesweeper game
     */
    @FXML
    public void initialize() {
        newGameButton.setOnAction(e -> handleNewGame());
        totalMines = 0;
        minesLeft = 0;
        firstMove = true;
        mainVbox.setStyle("-fx-background-color: #99ae68");
        newGameButton.setFocusTraversable(false);
        run();
    }

    /**
     * starts the minesweeper game
     */
    public void run() {
        displayBoardDimensions();
        makeGrid();
        placeMines();
        restOfGrid();
    }

    /**
     * starts a new game
     */
    private void handleNewGame() {
        totalMines = 0;
        minesLeft = 0;
        firstMove = true;
        displayBoard.getChildren().clear();
        displayBoard.getColumnConstraints().clear();
        displayBoard.getRowConstraints().clear();
        run();
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
     * place the mines randomly
     */
    public void placeMines() {
        for (int i = 0; i < cols; i++) {
            for (int j = 0; j < rows; j++) {
                int mine = random.nextInt(4);
                if (mine == 0) {
                    mineGrid[i][j].setMine(true);
                    minesLeft++;
                } else {
                    mineGrid[i][j].setMine(false);
                }
            }
        }
        totalMines = minesLeft;
        minesLeftLabel.setText(String.valueOf(minesLeft));
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
                Button button = new Button(" ");
                button.setMinWidth(30);
                button.setMinHeight(30);
                int col = i;
                int row = j;
                button.setOnMouseClicked(e -> {
                    if (e.getButton().equals(MouseButton.PRIMARY)) {
                        handleLeftClick(mineGrid[col][row], button);
                    } else {
                        handleRightClick(mineGrid[col][row], button);
                    }
                });
                button.setStyle("-fx-background-color: #e6f1ce");
                displayBoard.add(button, i, j);
                Coord coord = new Coord(i, j, false, 0, false, false, button);
                mineGrid[i][j] = coord;
            }
        }
        Main.getInstance().getStage().sizeToScene();
    }

    /**
     * Handles mouse left click.
     * Uncovers coord
     *
     * @param coord button coordinate
     */
    private void handleLeftClick(Coord coord, Button button) {
        if (firstMove) {
            Coord left = new Coord(0);
            Coord right = new Coord(0);
            Coord top = new Coord(0);
            Coord bottom = new Coord(0);
            Coord topLeft = new Coord(0);
            Coord topRight = new Coord(0);
            Coord bottomLeft = new Coord(0);
            Coord bottomRight = new Coord(0);

            if (coord.getCol() > 0) {
                left = mineGrid[coord.getCol() - 1][coord.getRow()];
            }
            if (coord.getCol() < cols - 1) {
                right = mineGrid[coord.getCol() + 1][coord.getRow()];
            }
            if (coord.getRow() > 0) {
                top = mineGrid[coord.getCol()][coord.getRow() - 1];
            }
            if (coord.getRow() < rows - 1) {
                bottom = mineGrid[coord.getCol()][coord.getRow() + 1];
            }
            if (coord.getCol() > 0 && coord.getRow() > 0) {
                topLeft = mineGrid[coord.getCol() - 1][coord.getRow() - 1];
            }
            if (coord.getCol() < cols - 1 && coord.getRow() > 0) {
                topRight = mineGrid[coord.getCol() + 1][coord.getRow() - 1];
            }
            if (coord.getCol() > 0 && coord.getRow() < rows - 1) {
                bottomLeft = mineGrid[coord.getCol() - 1][coord.getRow() + 1];
            }
            if (coord.getCol() < cols - 1 && coord.getRow() < rows - 1) {
                bottomRight = mineGrid[coord.getCol() + 1][coord.getRow() + 1];
            }
            int sum = left.getNum() + right.getNum() + top.getNum() + bottom.getNum()
                      + topLeft.getNum() + topRight.getNum() + bottomLeft.getNum() + bottomRight.getNum();

            if (coord.getMine() || sum > 0) {
                coord.setNum(0);
                minesLeft = 0;
                placeMines();
                restOfGrid();
                handleLeftClick(coord, button);
                return;
            }
            firstMove = false;

        }

        if (coord.getUncovered()) { // already uncovered coord
            return;
        }
        if (coord.getMine()) {
            button.setStyle("-fx-background-color: #ffd6d6");
            button.setText("💣");
            lostGame();
            for (int i = 0; i < cols; i++) {
                for (int j = 0; j < rows; j++) {
                    mineGrid[i][j].setUncovered(true);
                }
            }
            return;
        }
        coord.setUncovered(true);
        if (coord.getNum() != 0) {
            button.setText(String.valueOf(coord.getNum()));
        }
        coord.getButton().setStyle("-fx-background-color: #cfdfa8");

        if (coord.getNum() == 0) { // uncover surrounding 0 cells
            switch (random.nextInt(4)) {
                case 0:
                    coord.getButton().setText("🌸");
                    coord.getButton().setStyle("-fx-text-fill: #ea7171; -fx-background-color: #efe4f4");
                    break;
                case 1:
                    coord.getButton().setText("🌻");
                    coord.getButton().setStyle("-fx-text-fill: #d5ac36; -fx-background-color: #efe4f4");
                    break;
                case 2:
                    coord.getButton().setText("🌼");
                    coord.getButton().setStyle("-fx-text-fill: #692f87; -fx-background-color: #efe4f4");
                    break;
                default:
                    coord.getButton().setText("🌷");
                    coord.getButton().setStyle("-fx-text-fill: #a90707; -fx-background-color: #efe4f4");
                    break;
            }
            Coord left;
            Coord right;
            Coord top;
            Coord bottom;
            Coord topLeft;
            Coord topRight;
            Coord bottomLeft;
            Coord bottomRight;

            if (coord.getCol() > 0) {
                left = mineGrid[coord.getCol() - 1][coord.getRow()];
                handleLeftClick(left, left.getButton());
            }
            if (coord.getCol() < cols - 1) {
                right = mineGrid[coord.getCol() + 1][coord.getRow()];
                handleLeftClick(right, right.getButton());
            }
            if (coord.getRow() > 0) {
                top = mineGrid[coord.getCol()][coord.getRow() - 1];
                handleLeftClick(top, top.getButton());
            }
            if (coord.getRow() < rows - 1) {
                bottom = mineGrid[coord.getCol()][coord.getRow() + 1];
                handleLeftClick(bottom, bottom.getButton());
            }
            if (coord.getCol() > 0 && coord.getRow() > 0) {
                topLeft = mineGrid[coord.getCol() - 1][coord.getRow() - 1];
                handleLeftClick(topLeft, topLeft.getButton());
            }
            if (coord.getCol() < cols - 1 && coord.getRow() > 0) {
                topRight = mineGrid[coord.getCol() + 1][coord.getRow() - 1];
                handleLeftClick(topRight, topRight.getButton());
            }
            if (coord.getCol() > 0 && coord.getRow() < rows - 1) {
                bottomLeft = mineGrid[coord.getCol() - 1][coord.getRow() + 1];
                handleLeftClick(bottomLeft, bottomLeft.getButton());
            }
            if (coord.getCol() < cols - 1 && coord.getRow() < rows - 1) {
                bottomRight = mineGrid[coord.getCol() + 1][coord.getRow() + 1];
                handleLeftClick(bottomRight, bottomRight.getButton());
            }
        }
        checkEndGame();
    }

    /**
     * Handles mouse right click.
     * Places flag
     *
     * @param coord button coordinate
     */
    private void handleRightClick(Coord coord, Button button) {
        if (coord.getUncovered()) { // already clicked
            return;
        }
        if (coord.getFlagged()) { // already flagged- remove flag
            button.setText("");
            coord.setFlagged(false);
            minesLeft++;
            minesLeftLabel.setText(String.valueOf(minesLeft));
        } else {
            button.setText("🚩");
            coord.setFlagged(true);
            minesLeft--;
            minesLeftLabel.setText(String.valueOf(minesLeft));
        }

        if (minesLeft == 0) {
            checkEndGame();
        }
    }

    /**
     * sets numbers/empty cells around mines
     */
    public void restOfGrid() {
        for (int i = 0; i < cols; i++) {
            for (int j = 0; j < rows; j++) {
                if (mineGrid[i][j].getMine()) {
                    continue;
                }
                int numOfMines = 0;
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
                        assert right != null;
                        if (right.getMine()) { // right
                            numOfMines++;
                        }
                        assert bottom != null;
                        if (bottom.getMine()) { // bottom
                            numOfMines++;
                        }
                        if (mineGrid[i + 1][j + 1].getMine()) { // bottom-right
                            numOfMines++;
                        }
                    } else if (j == rows - 1) { // bottom left corner
                        assert right != null;
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
                        assert right != null;
                        if (right.getMine()) { // right
                            numOfMines++;
                        }
                        if (top.getMine()) { // top
                            numOfMines++;
                        }
                        assert bottom != null;
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
                        assert bottom != null;
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
                    assert bottom != null;
                    if (bottom.getMine()) { // bottom
                        numOfMines++;
                    }
                    assert right != null;
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
                    assert right != null;
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
                    assert bottom != null;
                    if (bottom.getMine()) { // bottom
                        numOfMines++;
                    }
                    assert right != null;
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
                mineGrid[i][j] = new Coord(i, j, mineGrid[i][j].getMine(), numOfMines,
                        false, false, mineGrid[i][j].getButton());
            }
        }
    }

    /**
     * game over dialog (lost)
     */
    private void lostGame() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Game Over");
        alert.setHeaderText("You hit a mine!");
        alert.setContentText("Click [New] to play again");
        alert.showAndWait();
    }

    /**
     * game over dialog (won)
     */
    private void wonGame() {
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Game Over");
        dialog.setHeaderText("You won!");
        dialog.setContentText("Click [New] to play again");
        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.showAndWait();
        for (int i = 0; i < cols; i++) {
            for (int j = 0; j < rows; j++) {
                mineGrid[i][j].setUncovered(true);
            }
        }
    }

    /**
     * checks if the game is over
     */
    private void checkEndGame() {
        int numUncovered = 0;
        for (int i = 0; i < cols; i++) {
            for (int j = 0; j < rows; j++) {
                if (mineGrid[i][j].getUncovered()) {
                    numUncovered++;
                }
            }
        }
        if (numUncovered == (cols * rows) - totalMines) {
            wonGame();
        }
    }
}