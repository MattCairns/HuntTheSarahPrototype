package com.matthewcairns.flameblade;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by Matthew Cairns on 17/05/2014.
 * All rights reserved.
 */
public class Enemy {
    Texture ghostImage = new Texture(Gdx.files.internal("ghost.png"));
    Rectangle ghost;
    float VELOCITY = 150.0f;

    public Enemy(float x, float y) {
        ghost = new Rectangle(x,y,32,32);

    }

    public void act(TiledMap map, Rectangle player) {
        Vector2 playerLoc = new Vector2(player.getX(), player.getY());
        Vector2 enemyLoc = new Vector2(ghost.getX(), ghost.getY());

        Vector2 direction = playerLoc.sub(enemyLoc);
        direction = direction.nor();

        ghost.x += (direction.x * VELOCITY) * Gdx.graphics.getDeltaTime();
        ghost.y += (direction.y * VELOCITY) * Gdx.graphics.getDeltaTime();

        if(Utils.wallCollision(map, ghost)) {
            ghost.x = enemyLoc.x;
            ghost.y = enemyLoc.y;
        }
    }

    public void draw(Batch batch) {
        batch.draw(ghostImage, ghost.getX(), ghost.getY());
    }

}
