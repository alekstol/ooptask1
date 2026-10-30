package org.example;
import java.util.ArrayList;
import java.util.List;
/** Конь: прыжок «буквой Г», фигуры не блокируют. */
public class Knight extends Piece {
    private static final int[][] D = {
            {1, 2}, {2, 1}, {2, -1}, {1, -2},
            {-1, -2}, {-2, -1}, {-2, 1}, {-1, 2}
    };
    /** @param color цвет @param square клетка */
    public Knight(Color color, Square square) { super(color, square); }
    /** {@inheritDoc} */
    @Override public PieceType getType() { return PieceType.KNIGHT; }
    /** {@inheritDoc} */
    @Override
    public List<Move> pseudoLegalMoves(Board board, Square ep) {
        List<Move> moves = new ArrayList<Move>();
        for (int[] d : D) {
            int r = square.getRow() + d[0];
            int c = square.getCol() + d[1];
            if (r < 0 || r >= Board.SIZE || c < 0 || c >= Board.SIZE) continue;
            Square to = new Square(r, c);
            Piece t = board.get(to);
            if (t == null || t.getColor() != color) moves.add(new Move(square, to));
        }
        return moves;
    }
    /** {@inheritDoc} */
    @Override public Piece copyTo(Square target) { return new Knight(color, target); }
}