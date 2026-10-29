package org.example;
public record Position(Board board, Color turnToMove,
                       CastleRights castleRights, Square enPassantTarget) {
    public static Position initial() {
        var b = new Board();
        b.set(Square.of("a1"), new Rook  (Color.WHITE, Square.of("a1")));
        b.set(Square.of("h1"), new Rook  (Color.WHITE, Square.of("h1")));
        b.set(Square.of("a8"), new Rook  (Color.BLACK, Square.of("a8")));
        b.set(Square.of("h8"), new Rook  (Color.BLACK, Square.of("h8")));
        b.set(Square.of("b1"), new Knight(Color.WHITE, Square.of("b1")));
        b.set(Square.of("g1"), new Knight(Color.WHITE, Square.of("g1")));
        b.set(Square.of("b8"), new Knight(Color.BLACK, Square.of("b8")));
        b.set(Square.of("g8"), new Knight(Color.BLACK, Square.of("g8")));
        b.set(Square.of("c1"), new Bishop(Color.WHITE, Square.of("c1")));
        b.set(Square.of("f1"), new Bishop(Color.WHITE, Square.of("f1")));
        b.set(Square.of("c8"), new Bishop(Color.BLACK, Square.of("c8")));
        b.set(Square.of("f8"), new Bishop(Color.BLACK, Square.of("f8")));
        b.set(Square.of("d1"), new Queen (Color.WHITE, Square.of("d1")));
        b.set(Square.of("e1"), new King  (Color.WHITE, Square.of("e1")));
        b.set(Square.of("d8"), new Queen (Color.BLACK, Square.of("d8")));
        b.set(Square.of("e8"), new King  (Color.BLACK, Square.of("e8")));
        for (char f = 'a'; f <= 'h'; f++) {
            b.set(Square.of("" + f + "2"), new Pawn(Color.WHITE, Square.of("" + f + "2")));
            b.set(Square.of("" + f + "7"), new Pawn(Color.BLACK, Square.of("" + f + "7")));
        }
        return new Position(b, Color.WHITE, CastleRights.initial(), null);
    }
    public Position withMove(Move move) {
        var nb = board.copy();
        var piece = nb.get(move.from());
        if (piece == null) throw new ChessException("Нет фигуры на " + move.from());
        if (piece.color() != turnToMove) throw new ChessException("Ход не вашего цвета");
        var target = nb.get(move.to());
        if (target != null && target.color() == piece.color())
            throw new ChessException("Нельзя бить свою фигуру на " + move.to());
        var rights = castleRights;
        Square newEp = null;
        boolean isEp = piece.type() == PieceType.PAWN
                && move.from().col() != move.to().col()
                && target == null;
        if (isEp) nb.set(move.from().row(), move.to().col(), null);

        if (piece.type() == PieceType.PAWN
                && Math.abs(move.to().row() - move.from().row()) == 2) {
            int epRow = (move.from().row() + move.to().row()) / 2;
            newEp = new Square(epRow, move.from().col());
        }
        nb.set(move.from(), null);
        if (move.isPromotion() && piece.type() == PieceType.PAWN) {
            nb.set(move.to(), createPiece(move.promotion(), piece.color(), move.to()));
        } else {
            nb.set(move.to(), piece);
            switch (piece) {
                case King k -> rights = rights.revokeAll(k.color());
                case Rook r -> rights = rights.revoke(r.color(), r.square().col() == 7);
                default     -> { }
            }
            if (target instanceof Rook rookTarget)
                rights = rights.revoke(rookTarget.color(), rookTarget.square().col() == 7);
        }
        if (piece.type() == PieceType.KING
                && Math.abs(move.to().col() - move.from().col()) == 2) {
            int row = move.from().row();
            if (move.to().col() == 6) {
                nb.set(row, 5, nb.get(row, 7));
                nb.set(row, 7, null);
            } else {
                nb.set(row, 3, nb.get(row, 0));
                nb.set(row, 0, null);
            }
        }
        return new Position(nb, turnToMove.opposite(), rights, newEp);
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
    @Override public String toString() { return board + "Ход: " + turnToMove + "\n"; }
}