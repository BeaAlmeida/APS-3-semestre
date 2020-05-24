package com.recicle.game.views;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.recicle.game.MainClass;
import com.recicle.game.Model;

public class Hud implements Disposable {
    private MainClass parent;
    private ExtendViewport viewport;
    public Stage stage;
    SpriteBatch batch;

    public static final int TIME = 30;
    public static int worldTimer; //Tempo total de jogo
    private float timeCount; //Contador
    public static int score;

    Label timeLabel;
    Label scoreLabel;

    public Hud(MainClass mainClass) {
        parent = mainClass;

        worldTimer = TIME;
        timeCount = 0;
        score = 0;

        viewport = new ExtendViewport(200, 200, new OrthographicCamera());
        batch = new SpriteBatch();
        stage = new Stage(viewport, batch);

        Table table = new Table();
        table.top();
        table.setFillParent(true);


        Label timeTextLabel = new Label("TIME", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        Label scoreTextLabel = new Label("SCORE", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        timeLabel = new Label(String.format("%03d", worldTimer), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        scoreLabel = new Label(String.format("%06d", score), new Label.LabelStyle(new BitmapFont(), Color.WHITE));

        table.add(scoreTextLabel).expandX().padTop(5);
        table.add(timeTextLabel).expandX().padTop(5);
        table.row();
        table.add(scoreLabel).expandX().padTop(5);
        table.add(timeLabel).expandX().padTop(5);

        stage.addActor(table);

    }

    public static void addScore(){
        score +=  10;
        worldTimer += 2;

    }
    public static void subScore(){
        score -= 10;

    }

    public void update(float dt) {
        timeCount += dt;
        if (timeCount >= 1) {
            if (worldTimer > 0) {
                worldTimer--;
                timeLabel.setText(String.format("%03d", worldTimer));
                timeCount = 0;
            }
        }
        if (worldTimer <= 0){
            System.out.println("Time out!");
            parent.changeScreen(MainClass.ENDGAME);
            parent.playScreen.model.trashBodies[parent.playScreen.model.index].setType(BodyDef.BodyType.DynamicBody);
            worldTimer = TIME;
            score = 0;

        }
        //if (worldTimer == 10) parent.playScreen.model.playSound(Model.TICTAC);

        if(score < 0) score = 0;

        scoreLabel.setText(String.format("%06d", score));

    }

    public int getScore(){
        return score;
    }

    @Override
    public void dispose() {
        stage.dispose();
    }

}
