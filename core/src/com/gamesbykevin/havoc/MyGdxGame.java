package com.gamesbykevin.havoc;

import com.badlogic.gdx.*;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.gamesbykevin.havoc.assets.AssetManagerHelper;
import com.gamesbykevin.havoc.level.Level;
import com.gamesbykevin.havoc.player.Player;
import com.gamesbykevin.havoc.assets.TextureHelper;

import static com.gamesbykevin.havoc.assets.AssetManagerHelper.*;
import static com.gamesbykevin.havoc.assets.AudioHelper.playHero;
import static com.gamesbykevin.havoc.assets.TextureHelper.*;

public class MyGdxGame extends ApplicationAdapter {

	//size of our window
	public static final int SIZE_WIDTH = 800;
	public static final int SIZE_HEIGHT = 480;

	//frames per second
	public static final float FPS = 90f;

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

	//used to draw progress bar info
	private ShapeRenderer shapeRenderer;
	private Viewport viewport;
	private SpriteBatch batch;
	private BitmapFont font;

	//dimensions of the progress bar
	private static final float PROGRESS_BAR_WIDTH = (getSizeWidth() / 2f);
	private static final float PROGRESS_BAR_HEIGHT = 50f;

	//change the size of the font
	private static final float FONT_SCALE = 1.25f;

	public enum Steps {
		Step1, Step2, Step3, Step4, Step5, Step6, Step7, Step8
	}

	//current step
	private Steps step;

	//has the game been created
	private boolean created = false;

	@Override
	public void create () {

		//create objects etc...
		getAssetManager();

		//create view port
		this.viewport = new StretchViewport(getSizeWidth(), getSizeHeight());

		//create sprite batch
		this.batch = new SpriteBatch();

		//create our object
		this.shapeRenderer = new ShapeRenderer();

		//create our font
		this.font = new BitmapFont();
		this.font.getData().setScale(FONT_SCALE);

		//start at step 1
		setStep(Steps.Step1);

		//flag the game has not been created
		setCreated(false);
	}

	@Override
	public void resize(int width, int height) {

		//call parent
		super.resize(width, height);

		//set it up for rendering
		getPlayer().getViewport().update(width, height);
		getViewport().update(width, height, true);
		getBatch().setProjectionMatrix(getViewport().getCamera().combined);
		getShapeRenderer().setProjectionMatrix(getViewport().getCamera().combined);
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

	public ShapeRenderer getShapeRenderer() {
		return this.shapeRenderer;
	}

	public SpriteBatch getBatch() {
		return this.batch;
	}

	public Viewport getViewport() {
		return this.viewport;
	}

	public BitmapFont getFont() {
		return this.font;
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

	@Override
	public void pause() {
		super.pause();
	}

	@Override
	public void resume() {
		super.resume();

		//use the asset manager
		Texture.setAssetManager(getAssetManager());

		//go back to reloading our assets
		setStep(Steps.Step1);
	}

	@Override
	public void render () {

		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);

		switch (getStep()) {

			case Step1:

				//if assets are loaded move to the next step
				if (getAssetManager().update()) {

					boolean success = true;

					//verify everything is loaded
					for (int i = 0; i < getPaths().size(); i++) {

						String path = getPaths().get(i);

						//if an asset is not loaded, we need to verify the remaining
						if (!getAssetManager().isLoaded(path)) {
							success = false;

							//check if the asset is a texture
							if (path.contains(ASSET_EXT_BMP) || path.contains(ASSET_EXT_PNG)) {
								getAssetManager().load(path, Texture.class);
							} else if (path.contains(ASSET_EXT_MP3) || path.contains(ASSET_EXT_OGG) || path.contains(ASSET_EXT_WAV)) {
								getAssetManager().load(path, Sound.class);
							}
							renderProgressBar("Verifying assets");
							break;
						}
					}

					if (isCreated()) {

						//if the game was already created go to step 8
						setStep(Steps.Step8);

					} else {

						//if we are good, go to the next step
						if (success) {
							setStep(Steps.Step2);
							renderProgressBar("Step 2 of 7 Generating dungeon");
						}
					}

				} else {
					getAssetManager().update();
					renderProgressBar(getAssetManager().getProgress(), "Step 1 of 7 Loading assets");
				}
				break;

			case Step2:

				//generate dungeon
				renderProgressBar("Step 2 of 7 Generating dungeon");
				getLevel().getDungeon().generate();

				//flag the players start location
				getPlayer().setStart(getLevel().getDungeon().getStartCol(), getLevel().getDungeon().getStartRow());
				setStep(Steps.Step3);
				break;

			case Step3:

				//creating weapons
				renderProgressBar("Step 3 of 7 Creating weapons");
				getLevel().getPlayer().createWeapons(getLevel());
				setStep(Steps.Step4);
				break;

			case Step4:

				//add obstacles
				renderProgressBar("Step 4 of 7 Spawning obstacles");
				getLevel().getObstacles().spawn();
				setStep(Steps.Step5);
				break;

			case Step5:

				//add enemies
				renderProgressBar("Step 5 of 7 Spawning enemies");
				getLevel().getEnemies().spawn();
				setStep(Steps.Step6);
				break;

			case Step6:

				//add collectibles
				renderProgressBar("Step 6 of 7 Spawning collectibles");
				getLevel().getCollectibles().spawn();
				setStep(Steps.Step7);
				break;

			case Step7:

				//create level textures
				if (COUNT < TOTAL) {
					renderProgressBar((COUNT / TOTAL), "Step 7 of 7 Applying textures");
					addTextures(getLevel());
				} else {
					renderProgressBar( "Step 7 of 7 Applying textures");
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

	private void renderProgressBar(String description) {
		renderProgressBar(1.0f, description);
	}

	private void renderProgressBar(float progress, String description) {

		float x = (getSizeWidth() - PROGRESS_BAR_WIDTH) / 2f;
		float y = (getSizeHeight() - PROGRESS_BAR_HEIGHT) / 2f;

		//render the outline
		getShapeRenderer().begin(ShapeRenderer.ShapeType.Line);
		getShapeRenderer().setColor(Color.WHITE);
		getShapeRenderer().rect(x, y, PROGRESS_BAR_WIDTH, PROGRESS_BAR_HEIGHT);
		getShapeRenderer().end();

		//fill the progress bar
		getShapeRenderer().begin(ShapeRenderer.ShapeType.Filled);
		getShapeRenderer().setColor(Color.WHITE);

		getShapeRenderer().rect(x, y, PROGRESS_BAR_WIDTH * progress, PROGRESS_BAR_HEIGHT);

		//draw the info text
		getBatch().begin();
		getFont().draw(getBatch(), description, x, y - (PROGRESS_BAR_HEIGHT / 2));
		getBatch().end();

		//we are finished
		getShapeRenderer().end();
	}

	@Override
	public void dispose () {

		if (this.player != null)
			this.player.dispose();
		if (this.level != null)
			this.level.dispose();
		if (this.shapeRenderer != null)
			this.shapeRenderer.dispose();
		if (this.batch != null)
			this.batch.dispose();
		if (this.font != null)
			this.font = null;
		if (this.assetManager != null) {
			AssetManagerHelper.dispose(this.assetManager);
			this.assetManager.dispose();
		}

		TextureHelper.dispose();

		this.player = null;
		this.level = null;
		this.assetManager = null;
		this.shapeRenderer = null;
		this.viewport = null;
		this.batch = null;
		this.font = null;
	}
}