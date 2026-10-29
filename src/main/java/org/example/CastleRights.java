package org.example;
public class CastleRights {
    private boolean whiteKingSide;
    private boolean whiteQueenSide;
    private boolean blackKingSide;
    private boolean blackQueenSide;
    public CastleRights(boolean whiteKingSide,
                        boolean whiteQueenSide,
                        boolean blackKingSide,
                        boolean blackQueenSide) {
        this.whiteKingSide = whiteKingSide;
        this.whiteQueenSide = whiteQueenSide;
        this.blackKingSide = blackKingSide;
        this.blackQueenSide = blackQueenSide;
    }
    public static CastleRights initial() {
        return new CastleRights(true, true, true, true);
    }
    public boolean canCastle(Color color, boolean kingSide) {
        if (color == Color.WHITE) {
            if (kingSide) {
                return whiteKingSide;
            } else {
                return whiteQueenSide;
            }
        } else {
            if (kingSide) {
                return blackKingSide;
            } else {
                return blackQueenSide;
            }
        }
    }
    public CastleRights revoke(Color color, boolean kingSide) {
        boolean wk = whiteKingSide;
        boolean wq = whiteQueenSide;
        boolean bk = blackKingSide;
        boolean bq = blackQueenSide;

        if (color == Color.WHITE) {
            if (kingSide) {
                wk = false;
            } else {
                wq = false;
            }
        } else {
            if (kingSide) {
                bk = false;
            } else {
                bq = false;
            }
        }
        return new CastleRights(wk, wq, bk, bq);
    }
    public CastleRights revokeAll(Color color) {
        if (color == Color.WHITE) {
            return new CastleRights(false, false, blackKingSide, blackQueenSide);
        } else {
            return new CastleRights(whiteKingSide, whiteQueenSide, false, false);
        }
    }
    public boolean isWhiteKingSide()  { return whiteKingSide; }
    public boolean isWhiteQueenSide() { return whiteQueenSide; }
    public boolean isBlackKingSide()  { return blackKingSide; }
    public boolean isBlackQueenSide() { return blackQueenSide; }
}