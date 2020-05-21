package com.recicle.game;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.audio.Sound;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import com.badlogic.gdx.utils.viewport.Viewport;
import com.recicle.game.loader.AssetsManager;


public class Model implements InputProcessor {
    //a model will control all the logic in our game

    private AssetsManager assMan;
    private BodyFactory bodyFactory;
    private ContactListener contList;

    private Viewport viewport;

    public World world; //Box2D uses a world to keep all the physical objects in the game.
    private Box2DDebugRenderer debugRenderer;

    private Body floor;
    public Body trashCan;

    //Catapult
    private static final float MAX_STRENGTH = 70;
    private static final float MAX_DISTANCE = 10;
    public Vector2 anchor;
    public Vector2 firingPosition;
    private float distance, angle;


    private Sound boing;
    private Sound boing2;
    public static final int BOING_SOUND = 0;
    public static final int BOING2_SOUND = 1;

    public static final int TRASH_CAN_WIDTH = 10;
    public static final int TRASH_CAN_HEIGHT = 16;
    public static final int TRASH_CAN_POSX = 32;
    public static final int TRASH_CAN_POSY = 2;


    public Model(Viewport viewport, AssetsManager assMan) {
        this.assMan = assMan;
        this.viewport = viewport;

        contList = new ContactListener(this);

        world = new World(new Vector2(0,-50f), true);
        world.setContactListener(new ContactListener(this)); //We have to tell the world to use our contact listener

        bodyFactory = BodyFactory.getInstance(world); // get our body factory singleton and store it in bodyFactory

        //create floor
        floor = bodyFactory.makeBoxPolyBody(0, 0, 500, 2, BodyDef.BodyType.StaticBody);

        trashCan = bodyFactory.makeBoxPolyBody(TRASH_CAN_POSX, TRASH_CAN_POSY, TRASH_CAN_WIDTH, TRASH_CAN_HEIGHT, BodyDef.BodyType.StaticBody);

        //catapult
        anchor = new Vector2(10,10);
        firingPosition = anchor.cpy();

        assMan.queueAddSounds();
        assMan.manager.finishLoading();
        boing = assMan.manager.get("sounds/boing.mp3", Sound.class);
        boing2 = assMan.manager.get("sounds/boing2.mp3", Sound.class);

    }

    //Our game logic here
    public void logicStep(float delta){ //a world step tells the Box2D world to move forward in time
        world.step(delta , 3, 3); //The first argument is the time-step, or the amount of time you want your world to simulate.


    }

    private float angleBetweenTwoPoints(){
        float angle = MathUtils.atan2(anchor.y - firingPosition.y,anchor.x - firingPosition.x);
        angle %= MathUtils.PI2;

        if(angle < 0) angle += MathUtils.PI2;

        return angle;

    }

    private float distanceBetweenTwoPoints(){
        return (float)Math.sqrt((anchor.x-firingPosition.x)*(anchor.x-firingPosition.x) + (anchor.y-firingPosition.y)*(anchor.y-firingPosition.y));
    }

    public void calculateAngleAndDistanceForBall(int screenX, int screenY){
        firingPosition.set(screenX,screenY);
        viewport.unproject(firingPosition);

        distance = distanceBetweenTwoPoints();
        angle = angleBetweenTwoPoints();

        if(distance > MAX_DISTANCE){
            distance = MAX_DISTANCE;
        }

        firingPosition.set(anchor.x + distance*-MathUtils.cos(angle),(anchor.y + distance*-MathUtils.sin(angle)));
    }

    public void createBall() {
        Body ball = bodyFactory.makeCirclePolyBody(anchor.x, anchor.y, 1, BodyDef.BodyType.DynamicBody);

        float velX = MAX_STRENGTH * MathUtils.cos(angle) * distance/MAX_DISTANCE;
        float velY = MAX_STRENGTH * MathUtils.sin(angle) * distance/MAX_DISTANCE;

        ball.setLinearVelocity(velX, velY);

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


    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        calculateAngleAndDistanceForBall(screenX,screenY);
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        createBall();
        firingPosition.set(anchor.cpy());
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        calculateAngleAndDistanceForBall(screenX, screenY);
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
