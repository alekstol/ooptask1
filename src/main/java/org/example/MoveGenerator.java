package org.example;
import java.util.ArrayList;
import java.util.List;
public final class MoveGenerator {
    public List<Move> legalMoves(Position pos) {
        var result = new ArrayList<Move>();
        var board  = pos.board();
        for (var piece : board.piecesOf(pos.turnToMove()))
            for (var m : piece.pseudoLegalMoves(board, pos.enPassantTarget()))
                if (!isInCheck(pos.withMove(m), pos.turnToMove()))
                    result.add(m);
        addCastles(pos, result);
        return List.copyOf(result);
    }
    public boolean isInCheck(Position pos, Color color) {
        var king = pos.board().kingOf(color);
        for (var p : pos.board().piecesOf(color.opposite()))
            for (var m : p.pseudoLegalMoves(pos.board(), null))
                if (m.to().equals(king.square())) return true;
        return false;
    }
    public void validateMove(Position pos, Move move) {
        if (!legalMoves(pos).contains(move))
            throw new ChessException("Недопустимый ход: " + move);
    }
    private void addCastles(Position pos, List<Move> out) {
        var board  = pos.board();
        var color  = pos.turnToMove();
        var rights = pos.castleRights();
        int row    = (color == Color.WHITE) ? 0 : 7;
        var from   = new Square(row, 4);
        if (!(board.get(from) instanceof King)) return;
        if (isInCheck(pos, color)) return;
        if (rights.canCastle(color, true)) {
            var f = new Square(row, 5);
            var g = new Square(row, 6);
            if (board.get(new Square(row, 7)) instanceof Rook
                    && board.isEmpty(f) && board.isEmpty(g)
                    && !attacked(board, f, color.opposite())
                    && !attacked(board, g, color.opposite()))
                out.add(new Move(from, g));
        }
        if (rights.canCastle(color, false)) {
            var d = new Square(row, 3);
            var c = new Square(row, 2);
            var b = new Square(row, 1);
            if (board.get(new Square(row, 0)) instanceof Rook
                    && board.isEmpty(d) && board.isEmpty(c) && board.isEmpty(b)
                    && !attacked(board, d, color.opposite())
                    && !attacked(board, c, color.opposite()))
                out.add(new Move(from, c));
        }
    }
    private boolean attacked(Board board, Square sq, Color by) {
        for (var p : board.piecesOf(by))
            for (var m : p.pseudoLegalMoves(board, null))
                if (m.to().equals(sq)) return true;
        return false;
    }
}