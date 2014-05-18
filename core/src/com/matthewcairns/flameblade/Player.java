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
    TextureRegion elfIdleUpLeft;
    TextureRegion elfIdleUpRight;
    TextureRegion elfIdleDownLeft;
    TextureRegion elfIdleDownRight;
    Animation elfWalkLeft;
    Animation elfWalkRight;
    Animation elfWalkUp;
    Animation elfWalkDown;
    Animation elfWalkUpLeft;
    Animation elfWalkUpRight;
    Animation elfWalkDownLeft;
    Animation elfWalkDownRight;

    private Sound walkingSound;
    private float timeSinceLastStep = 0.0f;

    float oldX;
    float oldY;

    float VELOCITY = 200.0f;
    float WALK_SPEED = 0.2f;
    float stateTime = 0.0f;

    public enum State {
        IDLE, WALKING, DYING, SHOOTING;
    }
    public enum FaceState {
        LEFT, RIGHT, UP, DOWN,
        UPLEFT, UPRIGHT, DOWNLEFT, DOWNRIGHT;
    }
    State state = State.IDLE;
    FaceState faceState = FaceState.RIGHT;

    public Player(float x, float y) {
        playerRec = new Rectangle(x, y, 32.0f, 32.0f);

        //Sprite to display when player is stationary.
        elfIdleRight = atlas.findRegion("elf_idle_right");
        elfIdleLeft = atlas.findRegion("elf_idle_left");
        elfIdleUp = atlas.findRegion("elf_idle_up");
        elfIdleDown = atlas.findRegion("elf_idle_down");
        elfIdleUpLeft = atlas.findRegion("elf_idle_up_left");
        elfIdleUpRight = atlas.findRegion("elf_idle_up_right");
        elfIdleDownLeft = atlas.findRegion("elf_idle_down_left");
        elfIdleDownRight = atlas.findRegion("elf_idle_down_right");

        //Animation for walking right
        elfWalkRight = Utils.createAnimation(atlas, new String[]{"elf_walk_right_one", "elf_walk_right_two"}, WALK_SPEED);

        //Animation for walking left
        elfWalkLeft = Utils.createAnimation(atlas, new String[]{"elf_walk_left_one", "elf_walk_left_two"}, WALK_SPEED);

        //Animation for walking up
        elfWalkUp = Utils.createAnimation(atlas, new String[]{"elf_walk_up_one", "elf_walk_up_two"}, WALK_SPEED);

        //Animation for walking down.
        elfWalkDown = Utils.createAnimation(atlas, new String[]{"elf_walk_down_one", "elf_walk_down_two"}, WALK_SPEED);

        //Animation for walking up left.
        elfWalkUpLeft = Utils.createAnimation(atlas, new String[]{"elf_walk_up_left_one", "elf_walk_up_left_two"}, WALK_SPEED);

        //Animation for walking up right.
        elfWalkUpRight = Utils.createAnimation(atlas, new String[]{"elf_walk_up_right_one", "elf_walk_up_right_two"}, WALK_SPEED);

        //Animation for walking down left.
        elfWalkDownLeft = Utils.createAnimation(atlas, new String[]{"elf_walk_down_left_one", "elf_walk_down_left_two"}, WALK_SPEED);

        //Animation for walking down right.
        elfWalkDownRight = Utils.createAnimation(atlas, new String[]{"elf_walk_down_right_one", "elf_walk_down_right_two"}, WALK_SPEED);

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

        if (Gdx.input.isKeyPressed(Input.Keys.A) && Gdx.input.isKeyPressed(Input.Keys.W)) {
            faceState = FaceState.UPLEFT;
            state = State.WALKING;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.W) && Gdx.input.isKeyPressed(Input.Keys.D)) {
            faceState = FaceState.UPRIGHT;
            state = State.WALKING;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.S) && Gdx.input.isKeyPressed(Input.Keys.D)) {
            faceState = FaceState.DOWNRIGHT;
            state = State.WALKING;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.S) && Gdx.input.isKeyPressed(Input.Keys.A)) {
            faceState = FaceState.DOWNLEFT;
            state = State.WALKING;
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
        if(state == State.IDLE && faceState == FaceState.UPLEFT) {
            batch.draw(elfIdleUpLeft, playerRec.getX(), playerRec.getY());
        }
        if(state == State.IDLE && faceState == FaceState.UPRIGHT) {
            batch.draw(elfIdleUpRight, playerRec.getX(), playerRec.getY());
        }
        if(state == State.IDLE && faceState == FaceState.DOWNRIGHT) {
            batch.draw(elfIdleDownRight, playerRec.getX(), playerRec.getY());
        }
        if(state == State.IDLE && faceState == FaceState.DOWNLEFT) {
            batch.draw(elfIdleDownLeft, playerRec.getX(), playerRec.getY());
        }

        if(state == State.WALKING && faceState == FaceState.LEFT)
            batch.draw(elfWalkLeft.getKeyFrame(stateTime, true), playerRec.getX(), playerRec.getY());

        if(state == State.WALKING && faceState == FaceState.RIGHT)
            batch.draw(elfWalkRight.getKeyFrame(stateTime, true), playerRec.getX(), playerRec.getY());

        if(state == State.WALKING && faceState == FaceState.UP)
            batch.draw(elfWalkUp.getKeyFrame(stateTime, true),playerRec.getX(), playerRec.getY());

        if(state == State.WALKING && faceState == FaceState.DOWN)
            batch.draw(elfWalkDown.getKeyFrame(stateTime, true), playerRec.getX(), playerRec.getY());

        if(state == State.WALKING && faceState == FaceState.UPLEFT)
            batch.draw(elfWalkUpLeft.getKeyFrame(stateTime, true), playerRec.getX(), playerRec.getY());

        if(state == State.WALKING && faceState == FaceState.UPRIGHT)
            batch.draw(elfWalkUpRight.getKeyFrame(stateTime, true), playerRec.getX(), playerRec.getY());

        if(state == State.WALKING && faceState == FaceState.DOWNRIGHT)
            batch.draw(elfWalkDownRight.getKeyFrame(stateTime, true),playerRec.getX(), playerRec.getY());

        if(state == State.WALKING && faceState == FaceState.DOWNLEFT)
            batch.draw(elfWalkDownLeft.getKeyFrame(stateTime, true), playerRec.getX(), playerRec.getY());



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
        if(faceState == FaceState.UPLEFT) {
            return "UPLEFT";
        }
        if(faceState == FaceState.UPRIGHT) {
            return "UPRIGHT";
        }
        if(faceState == FaceState.DOWNLEFT) {
            return "DOWNLEFT";
        }
        if(faceState == FaceState.DOWNRIGHT) {
            return "DOWNRIGHT";
        }
        return null;
    }

}
