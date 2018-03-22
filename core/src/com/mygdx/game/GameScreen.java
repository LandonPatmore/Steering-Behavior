package com.mygdx.game;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class GameScreen implements Screen {

    private ShapeRenderer shapeRenderer;
    private Vehicle vehicle;
    private Player player;
    private Building building;

    private OrthographicCamera camera;
    private Game game;

    public GameScreen(Game game) {
        this.game = game;
    }

    private void handleInput() {
        if(Gdx.input.isKeyPressed(Input.Keys.UP) && Gdx.input.isKeyPressed(Input.Keys.LEFT)){
            player.setPosition(player.getPosition().x - 2, player.getPosition().y + 2);
        } else if(Gdx.input.isKeyPressed(Input.Keys.UP) && Gdx.input.isKeyPressed(Input.Keys.RIGHT)){
            player.setPosition(player.getPosition().x + 2, player.getPosition().y + 2);
        } else if(Gdx.input.isKeyPressed(Input.Keys.DOWN) && Gdx.input.isKeyPressed(Input.Keys.LEFT)){
            player.setPosition(player.getPosition().x - 2, player.getPosition().y - 2);
        } else if(Gdx.input.isKeyPressed(Input.Keys.DOWN) && Gdx.input.isKeyPressed(Input.Keys.RIGHT)){
            player.setPosition(player.getPosition().x + 2, player.getPosition().y - 2);
        } else if(Gdx.input.isKeyPressed(Input.Keys.UP)){
            player.setPosition(player.getPosition().x, player.getPosition().y + 2);
        } else if(Gdx.input.isKeyPressed(Input.Keys.DOWN)){
            player.setPosition(player.getPosition().x, player.getPosition().y - 2);
        } else if(Gdx.input.isKeyPressed(Input.Keys.LEFT)){
            player.setPosition(player.getPosition().x - 2, player.getPosition().y);
        } else if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)){
            player.setPosition(player.getPosition().x + 2, player.getPosition().y);
        }
    }

    @Override
    public void show() {
        shapeRenderer = new ShapeRenderer();
        vehicle = new Vehicle();
        player = new Player(500, 500);

        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        building = new Building(Gdx.graphics.getWidth() / 2 - 300 / 2, Gdx.graphics.getHeight() / 2 - 150 / 2);
    }

    @Override
    public void render(float delta) {
        checkGameOver();
        handleInput();
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        shapeRenderer.setProjectionMatrix(camera.combined);

        renderPoints();
        renderBuilding();
        renderVehicle();
        renderPlayer();

        vehicle.state(player.getPosition());
        checkBuildingCollision();

    }

    private void checkGameOver() {
        if ((Math.abs(Math.floor(vehicle.getPosition().x) - Math.floor(player.getPosition().x)) <= vehicle.getR()) && (Math.abs(Math.floor(vehicle.getPosition().y) - Math.floor(player.getPosition().y)) <= vehicle.getR())) {
            game.setScreen(new EndScreen(game));
        }
    }

    private void checkBuildingCollision() {
        if (vehicle.overlaps(building)) {
            VectorHelper.negate(vehicle.getVelocity());
        }
    }

    private void renderPoints() {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        for (RoutePoint r : vehicle.getRoutePoints()) {
            shapeRenderer.setColor(r.getColor());
            shapeRenderer.circle(r.x, r.y, 5);
        }
        shapeRenderer.end();
    }

    private void renderVehicle() {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        shapeRenderer.setColor(vehicle.getVisionColor());
        shapeRenderer.arc(vehicle.getPosition().x, vehicle.getPosition().y, vehicle.getVisionRadius(), vehicle.looking(), vehicle.getVision());
        shapeRenderer.setColor(1, 0, 0, 1);
        shapeRenderer.circle(vehicle.getPosition().x, vehicle.getPosition().y, vehicle.getR());
        shapeRenderer.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);
    }

    private void renderPlayer() {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(0, 1, 0, 1);
        shapeRenderer.circle(player.getPosition().x, player.getPosition().y, 5);
        shapeRenderer.end();
    }

    private void renderBuilding() {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(building.getColor());
        shapeRenderer.rect(building.x, building.y, building.width, building.height);
        shapeRenderer.end();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        shapeRenderer.dispose();
    }
}
