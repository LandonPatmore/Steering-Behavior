package com.mygdx.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;

public class RoutePoint extends Vector2 {

    private final Color color = new Color(0,0,1,1);

    public RoutePoint(float x, float y) {
        super(x, y);
    }

    public Color getColor() {
        return color;
    }
}
