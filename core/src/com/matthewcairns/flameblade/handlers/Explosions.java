package com.matthewcairns.flameblade.handlers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by Matthew Cairns on 23/05/2014.
 * All rights reserved.
 */
public class Explosions {
    TextureAtlas atlas;
    Animation explode;
    float stateTime = 0.0f;
    float removalTime = 0.0f;
    Vector2 loc;
    Batch batch;
    int count = 0;
    boolean flaggedForRemoval = false;

    public Explosions(float x, float y, Batch b, int explosionType) {
        atlas = new TextureAtlas(Gdx.files.internal("elf_sprites.txt"));
        TextureRegion[] explodeFrames;
        if(explosionType == 0) {
            explodeFrames = new TextureRegion[4];
            for (int i = 0; i < explodeFrames.length; i++)
                explodeFrames[i] = atlas.findRegion("small_explosion", i);
            explode = new Animation(0.05f, explodeFrames);
            removalTime = 0.05f * 4;
        }
        else if(explosionType == 1) {
            explodeFrames = new TextureRegion[5];
            for (int i = 0; i < explodeFrames.length; i++)
                explodeFrames[i] = atlas.findRegion("big_explode", i);
            explode = new Animation(0.1f, explodeFrames);
            removalTime = 0.1f * 6;
        }

        loc = new Vector2(x, y);
        batch = b;
    }

    //Draws a simple explosion animation at the x and y location specified.
    public void explode() {
        stateTime += Gdx.graphics.getDeltaTime();
        batch.draw(explode.getKeyFrame(stateTime, false), loc.x, loc.y);
        count += 1;
        if(stateTime >= removalTime) {
            flaggedForRemoval = true;
        }

    }

    public Boolean getFlaggedForRemoval() { return flaggedForRemoval; }
}
