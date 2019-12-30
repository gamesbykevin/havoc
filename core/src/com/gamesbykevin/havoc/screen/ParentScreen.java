package com.gamesbykevin.havoc.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.gamesbykevin.havoc.MyGdxGame;
import com.gamesbykevin.havoc.graphics.Overlay;

public abstract class ParentScreen implements Screen {

    //screen size
    public static final int SCREEN_WIDTH = 800;
    public static final int SCREEN_HEIGHT = 480;

    //game object for reference
    private final MyGdxGame game;

    //our background image
    private Texture backgroundImage;

    //how to draw our background
    private Rectangle backgroundRect;

    //render all objects simultaneously
    private SpriteBatch batch;

    //camera perspective to project etc... images
    private OrthographicCamera camera;

    //our overlay screen
    private Overlay overlay;

    //parent directory for screen assets
    public static final String PATH_PARENT_DIR = "images/menu/";

    //where the background image is located
    public static final String PATH_BACKGROUND = PATH_PARENT_DIR + "background.jpg";

    public ParentScreen(MyGdxGame game) {

        //store game reference
        this.game = game;

        //create our camera of specified size
        this.camera = new OrthographicCamera();
        this.camera.setToOrtho(false, SCREEN_WIDTH, SCREEN_HEIGHT);

        //create our background
        this.backgroundImage = new Texture(Gdx.files.internal(PATH_BACKGROUND));

        //our background will cover the screen
        this.backgroundRect = new Rectangle();
        this.backgroundRect.width = SCREEN_WIDTH;
        this.backgroundRect.height = SCREEN_HEIGHT;
        this.backgroundRect.x = 0;
        this.backgroundRect.y = 0;
    }

    public Overlay getOverlay() {

        if (this.overlay == null)
            this.overlay = new Overlay(Overlay.OVERLAY_TEXT_PAUSED, Overlay.OVERLAY_TRANSPARENCY_PAUSED);

        return this.overlay;
    }

    public Texture getBackgroundImage() {
        return this.backgroundImage;
    }

    public Rectangle getBackgroundRect() {
        return this.backgroundRect;
    }

    public SpriteBatch getBatch() {

        //create a new sprite batch if null
        if (this.batch == null)
            this.batch = new SpriteBatch();

        return this.batch;
    }

    public MyGdxGame getGame() {
        return this.game;
    }

    public void clearScreen() {

        //clear the screen
        Gdx.gl.glClearColor(0f, 0f, 0f, 0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    @Override
    public void hide() {
        //do anything here?
    }

    @Override
    public void resize(int w, int h) {
        //do anything here?
    }

    @Override
    public void show() {
        //do anything here?
    }

    @Override
    public void pause() {
        //do anything here?
    }

    @Override
    public void resume() {
        //do anything here?
    }

    @Override
    public void render(float delta) {

        //clear the screen
        clearScreen();

        //good practice to update the camera at least once per frame
        getCamera().update();

        //set the screen projection coordinates
        getBatch().setProjectionMatrix(getCamera().combined);

        //start the batch
        getBatch().begin();
    }

    public void drawBackground() {
        drawBackground(getBatch());
    }

    public void drawBackground(Batch batch) {
        batch.draw(
            getBackgroundImage(),
            getBackgroundRect().x,
            getBackgroundRect().y,
            getBackgroundRect().width,
            getBackgroundRect().height
        );
    }

    @Override
    public void dispose() {

        if (batch != null)
            batch.dispose();
        if (backgroundImage != null)
            backgroundImage.dispose();
        if (overlay != null)
            overlay.dispose();

        this.backgroundRect = null;
        this.backgroundImage = null;
        this.batch = null;
        this.overlay = null;
        this.camera = null;
    }

    public OrthographicCamera getCamera() {
        return this.camera;
    }
}