package me.julie.minesweeper.controller;

import me.julie.minesweeper.model.Board;

public class GameController {
    private Board board;

    /**
     * controller for minesweeper application
     */
    public GameController() {
        board = initBoard();
    }

    /**
     * initializes minesweeper game
     */
    public void run() {

    }

    /**
     * initializes minesweeper board
     */
    private Board initBoard() {
        Board board = new Board();

        return board;
    }
}