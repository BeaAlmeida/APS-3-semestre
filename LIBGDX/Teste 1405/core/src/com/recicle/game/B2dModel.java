package com.recicle.game;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.recicle.game.controllers.KeyboardController;


public class B2dModel { //a model will control all the logic in our game

    public World world; //Box2D uses a world to keep all the physical objects in the game.
    private Box2DDebugRenderer debugRenderer;
    private KeyboardController controller;
    private OrthographicCamera camera;

    private Body bodyd;
    private Body bodys;
    private Body bodyk;
    private Body player;
    private Body water;

    public boolean isSwimming = false;
    //A flag is a boolean value in your code that is either on or off and is used to store the state of some object.


    public B2dModel(KeyboardController cont, OrthographicCamera cam) {
        controller = new KeyboardController();
        controller = cont; //Now we can access our controller in our model,

        world = new World(new Vector2(0,-10f), true);
        world.setContactListener(new B2dContactListener(this)); //We have to tell the world to use our contact listener

        camera = new OrthographicCamera();
        camera = cam;
        createFloor();
//        createObject();
//        createMovingObject();

        // get our body factory singleton and store it in bodyFactory
        BodyFactory bodyFactory = BodyFactory.getInstance(world);

        // add a player
        player = bodyFactory.makeBoxPolyBody(1, 1, 2, 2, 5, BodyDef.BodyType.DynamicBody,false);
        player.setUserData("IAMTHEPLAYER");

        // add some water
        water =  bodyFactory.makeBoxPolyBody(1, -8, 40, 4, BodyFactory.RUBBER, BodyDef.BodyType.StaticBody,false);
        water.setUserData("IAMTHESEA");
        // make the water a sensor so it doesn't obstruct our player
        bodyFactory.makeAllFixturesSensors(water);


    }

    //Our game logic here
    public void logicStep(float delta){ //a world step tells the Box2D world to move forward in time

        if(controller.left) {
            player.applyForceToCenter(-100, 0,true);
            System.out.println("Left");
        }
        else if(controller.right) {
            player.applyForceToCenter(100, 0,true);
            System.out.println("Right");
        }
        else if(controller.up) {
            player.applyForceToCenter(0, 500,true);
            System.out.println("Up");
        }
        else if(controller.down) {
            player.applyForceToCenter(0, -100,true);
            System.out.println("Down");
        }

        if(isSwimming) {
            player.applyForceToCenter(0, 40, true);
            System.out.println("Swimming");
        }

        if(controller.isMouse1Down && pointIntersectsBody(player,controller.mouseLocation)){
            System.out.println("Player was clicked");
        }

        world.step(delta , 3, 3);
        //The first argument is the time-step, or the amount of time you want your world to simulate.

    }

    public boolean pointIntersectsBody(Body body, Vector2 mouseLocation){
        Vector3 mousePos = new Vector3(mouseLocation,0); //convert mouseLocation to 3D position as the unproject method requires a 3D Vector
        camera.unproject(mousePos); // convert from screen potition to world position
        if(body.getFixtureList().first().testPoint(mousePos.x, mousePos.y)){
            return true;
        }

        return false;
    }

    private void createObject(){

        //create a new body definition (type and location)
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(0,0);


        // add it to the world
        bodyd = world.createBody(bodyDef);

        // set the shape (here we use a box 50 meters wide, 1 meter tall )
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(1,1);

        // set the properties of the object ( shape, weight, restitution(bouncyness)
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1f;

        // create the physical object in our body)
        // without this our body would just be data in the world
        bodyd.createFixture(shape, 0.0f);

        // we no longer use the shape object here so dispose of it.
        shape.dispose();
    }

    private void createFloor() {
    //The static body is almost identical to our dynamic body except we don’t need to define a density and we can just use the shape in our body’s createFixture method.

        // create a new body definition (type and location)
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(0, -10);
        // add it to the world
        bodys = world.createBody(bodyDef);
        // set the shape (here we use a box 50 meters wide, 1 meter tall )
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(50, 1);
        // create the physical object in our body)
        // without this our body would just be data in the world
        bodys.createFixture(shape, 0.0f);
        // we no longer use the shape object here so dispose of it.
        shape.dispose();
    }

    private void createMovingObject(){
    //This is exactly the same as our dynamic code except we have set the linear velocity.

        //create a new body definition (type and location)
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.KinematicBody;
        bodyDef.position.set(0,-12);


        // add it to the world
        bodyk = world.createBody(bodyDef);

        // set the shape (here we use a box 50 meters wide, 1 meter tall )
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(1,1);

        // set the properties of the object ( shape, weight, restitution(bouncyness)
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1f;

        // create the physical object in our body)
        // without this our body would just be data in the world
        bodyk.createFixture(shape, 0.0f);

        // we no longer use the shape object here so dispose of it.
        shape.dispose();

        bodyk.setLinearVelocity(0, 0.75f);
    }

}
