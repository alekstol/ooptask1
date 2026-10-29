package org.example;
import java.util.ArrayList;
import java.util.List;
public class Bishop extends Piece {
    private static final int[][] D = {{1,1},{1,-1},{-1,1},{-1,-1}};
    public Bishop(Color color, Square square) {
        super(color, square);
    }
    @Override
    public PieceType getType() {
        return PieceType.BISHOP;
    }
    @Override
    public List<Move> pseudoLegalMoves(Board board, Square ep) {
        List<Move> moves = new ArrayList<Move>();
        for (int i = 0; i < D.length; i++) {
            int r = square.getRow() + D[i][0];
            int c = square.getCol() + D[i][1];
            while (r >= 0 && r < Board.SIZE && c >= 0 && c < Board.SIZE) {
                Square to = new Square(r, c);
                Piece target = board.get(to);
                if (target == null) {
                    moves.add(new Move(square, to));
                } else {
                    if (target.getColor() != color) {
                        moves.add(new Move(square, to));
                    }
                    break;
                }
                r = r + D[i][0];
                c = c + D[i][1];
            }
        }
        return moves;
    }
    @Override
    public Piece copyTo(Square target) {
        return new Bishop(color, target);
    }
}