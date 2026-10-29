package org.example;
import java.util.ArrayList;
import java.util.List;
public class King extends Piece {
    private static final int[][] D = {
            {1,0},{1,1},{0,1},{-1,1},{-1,0},{-1,-1},{0,-1},{1,-1}
    };
    public King(Color color, Square square) {
        super(color, square);
    }
    @Override
    public PieceType getType() {
        return PieceType.KING;
    }
    @Override
    public List<Move> pseudoLegalMoves(Board board, Square ep) {
        List<Move> moves = new ArrayList<Move>();
        for (int i = 0; i < D.length; i++) {
            int r = square.getRow() + D[i][0];
            int c = square.getCol() + D[i][1];
            if (r < 0 || r >= Board.SIZE || c < 0 || c >= Board.SIZE) {
                continue;
            }
            Square to = new Square(r, c);
            Piece target = board.get(to);
            if (target == null) {
                moves.add(new Move(square, to));
            } else if (target.getColor() != color) {
                moves.add(new Move(square, to));
            }
        }
        return moves;
    }
    @Override
    public Piece copyTo(Square target) {
        return new King(color, target);
    }
}