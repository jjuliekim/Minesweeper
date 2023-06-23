package me.julie.minesweeper.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import me.julie.minesweeper.model.Board;

public class GameController {
    private Board board;
    private int minesLeft;
    @FXML
    private Button newButton;
    @FXML
    private Label minesLeftLabel;
    @FXML
    private GridPane gridPane;

    /**
     * controller for minesweeper application
     */
    public GameController() {
        this.minesLeft = 0;
        this.newButton = new Button();
        this.minesLeftLabel = new Label();
    }

    /**
     * initializes minesweeper game
     */
    public void run() {
        boardDimensions();
    }

    /**
     * Asks for board dimensions
     */
    private void boardDimensions() {
        Dialog<Board> dialog = new Dialog<>();
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
                    alert.setContentText("Please enter valid board dimensions");
                    alert.showAndWait();
                    boardDimensions();
                }
                board = new Board(Integer.parseInt(colsInput.getText()), Integer.parseInt(rowsInput.getText()));
                return board;
            } else {
                return null;
            }
        });
        dialog.showAndWait();
    }

}