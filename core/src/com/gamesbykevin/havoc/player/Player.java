package com.gamesbykevin.havoc.player;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.gamesbykevin.havoc.animation.SpriteAnimation;
import com.gamesbykevin.havoc.input.MyController;

import java.util.ArrayList;
import java.util.List;

public class Player {

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

    private static final float WEAPON_X_MAX = DEFAULT_WEAPON_X + 20;
    private static final float WEAPON_X_MIN = DEFAULT_WEAPON_X - 20;

    private static final float WEAPON_Y_MAX = DEFAULT_WEAPON_Y + 1;
    private static final float WEAPON_Y_MIN = DEFAULT_WEAPON_Y - 10;

    //how fast do we swing the weapon
    private static final float DEFAULT_VELOCITY_X = .5f;
    private static final float DEFAULT_VELOCITY_Y = .5f;

    //velocity to move weapon
    private float velocityX = DEFAULT_VELOCITY_X, velocityY = DEFAULT_VELOCITY_Y;

    //where to render our weapon
    private float weaponX = DEFAULT_WEAPON_X, weaponY = DEFAULT_WEAPON_Y;

    private static final String WEAPONS_DIR = "weapons/";

    public Player(MyController controller) {

        //store reference to controller
        this.controller = controller;

        //getAnimations().add(new SpriteAnimation(WEAPONS_DIR + "assault_rifle/", "assault_rifle_f", ".png", 30));
        //getAnimations().add(new SpriteAnimation(WEAPONS_DIR + "auto45/", "auto45_f", ".png", 6));
        getAnimations().add(new SpriteAnimation(WEAPONS_DIR + "buzzsaw_cannon/", "buzzsaw_cannon_f", ".png", 20));
        getAnimations().add(new SpriteAnimation(WEAPONS_DIR + "glock_handgun/", "glock_handgun_f", ".png", 10));
        //getAnimations().add(new SpriteAnimation(WEAPONS_DIR + "hunter_handgun/", "hunter_handgun_f", ".png", 7));
        getAnimations().add(new SpriteAnimation(WEAPONS_DIR + "impact_cannon/", "impact_cannon_f", ".png", 14));
        getAnimations().add(new SpriteAnimation(WEAPONS_DIR + "magnum/", "magnum_f", ".png", 15));
        //getAnimations().add(new SpriteAnimation(WEAPONS_DIR + "rifle/", "rifle_f", ".png", 58, 5));
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

        if (getController().isChange()) {

            setWeaponIndex(getWeaponIndex() + 1);

            if (getWeaponIndex() >= getAnimations().size())
                setWeaponIndex(0);

            getController().setChange(false);
            getAnimation().reset();
        } else {

            if (getController().isShooting()) {

                //update weapon animation
                getAnimation().update();

                //continue to loop as long as we are shooting
                getAnimation().setLoop(true);

            } else {

                //finish animation
                if (getAnimation().getIndex() != 0)
                    getAnimation().update();

                //if we aren't shooting stop looping
                getAnimation().setLoop(false);
            }
        }

        //if we are walking, move weapon
        if (getController().isMoveForward() || getController().isMoveBackward()) {

            //move the weapon
            setWeaponX(getWeaponX() + getVelocityX());
            setWeaponY(getWeaponY() + getVelocityY());

            //use the correct velocity
            if (getWeaponX() >= WEAPON_X_MAX || getWeaponX() <= WEAPON_X_MIN)
                setVelocityX(-getVelocityX());
            if (getWeaponY() >= WEAPON_Y_MAX || getWeaponY() <= WEAPON_Y_MIN)
                setVelocityY(-getVelocityY());

        } else {

            //reset back to the default
            if (getWeaponX() != DEFAULT_WEAPON_X || getWeaponY() != DEFAULT_WEAPON_Y) {

                //move weapon position back to the default
                if (getWeaponX() < DEFAULT_WEAPON_X)
                    setWeaponX(getWeaponX() + DEFAULT_VELOCITY_X);
                if (getWeaponX() > DEFAULT_WEAPON_X)
                    setWeaponX(getWeaponX() - DEFAULT_VELOCITY_X);
                if (getWeaponY() < DEFAULT_WEAPON_Y)
                    setWeaponY(getWeaponY() + DEFAULT_VELOCITY_Y);
                if (getWeaponY() > DEFAULT_WEAPON_Y)
                    setWeaponY(getWeaponY() - DEFAULT_VELOCITY_Y);
                if (Math.abs(getWeaponX() - DEFAULT_WEAPON_X) <= DEFAULT_VELOCITY_X)
                    setWeaponX(DEFAULT_WEAPON_X);
                if (Math.abs(getWeaponY() - DEFAULT_VELOCITY_Y) <= DEFAULT_VELOCITY_Y)
                    setWeaponY(DEFAULT_WEAPON_Y);
            }
        }
    }

    public void setWeaponIndex(int weaponIndex) {
        this.weaponIndex = weaponIndex;
    }

    public int getWeaponIndex() {
        return this.weaponIndex;
    }

    private List<SpriteAnimation> getAnimations() {

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