package com.matthewcairns.flameblade.handlers;

import com.badlogic.gdx.physics.box2d.*;

/**
 * Created by Matthew Cairns on 23/05/2014.
 * All rights reserved.
 */
public class MyContactListener implements ContactListener {

    //Called when two b2d fixtures collide.
    public void beginContact(Contact c) {
        Fixture fa = c.getFixtureA();
        Fixture fb = c.getFixtureB();


    }

    //Called when two fixtures no longer colliding.
    public void endContact(Contact c) {}

    public void preSolve(Contact c, Manifold m) {}
    public void postSolve(Contact c, ContactImpulse ci) {}
}
