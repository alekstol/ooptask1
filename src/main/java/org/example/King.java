package org.example;
import java.util.ArrayList;
import java.util.List;
public final class King extends Piece {
    private boolean hasMoved;
    private static final int[][] DELTAS = {
            { 1,  0}, { 1,  1}, { 0,  1}, {-1,  1},
            {-1,  0}, {-1, -1}, { 0, -1}, { 1, -1}
    };
    public King(Color color, Square square) { super(color, square); }
    @Override public PieceType type() { return PieceType.KING; }
    @Override
    public List<Move> pseudoLegalMoves(Board board, Square enPassantTarget) {
        var moves = new ArrayList<Move>();
        for (var d : DELTAS) {
            int r = square.row() + d[0];
            int c = square.col() + d[1];
            if (r < 0 || r >= Board.SIZE || c < 0 || c >= Board.SIZE) continue;
            var to = new Square(r, c);
            var target = board.get(to);
            if (target == null || target.color() != color) {
                moves.add(new Move(square, to));
            }
        }
        return List.copyOf(moves);
    }
    public void markMoved() { this.hasMoved = true; }
    public boolean hasMoved() { return hasMoved; }
    @Override
    public Piece copyTo(Square target) {
        var k = new King(color, target);
        k.hasMoved = this.hasMoved;
        return k;
    }
}