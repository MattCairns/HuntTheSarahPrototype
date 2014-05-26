package com.matthewcairns.flameblade;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.matthewcairns.flameblade.handlers.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Matthew Cairns on 06/05/2014.
 * All rights reserved.
 */
public class MainGame implements Screen {
    private final Flameblade game;
    private SpriteBatch batch;

    private OrthographicCamera camera;
    private OrthographicCamera b2dCamera;

    private TiledMap tiledMap;
    private TiledMapRenderer tiledMapRenderer;

    private Array<Body> wallBodies = new Array<Body>();

    private List<Bullet> bullets = new ArrayList<Bullet>();
    private float timeSinceLastFire = 0.0f;

    private Array<Explosions> explosions = new Array<Explosions>();

    //Box 2D world for physics simulation
    private World world;
    private Box2DDebugRenderer debugRenderer = new Box2DDebugRenderer();

    private MyContactListener cl;
    private AudioController audioController;

    private Player player;
    private Array<EnemyController> ec = new Array<EnemyController>();

    //Used for drawing ray casting lines
    private ShapeRenderer sr = new ShapeRenderer();
    private Boolean enableRaycasting = false;
    private float keyTime = 0.0f;

    public MainGame(final Flameblade gam, AudioController ac) {
        game = gam;

        batch = new SpriteBatch();

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);
        camera.update();

        tiledMap = new TmxMapLoader().load("testmap.tmx");
        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap);

        audioController = ac;

        startB2D();
    }

    private void startB2D() {
        world = new World(new Vector2(0,0), true);
        cl = new MyContactListener();
        world.setContactListener(cl);

        b2dCamera = new OrthographicCamera();
        b2dCamera.setToOrtho(false, Utils.convertToBox(800), Utils.convertToBox(480));
        b2dCamera.update();

        MapObjects objects = tiledMap.getLayers().get("spawns").getObjects();
        MapObject spawn = objects.get("spawn_point");
        Rectangle rect = ((RectangleMapObject)spawn).getRectangle();
        player = new Player(rect.getX(), rect.getY(), batch, world, audioController);
        cl.getPlayer(player);

        wallBodies = Utils.wallCollisionShapes(tiledMap, world);

        for(MapObject object :  tiledMap.getLayers().get("spawns").getObjects()) {
            if (object.getName().equals("enemy_spawn_point")) {
                Rectangle enemy_spawn_rect = ((RectangleMapObject) object).getRectangle();
                ec.add(new EnemyController(new Vector2(enemy_spawn_rect.getX(), enemy_spawn_rect.getY()), world, batch, 1.0f));
            }
        }

    }

    @Override
    public void render(float delta) {
        //Set background color
        Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 1);
        //Clear the screen
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //Set up the box2d and textures camera.
        initializeCameras();

        //Set the view of the tilemap to the camera and render
        tiledMapRenderer.setView(camera);
        tiledMapRenderer.render();

        fireBullet(); //Fire a bullet when the player hits the SPACE key

        for(EnemyController e : ec)
            e.createNewEnemy(); //Adds a new enemy to the world according the the EnemyController settings.

        player.act(); //Player movement logic

        drawBatch(); //Draws all the textures in the world

        //debugRenderer.render(world, b2dCamera.combined); //Enables drawing of box2d objects
        world.step(1/60f, 6, 2); //Steps the box2d world at 60 frames per second.

        removeBodiesToDelete(); //Removes any bodies that are queued for deletion

        enableRaycasting(); //Enables showing ray casting lines

        gameOver(); //If the players health reaches 0 return to main menu

    }

    private void initializeCameras() {
        //Update the camera for libgdx
        camera.position.set(Utils.convertToWorld(player.getBody().getWorldCenter().x),
                Utils.convertToWorld(player.getBody().getWorldCenter().y), 0);
        camera.update();

        //Update the camera for box2d
        b2dCamera.position.set(player.getBody().getWorldCenter().x,
                player.getBody().getWorldCenter().y, 0);
        b2dCamera.update();

    }

    private void drawBatch() {
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        player.draw();
        for(EnemyController e : ec)
            e.drawEnemies(player);
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
    }

    private void fireBullet() {
        float bullet_time = 0.2f;
        timeSinceLastFire += Gdx.graphics.getDeltaTime();
        if (Gdx.input.isKeyPressed(Input.Keys.SPACE) && timeSinceLastFire >= bullet_time) {
            bullets.add(new Bullet(player.getBody(), player.getFaceState(), world));
            timeSinceLastFire = 0.0f;
        }
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
            for(EnemyController e : ec) {
                e.destroyEnemy(b, explosions);
                if(e.getBody() == b) {
                    e.destroySelf();
                }
            }
            world.destroyBody(b);
            b.setUserData(null);
        }
        bodies.clear();
    }

    private void enableRaycasting() {
        //If the user hits the R button the keyboard raycasting lines are enabled
        if (Gdx.input.isKeyPressed(Input.Keys.R)) {
            if(keyTime > 0.5f) {
                keyTime = 0.0f;
                enableRaycasting = !enableRaycasting;
            }
        }
        keyTime += Gdx.graphics.getDeltaTime();

        if(enableRaycasting) {
            //TEMP
            sr.setProjectionMatrix(b2dCamera.combined);
            //Draw raycasts for enemies to player
            sr.begin(ShapeRenderer.ShapeType.Line);
            for (EnemyController e : ec)
                for (Enemy en : e.getEnemies())
                    en.drawRays(sr);
            sr.end();
        }
    }

    private void gameOver() {
        if(player.getPlayerHealth() == 0.0f) {
            this.dispose();
            game.setScreen(new MainMenuScreen(game));
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
    public void dispose() {
        world.dispose();

    }
}
