package com.matthewcairns.flameblade;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.matthewcairns.flameblade.handlers.B2DVars;
import com.matthewcairns.flameblade.handlers.Utils;

/**
 * Created by Matthew Cairns on 17/05/2014.
 * All rights reserved.
 */
public class Enemy {
    Texture ghostImage = new Texture(Gdx.files.internal("ghost.png"));
    BodyDef bodyDef;
    Body enemy;

    public Enemy(float x, float y, World world) {
        bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(Utils.convertToBox(x), Utils.convertToBox(y));

        CircleShape shape = new CircleShape();
        shape.setRadius(Utils.convertToBox(16.0f));

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 5f;
        fixtureDef.friction = 0.8f;
        fixtureDef.filter.categoryBits = B2DVars.BIT_ENEMIES;
        fixtureDef.filter.maskBits = B2DVars.BIT_WALLS | B2DVars.BIT_BULLETS | B2DVars.BIT_PLAYER;

        enemy = world.createBody(bodyDef);
        enemy.createFixture(fixtureDef).setUserData("enemy");
        shape.dispose();

    }

    public void act(Body player) {

        Vector2 playerLoc = new Vector2(player.getWorldCenter());
        Vector2 enemyLoc = new Vector2(enemy.getWorldCenter());

        Vector2 direction = playerLoc.sub(enemyLoc);
        direction = direction.nor();

        enemy.applyForce(direction.scl(10,10), enemy.getWorldCenter(), true);

    }

    public void draw(Batch batch) {
        batch.draw(ghostImage, Utils.convertToWorld(enemy.getWorldCenter().x)-16, Utils.convertToWorld(enemy.getWorldCenter().y)-16);
    }

}
