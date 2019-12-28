package com.gamesbykevin.havoc.weapon;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.collision.Ray;
import com.gamesbykevin.havoc.assets.AudioHelper;
import com.gamesbykevin.havoc.enemies.Enemy;
import com.gamesbykevin.havoc.level.Level;

import static com.gamesbykevin.havoc.assets.AssetManagerHelper.ASSET_DIR_WEAPONS;
import static com.gamesbykevin.havoc.assets.AssetManagerHelper.SPRITE_SHEET;
import static com.gamesbykevin.havoc.assets.AudioHelper.playSfx;
import static com.gamesbykevin.havoc.decals.Square.COLLISION_RADIUS;
import static com.gamesbykevin.havoc.enemies.Enemy.RANGE_NOTICE;
import static com.gamesbykevin.havoc.entities.EntityHelper.isObstructed;
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

    //how much ammo to add when we collect a collectible
    public static final float AMMO_SMALL_RATIO = .05f;
    public static final float AMMO_LARGE_RATIO = .15f;

    //when firing the weapon everyone with in range will be notified
    public static final float RANGE_FIRE_RATIO = 2.25f;

    //sprite sheet dimensions
    protected static final int SPRITE_SHEET_WIDTH = 200;
    protected static final int SPRITE_SHEET_HEIGHT = 200;
    protected static final int SPRITE_SHEET_COLS = 5;
    protected static final int SPRITE_SHEET_ROWS = 4;
    protected static final int SPRITE_SHEET_INCREMENT = 1;

    //buzz
    protected static final int BUZZ_REST_COL = 0;
    protected static final int BUZZ_REST_ROW = 0;
    protected static final int BUZZ_REST_COUNT = 1;
    protected static final int BUZZ_START_COL = 0;
    protected static final int BUZZ_START_ROW = 0;
    protected static final int BUZZ_START_COUNT = 3;
    protected static final int BUZZ_ATTACK_COL = 3;
    protected static final int BUZZ_ATTACK_ROW = 0;
    protected static final int BUZZ_ATTACK_COUNT = 2;
    protected static final int BUZZ_STOP_COL = 0;
    protected static final int BUZZ_STOP_ROW = 3;
    protected static final int BUZZ_STOP_COUNT = 5;

    //smg
    protected static final int SMG_REST_COL = 0;
    protected static final int SMG_REST_ROW = 0;
    protected static final int SMG_REST_COUNT = 1;
    protected static final int SMG_START_COL = 0;
    protected static final int SMG_START_ROW = 0;
    protected static final int SMG_START_COUNT = 1;
    protected static final int SMG_ATTACK_COL = 1;
    protected static final int SMG_ATTACK_ROW = 0;
    protected static final int SMG_ATTACK_COUNT = 3;
    protected static final int SMG_STOP_COL = 3;
    protected static final int SMG_STOP_ROW = 1;
    protected static final int SMG_STOP_COUNT = 3;

    //glock
    protected static final int GLOCK_REST_COL = 0;
    protected static final int GLOCK_REST_ROW = 0;
    protected static final int GLOCK_REST_COUNT = 1;
    protected static final int GLOCK_START_COL = 0;
    protected static final int GLOCK_START_ROW = 0;
    protected static final int GLOCK_START_COUNT = 3;
    protected static final int GLOCK_ATTACK_COL = 2;
    protected static final int GLOCK_ATTACK_ROW = 0;
    protected static final int GLOCK_ATTACK_COUNT = 8;
    protected static final int GLOCK_STOP_COL = 4;
    protected static final int GLOCK_STOP_ROW = 1;
    protected static final int GLOCK_STOP_COUNT = 1;

    //lance
    protected static final int LANCE_REST_COL = 0;
    protected static final int LANCE_REST_ROW = 0;
    protected static final int LANCE_REST_COUNT = 1;
    protected static final int LANCE_START_COL = 1;
    protected static final int LANCE_START_ROW = 0;
    protected static final int LANCE_START_COUNT = 3;
    protected static final int LANCE_ATTACK_COL = 4;
    protected static final int LANCE_ATTACK_ROW = 0;
    protected static final int LANCE_ATTACK_COUNT = 2;
    protected static final int LANCE_STOP_COL = 1;
    protected static final int LANCE_STOP_ROW = 3;
    protected static final int LANCE_STOP_COUNT = 4;

    //impact
    protected static final int IMPACT_REST_COL = 0;
    protected static final int IMPACT_REST_ROW = 0;
    protected static final int IMPACT_REST_COUNT = 1;
    protected static final int IMPACT_START_COL = 0;
    protected static final int IMPACT_START_ROW = 0;
    protected static final int IMPACT_START_COUNT = 2;
    protected static final int IMPACT_ATTACK_COL = 1;
    protected static final int IMPACT_ATTACK_ROW = 0;
    protected static final int IMPACT_ATTACK_COUNT = 3;
    protected static final int IMPACT_STOP_COL = 3;
    protected static final int IMPACT_STOP_ROW = 1;
    protected static final int IMPACT_STOP_COUNT = 6;

    //magnum
    protected static final int MAGNUM_REST_COL = 0;
    protected static final int MAGNUM_REST_ROW = 0;
    protected static final int MAGNUM_REST_COUNT = 1;
    protected static final int MAGNUM_START_COL = 0;
    protected static final int MAGNUM_START_ROW = 0;
    protected static final int MAGNUM_START_COUNT = 4;
    protected static final int MAGNUM_ATTACK_COL = 4;
    protected static final int MAGNUM_ATTACK_ROW = 0;
    protected static final int MAGNUM_ATTACK_COUNT = 11;
    protected static final int MAGNUM_STOP_COL = 3;
    protected static final int MAGNUM_STOP_ROW = 2;
    protected static final int MAGNUM_STOP_COUNT = 2;

    //shotgun
    protected static final int SHOTGUN_REST_COL = 0;
    protected static final int SHOTGUN_REST_ROW = 0;
    protected static final int SHOTGUN_REST_COUNT = 1;
    protected static final int SHOTGUN_START_COL = 0;
    protected static final int SHOTGUN_START_ROW = 0;
    protected static final int SHOTGUN_START_COUNT = 1;
    protected static final int SHOTGUN_ATTACK_COL = 1;
    protected static final int SHOTGUN_ATTACK_ROW = 0;
    protected static final int SHOTGUN_ATTACK_COUNT = 16;
    protected static final int SHOTGUN_STOP_COL = 2;
    protected static final int SHOTGUN_STOP_ROW = 3;
    protected static final int SHOTGUN_STOP_COUNT = 2;

    //different types of weapons
    public enum Type {
        buzz(35f, 30f, BULLETS_MAX_BUZZ, AudioHelper.Sfx.WeaponFireBuzz),
        glock(33f, 30f, BULLETS_MAX_GLOCK, AudioHelper.Sfx.WeaponFireGlock),
        impact(30f, 20f, BULLETS_MAX_IMPACT, AudioHelper.Sfx.WeaponFireImpact),
        magnum(50f, 30f, BULLETS_MAX_MAGNUM, AudioHelper.Sfx.WeaponFireMagnum),
        shotgun(75f, 8f, BULLETS_MAX_SHOTGUN, AudioHelper.Sfx.WeaponFireShotgun),
        smg(20f, 30f, BULLETS_MAX_SMG, AudioHelper.Sfx.WeaponFireSmg),
        lance(3f, 1f, BULLETS_MAX_LANCE, AudioHelper.Sfx.WeaponFireLance);

        private final float damage;
        private final float range;
        private final int bulletsMax;
        private final AudioHelper.Sfx shoot;
        private final String path;

        Type(float damage, float range, int bulletsMax, AudioHelper.Sfx shoot) {
            this.damage = damage;
            this.range = range;
            this.bulletsMax = bulletsMax;
            this.shoot = shoot;
            this.path = ASSET_DIR_WEAPONS + name() + SPRITE_SHEET;
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

        public String getPath() {
            return this.path;
        }

        public AudioHelper.Sfx getShoot() {
            return this.shoot;
        }
    }

    public static void checkAttack(Level level) {

        //get the current assigned weapon
        Weapon weapon = level.getPlayer().getWeapons().getWeapon();

        //we can only hit 1 enemy
        boolean strike = false;

        //check every enemy
        for (int i = 0; i < level.getEnemies().getEntityList().size(); i++) {

            Enemy enemy = (Enemy)level.getEnemies().getEntityList().get(i);

            //skip if dead
            if (!enemy.isSolid())
                continue;

            //if obstructed the bullet hit a wall
            boolean collisionWall = isObstructed(level, enemy);

            //did the bullet hit the enemy
            boolean collisionEnemy = false;

            //how far is the enemy from the player
            double distance = getDistance(enemy, level.getPlayer());

            //check if we are close enough to strike the enemy and make sure we didn't hit a wall
            if (!collisionWall && distance <= weapon.getRange()) {

                //hopefully we hit the enemy
                Ray ray = level.getPlayer().getCamera3d().getPickRay(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2);
                collisionEnemy = Intersector.intersectRaySphere(ray, enemy.getAnimation().getDecal().getPosition(), COLLISION_RADIUS, null);

                //if we didn't hit the enemy check 1 more spot
                if (!collisionEnemy) {
                    ray = level.getPlayer().getCamera3d().getPickRay(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 4);
                    collisionEnemy = Intersector.intersectRaySphere(ray, enemy.getAnimation().getDecal().getPosition(), COLLISION_RADIUS, null);
                }
            }

            //if we have collision and have not hit an enemy yet
            if (collisionEnemy && !strike) {

                //flag we hit a single enemy
                strike = true;

                if (!enemy.isHurt())
                    playSfx(level.getAssetManager(), enemy.getHurt());

                //the enemy is hurting
                enemy.setStatus(Enemy.Status.Hurt);

                //deduct the enemies health
                enemy.setHealth(enemy.getHealth() - weapon.getDamage());

                //flag chase the player
                enemy.setChase(true);

            } else if (distance < (RANGE_NOTICE * RANGE_FIRE_RATIO)) {

                //don't do anything if hurt
                if (enemy.isHurt())
                    continue;

                if (enemy.canShoot(level, distance) && !enemy.isShoot()) {

                    //if the enemy has a clear view of the player they can shoot
                    enemy.setStatus(Enemy.Status.Shoot);

                } else if (enemy.getPathIndex() > 0 || enemy.isIdle()) {

                    //flag chase the player
                    enemy.setChase(true);
                }
            }
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