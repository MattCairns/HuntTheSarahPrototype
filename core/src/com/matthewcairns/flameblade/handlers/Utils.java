package com.matthewcairns.flameblade.handlers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.utils.Array;

/**
 * Created by Matthew Cairns on 17/05/2014.
 * All rights reserved.
 */
public class Utils {


    static float WORLD_TO_BOX = 0.03125f;
    static float BOX_TO_WORLD = 32.0f;

    public static float convertToBox(float value) {
        return value / BOX_TO_WORLD;
    }
    public static float convertToWorld(float value) {
        return value * BOX_TO_WORLD;
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
            fixtureDef.shape = shape;
            fixtureDef.filter.categoryBits = B2DVars.BIT_WALLS;

            Body body = world.createBody(bd);
            body.createFixture(fixtureDef).setUserData("wall");
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


    public static Animation createAnimation(TextureAtlas atlas, String[] regions, float frameDuration) {
        TextureRegion[] frames = new TextureRegion[regions.length];
        for(int i = 0; i < frames.length; i++) {
            frames[i] = atlas.findRegion(regions[i]);
        }
        return new Animation(frameDuration, frames);
    }
}
