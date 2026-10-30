package org.example;
import java.util.ArrayList;
import java.util.List;
/**
 * Доска 8×8: матрица {@code Piece[8][8]}, {@code null} — пусто.
 * Индексы {@code [row][col]}, {@code row=0} — 1-я горизонталь.
 */
public class Board {
    /** Размер доски. */
    public static final int SIZE = 8;
    private final Piece[][] cells;
    /** Создаёт пустую доску. */
    public Board() { cells = new Piece[SIZE][SIZE]; }
    /** @param s клетка @return фигура или {@code null} */
    public Piece get(Square s) { return cells[s.getRow()][s.getCol()]; }
    /** @param s клетка @param p фигура или {@code null} */
    public void set(Square s, Piece p) { cells[s.getRow()][s.getCol()] = p; }
    /** @param r строка @param c столбец @return фигура или {@code null} */
    public Piece get(int r, int c) { return cells[r][c]; }
    /** @param r строка @param c столбец @param p фигура или {@code null} */
    public void set(int r, int c, Piece p) { cells[r][c] = p; }
    /** @param s клетка @return {@code true}, если пусто */
    public boolean isEmpty(Square s) { return get(s) == null; }
    /**
     * @param color цвет
     * @return король
     * @throws ChessException если короля нет
     */
    public Piece kingOf(Color color) {
        for (int r = 0; r < SIZE; r++)
            for (int c = 0; c < SIZE; c++) {
                Piece p = cells[r][c];
                if (p != null && p.getColor() == color && p.getType() == PieceType.KING)
                    return p;
            }
        throw new ChessException("Нет короля цвета " + color);
    }
    /** @return глубокая копия доски */
    public Board copy() {
        Board copy = new Board();
        for (int r = 0; r < SIZE; r++)
            for (int c = 0; c < SIZE; c++) {
                Piece p = cells[r][c];
                if (p != null) copy.cells[r][c] = p.copyTo(new Square(r, c));
            }
        return copy;
    }
    /** @param color цвет @return список фигур цвета */
    public List<Piece> piecesOf(Color color) {
        List<Piece> result = new ArrayList<Piece>();
        for (int r = 0; r < SIZE; r++)
            for (int c = 0; c < SIZE; c++) {
                Piece p = cells[r][c];
                if (p != null && p.getColor() == color) result.add(p);
            }
        return result;
    }
    /** Печать в {@code System.out}. */
    public void print() { System.out.print(toString()); }
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("  a b c d e f g h\n");
        for (int r = SIZE - 1; r >= 0; r--) {
            sb.append(r + 1).append(" ");
            for (int c = 0; c < SIZE; c++) {
                Piece p = cells[r][c];
                sb.append(p == null ? ". " : p.symbol() + " ");
            }
            sb.append(r + 1).append("\n");
        }
        sb.append("  a b c d e f g h\n");
        return sb.toString();
    }
}