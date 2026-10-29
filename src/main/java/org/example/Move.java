package org.example;
import java.util.Objects;

public class Move {
    private Square from;
    private Square to;
    private PieceType promotion;
    public Move(Square from, Square to) {
        this.from = from;
        this.to = to;
        this.promotion = null;
    }
    public Move(Square from, Square to, PieceType promotion) {
        this.from = from;
        this.to = to;
        this.promotion = promotion;
    }
    public Square getFrom() {
        return from;
    }
    public Square getTo() {
        return to;
    }
    public PieceType getPromotion() {
        return promotion;
    }
    public boolean isPromotion() {
        return promotion != null;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Move)) return false;
        Move other = (Move) o;
        return from.equals(other.from)
                && to.equals(other.to)
                && Objects.equals(promotion, other.promotion);
    }
    @Override
    public int hashCode() {
        return Objects.hash(from, to, promotion);
    }
    @Override
    public String toString() {
        return from + "->" + to + (isPromotion() ? " (" + promotion + ")" : "");
    }
}