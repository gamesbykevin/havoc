package com.gamesbykevin.havoc.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import static com.gamesbykevin.havoc.GameMain.*;
import static com.gamesbykevin.havoc.screen.ParentScreen.PATH_BACKGROUND;

public class MyProgressBar implements Disposable {

    //dimensions of the progress bar
    private static final float PROGRESS_BAR_WIDTH = (getSizeWidth() / 2f);
    private static final float PROGRESS_BAR_HEIGHT = 50f;

    //used to draw progress bar info
    private ShapeRenderer shapeRenderer;
    private Viewport viewport;
    private SpriteBatch batch;
    private BitmapFont font;

    //change the size of the font
    private static final float FONT_SCALE = 1.0f;

    public static final String TEXT_STEP_1 = "Step 1 of 7 Loading assets";
    public static final String TEXT_STEP_2 = "Step 2 of 7 Generating dungeon";
    public static final String TEXT_STEP_3 = "Step 3 of 7 Creating weapons";
    public static final String TEXT_STEP_4 = "Step 4 of 7 Spawning obstacles";
    public static final String TEXT_STEP_5 = "Step 5 of 7 Spawning enemies";
    public static final String TEXT_STEP_6 = "Step 6 of 7 Spawning collectibles";
    public static final String TEXT_STEP_7 = "Step 7 of 7 Applying textures";
    public static final String TEXT_VERIFY = "Verifying assets";

    //our background texture
    private Texture background;

    public MyProgressBar() {

        //create view port
        this.viewport = new StretchViewport(getSizeWidth(), getSizeHeight());

        //create sprite batch
        this.batch = new SpriteBatch();

        //create our object
        this.shapeRenderer = new ShapeRenderer();

        //create our font
        this.font = new BitmapFont();
        this.font.getData().setScale(FONT_SCALE);

        //create our background
        this.background = new Texture(Gdx.files.internal(PATH_BACKGROUND));
    }

    public void resize(int width, int height) {
        getViewport().update(width, height, true);
        getBatch().setProjectionMatrix(getViewport().getCamera().combined);
        getShapeRenderer().setProjectionMatrix(getViewport().getCamera().combined);
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

    public Texture getBackground() {
        return this.background;
    }

    public void renderProgressBar(String description) {
        renderProgressBar(1.0f, description);
    }

    public void renderProgressBar(float progress, String description) {

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
        getBatch().draw(getBackground(), 0, 0, SIZE_WIDTH, SIZE_HEIGHT);
        getFont().draw(getBatch(), description, x, y - (PROGRESS_BAR_HEIGHT / 2));
        getBatch().end();

        //we are finished
        getShapeRenderer().end();
    }

    @Override
    public void dispose() {
        if (this.shapeRenderer != null)
            this.shapeRenderer.dispose();
        if (this.batch != null)
            this.batch.dispose();
        if (this.font != null)
            this.font = null;
        if (this.background != null)
            this.background.dispose();

        this.shapeRenderer = null;
        this.viewport = null;
        this.batch = null;
        this.font = null;
        this.background = null;
    }
}