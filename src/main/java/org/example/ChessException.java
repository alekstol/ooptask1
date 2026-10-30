package org.example;
/**
 * Исключение домена шахмат.
 *
 * <p>Наследует {@link RuntimeException}; ловить стоит именно
 * {@code ChessException}, а не все {@code RuntimeException}.
 */
public class ChessException extends RuntimeException {
    /**
     * @param message описание ошибки
     */
    public ChessException(String message) {
        super(message);
    }
}