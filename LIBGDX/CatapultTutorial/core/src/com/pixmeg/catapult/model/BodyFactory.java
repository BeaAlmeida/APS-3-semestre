package com.pixmeg.catapult.model;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;

public class BodyFactory {
    private static BodyFactory thisInstance = null;
    private World world;

    private BodyFactory(World world) {
        this.world = world;
    }

    public static BodyFactory getInstance(World world) {
        if (thisInstance == null) thisInstance = new BodyFactory(world);
        return thisInstance;
    }

    static public FixtureDef makeFixture(Shape shape) {
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1;

        return fixtureDef;
    }

    static public BodyDef makeBody(float posX, float posY, BodyDef.BodyType bodyType, boolean fixedRotation) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = bodyType;
        bodyDef.position.x = posX;
        bodyDef.position.y = posY;
        bodyDef.fixedRotation = fixedRotation;

        return bodyDef;
    }

    public Body makeRectangleBody(float posX, float posY, float width, float height, BodyDef.BodyType bodyType) {
        return makeRectangleBody(posX, posY, width, height, bodyType, false);
    }

    public Body makeRectangleBody(float posX, float posY, float width, float height, BodyDef.BodyType bodyType, boolean fixedRotation) {
        Body body = world.createBody(makeBody(posX, posY, bodyType, fixedRotation));

        PolygonShape polygon = new PolygonShape();
        polygon.setAsBox(width, height);

        body.createFixture(makeFixture(polygon));

        polygon.dispose();

        return body;
    }

    public Body makeCircleBody(float posX, float posY, float radius, BodyDef.BodyType bodyType) {
        return makeCircleBody(posX, posY, radius, bodyType, false);
    }

    public Body makeCircleBody(float posX, float posY, float radius, BodyDef.BodyType bodyType, boolean fixedRotation) {
        Body body = world.createBody(makeBody(posX, posY, bodyType, fixedRotation));

        CircleShape circle = new CircleShape();
        circle.setRadius(radius);

        body.createFixture(makeFixture(circle));

        circle.dispose();

        return body;
    }
}
