package com.recicle.game.views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.recicle.game.MainClass;

public class EndScreen implements Screen {

    private MainClass parent; // a field to store our orchestrator
    public int score;

    private Stage stage;
    private Skin skin;

    public EndScreen(MainClass mainClass) {
        parent = mainClass;
        score = parent.hud.getScore();

        stage = new Stage(new ScreenViewport()); //This is the controller and will react to inputs from the user. In our constructor
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();

        parent.assetsManager.queueAddSkin();
        parent.assetsManager.manager.finishLoading();
        skin = parent.assetsManager.manager.get("skin/uiskin.json");
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);

        Table table = new Table(skin);
        table.setFillParent(true);
        table.background("window");

        stage.addActor(table);

        Label titleLabel = new Label("GAME OVER", skin, "optional" );
        Label scoreLabel = new Label("SCORE:" + score, skin, "optional" );
        TextButton newGame = new TextButton("Try Again", skin);
        TextButton exit = new TextButton("Exit", skin);

        table.add(titleLabel).align(Align.center).colspan(10).pad(0, 0, 50, 0);

        table.row().pad(10,0,0,0);

        table.add(scoreLabel).align(Align.center).colspan(10);

        table.row().pad(10,0,0,0);

        table.add(newGame).align(Align.center).colspan(10).pad(50, 0,0 , 0).fillX().uniformX();;

        table.row().pad(10,0,0,0);

        table.add(exit).align(Align.center).colspan(10).fillX().uniformX();

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

    }
}
