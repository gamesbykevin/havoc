package com.gamesbykevin.havoc.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.gamesbykevin.havoc.assets.AudioHelper;
import com.gamesbykevin.havoc.input.MyController;
import com.gamesbykevin.havoc.level.Level;
import com.gamesbykevin.havoc.util.Restart;
import com.gamesbykevin.havoc.util.Timer;
import com.gamesbykevin.havoc.weapon.Weapons;
import com.gamesbykevin.havoc.util.Disposable;
import com.gamesbykevin.havoc.util.Hud;

import static com.gamesbykevin.havoc.MyGdxGame.*;
import static com.gamesbykevin.havoc.assets.AssetManagerHelper.PATH_COLLECT;
import static com.gamesbykevin.havoc.assets.AssetManagerHelper.PATH_HURT;
import static com.gamesbykevin.havoc.assets.AudioHelper.playSfx;
import static com.gamesbykevin.havoc.level.Level.RENDER_RANGE;
import static com.gamesbykevin.havoc.player.PlayerHelper.HEIGHT_MIN_Z;
import static com.gamesbykevin.havoc.player.PlayerHelper.VELOCITY_Z;
import static com.gamesbykevin.havoc.util.Hud.*;

public final class Player implements Disposable, Restart {

    //health range
    public static final int HEALTH_MAX = 100;
    public static final int HEALTH_MIN = 0;
    public static final int HEALTH_LOW = 25;

    //what is our health
    private int health = 0;

    //do we have the key
    private boolean key = false;

    //did we find the goal?
    private boolean goal = false;

    //the previous location of the player
    private Vector3 previous;

    //track how long we played the level and when to show the level summary screen
    private Timer timerGame, timerGameOver;

    //is the player?
    private boolean hurt = false, collect = false;

    //how long to display the overlay
    private Timer timerHurt, timerCollect;

    //how long to display
    public static final float DURATION_HURT = 100f;
    public static final float DURATION_COLLECT = 100f;
    public static final float DURATION_NOTIFY = 5000f;
    public static final float DURATION_GAME_OVER = 1000f;

    //our 3d camera
    private PerspectiveCamera camera3d;

    //view port for the camera
    private Viewport viewport;

    //start location
    private int startCol, startRow;

    //the game controller
    private MyController controller;

    //weapons the player has
    private Weapons weapons;

    //height the camera will be at
    private static final float HEIGHT_START = 0.075f;

    //reference to our assets
    private final AssetManager assetManager;

    //text to display for notifications
    private String textNotify;

    //keep track how long message is displayed
    private Timer timerNotify;

    //font to display text
    private BitmapFont font;

    //provide space
    private static final int PADDING = 5;

    //where do we render our notification message
    private int notifyX, notifyY;

    //used for font metrics
    private GlyphLayout glyphLayout;

    public Player(AssetManager assetManager) {

        //store reference
        this.assetManager = assetManager;

        //reset values
        reset();
    }

    public int getNotifyX() {
        return this.notifyX;
    }

    public void setNotifyX(int notifyX) {
        this.notifyX = notifyX;
    }

    public int getNotifyY() {
        return this.notifyY;
    }

    public void setNotifyY(int notifyY) {
        this.notifyY = notifyY;
    }

    public Timer getTimerGameOver() {

        if (this.timerGameOver == null)
            this.timerGameOver = new Timer(DURATION_GAME_OVER);

        return this.timerGameOver;
    }

    public Timer getTimerGame() {

        if (this.timerGame == null)
            this.timerGame = new Timer(0);

        return this.timerGame;
    }

    public Timer getTimerHurt() {

        if (this.timerHurt == null)
            this.timerHurt = new Timer(DURATION_HURT);

        return this.timerHurt;
    }

    public Timer getTimerCollect() {

        if (this.timerCollect == null)
            this.timerCollect = new Timer(DURATION_COLLECT);

        return this.timerCollect;
    }

    public Timer getTimerNotify() {

        if (this.timerNotify == null)
            this.timerNotify = new Timer(DURATION_NOTIFY);

        return this.timerNotify;
    }

    public BitmapFont getFont() {

        if (this.font == null)
            this.font = new BitmapFont();

        return this.font;
    }

    public GlyphLayout getGlyphLayout() {

        if (this.glyphLayout == null)
            this.glyphLayout = new GlyphLayout();

        return this.glyphLayout;
    }

    public String getTextNotify() {
        return this.textNotify;
    }

    public void setTextNotify(String textNotify) {

        this.textNotify = textNotify;

        if (this.textNotify == null)
            return;

        //reset timer
        getTimerNotify().reset();

        //set text for our font metrics
        getGlyphLayout().setText(getFont(), getTextNotify());

        //now we know where to render the image
        setNotifyX((SIZE_WIDTH / 2) - (int)(getGlyphLayout().width / 2));
        setNotifyY(SIZE_HEIGHT - (int)(getGlyphLayout().height) - PADDING);
    }

    public AssetManager getAssetManager() {
        return this.assetManager;
    }

    public void createWeapons(Level level) {
        this.weapons = new Weapons(level);
    }

    public Weapons getWeapons() {
        return this.weapons;
    }

    public MyController getController() {

        if (this.controller == null)
            this.controller = new MyController(getAssetManager());

        return controller;
    }

    public PerspectiveCamera getCamera3d() {
        return getCamera3d(false);
    }

