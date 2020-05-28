package com.pixmeg.catapult.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.pixmeg.catapult.controller.CatapultTutorial;
import com.pixmeg.catapult.model.CtptModel;
import com.pixmeg.catapult.model.Hud;

public class MainScreen implements Screen {
    private CatapultTutorial parent;
    private CtptModel model;

    //private static final float W_WIDTH = 50;
    //private static final float W_HEIGHT = 50;

    private ExtendViewport viewport;
    private OrthographicCamera camera;
    private ShapeRenderer renderer;
    private Box2DDebugRenderer debugRenderer;
    private Hud hud;



    public MainScreen(CatapultTutorial catapultTutorial) {
        parent = catapultTutorial;

        camera = new OrthographicCamera();
        //viewport = new ExtendViewport(50, 50, camera);
        viewport = new ExtendViewport (CatapultTutorial.V_WIDTH,CatapultTutorial.V_HEIGHT, camera);

        renderer = new ShapeRenderer();
        hud = new Hud(parent.batch);

        model = new CtptModel(viewport);
        debugRenderer = new Box2DDebugRenderer();

    }



    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(model);
    }

    public void update(float dt){
        hud.update(dt);
        //if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
        Hud.addScore(200);
        //}

    }




    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        update(delta);

        parent.batch.setProjectionMatrix(hud.stage.getCamera().combined);
        hud.stage.draw();

        renderer.setProjectionMatrix(camera.combined);
        renderer.setColor(Color.ORANGE);

        renderer.begin(ShapeRenderer.ShapeType.Filled);

        renderer.circle(model.anchor.x,model.anchor.y,1);
        renderer.setColor(Color.WHITE);
        renderer.circle(model.firingPosition.x,model.firingPosition.y,1);
        renderer.line(model.firingPosition,model.anchor);

        renderer.end();

        debugRenderer.render(model.world, camera.combined);

        model.logicStep(delta);


        //Chama a tela GameOverScreen quando gameOver for true
        if (hud.gameOver()){
            parent.setScreen(new GameOverScreen(parent));
            dispose();
        }
    }


    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
    }
}
