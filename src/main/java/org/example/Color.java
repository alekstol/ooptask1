package org.example;
public enum Color {
    WHITE {
        @Override public Color opposite() { return BLACK; }
        @Override public int direction() { return 1; }
    },
    BLACK {
        @Override public Color opposite() { return WHITE; }
        @Override public int direction() { return -1; }
    };
    public abstract Color opposite();
    public abstract int direction();
}