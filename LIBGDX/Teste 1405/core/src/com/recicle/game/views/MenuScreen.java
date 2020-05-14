package com.recicle.game.views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.recicle.game.MainClass;

public class MenuScreen implements Screen {

    private Stage stage;
    private MainClass parent; // a field to store our orchestrator

//    Texture img;
//    SpriteBatch batch;


    public MenuScreen(MainClass mainClass) {
        parent = mainClass;

//        img = new Texture("badlogic.jpg");
//        batch = new SpriteBatch();

        stage = new Stage(new ScreenViewport()); //This is the controller and will react to inputs from the user. In our constructor
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
        // perform actions after user input and draw the items held within it


    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage); //This tells the screen to send any input from the user to the stage so it can respond.

        Table table = new Table();
        table.setFillParent(true);
        //table.setDebug(true);
        stage.addActor(table);

        Skin skin = new Skin(Gdx.files.internal("skin/glassy-ui.json")); //A Skin is libgxd’s way of keeping GUI elements like button textures, fonts and other GUI layout parameters all in one file.

        TextButton newGame = new TextButton("New Game", skin);
        TextButton preferences = new TextButton("Preferences", skin);
        TextButton exit = new TextButton("Exit", skin);

            table.add(newGame).fillX().uniformX();

        table.row().pad(10, 0, 10, 0);
            table.add(preferences).fillX().uniformX();

        table.row();
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

//        batch.begin();
//        batch.draw(img, 0, 0);
//        batch.end();


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

    }
}
