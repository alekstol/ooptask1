package org.example;
import java.util.ArrayList;
import java.util.List;
public final class Board {
    public static final int SIZE = 8;
    private final Piece[][] cells = new Piece[SIZE][SIZE];
    public Piece get(Square s)          { return cells[s.row()][s.col()]; }
    public void  set(Square s, Piece p) { cells[s.row()][s.col()] = p; }
    public Piece get(int r, int c)      { return cells[r][c]; }
    public void  set(int r, int c, Piece p) { cells[r][c] = p; }
    public boolean isEmpty(Square s)    { return get(s) == null; }
    public Piece kingOf(Color color) {
        for (int r = 0; r < SIZE; r++)
            for (int c = 0; c < SIZE; c++) {
                var p = cells[r][c];
                if (p != null && p.color() == color && p.type() == PieceType.KING) return p;
            }
        throw new ChessException("Нет короля цвета " + color);
    }
    public Board copy() {
        var copy = new Board();
        for (int r = 0; r < SIZE; r++)
            for (int c = 0; c < SIZE; c++) {
                var p = cells[r][c];
                if (p != null) copy.cells[r][c] = p.copyTo(new Square(r, c));
            }
        return copy;
    }
    public List<Piece> piecesOf(Color color) {
        var result = new ArrayList<Piece>(16);
        for (int r = 0; r < SIZE; r++)
            for (int c = 0; c < SIZE; c++) {
                var p = cells[r][c];
                if (p != null && p.color() == color) result.add(p);
            }
        return List.copyOf(result);
    }
    @Override public String toString() {
        var sb = new StringBuilder("  a b c d e f g h\n");
        for (int r = SIZE - 1; r >= 0; r--) {
            sb.append(r + 1).append(' ');
            for (int c = 0; c < SIZE; c++) {
                var p = cells[r][c];
                sb.append(p == null ? '.' : p.symbol()).append(' ');
            }
            sb.append(r + 1).append('\n');
        }
        sb.append("  a b c d e f g h\n");
        return sb.toString();
    }
}