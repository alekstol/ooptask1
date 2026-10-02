package org.example;
import java.util.Objects;
public record Square(int row, int col) {

    public Square {
        if (row < 0 || row >= Board.SIZE || col < 0 || col >= Board.SIZE) {
            throw new ChessException("Клетка вне доски: row=" + row + ", col=" + col);
        }
    }
    public static Square of(String s) {
        Objects.requireNonNull(s, "notation");
        if (s.length() != 2) {
            throw new ChessException("Некорректная нотация клетки: " + s);
        }
        int col = s.charAt(0) - 'a';
        int row = s.charAt(1) - '1';
        return new Square(row, col);
    }
    @Override
    public String toString() {
        return "" + (char) ('a' + col) + (char) ('1' + row);
    }
}
