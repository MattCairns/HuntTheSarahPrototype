package com.matthewcairns.flameblade;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.maps.MapObject;
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

    private Sound walkingSound;
    private float timeSinceLastStep = 0.0f;

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
        playerRec = new Rectangle(400.0f, 250.0f, 32.0f, 32.0f);

        //Sprite to display when player is stationary.
        elfIdleRight = atlas.findRegion("elf_idle_right");
        elfIdleLeft = atlas.findRegion("elf_idle_left");
        elfIdleUp = atlas.findRegion("elf_idle_up");
        elfIdleDown = atlas.findRegion("elf_idle_down");

        //Animation for walking right
        TextureRegion[] elfRightFrames = new TextureRegion[2];
        elfRightFrames[0] = atlas.findRegion("elf_walk_right_one");
        elfRightFrames[1] = atlas.findRegion("elf_walk_right_two");
        elfWalkRight = new Animation(0.5f, elfRightFrames);

        //Animation for walking left
        TextureRegion[] elfLeftFrames = new TextureRegion[2];
        elfLeftFrames[0] = atlas.findRegion("elf_walk_left_one");
        elfLeftFrames[1] = atlas.findRegion("elf_walk_left_two");
        elfWalkLeft = new Animation(0.5f, elfLeftFrames);

        //Animation for walking up
        TextureRegion[] elfUpFrames = new TextureRegion[2];
        elfUpFrames[0] = atlas.findRegion("elf_walk_up_one");
        elfUpFrames[1] = atlas.findRegion("elf_walk_up_two");
        elfWalkUp = new Animation(0.5f, elfUpFrames);

        //Animation for walking down.
        TextureRegion[] elfDownFrames = new TextureRegion[2];
        elfDownFrames[0] = atlas.findRegion("elf_walk_down_one");
        elfDownFrames[1] = atlas.findRegion("elf_walk_down_two");
        elfWalkDown = new Animation(0.5f, elfDownFrames);

        walkingSound = Gdx.audio.newSound(Gdx.files.internal("sounds/footstep06.ogg"));
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
        if(Utils.wallCollision(map, playerRec)) {
            playerRec.x = oldX;
            playerRec.y = oldY;
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

    public Rectangle getRectangle() { return playerRec; }
    public String getFaceState() {
        if(faceState == FaceState.UP) {
            return "UP";
        }
        if(faceState == FaceState.DOWN) {
            return "DOWN";
        }
        if(faceState == FaceState.LEFT) {
            return "LEFT";
        }
        if(faceState == FaceState.RIGHT) {
            return "RIGHT";
        }
        return null;
    }

}
