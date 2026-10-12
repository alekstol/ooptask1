package org.example;
public record CastleRights(boolean whiteKingSide,  boolean whiteQueenSide,
                           boolean blackKingSide,  boolean blackQueenSide) {
    public static CastleRights initial() { return new CastleRights(true, true, true, true); }
    public boolean canCastle(Color color, boolean kingSide) {
        return switch (color) {
            case WHITE -> kingSide ? whiteKingSide : whiteQueenSide;
            case BLACK -> kingSide ? blackKingSide : blackQueenSide;
        };
    }
    public CastleRights revoke(Color color, boolean kingSide) {
        return switch (color) {
            case WHITE -> kingSide
                    ? new CastleRights(false, whiteQueenSide, blackKingSide, blackQueenSide)
                    : new CastleRights(whiteKingSide, false, blackKingSide, blackQueenSide);
            case BLACK -> kingSide
                    ? new CastleRights(whiteKingSide, whiteQueenSide, false, blackQueenSide)
                    : new CastleRights(whiteKingSide, whiteQueenSide, blackKingSide, false);
        };
    }
    public CastleRights revokeAll(Color color) {
        return switch (color) {
            case WHITE -> new CastleRights(false, false, blackKingSide, blackQueenSide);
            case BLACK -> new CastleRights(whiteKingSide, whiteQueenSide, false, false);
        };
    }
}