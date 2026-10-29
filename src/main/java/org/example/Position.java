package org.example;
public class Position {
    private Board board;
    private Color turnToMove;
    private CastleRights castleRights;
    private Square enPassantTarget; // может быть null
    public Position(Board board, Color turnToMove,
                    CastleRights castleRights, Square enPassantTarget) {
        this.board = board;
        this.turnToMove = turnToMove;
        this.castleRights = castleRights;
        this.enPassantTarget = enPassantTarget;
    }
    public Board getBoard() {
        return board;
    }
    public Color getTurnToMove() {
        return turnToMove;
    }
    public CastleRights getCastleRights() {
        return castleRights;
    }
    public Square getEnPassantTarget() {
        return enPassantTarget;
    }
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
        CastleRights rights = castleRights;
        Square newEp = null;
        boolean isEp = false;
        if (piece.getType() == PieceType.PAWN
                && move.getFrom().getCol() != move.getTo().getCol()
                && target == null) {
            isEp = true;
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
            Piece promoted = createPiece(move.getPromotion(), piece.getColor(), move.getTo());
            nb.set(move.getTo(), promoted);
        } else {
            nb.set(move.getTo(), piece);
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
        }
        if (piece.getType() == PieceType.KING) {
            int diff = move.getTo().getCol() - move.getFrom().getCol();
            if (Math.abs(diff) == 2) {
                int row = move.getFrom().getRow();
                if (move.getTo().getCol() == 6) {
                    // короткая
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

        return new Position(nb, turnToMove.opposite(), rights, newEp);
    }
    private static Piece createPiece(PieceType type, Color color, Square sq) {
        if (type == PieceType.QUEEN) {
            return new Queen(color, sq);
        } else if (type == PieceType.ROOK) {
            return new Rook(color, sq);
        } else if (type == PieceType.BISHOP) {
            return new Bishop(color, sq);
        } else if (type == PieceType.KNIGHT) {
            return new Knight(color, sq);
        } else {
            throw new ChessException("Нельзя превратить в " + type);
        }
    }
    @Override
    public String toString() {
        return board + "Ход: " + turnToMove + "\n";
    }
}