package com.recicle.game.views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.codeandweb.physicseditor.PhysicsShapeCache;
import com.recicle.game.Model;
import com.recicle.game.MainClass;

import java.util.HashMap;

public class PlayScreen implements Screen {

    public static final int WIDTH = 800;
    public static final int HEIGHT = 500;
    public static final String TITLE = "The Recicle Game";

    public static final int V_WIDTH = 80;
    public static final int V_HEIGHT = 50;

    static final float SCALE = 0.05f;

    private MainClass parent; // a field to store our orchestrator
    private Model model;
    private Hud hud;

    private OrthographicCamera cam;
    private FitViewport viewport;

    public SpriteBatch batch;
    private TextureAtlas atlas;
    private Texture backgroundImage;
    private TextureAtlas.AtlasRegion tcBTex, tcRTex, tcYTex, tcGTex;

    final HashMap<String, Sprite> sprites = new HashMap<String, Sprite>();

    private ShapeRenderer renderer;

    private Box2DDebugRenderer debugRenderer;


    public PlayScreen(MainClass mainClass) {
        cam = new OrthographicCamera();
        viewport = new FitViewport(V_WIDTH, V_HEIGHT, cam);

        batch = new SpriteBatch(); //A SpriteBatch is used to make the rendering of lots of images happen all at once
        renderer = new ShapeRenderer();
        //atlas = new TextureAtlas("images/sprites.atlas");

        parent = mainClass;
        model = new Model(viewport, batch, parent.assetsManager);
        hud = new Hud(mainClass, batch);

        backgroundImage = new Texture("images/bg.png");

        parent.assetsManager.queueAddImages();
        parent.assetsManager.manager.finishLoading();
        atlas = parent.assetsManager.manager.get("images/sprites.atlas");

        tcBTex = atlas.findRegion("tc1");
        tcRTex = atlas.findRegion("tc2");
        tcYTex = atlas.findRegion("tc3");
        tcGTex = atlas.findRegion("tc4");


        addSprites();

        debugRenderer = new Box2DDebugRenderer(true,true,true,true,true,true);
    }

    public void addSprites() {
        Array<TextureAtlas.AtlasRegion> regions = atlas.getRegions();

        for (TextureAtlas.AtlasRegion region : regions) {
            Sprite sprite = atlas.createSprite(region.name);

            float width = sprite.getWidth() * SCALE;
            float height = sprite.getHeight() * SCALE;

            sprite.setSize(width, height);
            sprite.setOrigin(0, 0);

            sprites.put(region.name, sprite);
        }
    }

    public void drawSprite(String name, float x, float y, float degrees) {
        Sprite sprite = sprites.get(name);

        sprite.setPosition(x, y);
        sprite.setRotation(degrees);

        sprite.draw(batch);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(model);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.setProjectionMatrix(cam.combined);

        model.logicStep(delta);

        batch.begin();

        batch.draw(backgroundImage, 0, 0, V_WIDTH, V_HEIGHT);

        batch.draw(tcBTex, model.trashCanB.getPosition().x - (model.TRASH_CAN_WIDTH / 2), model.trashCanB.getPosition().y - (model.TRASH_CAN_HEIGHT / 2), model.TRASH_CAN_WIDTH , model.TRASH_CAN_HEIGHT );
        batch.draw(tcRTex, model.trashCanR.getPosition().x - (model.TRASH_CAN_WIDTH / 2), model.trashCanR.getPosition().y - (model.TRASH_CAN_HEIGHT / 2), model.TRASH_CAN_WIDTH , model.TRASH_CAN_HEIGHT );
        batch.draw(tcYTex, model.trashCanY.getPosition().x - (model.TRASH_CAN_WIDTH / 2), model.trashCanY.getPosition().y - (model.TRASH_CAN_HEIGHT / 2), model.TRASH_CAN_WIDTH , model.TRASH_CAN_HEIGHT );
        batch.draw(tcGTex, model.trashCanG.getPosition().x - (model.TRASH_CAN_WIDTH / 2), model.trashCanG.getPosition().y - (model.TRASH_CAN_HEIGHT / 2), model.TRASH_CAN_WIDTH , model.TRASH_CAN_HEIGHT );

        if(model.touchedDown) {
            for (int i = 0; i < model.trashBodies.length; i++) {
                Body body = model.trashBodies[i];
                String name = model.names[i];

                Vector2 position = body.getPosition();
                float degrees = (float) Math.toDegrees(body.getAngle());
                drawSprite(name, position.x, position.y, degrees);
            }
        }

        batch.end();

        update(delta);
        hud.stage.draw();




        renderer.setProjectionMatrix(cam.combined);
        renderer.setColor(Color.ORANGE);

        renderer.begin(ShapeRenderer.ShapeType.Filled);

        renderer.circle(model.anchor.x,model.anchor.y,1);
        renderer.setColor(Color.WHITE);
        renderer.circle(model.firingPosition.x,model.firingPosition.y,1);
        renderer.line(model.firingPosition,model.anchor);

        renderer.end();

        debugRenderer.render(model.world, cam.combined);




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

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        batch.dispose();
        atlas.dispose();
        sprites.clear();
        model.world.dispose();
        debugRenderer.dispose();



    }
}
