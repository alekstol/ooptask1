package org.example;
import java.util.ArrayList;
import java.util.List;
/** Ферзь: 8 направлений, скольжение до препятствия. */
public class Queen extends Piece {
    private static final int[][] D = {
            {1, 0}, {-1, 0}, {0, 1}, {0, -1},
            {1, 1}, {1, -1}, {-1, 1}, {-1, -1}
    };
    /** @param color цвет @param square клетка */
    public Queen(Color color, Square square) { super(color, square); }
    /** {@inheritDoc} */
    @Override public PieceType getType() { return PieceType.QUEEN; }
    /** {@inheritDoc} */
    @Override
    public List<Move> pseudoLegalMoves(Board board, Square ep) {
        List<Move> moves = new ArrayList<Move>();
        for (int[] d : D) {
            int r = square.getRow() + d[0];
            int c = square.getCol() + d[1];
            while (r >= 0 && r < Board.SIZE && c >= 0 && c < Board.SIZE) {
                Square to = new Square(r, c);
                Piece t = board.get(to);
                if (t == null) moves.add(new Move(square, to));
                else {
                    if (t.getColor() != color) moves.add(new Move(square, to));
                    break;
                }
                r += d[0]; c += d[1];
            }
        }
        return moves;
    }
    /** {@inheritDoc} */
    @Override public Piece copyTo(Square target) { return new Queen(color, target); }
}