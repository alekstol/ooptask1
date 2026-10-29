package org.example;
public record Square(int row, int col) {
    public Square {
        if (row < 0 || row >= Board.SIZE || col < 0 || col >= Board.SIZE)
            throw new ChessException("Клетка вне доски: " + row + "," + col);
    }
    public static Square of(String s) {
        if (s == null || s.length() != 2)
            throw new ChessException("Некорректная нотация: " + s);
        return new Square(s.charAt(1) - '1', s.charAt(0) - 'a');
    }
    @Override public String toString() {
        return "" + (char) ('a' + col) + (char) ('1' + row);
    }
}