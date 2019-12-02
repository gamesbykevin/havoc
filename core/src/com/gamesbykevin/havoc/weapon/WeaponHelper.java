package com.gamesbykevin.havoc.weapon;

import com.gamesbykevin.havoc.decals.Door;
import com.gamesbykevin.havoc.enemies.Enemy;
import com.gamesbykevin.havoc.entities.Entity;
import com.gamesbykevin.havoc.level.Level;

import static com.gamesbykevin.havoc.enemies.Enemy.RANGE_NOTICE;
import static com.gamesbykevin.havoc.enemies.EnemyHelper.chase;
import static com.gamesbykevin.havoc.input.MyController.SPEED_WALK;
import static com.gamesbykevin.havoc.util.Distance.getDistance;

public class WeaponHelper {

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

    //file extension
    protected static final String EXTENSION = ".png";

    //how many times do we check for collision
    private static final int ATTEMPT_LIMIT = 200;

    //how close does the bullet need to be for collision detection
    private static final double BULLET_DISTANCE = 1.25d;

    //how much ammo to add when we collect a collectible
    public static final float AMMO_SMALL_RATIO = .10f;
    public static final float AMMO_LARGE_RATIO = .33f;

    //different types of weapons
    public enum Type {
        Buzz(35f, 30f, BULLETS_MAX_BUZZ, "buzzsaw_cannon_f", WEAPONS_DIR + "buzzsaw_cannon/", 0, 1, 0, 3, 3, 2, 15, 5),
        Glock(33f, 30f, BULLETS_MAX_GLOCK, "glock_handgun_f", WEAPONS_DIR + "glock_handgun/", 0, 1, 0, 3, 2, 8, 9, 1),
        Impact(30f, 20f, BULLETS_MAX_IMPACT, "impact_cannon_f", WEAPONS_DIR + "impact_cannon/", 0, 1, 0, 2, 2, 2, 8, 6),
        Magnum(50f, 30f, BULLETS_MAX_MAGNUM, "magnum_f", WEAPONS_DIR + "magnum/", 0, 1, 0, 4, 4, 11, 14, 1),
        Shotgun(75f, 8f, BULLETS_MAX_SHOTGUN, "shotgun_f", WEAPONS_DIR + "shotgun/", 0, 1, 0, 1, 1, 16, 17, 2),
        Smg(20f, 30f, BULLETS_MAX_SMG, "smg_f", WEAPONS_DIR + "smg/", 0, 1, 0, 1, 1, 3, 9, 2),
        Lance(3f, 1f, BULLETS_MAX_LANCE, "thermic_lance_f", WEAPONS_DIR + "thermic_lance/", 0, 1, 1, 3, 4, 2, 16, 4);

            private final float damage;
            private final float range;
            private final int bulletsMax;
            private final String fileName;
            private final String dir;
            private final int restIndexStart;
            private final int restIndexCount;
            private final int startIndexStart;
            private final int startIndexCount;
            private final int attackIndexStart;
            private final int attackIndexCount;
            private final int stopIndexStart;
            private final int stopIndexCount;

            Type(float damage, float range, int bulletsMax, String fileName, String dir,
            int restIndexStart, int restIndexCount, int startIndexStart, int startIndexCount,
            int attackIndexStart, int attackIndexCount, int stopIndexStart, int stopIndexCount) {
                this.damage = damage;
                this.range = range;
                this.bulletsMax = bulletsMax;
                this.fileName = fileName;
                this.dir = dir;
                this.restIndexStart = restIndexStart;
                this.restIndexCount = restIndexCount;
                this.startIndexStart = startIndexStart;
                this.startIndexCount = startIndexCount;
                this.attackIndexStart = attackIndexStart;
                this.attackIndexCount = attackIndexCount;
                this.stopIndexStart = stopIndexStart;
                this.stopIndexCount = stopIndexCount;
            }

            public float getDamage() {
                return this.damage;
            }

            public float getRange() {
                return this.range;
            }

            public int getBulletsMax() {
                return this.bulletsMax;
            }

            public String getDir() {
                return this.dir;
            }

            public String getFileName() {
                return this.fileName;
            }

            public int getRestIndexStart() {
                return this.restIndexStart;
            }

            public int getRestIndexCount() {
                return this.restIndexCount;
            }

            public int getStartIndexStart() {
                return this.startIndexStart;
            }

            public int getStartIndexCount() {
                return this.startIndexCount;
            }

