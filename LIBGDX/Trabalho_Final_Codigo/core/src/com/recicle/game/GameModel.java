package com.recicle.game;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.codeandweb.physicseditor.PhysicsShapeCache;
import com.recicle.game.loader.AssetsManager;
import com.recicle.game.views.PlayScreen;

import java.util.Random;


public class GameModel implements InputProcessor {
    //Esta classe controla toda a lógica do jogo

    private FitViewport viewport;

    public World world; //Box2D uses a world to keep all the physical objects in the game.

    //Catapult
    private static final float MAX_STRENGTH = 70;
    private static final float MAX_DISTANCE = 9;
    public Vector2 anchor;
    public Vector2 firingPosition;
    private float distance, angle;

    //Sounds
    private static Sound tictac;
    private static Sound correct;
    private static Sound wrong;
    private static final int TICTAC = 0;
    public static final int CORRECT = 1;
    public static final int WRONG = 2;


    //Lixeiras e Lixos
    public Body trashCanB; //Lixeira Papel
    public Body trashCanR; //Lixeira Plástico
    public Body trashCanY; //Lixeira Metal
    public Body trashCanG; //Lixeira Vidro

    public static final float
            TRASH_CAN_WIDTH = 9,
            TRASH_CAN_HEIGHT = 12;
    private static final float
            TRASH_CAN_POSX = 32,
            TRASH_CAN_POSY = 7;

    private PhysicsShapeCache physicsBodies;
    public static final float SCALE = 0.042f;

    private static final int COUNT = 1;
    public Body[] trashBodies = new Body[COUNT];
    public String[] names = new String[COUNT];
    public int index;

    private boolean touchedDown = false;
    private Vector2 mouseLocation = new Vector2();


    public GameModel(FitViewport viewport, AssetsManager assMan) {
        this.viewport = viewport;

        world = new World(new Vector2(0,-50f), true);
        world.setContactListener(new GameContactListener()); //We have to tell the world to use our contact listener

        BodyFactory bodyFactory = BodyFactory.getInstance(world); // get our body factory singleton and store it in bodyFactory
        physicsBodies = new PhysicsShapeCache("physics.xml");

        //create floor
        Body floor = bodyFactory.makeBoxPolyBody(PlayScreen.V_WIDTH / 2f, 0, PlayScreen.V_WIDTH, 1, BodyDef.BodyType.StaticBody);
        Body lWall = bodyFactory.makeBoxPolyBody(0, PlayScreen.V_HEIGHT / 2f, 1, PlayScreen.V_HEIGHT, BodyDef.BodyType.StaticBody);
        Body rWall = bodyFactory.makeBoxPolyBody(PlayScreen.V_WIDTH + 6, PlayScreen.V_HEIGHT / 2f, 1, PlayScreen.V_HEIGHT, BodyDef.BodyType.StaticBody);

        floor.setUserData("BORDER");
        lWall.setUserData("BORDER");
        rWall.setUserData("BORDER");

        trashCanB = bodyFactory.makeBoxPolyBody(TRASH_CAN_POSX, TRASH_CAN_POSY, TRASH_CAN_WIDTH, TRASH_CAN_HEIGHT, BodyDef.BodyType.StaticBody);
        trashCanR = bodyFactory.makeBoxPolyBody(TRASH_CAN_POSX + 13, TRASH_CAN_POSY, TRASH_CAN_WIDTH, TRASH_CAN_HEIGHT, BodyDef.BodyType.StaticBody);
        trashCanY = bodyFactory.makeBoxPolyBody(TRASH_CAN_POSX + 26, TRASH_CAN_POSY, TRASH_CAN_WIDTH, TRASH_CAN_HEIGHT, BodyDef.BodyType.StaticBody);
        trashCanG = bodyFactory.makeBoxPolyBody(TRASH_CAN_POSX + 39, TRASH_CAN_POSY, TRASH_CAN_WIDTH, TRASH_CAN_HEIGHT, BodyDef.BodyType.StaticBody);

        Body trashCanSensorB = bodyFactory.makeBoxPolyBody(TRASH_CAN_POSX, TRASH_CAN_POSY + TRASH_CAN_HEIGHT / 2, TRASH_CAN_WIDTH * 0.6f, 0.025f, BodyDef.BodyType.StaticBody);
        Body trashCanSensorR = bodyFactory.makeBoxPolyBody(TRASH_CAN_POSX + 13, TRASH_CAN_POSY + TRASH_CAN_HEIGHT / 2, TRASH_CAN_WIDTH * 0.6f, 0.025f, BodyDef.BodyType.StaticBody);
        Body trashCanSensorY = bodyFactory.makeBoxPolyBody(TRASH_CAN_POSX + 26, TRASH_CAN_POSY + TRASH_CAN_HEIGHT / 2, TRASH_CAN_WIDTH * 0.6f, 0.025f, BodyDef.BodyType.StaticBody);
        Body trashCanSensorG = bodyFactory.makeBoxPolyBody(TRASH_CAN_POSX + 39, TRASH_CAN_POSY + TRASH_CAN_HEIGHT / 2, TRASH_CAN_WIDTH * 0.6f, 0.025f, BodyDef.BodyType.StaticBody);

        trashCanSensorB.setUserData("SENSOR_TRASH_CAN_PAPER");
        trashCanSensorR.setUserData("SENSOR_TRASH_CAN_PLASTIC");
        trashCanSensorY.setUserData("SENSOR_TRASH_CAN_METAL");
        trashCanSensorG.setUserData("SENSOR_TRASH_CAN_GLASS");

        //catapult
        anchor = new Vector2(10,10);
        //firingPosition = anchor.cpy();


        //Sounds
        assMan.queueAddSounds();
        assMan.manager.finishLoading();
        tictac = assMan.manager.get("sounds/tictac.mp3", Sound.class);
        correct = assMan.manager.get("sounds/correct.mp3", Sound.class);
        wrong = assMan.manager.get("sounds/wrong.mp3", Sound.class);
    }


