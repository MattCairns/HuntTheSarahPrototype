package com.matthewcairns.flameblade;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import javax.xml.soap.Text;

/**
 * Created by Matthew Cairns on 26/05/2014.
 * All rights reserved.
 */
public class GameHUD {
    TextureAtlas atlas;
    TextureRegion health;
    TextureRegion healthRight;
    TextureRegion healthLeft;

    public GameHUD() {
        atlas = new TextureAtlas(Gdx.files.internal("ui/ui.txt"));

        health = atlas.findRegion("barRed_horizontalMid");
        healthRight = atlas.findRegion("barRed_horizontalRight");
        healthLeft = atlas.findRegion("barRed_horizontalLeft");
    }

}
