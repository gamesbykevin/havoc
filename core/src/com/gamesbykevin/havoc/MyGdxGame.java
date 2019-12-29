package com.gamesbykevin.havoc;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.gamesbykevin.havoc.screen.ScreenHelper;
import com.gamesbykevin.havoc.util.Language;
import de.golfgl.gdxgamesvcs.IGameServiceClient;

public class MyGdxGame extends Game {

	//our game services client
	private final IGameServiceClient gsClient;

	//manage our screens
	private ScreenHelper screenHelper;

	//is the game paused?
	private boolean paused = false;

	//do we exit the game?
	public static boolean EXIT = false;

	public MyGdxGame(IGameServiceClient gsClient) {
		this.gsClient = gsClient;

		//flag false
		this.EXIT = false;
	}

	public static void exit(MyGdxGame game) {

		if (game != null)
			game.dispose();

		//exit app
		Gdx.app.exit();
	}

	@Override
	public void dispose() {

		super.dispose();

		//recycle screen
		if (this.screenHelper != null) {
			this.screenHelper.dispose();
			this.screenHelper = null;
		}

		//recycle language bundle
		Language.recycle();
	}

	@Override
	public void create() {

		//create our screens
		this.screenHelper = new ScreenHelper(this);
	}

	public IGameServiceClient getGsClient() {
		return this.gsClient;
	}

	public ScreenHelper getScreenHelper() {
	    return this.screenHelper;
    }

	public boolean isPaused() {
		return paused;
	}

	public void setPaused(final boolean paused) {
		this.paused = paused;

		System.out.println("Paused: " + paused);
	}

	@Override
	public void pause() {
		super.pause();
	}
}