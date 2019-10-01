package com.gamesbykevin.havoc.player;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.gamesbykevin.havoc.animation.SpriteAnimation;
import com.gamesbykevin.havoc.input.MyController;

import java.util.ArrayList;
import java.util.List;

import static com.gamesbykevin.havoc.player.PlayerHelper.*;

public final class Player {

    //animations for our weapons
    private List<SpriteAnimation> animations;

    //used to render images fast
    private SpriteBatch spriteBatch;

    //current selected weapon
    private int weaponIndex = 0;

    //controller reference
    private final MyController controller;

    //how long is each animation frame
    public static final float FRAME_DURATION = 50f;

    //default resting place for our weapons
    public static final float DEFAULT_WEAPON_X = 300;
    public static final float DEFAULT_WEAPON_Y = 0;

    protected static final float WEAPON_X_MAX = DEFAULT_WEAPON_X + 20;
    protected static final float WEAPON_X_MIN = DEFAULT_WEAPON_X - 20;

    protected static final float WEAPON_Y_MAX = DEFAULT_WEAPON_Y + 1;
    protected static final float WEAPON_Y_MIN = DEFAULT_WEAPON_Y - 10;

    //how fast do we swing the weapon
    protected static final float DEFAULT_VELOCITY_X = .5f;
    protected static final float DEFAULT_VELOCITY_Y = .5f;

    //velocity to move weapon
    private float velocityX = DEFAULT_VELOCITY_X, velocityY = DEFAULT_VELOCITY_Y;

    //where to render our weapon
    private float weaponX = DEFAULT_WEAPON_X, weaponY = DEFAULT_WEAPON_Y;

    private static final String WEAPONS_DIR = "weapons/";

    public Player(MyController controller) {

        //store reference to controller
        this.controller = controller;

        getAnimations().add(new SpriteAnimation(WEAPONS_DIR + "buzzsaw_cannon/", "buzzsaw_cannon_f", ".png", 20));
        getAnimations().add(new SpriteAnimation(WEAPONS_DIR + "glock_handgun/", "glock_handgun_f", ".png", 10));
        getAnimations().add(new SpriteAnimation(WEAPONS_DIR + "impact_cannon/", "impact_cannon_f", ".png", 14));
        getAnimations().add(new SpriteAnimation(WEAPONS_DIR + "magnum/", "magnum_f", ".png", 15));
        getAnimations().add(new SpriteAnimation(WEAPONS_DIR + "shotgun/", "shotgun_f", ".png", 19));
        getAnimations().add(new SpriteAnimation(WEAPONS_DIR + "smg/", "smg_f", ".png", 11, 10));
        getAnimations().add(new SpriteAnimation(WEAPONS_DIR + "thermic_lance/", "thermic_lance_f", ".png", 20));

        for (int i = 0; i < getAnimations().size(); i++) {
            getAnimations().get(i).setLoop(true);
            getAnimations().get(i).setFrameDuration(FRAME_DURATION);
        }

        setWeaponIndex((int)(Math.random() * getAnimations().size()));
    }

    public MyController getController() {
        return this.controller;
    }

    public void update() {

        //update weapon animation
        updateWeapon(this);

        //update the players location
        updateLocation(getController());

        //check for collision
        checkCollision(getController());

        //update the level
        updateLevel(this);
    }

    public void setWeaponIndex(int weaponIndex) {
        this.weaponIndex = weaponIndex;
    }

    public int getWeaponIndex() {
        return this.weaponIndex;
    }

    protected List<SpriteAnimation> getAnimations() {

        if (this.animations == null)
            this.animations = new ArrayList<>();

        return this.animations;
    }

    public SpriteAnimation getAnimation() {
        return getAnimations().get(getWeaponIndex());
    }

    public SpriteBatch getSpriteBatch() {

        if (this.spriteBatch == null)
            this.spriteBatch = new SpriteBatch();

        return this.spriteBatch;
    }

    public float getWeaponX() {
        return this.weaponX;
    }

    public void setWeaponX(float weaponX) {
        this.weaponX = weaponX;
    }

    public float getWeaponY() {
        return this.weaponY;
    }

    public void setWeaponY(float weaponY) {
        this.weaponY = weaponY;
    }

    public float getVelocityX() {
        return this.velocityX;
    }

    public void setVelocityX(float velocityX) {
        this.velocityX = velocityX;
    }

    public float getVelocityY() {
        return this.velocityY;
    }

    public void setVelocityY(float velocityY) {
        this.velocityY = velocityY;
    }

    public void render() {
        getSpriteBatch().setProjectionMatrix(getController().getCamera2d().combined);
        getController().getStage().getBatch().begin();
        getController().getStage().getBatch().draw(getAnimation().getImage(), getWeaponX(), getWeaponY());
        getController().getStage().getBatch().end();
    }
}