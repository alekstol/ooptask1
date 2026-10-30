package org.example;
import java.util.ArrayList;
import java.util.List;
/**
 * Полное состояние партии: доска, чей ход, права на рокировку, клетка
 * взятия на проходе, счётчики (полуходы, номер хода) и история позиций.
 *
 * <p><b>Инварианты состояния.</b>
 * <ul>
 *   <li>{@code board}, {@code turnToMove}, {@code castleRights} не {@code null};</li>
 *   <li>{@code halfmoveClock >= 0}, {@code fullmoveNumber >= 1};</li>
 *   <li>{@code history} не пуст и заканчивается слепком текущей позиции
 *       ({@link PositionKey});</li>
 *   <li>{@code enPassantTarget} либо {@code null}, либо на доске.</li>
 * </ul>
 *
 * <p><b>Иммутабельность.</b> После создания объект не меняется.
 * Применение хода возвращает <em>новую</em> позицию через
 * {@link #withMove(Move)}. Это позволяет {@link MoveGenerator} и
 * {@link Notation#replay} строить цепочки позиций без побочных эффектов.
 *
 * @see PositionKey
 * @see MoveGenerator
 * @see Notation
 */
public class Position {
    private final Board board;
    private final Color turnToMove;
    private final CastleRights castleRights;
    private final Square enPassantTarget;
    private final int halfmoveClock;
    private final int fullmoveNumber;
    private final List<PositionKey> history;
    /**
     * Создаёт позицию из готовых компонентов. Счётчики инициализируются
     * значениями {@code 0} и {@code 1}, история — одной (текущей)
     * позицией.
     *
     * @param board доска (не {@code null})
     * @param turnToMove чей ход
     * @param castleRights права на рокировку
     * @param enPassantTarget клетка взятия на проходе или {@code null}
     */
    public Position(Board board, Color turnToMove, CastleRights castleRights,
                    Square enPassantTarget) {
        this(board, turnToMove, castleRights, enPassantTarget, 0, 1,
                new ArrayList<PositionKey>());
    }
    /**
     * Полный конструктор, используемый внутри {@link #withMove(Move)}.
     * История копируется, затем в неё добавляется слепок текущей позиции.
     *
     * @param board доска
     * @param turnToMove чей ход
     * @param castleRights права
     * @param enPassantTarget клетка или {@code null}
     * @param halfmoveClock счётчик полуходов
     * @param fullmoveNumber номер хода
     * @param history история предыдущих позиций (будет скопирована)
     */
    private Position(Board board, Color turnToMove, CastleRights castleRights,
                     Square enPassantTarget, int halfmoveClock,
                     int fullmoveNumber, List<PositionKey> history) {
        this.board = board;
        this.turnToMove = turnToMove;
        this.castleRights = castleRights;
        this.enPassantTarget = enPassantTarget;
        this.halfmoveClock = halfmoveClock;
        this.fullmoveNumber = fullmoveNumber;
        this.history = new ArrayList<PositionKey>(history);
        this.history.add(PositionKey.of(this));
    }
    /**
     * @return доска позиции. Ссылка изменяема — не мутируйте её без
     *         необходимости, чтобы не нарушить инварианты позиции
     */
    public Board getBoard() { return board; }
    /** @return цвет того, чей сейчас ход */
    public Color getTurnToMove() { return turnToMove; }
    /** @return текущие права на рокировку */
    public CastleRights getCastleRights() { return castleRights; }
    /** @return клетка взятия на проходе или {@code null} */
    public Square getEnPassantTarget() { return enPassantTarget; }
    /** @return счётчик полуходов с последнего взятия или хода пешкой */
    public int getHalfmoveClock() { return halfmoveClock; }
    /** @return номер текущего хода (с 1) */
    public int getFullmoveNumber() { return fullmoveNumber; }
    /**
     * @return копия истории позиций (включая текущую); изменение
     *         возвращённого списка не влияет на позицию
     */
    public List<PositionKey> getHistory() {
        return new ArrayList<PositionKey>(history);
    }
    /**
     * Создаёт начальную расстановку: все фигуры на своих местах,
     * ход белых, все права на рокировку, взятие на проходе недоступно,
     * счётчики {@code 0}/{@code 1}, история — одна позиция.
     *
     * @return новая начальная позиция
     */
    public static Position initial() {
        Board b = new Board();
        b.set(Square.of("a1"), new Rook(Color.WHITE, Square.of("a1")));
        b.set(Square.of("h1"), new Rook(Color.WHITE, Square.of("h1")));
        b.set(Square.of("a8"), new Rook(Color.BLACK, Square.of("a8")));
        b.set(Square.of("h8"), new Rook(Color.BLACK, Square.of("h8")));
        b.set(Square.of("b1"), new Knight(Color.WHITE, Square.of("b1")));
        b.set(Square.of("g1"), new Knight(Color.WHITE, Square.of("g1")));
        b.set(Square.of("b8"), new Knight(Color.BLACK, Square.of("b8")));
        b.set(Square.of("g8"), new Knight(Color.BLACK, Square.of("g8")));
        b.set(Square.of("c1"), new Bishop(Color.WHITE, Square.of("c1")));
        b.set(Square.of("f1"), new Bishop(Color.WHITE, Square.of("f1")));
        b.set(Square.of("c8"), new Bishop(Color.BLACK, Square.of("c8")));
        b.set(Square.of("f8"), new Bishop(Color.BLACK, Square.of("f8")));
        b.set(Square.of("d1"), new Queen(Color.WHITE, Square.of("d1")));
        b.set(Square.of("e1"), new King(Color.WHITE, Square.of("e1")));
        b.set(Square.of("d8"), new Queen(Color.BLACK, Square.of("d8")));
        b.set(Square.of("e8"), new King(Color.BLACK, Square.of("e8")));
        for (char f = 'a'; f <= 'h'; f++) {
            b.set(Square.of("" + f + "2"), new Pawn(Color.WHITE, Square.of("" + f + "2")));
            b.set(Square.of("" + f + "7"), new Pawn(Color.BLACK, Square.of("" + f + "7")));
        }
        return new Position(b, Color.WHITE, CastleRights.initial(), null);
    }
    /**
     * Применяет ход и возвращает <em>новую</em> позицию. Исходная
     * не меняется.
     *
     * <p>Помимо перемещения фигуры, метод:
     * <ul>
     *   <li>снимает пешку при взятии на проходе;</li>
     *   <li>пересчитывает клетку {@code en passant} после двойного
     *       хода пешки;</li>
     *   <li>создаёт новую фигуру при превращении (через
     *       {@link #createPiece});</li>
     *   <li>отзывает права на рокировку при ходе короля/ладьи или
     *       взятии ладьи;</li>
     *   <li>переставляет ладью при рокировке;</li>
     *   <li>обновляет {@link #getHalfmoveClock()} (сброс при взятии или
     *       ходе пешкой, иначе {@code +1});</li>
     *   <li>обновляет {@link #getFullmoveNumber()} (после хода чёрных
     *       {@code +1});</li>
     *   <li>добавляет слепок новой позиции в {@link #getHistory()}.</li>
     * </ul>
     *
     * <p>Метод <b>не</b> проверяет легальность хода по правилам фигур —
     * это делает {@link MoveGenerator#legalMoves(Position)}. Он лишь
     * страхует от грубых ошибок: нет фигуры на {@code from}, цвет не
     * совпадает с {@link #getTurnToMove()}, на {@code to} стоит своя
     * фигура.
     *
     * @param move ход для применения
     * @return новая позиция после хода; ход переходит противнику
     * @throws ChessException если на {@code from} нет фигуры, цвет
     *         фигуры не совпадает с {@code turnToMove} или на {@code to}
     *         стоит своя фигура
     * @implNote В ветке превращения не отзывается право на рокировку
     *           у владельца срубленной ладьи — известное ограничение.
     */
    public Position withMove(Move move) {
        Board nb = board.copy();
        Piece piece = nb.get(move.getFrom());
        if (piece == null) {
            throw new ChessException("Нет фигуры на " + move.getFrom());
        }
        if (piece.getColor() != turnToMove) {
            throw new ChessException("Ход не вашего цвета");
        }
        Piece target = nb.get(move.getTo());
        if (target != null && target.getColor() == piece.getColor()) {
            throw new ChessException("Нельзя бить свою фигуру на " + move.getTo());
        }
        boolean isCapture = target != null;
        CastleRights rights = castleRights;
        Square newEp = null;
        boolean isEp = false;

        if (piece.getType() == PieceType.PAWN
                && move.getFrom().getCol() != move.getTo().getCol()
                && target == null) {
            isEp = true;
            isCapture = true;
        }
        if (isEp) {
            nb.set(move.getFrom().getRow(), move.getTo().getCol(), null);
        }
        if (piece.getType() == PieceType.PAWN) {
            int diff = move.getTo().getRow() - move.getFrom().getRow();
            if (Math.abs(diff) == 2) {
                int epRow = (move.getFrom().getRow() + move.getTo().getRow()) / 2;
                newEp = new Square(epRow, move.getFrom().getCol());
            }
        }
        nb.set(move.getFrom(), null);
        if (move.isPromotion() && piece.getType() == PieceType.PAWN) {
            nb.set(move.getTo(),
                    createPiece(move.getPromotion(), piece.getColor(), move.getTo()));
        } else {
            nb.set(move.getTo(), piece);
        }
        if (piece instanceof King) {
            rights = rights.revokeAll(piece.getColor());
        } else if (piece instanceof Rook) {
            boolean kingSide = piece.getSquare().getCol() == 7;
            rights = rights.revoke(piece.getColor(), kingSide);
        }
        if (target instanceof Rook) {
            boolean kingSide = target.getSquare().getCol() == 7;
            rights = rights.revoke(target.getColor(), kingSide);
        }
        if (piece.getType() == PieceType.KING) {
            int diff = move.getTo().getCol() - move.getFrom().getCol();
            if (Math.abs(diff) == 2) {
                int row = move.getFrom().getRow();
                if (move.getTo().getCol() == 6) {
                    Piece rook = nb.get(row, 7);
                    nb.set(row, 5, rook);
                    nb.set(row, 7, null);
                    if (rook != null) {
                        rook.setSquare(new Square(row, 5));
                    }
                } else {
                    Piece rook = nb.get(row, 0);
                    nb.set(row, 3, rook);
                    nb.set(row, 0, null);
                    if (rook != null) {
                        rook.setSquare(new Square(row, 3));
                    }
                }
            }
        }
        int newHalfmove = (isCapture || piece.getType() == PieceType.PAWN)
                ? 0 : halfmoveClock + 1;
        int newFullmove = (turnToMove == Color.BLACK)
                ? fullmoveNumber + 1 : fullmoveNumber;

        return new Position(nb, turnToMove.opposite(), rights, newEp,
                newHalfmove, newFullmove, history);
    }
    /**
     * Фабрика фигур для превращения пешки.
     *
     * <p>Допустимы только {@link PieceType#QUEEN}, {@link PieceType#ROOK},
     * {@link PieceType#BISHOP}, {@link PieceType#KNIGHT}. Королём или
     * пешкой превращать нельзя.
     *
     * @param type тип создаваемой фигуры
     * @param color цвет
     * @param sq клетка, на которую ставится фигура
     * @return новая фигура нужного типа и цвета
     * @throws ChessException если {@code type} — {@code KING} или
     *         {@code PAWN} (или иной недопустимый тип)
     */
    public static Piece createPiece(PieceType type, Color color, Square sq) {
        switch (type) {
            case QUEEN:  return new Queen(color, sq);
            case ROOK:   return new Rook(color, sq);
            case BISHOP: return new Bishop(color, sq);
            case KNIGHT: return new Knight(color, sq);
            default:
                throw new ChessException("Нельзя превратить в " + type);
        }
    }
    /**
     * @return строковое представление доски и чей ход, например:
     *         {@code "...доска...\nХод: WHITE\n"}
     */
    @Override
    public String toString() {
        return board + "Ход: " + turnToMove + "\n";
    }
}