package com.gamesbykevin.havoc.weapon;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.collision.Ray;
import com.gamesbykevin.havoc.enemies.Enemy;
import com.gamesbykevin.havoc.level.Level;

import static com.gamesbykevin.havoc.assets.AssetManagerHelper.ASSET_DIR_WEAPONS;
import static com.gamesbykevin.havoc.assets.AudioHelper.playHurt;
import static com.gamesbykevin.havoc.decals.Square.COLLISION_RADIUS;
import static com.gamesbykevin.havoc.enemies.Enemy.RANGE_NOTICE;
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
    public static final float AMMO_SMALL_RATIO = .10f;
    public static final float AMMO_LARGE_RATIO = .25f;

    //different types of weapons
    public enum Type {
        Buzz(35f, 30f, BULLETS_MAX_BUZZ, "buzzsaw_cannon_f", "buzzsaw_cannon/", 0, 1, 0, 3, 3, 2, 15, 5),
        Glock(33f, 30f, BULLETS_MAX_GLOCK, "glock_handgun_f", "glock_handgun/", 0, 1, 0, 3, 2, 8, 9, 1),
        Impact(30f, 20f, BULLETS_MAX_IMPACT, "impact_cannon_f", "impact_cannon/", 0, 1, 0, 2, 2, 2, 8, 6),
        Magnum(50f, 30f, BULLETS_MAX_MAGNUM, "magnum_f", "magnum/", 0, 1, 0, 4, 4, 11, 14, 1),
        Shotgun(75f, 8f, BULLETS_MAX_SHOTGUN, "shotgun_f", "shotgun/", 0, 1, 0, 1, 1, 16, 17, 2),
        Smg(20f, 30f, BULLETS_MAX_SMG, "smg_f", "smg/", 0, 1, 0, 1, 1, 3, 9, 2),
        Lance(3f, 1f, BULLETS_MAX_LANCE, "thermic_lance_f", "thermic_lance/", 0, 1, 1, 3, 4, 2, 16, 4);

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
                this.dir = ASSET_DIR_WEAPONS + dir;
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

            //how far are we from the enemy
            double distance = getDistance(enemy, level.getPlayer().getCamera3d());

            //if too far away to attack, skip this enemy
            if (distance >= weapon.getRange())
                continue;

            //we check the middle of the screen
            Ray ray = level.getPlayer().getCamera3d().getPickRay(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2);

            int colMin = (int)((level.getPlayer().getCamera3d().position.x > enemy.getCol()) ? enemy.getCol() : level.getPlayer().getCamera3d().position.x);
            int colMax = (int)((level.getPlayer().getCamera3d().position.x < enemy.getCol()) ? enemy.getCol() : level.getPlayer().getCamera3d().position.x);
            int rowMin = (int)((level.getPlayer().getCamera3d().position.y > enemy.getRow()) ? enemy.getRow() : level.getPlayer().getCamera3d().position.y);
            int rowMax = (int)((level.getPlayer().getCamera3d().position.y < enemy.getRow()) ? enemy.getRow() : level.getPlayer().getCamera3d().position.y);

            //is there collision
            boolean collisionWall = false;

            //check if we hit a wall
            for (int row = rowMin; row <= rowMax; row++) {
                for (int col = colMin; col <= colMax; col++) {

                    //skip on collision
                    if (collisionWall)
                        break;

                    if (level.getDungeon().hasMap(col, row))
                        continue;

                    if (level.getWall(col, row) != null && level.getWall(col, row).hasCollision(ray))
                        collisionWall = true;
                }
            }

            boolean collisionEnemy = false;

            //check if we hit the enemy if we didn't hit a wall
            if (!collisionWall)
                collisionEnemy = Intersector.intersectRaySphere(ray, enemy.getAnimation().getDecal().getPosition(), COLLISION_RADIUS, null);

            //if we have collision and have not hit an enemy yet
            if (collisionEnemy && !strike) {

                //flag we hit a single enemy
                strike = true;

                if (!enemy.isHurt())
                    playHurt(level.getAssetManager());

                //the enemy is hurting
                enemy.setStatus(Enemy.Status.Hurt);

                //deduct the enemies health
                enemy.setHealth(enemy.getHealth() - weapon.getDamage());

                //flag chase the player
                enemy.setChase(true);

            } else if (distance < RANGE_NOTICE) {

                //don't do anything if hurt
                if (enemy.isHurt())
                    continue;

                if (enemy.canShoot(level, distance) && !enemy.isShoot()) {

                    //if the enemy has a clear view of the player they can shoot
                    enemy.setStatus(Enemy.Status.Shoot);

                    //flag chase the player
                    enemy.setChase(true);

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