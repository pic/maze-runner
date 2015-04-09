package com.simplificator.gdx;

import com.badlogic.gdx.math.Vector2;

public class RotMatrix {

    double sin;
    double cos;

    public RotMatrix(double angle) {
        sin = Math.sin(angle);
        cos = Math.cos(angle);
    }

    public float getX(Vector2 point) {
        return (float) (cos * point.x - sin * point.y);
    }

    public float getZ(Vector2 point) {
        return (float) (sin * point.x + cos * point.y);
    }
}
