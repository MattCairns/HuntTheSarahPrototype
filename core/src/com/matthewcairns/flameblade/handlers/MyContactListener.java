package com.matthewcairns.flameblade.handlers;

import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;

/**
 * Created by Matthew Cairns on 23/05/2014.
 * All rights reserved.
 */
public class MyContactListener implements ContactListener {
    Array<Body> bodiesToDestroy = new Array<Body>();

    //Called when two b2d fixtures collide.
    public void beginContact(Contact c) {
        Fixture fa = c.getFixtureA();
        Fixture fb = c.getFixtureB();

        if(fa.getUserData() != null && fa.getUserData().equals("wall")) {
            if(fb.getUserData() != null && fb.getUserData().equals("bullet")) {
                if(!bodiesToDestroy.contains(fb.getBody(), true))
                    bodiesToDestroy.add(fb.getBody());
                System.out.println("Destroying bullet");
            }
        }
        else if(fb.getUserData() != null && fb.getUserData().equals("wall")) {
            if(fa.getUserData() != null && fa.getUserData().equals("bullet")) {
                if(!bodiesToDestroy.contains(fa.getBody(), true))
                    bodiesToDestroy.add(fa.getBody());
                System.out.println("Destroying bullet");
            }
        }


    }

    //Called when two fixtures no longer colliding.
    public void endContact(Contact c) {}

    public void preSolve(Contact c, Manifold m) {}
    public void postSolve(Contact c, ContactImpulse ci) {}

    public Array<Body> getBodies() { return bodiesToDestroy; }
}
