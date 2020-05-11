package com.recicle.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.recicle.game.views.EndScreen;
import com.recicle.game.views.LoadingScreen;
import com.recicle.game.views.PlayScreen;
import com.recicle.game.views.MenuScreen;
import com.recicle.game.views.PreferencesScreen;

public class MainClass extends Game {
	//A game class is used to delegate between different screens

	SpriteBatch batch;

	private LoadingScreen loadingScreen;
	private PreferencesScreen preferencesScreen;
	private MenuScreen menuScreen;
	private PlayScreen playScreen;
	private EndScreen endScreen;

	public final static int MENU = 0;
	public final static int PREFERENCES = 1;
	public final static int APPLICATION = 2;
	public final static int ENDGAME = 3;

	private AppPreferences preferences;

	@Override
	public void create () {
		batch = new SpriteBatch();

		loadingScreen = new LoadingScreen(this);
		setScreen(loadingScreen);

		preferences = new AppPreferences();
	}

	public void changeScreen(int screen){
		switch(screen){
			case MENU:
				if(menuScreen == null) menuScreen = new MenuScreen(this);
				this.setScreen(menuScreen);
				break;
			case PREFERENCES:
				if(preferencesScreen == null) preferencesScreen = new PreferencesScreen(this);
				this.setScreen(preferencesScreen);
				break;
			case APPLICATION:
				if(playScreen == null) playScreen = new PlayScreen(this);
				this.setScreen(playScreen);
				break;
			case ENDGAME:
				if(endScreen == null) endScreen = new EndScreen(this);
				this.setScreen(endScreen);
				break;
		}
	}

	public AppPreferences getPreferences() {
		return this.preferences;
	}

	@Override
	public void render () {
		super.render();

	}
	
	@Override
	public void dispose () {
		super.dispose();
		batch.dispose();

	}
}
