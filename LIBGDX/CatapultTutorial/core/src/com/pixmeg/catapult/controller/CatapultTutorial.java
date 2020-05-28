package com.pixmeg.catapult.controller;

import com.badlogic.gdx.Game;
import com.pixmeg.catapult.view.MainScreen;

public class CatapultTutorial extends Game {
    private MainScreen mainScreen;

	@Override
	public void create() {
		mainScreen = new MainScreen(this);
		setScreen(mainScreen);
	}
}
