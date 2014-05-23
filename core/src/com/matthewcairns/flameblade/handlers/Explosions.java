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
    Vector2 loc;
    Batch batch;
    int count = 0;
    boolean flaggedForRemoval = false;

    public Explosions(float x, float y, Batch b) {
        atlas = new TextureAtlas(Gdx.files.internal("elf_sprites.txt"));
        TextureRegion[] explodeFrames = new TextureRegion[4];
        explodeFrames[0] = atlas.findRegion("small_explosion_one");
        explodeFrames[1] = atlas.findRegion("small_explosion_two");
        explodeFrames[2] = atlas.findRegion("small_explosion_three");
        explodeFrames[3] = atlas.findRegion("small_explosion_four");
        explode = new Animation(0.05f, explodeFrames);

        loc = new Vector2(x, y);
        batch = b;
    }

    //Draws a simple explosion animation at the x and y location specified.
    public void smallExplosion() {
        stateTime += Gdx.graphics.getDeltaTime();
        System.out.println(stateTime);
        batch.draw(explode.getKeyFrame(stateTime, false), loc.x, loc.y);
        count += 1;
        if(count > 4) {
            flaggedForRemoval = true;
        }

    }

    public Boolean getFlaggedForRemoval() { return flaggedForRemoval; }
}
