package com.ajrod.duckhunt.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.ajrod.duckhunt.DuckHunt;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.height = DuckHunt.HEIGHT;
		config.width = DuckHunt.WIDTH;
		config.title = DuckHunt.TITLE;
		new LwjglApplication(new DuckHunt(), config);
	}
}
