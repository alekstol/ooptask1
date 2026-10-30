package org.example;
/**
 * Цвет фигуры в шахматной партии.
 *
 * <p>Каждая константа знает свой противоположный цвет
 * ({@link #opposite()}) и направление движения пешки по строкам
 * ({@link #direction()}).
 *
 * @see #opposite()
 * @see #direction()
 */
public enum Color {
    /** Белые. Пешки движутся в сторону увеличения индекса строки. */
    WHITE {
        /** {@inheritDoc} */
        @Override public Color opposite() { return BLACK; }
        /** {@inheritDoc} */
        @Override public int direction() { return 1; }
    },
    /** Чёрные. Пешки движутся в сторону уменьшения индекса строки. */
    BLACK {
        /** {@inheritDoc} */
        @Override public Color opposite() { return WHITE; }
        /** {@inheritDoc} */
        @Override public int direction() { return -1; }
    };
    /**
     * @return противоположный цвет: {@link #BLACK} для белых, {@link #WHITE} для чёрных
     */
    public abstract Color opposite();

    /**
     * @return {@code +1} для белых, {@code -1} для чёрных
     */
    public abstract int direction();
}