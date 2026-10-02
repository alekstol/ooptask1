package org.example;
import java.awt.*;
import java.util.List;
import java.util.Objects;
public sealed abstract class Piece
        permits King, Queen, Rook, Bishop, Knight, Pawn {
    protected final Color color;
    protected Square square;
    protected Piece(Color color, Square square) {
        this.color = Objects.requireNonNull(color, "color");
        this.square = Objects.requireNonNull(square, "square");
    }
    public Color color() { return color; }
    public Square square() { return square; }
    public abstract PieceType type();
    public abstract List<Move> pseudoLegalMoves(Board board);
    public char symbol() {
        var letter = type().letter();
        return color == Color.WHITE ? letter : Character.toLowerCase(letter);
    }
    void setSquare(Square newSquare) {
        this.square = Objects.requireNonNull(newSquare, "square");
    }
    public abstract Piece copyTo(Square target);
}
