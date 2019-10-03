package com.gamesbykevin.havoc;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.*;
import com.gamesbykevin.havoc.input.MyController;
import com.gamesbykevin.havoc.level.Level;
import com.gamesbykevin.havoc.player.Player;

public class MyGdxGame extends ApplicationAdapter {

	//size of our window
	public static final int SIZE_WIDTH = 800;
	public static final int SIZE_HEIGHT = 480;

	//frames per second
	public static final float FPS = 90f;

	/**
	 * How long is 1 frame in milliseconds
	 */
	public static final float FRAME_MS = (1000f / FPS);

	//how we will control the game
	private MyController controller;

	//our game level
	private Level level;

	//our hero
	private Player player;

	@Override
	public void create () {

		//create our level
		getLevel().generateMaze();

		//create the controller
		getController();
	}

    public Player getPlayer() {

	    if (this.player == null)
	        this.player = new Player(getController());

        return this.player;
    }

    public Level getLevel() {

		if (this.level == null)
			this.level = new Level();

		return this.level;
	}

	public MyController getController() {

		if (this.controller == null)
			this.controller = new MyController(getLevel());

		return this.controller;
	}

	@Override
	public void render () {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);

		final long time = System.currentTimeMillis();

        //update the player
        getPlayer().update();

        //render the level
        getLevel().render();

        //render the controls
        getController().render();

        //render the player
        getPlayer().render();

        final long elapsed = System.currentTimeMillis() - time;

        try {
        	//maintain steady game speed
			if (elapsed > FRAME_MS) {
				Thread.sleep(1);
			} else {
				Thread.sleep((long)(FRAME_MS - elapsed));
			}
		} catch (Exception e) {
        	e.printStackTrace();
		}
	}

	@Override
	public void dispose () {

	}
}