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
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.recicle.game.GameModel;
import com.recicle.game.MainClass;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class PlayScreen implements Screen {

    public static final int WIDTH = 800;
    public static final int HEIGHT = 500;
    public static final String TITLE = "The Recicle Game";

    public static final int V_WIDTH = 80;
    public static final int V_HEIGHT = 50;

    private MainClass parent; // a field to store our orchestrator
    private GameModel model;

    private OrthographicCamera cam;
    private FitViewport viewport;

    private SpriteBatch batch;
    private TextureAtlas atlas;
    private Texture backgroundImage;
    private TextureAtlas.AtlasRegion tcBTex, tcRTex, tcYTex, tcGTex, robozin;

    private final HashMap<String, Sprite> sprites = new HashMap<>();

    private ShapeRenderer renderer;

    private Box2DDebugRenderer debugRenderer;

    private static Set<Body> toBeDeletedBodies = new HashSet<>();

    private Array<Body> disposableBodies = new Array<>();


    public PlayScreen(MainClass mainClass) {
        cam = new OrthographicCamera();
        viewport = new FitViewport(V_WIDTH, V_HEIGHT, cam);

        batch = new SpriteBatch(); //A SpriteBatch is used to make the rendering of lots of images happen all at once
        renderer = new ShapeRenderer();

        parent = mainClass;
        model = new GameModel(viewport, parent.assetsManager);


        backgroundImage = new Texture("images/bg.png");

        parent.assetsManager.queueAddImages();
        parent.assetsManager.manager.finishLoading();
        atlas = parent.assetsManager.manager.get("images/sprites.atlas");

        tcBTex = atlas.findRegion("tc1");
        tcRTex = atlas.findRegion("tc2");
        tcYTex = atlas.findRegion("tc3");
        tcGTex = atlas.findRegion("tc4");
        robozin = atlas.findRegion("robo");

        addSprites();

        debugRenderer = new Box2DDebugRenderer(true,true,true,true,true,true);
    }

    private void addSprites() {
        Array<TextureAtlas.AtlasRegion> regions = atlas.getRegions();

        for (TextureAtlas.AtlasRegion region : regions) {
            Sprite sprite = atlas.createSprite(region.name);

            float width = sprite.getWidth() * GameModel.SCALE;
            float height = sprite.getHeight() * GameModel.SCALE;

            sprite.setSize(width, height);
            sprite.setOrigin(0, 0);

            sprites.put(region.name, sprite);
        }
    }

    private void drawSprite(String name, float x, float y, float degrees) {
        Sprite sprite = sprites.get(name);

        sprite.setPosition(x, y);
        sprite.setRotation(degrees);

        sprite.draw(batch);
    }

    public static boolean addDeletedBody(Body body){
        return toBeDeletedBodies.add(body);
    }

    @Override
    public void show() {
        model.world.getBodies(disposableBodies);

        for (Body body : disposableBodies) {
            if (body.getUserData() == "PAPER" || body.getUserData() == "PLASTIC" || body.getUserData() == "METAL" || body.getUserData() == "GLASS") {
                if (!toBeDeletedBodies.contains(body)) {
                    model.world.destroyBody(body);
                }
            }
        }

        model.firingPosition = model.anchor.cpy();

        model.generateTrash();

        Gdx.input.setInputProcessor(model);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.setProjectionMatrix(cam.combined);

        model.logicStep(delta);

        batch.begin();
        //Background
        batch.draw(backgroundImage, 0, 0, V_WIDTH, V_HEIGHT);

        //Lixeiras
        batch.draw(tcBTex, model.trashCanB.getPosition().x - (GameModel.TRASH_CAN_WIDTH / 2), model.trashCanB.getPosition().y - (GameModel.TRASH_CAN_HEIGHT / 2), GameModel.TRASH_CAN_WIDTH, GameModel.TRASH_CAN_HEIGHT);
        batch.draw(tcRTex, model.trashCanR.getPosition().x - (GameModel.TRASH_CAN_WIDTH / 2), model.trashCanR.getPosition().y - (GameModel.TRASH_CAN_HEIGHT / 2), GameModel.TRASH_CAN_WIDTH, GameModel.TRASH_CAN_HEIGHT);
        batch.draw(tcYTex, model.trashCanY.getPosition().x - (GameModel.TRASH_CAN_WIDTH / 2), model.trashCanY.getPosition().y - (GameModel.TRASH_CAN_HEIGHT / 2), GameModel.TRASH_CAN_WIDTH, GameModel.TRASH_CAN_HEIGHT);
        batch.draw(tcGTex, model.trashCanG.getPosition().x - (GameModel.TRASH_CAN_WIDTH / 2), model.trashCanG.getPosition().y - (GameModel.TRASH_CAN_HEIGHT / 2), GameModel.TRASH_CAN_WIDTH, GameModel.TRASH_CAN_HEIGHT);

        //Catapult
        batch.draw(robozin, model.anchor.x - (model.anchor.x / 2) - 5, model.anchor.y - (model.anchor.y / 2) - 1, 15, 15);

        //Lixos
        for (int i = 0; i < model.trashBodies.length; i++) {
            Body body = model.trashBodies[model.index];

            if (!body.isAwake()) {
                addDeletedBody(body);
                Hud.subScore();
            }
            else {
                String name = model.names[model.index];
                Vector2 position = body.getPosition();
                float degrees = (float) Math.toDegrees(body.getAngle());

                drawSprite(name, position.x, position.y, degrees);
            }
        }

        batch.end();

        renderer.setProjectionMatrix(cam.combined);
        renderer.setColor(Color.ORANGE);
        renderer.begin(ShapeRenderer.ShapeType.Filled);
        renderer.line(model.firingPosition,model.anchor);
        renderer.end();

        update(delta);
        parent.hud.stage.draw();

        //debugRenderer.render(model.world, cam.combined);

    }

    private void update(float dt){
        parent.hud.update(dt);
        if(toBeDeletedBodies != null) {
            for (Body body : toBeDeletedBodies) {
                toBeDeletedBodies.remove(body);
                model.world.destroyBody(body);
                model.generateTrash();
            }
        }

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
