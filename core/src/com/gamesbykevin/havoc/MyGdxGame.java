package com.gamesbykevin.havoc;

import com.badlogic.gdx.*;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.gamesbykevin.havoc.assets.AssetManagerHelper;
import com.gamesbykevin.havoc.level.Level;
import com.gamesbykevin.havoc.player.Player;

import static com.gamesbykevin.havoc.assets.AssetManagerHelper.*;
import static com.gamesbykevin.havoc.texture.TextureHelper.addTextures;

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

	//where we load our assets
	private AssetManager assetManager;

	//used to draw progress bar info
	private ShapeRenderer shapeRenderer;
	private Viewport viewport;
	private SpriteBatch batch;
	private BitmapFont font;

	//dimensions of the progress bar
	private static final float PROGRESS_BAR_WIDTH = (SIZE_WIDTH / 2f);
	private static final float PROGRESS_BAR_HEIGHT = 50f;

	//change the size of the font
	private static final float FONT_SCALE = 1.75f;

	public enum Steps {
		Step1, Step2, Step3, Step4, Step5, Step6, Step7, Step8
	}

	//current step
	private Steps step;

	@Override
	public void create () {

		//create objects etc...
		getAssetManager();

		//create view port
		this.viewport = new StretchViewport(SIZE_WIDTH, SIZE_HEIGHT);

		//create sprite batch
		this.batch = new SpriteBatch();

		//create our object
		this.shapeRenderer = new ShapeRenderer();

		//create our font
		this.font = new BitmapFont();
		this.font.getData().setScale(FONT_SCALE);

		//start at step 1
		setStep(Steps.Step1);
	}

	@Override
	public void resize(int width, int height) {

		//call parent
		super.resize(width, height);

		//set it up for rendering
		getViewport().update(width, height, true);
		getBatch().setProjectionMatrix(getViewport().getCamera().combined);
		getShapeRenderer().setProjectionMatrix(getViewport().getCamera().combined);
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
							}

							renderProgressBar(true, "Verifying assets");
							break;
						}
					}

					//if we are good, go to the next step
					if (success) {
						setStep(Steps.Step2);
						renderProgressBar(true, "Generating dungeon");
					}

				} else {
					getAssetManager().update();
					renderProgressBar(false, "Loading assets");
					System.out.println("progress: " + getAssetManager().getProgress());
				}
				break;

			case Step2:

				//generate dungeon
				renderProgressBar(true, "Generating dungeon");
				getLevel().getDungeon().generate();

				//flag the players start location
				getPlayer().setStart(getLevel().getDungeon().getStartCol(), getLevel().getDungeon().getStartRow());
				setStep(Steps.Step3);
				break;

			case Step3:

				//creating weapons
				renderProgressBar(true, "Creating weapons");
				getLevel().getPlayer().createWeapons(getLevel());
				setStep(Steps.Step4);
				break;

			case Step4:

				//add obstacles
				renderProgressBar(true, "Spawning obstacles");
				getLevel().getObstacles().spawn();
				setStep(Steps.Step5);
				break;

			case Step5:

				//add enemies
				renderProgressBar(true, "Spawning enemies");
				getLevel().getEnemies().spawn();
				setStep(Steps.Step6);
				break;

			case Step6:

				//add collectibles
				renderProgressBar(true, "Spawning collectibles");
				getLevel().getCollectibles().spawn();
				setStep(Steps.Step7);
				break;

			case Step7:

				//create level textures
				renderProgressBar(true, "Applying textures");
				addTextures(getLevel());
				setStep(Steps.Step8);
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

	private void renderProgressBar(boolean fill, String description) {

		System.out.println(description);

		//get the progress
		float progress = getAssetManager().getProgress();

		float x = (SIZE_WIDTH - PROGRESS_BAR_WIDTH) / 2f;
		float y = (SIZE_HEIGHT - PROGRESS_BAR_HEIGHT) / 2f;

		//render the outline
		getShapeRenderer().begin(ShapeRenderer.ShapeType.Line);
		getShapeRenderer().setColor(Color.WHITE);
		getShapeRenderer().rect(x, y, PROGRESS_BAR_WIDTH, PROGRESS_BAR_HEIGHT);
		getShapeRenderer().end();

		//fill the progress bar
		getShapeRenderer().begin(ShapeRenderer.ShapeType.Filled);
		getShapeRenderer().setColor(Color.WHITE);

		if (fill) {
			getShapeRenderer().rect(x, y, PROGRESS_BAR_WIDTH, PROGRESS_BAR_HEIGHT);
		} else {
			getShapeRenderer().rect(x, y, PROGRESS_BAR_WIDTH * progress, PROGRESS_BAR_HEIGHT);
		}

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

		this.player = null;
		this.level = null;
		this.assetManager = null;
		this.shapeRenderer = null;
		this.viewport = null;
		this.batch = null;
		this.font = null;
	}
}