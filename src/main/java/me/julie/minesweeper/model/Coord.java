package me.julie.minesweeper.model;

/**
 * represents a spot on the grid
 */
public class Coord {
    private final int col;
    private final int row;
    private final boolean mine; // is there a mine?
    private final int num; // num of mines surrounding coord
    private boolean uncovered; // spot not empty?
    private boolean flagged; // spot already flagged?

    public Coord(int col, int row, boolean mine, int num, boolean uncovered, boolean flagged) {
        this.col = col;
        this.row = row;
        this.mine = mine;
        this.num = num;
        this.uncovered = uncovered;
        this.flagged = flagged;
    }

    public int getCol() {
        return col;
    }

    public int getRow() {
        return row;
    }

    public boolean getMine() {
        return mine;
    }

    public int getNum() {
        return num;
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
}
