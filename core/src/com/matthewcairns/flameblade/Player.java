package com.matthewcairns.flameblade;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
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
    TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("elf_sprites.txt"));

    TextureRegion elfIdleLeft;
    TextureRegion elfIdleRight;
    TextureRegion elfIdleUp;
    TextureRegion elfIdleDown;
    Animation elfWalkLeft;
    Animation elfWalkRight;
    Animation elfWalkUp;
    Animation elfWalkDown;

    float x;
    float y;

    float SPEED = 5.0f;

    float stateTime = 0.0f;

    public enum State {
        IDLE, WALKING, DYING, SHOOTING;
    }

    State state = State.IDLE;
    boolean facingLeft = true;


    public Player() {
        x = 400.0f;
        y = 240.0f;

        elfIdleRight = atlas.findRegion("elf_prone_right");
        elfIdleLeft = atlas.findRegion("elf_prone_left");
        elfIdleUp = atlas.findRegion("elf_prone_up");
        elfIdleDown = atlas.findRegion("elf_prone_down");

        TextureRegion[] elfRightFrames = new TextureRegion[2];
        elfRightFrames[0] = atlas.findRegion("elf_walk_right_one");
        elfRightFrames[1] = atlas.findRegion("elf_walk_right_two");
        elfWalkRight = new Animation(0.5f, elfRightFrames);

        TextureRegion[] elfLeftFrames = new TextureRegion[2];
        elfLeftFrames[0] = atlas.findRegion("elf_walk_left_one");
        elfLeftFrames[1] = atlas.findRegion("elf_walk_left_two");
        elfWalkLeft = new Animation(0.5f, elfLeftFrames);

        TextureRegion[] elfUpFrames = new TextureRegion[2];
        elfUpFrames[0] = atlas.findRegion("elf_walk_up_one");
        elfUpFrames[1] = atlas.findRegion("elf_walk_up_two");
        elfWalkUp = new Animation(0.5f, elfUpFrames);

    }


    public void act(float delta) {
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            facingLeft = true;
            state = State.WALKING;
            x -= SPEED;


        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            facingLeft = false;
            state = State.WALKING;
            x += SPEED;
        }

        if (!Gdx.input.isKeyPressed(Input.Keys.A) && !Gdx.input.isKeyPressed(Input.Keys.D))
            state = State.IDLE;


    }

    public void draw(Batch batch) {

        stateTime += Gdx.graphics.getDeltaTime();
        if(state == State.IDLE && facingLeft) {
            batch.draw(elfIdleLeft, x, y);
        }
        if(state == State.IDLE && !facingLeft) {
            batch.draw(elfIdleRight, x, y);
        }

        if(state == State.WALKING && facingLeft)
            batch.draw(elfWalkLeft.getKeyFrame(stateTime, true), x, y);

        else if(state == State.WALKING)
            batch.draw(elfWalkRight.getKeyFrame(stateTime, true), x, y);

    }

}
