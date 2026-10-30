package org.example;
import java.util.List;
/**
 * Демонстрация: мат/пат, ничьи (троекратное повторение, 50 ходов),
 * запись SAN и обратный разбор с воспроизведением всех позиций,
 * инварианты {@link Position}, контракты equals/hashCode.
 */
public class Demo {
    public static void main(String[] args) {
        MoveGenerator gen = new MoveGenerator();
        // ===== 1. Начальная позиция и инварианты =====
        Position start = Position.initial();
        System.out.println("=== 1. Начальная позиция (32 фигуры) ===");
        System.out.println(start);
        System.out.println("Инварианты:");
        System.out.println("  board != null: " + (start.getBoard() != null));
        System.out.println("  turnToMove: " + start.getTurnToMove());
        System.out.println("  enPassantTarget: " + start.getEnPassantTarget());
        System.out.println("  halfmoveClock: " + start.getHalfmoveClock());
        System.out.println("  fullmoveNumber: " + start.getFullmoveNumber());
        System.out.println("  history.size: " + start.getHistory().size());
        System.out.println();
        // ===== 2. Мат: дурацкий мат =====
        System.out.println("=== 2. Мат (дурацкий мат) ===");
        Position fool = start;
        for (Move m : Notation.parse(fool, "1. f3 e5 2. g4 Qh4#", gen)) {
            fool = fool.withMove(m);
        }
        System.out.println(fool);
        System.out.println("isCheckmate: " + gen.isCheckmate(fool));
        System.out.println("isStalemate: " + gen.isStalemate(fool));
        System.out.println("result: " + gen.result(fool));
        System.out.println();
        // ===== 3. Пат =====
        System.out.println("=== 3. Пат ===");
        Board sb = new Board();
        sb.set(Square.of("a8"), new King(Color.BLACK, Square.of("a8")));
        sb.set(Square.of("c6"), new King(Color.WHITE, Square.of("c6")));
        sb.set(Square.of("b6"), new Queen(Color.WHITE, Square.of("b6")));
        Position sp = new Position(sb, Color.BLACK, CastleRights.initial(), null);
        System.out.println(sp);
        System.out.println("isInCheck(black): " + gen.isInCheck(sp, Color.BLACK));
        System.out.println("isStalemate: " + gen.isStalemate(sp));
        System.out.println("result: " + gen.result(sp));
        System.out.println();
        // ===== 4. Ничья по 50 ходам =====
        System.out.println("=== 4. Правило 50 ходов ===");
        Position fifty = start;
        String shuffle =
                "Nf3 Nf6 Ng1 Ng8 Nf3 Nf6 Ng1 Ng8 Nf3 Nf6 Ng1 Ng8 Nf3 Nf6 Ng1 Ng8 "
                        + "Nf3 Nf6 Ng1 Ng8 Nf3 Nf6 Ng1 Ng8 Nf3 Nf6 Ng1 Ng8 Nf3 Nf6 Ng1 Ng8 "
                        + "Nf3 Nf6 Ng1 Ng8 Nf3 Nf6 Ng1 Ng8 Nf3 Nf6 Ng1 Ng8 Nf3 Nf6 Ng1 Ng8 "
                        + "Nf3 Nf6 Ng1 Ng8 Nf3 Nf6 Ng1 Ng8 Nf3 Nf6 Ng1 Ng8 Nf3 Nf6 Ng1 Ng8 "
                        + "Nf3 Nf6 Ng1 Ng8 Nf3 Nf6 Ng1 Ng8 Nf3 Nf6 Ng1 Ng8 Nf3 Nf6 Ng1 Ng8 "
                        + "Nf3 Nf6 Ng1 Ng8 Nf3 Nf6 Ng1 Ng8 Nf3 Nf6 Ng1 Ng8 Nf3 Nf6 Ng1 Ng8 "
                        + "Nf3 Nf6 Ng1 Ng8 Nf3 Nf6 Ng1 Ng8 Nf3 Nf6 Ng1 Ng8 Nf3 Nf6 Ng1 Ng8 "
                        + "Nf3 Nf6 Ng1 Ng8 Nf3 Nf6 Ng1 Ng8 Nf3 Nf6 Ng1 Ng8 Nf3 Nf6 Ng1 Ng8 "
                        + "Nf3 Nf6 Ng1 Ng8 Nf3 Nf6 Ng1 Ng8 Nf3 Nf6 Ng1 Ng8 Nf3 Nf6 Ng1 Ng8 "
                        + "Nf3 Nf6 Ng1 Ng8 Nf3 Nf6 Ng1 Ng8 Nf3 Nf6 Ng1 Ng8 Nf3 Nf6 Ng1 Ng8 "
                        + "Nf3 Nf6 Ng1 Ng8 Nf3 Nf6 Ng1 Ng8 Nf3 Nf6 Ng1 Ng8 Nf3 Nf6 Ng1 Ng8 "
                        + "Nf3 Nf6 Ng1 Ng8 Nf3 Nf6 Ng1 Ng8 Nf3 Nf6 Ng1 Ng8 Nf3 Nf6 Ng1 Ng8 "
                        + "Nf3 Nf6 Ng1 Ng8";
        for (Move m : Notation.parse(fifty, shuffle, gen)) fifty = fifty.withMove(m);
        System.out.println("halfmoveClock: " + fifty.getHalfmoveClock());
        System.out.println("isFiftyMoveRule: " + gen.isFiftyMoveRule(fifty));
        System.out.println("result: " + gen.result(fifty));
        System.out.println();
        // ===== 5. Троекратное повторение =====
        System.out.println("=== 5. Троекратное повторение ===");
        Position rep = start;
        for (Move m : Notation.parse(rep, "Nf3 Nf6 Ng1 Ng8 Nf3 Nf6 Ng1 Ng8", gen)) {
            rep = rep.withMove(m);
        }
        System.out.println("history.size: " + rep.getHistory().size());
        System.out.println("isThreefoldRepetition: " + gen.isThreefoldRepetition(rep));
        System.out.println("result: " + gen.result(rep));
        System.out.println();
        // ===== 6. Запись SAN =====
        System.out.println("=== 6. Запись SAN ===");
        String san = "1. e4 e5 2. Nf3 Nc6 3. Bb5 a6";
        Position game = start;
        List<Move> moves = Notation.parse(game, san, gen);
        StringBuilder rec = new StringBuilder();
        for (int i = 0; i < moves.size(); i++) {
            Move m = moves.get(i);
            String s = Notation.toSan(game, m, game.withMove(m), gen);
            if (i % 2 == 0) rec.append((i / 2 + 1)).append(". ");
            rec.append(s).append(' ');
            game = game.withMove(m);
        }
        System.out.println("Исходная SAN:  " + san);
        System.out.println("Записано SAN:  " + rec.toString().trim());
        System.out.println("Совпадает:     " + san.equals(rec.toString().trim()));
        System.out.println();
        // ===== 7. Replay всех позиций =====
        System.out.println("=== 7. Replay всех позиций ===");
        List<Position> positions = Notation.replay(start, san, gen);
        System.out.println("Позиций (включая начальную): " + positions.size());
        for (int i = 0; i < positions.size(); i++) {
            Position p = positions.get(i);
            System.out.println("--- #" + i + " (ход: " + p.getTurnToMove()
                    + ", halfmove=" + p.getHalfmoveClock()
                    + ", fullmove=" + p.getFullmoveNumber() + ") ---");
            System.out.println(p);
        }
        // ===== 8. Контракты equals/hashCode =====
        System.out.println("=== 8. Контракты equals/hashCode ===");
        Move m1 = new Move(Square.of("e2"), Square.of("e4"));
        Move m2 = new Move(Square.of("e2"), Square.of("e4"));
        System.out.println("Move equals: " + m1.equals(m2)
                + ", hash equal: " + (m1.hashCode() == m2.hashCode()));
        System.out.println("PositionKey(start) == PositionKey(start): "
                + PositionKey.of(start).equals(PositionKey.of(Position.initial())));
        System.out.println();
        // ===== 9. Иммутабельность Position =====
        System.out.println("=== 9. Иммутабельность Position ===");
        Position afterE4 = start.withMove(m1);
        System.out.println("start.turnToMove: " + start.getTurnToMove());
        System.out.println("afterE4.turnToMove: " + afterE4.getTurnToMove());
        System.out.println("start.history.size: " + start.getHistory().size());
        System.out.println("afterE4.history.size: " + afterE4.getHistory().size());
        System.out.println("start.board == afterE4.board: "
                + (start.getBoard() == afterE4.getBoard()));
        System.out.println();
        System.out.println("=== Готово ===");
    }
}