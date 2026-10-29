package org.example;
import java.util.List;
public sealed abstract class Piece
        permits King, Queen, Rook, Bishop, Knight, Pawn {
    protected final Color color;
    protected final Square square;
    protected Piece(Color color, Square square) {
        this.color  = color;
        this.square = square;
    }
    public Color color()   { return color; }
    public Square square() { return square; }
    public abstract PieceType type();
    public abstract List<Move> pseudoLegalMoves(Board board, Square enPassantTarget);
    public abstract Piece copyTo(Square target);
    public char symbol() {
        char letter = type().letter();
        return color == Color.WHITE ? letter : Character.toLowerCase(letter);
    }
}