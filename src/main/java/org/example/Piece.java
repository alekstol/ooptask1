package org.example;
import java.util.List;
/**
 * Абстрактная фигура: цвет и текущая клетка.
 *
 * <p><b>Закрытая иерархия.</b> Наследники — {@link Bishop}, {@link Rook},
 * {@link Queen}, {@link Knight}, {@link King}, {@link Pawn}. Правила
 * движения реализуются полиморфно в
 * {@link #pseudoLegalMoves(Board, Square)}; проверка шаха и легальности —
 * во вспомогательном слое {@link MoveGenerator}.
 *
 * @see MoveGenerator
 */
public abstract class Piece {
    /** Цвет фигуры. */
    protected final Color color;
    /** Текущая клетка. */
    protected Square square;
    /**
     * @param color цвет
     * @param square начальная клетка
     */
    public Piece(Color color, Square square) {
        this.color = color;
        this.square = square;
    }
    /** @return цвет */
    public Color getColor() { return color; }
    /** @return текущая клетка */
    public Square getSquare() { return square; }
    /**
     * Меняет клетку (при копировании и рокировке).
     *
     * @param square новая клетка
     */
    public void setSquare(Square square) { this.square = square; }
    /** @return тип фигуры */
    public abstract PieceType getType();
    /**
     * Псевдолегальные ходы: по правилам фигуры, <em>без</em> проверки шаха.
     *
     * @param board доска
     * @param enPassantTarget клетка взятия на проходе или {@code null};
     *                        используется только {@link Pawn}
     * @return список ходов (может быть пустым)
     */
    public abstract List<Move> pseudoLegalMoves(Board board, Square enPassantTarget);
    /**
     * Копия фигуры на новой клетке.
     *
     * @param target клетка для копии
     * @return новая фигура того же типа и цвета
     */
    public abstract Piece copyTo(Square target);
    /**
     * @return заглавная буква для белых, строчная — для чёрных
     */
    public char symbol() {
        char letter = getType().getLetter();
        return color == Color.WHITE ? letter : Character.toLowerCase(letter);
    }
}