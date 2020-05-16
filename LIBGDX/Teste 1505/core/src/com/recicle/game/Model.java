package com.recicle.game;

import com.badlogic.gdx.audio.Sound;
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
import com.recicle.game.loader.AssetsManager;


public class Model {
    //a model will control all the logic in our game

    public World world; //Box2D uses a world to keep all the physical objects in the game.
    private Box2DDebugRenderer debugRenderer;
    private KeyboardController cont;
    private OrthographicCamera cam;
    private AssetsManager assMan;

    private Body bodyd;
    private Body bodys;
    private Body bodyk;
    public Body player;
    private Body water;

    private Sound boing;
    private Sound boing2;
    public static final int BOING_SOUND = 0;
    public static final int BOING2_SOUND = 1;

    public boolean isSwimming = false;
    //A flag is a boolean value in your code that is either on or off and is used to store the state of some object.


    public Model(KeyboardController controller, OrthographicCamera camera, AssetsManager assetsManager) {
        assMan = new AssetsManager();
        assMan = assetsManager;

        cont = new KeyboardController();
        cont = controller; //Now we can access our controller in our model,

        cam = new OrthographicCamera();
        cam = camera;

        world = new World(new Vector2(0,-10f), true);
        world.setContactListener(new ContactListener(this)); //We have to tell the world to use our contact listener

        BodyFactory bodyFactory = BodyFactory.getInstance(world); // get our body factory singleton and store it in bodyFactory

        createFloor();

        // add a player
        player = bodyFactory.makeBoxPolyBody(1, 1, 2, 2, BodyFactory.RUBBER, BodyDef.BodyType.DynamicBody,false);
        player.setUserData("IAMTHEPLAYER");

        // add some water
        water =  bodyFactory.makeBoxPolyBody(1, -8, 40, 4, BodyFactory.RUBBER, BodyDef.BodyType.StaticBody,false);
        water.setUserData("IAMTHESEA");
        bodyFactory.makeAllFixturesSensors(water); // make the water a sensor so it doesn't obstruct our player

        assetsManager.queueAddSounds();
        assetsManager.manager.finishLoading();
        boing = assetsManager.manager.get("sounds/boing.mp3", Sound.class);
        boing2 = assetsManager.manager.get("sounds/boing2.mp3", Sound.class);

    }

    //Our game logic here
    public void logicStep(float delta){ //a world step tells the Box2D world to move forward in time

        if(cont.left) {
            player.applyForceToCenter(-100, 0,true);
            System.out.println("Left");
        }
        else if(cont.right) {
            player.applyForceToCenter(100, 0,true);
            System.out.println("Right");
        }
        else if(cont.up) {
            player.applyForceToCenter(0, 100,true);
            System.out.println("Up");
        }
        else if(cont.down) {
            player.applyForceToCenter(0, -100,true);
            System.out.println("Down");
        }

        if(isSwimming) {
            player.applyForceToCenter(0, 40, true);
            System.out.println("Swimming");
        }

        if(cont.isMouse1Down && pointIntersectsBody(player,cont.mouseLocation)){
            System.out.println("Player was clicked");
        }

        world.step(delta , 3, 3);
        //The first argument is the time-step, or the amount of time you want your world to simulate.

    }

    public boolean pointIntersectsBody(Body body, Vector2 mouseLocation){
        Vector3 mousePos = new Vector3(mouseLocation,0); //convert mouseLocation to 3D position as the unproject method requires a 3D Vector
        cam.unproject(mousePos); // convert from screen potition to world position
        if(body.getFixtureList().first().testPoint(mousePos.x, mousePos.y)){
            return true;
        }

        return false;
    }

    public void playSound(int sound){
        switch(sound){
            case BOING_SOUND:
                boing.play();
                break;
            case BOING2_SOUND:
                boing2.play();
                break;
        }
    }

    private void createObject() {
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
