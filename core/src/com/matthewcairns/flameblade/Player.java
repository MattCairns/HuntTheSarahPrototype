package com.matthewcairns.flameblade;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

/**
 * Created by Matthew Cairns on 06/05/2014.
 * All rights reserved.
 */
public class Player {
    TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("elf_sprites.txt"));
    Rectangle playerRec;

    TextureRegion elfIdleLeft;
    TextureRegion elfIdleRight;
    TextureRegion elfIdleUp;
    TextureRegion elfIdleDown;
    Animation elfWalkLeft;
    Animation elfWalkRight;
    Animation elfWalkUp;
    Animation elfWalkDown;


    float oldX;
    float oldY;


    float VELOCITY = 200.0f;

    float stateTime = 0.0f;

    public enum State {
        IDLE, WALKING, DYING, SHOOTING;
    }

    public enum FaceState {
        LEFT, RIGHT, UP, DOWN;
    }

    State state = State.IDLE;
    FaceState faceState = FaceState.RIGHT;


    public Player() {
        playerRec = new Rectangle(400.0f, 300.0f, 32.0f, 32.0f);


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


    public void act(float delta, TiledMap map) {
        oldX = playerRec.getX();
        oldY = playerRec.getY();

        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            faceState = FaceState.LEFT;
            state = State.WALKING;
            playerRec.x -= VELOCITY * delta;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            faceState = FaceState.RIGHT;
            state = State.WALKING;
            playerRec.x += VELOCITY * delta;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            faceState = FaceState.UP;
            state = State.WALKING;
            playerRec.y += VELOCITY * delta;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            faceState = FaceState.DOWN;
            state = State.WALKING;
            playerRec.y -= VELOCITY * delta;
        }

        if (!Gdx.input.isKeyPressed(Input.Keys.A) &&
            !Gdx.input.isKeyPressed(Input.Keys.D) &&
            !Gdx.input.isKeyPressed(Input.Keys.W) &&
            !Gdx.input.isKeyPressed(Input.Keys.S))
            state = State.IDLE;

        //Load all of the objects from the collide layer and check if they overlap the player
        MapObjects objects = map.getLayers().get("collide").getObjects();
        for(RectangleMapObject rectangleObject : objects.getByType(RectangleMapObject.class)) {
            Rectangle rectangle = rectangleObject.getRectangle();
            if (Intersector.overlaps(rectangle, playerRec)) {
                playerRec.x = oldX;
                playerRec.y = oldY;

            }
        }


    }

    public void draw(Batch batch) {
        stateTime += Gdx.graphics.getDeltaTime();
        if(state == State.IDLE && faceState == FaceState.LEFT) {
            batch.draw(elfIdleLeft, playerRec.getX(), playerRec.getY());
        }
        if(state == State.IDLE && faceState == FaceState.RIGHT) {
            batch.draw(elfIdleRight, playerRec.getX(), playerRec.getY());
        }
        if(state == State.IDLE && faceState == FaceState.UP) {
            batch.draw(elfIdleUp, playerRec.getX(), playerRec.getY());
        }
        if(state == State.IDLE && faceState == FaceState.DOWN) {
            batch.draw(elfIdleDown, playerRec.getX(), playerRec.getY());
        }

        if(state == State.WALKING && faceState == FaceState.LEFT)
            batch.draw(elfWalkLeft.getKeyFrame(stateTime, true), playerRec.getX(), playerRec.getY());

        if(state == State.WALKING && faceState == FaceState.RIGHT)
            batch.draw(elfWalkRight.getKeyFrame(stateTime, true), playerRec.getX(), playerRec.getY());

        if(state == State.WALKING && faceState == FaceState.UP)
            batch.draw(elfWalkUp.getKeyFrame(stateTime, true),playerRec.getX(), playerRec.getY());

        if(state == State.WALKING && faceState == FaceState.DOWN)
            batch.draw(elfWalkDown.getKeyFrame(stateTime, true), playerRec.getX(), playerRec.getY());

    }

}
