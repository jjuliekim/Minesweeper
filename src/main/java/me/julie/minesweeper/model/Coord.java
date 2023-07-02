package me.julie.minesweeper.model;

import javafx.scene.control.Button;

/**
 * represents a spot on the grid
 */
public class Coord {
    private int col;
    private int row;
    private boolean mine; // is there a mine?
    private int num; // num of mines surrounding coord
    private boolean uncovered; // spot not empty?
    private boolean flagged; // spot already flagged?
    private Button button;

    public Coord(int col, int row, boolean mine, int num, boolean uncovered, boolean flagged, Button button) {
        this.col = col;
        this.row = row;
        this.mine = mine;
        this.num = num;
        this.uncovered = uncovered;
        this.flagged = flagged;
        this.button = button;
    }

    public Coord(int num) {
        this.num = 0;
    }

    public int getCol() {
        return col;
    }

    public int getRow() {
        return row;
    }

    public void setMine(boolean mine) {
        this.mine = mine;
    }

    public boolean getMine() {
        return mine;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public boolean getUncovered() {
        return uncovered;
    }

    public boolean getFlagged() {
        return flagged;
    }

    public void setFlagged(boolean flag) {
        this.flagged = flag;
    }

    public void setUncovered(boolean uncovered) {
        this.uncovered = uncovered;
    }

    public Button getButton() {
        return button;
    }
}
