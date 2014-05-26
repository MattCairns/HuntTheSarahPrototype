package com.matthewcairns.flameblade;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.matthewcairns.flameblade.handlers.Utils;

import javax.xml.soap.Text;

/**
 * Created by Matthew Cairns on 26/05/2014.
 * All rights reserved.
 */
public class HealthBar {
    TextureAtlas atlas;
    NinePatchDrawable health;
//    NinePatchDrawable  healthBarBackground;
//    TextureRegion healthRight;
//    TextureRegion healthLeft;

    public HealthBar() {
        atlas = new TextureAtlas(Gdx.files.internal("ui/ui.txt"));

        NinePatch healthNP = new NinePatch(new Texture(Gdx.files.internal("ui/healthbar.png")), 5, 5, 4, 4);
        //NinePatch healthBarBackgroundNP = new NinePatch(atlas.findRegion("buttonLong_brown"), 5, 5, 4, 4);
        health = new NinePatchDrawable(healthNP);
        //healthBarBackground = new NinePatchDrawable(healthBarBackgroundNP);
    }

    public void draw(Vector2 loc,  Batch batch, float playerHealth) {
        float hp = playerHealth / 100.0f;

        //healthBarBackground.draw(batch, Utils.convertToWorld(loc.x) - 390, Utils.convertToWorld(loc.y) + (230-49), 190, 49);
        health.draw(batch, Utils.convertToWorld(loc.x) - 390, Utils.convertToWorld(loc.y) + (230-18), hp * 100.0f, 20.0f);
    }

}
