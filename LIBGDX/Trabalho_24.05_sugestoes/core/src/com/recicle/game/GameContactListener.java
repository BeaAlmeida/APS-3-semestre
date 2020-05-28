package com.recicle.game;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.recicle.game.views.Hud;
import com.recicle.game.views.PlayScreen;

public class GameContactListener implements com.badlogic.gdx.physics.box2d.ContactListener {

    private GameModel parent;
    private AppPreferences preferences;


    public GameContactListener(GameModel parent) {
        this.parent = parent;
        preferences = new AppPreferences();
    }

    private void deleteBody(Fixture fixture) {
        if (!PlayScreen.isDeletedBody(fixture.getBody())) {
            PlayScreen.addDeletedBody(fixture.getBody());
        }
    }

    @Override
    public void beginContact(Contact contact) {
            Fixture fa = contact.getFixtureA();
            Fixture fb = contact.getFixtureB();

        if(fa.getBody().getType() == BodyDef.BodyType.DynamicBody && fb.getBody().getUserData() != "LIXEIRA"){
            deleteBody(fa);
        }
        else if (fb.getBody().getType() == BodyDef.BodyType.DynamicBody && fa.getBody().getUserData() != "LIXEIRA"){
            deleteBody(fb);
        }

        if(fa.getBody().getUserData() == "SENSOR_LIXEIRA_PAPEL") {
            Object userData = fb.getBody().getUserData();
            if ("PAPEL".equals(userData)) Hud.addScore();
            else Hud.subScore();
        }

        else if(fb.getBody().getUserData() == "SENSOR_LIXEIRA_PAPEL") {
            Object userData = fa.getBody().getUserData();
            if ("PAPEL".equals(userData)) Hud.addScore();
            else Hud.subScore();
        }

        if(fa.getBody().getUserData() == "SENSOR_LIXEIRA_PLASTICO") {
            Object userData = fb.getBody().getUserData();
            if ("PLASTICO".equals(userData)) Hud.addScore();
            else Hud.subScore();
        }

        else if(fb.getBody().getUserData() == "SENSOR_LIXEIRA_PLASTICO") {
            Object userData = fa.getBody().getUserData();
            if ("PLASTICO".equals(userData)) Hud.addScore();
            else Hud.subScore();
        }

        if(fa.getBody().getUserData() == "SENSOR_LIXEIRA_METAL") {
            Object userData = fb.getBody().getUserData();
            if ("LATA".equals(userData)) Hud.addScore();
            else Hud.subScore();
        }

        else if(fb.getBody().getUserData() == "SENSOR_LIXEIRA_METAL") {
            Object userData = fa.getBody().getUserData();
            if ("LATA".equals(userData)) Hud.addScore();
            else Hud.subScore();
        }

        if(fa.getBody().getUserData() == "SENSOR_LIXEIRA_VIDRO") {
            Object userData = fb.getBody().getUserData();
            if ("VIDRO".equals(userData)) Hud.addScore();
            else Hud.subScore();
        }

        else if(fb.getBody().getUserData() == "SENSOR_LIXEIRA_VIDRO") {
            Object userData = fa.getBody().getUserData();
            if ("VIDRO".equals(userData)) Hud.addScore();
            else Hud.subScore();
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
