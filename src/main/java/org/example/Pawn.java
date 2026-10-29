package org.example;
import java.util.ArrayList;
import java.util.List;
public class Pawn extends Piece {
    public Pawn(Color color, Square square) {
        super(color, square);
    }
    @Override
    public PieceType getType() {
        return PieceType.PAWN;
    }
    @Override
    public List<Move> pseudoLegalMoves(Board board, Square ep) {
        List<Move> moves = new ArrayList<Move>();
        int dir = color.direction();
        int startRow;
        int promoRow;
        if (color == Color.WHITE) {
            startRow = 1;
            promoRow = 7;
        } else {
            startRow = 6;
            promoRow = 0;
        }
        int r = square.getRow();
        int c = square.getCol();
        Square one = new Square(r + dir, c);
        if (board.isEmpty(one)) {
            addPawnMove(moves, one, promoRow);
            if (r == startRow) {
                Square two = new Square(r + 2 * dir, c);
                if (board.isEmpty(two)) {
                    moves.add(new Move(square, two));
                }
            }
        }
        int[] dc = {-1, 1};
        for (int i = 0; i < dc.length; i++) {
            int nc = c + dc[i];
            if (nc < 0 || nc >= Board.SIZE) {
                continue;
            }
            Square to = new Square(r + dir, nc);
            Piece target = board.get(to);
            if (target != null && target.getColor() != color) {
                addPawnMove(moves, to, promoRow);
            }
        }
        if (ep != null) {
            if (ep.getRow() == r + dir && Math.abs(ep.getCol() - c) == 1) {
                moves.add(new Move(square, ep));
            }
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
    @Override
    public Piece copyTo(Square target) {
        return new Pawn(color, target);
    }
}