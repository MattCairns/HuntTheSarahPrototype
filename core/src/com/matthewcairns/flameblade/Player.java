package com.matthewcairns.flameblade;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.AlphaAction;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.actions.RotateByAction;

/**
 * Created by Matthew Cairns on 06/05/2014.
 * All rights reserved.
 */
public class Player {
    //Load texture and create a rectangle matching the players size.
    Texture texture = new Texture(Gdx.files.internal("player.png"));
    Sprite player = new Sprite(texture, 64, 64);
    Vector2 playerPos = new Vector2(400-player.getWidth()/2, 240-player.getHeight()/2);


    Vector2 mousePos;

    public Player() {
        player.setPosition(400-32, 240-32);
    }

    public void act(float delta) {
        //Store position of mouse as a vector.
        mousePos = new Vector2(Gdx.input.getX(), Gdx.input.getY());
        Vector2 playerMouseAngle = mousePos.sub(playerPos);
        //Rotates the player to the position of the mouse.
        player.setRotation(-(playerMouseAngle.angle()-90));


    }

    public void draw(Batch batch) {
        player.draw(batch);
    }
}
