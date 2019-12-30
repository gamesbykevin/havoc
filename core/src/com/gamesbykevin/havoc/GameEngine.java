package com.gamesbykevin.havoc;

import com.badlogic.gdx.*;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.*;
import com.gamesbykevin.havoc.assets.AssetManagerHelper;
import com.gamesbykevin.havoc.assets.AudioHelper;
import com.gamesbykevin.havoc.assets.TextureHelper;

import static com.gamesbykevin.havoc.assets.AudioHelper.*;
import static com.gamesbykevin.havoc.dungeon.DungeonHelper.DUNGEON_SIZE;
import com.gamesbykevin.havoc.level.Level;
import com.gamesbykevin.havoc.player.Player;
import com.gamesbykevin.havoc.util.Disposable;
import com.gamesbykevin.havoc.util.MyProgressBar;

import java.util.Random;

import static com.gamesbykevin.havoc.assets.TextureHelper.*;
import static com.gamesbykevin.havoc.dungeon.RoomHelper.ROOM_DIMENSION_MAX;
import static com.gamesbykevin.havoc.enemies.Enemies.ENEMIES_PER_ROOM_MAX;
import static com.gamesbykevin.havoc.enemies.Enemies.ENEMIES_PER_ROOM_MIN;
import static com.gamesbykevin.havoc.util.MyProgressBar.*;

public class GameEngine implements Disposable {

	//size of our window
	public static final int SIZE_WIDTH = 800;
	public static final int SIZE_HEIGHT = 480;

	//frames per second
	public static final float FPS = 60f;

	/**
	 * How long is 1 frame in milliseconds?
	 */
	public static final float FRAME_MS = (1000f / FPS);

	//our game level
	private Level level;

	//our hero
	private Player player;

	//where we load our assets
	private AssetManager assetManager;

	public enum Steps {
		Step1, Step2, Step3, Step4, Step5, Step6, Step7, Step8
	}

	//current step
	private Steps step;

	//has the game been created
	private boolean created = false;

	//render progress bar to show progress
	private MyProgressBar myProgressBar;

	//object used to make random decisions
	private static Random RANDOM;

	public GameEngine(int index) {

		//setup the level
		setLevel(index);

		//create objects etc...
		getAssetManager();

		//start at step 1
		setStep(Steps.Step1);

		//flag the game has not been created
		setCreated(false);

		//create progress bar
		getMyProgressBar();
	}

	public MyProgressBar getMyProgressBar() {

		if (this.myProgressBar == null)
			this.myProgressBar = new MyProgressBar();

		return this.myProgressBar;
	}

	public static Random getRandom() {
		return RANDOM;
	}

	public void resize(int width, int height) {

		//set it up for rendering
		getPlayer().getViewport().update(width, height);
	}

	private void setLevel(int index) {

		float factor = 0;
		int enemyMax = 0;
		int enemyMin = 0;

		//always use the same seed to generate the same levels etc...
		RANDOM = new Random(index);

		switch (index) {

			default:
			case 0:
				factor = 2;
				enemyMax = 1;
				enemyMin = 1;
				break;

			case 1:
				factor = 2;
				enemyMax = 2;
				enemyMin = 1;
				break;

			case 2:
				factor = 3;
				enemyMax = 3;
				enemyMin = 2;
				break;

			case 3:
				factor = 3;
				enemyMax = 3;
				enemyMin = 3;
				break;

			case 4:
				factor = 4;
				enemyMax = 3;
				enemyMin = 2;
				break;

			case 5:
				factor = 4;
				enemyMax = 3;
				enemyMin = 3;
				break;

			case 6:
				factor = 5;
				enemyMax = 3;
				enemyMin = 2;
				break;

			case 7:
				factor = 5;
				enemyMax = 3;
				enemyMin = 3;
				break;

			case 8:
				factor = 6;
				enemyMax = 3;
				enemyMin = 2;
				break;

			case 9:
				factor = 6;
				enemyMax = 3;
				enemyMin = 3;
				break;
		}

		//set the entire size of the dungeon
		DUNGEON_SIZE = (int)(ROOM_DIMENSION_MAX * factor);

		//how many enemies can we have in 1 room
		ENEMIES_PER_ROOM_MAX = enemyMax;

		//the fewest number of enemies per room
		ENEMIES_PER_ROOM_MIN = enemyMin;
	}

	public boolean isCreated() {
		return this.created;
	}

	public void setCreated(boolean created) {
		this.created = created;
	}

