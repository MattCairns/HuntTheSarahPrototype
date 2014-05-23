package com.matthewcairns.flameblade.handlers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Created by Matthew Cairns on 23/05/2014.
 * All rights reserved.
 */
public class Explosions {
    TextureAtlas atlas;
    Animation explode;
    float stateTime = 0.0f;

    public Explosions() {
        atlas = new TextureAtlas(Gdx.files.internal("elf_sprites.txt"));
        TextureRegion[] explodeFrames = new TextureRegion[4];
        explodeFrames[0] = atlas.findRegion("small_explosion_one");
        explodeFrames[1] = atlas.findRegion("small_explosion_two");
        explodeFrames[2] = atlas.findRegion("small_explosion_three");
        explodeFrames[3] = atlas.findRegion("small_explosion_four");
        explode = new Animation(0.2f, explodeFrames);
    }

    //Draws a simple explosion animation at the x and y location specified.
    public void smallExplosion(Batch batch, float x, float y) {
        stateTime += Gdx.graphics.getDeltaTime();
        System.out.println(stateTime);
        batch.draw(explode.getKeyFrame(stateTime, false), x, y);
    }
}
