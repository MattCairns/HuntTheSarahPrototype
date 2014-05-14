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

    public enum FaceState {
        LEFT, RIGHT, UP, DOWN;
    }

    State state = State.IDLE;
    FaceState faceState = FaceState.RIGHT;
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

        TextureRegion[] elfDownFrames = new TextureRegion[2];
        elfDownFrames[0] = atlas.findRegion("elf_walk_down_one");
        elfDownFrames[1] = atlas.findRegion("elf_walk_down_two");
        elfWalkDown = new Animation(0.5f, elfDownFrames);

    }


    public void act(float delta) {
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            faceState = FaceState.LEFT;
            state = State.WALKING;
            x -= SPEED;


        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            faceState = FaceState.RIGHT;
            state = State.WALKING;
            x += SPEED;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            faceState = FaceState.UP;
            state = State.WALKING;
            y += SPEED;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            faceState = FaceState.DOWN;
            state = State.WALKING;
            y -= SPEED;
        }

        if (!Gdx.input.isKeyPressed(Input.Keys.A) &&
            !Gdx.input.isKeyPressed(Input.Keys.D) &&
            !Gdx.input.isKeyPressed(Input.Keys.W) &&
            !Gdx.input.isKeyPressed(Input.Keys.S))
            state = State.IDLE;


    }

    public void draw(Batch batch) {

        stateTime += Gdx.graphics.getDeltaTime();
        if(state == State.IDLE && faceState == FaceState.LEFT) {
            batch.draw(elfIdleLeft, x, y);
        }
        if(state == State.IDLE && faceState == FaceState.RIGHT) {
            batch.draw(elfIdleRight, x, y);
        }
        if(state == State.IDLE && faceState == FaceState.UP) {
            batch.draw(elfIdleUp, x, y);
        }
        if(state == State.IDLE && faceState == FaceState.DOWN) {
            batch.draw(elfIdleDown, x, y);
        }

        if(state == State.WALKING && faceState == FaceState.LEFT)
            batch.draw(elfWalkLeft.getKeyFrame(stateTime, true), x, y);

        if(state == State.WALKING && faceState == FaceState.RIGHT)
            batch.draw(elfWalkRight.getKeyFrame(stateTime, true), x, y);

        if(state == State.WALKING && faceState == FaceState.UP)
            batch.draw(elfWalkUp.getKeyFrame(stateTime, true), x, y);

        if(state == State.WALKING && faceState == FaceState.DOWN)
            batch.draw(elfWalkDown.getKeyFrame(stateTime, true), x, y);

    }

}
