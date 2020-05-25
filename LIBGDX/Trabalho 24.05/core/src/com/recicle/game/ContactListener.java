package com.recicle.game;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.utils.Array;
import com.recicle.game.views.Hud;
import com.recicle.game.views.PlayScreen;

public class ContactListener implements com.badlogic.gdx.physics.box2d.ContactListener {

    private Model parent;


    public ContactListener(Model parent) {
        this.parent = parent;
    }

    private void deleteBody(Fixture fixture) {
        if (PlayScreen.isDeletedBody(fixture.getBody()) == false) {
            PlayScreen.addDeletedBody(fixture.getBody());

        }
    }

    @Override
    public void beginContact(Contact contact) {

            Fixture fa = contact.getFixtureA();
            Fixture fb = contact.getFixtureB();

        if(fa.getBody().getType() == BodyDef.BodyType.DynamicBody){
            deleteBody(fa);
        }
        else if (fb.getBody().getType() == BodyDef.BodyType.DynamicBody){
            deleteBody(fb);
        }


        if(fa.getBody().getUserData() == "LIXEIRA_PAPEL") {
            Object userData = fb.getBody().getUserData();
            if ("PAPEL".equals(userData)) {
                Hud.addScore();
                parent.playSound(Model.CORRECT);
            }
            else {
                Hud.subScore();
                parent.playSound(Model.WRONG);
            }
        }

        else if(fb.getBody().getUserData() == "LIXEIRA_PAPEL") {
            Object userData = fa.getBody().getUserData();
            if ("PAPEL".equals(userData)) {
                Hud.addScore();
                parent.playSound(Model.CORRECT);
            }
            else {
                Hud.subScore();
                parent.playSound(Model.WRONG);
            }
        }

        if(fa.getBody().getUserData() == "LIXEIRA_PLASTICO") {
            Object userData = fb.getBody().getUserData();
            if ("PLASTICO".equals(userData)) {
                Hud.addScore();
                parent.playSound(Model.CORRECT);
            }
            else {
                Hud.subScore();
                parent.playSound(Model.WRONG);
            }
        }

        else if(fb.getBody().getUserData() == "LIXEIRA_PLASTICO") {
            Object userData = fa.getBody().getUserData();
            if ("PLASTICO".equals(userData)) {
                Hud.addScore();
                parent.playSound(Model.CORRECT);
            }
            else {
                Hud.subScore();
                parent.playSound(Model.WRONG);
            }
        }

        if(fa.getBody().getUserData() == "LIXEIRA_LATA") {
            Object userData = fb.getBody().getUserData();
            if ("LATA".equals(userData)) {
                Hud.addScore();
                parent.playSound(Model.CORRECT);
            }
            else {
                Hud.subScore();
                parent.playSound(Model.WRONG);
            }
        }

        else if(fb.getBody().getUserData() == "LIXEIRA_LATA") {
            Object userData = fa.getBody().getUserData();
            if ("LATA".equals(userData)) {
                Hud.addScore();
                parent.playSound(Model.CORRECT);
            }
            else {
                Hud.subScore();
                parent.playSound(Model.WRONG);
            }
        }

        if(fa.getBody().getUserData() == "LIXEIRA_VIDRO") {
            Object userData = fb.getBody().getUserData();
            if ("VIDRO".equals(userData)) {
                Hud.addScore();
                parent.playSound(Model.CORRECT);
            }
            else {
                Hud.subScore();
                parent.playSound(Model.WRONG);
            }
        }

        else if(fb.getBody().getUserData() == "LIXEIRA_VIDRO") {
            Object userData = fa.getBody().getUserData();
            if ("VIDRO".equals(userData)) {
                Hud.addScore();
                parent.playSound(Model.CORRECT);
            }
            else {
                Hud.subScore();
                parent.playSound(Model.WRONG);
            }
        }

        if (fb.getBody().getUserData() == "FLOOR") Hud.subScore();
        else if (fa.getBody().getUserData() == "FLOOR") Hud.subScore();


    }




    @Override
    public void endContact(Contact contact) {

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}