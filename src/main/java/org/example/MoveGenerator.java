package org.example;
import java.util.ArrayList;
import java.util.List;
public class MoveGenerator {
    public List<Move> legalMoves(Position pos) {
        List<Move> result = new ArrayList<Move>();
        Board board = pos.getBoard();
        List<Piece> pieces = board.piecesOf(pos.getTurnToMove());
        for (int i = 0; i < pieces.size(); i++) {
            Piece piece = pieces.get(i);
            List<Move> pseudo = piece.pseudoLegalMoves(board, pos.getEnPassantTarget());
            for (int j = 0; j < pseudo.size(); j++) {
                Move m = pseudo.get(j);
                Position next = pos.withMove(m);
                if (!isInCheck(next, pos.getTurnToMove())) {
                    result.add(m);
                }
            }
        }
        addCastles(pos, result);
        return result;
    }
    public boolean isInCheck(Position pos, Color color) {
        Piece king = pos.getBoard().kingOf(color);

        List<Piece> enemies = pos.getBoard().piecesOf(color.opposite());
        for (int i = 0; i < enemies.size(); i++) {
            Piece p = enemies.get(i);
            List<Move> moves = p.pseudoLegalMoves(pos.getBoard(), null);
            for (int j = 0; j < moves.size(); j++) {
                if (moves.get(j).getTo().equals(king.getSquare())) {
                    return true;
                }
            }
        }
        return false;
    }
    public void validateMove(Position pos, Move move) {
        List<Move> legal = legalMoves(pos);
        if (!legal.contains(move)) {
            throw new ChessException("Недопустимый ход: " + move);
        }
    }
    private void addCastles(Position pos, List<Move> out) {
        Board board = pos.getBoard();
        Color color = pos.getTurnToMove();
        CastleRights rights = pos.getCastleRights();
        int row;
        if (color == Color.WHITE) {
            row = 0;
        } else {
            row = 7;
        }
        Square from = new Square(row, 4);
        Piece king = board.get(from);
        if (!(king instanceof King)) {
            return;
        }
        if (isInCheck(pos, color)) {
            return;
        }
        if (rights.canCastle(color, true)) {
            Square f = new Square(row, 5);
            Square g = new Square(row, 6);
            Square h = new Square(row, 7);
            if (board.get(h) instanceof Rook
                    && board.isEmpty(f)
                    && board.isEmpty(g)
                    && !attacked(board, f, color.opposite())
                    && !attacked(board, g, color.opposite())) {
                out.add(new Move(from, g));
            }
        }
        if (rights.canCastle(color, false)) {
            Square a = new Square(row, 0);
            Square b = new Square(row, 1);
            Square c = new Square(row, 2);
            Square d = new Square(row, 3);
            if (board.get(a) instanceof Rook
                    && board.isEmpty(b)
                    && board.isEmpty(c)
                    && board.isEmpty(d)
                    && !attacked(board, c, color.opposite())
                    && !attacked(board, d, color.opposite())) {
                out.add(new Move(from, c));
            }
        }
    }
    private boolean attacked(Board board, Square sq, Color by) {
        List<Piece> pieces = board.piecesOf(by);
        for (int i = 0; i < pieces.size(); i++) {
            Piece p = pieces.get(i);
            List<Move> moves = p.pseudoLegalMoves(board, null);
            for (int j = 0; j < moves.size(); j++) {
                if (moves.get(j).getTo().equals(sq)) {
                    return true;
                }
            }
        }
        return false;
    }
}