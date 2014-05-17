package com.matthewcairns.flameblade;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Matthew Cairns on 06/05/2014.
 * All rights reserved.
 */
public class MainGame implements Screen {
    final Flameblade game;
    SpriteBatch batch;
    OrthographicCamera camera;

    TiledMap tiledMap;
    TiledMapRenderer tiledMapRenderer;

    List<Bullet> bullets = new ArrayList<Bullet>();
    float bullet_time = 0.2f;
    float time_since_last_fire = 0.0f;

    Player player;

    Utils utils = new Utils();

    public MainGame(final Flameblade gam) {
        game = gam;

        player = new Player();
        batch = new SpriteBatch();

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);
        camera.update();

        tiledMap = new TmxMapLoader().load("testmap.tmx");
        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        time_since_last_fire += Gdx.graphics.getDeltaTime();

        camera.position.set(player.getRectangle().getX()+16, player.getRectangle().getY() +16, 0);
        camera.update();

        tiledMapRenderer.setView(camera);
        tiledMapRenderer.render();
        if (Gdx.input.isKeyPressed(Input.Keys.SPACE) && time_since_last_fire >= bullet_time) {
            bullets.add(new Bullet(player.getRectangle(), player.getFaceState()));
            time_since_last_fire = 0.0f;
        }

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        player.draw(batch);
        for(Bullet element : bullets) {
            element.draw(batch);
        }
        //Create a deep copy of all the bullets then iterate over them.
        List<Bullet> copy = new ArrayList<Bullet>(bullets.size());
        for(Bullet bullet : bullets) copy.add(bullet);
        for(Bullet bullet : copy) {
            //If the bullets collides with a wall then explode and remove from list.
            if(Utils.wallCollision(tiledMap, bullet.getRectangle())) {
                utils.explode(batch, bullet.getRectangle().getX(), bullet.getRectangle().getY());
                bullets.remove(bullet);
            }

        }
        batch.end();





        player.act(Gdx.graphics.getDeltaTime(), tiledMap);
    }

    @Override
    public void resume() {}
    @Override
    public void pause() {}
    @Override
    public void resize(int width, int height) {}
    @Override
    public void hide() {}
    @Override
    public void show() {}
    @Override
    public void dispose() {}
}
