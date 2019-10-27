package com.gamesbykevin.havoc.player.weapon;

import com.badlogic.gdx.math.Vector3;
import com.gamesbykevin.havoc.decals.Door;
import com.gamesbykevin.havoc.enemies.Enemies;
import com.gamesbykevin.havoc.enemies.Enemy;
import com.gamesbykevin.havoc.entities.Entity;
import com.gamesbykevin.havoc.input.MyController;
import com.gamesbykevin.havoc.level.Level;
import com.gamesbykevin.havoc.player.Player;

import static com.gamesbykevin.havoc.entities.Entities.getDistance;

public class WeaponHelper {

    //where our guns are located
    public static final int INDEX_LANCE = 0;
    public static final int INDEX_GLOCK = 1;
    public static final int INDEX_SMG = 2;
    public static final int INDEX_IMPACT = 3;
    public static final int INDEX_MAGNUM = 4;
    public static final int INDEX_SHOTGUN = 5;
    public static final int INDEX_BUZZ = 6;

    //how many bullets can we hold per weapon
    public static final int BULLETS_MAX_BUZZ = 500;
    public static final int BULLETS_MAX_GLOCK = 50;
    public static final int BULLETS_MAX_IMPACT = 250;
    public static final int BULLETS_MAX_MAGNUM = 75;
    public static final int BULLETS_MAX_SHOTGUN = 100;
    public static final int BULLETS_MAX_SMG = 300;
    public static final int BULLETS_MAX_LANCE = -1;

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
    protected static final float DEFAULT_VELOCITY_SWITCH_Y = 6f;

    //where our weapon images are located
    protected static final String WEAPONS_DIR = "weapons/";

    //how many times do we check for collision
    private static final int ATTEMPT_LIMIT = 200;

    //how close does the bullet need to be for collision detection
    private static final double BULLET_DISTANCE = 1.25d;

    public static final int AMMO_SMALL = 25;
    public static final int AMMO_LARGE = 75;

    public static void reset(Weapon weapon) {

        if (weapon == null)
            return;

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

    public static void checkAttack(MyController controller, Weapon weapon) {

        //level instance
        Level level = controller.getLevel();

        //what direction are we facing
        double angle = Math.toRadians(controller.getRotation());

        //start position of attack
        float col = controller.getCamera3d().position.x;
        float row = controller.getCamera3d().position.y;

        //calculate the distance moved
        double xa = (0 * Math.cos(angle)) - (1 * Math.sin(angle));
        double ya = (1 * Math.cos(angle)) + (0 * Math.sin(angle));
        xa *= controller.getSpeed();
        ya *= controller.getSpeed();

        int attempts = 0;

        //do we have range
        boolean range = false;

        while (attempts < ATTEMPT_LIMIT) {

            for (int i = 0; i < level.getEnemies().getEntityList().size(); i++) {

                Entity entity = level.getEnemies().getEntityList().get(i);

                //skip if dead
                if (!entity.isSolid())
                    continue;

                //how far are we from the enemy
                double playerDistance = getDistance(entity, controller.getCamera3d().position);

                //if too far away to attack skip this enemy
                if (playerDistance >= weapon.getRange())
                    continue;

                //flag that we have range with at least 1 enemy
                range = true;

                //calculate distance
                double dist = getDistance(entity, col, row);

                //if close enough, we hit the enemy
                if (dist <= BULLET_DISTANCE) {
                    Enemy enemy = (Enemy)entity;
                    enemy.setHealth(enemy.getHealth() - weapon.getDamage());
                    enemy.setCol(enemy.getCol() + (float)xa);
                    enemy.setRow(enemy.getRow() + (float)ya);
                    return;
                }
            }

            //if we don't have range with any enemies skip this
            if (!range)
                break;

            //move to the next position
            col += xa;
            row += ya;

            //check if we hit a wall
            if (level.hasWall((int)col, (int)row))
                return;

            if (level.hasDoor((int)col, (int)row)) {

                //get the door at the current location
                Door door = level.getDoorDecal((int)col, (int)row);

                //if the door is closed then we hit the door
                if (door != null && !door.isOpen())
                    return;
            }

            //keep track of the attempts
            attempts++;
        }
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