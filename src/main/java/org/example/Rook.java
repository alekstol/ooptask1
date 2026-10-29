package org.example;
import java.util.ArrayList;
import java.util.List;
public final class Rook extends Piece {
    private static final int[][] D = {{1,0},{-1,0},{0,1},{0,-1}};
    public Rook(Color color, Square square) { super(color, square); }
    @Override public PieceType type() { return PieceType.ROOK; }
    @Override
    public List<Move> pseudoLegalMoves(Board board, Square ep) {
        var moves = new ArrayList<Move>();
        for (var d : D) {
            int r = square.row() + d[0], c = square.col() + d[1];
            while (r >= 0 && r < Board.SIZE && c >= 0 && c < Board.SIZE) {
                var to = new Square(r, c);
                var t = board.get(to);
                if (t == null) moves.add(new Move(square, to));
                else { if (t.color() != color) moves.add(new Move(square, to)); break; }
                r += d[0]; c += d[1];
            }
        }
        return List.copyOf(moves);
    }
    @Override public Piece copyTo(Square target) { return new Rook(color, target); }
}