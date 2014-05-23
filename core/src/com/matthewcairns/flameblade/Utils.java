package com.matthewcairns.flameblade;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.utils.Array;

import java.awt.*;

/**
 * Created by Matthew Cairns on 17/05/2014.
 * All rights reserved.
 */
public class Utils {
    TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("elf_sprites.txt"));
    Animation explode;
    float stateTime = 0.0f;

    static float WORLD_TO_BOX = 0.03125f;
    static float BOX_TO_WORLD = 32.0f;

    public static float convertToBox(float x) {
        return x / BOX_TO_WORLD;
    }
    public static float convertToWorld(float x) {
        return x * BOX_TO_WORLD;
    }

    public Utils() {
        TextureRegion[] explodeFrames = new TextureRegion[4];
        explodeFrames[0] = atlas.findRegion("small_explosion_one");
        explodeFrames[1] = atlas.findRegion("small_explosion_two");
        explodeFrames[2] = atlas.findRegion("small_explosion_three");
        explodeFrames[3] = atlas.findRegion("small_explosion_four");
        explode = new Animation(0.2f, explodeFrames);
    }

    //Checks if there has been a collision between the map walls and any object passed to the function.
    //Returns true if there is a collision.
    public static Array<Body> wallCollisionShapes(TiledMap map, World world) {
        Array<Body> bodies = new Array<Body>();

        for(MapObject object :  map.getLayers().get("Collide").getObjects()) {

            Shape shape;
            FixtureDef fixtureDef = new FixtureDef();

            shape = getRectangle((RectangleMapObject)object);

            BodyDef bd = new BodyDef();
            bd.type = BodyDef.BodyType.StaticBody;
            Rectangle rec = ((RectangleMapObject) object).getRectangle();
            //bd.position.set(rec.getX(), rec.getY());
            fixtureDef.shape = shape;
            fixtureDef.filter.categoryBits = B2DVars.BIT_WALLS;
            fixtureDef.filter.maskBits = B2DVars.BIT_WALLS | B2DVars.BIT_BULLETS | B2DVars.BIT_PLAYER | B2DVars.BIT_ENEMIES;

            Body body = world.createBody(bd);
            body.createFixture(fixtureDef);
            bodies.add(body);
            shape.dispose();

        }
        return bodies;
    }

    private static PolygonShape getRectangle(RectangleMapObject rectangleObject) {
        Rectangle rectangle = rectangleObject.getRectangle();
        PolygonShape polygon = new PolygonShape();
        Vector2 size = new Vector2((rectangle.x + rectangle.width * 0.5f) * WORLD_TO_BOX ,
                                   (rectangle.y + rectangle.height *0.5f) * WORLD_TO_BOX);
        polygon.setAsBox(rectangle.width * 0.5f * WORLD_TO_BOX,
                         rectangle.height * 0.5f * WORLD_TO_BOX,
                         size, 0.0f);
        return polygon;
    }

    //Draws a simple explosion animation at the x and y location specified.
    public void explode(Batch batch, float x, float y) {
        stateTime += Gdx.graphics.getDeltaTime();
        System.out.println(stateTime);
        batch.draw(explode.getKeyFrame(stateTime, true), x, y);
    }


    public static Animation createAnimation(TextureAtlas atlas, String[] regions, float frameDuration) {
        TextureRegion[] frames = new TextureRegion[regions.length];
        for(int i = 0; i < frames.length; i++) {
            frames[i] = atlas.findRegion(regions[i]);
        }
        return new Animation(frameDuration, frames);
    }
}
