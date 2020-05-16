package com.recicle.game;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;

public class ContactListener implements com.badlogic.gdx.physics.box2d.ContactListener {

    private Model parent;

    public ContactListener(Model parent) {
        this.parent = parent;
    }

    @Override
    public void beginContact(Contact contact) {
            System.out.println("Contact");
            Fixture fa = contact.getFixtureA();
            Fixture fb = contact.getFixtureB();
            System.out.println(fa.getBody().getType()+" has hit "+ fb.getBody().getType());

        if(fa.getBody().getUserData() == "IAMTHESEA") {
            parent.isSwimming = true;
            return;
        }
        else if(fb.getBody().getUserData() == "IAMTHESEA") {
            parent.isSwimming = true;
            return; //The return tells your application that it is done processing the data and can return to whatever it wants to do next.

        }

        if(fa.getBody().getType() == BodyDef.BodyType.StaticBody) {
            this.shootUpInAir(fa, fb);
        }
        else if(fb.getBody().getType() == BodyDef.BodyType.StaticBody) {
            this.shootUpInAir(fb, fa);
        }
        else { }


        //In x: negative values for left and positive values for right.
        //In y: negative values for up and positive values for down.

    }

    private void shootUpInAir(Fixture staticFixture, Fixture otherFixture){
        System.out.println("Adding Force");
        otherFixture.getBody().applyForceToCenter(new Vector2(-100000,-100000), true);
        if (new AppPreferences().isSoundEnabled()) parent.playSound(Model.BOING_SOUND);

    }

    @Override
    public void endContact(Contact contact) {
        System.out.println("Lost Contact");
        Fixture fa = contact.getFixtureA();
        Fixture fb = contact.getFixtureB();
        if(fa.getBody().getUserData() == "IAMTHESEA") {
            parent.isSwimming = false;
            return;
        }
        else if(fb.getBody().getUserData() == "IAMTHESEA") {
            parent.isSwimming = false;
            return;
        }

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
