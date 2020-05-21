package com.recicle.game.views;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.recicle.game.MainClass;

public class Hud implements Disposable {
    private MainClass parent;
    private ExtendViewport viewport;
    public Stage stage;

    private int worldTimer;
    private float timeCount;
    private static int score;

    public static final int TIME = 10;

    private Label countdownLabel;
    private static Label scoreLabel;

    public Hud(MainClass mainClass, SpriteBatch batch) {
        parent = mainClass;
        worldTimer = TIME;
        timeCount = 0;
        score = 0;

        viewport = new ExtendViewport(200, 200, new OrthographicCamera());
        stage = new Stage(viewport, batch);

        Table table = new Table();
        table.top();
        table.setFillParent(true);

        countdownLabel = new Label(String.format("%03d", worldTimer), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        scoreLabel = new Label(String.format("%06d", score), new Label.LabelStyle(new BitmapFont(), Color.WHITE));

        table.add(countdownLabel).expandX().padTop(5);
        table.add(scoreLabel).expandX().padTop(5);

        table.row();
        table.add(scoreLabel).expandX();
        table.add(countdownLabel).expandX();

        stage.addActor(table);

    }

    public void update(float dt) {
        timeCount += dt;
        if (timeCount >= 1) {
            if (worldTimer > 0) {
                worldTimer--;
                countdownLabel.setText(String.format("%03d", worldTimer));
                timeCount = 0;
            }
        }

        if (worldTimer == 0){
            System.out.println("Time out!");
            parent.changeScreen(MainClass.LOADING);
            worldTimer = TIME;
        }

        scoreLabel.setText(String.format("%06d", score));

    }

    @Override
    public void dispose() {
        stage.dispose();
    }

}
