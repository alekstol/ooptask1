package org.example;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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
    public Piece get(Square s) {
        Objects.requireNonNull(s, "square");
        return cells[s.row()][s.col()];
    }

    public void set(Square s, Piece piece) {
        Objects.requireNonNull(s, "square");
        cells[s.row()][s.col()] = piece;
    }

    public boolean isEmpty(Square s) {
        return get(s) == null;
    }
    public Board copy() {
        var copy = new Board();
        for (var row = 0; row < SIZE; row++) {
            for (var col = 0; col < SIZE; col++) {
                var p = cells[row][col];
                if (p != null) {
                    copy.cells[row][col] = p.copyTo(new Square(row, col));
                }
            }
        }
        return copy;
    }
    public List<Piece> piecesOf(Color color) {
        var result = new ArrayList<Piece>(16);
        for (var row = 0; row < SIZE; row++) {
            for (var col = 0; col < SIZE; col++) {
                var p = cells[row][col];
                if (p != null && p.color() == color) {
                    result.add(p);
                }
            }
        }
        return List.copyOf(result);
    }
    @Override
    public String toString() {
        var sb = new StringBuilder();
        sb.append("  a b c d e f g h\n");
        for (var row = SIZE - 1; row >= 0; row--) {
            sb.append(row + 1).append(' ');
            for (var col = 0; col < SIZE; col++) {
                var p = cells[row][col];
                sb.append(p == null ? '.' : p.symbol()).append(' ');
            }
            sb.append(row + 1).append('\n');
        }
        sb.append("  a b c d e f g h\n");
        return sb.toString();
    }
}