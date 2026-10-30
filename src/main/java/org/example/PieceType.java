package org.example;
/**
 * Тип шахматной фигуры и её буквенный символ.
 *
 * @see Piece#symbol()
 */
public enum PieceType {
    /** Король. */    KING('K'),
    /** Ферзь. */     QUEEN('Q'),
    /** Ладья. */     ROOK('R'),
    /** Слон. */      BISHOP('B'),
    /** Конь. */      KNIGHT('N'),
    /** Пешка. */     PAWN('P');
    private final char letter;
    /**
     * @param letter буква фигуры для печати
     */
    PieceType(char letter) {
        this.letter = letter;
    }
    /** @return буква фигуры */
    public char getLetter() {
        return letter;
    }
}