package org.example;
import java.util.ArrayList;
import java.util.List;
public final class Pawn extends Piece {
    public Pawn(Color color, Square square) { super(color, square); }
    @Override public PieceType type() { return PieceType.PAWN; }
    @Override
    public List<Move> pseudoLegalMoves(Board board, Square ep) {
        var moves = new ArrayList<Move>();
        int dir      = color.direction();
        int startRow = (color == Color.WHITE) ? 1 : 6;
        int promoRow = (color == Color.WHITE) ? 7 : 0;
        int r = square.row(), c = square.col();

        var one = new Square(r + dir, c);
        if (board.isEmpty(one)) {
            addMove(moves, one, promoRow);
            if (r == startRow) {
                var two = new Square(r + 2 * dir, c);
                if (board.isEmpty(two)) moves.add(new Move(square, two));
            }
        }
        for (int dc : new int[]{-1, 1}) {
            int nc = c + dc;
            if (nc < 0 || nc >= Board.SIZE) continue;
            var to = new Square(r + dir, nc);
            var t = board.get(to);
            if (t != null && t.color() != color) addMove(moves, to, promoRow);
        }
        if (ep != null && ep.row() == r + dir && Math.abs(ep.col() - c) == 1)
            moves.add(new Move(square, ep));
        return List.copyOf(moves);
    }
    private void addMove(List<Move> moves, Square to, int promoRow) {
        if (to.row() == promoRow) {
            for (var t : new PieceType[]{PieceType.QUEEN, PieceType.ROOK,
                    PieceType.BISHOP, PieceType.KNIGHT})
                moves.add(new Move(square, to, t));
        } else moves.add(new Move(square, to));
    }
    @Override public Piece copyTo(Square target) { return new Pawn(color, target); }
}