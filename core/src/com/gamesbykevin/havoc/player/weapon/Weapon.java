package com.gamesbykevin.havoc.player.weapon;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.gamesbykevin.havoc.animation.SpriteAnimation;
import com.gamesbykevin.havoc.input.MyController;

import static com.gamesbykevin.havoc.player.weapon.WeaponHelper.*;

public abstract class Weapon {

    //velocity to move weapon
    private float velocityX = DEFAULT_VELOCITY_X, velocityY = DEFAULT_VELOCITY_Y;

    //where to render our weapon
    private float weaponX = DEFAULT_WEAPON_X, weaponY = DEFAULT_WEAPON_Y;

    //what are we doing with the weapon
    private boolean isResting = true, isStarting = false, isAttacking = false, isStopping = false, isSwitchingOn = false, isSwitchingOff = false;

    //how many bullets do we have
    private int bullets;

    //how much damage does each bullet inflict
    private float damage;

    //how close do we need to be in order to attack?
    private float range;

    //different types of weapons
    public enum Type {
        Buzz, Glock, Impact, Magnum, Shotgun, Smg, Lance
    }

    //which weapon is this?
    private final Type type;

    //render sprite when doing nothing
    protected SpriteAnimation resting;

    //render animation when about to attack
    protected SpriteAnimation starting;

    //render animation when attacking
    protected SpriteAnimation attacking;

    //render animation when we stop attacking
    protected SpriteAnimation stopping;

    public Weapon(Type type) {
        this.type = type;
    }

    public Type getType() {
        return this.type;
    }

    public float getRange() {
        return this.range;
    }

    public void setRange(float range) {
        this.range = range;
    }

    public int getBullets() {
        return this.bullets;
    }

    public void setBullets(int bullets) {
        this.bullets = bullets;
    }

    public float getDamage() {
        return this.damage;
    }

    public void setDamage(float damage) {
        this.damage = damage;
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
        return this.isResting;
    }

    public void setResting(boolean resting) {
        this.isResting = resting;
    }

    public boolean isStarting() {
        return this.isStarting;
    }

    public void setStarting(boolean starting) {
        this.isStarting = starting;
    }

    public boolean isAttacking() {
        return this.isAttacking;
    }

    public void setAttacking(boolean attacking) {
        this.isAttacking = attacking;
    }

    public boolean isStopping() {
        return this.isStopping;
    }

    public void setStopping(boolean stopping) {
        this.isStopping = stopping;
    }

    public SpriteAnimation getResting() {
        return this.resting;
    }

    public SpriteAnimation getStarting() {
        return this.starting;
    }

    public SpriteAnimation getAttacking() {
        return this.attacking;
    }

    public SpriteAnimation getStopping() {
        return this.stopping;
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

    public void update(MyController controller) {

        //if we are walking, move weapon as if we are carrying it
        if (controller.isMoveForward() || controller.isMoveBackward()) {
            carryWeapon();
        } else {
            holdWeapon();
        }

        //if we are resting and the player wants to shoot
        if (isResting() && controller.isShooting() && getBullets() != 0) {

            //stop resting
            setResting(false);
            getResting().reset();

            //start next animation
            setStarting(true);
            getStarting().reset();

        } else if (isStarting() && getStarting().isFinish()) {

            //we finished starting our attack
            setStarting(false);
            getStarting().reset();

            //start attacking
            setAttacking(true);
            getAttacking().reset();

            //take a bullet away
            setBullets(getBullets() - 1);

            //check if attack hit enemy
            checkAttack(controller, this);

        } else if (isAttacking() && getAttacking().isFinish()) {

            //if the player no longer wishes to shoot, or we ran out of bullets we will stop
            if (!controller.isShooting() || getBullets() == 0) {

                //stop the attack animation
                setAttacking(false);
                getAttacking().reset();

                //start the "stop attack" animation
                setStopping(true);
                getStopping().reset();

            } else {

                //continue to attack
                getAttacking().reset();

                //take a bullet away
                setBullets(getBullets() - 1);

                //check if attack hit enemy
                checkAttack(controller, this);
            }

        } else if (isStopping() && getStopping().isFinish()) {

            //stop the animation
            setStopping(false);
            getStopping().reset();

            //go back to resting
            setResting(true);
            getResting().reset();
        }

        if (isResting()) {
            getResting().update();
        } else if (isStarting()) {
            getStarting().update();
        } else if (isAttacking()) {
            getAttacking().update();
        } else if (isStopping()) {
            getStopping().update();
        }
    }

    private void holdWeapon() {

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

    private void carryWeapon() {

        //move the weapon
        setWeaponX(getWeaponX() + getVelocityX());
        setWeaponY(getWeaponY() + getVelocityY());

        //use the correct velocity
        if (getWeaponX() >= WEAPON_X_MAX || getWeaponX() <= WEAPON_X_MIN)
            setVelocityX(-getVelocityX());
        if (getWeaponY() >= WEAPON_Y_MAX || getWeaponY() <= WEAPON_Y_MIN)
            setVelocityY(-getVelocityY());
    }

    public void render(Batch batch) {

        if (isResting()) {
            batch.draw(getResting().getImage(), getWeaponX(), getWeaponY());
        } else if (isStarting()) {
            batch.draw(getStarting().getImage(), getWeaponX(), getWeaponY());
        } else if (isAttacking()) {
            batch.draw(getAttacking().getImage(), getWeaponX(), getWeaponY());
        } else if (isStopping()) {
            batch.draw(getStopping().getImage(), getWeaponX(), getWeaponY());
        }
    }
}