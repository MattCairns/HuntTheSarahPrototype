package com.matthewcairns.flameblade;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

/**
 * Created by Matthew Cairns on 17/05/2014.
 * All rights reserved.
 */
public class Enemy {
    Texture ghostImage = new Texture(Gdx.files.internal("ghost.png"));
    Rectangle ghost;
    BodyDef bodyDef;
    Body enemy;
    float VELOCITY = 150.0f;

    public Enemy(float x, float y, World world) {
        bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(x, y);

        CircleShape shape = new CircleShape();
        Vector2 size = new Vector2(16, 16);
        shape.setRadius(16.0f);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 0.0f;
        fixtureDef.friction = 0.8f;

        enemy = world.createBody(bodyDef);
        enemy.createFixture(fixtureDef);
        shape.dispose();
        ghost = new Rectangle(x,y,32,32);

    }

    public void act(TiledMap map, Body player) {

        Vector2 playerLoc = new Vector2(player.getWorldCenter().x, player.getWorldCenter().y);
        Vector2 enemyLoc = new Vector2(ghost.getX(), ghost.getY());

        Vector2 direction = playerLoc.sub(enemyLoc);
        direction = direction.nor();

        ghost.x += (direction.x * VELOCITY) * Gdx.graphics.getDeltaTime();
        ghost.y += (direction.y * VELOCITY) * Gdx.graphics.getDeltaTime();

        enemy.applyForce(direction.scl(100,100), enemy.getWorldCenter(), true);

    }

    public void draw(Batch batch) {
        batch.draw(ghostImage, enemy.getWorldCenter().x-16, enemy.getWorldCenter().y-16);
    }

}