	public static int getSizeWidth() {
		return SIZE_WIDTH;
	}

	public static int getSizeHeight() {
		return SIZE_HEIGHT;
	}

	public Level getLevel() {

		if (this.level == null)
			this.level = new Level(getAssetManager(), getPlayer());

		return this.level;
	}

	public Player getPlayer() {

	    if (this.player == null)
	        this.player = new Player(getAssetManager());

        return this.player;
    }

	public Steps getStep() {
		return this.step;
	}

	public void setStep(Steps step) {
		this.step = step;
	}

	private AssetManager getAssetManager() {

		if (this.assetManager == null) {
			this.assetManager = new AssetManager();
			AssetManagerHelper.load(this.assetManager);
		}

		return this.assetManager;
	}

	public void resume(InputMultiplexer inputMultiplexer) {

		getPlayer().getController().setInput(inputMultiplexer);

		//use the asset manager
		Texture.setAssetManager(getAssetManager());

		//go back to reloading our assets
		setStep(Steps.Step1);

		//flag that we are no longer paused
		setPaused(false);
	}

	public void setPaused(boolean paused) {
		getLevel().setPaused(paused);
	}

	public void render () {

		//clear screen
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);

		switch (getStep()) {

			case Step1:

				//if assets are loaded move to the next step
				if (getAssetManager().update()) {

					//stop all audio and start the theme
					stop(getAssetManager());
					playMusic(getAssetManager(), AudioHelper.Song.Theme);

					if (isCreated()) {

						//if the game was already created go to step 8
						setStep(Steps.Step8);

					} else {

						//if we are good, go to the next step
						setStep(Steps.Step2);
						getMyProgressBar().renderProgressBar(TEXT_STEP_2);
					}

				} else {
					getMyProgressBar().renderProgressBar(getAssetManager().getProgress(), TEXT_STEP_1);
				}
				break;

			case Step2:

				//generate dungeon
				getMyProgressBar().renderProgressBar(TEXT_STEP_2);
				getLevel().getDungeon().generate();

				//flag the players start location
				getPlayer().setStart(getLevel().getDungeon().getStartCol(), getLevel().getDungeon().getStartRow());
				setStep(Steps.Step3);
				break;

			case Step3:

				//creating weapons
				getMyProgressBar().renderProgressBar(TEXT_STEP_3);
				getLevel().getPlayer().createWeapons(getLevel());
				setStep(Steps.Step4);
				break;

			case Step4:

				//add obstacles
				getMyProgressBar().renderProgressBar(TEXT_STEP_4);
				getLevel().getObstacles().spawn();
				setStep(Steps.Step5);
				break;

			case Step5:

				//add enemies
				getMyProgressBar().renderProgressBar(TEXT_STEP_5);
				getLevel().getEnemies().spawn();
				setStep(Steps.Step6);
				break;

			case Step6:

				//add collectibles
				getMyProgressBar().renderProgressBar(TEXT_STEP_6);
				getLevel().getCollectibles().spawn();
				setStep(Steps.Step7);
				break;

			case Step7:

				//create level textures
				if (COUNT < TOTAL) {
					getMyProgressBar().renderProgressBar((COUNT / TOTAL), TEXT_STEP_7);
					addTextures(getLevel());
				} else {
					getMyProgressBar().renderProgressBar(TEXT_STEP_7);
					setStep(Steps.Step8);
					playHero(getAssetManager());

					//flag game has been created
					setCreated(true);
				}
				break;

			case Step8:

				//at this point render the game
				final long time = System.currentTimeMillis();

				//render the level
				getLevel().render();

				//render player objects etc...
				getPlayer().render();

				final long elapsed = System.currentTimeMillis() - time;

				try {
					//maintain steady game speed
					if (elapsed > FRAME_MS) {
						Thread.sleep(1);
					} else {
						Thread.sleep((long) (FRAME_MS - elapsed));
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
		}
	}

	@Override
	public void dispose () {

		//stop all audio
		stop(this.assetManager);

		if (this.player != null)
			this.player.dispose();
		if (this.level != null)
			this.level.dispose();
		if (this.myProgressBar != null)
			this.myProgressBar.dispose();
		if (this.assetManager != null)
			AssetManagerHelper.dispose(this.assetManager);

		TextureHelper.dispose();
		this.player = null;
		this.level = null;
		this.myProgressBar = null;
		this.step = null;
		this.created = false;
		this.assetManager = null;
		RANDOM = null;
	}
}