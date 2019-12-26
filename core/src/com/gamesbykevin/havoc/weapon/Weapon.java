package com.gamesbykevin.havoc.weapon;

import com.gamesbykevin.havoc.animation.SpriteAnimation;
import com.gamesbykevin.havoc.assets.AudioHelper;
import com.gamesbykevin.havoc.entities.Entity2d;
import com.gamesbykevin.havoc.input.MyController;
import com.gamesbykevin.havoc.level.Level;
import com.gamesbykevin.havoc.util.Disposable;

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

    //do we play a sound effect?
    private AudioHelper.Sfx soundEffect;

    public Weapon(Type type) {

        //call parent
        super(ANIMATIONS);

        //save the type of weapon
        this.type = type;

        //assign sound effect
        setShoot(type.getShoot());

        //setup the weapon animations for each weapon differently
        switch (type) {

            case buzz:
                getAnimations()[INDEX_RESTING] = new SpriteAnimation(type.getPath(), SPRITE_SHEET_COLS, SPRITE_SHEET_ROWS, SPRITE_SHEET_WIDTH, SPRITE_SHEET_HEIGHT, BUZZ_REST_COL, BUZZ_REST_ROW, SPRITE_SHEET_INCREMENT, BUZZ_REST_COUNT, FRAME_DURATION);
                getAnimations()[INDEX_STARTING] = new SpriteAnimation(type.getPath(), SPRITE_SHEET_COLS, SPRITE_SHEET_ROWS, SPRITE_SHEET_WIDTH, SPRITE_SHEET_HEIGHT,BUZZ_START_COL, BUZZ_START_ROW, SPRITE_SHEET_INCREMENT, BUZZ_START_COUNT, FRAME_DURATION);
                getAnimations()[INDEX_ATTACKING] = new SpriteAnimation(type.getPath(), SPRITE_SHEET_COLS, SPRITE_SHEET_ROWS, SPRITE_SHEET_WIDTH, SPRITE_SHEET_HEIGHT, BUZZ_ATTACK_COL, BUZZ_ATTACK_ROW, SPRITE_SHEET_INCREMENT, BUZZ_ATTACK_COUNT, FRAME_DURATION);
                getAnimations()[INDEX_STOPPING] = new SpriteAnimation(type.getPath(), SPRITE_SHEET_COLS, SPRITE_SHEET_ROWS, SPRITE_SHEET_WIDTH, SPRITE_SHEET_HEIGHT, BUZZ_STOP_COL, BUZZ_STOP_ROW, SPRITE_SHEET_INCREMENT, BUZZ_STOP_COUNT, FRAME_DURATION);
                break;

            case smg:
                getAnimations()[INDEX_RESTING] = new SpriteAnimation(type.getPath(), SPRITE_SHEET_COLS, SPRITE_SHEET_ROWS, SPRITE_SHEET_WIDTH, SPRITE_SHEET_HEIGHT, SMG_REST_COL, SMG_REST_ROW, SPRITE_SHEET_INCREMENT, SMG_REST_COUNT, FRAME_DURATION);
                getAnimations()[INDEX_STARTING] = new SpriteAnimation(type.getPath(), SPRITE_SHEET_COLS, SPRITE_SHEET_ROWS, SPRITE_SHEET_WIDTH, SPRITE_SHEET_HEIGHT,SMG_START_COL, SMG_START_ROW, SPRITE_SHEET_INCREMENT, SMG_START_COUNT, FRAME_DURATION);
                getAnimations()[INDEX_ATTACKING] = new SpriteAnimation(type.getPath(), SPRITE_SHEET_COLS, SPRITE_SHEET_ROWS, SPRITE_SHEET_WIDTH, SPRITE_SHEET_HEIGHT, SMG_ATTACK_COL, SMG_ATTACK_ROW, SPRITE_SHEET_INCREMENT, SMG_ATTACK_COUNT, FRAME_DURATION);
                getAnimations()[INDEX_STOPPING] = new SpriteAnimation(type.getPath(), SPRITE_SHEET_COLS, SPRITE_SHEET_ROWS, SPRITE_SHEET_WIDTH, SPRITE_SHEET_HEIGHT, SMG_STOP_COL, SMG_STOP_ROW, SPRITE_SHEET_INCREMENT, SMG_STOP_COUNT, FRAME_DURATION);
                break;

            case glock:
                getAnimations()[INDEX_RESTING] = new SpriteAnimation(type.getPath(), SPRITE_SHEET_COLS, SPRITE_SHEET_ROWS, SPRITE_SHEET_WIDTH, SPRITE_SHEET_HEIGHT, GLOCK_REST_COL, GLOCK_REST_ROW, SPRITE_SHEET_INCREMENT, GLOCK_REST_COUNT, FRAME_DURATION);
                getAnimations()[INDEX_STARTING] = new SpriteAnimation(type.getPath(), SPRITE_SHEET_COLS, SPRITE_SHEET_ROWS, SPRITE_SHEET_WIDTH, SPRITE_SHEET_HEIGHT,GLOCK_START_COL, GLOCK_START_ROW, SPRITE_SHEET_INCREMENT, GLOCK_START_COUNT, FRAME_DURATION);
                getAnimations()[INDEX_ATTACKING] = new SpriteAnimation(type.getPath(), SPRITE_SHEET_COLS, SPRITE_SHEET_ROWS, SPRITE_SHEET_WIDTH, SPRITE_SHEET_HEIGHT, GLOCK_ATTACK_COL, GLOCK_ATTACK_ROW, SPRITE_SHEET_INCREMENT, GLOCK_ATTACK_COUNT, FRAME_DURATION);
                getAnimations()[INDEX_STOPPING] = new SpriteAnimation(type.getPath(), SPRITE_SHEET_COLS, SPRITE_SHEET_ROWS, SPRITE_SHEET_WIDTH, SPRITE_SHEET_HEIGHT, GLOCK_STOP_COL, GLOCK_STOP_ROW, SPRITE_SHEET_INCREMENT, GLOCK_STOP_COUNT, FRAME_DURATION);
                break;

            case lance:
                getAnimations()[INDEX_RESTING] = new SpriteAnimation(type.getPath(), SPRITE_SHEET_COLS, SPRITE_SHEET_ROWS, SPRITE_SHEET_WIDTH, SPRITE_SHEET_HEIGHT, LANCE_REST_COL, LANCE_REST_ROW, SPRITE_SHEET_INCREMENT, LANCE_REST_COUNT, FRAME_DURATION);
                getAnimations()[INDEX_STARTING] = new SpriteAnimation(type.getPath(), SPRITE_SHEET_COLS, SPRITE_SHEET_ROWS, SPRITE_SHEET_WIDTH, SPRITE_SHEET_HEIGHT,LANCE_START_COL, LANCE_START_ROW, SPRITE_SHEET_INCREMENT, LANCE_START_COUNT, FRAME_DURATION);
                getAnimations()[INDEX_ATTACKING] = new SpriteAnimation(type.getPath(), SPRITE_SHEET_COLS, SPRITE_SHEET_ROWS, SPRITE_SHEET_WIDTH, SPRITE_SHEET_HEIGHT, LANCE_ATTACK_COL, LANCE_ATTACK_ROW, SPRITE_SHEET_INCREMENT, LANCE_ATTACK_COUNT, FRAME_DURATION);
                getAnimations()[INDEX_STOPPING] = new SpriteAnimation(type.getPath(), SPRITE_SHEET_COLS, SPRITE_SHEET_ROWS, SPRITE_SHEET_WIDTH, SPRITE_SHEET_HEIGHT, LANCE_STOP_COL, LANCE_STOP_ROW, SPRITE_SHEET_INCREMENT, LANCE_STOP_COUNT, FRAME_DURATION);
                break;

            case impact:
                getAnimations()[INDEX_RESTING] = new SpriteAnimation(type.getPath(), SPRITE_SHEET_COLS, SPRITE_SHEET_ROWS, SPRITE_SHEET_WIDTH, SPRITE_SHEET_HEIGHT, IMPACT_REST_COL, IMPACT_REST_ROW, SPRITE_SHEET_INCREMENT, IMPACT_REST_COUNT, FRAME_DURATION);
                getAnimations()[INDEX_STARTING] = new SpriteAnimation(type.getPath(), SPRITE_SHEET_COLS, SPRITE_SHEET_ROWS, SPRITE_SHEET_WIDTH, SPRITE_SHEET_HEIGHT,IMPACT_START_COL, IMPACT_START_ROW, SPRITE_SHEET_INCREMENT, IMPACT_START_COUNT, FRAME_DURATION);
                getAnimations()[INDEX_ATTACKING] = new SpriteAnimation(type.getPath(), SPRITE_SHEET_COLS, SPRITE_SHEET_ROWS, SPRITE_SHEET_WIDTH, SPRITE_SHEET_HEIGHT, IMPACT_ATTACK_COL, IMPACT_ATTACK_ROW, SPRITE_SHEET_INCREMENT, IMPACT_ATTACK_COUNT, FRAME_DURATION);
                getAnimations()[INDEX_STOPPING] = new SpriteAnimation(type.getPath(), SPRITE_SHEET_COLS, SPRITE_SHEET_ROWS, SPRITE_SHEET_WIDTH, SPRITE_SHEET_HEIGHT, IMPACT_STOP_COL, IMPACT_STOP_ROW, SPRITE_SHEET_INCREMENT, IMPACT_STOP_COUNT, FRAME_DURATION);
                break;

            case magnum:
                getAnimations()[INDEX_RESTING] = new SpriteAnimation(type.getPath(), SPRITE_SHEET_COLS, SPRITE_SHEET_ROWS, SPRITE_SHEET_WIDTH, SPRITE_SHEET_HEIGHT, MAGNUM_REST_COL, MAGNUM_REST_ROW, SPRITE_SHEET_INCREMENT, MAGNUM_REST_COUNT, FRAME_DURATION);
                getAnimations()[INDEX_STARTING] = new SpriteAnimation(type.getPath(), SPRITE_SHEET_COLS, SPRITE_SHEET_ROWS, SPRITE_SHEET_WIDTH, SPRITE_SHEET_HEIGHT,MAGNUM_START_COL, MAGNUM_START_ROW, SPRITE_SHEET_INCREMENT, MAGNUM_START_COUNT, FRAME_DURATION);
                getAnimations()[INDEX_ATTACKING] = new SpriteAnimation(type.getPath(), SPRITE_SHEET_COLS, SPRITE_SHEET_ROWS, SPRITE_SHEET_WIDTH, SPRITE_SHEET_HEIGHT, MAGNUM_ATTACK_COL, MAGNUM_ATTACK_ROW, SPRITE_SHEET_INCREMENT, MAGNUM_ATTACK_COUNT, FRAME_DURATION);
                getAnimations()[INDEX_STOPPING] = new SpriteAnimation(type.getPath(), SPRITE_SHEET_COLS, SPRITE_SHEET_ROWS, SPRITE_SHEET_WIDTH, SPRITE_SHEET_HEIGHT, MAGNUM_STOP_COL, MAGNUM_STOP_ROW, SPRITE_SHEET_INCREMENT, MAGNUM_STOP_COUNT, FRAME_DURATION);
                break;

            case shotgun:
                getAnimations()[INDEX_RESTING] = new SpriteAnimation(type.getPath(), SPRITE_SHEET_COLS, SPRITE_SHEET_ROWS, SPRITE_SHEET_WIDTH, SPRITE_SHEET_HEIGHT, SHOTGUN_REST_COL, SHOTGUN_REST_ROW, SPRITE_SHEET_INCREMENT, SHOTGUN_REST_COUNT, FRAME_DURATION);
                getAnimations()[INDEX_STARTING] = new SpriteAnimation(type.getPath(), SPRITE_SHEET_COLS, SPRITE_SHEET_ROWS, SPRITE_SHEET_WIDTH, SPRITE_SHEET_HEIGHT,SHOTGUN_START_COL, SHOTGUN_START_ROW, SPRITE_SHEET_INCREMENT, SHOTGUN_START_COUNT, FRAME_DURATION);
                getAnimations()[INDEX_ATTACKING] = new SpriteAnimation(type.getPath(), SPRITE_SHEET_COLS, SPRITE_SHEET_ROWS, SPRITE_SHEET_WIDTH, SPRITE_SHEET_HEIGHT, SHOTGUN_ATTACK_COL, SHOTGUN_ATTACK_ROW, SPRITE_SHEET_INCREMENT, SHOTGUN_ATTACK_COUNT, FRAME_DURATION);
                getAnimations()[INDEX_STOPPING] = new SpriteAnimation(type.getPath(), SPRITE_SHEET_COLS, SPRITE_SHEET_ROWS, SPRITE_SHEET_WIDTH, SPRITE_SHEET_HEIGHT, SHOTGUN_STOP_COL, SHOTGUN_STOP_ROW, SPRITE_SHEET_INCREMENT, SHOTGUN_STOP_COUNT, FRAME_DURATION);
                break;
        }

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

    public AudioHelper.Sfx getSoundEffect() {
        return this.soundEffect;
    }

    public void setSoundEffect(AudioHelper.Sfx soundEffect) {
        this.soundEffect = soundEffect;
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
                setSoundEffect(AudioHelper.Sfx.WeaponFireEmpty);
                controller.setShooting(false);
            }
        }

        //if the current animation is finished
        if (getAnimation().isFinish()) {

            if (isStarting()) {

                setIndex(INDEX_ATTACKING);

                //play sound effect
                setSoundEffect((getBullets() != 0) ? getShoot() : AudioHelper.Sfx.WeaponFireEmpty);

                //take a bullet away
                setBullets(getBullets() - 1);

                //check if attack hit enemy
                checkAttack(level);

            } else if (isAttacking()) {

                //if the player no longer wishes to shoot, or we ran out of bullets we will stop
                if (!controller.isShooting() || getBullets() == 0) {

                    //play empty sound effect
                    if (getBullets() == 0)
                        setSoundEffect(AudioHelper.Sfx.WeaponFireEmpty);

                    setIndex(INDEX_STOPPING);

                } else {

                    //continue to attack
                    getAnimation().reset();

                    //play sound effect
                    setSoundEffect((getBullets() != 0) ? getShoot() : AudioHelper.Sfx.WeaponFireEmpty);

                    //take a bullet away
                    setBullets(getBullets() - 1);

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