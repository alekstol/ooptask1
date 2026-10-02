package org.example;
import java.util.ArrayList;
import java.util.List;
public final class Knight extends Piece {
    private static final int[][] DELTAS = {
            {1,2},{2,1},{2,-1},{1,-2},{-1,-2},{-2,-1},{-2,1},{-1,2}
    };
    public Knight(Color color, Square square) { super(color, square); }
    @Override public PieceType type() { return PieceType.KNIGHT; }
    @Override
    public List<Move> pseudoLegalMoves(Board board) {
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
    @Override public Piece copyTo(Square target) { return new Knight(color, target); }
}