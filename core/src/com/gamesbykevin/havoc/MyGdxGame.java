package com.gamesbykevin.havoc;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.*;
import com.gamesbykevin.havoc.collectables.CollectibleHelper;
import com.gamesbykevin.havoc.collectables.Collectibles;
import com.gamesbykevin.havoc.input.MyController;
import com.gamesbykevin.havoc.level.Level;
import com.gamesbykevin.havoc.player.Player;
import com.gamesbykevin.havoc.player.weapon.Weapons;
import com.gamesbykevin.havoc.texture.TextureHelper;

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

	//our game level
	private Level level;

	//our hero
	private Player player;

	//our weapon inventory
	private Weapons weapons;

	@Override
	public void create () {

		//create objects etc...
		getLevel();
		getWeapons();
	}

	public Level getLevel() {

		if (this.level == null)
			this.level = new Level(getPlayer());

		return this.level;
	}

	public Player getPlayer() {

	    if (this.player == null)
	        this.player = new Player();

        return this.player;
    }

	public Weapons getWeapons() {

		if (this.weapons == null)
			this.weapons = new Weapons(getLevel());

		return this.weapons;
	}

	@Override
	public void render () {

		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);

		final long time = System.currentTimeMillis();

		//check if any collectibles have been collected
		CollectibleHelper.check(getLevel(), getWeapons());

        //render the level
        getLevel().render();

		//render player etc...
		getPlayer().render(getWeapons());

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

		if (this.player != null)
			this.player.dispose();
		if (this.level != null)
			this.level.dispose();
		if (this.weapons != null)
			this.weapons.dispose();

		this.player = null;
		this.level = null;
		this.weapons = null;
		TextureHelper.recycle();
	}
}