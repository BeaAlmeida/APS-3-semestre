package com.pixmeg.catapult.controller;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.pixmeg.catapult.view.MainScreen;

public class CatapultTutorial extends Game {
    private MainScreen mainScreen;
	public SpriteBatch batch;

	public static final int V_WIDTH = 50;
	public static final int V_HEIGHT = 50;
	OrthographicCamera camera;
	ExtendViewport viewport;

	@Override
	public void create() {
		batch = new SpriteBatch();
		mainScreen = new MainScreen(this);
		setScreen(mainScreen);

		camera = new OrthographicCamera();
		viewport = new ExtendViewport(CatapultTutorial.V_WIDTH,CatapultTutorial.V_HEIGHT, camera);

	}

	@Override
	public void dispose() {
		super.dispose();
		batch.dispose();
	}


	@Override
	public void render () {
		super.render();
	}
}
