package org.example;
import java.util.ArrayList;
import java.util.List;
public final class Rook extends Piece {
    private boolean hasMoved;
    private static final int[][] DIRECTIONS = {
            { 1,  0}, {-1,  0}, { 0,  1}, { 0, -1}
    };
    public Rook(Color color, Square square) { super(color, square); }
    @Override public PieceType type() { return PieceType.ROOK; }
    @Override
    public List<Move> pseudoLegalMoves(Board board, Square enPassantTarget) {
        var moves = new ArrayList<Move>();
        for (var dir : DIRECTIONS) {
            int r = square.row() + dir[0];
            int c = square.col() + dir[1];
            while (r >= 0 && r < Board.SIZE && c >= 0 && c < Board.SIZE) {
                var to = new Square(r, c);
                var target = board.get(to);
                if (target == null) {
                    moves.add(new Move(square, to));
                } else {
                    if (target.color() != color) moves.add(new Move(square, to));
                    break;
                }
                r += dir[0];
                c += dir[1];
            }
        }
        return List.copyOf(moves);
    }
    public void markMoved() { this.hasMoved = true; }
    public boolean hasMoved() { return hasMoved; }
    @Override
    public Piece copyTo(Square target) {
        var r = new Rook(color, target);
        r.hasMoved = this.hasMoved;
        return r;
    }
}