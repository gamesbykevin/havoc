package com.gamesbykevin.havoc.weapon;

import com.gamesbykevin.havoc.animation.SpriteAnimation;
import com.gamesbykevin.havoc.assets.AudioHelper;
import com.gamesbykevin.havoc.entities.Entity2d;
import com.gamesbykevin.havoc.input.MyController;
import com.gamesbykevin.havoc.level.Level;
import com.gamesbykevin.havoc.util.Disposable;

import static com.gamesbykevin.havoc.assets.AssetManagerHelper.ASSET_EXT_PNG;
import static com.gamesbykevin.havoc.assets.AudioHelper.playWeapon;
import static com.gamesbykevin.havoc.weapon.WeaponHelper.*;

public class Weapon extends Entity2d implements Disposable {

    //velocity to move weapon
    private float velocityX = DEFAULT_VELOCITY_X, velocityY = DEFAULT_VELOCITY_Y;

    //what are we doing with the weapon
    private boolean isSwitchingOn = false, isSwitchingOff = false;

    //how many bullets do we have
    private int bullets;

    //which weapon is this?
    private final Type type;

    //the gun has 4 animations
    public static final int ANIMATIONS = 4;

    //different animations for the weapon
    public static final int INDEX_RESTING   = 0;
    public static final int INDEX_STARTING  = 1;
    public static final int INDEX_ATTACKING = 2;
    public static final int INDEX_STOPPING  = 3;

    //how many bullets to add by default
    public static final float BULLETS_DEFAULT_RATIO = .33f;

    //shoot sound effect
    private AudioHelper.Sfx shoot;

    public Weapon(Type type) {

        //call parent
        super(ANIMATIONS);

        //save the type of weapon
        this.type = type;

        //assign sound effect
        setShoot(type.shoot);

        //setup the weapons animations
        getAnimations()[INDEX_RESTING] = new SpriteAnimation(getType().getDir(), getType().getFileName(), ASSET_EXT_PNG, getType().getRestIndexStart(), getType().getRestIndexCount(), FRAME_DURATION);
        getAnimations()[INDEX_STARTING] = new SpriteAnimation(getType().getDir(), getType().getFileName(), ASSET_EXT_PNG, getType().getStartIndexStart(), getType().getStartIndexCount(), FRAME_DURATION);
        getAnimations()[INDEX_ATTACKING] = new SpriteAnimation(getType().getDir(), getType().getFileName(), ASSET_EXT_PNG, getType().getAttackIndexStart(), getType().getAttackIndexCount(), FRAME_DURATION);
        getAnimations()[INDEX_STOPPING] = new SpriteAnimation(getType().getDir(), getType().getFileName(), ASSET_EXT_PNG, getType().getStopIndexStart(), getType().getStopIndexCount(), FRAME_DURATION);

        //reset
        reset();

        //reset bullet count
        resetBullets();
    }

    public void resetBullets() {

        //start with the default bullets
        setBullets((int)(getType().getBulletsMax() * BULLETS_DEFAULT_RATIO));
    }

    @Override
    public void reset() {

        //flag that we want to switch on the weapon
        setSwitchingOff(false);
        setSwitchingOn(true);

        //position off the screen
        setCol(DEFAULT_WEAPON_X);
        setRow(DEFAULT_WEAPON_Y - WEAPON_HEIGHT);

        //set default animation
        setIndex(INDEX_RESTING);
    }

    @Override
    public void dispose() {
        super.dispose();
    }

    public Type getType() {
        return this.type;
    }

    public float getRange() {
        return getType().getRange();
    }

    public int getBullets() {
        return this.bullets;
    }

    public void addAmmoLarge() {
        setBullets(getBullets() + (int)(getType().getBulletsMax() * AMMO_LARGE_RATIO));
    }

    public void addAmmoSmall() {
        setBullets(getBullets() + (int)(getType().getBulletsMax() * AMMO_SMALL_RATIO));
    }

    public void setBullets(int bullets) {
        this.bullets = bullets;

        if (this.bullets > getType().getBulletsMax())
            this.bullets = getType().getBulletsMax();
    }

    public AudioHelper.Sfx getShoot() {
        return this.shoot;
    }

    public void setShoot(AudioHelper.Sfx shoot) {
        this.shoot = shoot;
    }

