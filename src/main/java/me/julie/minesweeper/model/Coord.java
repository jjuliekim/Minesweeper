package me.julie.minesweeper.model;

/**
 * represents a spot on the grid
 */
public class Coord {
    private final int col;
    private final int row;
    private final boolean mine;
    private final int num;

    public Coord(int col, int row, boolean mine, int num) {
        this.col = col;
        this.row = row;
        this.mine = mine;
        this.num = num;
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
}
