package com.matthewcairns.flameblade;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.FitViewport;

/**
 * Created by Matthew Cairns on 06/05/2014.
 * All rights reserved.
 */
public class MainMenuScreen implements Screen {
    final Flameblade game;

    Stage stage;

    TextButton titleButton;
    TextButton playButton;
    TextButton.TextButtonStyle style;

    OrthographicCamera camera;

    public MainMenuScreen(final Flameblade gam) {
        game = gam;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);

        stage = new Stage(new FitViewport(800, 480));
        Gdx.input.setInputProcessor(stage);

        style = new TextButton.TextButtonStyle();
        style.font = game.font;
        titleButton = new TextButton("Flameblade", style);
        titleButton.setPosition(400, 350);
        playButton = new TextButton("Click to Play", style);
        playButton.setPosition(400, 250);

        playButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                gam.setScreen(new MainGame(game));
            }
        });

        stage.addActor(titleButton);
        stage.addActor(playButton);

    }

    @Override
    public void render (float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        stage.draw();
    }

    @Override
    public void resume() {}
    @Override
    public void pause() {}
    @Override
    public void resize(int width, int height) {}
    @Override
    public void hide() {}
    @Override
    public void show() {}
    @Override
    public void dispose() {
        stage.dispose();
    }
}
