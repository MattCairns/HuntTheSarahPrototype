package com.matthewcairns.flameblade;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.matthewcairns.flameblade.handlers.EnemyController;
import com.matthewcairns.flameblade.handlers.Explosions;
import com.matthewcairns.flameblade.handlers.MyContactListener;
import com.matthewcairns.flameblade.handlers.Utils;

import java.util.ArrayList;
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

    Array<Explosions> explosions = new Array<Explosions>();

    //Box 2D world for physics simulation
    World world;
    MyContactListener cl;

    Box2DDebugRenderer debugRenderer = new Box2DDebugRenderer();
    OrthographicCamera b2dCamera;

    Music music;

    Player player;
    Enemy enemy;
    EnemyController ec;

    public MainGame(final Flameblade gam) {
        game = gam;

        batch = new SpriteBatch();

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);
        camera.update();

        b2dCamera = new OrthographicCamera();
        b2dCamera.setToOrtho(false, Utils.convertToBox(800), Utils.convertToBox(480));
        b2dCamera.update();

        world = new World(new Vector2(0,0), true);
        cl = new MyContactListener();
        world.setContactListener(cl);

        tiledMap = new TmxMapLoader().load("testmap.tmx");
        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap);


        MapObjects objects = tiledMap.getLayers().get("spawns").getObjects();
        MapObject spawn = objects.get("spawn_point");
        Rectangle rect = ((RectangleMapObject)spawn).getRectangle();
        player = new Player(rect.getX(), rect.getY(), world);
        cl.getPlayer(player);

        wallBodies = Utils.wallCollisionShapes(tiledMap, world);

        MapObject enemy_spawn = objects.get("enemy_spawn_point");
        Rectangle enemy_spawn_rect = ((RectangleMapObject)enemy_spawn).getRectangle();
        System.out.println(enemy_spawn_rect.getX());
        ec = new EnemyController(new Vector2(enemy_spawn_rect.getX(), enemy_spawn_rect.getY()), world, batch, 0.5f);

        music = Gdx.audio.newMusic(Gdx.files.internal("music/swashingthebuck.ogg"));
        music.play();
    }

    private void removeBodiesToDelete() {
        Array<Body> bodies = cl.getBodies();
        for(Body b : bodies) {
            //Create a deep copy of all the bullets then iterate over them.
            List<Bullet> copy = new ArrayList<Bullet>(bullets.size());
            for(Bullet bullet : bullets) copy.add(bullet);
            for(Bullet bullet : copy) {
                //If the bullets collides with a wall then add to explosion list and remove from bullet list
                if(bullet.getBody() == b) {
                    bullets.remove(bullet);
                    explosions.add(new Explosions(Utils.convertToWorld(b.getWorldCenter().x)-16,
                            Utils.convertToWorld(b.getWorldCenter().y)-16,
                            batch, 0));
                }
            }

            ec.destroyEnemy(b, explosions);

            world.destroyBody(b);
            b.setUserData(null);
        }
        bodies.clear();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 1);
        //Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);



        time_since_last_fire += Gdx.graphics.getDeltaTime();

        //Update the camera for libgdx
        camera.position.set(Utils.convertToWorld(player.getBody().getWorldCenter().x),
                            Utils.convertToWorld(player.getBody().getWorldCenter().y), 0);
        camera.update();

        //Update the camera for box2d
        b2dCamera.position.set(player.getBody().getWorldCenter().x,
                               player.getBody().getWorldCenter().y, 0);
        b2dCamera.update();

        tiledMapRenderer.setView(camera);
        tiledMapRenderer.render();
        if (Gdx.input.isKeyPressed(Input.Keys.SPACE) && time_since_last_fire >= bullet_time) {
            bullets.add(new Bullet(player.getBody(), player.getFaceState(), world));
            time_since_last_fire = 0.0f;
        }

        ec.createNewEnemy();

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        player.draw(batch);
        ec.drawEnemies(player);
        for(Bullet element : bullets) {
            element.draw(batch);
        }

        Array<Explosions> copy = new Array<Explosions>();
        for(Explosions e : explosions) copy.add(e);
        for(Explosions e : copy) {
            e.explode();
            if(e.getFlaggedForRemoval()) {
                explosions.removeValue(e, true);
            }
        }

        batch.end();

        player.act();

        debugRenderer.render(world, b2dCamera.combined);
        world.step(1/60f, 6, 2);

        removeBodiesToDelete();

        if(player.getPlayerHealth() == 0.0f) {
            pause();
        }

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
