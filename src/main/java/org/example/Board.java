package org.example;
import java.util.ArrayList;
import java.util.List;
public class Board {
    public static final int SIZE = 8;
    private Piece[][] cells;
    public Board() {
        cells = new Piece[SIZE][SIZE];
    }
    public Piece get(Square s) {
        return cells[s.getRow()][s.getCol()];
    }
    public void set(Square s, Piece p) {
        cells[s.getRow()][s.getCol()] = p;
    }
    public Piece get(int r, int c) {
        return cells[r][c];
    }
    public void set(int r, int c, Piece p) {
        cells[r][c] = p;
    }
    public boolean isEmpty(Square s) {
        return get(s) == null;
    }
    public Piece kingOf(Color color) {
        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                Piece p = cells[r][c];
                if (p != null && p.getColor() == color && p.getType() == PieceType.KING) {
                    return p;
                }
            }
        }
        throw new ChessException("Нет короля цвета " + color);
    }
    public Board copy() {
        Board copy = new Board();
        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                Piece p = cells[r][c];
                if (p != null) {
                    copy.cells[r][c] = p.copyTo(new Square(r, c));
                }
            }
        }
        return copy;
    }
    public List<Piece> piecesOf(Color color) {
        List<Piece> result = new ArrayList<Piece>();
        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                Piece p = cells[r][c];
                if (p != null && p.getColor() == color) {
                    result.add(p);
                }
            }
        }
        return result;
    }
    public void print() {
        System.out.println("  a b c d e f g h");
        for (int r = SIZE - 1; r >= 0; r--) {
            System.out.print((r + 1) + " ");
            for (int c = 0; c < SIZE; c++) {
                Piece p = cells[r][c];
                if (p == null) {
                    System.out.print(". ");
                } else {
                    System.out.print(p.symbol() + " ");
                }
            }
            System.out.println(r + 1);
        }
        System.out.println("  a b c d e f g h");
    }
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("  a b c d e f g h\n");
        for (int r = SIZE - 1; r >= 0; r--) {
            sb.append(r + 1).append(" ");
            for (int c = 0; c < SIZE; c++) {
                Piece p = cells[r][c];
                if (p == null) {
                    sb.append(". ");
                } else {
                    sb.append(p.symbol()).append(" ");
                }
            }
            sb.append(r + 1).append("\n");
        }
        sb.append("  a b c d e f g h\n");
        return sb.toString();
    }
}