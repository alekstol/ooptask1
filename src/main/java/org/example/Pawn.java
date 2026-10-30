package org.example;
import java.util.ArrayList;
import java.util.List;
/**
 * Пешка: ход на 1 вперёд, двойной со старта, взятие по диагонали,
 * взятие на проходе, превращение (Q/R/B/N).
 */
public class Pawn extends Piece {
    /** @param color цвет @param square клетка */
    public Pawn(Color color, Square square) { super(color, square); }
    /** {@inheritDoc} */
    @Override public PieceType getType() { return PieceType.PAWN; }
    /** {@inheritDoc} */
    @Override
    public List<Move> pseudoLegalMoves(Board board, Square ep) {
        List<Move> moves = new ArrayList<Move>();
        int dir = color.direction();
        int startRow = (color == Color.WHITE) ? 1 : 6;
        int promoRow = (color == Color.WHITE) ? 7 : 0;
        int r = square.getRow();
        int c = square.getCol();
        Square one = new Square(r + dir, c);
        if (board.isEmpty(one)) {
            addPawnMove(moves, one, promoRow);
            if (r == startRow) {
                Square two = new Square(r + 2 * dir, c);
                if (board.isEmpty(two)) moves.add(new Move(square, two));
            }
        }
        for (int dc : new int[]{-1, 1}) {
            int nc = c + dc;
            if (nc < 0 || nc >= Board.SIZE) continue;
            Square to = new Square(r + dir, nc);
            Piece t = board.get(to);
            if (t != null && t.getColor() != color) addPawnMove(moves, to, promoRow);
        }
        if (ep != null && ep.getRow() == r + dir && Math.abs(ep.getCol() - c) == 1) {
            moves.add(new Move(square, ep));
        }
        return moves;
    }
    private void addPawnMove(List<Move> moves, Square to, int promoRow) {
        if (to.getRow() == promoRow) {
            moves.add(new Move(square, to, PieceType.QUEEN));
            moves.add(new Move(square, to, PieceType.ROOK));
            moves.add(new Move(square, to, PieceType.BISHOP));
            moves.add(new Move(square, to, PieceType.KNIGHT));
        } else {
            moves.add(new Move(square, to));
        }
    }
    /** {@inheritDoc} */
    @Override public Piece copyTo(Square target) { return new Pawn(color, target); }
}