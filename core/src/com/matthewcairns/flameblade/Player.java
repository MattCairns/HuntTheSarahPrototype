package com.matthewcairns.flameblade;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
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
    Texture texture = new Texture(Gdx.files.internal("player.png"));
    Rectangle player = new Rectangle(400, 240, 64, 64);

    AlphaAction action = new AlphaAction();

    Vector2 mousePos;

    public void act(float delta) {
        mousePos = new Vector2(Gdx.input.getX(), Gdx.input.getY());


    }

    public void draw(Batch batch) {
        batch.draw(texture, 400, 240);
    }
}
