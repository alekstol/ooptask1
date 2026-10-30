package org.example;
/**
 * Иммутабельные права на рокировку.
 *
 * <p>{@link #revoke} и {@link #revokeAll} возвращают <em>новый</em>
 * объект, не меняя текущий.
 */
public class CastleRights {
    private final boolean wks, wqs, bks, bqs;
    /**
     * @param wks белые — короткая
     * @param wqs белые — длинная
     * @param bks чёрные — короткая
     * @param bqs чёрные — длинная
     */
    public CastleRights(boolean wks, boolean wqs, boolean bks, boolean bqs) {
        this.wks = wks; this.wqs = wqs; this.bks = bks; this.bqs = bqs;
    }
    /** @return все права разрешены */
    public static CastleRights initial() { return new CastleRights(true, true, true, true); }
    /** @param color цвет @param kingSide {@code true} — короткая @return можно ли */
    public boolean canCastle(Color color, boolean kingSide) {
        if (color == Color.WHITE) return kingSide ? wks : wqs;
        return kingSide ? bks : bqs;
    }
    /** @return новый объект с отозванным правом */
    public CastleRights revoke(Color color, boolean kingSide) {
        boolean nwks = wks, nwqs = wqs, nbks = bks, nbqs = bqs;
        if (color == Color.WHITE) { if (kingSide) nwks = false; else nwqs = false; }
        else                      { if (kingSide) nbks = false; else nbqs = false; }
        return new CastleRights(nwks, nwqs, nbks, nbqs);
    }
    /** @return новый объект без прав цвета */
    public CastleRights revokeAll(Color color) {
        if (color == Color.WHITE) return new CastleRights(false, false, bks, bqs);
        return new CastleRights(wks, wqs, false, false);
    }
    /** @return белые — короткая */
    public boolean isWhiteKingSide()  { return wks; }
    /** @return белые — длинная */
    public boolean isWhiteQueenSide() { return wqs; }
    /** @return чёрные — короткая */
    public boolean isBlackKingSide()  { return bks; }
    /** @return чёрные — длинная */
    public boolean isBlackQueenSide() { return bqs; }
}