    public float getDamage() {
        return getType().getDamage();
    }

    public boolean isSwitchingOn() {
        return this.isSwitchingOn;
    }

    public void setSwitchingOn(boolean switchingOn) {
        this.isSwitchingOn = switchingOn;
    }

    public boolean isSwitchingOff() {
        return this.isSwitchingOff;
    }

    public void setSwitchingOff(boolean switchingOff) {
        this.isSwitchingOff = switchingOff;
    }

    public boolean isResting() {
        return (getIndex() == INDEX_RESTING);
    }

    public boolean isStarting() {
        return (getIndex() == INDEX_STARTING);
    }

    public boolean isAttacking() {
        return (getIndex() == INDEX_ATTACKING);
    }

    public boolean isStopping() {
        return (getIndex() == INDEX_STOPPING);
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

    @Override
    public void update(Level level) {

        //get the controller
        MyController controller = level.getPlayer().getController();

        //if we are walking, move weapon as if we are carrying it
        if (controller.isMoveForward() || controller.isMoveBackward() || controller.getKnobPercentY() != 0) {
            carryWeapon();
        } else {
            holdWeapon();
        }

        //if we are resting and the player wants to shoot
        if (isResting() && controller.isShooting()) {

            if (getBullets() != 0) {
                setIndex(INDEX_STARTING);
            } else {
                playWeapon(level.getAssetManager(), this);
                controller.setShooting(false);
            }
        }

        //if the current animation is finished
        if (getAnimation().isFinish()) {

            if (isStarting()) {

                setIndex(INDEX_ATTACKING);

                //take a bullet away
                setBullets(getBullets() - 1);

                //play sound effect
                playWeapon(level.getAssetManager(), this);

                //check if attack hit enemy
                checkAttack(level);

            } else if (isAttacking()) {

                //if the player no longer wishes to shoot, or we ran out of bullets we will stop
                if (!controller.isShooting() || getBullets() == 0) {

                    //play empty sound effect
                    if (getBullets() == 0)
                        playWeapon(level.getAssetManager(), this);

                    setIndex(INDEX_STOPPING);

                } else {

                    //continue to attack
                    getAnimation().reset();

                    //take a bullet away
                    setBullets(getBullets() - 1);

                    //play sound effect
                    playWeapon(level.getAssetManager(), this);

                    //check if attack hit enemy
                    checkAttack(level);
                }

            } else if (isStopping()) {
                setIndex(INDEX_RESTING);
            }
        }

        //update the animation
        getAnimation().update();
    }

    private void holdWeapon() {

        //reset back to the default
        if (getCol() != DEFAULT_WEAPON_X || getRow() != DEFAULT_WEAPON_Y) {

            //move weapon position back to the default
            if (getCol() < DEFAULT_WEAPON_X)
                setCol(getCol() + DEFAULT_VELOCITY_X);
            if (getCol() > DEFAULT_WEAPON_X)
                setCol(getCol() - DEFAULT_VELOCITY_X);
            if (getRow() < DEFAULT_WEAPON_Y)
                setRow(getRow() + DEFAULT_VELOCITY_Y);
            if (getRow() > DEFAULT_WEAPON_Y)
                setRow(getRow() - DEFAULT_VELOCITY_Y);
            if (Math.abs(getCol() - DEFAULT_WEAPON_X) <= DEFAULT_VELOCITY_X)
                setCol(DEFAULT_WEAPON_X);
            if (Math.abs(getRow() - DEFAULT_VELOCITY_Y) <= DEFAULT_VELOCITY_Y)
                setRow(DEFAULT_WEAPON_Y);
        }
    }

    private void carryWeapon() {

        //move the weapon
        setCol(getCol() + getVelocityX());
        setRow(getRow() + getVelocityY());

        //use the correct velocity
        if (getCol() >= WEAPON_X_MAX || getCol() <= WEAPON_X_MIN)
            setVelocityX(-getVelocityX());
        if (getRow() >= WEAPON_Y_MAX || getRow() <= WEAPON_Y_MIN)
            setVelocityY(-getVelocityY());
    }

    @Override
    public void setIndex(int index) {
        super.setIndex(index);
        getAnimation().reset();
    }
}