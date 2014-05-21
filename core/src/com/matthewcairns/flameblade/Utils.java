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

/**
 * Created by Matthew Cairns on 17/05/2014.
 * All rights reserved.
 */
public class Utils {
    TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("elf_sprites.txt"));
    Animation explode;
    float stateTime = 0.0f;

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
    public static Boolean wallCollision(TiledMap map, Rectangle objectRec) {

        for(MapObject object :  map.getLayers().get("Collide").getObjects()) {
            if(object instanceof RectangleMapObject) {
                Rectangle rect = ((RectangleMapObject) object).getRectangle();
                System.out.println(rect.getWidth());

                if (Intersector.overlaps(rect, objectRec)) {
                   return true;
                }
            }
        }
        return false;
    }

    //Draws a simple explosion animation at the x and y location specified.
    public void explode(Batch batch, float x, float y) {
        stateTime += Gdx.graphics.getDeltaTime();
        System.out.println(stateTime);
        batch.draw(explode.getKeyFrame(stateTime, true), x, y);
    }


    public static Animation createAnimation(TextureAtlas atlas, String[] regions, float frameDuration) {
        TextureRegion[] frames = new TextureRegion[regions.length];
        System.out.println(regions.length);
        for(int i = 0; i < frames.length; i++) {
            System.out.println(regions[i]);
            frames[i] = atlas.findRegion(regions[i]);
        }
        return new Animation(frameDuration, frames);
    }
}
