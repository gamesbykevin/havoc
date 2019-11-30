package com.gamesbykevin.havoc.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector3;
import com.gamesbykevin.havoc.input.MyController;
import com.gamesbykevin.havoc.player.weapon.Weapon;
import com.gamesbykevin.havoc.player.weapon.Weapons;
import com.gamesbykevin.havoc.util.Disposable;
import com.gamesbykevin.havoc.util.Hud;

import static com.gamesbykevin.havoc.MyGdxGame.SIZE_HEIGHT;
import static com.gamesbykevin.havoc.MyGdxGame.SIZE_WIDTH;
import static com.gamesbykevin.havoc.level.Level.RENDER_RANGE;
import static com.gamesbykevin.havoc.util.Hud.*;

public final class Player implements Disposable {

    //health range
    public static final int HEALTH_MAX = 100;
    public static final int HEALTH_MIN = 0;

    //what is our health
    private int health = 0;

    //do we have the key
    private boolean key = false;

    //the previous location of the player
    private Vector3 previous;

    //is the player hurt?
    private boolean hurt = false;

    //texture to represent the player is hurt
    private Texture hurtImage;

    //our 3d camera
    private PerspectiveCamera camera3d;

    //start location
    private int startCol, startRow;

    //the game controller
    private MyController controller;

    public Player() {

        //start out with the max health
        setHealth(HEALTH_MAX);

        //we don't have the key (yet)
        setKey(false);

        //player isn't hurt (just yet)
        setHurt(false);

        //store reference to the hurt image
        this.hurtImage = new Texture(Gdx.files.internal("hud/hurt.png"));
    }

    public MyController getController() {

        if (this.controller == null)
            this.controller = new MyController();

        return controller;
    }

    public PerspectiveCamera getCamera3d() {
        return getCamera3d(false);
    }

    public PerspectiveCamera getCamera3d(boolean reset) {

        //create if null
        if (this.camera3d == null) {
            float w = Gdx.graphics.getWidth();
            float h = Gdx.graphics.getHeight();
            this.camera3d = new PerspectiveCamera(67, 1, h/w);
            this.camera3d.near = .05f;
            this.camera3d.far = RENDER_RANGE;
        }

        if (reset) {

            this.camera3d.direction.set(0, 0, -1);
            this.camera3d.up.set(0, 1, 0);
            this.camera3d.update();

            //begin at the start
            float x = getStartCol();
            float y = getStartRow();

            //this.camera3d.position.set((ROOM_SIZE / 2) + .5f, (ROOM_SIZE / 2) + .5f,0);
            this.camera3d.position.set(x, y,0);
            this.camera3d.position.z = 1.50f;
            this.camera3d.rotate(Vector3.X, 90);

            //assign the previous position
            updatePrevious(this.camera3d.position);
        }

        //return our instance
        return this.camera3d;
    }

    public float getStartCol() {
        return this.startCol;
    }

    public void setStart(int col, int row) {
        this.startCol = col;
        this.startRow = row;
        getCamera3d(true);
    }

    public float getStartRow() {
        return this.startRow;
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

    public boolean isHurt() {
        return this.hurt;
    }

    public void setHurt(boolean hurt) {
        this.hurt = hurt;
    }

    public Texture getHurtImage() {
        return this.hurtImage;
    }

    //update the player
    public void update() {
        //do we need to do anything here
    }

    public void render(Weapons weapons) {

        //render the controller first
        getController().render();

        //used to render 2d items etc...
        Batch batch = getController().getStage().getBatch();

        //start
        batch.begin();

        //if hurt render the screen graphic
        if (isHurt()) {
            batch.draw(getHurtImage(), 0, 0, SIZE_WIDTH, SIZE_HEIGHT);
            setHurt(false);
        }

        //render health
        Hud.renderNumber(
            batch,
            getHealth(),
            HUD_HEALTH_X, HUD_HEALTH_Y,
            HUD_NUMBER_WIDTH, HUD_NUMBER_HEIGHT,
            HUD_NUMBER_PAD
        );

        //render key?
        if (hasKey())
            renderKey(batch);

        //render the weapons
        weapons.render();

        //we are done
        batch.end();
    }

    @Override
    public void dispose() {

        if (this.hurtImage != null)
            this.hurtImage.dispose();
        if (this.controller != null)
            this.controller.dispose();

        this.hurtImage = null;
        this.previous = null;
        this.controller = null;
        this.camera3d = null;
    }
}