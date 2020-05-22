package com.recicle.game;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;

public class BodyFactory {

    private static BodyFactory thisInstance;
    private World world;

    public static final int STEEL = 0;
    public static final int WOOD = 1;
    public static final int RUBBER = 2;
    public static final int STONE = 3;

    private final float DEGTORAD = 0.0174533f;

    private BodyFactory(World world){ this.world = world; }

    public static BodyFactory getInstance(World world){
        if(thisInstance == null) thisInstance = new BodyFactory(world);
        return thisInstance;
    }

    //A body is just the data part and the fixtures are the physical objects that collide.

    static public FixtureDef makeFixture(Shape shape){
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1f;
        fixtureDef.friction = 1f;
        fixtureDef.restitution = 0.2f;

        return fixtureDef;
    }

    public Body makeCirclePolyBody(float posx, float posy, float radius, BodyDef.BodyType bodyType){
        return makeCirclePolyBody( posx,  posy,  radius, bodyType,  false);
    }

    public Body makeCirclePolyBody(float posx, float posy, float radius, BodyDef.BodyType bodyType, boolean fixedRotation){
        BodyDef boxBodyDef = new BodyDef();
        boxBodyDef.type = bodyType;
        boxBodyDef.position.x = posx;
        boxBodyDef.position.y = posy;
        boxBodyDef.fixedRotation = fixedRotation;

        Body boxBody = world.createBody(boxBodyDef);
        CircleShape circleShape = new CircleShape();
        circleShape.setRadius(radius);
        boxBody.createFixture(makeFixture(circleShape));
        circleShape.dispose();
        return boxBody;

    }


    public Body makeBoxPolyBody(float posx, float posy, float width, float height, BodyDef.BodyType bodyType){
        return makeBoxPolyBody(posx, posy, width, height, bodyType, false);
    }


    public Body makeBoxPolyBody(float posx, float posy, float width, float height, BodyDef.BodyType bodyType, boolean fixedRotation){

        BodyDef boxBodyDef = new BodyDef();
        boxBodyDef.type = bodyType;
        boxBodyDef.position.x = posx;
        boxBodyDef.position.y = posy;
        boxBodyDef.fixedRotation = fixedRotation;

        Body boxBody = world.createBody(boxBodyDef);
        PolygonShape poly = new PolygonShape();
        poly.setAsBox(width/2, height/2);
        boxBody.createFixture(makeFixture(poly));
        poly.dispose();
        return boxBody;

    }


}
