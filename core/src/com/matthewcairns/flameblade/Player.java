package com.matthewcairns.flameblade;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.matthewcairns.flameblade.handlers.AudioController;
import com.matthewcairns.flameblade.handlers.B2DVars;
import com.matthewcairns.flameblade.handlers.Utils;

/**
 * Created by Matthew Cairns on 06/05/2014.
 * All rights reserved.
 */
public class Player {
    TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("elf_sprites.txt"));
    Texture damageRed = new Texture(Gdx.files.internal("damage_red_flash.png"));
    Batch batch;
    BodyDef bodyDef;
    Body player;

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

    AudioController audioController;

    private float timeSinceLastStep = 0.0f;

    float oldX;
    float oldY;

    float SPEED = 500.0f;
    float WALK_SPEED = 0.2f;
    float stateTime = 0.0f;

    float playerHealth = 100.0f;
    boolean playerHurt = false;
    float fadeTime = 0.0f;

    public enum State {
        IDLE, WALKING, DYING
    }
    public enum FaceState {
        LEFT, RIGHT, UP, DOWN,
        UPLEFT, UPRIGHT, DOWNLEFT, DOWNRIGHT
    }
    State state = State.IDLE;
    FaceState faceState = FaceState.RIGHT;

    public Player(float x, float y, Batch batch, World world, AudioController ac) {
        bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(Utils.convertToBox(x), Utils.convertToBox(y));

        CircleShape shape = new CircleShape();
        shape.setRadius(Utils.convertToBox(16.0f));

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 0.0f;
        fixtureDef.friction = 0.8f;
        fixtureDef.filter.categoryBits = B2DVars.BIT_PLAYER;
        fixtureDef.filter.maskBits = B2DVars.BIT_WALLS | B2DVars.BIT_ENEMIES;

        player = world.createBody(bodyDef);
        player.createFixture(fixtureDef).setUserData("player");
        shape.dispose();

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

        this.batch = batch;

        audioController = ac;
    }


    public void act() {
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            faceState = FaceState.LEFT;
            state = State.WALKING;
            Vector2 vec = new Vector2(-SPEED, 0.0f);
            player.applyForce(vec, player.getWorldCenter(), true);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            faceState = FaceState.RIGHT;
            state = State.WALKING;
            Vector2 vec = new Vector2(SPEED, 0.0f);
            player.applyForce(vec, player.getWorldCenter(), true);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            faceState = FaceState.UP;
            state = State.WALKING;
            Vector2 vec = new Vector2(0.0f, SPEED);
            player.applyForce(vec, player.getWorldCenter(), true);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            faceState = FaceState.DOWN;
            state = State.WALKING;
            Vector2 vec = new Vector2(0.0f, -SPEED);
            player.applyForce(vec, player.getWorldCenter(), true);
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
            player.setLinearVelocity(0.0f, 0.0f);

    }

    public void draw() {
        float delta = Gdx.graphics.getDeltaTime();
        stateTime += delta;

        switch(state) {
            case IDLE:
                switch (faceState) {
                    case LEFT:
                        batch.draw(elfIdleLeft, Utils.convertToWorld(player.getWorldCenter().x) - 16, Utils.convertToWorld(player.getWorldCenter().y) - 16);
                        break;
                    case RIGHT:
                        batch.draw(elfIdleRight, Utils.convertToWorld(player.getWorldCenter().x) - 16, Utils.convertToWorld(player.getWorldCenter().y) - 16);
                        break;
                    case UP:
                        batch.draw(elfIdleUp, Utils.convertToWorld(player.getWorldCenter().x) - 16, Utils.convertToWorld(player.getWorldCenter().y) - 16);
                        break;
                    case DOWN:
                        batch.draw(elfIdleDown, Utils.convertToWorld(player.getWorldCenter().x) - 16, Utils.convertToWorld(player.getWorldCenter().y) - 16);
                        break;
                    case UPLEFT:
                        batch.draw(elfIdleUpLeft, Utils.convertToWorld(player.getWorldCenter().x) - 16, Utils.convertToWorld(player.getWorldCenter().y) - 16);
                        break;
                    case UPRIGHT:
                        batch.draw(elfIdleUpRight, Utils.convertToWorld(player.getWorldCenter().x) - 16, Utils.convertToWorld(player.getWorldCenter().y) - 16);
                        break;
                    case DOWNRIGHT:
                        batch.draw(elfIdleDownRight, Utils.convertToWorld(player.getWorldCenter().x) - 16, Utils.convertToWorld(player.getWorldCenter().y) - 16);
                        break;
                    case DOWNLEFT:
                        batch.draw(elfIdleDownLeft, Utils.convertToWorld(player.getWorldCenter().x) - 16, Utils.convertToWorld(player.getWorldCenter().y) - 16);
                        break;
                }
                break;

            case WALKING:
                switch (faceState) {
                    case LEFT:
                        batch.draw(elfWalkLeft.getKeyFrame(stateTime, true), Utils.convertToWorld(player.getWorldCenter().x) - 16, Utils.convertToWorld(player.getWorldCenter().y) - 16);
                        break;
                    case RIGHT:
                        batch.draw(elfWalkRight.getKeyFrame(stateTime, true), Utils.convertToWorld(player.getWorldCenter().x) - 16, Utils.convertToWorld(player.getWorldCenter().y) - 16);
                        break;
                    case UP:
                        batch.draw(elfWalkUp.getKeyFrame(stateTime, true), Utils.convertToWorld(player.getWorldCenter().x) - 16, Utils.convertToWorld(player.getWorldCenter().y) - 16);
                        break;
                    case DOWN:
                        batch.draw(elfWalkDown.getKeyFrame(stateTime, true), Utils.convertToWorld(player.getWorldCenter().x) - 16, Utils.convertToWorld(player.getWorldCenter().y) - 16);
                        break;
                    case UPLEFT:
                        batch.draw(elfWalkUpLeft.getKeyFrame(stateTime, true), Utils.convertToWorld(player.getWorldCenter().x) - 16, Utils.convertToWorld(player.getWorldCenter().y) - 16);
                        break;
                    case UPRIGHT:
                        batch.draw(elfWalkUpRight.getKeyFrame(stateTime, true), Utils.convertToWorld(player.getWorldCenter().x) - 16, Utils.convertToWorld(player.getWorldCenter().y) - 16);
                        break;
                    case DOWNRIGHT:
                        batch.draw(elfWalkDownRight.getKeyFrame(stateTime, true), Utils.convertToWorld(player.getWorldCenter().x) - 16, Utils.convertToWorld(player.getWorldCenter().y) - 16);
                        break;
                    case DOWNLEFT:
                        batch.draw(elfWalkDownLeft.getKeyFrame(stateTime, true), Utils.convertToWorld(player.getWorldCenter().x) - 16, Utils.convertToWorld(player.getWorldCenter().y) - 16);
                        break;
                }
                break;
        }

    }

    public Body getBody() { return player; }
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

    public float getPlayerHealth() {
        return playerHealth;
    }

    public void setPlayerHealth(float playerHealth) {
        this.playerHealth = playerHealth;
        System.out.println(playerHealth);
        audioController.getSound("Player Hurt").play();

        batch.begin();
        batch.draw(damageRed, Utils.convertToWorld(player.getWorldCenter().x)-400, Utils.convertToWorld(player.getWorldCenter().y)-240);
        batch.end();

    }


}
