package org.example;
import java.util.Objects;
/**
 * Клетка доски 8×8.
 *
 * <p>{@code row} — 0 (1-я горизонталь) … 7 (8-я),
 * {@code col} — 0 (файл a) … 7 (файл h).
 * Сравнение — по значению.
 */
public class Square {
    private final int row;
    private final int col;
    /**
     * @param row индекс строки, 0..7
     * @param col индекс столбца, 0..7
     * @throws ChessException если координаты вне диапазона
     */
    public Square(int row, int col) {
        if (row < 0 || row >= Board.SIZE || col < 0 || col >= Board.SIZE) {
            throw new ChessException("Клетка вне доски: " + row + "," + col);
        }
        this.row = row;
        this.col = col;
    }
    /** @return индекс строки */
    public int getRow() { return row; }
    /** @return индекс столбца */
    public int getCol() { return col; }
    /**
     * Парсит нотацию вида {@code "e4"}.
     *
     * @param s строка из двух символов
     * @return клетка
     * @throws ChessException если {@code s == null} или длина не 2
     */
    public static Square of(String s) {
        if (s == null || s.length() != 2) {
            throw new ChessException("Некорректная нотация: " + s);
        }
        return new Square(s.charAt(1) - '1', s.charAt(0) - 'a');
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Square)) return false;
        Square other = (Square) o;
        return row == other.row && col == other.col;
    }
    @Override
    public int hashCode() { return Objects.hash(row, col); }
    @Override
    public String toString() {
        return "" + (char) ('a' + col) + (char) ('1' + row);
    }
}