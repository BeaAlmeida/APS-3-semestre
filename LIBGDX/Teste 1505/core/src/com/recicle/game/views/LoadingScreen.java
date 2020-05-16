package com.recicle.game.views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TiledDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.recicle.game.MainClass;


public class LoadingScreen implements Screen {

    private MainClass parent; // a field to store our orchestrator

    private TextureAtlas atlas;
    private TextureAtlas.AtlasRegion title;
    private TextureAtlas.AtlasRegion dash;
    private TextureAtlas.AtlasRegion background;
    private TextureAtlas.AtlasRegion copyright;

    private Animation flameAnimation;
    private Stage stage;

    public final int IMAGE = 0;
    public final int FONT = 1;
    public final int PARTY = 2;
    public final int SOUND = 3;
    public final int MUSIC = 4;

    private int currentLoadingStage = 0;
    public float countDown = 2f; // timer for exiting loading screen (5 seconds)

    private Image titleImage;
    private Table table;
    private Table loadingTable;
    private Image copyrightImage;


    public LoadingScreen(MainClass mainClass) {
        parent = mainClass;
//        batch = new SpriteBatch();
//        batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE);
        stage = new Stage(new ScreenViewport());

        loadAssets();

        parent.assetsManager.queueAddImages();
        System.out.println("Loading images....");

    }

    public void loadAssets(){
        parent.assetsManager.queueAddLoadingImages();
        parent.assetsManager.manager.finishLoading();

        atlas = parent.assetsManager.manager.get("images/loading.atlas");
        title = atlas.findRegion("staying-alight-logo");
        dash = atlas.findRegion("loading-dash");
        background = atlas.findRegion("flamebackground");
        copyright = atlas.findRegion("copyright");
        flameAnimation = new Animation(0.07f, atlas.findRegions("flames/flames"), Animation.PlayMode.LOOP);


    }

    @Override
    public void show() {
        titleImage = new Image(title);
        copyrightImage = new Image(copyright);

        table = new Table(); //main table
        table.setFillParent(true);
        table.setDebug(false);
        table.setBackground(new TiledDrawable(background));

        loadingTable = new Table(); //table which holds the 10 loading bar parts
        loadingTable.add(new LoadingBarPart(dash,flameAnimation));
        loadingTable.add(new LoadingBarPart(dash,flameAnimation));
        loadingTable.add(new LoadingBarPart(dash,flameAnimation));
        loadingTable.add(new LoadingBarPart(dash,flameAnimation));
        loadingTable.add(new LoadingBarPart(dash,flameAnimation));
        loadingTable.add(new LoadingBarPart(dash,flameAnimation));
        loadingTable.add(new LoadingBarPart(dash,flameAnimation));
        loadingTable.add(new LoadingBarPart(dash,flameAnimation));
        loadingTable.add(new LoadingBarPart(dash,flameAnimation));
        loadingTable.add(new LoadingBarPart(dash,flameAnimation));


        table.add(titleImage).align(Align.center).pad(10, 0, 0, 0).colspan(10);
        table.row();
        table.add(loadingTable).width(400);
        table.row();
        table.add(copyrightImage).align(Align.center).pad(200, 0, 0, 0).colspan(10);

        stage.addActor(table);

    }


//    private void drawLoadingBar(int stage, TextureRegion currentFrame){
//        for(int i = 0; i < stage;i++){
//            batch.draw(currentFrame, 50 + (i * 50), 150, 50, 50);
//            batch.draw(dash, 35 + (i * 50), 140, 80, 80);
//        }
//    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0,0,0,1); //  clear the screen
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

//        stateTime = 0f;
//        stateTime += delta;
//        TextureRegion currentFrame = (TextureRegion) flameAnimation.getKeyFrame(stateTime, true);
//
//        // start SpriteBatch and draw the logo
//        batch.begin();
//            drawLoadingBar(currentLoadingStage * 2, currentFrame);
//            batch.draw(title, 135, 250);
//        batch.end();


        if (parent.assetsManager.manager.update()) {
            currentLoadingStage+= 1;

            if(currentLoadingStage <= 5) {
                loadingTable.getCells().get((currentLoadingStage-1)*2).getActor().setVisible(true);  // new
                loadingTable.getCells().get((currentLoadingStage-1)*2+1).getActor().setVisible(true);
            }

            switch(currentLoadingStage) {
                case FONT:
                    System.out.println("Loading fonts....");
                    parent.assetsManager.queueAddFonts(); // first load done, now start fonts
                    break;

                case PARTY:
                    System.out.println("Loading Particle Effects....");
                    parent.assetsManager.queueAddParticleEffects(); // fonts are done now do party effects
                    break;

                case SOUND:
                    System.out.println("Loading Sounds....");
                    parent.assetsManager.queueAddSounds();
                    break;

                case MUSIC:
                    System.out.println("Loading fonts....");
                    parent.assetsManager.queueAddMusic();
                    break;

                case 5:
                    System.out.println("Finished"); // all done
                    break;
            }

            if (currentLoadingStage >5) {
                countDown -= delta;  // timer to stay on loading screen for short preiod once done loading
                currentLoadingStage = 5;  // cap loading stage to 5 as will use later to display progress bar anbd more than 5 would go off the screen
                if(countDown < 0) { // countdown is complete
                    parent.changeScreen(MainClass.MENU);  /// go to menu screen
                }
            }
        }

        stage.act();
        stage.draw();

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
