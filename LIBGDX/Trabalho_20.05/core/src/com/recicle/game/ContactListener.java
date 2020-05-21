package com.recicle.game;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.utils.Array;

public class ContactListener implements com.badlogic.gdx.physics.box2d.ContactListener {

    private Model parent;

    Array<Body> toBeDeletedBodies = new Array<>();

    public ContactListener(Model parent) {
        this.parent = parent;
    }

    @Override
    public void beginContact(Contact contact) {
            System.out.println("Contact");
            Fixture fa = contact.getFixtureA();
            Fixture fb = contact.getFixtureB();
            //System.out.println(fa.getBody().getType()+" has hit "+ fb.getBody().getType());

        if(fa.getBody().getType() == BodyDef.BodyType.StaticBody) {
            fb.getBody().applyForceToCenter(new Vector2(0, 10),true);

        }
        else if(fb.getBody().getType() == BodyDef.BodyType.StaticBody) {
            fb.getBody().applyForceToCenter(new Vector2(0, 10),true);

        }
        else { }

    }


    @Override
    public void endContact(Contact contact) {
        System.out.println("Lost Contact");
        Fixture fa = contact.getFixtureA();
        Fixture fb = contact.getFixtureB();

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