    public PerspectiveCamera getCamera3d(boolean reset) {

        //create if null
        if (this.camera3d == null) {
            this.camera3d = new PerspectiveCamera(67f, getSizeWidth(), getSizeHeight());
            this.viewport = new StretchViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), this.camera3d);
            this.camera3d.near = .1f;
            this.camera3d.far = (RENDER_RANGE * 3);
        }

        if (reset) {

            this.camera3d.direction.set(0, 0, -1);
            this.camera3d.up.set(0, 1, 0);
            this.camera3d.update();

            //begin at the start
            float x = getStartCol();
            float y = getStartRow();

            this.camera3d.position.set(x, y, HEIGHT_START);
            this.camera3d.rotate(Vector3.X, 90);

            //assign the previous position
            updatePrevious(this.camera3d.position);
        }

        //return our instance
        return this.camera3d;
    }

    public Viewport getViewport() {
        return this.viewport;
    }

    public float getStartCol() {
        return this.startCol;
    }

    public float getStartRow() {
        return this.startRow;
    }

    public void setStart(int col, int row) {
        this.startCol = col;
        this.startRow = row;
        getCamera3d(true);
    }

    public int getHealth() {
        return this.health;
    }

    public void setHealth(int health) {
        this.health = health;

        if (this.health < HEALTH_MIN)
            this.health = HEALTH_MIN;
        if (this.health > HEALTH_MAX)
            this.health = HEALTH_MAX;

        if (isDead()) {
            setTextNotify("You died");
        } else if (getHealth() <= HEALTH_LOW) {
            setTextNotify("Low health");
        }
    }

    public boolean isGoal() {
        return this.goal;
    }

    public void setGoal(boolean goal) {
        this.goal = goal;
    }

    public boolean hasKey() {
        return this.key;
    }

    public void setKey(boolean key) {
        this.key = key;
    }

    public Vector3 getPrevious() {

        if (this.previous == null)
            this.previous = new Vector3();

        return this.previous;
    }

    public void updatePrevious(Vector3 previous) {
        getPrevious().x = previous.x;
        getPrevious().y = previous.y;
        getPrevious().z = previous.z;
    }

    public boolean isDead() {
        return (getHealth() <= HEALTH_MIN);
    }

    public boolean isHurt() {
        return this.hurt;
    }

    public void setHurt(boolean hurt) {
        this.hurt = hurt;

        if (isHurt())
            getTimerHurt().reset();
    }

    public void setCollect(boolean collect) {
        this.collect = collect;

        if (isCollect())
            getTimerCollect().reset();
    }

    public boolean isCollect() {
        return this.collect;
    }

    //update the player
    public void update(Level level) {

        //update notification timer
        getTimerNotify().update();

        if (isDead()) {

            if (getCamera3d().position.z > HEIGHT_MIN_Z) {

                if (getCamera3d().position.z == HEIGHT_START)
                    playSfx(level.getAssetManager(), AudioHelper.Sfx.HeroDead);

                getCamera3d().position.z -= VELOCITY_Z;

                //don't go below
                if (getCamera3d().position.z < HEIGHT_MIN_Z)
                    getCamera3d().position.z = HEIGHT_MIN_Z;

                //reset the controller
                getController().reset();

            } else {

                //if the controller has been touched, flag reset
                if (getController().isTouch() || Gdx.input.justTouched())
                    level.setReset(true);
            }

        } else if (isGoal()) {

            setTextNotify("Level Complete");
            getTimerGameOver().update();

        } else {

            //if not dead or at the goal update the game timer
            getTimerGame().update();
        }
    }

    @Override
    public void dispose() {

        if (this.controller != null)
            this.controller.dispose();
        if (this.weapons != null)
            this.weapons.dispose();
        if (this.font != null)
            this.font.dispose();

        this.previous = null;
        this.controller = null;
        this.camera3d = null;
        this.weapons = null;
        this.font = null;
        this.timerNotify = null;
        this.textNotify = null;
        this.glyphLayout = null;
        this.timerHurt = null;
        this.timerCollect = null;
        this.timerGame = null;
    }

    @Override
    public void reset() {

        //start out with the max health
        setHealth(HEALTH_MAX);

        //we don't have the key (yet)
        setKey(false);

        //player isn't hurt (just yet)
        setHurt(false);

        //reset the camera position
        getCamera3d(true);

        //we didn't find the goal yet
        setGoal(false);

        //reset timer
        setTextNotify(null);

        getTimerCollect().reset();
        getTimerHurt().reset();
        getTimerNotify().reset();
        getTimerGame().reset();
        getTimerGameOver().reset();
    }

    public void render() {

        //render the controller first if not dead
        if (!isDead() && !isGoal())
            getController().render();

        //used to render 2d items etc...
        Batch batch = getController().getStage().getBatch();

        //start
        batch.begin();

        //if hurt render the screen graphic
        if (isHurt() || isDead()) {

            batch.draw(getAssetManager().get(PATH_HURT, Texture.class), 0, 0, getSizeWidth(), getSizeHeight());

            if (getTimerHurt().isExpired()) {
                setHurt(false);
            } else {
                getTimerHurt().update();
            }

        } else if (isCollect()) {

            batch.draw(getAssetManager().get(PATH_COLLECT, Texture.class), 0, 0, getSizeWidth(), getSizeHeight());

            if (getTimerCollect().isExpired()) {
                setCollect(false);
            } else {
                getTimerCollect().update();
            }

        } else if (isGoal()) {

            //if time expired show level stats
            if (getTimerGameOver().isExpired()) {
                //display stats
            }
        }

        if (!isGoal()) {

            //render health
            Hud.renderNumber(getAssetManager(), batch, getHealth(), HUD_HEALTH_X, HUD_HEALTH_Y);

            //render key?
            if (hasKey())
                renderKey(getAssetManager(), batch);

            //render the weapons
            if (!isDead())
                getWeapons().render();
        }

        //if we have text to display and the timer hasn't expired yet
        if (getTextNotify() != null && !getTimerNotify().isExpired())
            getFont().draw(batch, getTextNotify(), getNotifyX(), getNotifyY());

        //we are done
        batch.end();
    }
}