package org.example;
import java.util.ArrayList;
import java.util.List;
public final class King extends Piece {
    private static final int[][] D = {
            {1,0},{1,1},{0,1},{-1,1},{-1,0},{-1,-1},{0,-1},{1,-1}
    };
    public King(Color color, Square square) { super(color, square); }
    @Override public PieceType type() { return PieceType.KING; }
    @Override
    public List<Move> pseudoLegalMoves(Board board, Square ep) {
        var moves = new ArrayList<Move>();
        for (var d : D) {
            int r = square.row() + d[0], c = square.col() + d[1];
            if (r < 0 || r >= Board.SIZE || c < 0 || c >= Board.SIZE) continue;
            var to = new Square(r, c);
            var t = board.get(to);
            if (t == null || t.color() != color) moves.add(new Move(square, to));
        }
        return List.copyOf(moves);
    }
    @Override public Piece copyTo(Square target) { return new King(color, target); }
}