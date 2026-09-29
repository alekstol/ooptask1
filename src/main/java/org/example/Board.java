package org.example;
public final class Board {
    public static final int SIZE = 8;

    private final Piece[][] cells;

    public Board() {
        this.cells = new Piece[SIZE][SIZE];
    }

    public Piece get(int row, int col) {
        return cells[row][col];
    }

    public void set(int row, int col, Piece piece) {
        cells[row][col] = piece;
    }
}