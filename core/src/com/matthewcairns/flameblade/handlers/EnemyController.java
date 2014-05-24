package com.matthewcairns.flameblade.handlers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.matthewcairns.flameblade.Bullet;
import com.matthewcairns.flameblade.Enemy;
import com.matthewcairns.flameblade.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Matthew Cairns on 24/05/2014.
 * All rights reserved.
 */
public class EnemyController {
    World world;
    Array<Enemy> enemies;
    float timeSinceLastSpawn;
    Vector2 spawnLocation;
    Batch batch;

    public EnemyController(Vector2 sl, World w, Batch b) {
        enemies = new Array<Enemy>();
        timeSinceLastSpawn = 0.0f;
        spawnLocation = sl;
        world = w;
        batch = b;
    }

    public void createNewEnemy() {
        if(timeSinceLastSpawn > 1.0f) {
            enemies.add(new Enemy(spawnLocation.x, spawnLocation.y, world));
            timeSinceLastSpawn = 0.0f;
        }
        timeSinceLastSpawn += Gdx.graphics.getDeltaTime();
    }

    public void drawEnemies(Player player) {
        for(Enemy e : enemies) {
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

            }
        }
    }
}
