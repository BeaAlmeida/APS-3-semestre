package com.pixmeg.catapult.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.pixmeg.catapult.controller.CatapultTutorial;
import com.pixmeg.catapult.view.GameOverScreen;
import com.pixmeg.catapult.view.MainScreen;


public class Hud implements Disposable {
    private CatapultTutorial parent;

    public Stage stage;
    private ExtendViewport viewport;

    private Integer worldTimer;
    private float timeCount;
    private static Integer score;


    private Label countdownLabel;
    private static Label scoreLabel;
    private Label timeLabel;
    private Label levelLabel;
    private Label worldLabel;
    private Label textDemo;
    private GameOverScreen gameOverScreen;

    public Hud(SpriteBatch sb) {
        //definir variaveis
        worldTimer = 10;
        timeCount = 0;
        score = 0;


        //viewport = new ExtendViewport(CatapultTutorial.V_WIDTH, CatapultTutorial.V_HEIGHT, new OrthographicCamera());
        viewport = new ExtendViewport(200, 200, new OrthographicCamera());
        stage = new Stage(viewport, sb);

        // define uma tabela usada para organizar os rótulos de hud
        Table table = new Table();

        //Alinha a tabela ao top
        table.top();

        //A tabela preenche td o estagio
        table.setFillParent(true);

        //Define estilo da Label
        countdownLabel = new Label(String.format("%03d", worldTimer), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        scoreLabel = new Label(String.format("%06d", score), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        timeLabel = new Label("TIME", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        levelLabel = new Label("1-1", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        worldLabel = new Label("WORLD", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        textDemo = new Label("SCORE", new Label.LabelStyle(new BitmapFont(), Color.WHITE));


        // adiciona as labels na tabela, preenchendo a parte superior e dando a todos largura igual com expandX
        table.add(textDemo).expandX().padTop(5);
        table.add(worldLabel).expandX().padTop(5);
        table.add(timeLabel).expandX().padTop(5);

        //Adiciona uma segunda linha
        table.row();
        table.add(scoreLabel).expandX();
        table.add(levelLabel).expandX();
        table.add(countdownLabel).expandX();

        //Adiciona a tabela no estagio
        stage.addActor(table);
    }


    public void update(float dt) {
        timeCount += dt;
        if (timeCount >= 1) {
            if (worldTimer > 0) {
                worldTimer--;
                countdownLabel.setText(String.format("%03d", worldTimer));
                timeCount = 0;
                //if (worldTimer == 0) {
                    //parent.setScreen(new GameOverScreen(parent));
                    //dispose();
                    //Gdx.app.exit();
                //}
            }
        }

    }


    public boolean gameOver(){
        if (worldTimer == 0 ){
        return true;
        }
        return false;
    }

    public static void addScore(int value){  //Inserir Hud.addScore(pontuacao)
        score += value;
        scoreLabel.setText(String.format("%06d", score));
            /*if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
                Hud.addScore(200);
            }*/
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
