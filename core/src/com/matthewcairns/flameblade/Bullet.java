package com.matthewcairns.flameblade;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

/**
 * Created by Matthew Cairns on 16/05/2014.
 * All rights reserved.
 */
public class Bullet {
    TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("elf_sprites.txt"));
    BodyDef bodyDef;
    Body bullet;

    //TextureRegion arrowImage;
    Texture arrowImage = new Texture(Gdx.files.internal("arrow_b2d.png"));

    Sound arrowShoot;


    float VELOCITY = 5000.0f;
    String arrow_dir;

    public Bullet(Body player, String direction, World world) {
        bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(player.getWorldCenter().x, player.getWorldCenter().y);

        CircleShape shape = new CircleShape();
        Vector2 size = new Vector2(16, 16);
        shape.setRadius(5.0f);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 0.0f;
        fixtureDef.friction = 0.8f;

        bullet = world.createBody(bodyDef);
        bullet.createFixture(fixtureDef);
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

        arrowShoot = Gdx.audio.newSound(Gdx.files.internal("sounds/arrow_shoot.mp3"));
        arrowShoot.play();
    }

    public void draw(Batch batch) {
        batch.draw(arrowImage, bullet.getWorldCenter().x, bullet.getWorldCenter().y);
        if(arrow_dir.equals("UP"))
            bullet.setLinearVelocity(0.0f, VELOCITY);
        if(arrow_dir.equals("DOWN"))
            bullet.setLinearVelocity(0.0f, -VELOCITY);
        if(arrow_dir.equals("LEFT"))
            bullet.setLinearVelocity(-VELOCITY, 0.0f);
        if(arrow_dir.equals("RIGHT"))
            bullet.setLinearVelocity(VELOCITY, 0.0f);
        if(arrow_dir.equals("UPLEFT")) {
            bullet.setLinearVelocity(-VELOCITY, VELOCITY);
        }
        if(arrow_dir.equals("UPRIGHT")) {
            bullet.setLinearVelocity(VELOCITY, VELOCITY);
        }
        if(arrow_dir.equals("DOWNRIGHT")) {
            bullet.setLinearVelocity(VELOCITY, -VELOCITY);
        }
        if(arrow_dir.equals("DOWNLEFT")) {
            bullet.setLinearVelocity(-VELOCITY, -VELOCITY);
        }
    }

    public Body getBody() {
        return bullet;
    }

}
