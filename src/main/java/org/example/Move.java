package org.example;
import java.util.Objects;
/**
 * Ход: {@code from}, {@code to}, опционально {@code promotion}.
 * {@code promotion == null} ⇔ не превращение.
 */
public class Move {
    private final Square from;
    private final Square to;
    private final PieceType promotion;
    /** @param from откуда @param to куда */
    public Move(Square from, Square to) { this(from, to, null); }
    /** @param from откуда @param to куда @param promotion тип превращения или {@code null} */
    public Move(Square from, Square to, PieceType promotion) {
        this.from = from; this.to = to; this.promotion = promotion;
    }
    /** @return откуда */
    public Square getFrom() { return from; }
    /** @return куда */
    public Square getTo() { return to; }
    /** @return тип превращения или {@code null} */
    public PieceType getPromotion() { return promotion; }
    /** @return {@code true}, если превращение */
    public boolean isPromotion() { return promotion != null; }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Move)) return false;
        Move m = (Move) o;
        return from.equals(m.from) && to.equals(m.to)
                && Objects.equals(promotion, m.promotion);
    }
    @Override
    public int hashCode() { return Objects.hash(from, to, promotion); }
    @Override
    public String toString() {
        return from + "->" + to + (isPromotion() ? " (" + promotion + ")" : "");
    }
}