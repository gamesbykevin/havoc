package com.gamesbykevin.havoc;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.gamesbykevin.havoc.input.ScreenController;
import com.gamesbykevin.havoc.screen.ScreenHelper;
import com.gamesbykevin.havoc.util.Language;
import de.golfgl.gdxgamesvcs.IGameServiceClient;

public class MyGdxGame extends Game {

	//our game services client
	private final IGameServiceClient gsClient;

	//manage our screens
	private ScreenHelper screenHelper;

	//controller for our screens
	private ScreenController controller;

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

		//flag exit
		EXIT = true;

		if (game != null)
			game.dispose();

		//exit app
		Gdx.app.exit();
	}

	@Override
	public void create() {

		//create our menu controller first
		this.controller = new ScreenController(this);

		//create our screens
		this.screenHelper = new ScreenHelper(this);
	}

	public ScreenController getController() {
		return this.controller;
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
	}

	@Override
	public void pause() {
		//call parent
		super.pause();
	}

	@Override
	public void dispose() {

		super.dispose();

		//recycle screen
		if (this.screenHelper != null) {
			this.screenHelper.dispose();
			this.screenHelper = null;
		}

		if (this.controller != null)
			this.controller.dispose();

		this.controller = null;

		//recycle language bundle
		Language.recycle();
	}
}