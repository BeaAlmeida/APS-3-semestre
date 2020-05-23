package com.recicle.game;

import com.badlogic.gdx.Game;
import com.recicle.game.loader.AssetsManager;
import com.recicle.game.views.EndScreen;
import com.recicle.game.views.LoadingScreen;
import com.recicle.game.views.PlayScreen;
import com.recicle.game.views.MenuScreen;
import com.recicle.game.views.PreferencesScreen;

public class MainClass extends Game {
	//A game class is used to delegate between different screens


	public LoadingScreen loadingScreen;
	private PreferencesScreen preferencesScreen;
	private MenuScreen menuScreen;
	public PlayScreen playScreen;
	private EndScreen endScreen;

	public AppPreferences preferences = new AppPreferences();
	public AssetsManager assetsManager = new AssetsManager();;

	public final static int LOADING = 0;
	public final static int MENU = 1;
	public final static int PREFERENCES = 2;
	public final static int APPLICATION = 3;
	public final static int ENDGAME = 4;


	@Override
	public void create () {
		changeScreen(0);



	}

	public void changeScreen(int screen){
		switch(screen){
			case LOADING:
				if(loadingScreen == null) loadingScreen = new LoadingScreen(this);
				this.setScreen(loadingScreen);
				break;
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

	public AppPreferences getPreferences() { return this.preferences; }

	@Override
	public void render () {
		super.render();

	}
	
	@Override
	public void dispose () {
		super.dispose();

		assetsManager.manager.dispose();

	}
}
