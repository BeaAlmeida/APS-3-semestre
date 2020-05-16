package com.pixmeg.catapult.model;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.Viewport;

public class CtptModel implements InputProcessor {
    private static final float MAX_STRENGTH = 80;
    private static final float MAX_DISTANCE = 10;

    private Viewport viewport;

    public World world;
    private BodyFactory bodyFactory;

    public Vector2 anchor;
    public Vector2 firingPosition;
    private float distance, angle;

    public CtptModel(Viewport viewport) {
        this.viewport = viewport;

        Box2D.init();
        world = new World(new Vector2(0, -50), true);

        bodyFactory = BodyFactory.getInstance(world);

        anchor = new Vector2(10,10);
        firingPosition = anchor.cpy();
    }

    public void logicStep(float delta) {
        world.step(1/60f, 3, 3);
    }

    private float angleBetweenTwoPoints(){
        float angle = MathUtils.atan2(anchor.y - firingPosition.y,anchor.x - firingPosition.x);
        angle %= MathUtils.PI2;

        if(angle < 0){
            angle += MathUtils.PI2;
        }

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

    public void createBall() {
        Body body = bodyFactory.makeCircleBody(anchor.x, anchor.y, 1, BodyDef.BodyType.DynamicBody);

        float velX = MAX_STRENGTH * MathUtils.cos(angle) * distance/MAX_DISTANCE;
        float velY = MAX_STRENGTH * MathUtils.sin(angle) * distance/MAX_DISTANCE;

        body.setLinearVelocity(velX, velY);
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
        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        createBall();
        firingPosition.set(anchor.cpy());
        return true;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        calculateAngleAndDistanceForBall(screenX, screenY);
        return true;
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
