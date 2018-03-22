package com.mygdx.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;

public class Building extends Rectangle {

    private final Color color = new Color(1,1,1,1);


    public Building(float x, float y) {
        super(x, y, 300, 150);

    }

    public Color getColor() {
        return color;
    }
}
