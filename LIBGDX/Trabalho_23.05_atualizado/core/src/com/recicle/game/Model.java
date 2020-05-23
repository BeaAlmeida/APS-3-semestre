package com.recicle.game;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.audio.Sound;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import com.badlogic.gdx.utils.viewport.Viewport;
import com.codeandweb.physicseditor.PhysicsShapeCache;
import com.recicle.game.loader.AssetsManager;
import com.recicle.game.views.PlayScreen;

import java.util.Random;


public class Model implements InputProcessor {
    //a model will control all the logic in our game

    private AssetsManager assMan;
    private BodyFactory bodyFactory;
    private ContactListener contList;

    private SpriteBatch batch;

    private FitViewport viewport;

    public World world; //Box2D uses a world to keep all the physical objects in the game.
    private Box2DDebugRenderer debugRenderer;

    private Body floor;
    public Body trashCanB; //Lixeira Papel
    public Body trashCanR; //Lixeira Plástico
    public Body trashCanY; //Lixeira Metal
    public Body trashCanG; //Lixeira Vidro
    public Body bracoRobozin;


    //Catapult
    private static final float MAX_STRENGTH = 70;
    private static final float MAX_DISTANCE = 10;
    public Vector2 anchor;
    public Vector2 firingPosition;
    private float distance, angle;


    private Sound tictac;
    private Sound correct;
    private Sound wrong;
    public static final int TICTAC = 0;
    public static final int CORRECT = 1;
    public static final int WRONG = 2;

    public static final float
            TRASH_CAN_WIDTH = 9,
            TRASH_CAN_HEIGHT = 12,
            TRASH_CAN_POSX = 40,
            TRASH_CAN_POSY = 10;

    public PhysicsShapeCache physicsBodies;
    static final float SCALE = 0.05f;

    static final int COUNT = 1;
    public Body[] trashBodies = new Body[COUNT];
    public String[] names = new String[COUNT];
    public int index;
    String name = new String();

    public boolean touchedDown = false;

    public Array<Body> bodies = new Array<Body>();


    public Model(FitViewport viewport, SpriteBatch batch, AssetsManager assMan) {
        this.assMan = assMan;
        this.viewport = viewport;
        this.batch = batch;

        contList = new ContactListener(this);

        world = new World(new Vector2(0,-50f), true);
        world.setContactListener(new ContactListener(this)); //We have to tell the world to use our contact listener

        bodyFactory = BodyFactory.getInstance(world); // get our body factory singleton and store it in bodyFactory
        physicsBodies = new PhysicsShapeCache("physics.xml");


        //create floor
        floor = bodyFactory.makeBoxPolyBody(0, 0, 500, 1, BodyDef.BodyType.StaticBody);
        floor.setUserData("FLOOR");


        trashCanB = bodyFactory.makeBoxPolyBody(TRASH_CAN_POSX, TRASH_CAN_POSY, TRASH_CAN_WIDTH, TRASH_CAN_HEIGHT, BodyDef.BodyType.StaticBody);
        trashCanR = bodyFactory.makeBoxPolyBody(TRASH_CAN_POSX + 10, TRASH_CAN_POSY, TRASH_CAN_WIDTH, TRASH_CAN_HEIGHT, BodyDef.BodyType.StaticBody);
        trashCanY = bodyFactory.makeBoxPolyBody(TRASH_CAN_POSX + 20, TRASH_CAN_POSY, TRASH_CAN_WIDTH, TRASH_CAN_HEIGHT, BodyDef.BodyType.StaticBody);
        trashCanG = bodyFactory.makeBoxPolyBody(TRASH_CAN_POSX + 30, TRASH_CAN_POSY, TRASH_CAN_WIDTH, TRASH_CAN_HEIGHT, BodyDef.BodyType.StaticBody);

        trashCanB.setUserData("LIXEIRA_PAPEL");
        trashCanR.setUserData("LIXEIRA_PLASTICO");
        trashCanY.setUserData("LIXEIRA_METAL");
        trashCanG.setUserData("LIXEIRA_VIDRO");

        //catapult
        anchor = new Vector2(10,10);
        firingPosition = anchor.cpy();



        assMan.queueAddSounds();
        assMan.manager.finishLoading();
        tictac = assMan.manager.get("sounds/tictac.mp3", Sound.class);
        correct = assMan.manager.get("sounds/correct.mp3", Sound.class);
        wrong = assMan.manager.get("sounds/wrong.mp3", Sound.class);
    }

    //Our game logic here
    public void logicStep(float delta){ //a world step tells the Box2D world to move forward in time
        world.step(delta , 3, 3); //The first argument is the time-step, or the amount of time you want your world to simulate.


    }

    private Body createBody(String name, float x, float y, float rotation) {
        Body body = physicsBodies.createBody(name, world, SCALE, SCALE);
        body.setTransform(x, y, rotation);

        return body;
    }

    public void generateTrash() {
        String[] trashNames = new String[]{ "papel1", "papel2", "papel3", "papel4", "papel5", "lata1", "lata2", "lata3", "lata4", "vidro1", "vidro2", "vidro3", "vidro4"};



        Random random = new Random();

        for (int index = 0; index < trashBodies.length; index++) {
            name = trashNames[random.nextInt(trashNames.length)];

            names[index] = name;

            trashBodies[index] = createBody(name, anchor.x, anchor.y, 0);
            trashBodies[index].setType(BodyDef.BodyType.StaticBody);

            if(name.contains("papel")) trashBodies[index].setUserData("PAPEL");
            if(name.contains("plastico")) trashBodies[index].setUserData("PLASTICO");
            if(name.contains("lata")) trashBodies[index].setUserData("LATA");
            if(name.contains("vidro")) trashBodies[index].setUserData("VIDRO");


        }


    }


    public void playSound(int sound){
        switch(sound){
            case TICTAC:
                tictac.play();
                break;
            case CORRECT:
                correct.play();
                break;
            case WRONG:
                wrong.play();
                break;
        }
    }

    public float angleBetweenTwoPoints(){
        float angle = MathUtils.atan2(anchor.y - firingPosition.y,anchor.x - firingPosition.x);
        angle %= MathUtils.PI2;

        if(angle < 0) angle += MathUtils.PI2;

        return angle;

    }



    public float distanceBetweenTwoPoints(){
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


    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        generateTrash();

        touchedDown = true;
        System.out.println(name);

        calculateAngleAndDistanceForBall(screenX,screenY);

        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        float velX = MAX_STRENGTH * MathUtils.cos(angle) * distance/MAX_DISTANCE;
        float velY = MAX_STRENGTH * MathUtils.sin(angle) * distance/MAX_DISTANCE;

        trashBodies[index].setType(BodyDef.BodyType.DynamicBody);
        trashBodies[index].setLinearVelocity(velX, velY);
        firingPosition.set(anchor.cpy());

        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        trashBodies[index].setTransform(firingPosition.x, firingPosition.y, 0);

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


}


