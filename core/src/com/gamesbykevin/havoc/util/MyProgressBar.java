package com.gamesbykevin.havoc.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import static com.gamesbykevin.havoc.GameEngine.*;
import static com.gamesbykevin.havoc.screen.ParentScreen.PATH_BACKGROUND;

public class MyProgressBar implements Disposable {

    //dimensions of the progress bar
    private static final float PROGRESS_BAR_WIDTH = (getSizeWidth() / 2f);
    private static final float PROGRESS_BAR_HEIGHT = 100f;

    //where to render the progress bar
    private int progressX, progressY;

    //used to draw progress bar info
    private Viewport viewport;
    private SpriteBatch batch;
    private BitmapFont font;

    //change the size of the font
    private static final float FONT_SCALE = 1.5f;

    public static final String TEXT_STEP_1 = "Step 1 of 7 Loading assets";
    public static final String TEXT_STEP_2 = "Step 2 of 7 Generating dungeon";
    public static final String TEXT_STEP_3 = "Step 3 of 7 Creating weapons";
    public static final String TEXT_STEP_4 = "Step 4 of 7 Spawning obstacles";
    public static final String TEXT_STEP_5 = "Step 5 of 7 Spawning enemies";
    public static final String TEXT_STEP_6 = "Step 6 of 7 Spawning collectibles";
    public static final String TEXT_STEP_7 = "Step 7 of 7 Applying textures";
    public static final String TEXT_VERIFY = "Verifying assets";

    //our background texture
    private Texture background, backgroundProgress, progress;

    public static final String PATH_PROGRESS_BACKGROUND = "images/progress/background.png";
    public static final String PATH_PROGRESS = "images/progress/progress.png";

    public MyProgressBar() {

        //create view port
        this.viewport = new StretchViewport(getSizeWidth(), getSizeHeight());

        //create sprite batch
        this.batch = new SpriteBatch();

        //create our font
        this.font = new BitmapFont();
        this.font.getData().setScale(FONT_SCALE);

        //create our background
        this.background = new Texture(Gdx.files.internal(PATH_BACKGROUND));
        this.backgroundProgress = new Texture(Gdx.files.internal(PATH_PROGRESS_BACKGROUND));
        this.progress = new Texture(Gdx.files.internal(PATH_PROGRESS));

        this.progressX = (int)((getSizeWidth() - PROGRESS_BAR_WIDTH) / 2);
        this.progressY = (int)((getSizeHeight() - PROGRESS_BAR_HEIGHT) / 2);
    }

    public void resize(int width, int height) {
        getViewport().update(width, height, true);
        getBatch().setProjectionMatrix(getViewport().getCamera().combined);
    }

    public Texture getBackgroundProgress() {
        return this.backgroundProgress;
    }

    public Texture getProgress() {
        return this.progress;
    }

    public int getProgressX() {
        return this.progressX;
    }

    public int getProgressY() {
        return this.progressY;
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

    public Texture getBackground() {
        return this.background;
    }

    public void renderProgressBar(String description) {
        renderProgressBar(1.0f, description);
    }

    public void renderProgressBar(float progress, String description) {

        //start render
        getBatch().begin();

        //draw screen background
        getBatch().draw(getBackground(), 0, 0, SIZE_WIDTH, SIZE_HEIGHT);

        //render progress bar background
        getBatch().draw(getBackgroundProgress(), getProgressX(), getProgressY(), PROGRESS_BAR_WIDTH, PROGRESS_BAR_HEIGHT);

        //render progress
        getBatch().draw(getProgress(), getProgressX(), getProgressY(), PROGRESS_BAR_WIDTH * progress, PROGRESS_BAR_HEIGHT, 0, 0, (int)(getProgress().getWidth() * progress), getProgress().getHeight(), false, false);

        //render text description
        getFont().draw(getBatch(), description, getProgressX(), getProgressY() - (PROGRESS_BAR_HEIGHT / 3));

        //finish render
        getBatch().end();
    }

    @Override
    public void dispose() {
        if (this.batch != null)
            this.batch.dispose();
        if (this.font != null)
            this.font = null;
        if (this.background != null)
            this.background.dispose();
        if (this.backgroundProgress != null)
            this.backgroundProgress.dispose();
        if (this.progress != null)
            this.progress.dispose();

        this.viewport = null;
        this.batch = null;
        this.font = null;
        this.background = null;
        this.backgroundProgress = null;
        this.progress = null;
    }
}