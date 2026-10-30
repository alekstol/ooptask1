package org.example;
import java.util.ArrayList;
import java.util.List;
/**
 * Король: одна клетка в любом направлении.
 * <p>Рокировка здесь не генерируется — см. {@link MoveGenerator}.
 */
public class King extends Piece {

    private static final int[][] D = {
            {1, 0}, {1, 1}, {0, 1}, {-1, 1},
            {-1, 0}, {-1, -1}, {0, -1}, {1, -1}
    };
    /** @param color цвет @param square клетка */
    public King(Color color, Square square) { super(color, square); }
    /** {@inheritDoc} */
    @Override public PieceType getType() { return PieceType.KING; }
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
    @Override public Piece copyTo(Square target) { return new King(color, target); }
}