    public void logicStep(float delta){
        //World.step diz ao mundo Box2D simular através do tempo
        world.step(delta , 3, 3);

    }

    private Body createBody(String name, float x, float y) {
        Body body = physicsBodies.createBody(name, world, SCALE, SCALE);
        body.setTransform(x, y, (float) 0);

        return body;
    }

    public void generateTrash() {
        String[] trashNames = new String[]{ "papel1", "papel2", "papel4", "papel5", "plastico1", "plastico2", "plastico3", "lata1", "lata2", "lata3", "lata4", "vidro1", "vidro2", "vidro3", "vidro4"};

        Random random = new Random();

        for (int index = 0; index < trashBodies.length; index++) {
            String name = trashNames[random.nextInt(trashNames.length)];

            names[index] = name;

            trashBodies[index] = createBody(name, anchor.x, anchor.y);
            trashBodies[index].setType(BodyDef.BodyType.StaticBody);

            if(name.contains("papel")) trashBodies[index].setUserData("PAPER");
            if(name.contains("plastico")) trashBodies[index].setUserData("PLASTIC");
            if(name.contains("lata")) trashBodies[index].setUserData("METAL");
            if(name.contains("vidro")) trashBodies[index].setUserData("GLASS");

        }

    }


    public static void playSound(int sound){
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

    private boolean pointIntersectsBody(Body body, Vector2 mouseLocation) {
        viewport.unproject(mouseLocation);

        for (Fixture fixture : body.getFixtureList()) {
            if (fixture.testPoint(mouseLocation.x, mouseLocation.y)) return true;

        }

        return false;
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

    private void calculateAngleAndDistanceForBall(int screenX, int screenY){
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
        mouseLocation.x = screenX;
        mouseLocation.y = screenY;

        if (pointIntersectsBody(trashBodies[index], mouseLocation) && trashBodies[index].getType() == BodyDef.BodyType.StaticBody) {
            calculateAngleAndDistanceForBall(screenX,screenY);
            touchedDown = true;
        }

        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if (touchedDown) {
            float velX = MAX_STRENGTH * MathUtils.cos(angle) * distance/MAX_DISTANCE;
            float velY = MAX_STRENGTH * MathUtils.sin(angle) * distance/MAX_DISTANCE;

            trashBodies[index].setType(BodyDef.BodyType.DynamicBody);
            trashBodies[index].setLinearVelocity(velX, velY);
            firingPosition.set(anchor.cpy());

            touchedDown = false;
        }

        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        if (touchedDown) {
            trashBodies[index].setTransform(firingPosition.x, firingPosition.y, 0);

            calculateAngleAndDistanceForBall(screenX, screenY);
        }

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


