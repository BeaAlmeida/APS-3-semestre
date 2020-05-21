package com.recicle.game.views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.recicle.game.Model;
import com.recicle.game.MainClass;

public class PlayScreen implements Screen {

    private MainClass parent; // a field to store our orchestrator
    private Model model;
    private Hud hud;

    private OrthographicCamera cam;
    private ExtendViewport viewport;

    private SpriteBatch batch;
    private TextureAtlas.AtlasRegion trashCanTex;
    private TextureAtlas atlas;

    private ShapeRenderer renderer;

    private Box2DDebugRenderer debugRenderer;


    public PlayScreen(MainClass mainClass) {
        parent = mainClass;

        cam = new OrthographicCamera();
        viewport = new ExtendViewport(50, 50, cam);

        model = new Model(viewport, parent.assetsManager);

        batch = new SpriteBatch(); //A SpriteBatch is used to make the rendering of lots of images happen all at once
        batch.setProjectionMatrix(cam.combined);

        hud = new Hud(mainClass, batch);

        renderer = new ShapeRenderer();

        // tells our asset manger that we want to load the images set in loadImages method
        parent.assetsManager.queueAddImages();
        parent.assetsManager.manager.finishLoading();

        atlas = parent.assetsManager.manager.get("images/sprites.atlas");
        trashCanTex = atlas.findRegion("tc1");

        debugRenderer = new Box2DDebugRenderer(true,true,true,true,true,true);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(model);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        update(delta);
        //batch.setProjectionMatrix(hud.stage.getCamera().combined);

        hud.stage.draw();

        batch.begin();

        batch.draw(trashCanTex, model.TRASH_CAN_POSX - model.TRASH_CAN_WIDTH / 2 , model.TRASH_CAN_POSY - model.TRASH_CAN_HEIGHT / 2 , model.TRASH_CAN_WIDTH * 2 , model.TRASH_CAN_HEIGHT * 2);

        batch.end();

        renderer.setProjectionMatrix(cam.combined);
        renderer.setColor(Color.ORANGE);

        renderer.begin(ShapeRenderer.ShapeType.Filled);

        renderer.circle(model.anchor.x,model.anchor.y,1);
        renderer.setColor(Color.WHITE);
        renderer.circle(model.firingPosition.x,model.firingPosition.y,1);
        renderer.line(model.firingPosition,model.anchor);

        renderer.end();

        debugRenderer.render(model.world, cam.combined);

        model.logicStep(delta);

    }

    public void update(float dt){
        hud.update(dt);
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {
        batch.begin();
        //batch.draw(playerTex, model.player.getPosition().x,model.player.getPosition().y);
        batch.end();
    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        model.world.dispose();

    }
}
