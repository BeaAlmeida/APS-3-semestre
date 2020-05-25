package com.recicle.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.recicle.game.MainClass;
import com.recicle.game.views.PlayScreen;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = PlayScreen.WIDTH;
		config.height = PlayScreen.HEIGHT;
		config.title = PlayScreen.TITLE;
		new LwjglApplication(new MainClass(), config);
	}
}
