package com.gamesbykevin.havoc.player.weapon;

import com.gamesbykevin.havoc.player.Player;

public class WeaponHelper {

    //how long is each animation frame
    public static final float FRAME_DURATION = 50f;

    //dimensions of the weapon images
    public static final float WEAPON_WIDTH = 200;
    public static final float WEAPON_HEIGHT = 200;

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

    //how fast do we switch our weapon
    protected static final float DEFAULT_VELOCITY_SWITCH_Y = 5f;

    //where our weapon images are located
    protected static final String WEAPONS_DIR = "weapons/";

    public static void reset(Weapon weapon) {

        //flag that we want to switch on the weapon
        weapon.setSwitchingOff(false);
        weapon.setSwitchingOn(true);

        //position off the screen
        weapon.setWeaponY(DEFAULT_WEAPON_Y - WEAPON_HEIGHT);

        weapon.setResting(true);
        weapon.getResting().reset();
        weapon.getResting().setLoop(false);
        weapon.getResting().setFrameDuration(FRAME_DURATION);

        weapon.setStarting(false);
        weapon.getStarting().reset();
        weapon.getStarting().setLoop(false);
        weapon.getStarting().setFrameDuration(FRAME_DURATION);

        weapon.setAttacking(false);
        weapon.getAttacking().reset();
        weapon.getAttacking().setLoop(false);
        weapon.getAttacking().setFrameDuration(FRAME_DURATION);

        weapon.setStopping(false);
        weapon.getStopping().reset();
        weapon.getStopping().setLoop(false);
        weapon.getStopping().setFrameDuration(FRAME_DURATION);
    }

    public static void updateWeapon(Player player) {

        //get our current weapon
        Weapon weapon = player.getWeapon();

        //if we want to switch weapons we can't be doing anything else
        if (player.getController().isChange()) {

            //we have to be resting to switch weapons
            if (!weapon.isResting())
                player.getController().setChange(false);
            if (weapon.isSwitchingOff() || weapon.isSwitchingOn())
                player.getController().setChange(false);
        }

        if (weapon.isSwitchingOff()) {

            //update the weapon
            switchWeaponOff(weapon);

            //if we are no longer switching off
            if (!weapon.isSwitchingOff()) {

                //reset the current weapon
                reset(weapon);

                //move to the next weapon
                player.setWeaponIndex(player.getWeaponIndex() + 1);

                //flag switching on
                reset(player.getWeapon());
            }

        } else if (weapon.isSwitchingOn()) {

            //update the weapon
            switchWeaponOn(weapon);

            //if we are done switching on
            if (!weapon.isSwitchingOn())
                player.getController().setChange(false);

        } else {

            if (player.getController().isChange())
                weapon.setSwitchingOff(true);

            //update the current weapon if we aren't switching
            weapon.update(player.getController());
        }
    }

    private static void switchWeaponOn(Weapon weapon) {

        if (weapon.getWeaponY() < DEFAULT_WEAPON_Y) {
            weapon.setWeaponY(weapon.getWeaponY() + DEFAULT_VELOCITY_SWITCH_Y);
        } else {
            weapon.setWeaponY(DEFAULT_WEAPON_Y);
            weapon.setSwitchingOn(false);
        }
    }

    private static void switchWeaponOff(Weapon weapon) {

        if (weapon.getWeaponY() > DEFAULT_WEAPON_Y - WEAPON_HEIGHT) {
            weapon.setWeaponY(weapon.getWeaponY() - DEFAULT_VELOCITY_SWITCH_Y);
        } else {
            weapon.setWeaponY(DEFAULT_WEAPON_Y - WEAPON_HEIGHT);
            weapon.setSwitchingOff(false);
        }
    }
}