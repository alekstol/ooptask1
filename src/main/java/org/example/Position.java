package org.example;
import java.util.Objects;
public record Position(Board board, Color turnToMove,
                       CastleRights castleRights, Square enPassantTarget) {
    public Position {
        Objects.requireNonNull(board, "board");
        Objects.requireNonNull(turnToMove, "turnToMove");
        Objects.requireNonNull(castleRights, "castleRights");
    }
    public static Position initial() {
        var board = new Board();
        board.set(Square.of("a1"), new Rook (Color.WHITE, Square.of("a1")));
        board.set(Square.of("h1"), new Rook (Color.WHITE, Square.of("h1")));
        board.set(Square.of("a8"), new Rook (Color.BLACK, Square.of("a8")));
        board.set(Square.of("h8"), new Rook (Color.BLACK, Square.of("h8")));
        board.set(Square.of("b1"), new Knight(Color.WHITE, Square.of("b1")));
        board.set(Square.of("g1"), new Knight(Color.WHITE, Square.of("g1")));
        board.set(Square.of("b8"), new Knight(Color.BLACK, Square.of("b8")));
        board.set(Square.of("g8"), new Knight(Color.BLACK, Square.of("g8")));
        board.set(Square.of("c1"), new Bishop(Color.WHITE, Square.of("c1")));
        board.set(Square.of("f1"), new Bishop(Color.WHITE, Square.of("f1")));
        board.set(Square.of("c8"), new Bishop(Color.BLACK, Square.of("c8")));
        board.set(Square.of("f8"), new Bishop(Color.BLACK, Square.of("f8")));
        board.set(Square.of("d1"), new Queen(Color.WHITE, Square.of("d1")));
        board.set(Square.of("e1"), new King (Color.WHITE, Square.of("e1")));
        board.set(Square.of("d8"), new Queen(Color.BLACK, Square.of("d8")));
        board.set(Square.of("e8"), new King (Color.BLACK, Square.of("e8")));
        for (char f = 'a'; f <= 'h'; f++) {
            board.set(Square.of("" + f + "2"), new Pawn(Color.WHITE, Square.of("" + f + "2")));
            board.set(Square.of("" + f + "7"), new Pawn(Color.BLACK, Square.of("" + f + "7")));
        }
        return new Position(board, Color.WHITE, CastleRights.initial(), null);
    }
    public Position withMove(Move move) {
        var newBoard = board.copy();
        var piece = newBoard.get(move.from());
        if (piece == null) throw new ChessException("Нет фигуры на " + move.from());
        if (piece.color() != turnToMove) throw new ChessException("Ход не вашего цвета");
        var target = newBoard.get(move.to());
        if (target != null && target.color() == piece.color())
            throw new ChessException("Нельзя бить свою фигуру на " + move.to());
        var rights = castleRights;
        Square newEp = null;
        boolean isEnPassant = piece.type() == PieceType.PAWN
                && move.from().col() != move.to().col()
                && target == null;
        if (isEnPassant) {
            newBoard.set(move.from().row(), move.to().col(), null);
        }
        if (piece.type() == PieceType.PAWN
                && Math.abs(move.to().row() - move.from().row()) == 2) {
            int epRow = (move.from().row() + move.to().row()) / 2;
            newEp = new Square(epRow, move.from().col());
        }
        newBoard.set(move.from(), null);
        if (move.isPromotion() && piece.type() == PieceType.PAWN) {
            var promoted = createPiece(move.promotion(), piece.color(), move.to());
            newBoard.set(move.to(), promoted);
        } else {
            newBoard.set(move.to(), piece);
            switch (piece) {
                case King k  -> { k.markMoved(); rights = rights.revokeAll(k.color()); }
                case Rook r  -> { r.markMoved();
                    boolean kingSide = r.square().col() == 7;
                    rights = rights.revoke(r.color(), kingSide); }
                case Pawn p  -> p.markMoved();
                default -> { }
            }
            if (target instanceof Rook rookTarget) {
                boolean kingSide = rookTarget.square().col() == 7;
                rights = rights.revoke(rookTarget.color(), kingSide);
            }
        }
        if (piece.type() == PieceType.KING
                && Math.abs(move.to().col() - move.from().col()) == 2) {
            if (move.to().col() == 6) {
                var rook = newBoard.get(move.from().row(), 7);
                newBoard.set(move.from().row(), 7, null);
                newBoard.set(move.from().row(), 5, rook);
                if (rook != null) rook.setSquare(new Square(move.from().row(), 5));
            } else {
                var rook = newBoard.get(move.from().row(), 0);
                newBoard.set(move.from().row(), 0, null);
                newBoard.set(move.from().row(), 3, rook);
                if (rook != null) rook.setSquare(new Square(move.from().row(), 3));
            }
        }
        return new Position(newBoard, turnToMove.opposite(), rights, newEp);
    }
    private static Piece createPiece(PieceType type, Color color, Square sq) {
        return switch (type) {
            case QUEEN  -> new Queen (color, sq);
            case ROOK   -> new Rook  (color, sq);
            case BISHOP -> new Bishop(color, sq);
            case KNIGHT -> new Knight(color, sq);
            case KING, PAWN -> throw new ChessException("Нельзя превратить в " + type);
        };
    }
    @Override
    public String toString() {
        return board + "Ход: " + turnToMove + "\n";
    }
}