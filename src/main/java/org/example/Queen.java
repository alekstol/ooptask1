package org.example;
import java.util.ArrayList;
import java.util.List;
public final class Queen extends Piece {
    private static final int[][] DIRECTIONS = {
            { 1,  0}, {-1,  0}, { 0,  1}, { 0, -1},   // как ладья
            { 1,  1}, { 1, -1}, {-1,  1}, {-1, -1}    // как слон
    };
    public Queen(Color color, Square square) { super(color, square); }
    @Override public PieceType type() { return PieceType.QUEEN; }
    @Override
    public List<Move> pseudoLegalMoves(Board board, Square enPassantTarget) {
        var moves = new ArrayList<Move>();
        for (var dir : DIRECTIONS) {
            int r = square.row() + dir[0];
            int c = square.col() + dir[1];
            while (r >= 0 && r < Board.SIZE && c >= 0 && c < Board.SIZE) {
                var to = new Square(r, c);
                var target = board.get(to);
                if (target == null) {
                    moves.add(new Move(square, to));
                } else {
                    if (target.color() != color) moves.add(new Move(square, to));
                    break;
                }
                r += dir[0];
                c += dir[1];
            }
        }
        return List.copyOf(moves);
    }
    @Override public Piece copyTo(Square target) { return new Queen(color, target); }
}