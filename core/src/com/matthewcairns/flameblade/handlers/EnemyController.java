package com.matthewcairns.flameblade.handlers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.matthewcairns.flameblade.Enemy;
import com.matthewcairns.flameblade.Player;


/**
 * Created by Matthew Cairns on 24/05/2014.
 * All rights reserved.
 */
public class EnemyController {
    BodyDef bodyDef;
    Body spawner;

    int hitsToDie = 6;
    int maxEnemies = 20;
    int numEnemies = 0;

    TextureAtlas atlas;
    TextureRegion spawnerImage;
    World world;
    Array<Enemy> enemies;
    float timeSinceLastSpawn;
    float spawnRate;
    Vector2 spawnLocation;
    Batch batch;

    boolean spawnEnemies = true;


    public EnemyController(Vector2 sl, World w, Batch b, float sr) {
        atlas = new TextureAtlas(Gdx.files.internal("elf_sprites.txt"));
        spawnerImage = atlas.findRegion("ghost_spawn", 0);
        enemies = new Array<Enemy>();
        timeSinceLastSpawn = 0.0f;
        spawnLocation = sl;
        world = w;
        batch = b;
        spawnRate = sr;


        bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(Utils.convertToBox(sl.x+16), Utils.convertToBox(sl.y+16));

        CircleShape shape = new CircleShape();
        shape.setRadius(Utils.convertToBox(16.0f));

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.filter.categoryBits = B2DVars.BIT_SPAWNER;

        spawner = world.createBody(bodyDef);
        spawner.createFixture(fixtureDef).setUserData("spawner");
        shape.dispose();
    }

    public void createNewEnemy() {
        if(spawnEnemies && numEnemies <= maxEnemies) {
            if (timeSinceLastSpawn > spawnRate) {
                enemies.add(new Enemy(spawnLocation.x, spawnLocation.y, world));
                timeSinceLastSpawn = 0.0f;
                numEnemies++;

            }
            timeSinceLastSpawn += Gdx.graphics.getDeltaTime();
        }
    }

    public void drawEnemies(Player player) {
        if(spawnEnemies)
            batch.draw(spawnerImage, spawnLocation.x, spawnLocation.y);
        for (Enemy e : enemies) {
            e.draw(batch);
            e.act(player.getBody());
        }

    }

    public void destroyEnemy(Body b, Array<Explosions> explosions) {
        Array<Enemy> copy = new Array<Enemy>(enemies.size);
        for(Enemy e : enemies) copy.add(e);
        for(Enemy e : copy) {
            if(b == e.getBody()) {
                enemies.removeValue(e, true);
                explosions.add(new Explosions(Utils.convertToWorld(b.getWorldCenter().x)-16,
                                              Utils.convertToWorld(b.getWorldCenter().y)-16,
                                              batch, 1));

                numEnemies--;


            }
        }
    }

    public Array<Enemy> getEnemies() {
        return enemies;
    }

    public void destroySelf() {
        spawnEnemies = false;
    }

    public int getHitsToDie() {
        return hitsToDie;
    }

    public Body getBody() {
        return spawner;
    }
}
