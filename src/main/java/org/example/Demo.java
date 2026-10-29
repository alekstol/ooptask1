package org.example;

public class Demo {
    public static void main(String[] args) {
        var gen = new MoveGenerator();
        var pos = Position.initial();
        System.out.println("Начальная позиция:\n" + pos);
        System.out.println("Легальных ходов белых: " + gen.legalMoves(pos).size() + "\n");
        pos = pos.withMove(new Move(Square.of("e2"), Square.of("e4")));
        System.out.println("После e2-e4:\n" + pos);
        System.out.println("Шах белым? " + gen.isInCheck(pos, Color.WHITE) + "\n");
        var b1 = new Board();
        b1.set(Square.of("e1"), new King (Color.WHITE, Square.of("e1")));
        b1.set(Square.of("e8"), new King (Color.BLACK, Square.of("e8")));
        b1.set(Square.of("e5"), new Queen(Color.BLACK, Square.of("e5")));
        System.out.println("Шах (Qe5)? " + gen.isInCheck(
                new Position(b1, Color.WHITE, CastleRights.initial(), null), Color.WHITE) + "\n");
        var b2 = new Board();
        b2.set(Square.of("e1"), new King (Color.WHITE, Square.of("e1")));
        b2.set(Square.of("h1"), new Rook (Color.WHITE, Square.of("h1")));
        b2.set(Square.of("e8"), new King (Color.BLACK, Square.of("e8")));
        var castlePos = new Position(b2, Color.WHITE, CastleRights.initial(), null);
        System.out.println("Рокировки белых:");
        gen.legalMoves(castlePos).stream()
                .filter(m -> m.from().equals(Square.of("e1"))
                        && Math.abs(m.to().col() - 4) == 2)
                .forEach(m -> System.out.println("  " + m));
        var b3 = new Board();
        b3.set(Square.of("e5"), new Pawn(Color.WHITE, Square.of("e5")));
        b3.set(Square.of("d5"), new Pawn(Color.BLACK, Square.of("d5")));
        b3.set(Square.of("e1"), new King(Color.WHITE, Square.of("e1")));
        b3.set(Square.of("e8"), new King(Color.BLACK, Square.of("e8")));
        var epPos = new Position(b3, Color.WHITE, CastleRights.initial(), Square.of("d6"));
        System.out.println("\nВзятие на проходе:");
        gen.legalMoves(epPos).stream()
                .filter(m -> m.from().equals(Square.of("e5")) && m.to().equals(Square.of("d6")))
                .forEach(m -> System.out.println("  " + m));
        var b4 = new Board();
        b4.set(Square.of("e7"), new Pawn(Color.WHITE, Square.of("e7")));
        b4.set(Square.of("a1"), new King(Color.WHITE, Square.of("a1")));
        b4.set(Square.of("h8"), new King(Color.BLACK, Square.of("h8")));
        System.out.println("\nПревращение e7:");
        b4.get(Square.of("e7")).pseudoLegalMoves(b4, null).forEach(m ->
                System.out.println("  " + m.from() + "->" + m.to() + " (" + m.promotion() + ")"));
        System.out.println();
        try {
            gen.validateMove(Position.initial(), new Move(Square.of("e2"), Square.of("e5")));
        } catch (ChessException e) {
            System.out.println("Поймано: " + e.getMessage());
        }
    }
}