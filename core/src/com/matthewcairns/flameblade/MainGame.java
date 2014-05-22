package com.matthewcairns.flameblade;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.CircleMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;

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

    Array<Body> wallBodies = new Array<Body>();

    List<Bullet> bullets = new ArrayList<Bullet>();
    float bullet_time = 0.2f;
    float time_since_last_fire = 0.0f;

    //Box 2D world for physics simulation
    World world = new World(new Vector2(0,0), true);
    Box2DDebugRenderer debugRenderer = new Box2DDebugRenderer();
    static final float WORLD_TO_BOX = 0.01f;
    static final float BOX_TO_WORLD = 100.0f;

    Player player;
    Enemy enemy;

    Utils utils = new Utils();

    public MainGame(final Flameblade gam) {
        game = gam;

        batch = new SpriteBatch();

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);
        camera.update();

        tiledMap = new TmxMapLoader().load("testmap.tmx");
        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap);


        MapObjects objects = tiledMap.getLayers().get("spawns").getObjects();
        MapObject spawn = objects.get("spawn_point");
        Rectangle rect = ((RectangleMapObject)spawn).getRectangle();

        wallBodies = Utils.wallCollisionShapes(tiledMap, 32.0f, world);

        player = new Player(rect.getX(), rect.getY(), world);
        enemy = new Enemy(300, 230, world);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        time_since_last_fire += Gdx.graphics.getDeltaTime();

        camera.position.set(player.getBody().getPosition().x +16, player.getBody().getPosition().y +16, 0);
        camera.update();

        tiledMapRenderer.setView(camera);
        tiledMapRenderer.render();
        if (Gdx.input.isKeyPressed(Input.Keys.SPACE) && time_since_last_fire >= bullet_time) {
            bullets.add(new Bullet(player.getBody(), player.getFaceState(), world));
            time_since_last_fire = 0.0f;
        }

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        player.draw(batch);
        enemy.draw(batch);
//        for(Bullet element : bullets) {
//            element.draw(batch);
//        }
//        //Create a deep copy of all the bullets then iterate over them.
//        List<Bullet> copy = new ArrayList<Bullet>(bullets.size());
//        for(Bullet bullet : bullets) copy.add(bullet);
//        for(Bullet bullet : copy) {
//            //If the bullets collides with a wall then explode and remove from list.
//            if(Utils.wallCollision(tiledMap, bullet.getRectangle())) {
//                utils.explode(batch, bullet.getRectangle().getX(), bullet.getRectangle().getY());
//                bullets.remove(bullet);
//            }
//
//        }
        batch.end();

        player.act(Gdx.graphics.getDeltaTime(), tiledMap);
        enemy.act(tiledMap, player.getBody());

        debugRenderer.render(world, camera.combined);
        world.step(1/60f, 6, 2);

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
