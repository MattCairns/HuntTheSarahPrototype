package com.matthewcairns.flameblade.handlers;

import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.matthewcairns.flameblade.Player;

/**
 * Created by Matthew Cairns on 23/05/2014.
 * All rights reserved.
 */
public class MyContactListener implements ContactListener {
    Array<Body> bodiesToDestroy = new Array<Body>();
    Player player;

    public void getPlayer(Player player) {
        this.player = player;
    }

    //Called when two b2d fixtures collide.
    public void beginContact(Contact c) {
        Fixture fa = c.getFixtureA();
        Fixture fb = c.getFixtureB();

        //If bullet hits a wall then add the body to the destroy list.
        if(fa.getUserData() != null && fa.getUserData().equals("wall")) {
            if(fb.getUserData() != null && fb.getUserData().equals("bullet")) {
                if(!bodiesToDestroy.contains(fb.getBody(), true))
                    bodiesToDestroy.add(fb.getBody());
            }
        }
        else if(fb.getUserData() != null && fb.getUserData().equals("wall")) {
            if(fa.getUserData() != null && fa.getUserData().equals("bullet")) {
                if(!bodiesToDestroy.contains(fa.getBody(), true))
                    bodiesToDestroy.add(fa.getBody());
            }
        }

        //If enemy hits a bullet then add both bodies to the destroy list.
        if(fa.getUserData() != null && fa.getUserData().equals("bullet")) {
            if(fb.getUserData() != null && fb.getUserData().equals("enemy")) {
                if(!bodiesToDestroy.contains(fb.getBody(), true))
                    bodiesToDestroy.add(fb.getBody());
                if(!bodiesToDestroy.contains(fa.getBody(), true))
                    bodiesToDestroy.add(fa.getBody());
            }
        }
        else if(fb.getUserData() != null && fb.getUserData().equals("bullet")) {
            if(fa.getUserData() != null && fa.getUserData().equals("enemy")) {
                if(!bodiesToDestroy.contains(fb.getBody(), true))
                    bodiesToDestroy.add(fb.getBody());
                if(!bodiesToDestroy.contains(fa.getBody(), true))
                    bodiesToDestroy.add(fa.getBody());
            }
        }

        //If an enemy hits the player reduce player HP
        if(fa.getUserData() != null && fa.getUserData().equals("player")) {
            if(fb.getUserData() != null && fb.getUserData().equals("enemy")) {
                if(!bodiesToDestroy.contains(fb.getBody(), true))
                    bodiesToDestroy.add(fb.getBody());

                player.setPlayerHealth(player.getPlayerHealth()-5.0f);


            }
        }
        else if(fb.getUserData() != null && fb.getUserData().equals("player")) {
            if(fa.getUserData() != null && fa.getUserData().equals("enemy")) {
                if(!bodiesToDestroy.contains(fa.getBody(), true))
                    bodiesToDestroy.add(fa.getBody());
                player.setPlayerHealth(player.getPlayerHealth()-5.0f);
            }
        }
    }

    //Called when two fixtures no longer colliding.
    public void endContact(Contact c) {}

    public void preSolve(Contact c, Manifold m) {}
    public void postSolve(Contact c, ContactImpulse ci) {}

    public Array<Body> getBodies() { return bodiesToDestroy; }
}
