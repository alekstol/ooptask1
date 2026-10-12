package org.example;
import java.util.ArrayList;
import java.util.List;
public final class Pawn extends Piece {
    private boolean hasMoved;
    public Pawn(Color color, Square square) { super(color, square); }
    @Override public PieceType type() { return PieceType.PAWN; }
    @Override
    public List<Move> pseudoLegalMoves(Board board, Square enPassantTarget) {
        var moves = new ArrayList<Move>();
        int dir      = color.direction();
        int startRow = (color == Color.WHITE) ? 1 : 6;
        int promoRow = (color == Color.WHITE) ? 7 : 0;
        int r = square.row();
        int c = square.col();
        var one = new Square(r + dir, c);
        if (board.isEmpty(one)) {
            addPawnMove(moves, one, promoRow);
            // вперёд на 2 с начальной позиции
            if (!hasMoved && r == startRow) {
                var two = new Square(r + 2 * dir, c);
                if (board.isEmpty(two)) moves.add(new Move(square, two));
            }
        }
        for (int dc : new int[]{-1, 1}) {
            int nc = c + dc;
            if (nc < 0 || nc >= Board.SIZE) continue;
            var to = new Square(r + dir, nc);
            var target = board.get(to);
            if (target != null && target.color() != color) {
                addPawnMove(moves, to, promoRow);
            }
        }
        if (enPassantTarget != null
                && enPassantTarget.row() == r + dir
                && Math.abs(enPassantTarget.col() - c) == 1) {
            moves.add(new Move(square, enPassantTarget));
        }
        return List.copyOf(moves);
    }
    private void addPawnMove(List<Move> moves, Square to, int promoRow) {
        if (to.row() == promoRow) {
            moves.add(new Move(square, to, PieceType.QUEEN));
            moves.add(new Move(square, to, PieceType.ROOK));
            moves.add(new Move(square, to, PieceType.BISHOP));
            moves.add(new Move(square, to, PieceType.KNIGHT));
        } else {
            moves.add(new Move(square, to));
        }
    }
    public void markMoved() { this.hasMoved = true; }
    public boolean hasMoved() { return hasMoved; }
    @Override
    public Piece copyTo(Square target) {
        var p = new Pawn(color, target);
        p.hasMoved = this.hasMoved;
        return p;
    }
}