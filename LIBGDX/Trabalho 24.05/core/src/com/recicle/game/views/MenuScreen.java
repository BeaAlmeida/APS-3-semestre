package com.recicle.game.views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TiledDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.recicle.game.MainClass;

import org.w3c.dom.css.CSS2Properties;

public class MenuScreen implements Screen {


    private Stage stage;
    private MainClass parent; // a field to store our orchestrator

    private Skin skin;

    public static Music themeSong;


    public MenuScreen(MainClass mainClass) {
        parent = mainClass;

        stage = new Stage(new ScreenViewport()); //This is the controller and will react to inputs from the user. In our constructor
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();

        parent.assetsManager.queueAddSkin();
        parent.assetsManager.manager.finishLoading();
        skin = parent.assetsManager.manager.get("skin/uiskin.json");

        parent.assetsManager.queueAddMusic();;
        parent.assetsManager.manager.finishLoading();

        themeSong = parent.assetsManager.manager.get("music/theme.mp3");
        themeSong.play();
        themeSong.setLooping(true);
        themeSong.setVolume(parent.preferences.getMusicVolume());
        if (parent.preferences.isMusicEnabled() == false) themeSong.pause();
        else themeSong.play();


    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage); //This tells the screen to send any input from the user to the stage so it can respond.

        Table table = new Table(skin);
        table.setFillParent(true);
        table.background("window");

        stage.addActor(table);

        Label titleLabel = new Label("*** THE RECICLE GAME ***", skin, "optional" );
        TextButton newGame = new TextButton("New Game", skin);
        TextButton preferences = new TextButton("Preferences", skin);
        TextButton exit = new TextButton("Exit", skin);

        table.add(titleLabel).fillX().uniformX().pad(0, 0, 70, 0);
        table.row();
        table.add(newGame).fillX().uniformX();
        table.row().pad(10,0,0,0);
        table.add(preferences).fillX().uniformX();
        table.row().pad(10,0,0,0);
        table.add(exit).fillX().uniformX();

        exit.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) { //Anything inside the changed method will be executed when the button is clicked
                Gdx.app.exit();
            }
        });

        newGame.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                parent.changeScreen(MainClass.APPLICATION);
            }
        });

        preferences.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                parent.changeScreen(MainClass.PREFERENCES);
            }
        });

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();



    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);

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
        stage.dispose();
        themeSong.dispose();

    }
}
