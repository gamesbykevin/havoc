package com.gamesbykevin.havoc;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import de.golfgl.gdxgamesvcs.NoGameServiceClient;

import static com.gamesbykevin.havoc.GameEngine.SIZE_HEIGHT;
import static com.gamesbykevin.havoc.GameEngine.SIZE_WIDTH;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = SIZE_WIDTH;
		config.height = SIZE_HEIGHT;
		new LwjglApplication(new MyGdxGame(new NoGameServiceClient()), config);
	}
}