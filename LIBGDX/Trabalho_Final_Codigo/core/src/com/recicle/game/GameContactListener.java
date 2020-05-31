package com.recicle.game;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.recicle.game.views.Hud;
import com.recicle.game.views.PlayScreen;

public class GameContactListener implements com.badlogic.gdx.physics.box2d.ContactListener {

    private void triggerTrashCan(Body body, String userData) {
        if (PlayScreen.addDeletedBody(body)) {
            if (userData.equals(body.getUserData())) {
                Hud.addScore();
            }
            else {
                Hud.subScore();
            }
        }
    }

    private void triggerBorder(Body body) {
        if (PlayScreen.addDeletedBody(body)) Hud.subScore();
    }

    @Override
    public void beginContact(Contact contact) {
        Body ba = contact.getFixtureA().getBody();
        Body bb = contact.getFixtureB().getBody();

        if(ba.getUserData() == "SENSOR_TRASH_CAN_PAPER") {
            triggerTrashCan(bb, "PAPER");
        }
        else if(bb.getUserData() == "SENSOR_TRASH_CAN_PAPER") {
            triggerTrashCan(ba, "PAPER");
        }

        if(ba.getUserData() == "SENSOR_TRASH_CAN_PLASTIC") {
            triggerTrashCan(bb, "PLASTIC");
        }
        else if(bb.getUserData() == "SENSOR_TRASH_CAN_PLASTIC") {
            triggerTrashCan(ba, "PLASTIC");
        }

        if(ba.getUserData() == "SENSOR_TRASH_CAN_METAL") {
            triggerTrashCan(bb, "METAL");
        }
        else if(bb.getUserData() == "SENSOR_TRASH_CAN_METAL") {
            triggerTrashCan(ba, "METAL");
        }

        if(ba.getUserData() == "SENSOR_TRASH_CAN_GLASS") {
            triggerTrashCan(bb, "GLASS");
        }
        else if(bb.getUserData() == "SENSOR_TRASH_CAN_GLASS") {
            triggerTrashCan(ba, "GLASS");
        }

        if (ba.getUserData() == "BORDER") {
            triggerBorder(bb);
        }
        else if (bb.getUserData() == "BORDER") {
            triggerBorder(ba);
        }
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
