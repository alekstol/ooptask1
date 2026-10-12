package org.example;
public class Demo {
    public static void main(String[] args) {
        var gen = new MoveGenerator();
        System.out.println("Правила шахматных ходов");
        System.out.println();
        var pos = Position.initial();
        System.out.println("Начальная позиция:");
        System.out.println(pos);
        System.out.println("Все допустимые ходы белых");
        var moves = gen.legalMoves(pos);
        System.out.println("Всего: " + moves.size());
        moves.forEach(m -> System.out.println("  " + m.from() + " -> " + m.to()
                + (m.isPromotion() ? " (" + m.promotion() + ")" : "")));
        pos = pos.withMove(new Move(Square.of("e2"), Square.of("e4")));
        System.out.println();
        System.out.println("После e2-e4");
        System.out.println(pos);
        System.out.println("Шах белым в текущей позиции?");
        System.out.println(gen.isInCheck(pos, Color.WHITE));
        System.out.println();
        System.out.println("Позиция с шахом: чёрный ферзь e5 бьёт в белого короля e1");
        var checkBoard = new Board();
        checkBoard.set(Square.of("e1"), new King (Color.WHITE, Square.of("e1")));
        checkBoard.set(Square.of("e8"), new King (Color.BLACK, Square.of("e8")));
        checkBoard.set(Square.of("e5"), new Queen(Color.BLACK, Square.of("e5")));
        var checkPos = new Position(checkBoard, Color.WHITE, CastleRights.initial(), null);
        System.out.println("Шах белым? " + gen.isInCheck(checkPos, Color.WHITE));
        System.out.println();
        System.out.println("Рокировка: король e1, ладьи a1 и h1, поля свободны");
        var castleBoard = new Board();
        castleBoard.set(Square.of("e1"), new King (Color.WHITE, Square.of("e1")));
        castleBoard.set(Square.of("a1"), new Rook (Color.WHITE, Square.of("a1")));
        castleBoard.set(Square.of("h1"), new Rook (Color.WHITE, Square.of("h1")));
        castleBoard.set(Square.of("e8"), new King (Color.BLACK, Square.of("e8")));
        var castlePos = new Position(castleBoard, Color.WHITE, CastleRights.initial(), null);
        var castleMoves = gen.legalMoves(castlePos).stream()
                .filter(m -> m.from().equals(Square.of("e1")))
                .filter(m -> Math.abs(m.to().col() - 4) == 2)
                .toList();
        if (castleMoves.isEmpty()) {
            System.out.println("  Рокировка недоступна");
        } else {
            castleMoves.forEach(m -> System.out.println(
                    "  " + m.from() + " -> " + m.to()
                            + (m.to().col() == 6 ? "   (короткая)" : "   (длинная)")));
        }
        System.out.println();
        System.out.println("Рокировка под шахом запрещена");
        var castleCheckBoard = new Board();
        castleCheckBoard.set(Square.of("e1"), new King (Color.WHITE, Square.of("e1")));
        castleCheckBoard.set(Square.of("h1"), new Rook (Color.WHITE, Square.of("h1")));
        castleCheckBoard.set(Square.of("e8"), new King (Color.BLACK, Square.of("e8")));
        castleCheckBoard.set(Square.of("e5"), new Queen(Color.BLACK, Square.of("e5"))); // бьёт в e1
        var castleCheckPos = new Position(
                castleCheckBoard, Color.WHITE, CastleRights.initial(), null);
        var blocked = gen.legalMoves(castleCheckPos).stream()
                .filter(m -> m.from().equals(Square.of("e1")))
                .filter(m -> Math.abs(m.to().col() - 4) == 2)
                .toList();
        System.out.println(blocked.isEmpty()
                ? "  Рокировка недоступна: король под шахом"
                : "  ОШИБКА: рокировка не должна быть доступна");
        System.out.println();
        System.out.println("Взятие на проходе");
        var epBoard = new Board();
        epBoard.set(Square.of("e5"), new Pawn(Color.WHITE, Square.of("e5")));
        epBoard.set(Square.of("d5"), new Pawn(Color.BLACK, Square.of("d5")));
        epBoard.set(Square.of("e1"), new King(Color.WHITE, Square.of("e1")));
        epBoard.set(Square.of("e8"), new King(Color.BLACK, Square.of("e8")));
        var epPos = new Position(epBoard, Color.WHITE, CastleRights.initial(), Square.of("d6"));
        var epMoves = gen.legalMoves(epPos).stream()
                .filter(m -> m.from().equals(Square.of("e5")))
                .toList();
        epMoves.forEach(m -> System.out.println("  " + m.from() + " -> " + m.to()
                + (m.to().equals(Square.of("d6")) ? "   (взятие на проходе)" : "")));
        System.out.println();
        System.out.println("Превращение пешки e7");
        var promoBoard = new Board();
        promoBoard.set(Square.of("e7"), new Pawn(Color.WHITE, Square.of("e7")));
        promoBoard.set(Square.of("a1"), new King(Color.WHITE, Square.of("a1")));
        promoBoard.set(Square.of("h8"), new King(Color.BLACK, Square.of("h8")));
        var promoPos = new Position(promoBoard, Color.WHITE, CastleRights.initial(), null);
        var pawn = promoBoard.get(Square.of("e7"));
        pawn.pseudoLegalMoves(promoBoard, null).forEach(m ->
                System.out.println("  " + m.from() + " -> " + m.to()
                        + " (" + m.promotion() + ")"));
        System.out.println();
        System.out.println("Недопустимый ход e7 -> e4");
        try {
            gen.validateMove(pos, new Move(Square.of("e7"), Square.of("e4")));
            System.out.println("  ОШИБКА: исключение не выброшено");
        } catch (ChessException e) {
            System.out.println("  Поймано исключение: " + e.getMessage());
        }
        System.out.println();
        System.out.println("Ход, оставляющий короля под боем");
        var pinBoard = new Board();
        pinBoard.set(Square.of("e1"), new King (Color.WHITE, Square.of("e1")));
        pinBoard.set(Square.of("e8"), new King (Color.BLACK, Square.of("e8")));
        pinBoard.set(Square.of("e4"), new Rook (Color.WHITE, Square.of("e4"))); // связана по линии e
        pinBoard.set(Square.of("e2"), new Rook (Color.BLACK, Square.of("e2"))); // бьёт в e1
        var pinPos = new Position(pinBoard, Color.WHITE, CastleRights.initial(), null);
        var rook = pinBoard.get(Square.of("e4"));
        System.out.println("Псевдо-ходы ладьи e4: " + rook.pseudoLegalMoves(pinBoard, null).size());
        var legal = gen.legalMoves(pinPos).stream()
                .filter(m -> m.from().equals(Square.of("e4")))
                .toList();
        System.out.println("Реально допустимых ходов ладьи e4: " + legal.size());
        legal.forEach(m -> System.out.println("  " + m.from() + " -> " + m.to()));
        System.out.println();
    }
}