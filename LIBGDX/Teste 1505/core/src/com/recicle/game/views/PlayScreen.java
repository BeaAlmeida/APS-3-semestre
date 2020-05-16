package com.recicle.game.views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.recicle.game.Model;
import com.recicle.game.MainClass;
import com.recicle.game.controllers.KeyboardController;

public class PlayScreen implements Screen {

    private MainClass parent; // a field to store our orchestrator
    OrthographicCamera cam;

    SpriteBatch batch;
    TextureAtlas.AtlasRegion playerTex;
    TextureAtlas atlas;

    Model model;
    KeyboardController controller;

    Box2DDebugRenderer debugRenderer;



    public PlayScreen(MainClass mainClass) {
        parent = mainClass;
        cam = new OrthographicCamera(32,24);

        batch = new SpriteBatch(); //A SpriteBatch is used to make the rendering of lots of images happen all at once
        batch.setProjectionMatrix(cam.combined);

        controller = new KeyboardController();
        model = new Model(controller, cam, parent.assetsManager);

        // tells our asset manger that we want to load the images set in loadImages method
        parent.assetsManager.queueAddImages();
        parent.assetsManager.manager.finishLoading();
        atlas = parent.assetsManager.manager.get("images/game.atlas"); // new
        playerTex = atlas.findRegion("player"); // updated

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

        batch.begin();
        batch.draw(playerTex, model.player.getPosition().x - 1,model.player.getPosition().y - 1,2, 2);
        batch.end();

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
        batch.begin();
        batch.draw(playerTex, model.player.getPosition().x,model.player.getPosition().y);
        batch.end();
    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
