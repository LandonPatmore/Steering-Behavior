package com.mygdx.game;

import com.badlogic.gdx.Game;

public class GuardGame extends Game {
    @Override
    public void create() {
        setScreen(new GameScreen(this));
    }
}
