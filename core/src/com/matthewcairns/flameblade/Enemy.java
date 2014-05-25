package com.matthewcairns.flameblade;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.matthewcairns.flameblade.handlers.B2DVars;
import com.matthewcairns.flameblade.handlers.Utils;

/**
 * Created by Matthew Cairns on 17/05/2014.
 * All rights reserved.
 */
public class Enemy {
    Texture ghostImage = new Texture(Gdx.files.internal("sarah_enemy.png"));
    BodyDef bodyDef;
    Body enemy;
    Body player;
    World world;

    Vector2 enemyLoc = new Vector2(), playerLoc = new Vector2();
    Vector2 collision = new Vector2(), enemynormal = new Vector2();
    Vector2 origin;

    boolean enemy_chase = false;

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

        origin = new Vector2(x, y);

        this.world = world;

    }

    public void act(Body p) {

        player = p;

        playerLoc.set(player.getWorldCenter());

        enemyLoc.set(enemy.getWorldCenter());

        Vector2 direction = playerLoc.sub(enemyLoc);
        direction = direction.nor();

        RayCastCallback callback = new RayCastCallback() {
            @Override
            public float reportRayFixture(Fixture fixture, Vector2 point, Vector2 normal, float fraction) {
                if(fixture.getUserData().equals("player")) {
                    enemy_chase = true;
                }
                else {
                    enemy_chase = false;
                }


                collision.set(point);
                enemynormal.set(normal);
                return fraction;
            }
        };

        world.rayCast(callback, enemy.getWorldCenter(), player.getWorldCenter());

        if(enemy_chase) {
            System.out.println("Chasing player");
            enemy.applyForce(direction.scl(10, 10), enemy.getWorldCenter(), true);
        }
        else {
            enemy.applyForce(direction.scl(10, 10), origin, true);

        }

    }

    public void draw(Batch batch) {
        batch.draw(ghostImage, Utils.convertToWorld(enemy.getWorldCenter().x)-11, Utils.convertToWorld(enemy.getWorldCenter().y)-16);



    }

    public void drawRays(ShapeRenderer sr) {
        sr.line(enemy.getWorldCenter(), collision);
    }

    public Body getBody() {
        return enemy;
    }

}
