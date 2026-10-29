package org.example;
import java.util.List;
public abstract class Piece {
    protected Color color;
    protected Square square;
    public Piece(Color color, Square square) {
        this.color = color;
        this.square = square;
    }
    public Color getColor() {
        return color;
    }
    public Square getSquare() {
        return square;
    }
    public abstract PieceType getType();
    public abstract List<Move> pseudoLegalMoves(Board board, Square enPassantTarget);
    public abstract Piece copyTo(Square target);
    public char symbol() {
        char letter = getType().getLetter();
        if (color == Color.WHITE) {
            return letter;
        } else {
            return Character.toLowerCase(letter);
        }
    }
}