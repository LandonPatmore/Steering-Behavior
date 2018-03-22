package com.mygdx.game;

import com.badlogic.gdx.math.Vector2;

public class Player {

    private Vector2 position;

    public Player(float x, float y) {
        position = new Vector2(x, y);
    }

    public Vector2 getPosition() {
        return position;
    }

    public void setPosition(float x, float y) {
        this.position.set(x,y);
    }
}
