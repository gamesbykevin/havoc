package com.gamesbykevin.havoc.util;

public class Slope {

    public static float solveX(float m, float y, float b) {
        return ((y - b) / m);
    }

    public static float solveY(float m, float x, float b) {
        return ((m * x) + b);
    }

    public static float getYintercept(float x, float y, float m) {
        return y + -(m * x);
    }

    public static float getSlope(float x1, float y1, float x2, float y2) {
        return ((y2 - y1) / (x2 - x1));
    }
}