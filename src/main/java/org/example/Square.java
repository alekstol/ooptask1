package org.example;
import java.util.Objects;
public class Square {
    private int row;
    private int col;
    public Square(int row, int col) {
        if (row < 0 || row >= Board.SIZE || col < 0 || col >= Board.SIZE) {
            throw new ChessException("Клетка вне доски: " + row + "," + col);
        }
        this.row = row;
        this.col = col;
    }
    public int getRow() {
        return row;
    }
    public int getCol() {
        return col;
    }
    public static Square of(String s) {
        if (s == null || s.length() != 2) {
            throw new ChessException("Некорректная нотация: " + s);
        }
        int col = s.charAt(0) - 'a';
        int row = s.charAt(1) - '1';
        return new Square(row, col);
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Square)) return false;
        Square other = (Square) o;
        return row == other.row && col == other.col;
    }
    @Override
    public int hashCode() {
        return Objects.hash(row, col);
    }
    @Override
    public String toString() {
        char file = (char) ('a' + col);
        char rank = (char) ('1' + row);
        return "" + file + rank;
    }
}