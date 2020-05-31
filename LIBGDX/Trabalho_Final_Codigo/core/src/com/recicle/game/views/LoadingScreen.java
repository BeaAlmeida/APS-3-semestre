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
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TiledDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.recicle.game.MainClass;


public class LoadingScreen implements Screen {

    private MainClass parent; // a field to store our orchestrator

    private Stage stage;
    private Skin skin;

    private Table table;
    private Label titleLabel;
    private Label subTitleLabel;

    public final int IMAGE = 0;
    public final int FONT = 1;
    public final int PARTY = 2;
    public final int SOUND = 3;
    public final int MUSIC = 4;

    private int currentLoadingStage = 0;
    public float countDown = 2f; // timer for exiting loading screen (5 seconds)
    private ProgressBar progressBar;


    public LoadingScreen(MainClass mainClass) {
        parent = mainClass;
        stage = new Stage(new ScreenViewport());

        parent.assetsManager.queueAddSkin();
        parent.assetsManager.manager.finishLoading();
        skin = parent.assetsManager.manager.get("skin/uiskin.json");

        parent.assetsManager.queueAddImages();
        System.out.println("Loading images....");

        progressBar = new ProgressBar(0f, 1f, 0.1f, false, skin);
        progressBar.setValue(1f);
        progressBar.setAnimateDuration(0.25f);
        progressBar.setBounds(10,10,100,20);

        stage.addActor(progressBar);

    }



    @Override
    public void show() {

        table = new Table(skin); //main table
        table.setFillParent(true);
        table.background("window");

        titleLabel = new Label("Loading", skin);
        subTitleLabel = new Label("APS 2020/1", skin );

        table.add(titleLabel).align(Align.center).pad(100, 0, 20, 0).colspan(10);
        table.row();
        table.add(progressBar).width(400);
        table.row();
        table.add(subTitleLabel).align(Align.center).pad(150, 0, 10, 0).colspan(10);

        stage.addActor(table);


    }



    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0,0,0,1); //  clear the screen
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);



        if (parent.assetsManager.manager.update()) {
            currentLoadingStage+= 1;

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
