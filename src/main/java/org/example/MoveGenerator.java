package org.example;
import java.util.ArrayList;
import java.util.List;
/**
 * Вспомогательный слой: легальные ходы, шах, мат, пат, ничьи.
 *
 * <p>Акцент проекта — {@link #isCheckmate}, {@link #isStalemate},
 * {@link #isThreefoldRepetition}, {@link #isFiftyMoveRule},
 * {@link #result}. Генерация ходов и проверка шаха — внутренние
 * помощники для этих состояний и для {@link Notation}.
 *
 * @see Notation
 * @see PositionKey
 */
public class MoveGenerator {

    /** Результат партии. */
    public enum Result {
        /** Игра продолжается. */      ONGOING,
        /** Мат белым. */               BLACK_WIN,
        /** Мат чёрным. */              WHITE_WIN,
        /** Ничья. */                   DRAW
    }
    /**
     * Легальные ходы. Внутренний помощник для мата/пата и нотации.
     *
     * @param pos позиция
     * @return список легальных ходов
     * @throws ChessException если нет короля нужного цвета
     */
    public List<Move> legalMoves(Position pos) {
        List<Move> result = new ArrayList<Move>();
        Board board = pos.getBoard();
        for (Piece piece : board.piecesOf(pos.getTurnToMove())) {
            for (Move m : piece.pseudoLegalMoves(board, pos.getEnPassantTarget())) {
                if (!isInCheck(pos.withMove(m), pos.getTurnToMove())) {
                    result.add(m);
                }
            }
        }
        addCastles(pos, result);
        return result;
    }
    /**
     * @param pos позиция
     * @param color чей король
     * @return {@code true}, если король под шахом
     * @throws ChessException если короля цвета нет
     */
    public boolean isInCheck(Position pos, Color color) {
        Piece king = pos.getBoard().kingOf(color);
        for (Piece p : pos.getBoard().piecesOf(color.opposite())) {
            for (Move m : p.pseudoLegalMoves(pos.getBoard(), null)) {
                if (m.getTo().equals(king.getSquare())) return true;
            }
        }
        return false;
    }
    /** @param pos позиция @return мат тому, чей ход */
    public boolean isCheckmate(Position pos) {
        return isInCheck(pos, pos.getTurnToMove()) && legalMoves(pos).isEmpty();
    }
    /** @param pos позиция @return пат тому, чей ход */
    public boolean isStalemate(Position pos) {
        return !isInCheck(pos, pos.getTurnToMove()) && legalMoves(pos).isEmpty();
    }
    /** @param pos позиция @return {@code halfmoveClock >= 100} */
    public boolean isFiftyMoveRule(Position pos) {
        return pos.getHalfmoveClock() >= 100;
    }
    /** @param pos позиция @return позиция встречалась в истории ≥ 3 раз */
    public boolean isThreefoldRepetition(Position pos) {
        PositionKey current = PositionKey.of(pos);
        int count = 0;
        for (PositionKey k : pos.getHistory()) {
            if (k.equals(current)) count++;
        }
        return count >= 3;
    }
    /**
     * Недостаток материала: K vs K, K+minor vs K, K+B vs K+B
     * (одноцветные слоны).
     *
     * @param pos позиция
     * @return {@code true}, если материала недостаточно для мата
     * @implNote K+B vs K+B с разными цветами и K+N+N vs K не покрыты.
     */
    public boolean isInsufficientMaterial(Position pos) {
        int wMinor = 0, bMinor = 0, wBishops = 0, bBishops = 0;
        Square wb = null, bb = null;
        for (Piece p : pos.getBoard().piecesOf(Color.WHITE)) {
            if (p.getType() == PieceType.KING) continue;
            if (p.getType() == PieceType.BISHOP) { wBishops++; wMinor++; wb = p.getSquare(); }
            else if (p.getType() == PieceType.KNIGHT) { wMinor++; }
            else return false;
        }
        for (Piece p : pos.getBoard().piecesOf(Color.BLACK)) {
            if (p.getType() == PieceType.KING) continue;
            if (p.getType() == PieceType.BISHOP) { bBishops++; bMinor++; bb = p.getSquare(); }
            else if (p.getType() == PieceType.KNIGHT) { bMinor++; }
            else return false;
        }
        if (wMinor == 0 && bMinor == 0) return true;
        if (wMinor + bMinor == 1) return true;
        if (wMinor == 1 && bMinor == 1 && wBishops == 1 && bBishops == 1
                && wb != null && bb != null) {
            boolean wDark = (wb.getRow() + wb.getCol()) % 2 != 0;
            boolean bDark = (bb.getRow() + bb.getCol()) % 2 != 0;
            return wDark == bDark;
        }
        return false;
    }
    /** @param pos позиция @return результат партии */
    public Result result(Position pos) {
        if (isCheckmate(pos)) {
            return pos.getTurnToMove() == Color.WHITE ? Result.BLACK_WIN : Result.WHITE_WIN;
        }
        if (isStalemate(pos) || isFiftyMoveRule(pos)
                || isThreefoldRepetition(pos) || isInsufficientMaterial(pos)) {
            return Result.DRAW;
        }
        return Result.ONGOING;
    }
    /** @param pos позиция @return {@code true}, если партия завершена */
    public boolean isGameOver(Position pos) {
        return result(pos) != Result.ONGOING;
    }
    private void addCastles(Position pos, List<Move> out) {
        Board board = pos.getBoard();
        Color color = pos.getTurnToMove();
        CastleRights rights = pos.getCastleRights();
        int row = (color == Color.WHITE) ? 0 : 7;
        Square from = new Square(row, 4);
        if (!(board.get(from) instanceof King)) return;
        if (isInCheck(pos, color)) return;
        if (rights.canCastle(color, true)) {
            Square f = new Square(row, 5);
            Square g = new Square(row, 6);
            if (board.get(new Square(row, 7)) instanceof Rook
                    && board.isEmpty(f) && board.isEmpty(g)
                    && !attacked(board, f, color.opposite())
                    && !attacked(board, g, color.opposite())) {
                out.add(new Move(from, g));
            }
        }
        if (rights.canCastle(color, false)) {
            Square b = new Square(row, 1);
            Square c = new Square(row, 2);
            Square d = new Square(row, 3);
            if (board.get(new Square(row, 0)) instanceof Rook
                    && board.isEmpty(b) && board.isEmpty(c) && board.isEmpty(d)
                    && !attacked(board, c, color.opposite())
                    && !attacked(board, d, color.opposite())) {
                out.add(new Move(from, c));
            }
        }
    }
    private boolean attacked(Board board, Square sq, Color by) {
        for (Piece p : board.piecesOf(by)) {
            for (Move m : p.pseudoLegalMoves(board, null)) {
                if (m.getTo().equals(sq)) return true;
            }
        }
        return false;
    }
}