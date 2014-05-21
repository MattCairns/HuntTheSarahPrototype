package com.matthewcairns.flameblade;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

/**
 * Created by Matthew Cairns on 16/05/2014.
 * All rights reserved.
 */
public class Bullet {
    TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("elf_sprites.txt"));
    Rectangle arrow;

    TextureRegion arrowImage;

    Sound arrowShoot;

    float VELOCITY = 500.0f;
    String arrow_dir;

    public Bullet(Rectangle player, String direction) {
        if(direction.equals("UP"))
            arrowImage = atlas.findRegion("arrow_up");
        if(direction.equals("DOWN"))
            arrowImage = atlas.findRegion("arrow_down");
        if(direction.equals("LEFT"))
            arrowImage = atlas.findRegion("arrow_left");
        if(direction.equals("RIGHT"))
            arrowImage = atlas.findRegion("arrow_right");
        if(direction.equals("UPLEFT"))
            arrowImage = atlas.findRegion("arrow_up_left");
        if(direction.equals("UPRIGHT"))
            arrowImage = atlas.findRegion("arrow_up_right");
        if(direction.equals("DOWNRIGHT"))
            arrowImage = atlas.findRegion("arrow_down_right");
        if(direction.equals("DOWNLEFT"))
            arrowImage = atlas.findRegion("arrow_down_left");


        arrow = new Rectangle(player.x, player.y, 32.0f, 32.0f);


        arrow_dir = direction;

        arrowShoot = Gdx.audio.newSound(Gdx.files.internal("sounds/arrow_shoot.mp3"));
        arrowShoot.play();
    }

    public void draw(Batch batch) {
        batch.draw(arrowImage, arrow.getX(), arrow.getY());
        float delta = Gdx.graphics.getDeltaTime();
        if(arrow_dir.equals("UP"))
            arrow.y += VELOCITY * delta;
        if(arrow_dir.equals("DOWN"))
            arrow.y -= VELOCITY *delta;
        if(arrow_dir.equals("LEFT"))
            arrow.x -= VELOCITY * delta;
        if(arrow_dir.equals("RIGHT"))
            arrow.x += VELOCITY * delta;
        if(arrow_dir.equals("UPLEFT")) {
            arrow.y += VELOCITY  * delta;
            arrow.x -= VELOCITY  * delta;
        }
        if(arrow_dir.equals("UPRIGHT")) {
            arrow.y += VELOCITY * delta;
            arrow.x += VELOCITY * delta;
        }
        if(arrow_dir.equals("DOWNRIGHT")) {
            arrow.x += VELOCITY  * delta;
            arrow.y -= VELOCITY  * delta;
        }
        if(arrow_dir.equals("DOWNLEFT")) {
            arrow.x -= VELOCITY  * delta;
            arrow.y -= VELOCITY  * delta;
        }
    }

    public Rectangle getRectangle() {
        return arrow;
    }

}
