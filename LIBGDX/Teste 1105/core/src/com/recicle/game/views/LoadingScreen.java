package com.recicle.game.views;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.recicle.game.MainClass;
import jdk.nashorn.internal.codegen.ClassEmitter;

public class LoadingScreen implements Screen {

    private MainClass parent; // a field to store our orchestrator
    Texture img;
    SpriteBatch batch;


    public LoadingScreen(MainClass mainClass) {
        parent = mainClass;
        batch = new SpriteBatch();
        img = new Texture("badlogic.jpg");

    }

    @Override
    public void show() { }

    @Override
    public void render(float delta) {
        parent.changeScreen(MainClass.MENU); //Proceed to load the menu screen

        batch.begin();
            batch.draw(img,0,0);
        batch.end();
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
        batch.dispose();

    }
}