            public int getAttackIndexStart() {
                return this.attackIndexStart;
            }

            public int getAttackIndexCount() {
                return this.attackIndexCount;
            }

            public int getStopIndexStart() {
                return this.stopIndexStart;
            }

            public int getStopIndexCount() {
                return this.stopIndexCount;
            }
        }

    public static void checkAttack(Level level) {

        final double rotation = level.getPlayer().getController().getRotation();

        //get the current assigned weapon
        Weapon weapon = level.getPlayer().getWeapons().getWeapon();

        //what direction are we facing
        double angle = Math.toRadians(rotation);

        //start position of attack
        float col = level.getPlayer().getCamera3d().position.x;
        float row = level.getPlayer().getCamera3d().position.y;

        //calculate the distance moved
        float xa = (float)((0 * Math.cos(angle)) - (1 * Math.sin(angle)));
        float ya = (float)((1 * Math.cos(angle)) + (0 * Math.sin(angle)));
        xa *= SPEED_WALK;
        ya *= SPEED_WALK;

        int attempts = 0;

        //do we have range
        boolean range = false;

        //enemy that we hit
        Enemy enemy = null;

        //check for bullet impact only so many times
        while (attempts < ATTEMPT_LIMIT) {

            for (int i = 0; i < level.getEnemies().getEntityList().size(); i++) {

                Entity entity = level.getEnemies().getEntityList().get(i);

                //skip if dead
                if (!entity.isSolid())
                    continue;

                //how far are we from the enemy
                double playerDistance = getDistance(entity, level.getPlayer().getCamera3d().position);

                //if too far away to attack skip this enemy
                if (playerDistance >= weapon.getRange())
                    continue;

                //flag that we have range with at least 1 enemy
                range = true;

                //calculate distance
                double distance = getDistance(entity, col, row);

                //if close enough to cause damage to a single enemy
                if (distance <= BULLET_DISTANCE && enemy == null) {

                    //we can only hit 1 enemy
                    enemy = (Enemy)entity;

                    //the enemy is hurting
                    enemy.setStatus(Enemy.Status.Hurt);

                    //deduct the enemies health
                    enemy.setHealth(enemy.getHealth() - weapon.getDamage());

                    //setup the chase
                    chase(level, enemy);

                } else if (distance <= RANGE_NOTICE) {

                    //if the bullet is close enough to the enemy they should be alerted
                    Enemy tmp = (Enemy) entity;

                    //don't do anything if hurt
                    if (tmp.isHurt())
                        continue;

                    if (!tmp.isObstructed(level) && !tmp.isShoot()) {

                        //if the enemy has a clear view of the player they can shoot
                        tmp.setStatus(Enemy.Status.Shoot);
                        chase(level, tmp);

                    } else if (tmp.getPathIndex() > 0 || tmp.isIdle()) {

                        //if we didn't calculate yet or the enemy is idle
                        chase(level, tmp);
                    }
                }
            }

            //if we don't have range with any enemies skip this
            if (!range)
                break;

            //if we struck an enemy
            if (enemy != null)
                break;

            //move to the next position
            col += xa;
            row += ya;

            //check if we went out of bounds
            if (!level.getDungeon().hasMap((int)col, (int)row))
                return;

            if (level.getDungeon().hasInteract((int)col, (int)row)) {

                //get the door at the current location
                Door door = level.getDoorDecal((int)col, (int)row);

                if (door != null) {

                    //we hit the door
                    if (!door.isOpen())
                        return;

                } else {

                    //we hit something else
                    return;
                }
            }

            //keep track of the attempts
            attempts++;
        }
    }

    protected static void switchWeaponOn(Weapon weapon) {

        if (weapon.getRow() < DEFAULT_WEAPON_Y) {
            weapon.setRow(weapon.getRow() + DEFAULT_VELOCITY_SWITCH_Y);
        } else {
            weapon.setRow(DEFAULT_WEAPON_Y);
            weapon.setSwitchingOn(false);
        }
    }

    protected static void switchWeaponOff(Weapon weapon) {

        if (weapon.getRow() > DEFAULT_WEAPON_Y - WEAPON_HEIGHT) {
            weapon.setRow(weapon.getRow() - DEFAULT_VELOCITY_SWITCH_Y);
        } else {
            weapon.setRow(DEFAULT_WEAPON_Y - WEAPON_HEIGHT);
            weapon.setSwitchingOff(false);
        }
    }
}