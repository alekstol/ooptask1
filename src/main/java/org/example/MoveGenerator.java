package org.example;
import java.util.ArrayList;
import java.util.List;
public final class MoveGenerator {
    public List<Move> legalMoves(Position pos) {
        var result = new ArrayList<Move>();
        var board  = pos.board();

        for (var piece : board.piecesOf(pos.turnToMove())) {
            for (var move : piece.pseudoLegalMoves(board, pos.enPassantTarget())) {
                if (isSafe(pos, move)) result.add(move);
            }
        }
        addCastles(pos, result);
        return List.copyOf(result);
    }
    public boolean isInCheck(Position pos, Color color) {
        var king = pos.board().kingOf(color);
        return isAttacked(pos.board(), king.square(), color.opposite());
    }
    private boolean isSafe(Position pos, Move move) {
        var next = pos.withMove(move);
        return !isInCheck(next, pos.turnToMove());
    }
    private boolean isAttacked(Board board, Square square, Color by) {
        for (var piece : board.piecesOf(by)) {
            for (var m : piece.pseudoLegalMoves(board, null)) {
                if (m.to().equals(square)) return true;
            }
        }
        return false;
    }
    private void addCastles(Position pos, List<Move> out) {
        var board  = pos.board();
        var color  = pos.turnToMove();
        var rights = pos.castleRights();
        int row      = (color == Color.WHITE) ? 0 : 7;
        var kingFrom = new Square(row, 4);
        var king     = board.get(kingFrom);
        if (!(king instanceof King k) || k.hasMoved()) return;
        if (isInCheck(pos, color)) return;
        if (rights.canCastle(color, true)) {
            var f1 = new Square(row, 5);
            var g1 = new Square(row, 6);
            var h1 = new Square(row, 7);
            var rook = board.get(h1);
            if (rook instanceof Rook r && !r.hasMoved()
                    && board.isEmpty(f1) && board.isEmpty(g1)
                    && !isAttacked(board, f1, color.opposite())
                    && !isAttacked(board, g1, color.opposite())) {
                out.add(new Move(kingFrom, g1));
            }
        }
        if (rights.canCastle(color, false)) {
            var d1 = new Square(row, 3);
            var c1 = new Square(row, 2);
            var b1 = new Square(row, 1);
            var a1 = new Square(row, 0);
            var rook = board.get(a1);
            if (rook instanceof Rook r && !r.hasMoved()
                    && board.isEmpty(d1) && board.isEmpty(c1) && board.isEmpty(b1)
                    && !isAttacked(board, d1, color.opposite())
                    && !isAttacked(board, c1, color.opposite())) {
                out.add(new Move(kingFrom, c1));
            }
        }
    }
    public void validateMove(Position pos, Move move) {
        var legal = legalMoves(pos);
        if (!legal.contains(move)) {
            throw new ChessException("Недопустимый ход: " + move);
        }
    }
}