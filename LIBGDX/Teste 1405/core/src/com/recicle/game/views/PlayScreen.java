package com.recicle.game.views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.recicle.game.B2dModel;
import com.recicle.game.MainClass;
import com.recicle.game.controllers.KeyboardController;

public class PlayScreen implements Screen {

    private MainClass parent; // a field to store our orchestrator
    OrthographicCamera cam;

    B2dModel model;
    KeyboardController controller;

    Box2DDebugRenderer debugRenderer;



    public PlayScreen(MainClass mainClass) {
        parent = mainClass;
        cam = new OrthographicCamera(32,24);

        controller = new KeyboardController();
        model = new B2dModel(controller, cam);

        debugRenderer = new Box2DDebugRenderer(true,true,true,true,true,true);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(controller); //set our controller as the class which processes inputs

    }

    @Override
    public void render(float delta) {
        model.logicStep(delta);
        Gdx.gl.glClearColor(0f, 0f, 0f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        debugRenderer.render(model.world, cam.combined);

    }

    @Override
    public void resize(int width, int height) {

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
