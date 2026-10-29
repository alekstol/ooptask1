package org.example;
public class Demo {
    public static void main(String[] args) {
        MoveGenerator gen = new MoveGenerator();
        Position pos = Position.initial();
        System.out.println("Начальная позиция:");
        System.out.println(pos);
        System.out.println("Легальных ходов белых: " + gen.legalMoves(pos).size());
        System.out.println();
        pos = pos.withMove(new Move(Square.of("e2"), Square.of("e4")));
        System.out.println("После e2-e4:");
        System.out.println(pos);
        System.out.println("Шах белым? " + gen.isInCheck(pos, Color.WHITE));
        System.out.println();
        Board b1 = new Board();
        b1.set(Square.of("e1"), new King(Color.WHITE, Square.of("e1")));
        b1.set(Square.of("e8"), new King(Color.BLACK, Square.of("e8")));
        b1.set(Square.of("e5"), new Queen(Color.BLACK, Square.of("e5")));
        Position p1 = new Position(b1, Color.WHITE, CastleRights.initial(), null);
        System.out.println("Шах (Qe5)? " + gen.isInCheck(p1, Color.WHITE));
        System.out.println();
        Board b2 = new Board();
        b2.set(Square.of("e1"), new King(Color.WHITE, Square.of("e1")));
        b2.set(Square.of("h1"), new Rook(Color.WHITE, Square.of("h1")));
        b2.set(Square.of("e8"), new King(Color.BLACK, Square.of("e8")));
        Position p2 = new Position(b2, Color.WHITE, CastleRights.initial(), null);
        System.out.println("Рокировки белых:");
        java.util.List<Move> moves2 = gen.legalMoves(p2);
        for (int i = 0; i < moves2.size(); i++) {
            Move m = moves2.get(i);
            if (m.getFrom().equals(Square.of("e1"))
                    && Math.abs(m.getTo().getCol() - 4) == 2) {
                System.out.println("  " + m);
            }
        }
        System.out.println();
        Board b3 = new Board();
        b3.set(Square.of("e5"), new Pawn(Color.WHITE, Square.of("e5")));
        b3.set(Square.of("d5"), new Pawn(Color.BLACK, Square.of("d5")));
        b3.set(Square.of("e1"), new King(Color.WHITE, Square.of("e1")));
        b3.set(Square.of("e8"), new King(Color.BLACK, Square.of("e8")));
        Position p3 = new Position(b3, Color.WHITE, CastleRights.initial(), Square.of("d6"));
        System.out.println("Взятие на проходе:");
        java.util.List<Move> moves3 = gen.legalMoves(p3);
        for (int i = 0; i < moves3.size(); i++) {
            Move m = moves3.get(i);
            if (m.getFrom().equals(Square.of("e5")) && m.getTo().equals(Square.of("d6"))) {
                System.out.println("  " + m);
            }
        }
        System.out.println();
        Board b4 = new Board();
        b4.set(Square.of("e7"), new Pawn(Color.WHITE, Square.of("e7")));
        b4.set(Square.of("a1"), new King(Color.WHITE, Square.of("a1")));
        b4.set(Square.of("h8"), new King(Color.BLACK, Square.of("h8")));
        System.out.println("Превращение e7:");
        Piece pawn = b4.get(Square.of("e7"));
        java.util.List<Move> promos = pawn.pseudoLegalMoves(b4, null);
        for (int i = 0; i < promos.size(); i++) {
            Move m = promos.get(i);
            if (m.isPromotion()) {
                System.out.println("  " + m);
            }
        }
        System.out.println();
        try {
            gen.validateMove(Position.initial(),
                    new Move(Square.of("e2"), Square.of("e5")));
            System.out.println("ОШИБКА: ход должен быть запрещён");
        } catch (ChessException e) {
            System.out.println("Поймано: " + e.getMessage());
        }
    }
}