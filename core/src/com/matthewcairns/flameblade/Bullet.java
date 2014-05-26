package com.matthewcairns.flameblade;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.physics.box2d.*;
import com.matthewcairns.flameblade.handlers.B2DVars;
import com.matthewcairns.flameblade.handlers.Utils;

/**
 * Created by Matthew Cairns on 16/05/2014.
 * All rights reserved.
 */
public class Bullet {
    TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("elf_sprites.txt"));
    Texture bulletImage = new Texture(Gdx.files.internal("bullet.png"));
    BodyDef bodyDef;
    Body bullet;

    //TextureRegion arrowImage;
    //Texture arrowImage = new Texture(Gdx.files.internal("arrow_b2d.png"));

    Sound arrowShoot;

    float VELOCITY = 20.0f;
    String arrow_dir;

    public Bullet(Body player, String direction, World world) {
        bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(player.getWorldCenter().x, player.getWorldCenter().y);
        bodyDef.bullet = true;

        CircleShape shape = new CircleShape();
        shape.setRadius(Utils.convertToBox(5.0f));

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 10.0f;
        fixtureDef.friction = 0.8f;
        fixtureDef.filter.categoryBits = B2DVars.BIT_BULLETS;
        fixtureDef.filter.maskBits = B2DVars.BIT_WALLS | B2DVars.BIT_BULLETS | B2DVars.BIT_ENEMIES;

        bullet = world.createBody(bodyDef);
        bullet.createFixture(fixtureDef).setUserData("bullet");

        shape.dispose();

//        if(direction.equals("UP"))
//            arrowImage = atlas.findRegion("arrow_up");
//        if(direction.equals("DOWN"))
          //arrowImage = atlas.findRegion("arrow_down");
//        if(direction.equals("LEFT"))
//            arrowImage = atlas.findRegion("arrow_left");
//        if(direction.equals("RIGHT"))
//            arrowImage = atlas.findRegion("arrow_right");
//        if(direction.equals("UPLEFT"))
//            arrowImage = atlas.findRegion("arrow_up_left");
//        if(direction.equals("UPRIGHT"))
//            arrowImage = atlas.findRegion("arrow_up_right");
//        if(direction.equals("DOWNRIGHT"))
//            arrowImage = atlas.findRegion("arrow_down_right");
//        if(direction.equals("DOWNLEFT"))
//            arrowImage = atlas.findRegion("arrow_down_left");

        arrow_dir = direction;

        arrowShoot = Gdx.audio.newSound(Gdx.files.internal("sounds/fireelf.ogg"));
        arrowShoot.play();
    }

    public void draw(Batch batch) {
       batch.draw(bulletImage, Utils.convertToWorld(bullet.getWorldCenter().x)-5, Utils.convertToWorld(bullet.getWorldCenter().y)-5);
        if(arrow_dir.equals("UP"))
            bullet.applyForceToCenter(0.0f, VELOCITY,true);
        if(arrow_dir.equals("DOWN"))
            bullet.applyForceToCenter(0.0f, -VELOCITY,true);
        if(arrow_dir.equals("LEFT"))
            bullet.applyForceToCenter(-VELOCITY, 0.0f,true);
        if(arrow_dir.equals("RIGHT"))
            bullet.applyForceToCenter(VELOCITY, 0.0f, true);
        if(arrow_dir.equals("UPLEFT")) {
            bullet.applyForceToCenter(-VELOCITY, VELOCITY,true);
        }
        if(arrow_dir.equals("UPRIGHT")) {
            bullet.applyForceToCenter(VELOCITY, VELOCITY,true);
        }
        if(arrow_dir.equals("DOWNRIGHT")) {
            bullet.applyForceToCenter(VELOCITY, -VELOCITY,true);
        }
        if(arrow_dir.equals("DOWNLEFT")) {
            bullet.applyForceToCenter(-VELOCITY, -VELOCITY,true);
        }
    }

    public Body getBody() {
        return bullet;
    }

}
