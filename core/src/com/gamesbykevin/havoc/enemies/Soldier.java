package com.gamesbykevin.havoc.enemies;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.math.Vector3;
import com.gamesbykevin.havoc.animation.DecalAnimation;
import com.gamesbykevin.havoc.assets.AudioHelper.Sfx;

import static com.gamesbykevin.havoc.animation.DecalAnimation.*;
import static com.gamesbykevin.havoc.assets.AssetManagerHelper.*;
import static com.gamesbykevin.havoc.assets.AudioHelper.Sfx.*;
import static com.gamesbykevin.havoc.enemies.EnemyHelper.*;
import static com.gamesbykevin.havoc.enemies.EnemyHelper.DIRECTION_E;

public final class Soldier extends Enemy {

    //different amounts of damage for each soldier
    public static final int DAMAGE_MAX_DOCTOR = 2;
    public static final int DAMAGE_MAX_GUARD = 3;
    public static final int DAMAGE_MAX_SERGEANT = 4;
    public static final int DAMAGE_MAX_LIEUTENANT = 5;
    public static final int DAMAGE_MAX_OFFICER = 6;
    public static final int DAMAGE_MAX_MAJOR = 7;

    //animation delay
    protected static final float DURATION_IDLE =  1000f;
    protected static final float DURATION_PAIN =  250f;
    protected static final float DURATION_WALK =  200f;
    protected static final float DURATION_DIE =   150f;
    protected static final float DURATION_SHOOT = 250f;
    protected static final float DURATION_ALERT = 175f;
    protected static final float DURATION_PAUSE = 1100f;

    //different animations
    public static final int INDEX_DIE = 0;
    public static final int INDEX_PAIN = 1;
    public static final int INDEX_IDLE_S = 2;
    public static final int INDEX_IDLE_SW = 3;
    public static final int INDEX_IDLE_W = 4;
    public static final int INDEX_IDLE_NW = 5;
    public static final int INDEX_IDLE_N = 6;
    public static final int INDEX_IDLE_NE = 7;
    public static final int INDEX_IDLE_E = 8;
    public static final int INDEX_IDLE_SE = 9;
    public static final int INDEX_SHOOT = 10;
    public static final int INDEX_WALK_S = 11;
    public static final int INDEX_WALK_SW = 12;
    public static final int INDEX_WALK_W = 13;
    public static final int INDEX_WALK_NW = 14;
    public static final int INDEX_WALK_N = 15;
    public static final int INDEX_WALK_NE = 16;
    public static final int INDEX_WALK_E = 17;
    public static final int INDEX_WALK_SE = 18;
    public static final int INDEX_ALERT = 19;
    public static final int INDEX_PAUSE = 20;

    //how many animations are there?
    public static final int ANIMATION_COUNT = 21;

    //size of sprite sheet
    private static final int SPRITE_SHEET_COLS = 7;
    private static final int SPRITE_SHEET_ROWS = 7;

    public enum Type {

        doctor_1(DAMAGE_MAX_DOCTOR, EnemyAlert5, EnemyHurt5, EnemyDead5, EnemyWeaponShoot5),
        doctor_2(DAMAGE_MAX_DOCTOR, EnemyAlert5, EnemyHurt5, EnemyDead5, EnemyWeaponShoot5),
        guard_1(DAMAGE_MAX_GUARD, EnemyAlert1, EnemyHurt1, EnemyDead1, EnemyWeaponShoot1),
        guard_2(DAMAGE_MAX_GUARD, EnemyAlert1, EnemyHurt1, EnemyDead1, EnemyWeaponShoot1),
        guard_3(DAMAGE_MAX_GUARD, EnemyAlert1, EnemyHurt1, EnemyDead1, EnemyWeaponShoot1),
        guard_4(DAMAGE_MAX_GUARD, EnemyAlert1, EnemyHurt1, EnemyDead1, EnemyWeaponShoot1),
        guard_5(DAMAGE_MAX_GUARD, EnemyAlert1, EnemyHurt1, EnemyDead1, EnemyWeaponShoot1),
        guard_6(DAMAGE_MAX_GUARD, EnemyAlert1, EnemyHurt1, EnemyDead1, EnemyWeaponShoot1),
        guard_7(DAMAGE_MAX_GUARD, EnemyAlert1, EnemyHurt1, EnemyDead1, EnemyWeaponShoot1),
        guard_8(DAMAGE_MAX_GUARD, EnemyAlert1, EnemyHurt1, EnemyDead1, EnemyWeaponShoot1),
        guard_9(DAMAGE_MAX_GUARD, EnemyAlert1, EnemyHurt1, EnemyDead1, EnemyWeaponShoot1),
        guard_10(DAMAGE_MAX_GUARD, EnemyAlert1, EnemyHurt1, EnemyDead1, EnemyWeaponShoot1),
        guard_11(DAMAGE_MAX_GUARD, EnemyAlert1, EnemyHurt1, EnemyDead1, EnemyWeaponShoot1),
        guard_12(DAMAGE_MAX_GUARD, EnemyAlert1, EnemyHurt1, EnemyDead1, EnemyWeaponShoot1),
        lieutenant_1(DAMAGE_MAX_LIEUTENANT, EnemyAlert6, EnemyHurt6, EnemyDead6, EnemyWeaponShoot6),
        lieutenant_2(DAMAGE_MAX_LIEUTENANT, EnemyAlert6, EnemyHurt6, EnemyDead6, EnemyWeaponShoot6),
        lieutenant_3(DAMAGE_MAX_LIEUTENANT, EnemyAlert6, EnemyHurt6, EnemyDead6, EnemyWeaponShoot6),
        lieutenant_4(DAMAGE_MAX_LIEUTENANT, EnemyAlert6, EnemyHurt6, EnemyDead6, EnemyWeaponShoot6),
        lieutenant_5(DAMAGE_MAX_LIEUTENANT, EnemyAlert6, EnemyHurt6, EnemyDead6, EnemyWeaponShoot6),
        major_1(DAMAGE_MAX_MAJOR, EnemyAlert2, EnemyHurt2, EnemyDead2, EnemyWeaponShoot2),
        major_2(DAMAGE_MAX_MAJOR, EnemyAlert2, EnemyHurt2, EnemyDead2, EnemyWeaponShoot2),
        officer_1(DAMAGE_MAX_OFFICER, EnemyAlert4, EnemyHurt4, EnemyDead4, EnemyWeaponShoot4),
        officer_2(DAMAGE_MAX_OFFICER, EnemyAlert4, EnemyHurt4, EnemyDead4, EnemyWeaponShoot4),
        officer_3(DAMAGE_MAX_OFFICER, EnemyAlert4, EnemyHurt4, EnemyDead4, EnemyWeaponShoot4),
        officer_4(DAMAGE_MAX_OFFICER, EnemyAlert4, EnemyHurt4, EnemyDead4, EnemyWeaponShoot4),
        officer_5(DAMAGE_MAX_OFFICER, EnemyAlert4, EnemyHurt4, EnemyDead4, EnemyWeaponShoot4),
        sergeant_1(DAMAGE_MAX_SERGEANT, EnemyAlert3, EnemyHurt3, EnemyDead3, EnemyWeaponShoot3),
        sergeant_2(DAMAGE_MAX_SERGEANT, EnemyAlert3, EnemyHurt3, EnemyDead3, EnemyWeaponShoot3),
        sergeant_3(DAMAGE_MAX_SERGEANT, EnemyAlert3, EnemyHurt3, EnemyDead3, EnemyWeaponShoot3),
        sergeant_4(DAMAGE_MAX_SERGEANT, EnemyAlert3, EnemyHurt3, EnemyDead3, EnemyWeaponShoot3),
        sergeant_5(DAMAGE_MAX_SERGEANT, EnemyAlert3, EnemyHurt3, EnemyDead3, EnemyWeaponShoot3);

        private final Sfx alert, hurt, dead, shoot;
        private final int damageMax;
        private final String path;

        Type(int damageMax, Sfx alert, Sfx hurt, Sfx dead, Sfx shoot) {
            this.damageMax = damageMax;
            this.alert = alert;
            this.hurt = hurt;
            this.dead = dead;
            this.shoot = shoot;
            this.path = ASSET_DIR_SOLDIER + name() + SPRITE_SHEET;
        }

        public String getPath() {
            return this.path;
        }

        public Sfx getAlert() {
            return this.alert;
        }

        public Sfx getHurt() {
            return this.hurt;
        }

        public Sfx getDead() {
            return this.dead;
        }

        public Sfx getShoot() {
            return this.shoot;
        }

        public int getDamageMax() {
            return this.damageMax;
        }
    }

    //type of soldier
    private final Type type;

    public Soldier(AssetManager assetManager, Type type) {

        //call parent
        super(ANIMATION_COUNT);

        //assign type
        this.type = type;

        //assign sound effects
        super.setShoot(getType().getShoot());
        super.setDead(getType().getDead());
        super.setAlert(getType().getAlert());
        super.setHurt(getType().getHurt());

        //how much damage can the soldier cause
        setDamageMax(type.getDamageMax());

        //setup animations
        addAnimation(assetManager, INDEX_DIE, 0, 0, 1, 4, DURATION_DIE);
        addAnimation(assetManager, INDEX_PAIN,4, 0, 1, 2, DURATION_PAIN);
        addAnimation(assetManager, INDEX_IDLE_S,2, 1, 1, 1, DURATION_IDLE);
        addAnimation(assetManager, INDEX_IDLE_SW,3, 1, 1, 1, DURATION_IDLE);
        addAnimation(assetManager, INDEX_IDLE_W,4, 1, 1, 1, DURATION_IDLE);
        addAnimation(assetManager, INDEX_IDLE_NW,5, 1, 1, 1, DURATION_IDLE);
        addAnimation(assetManager, INDEX_IDLE_N,6, 1, 1, 1, DURATION_IDLE);
        addAnimation(assetManager, INDEX_IDLE_NE,0, 2, 1, 1, DURATION_IDLE);
        addAnimation(assetManager, INDEX_IDLE_E,1, 2, 1, 1, DURATION_IDLE);
        addAnimation(assetManager, INDEX_IDLE_SE,2, 2, 1, 1, DURATION_IDLE);
        addAnimation(assetManager, INDEX_SHOOT,6, 0, 1, 3, DURATION_SHOOT);
        addAnimation(assetManager, INDEX_ALERT,0, 1, 1, 2, DURATION_ALERT);
        addAnimation(assetManager, INDEX_PAUSE,6, 0, 1,1, DURATION_PAUSE);
        addAnimation(assetManager, INDEX_WALK_S,3, 2, 8, 4, DURATION_WALK);
        addAnimation(assetManager, INDEX_WALK_SW,4, 2, 8, 4, DURATION_WALK);
        addAnimation(assetManager, INDEX_WALK_W,5, 2, 8, 4, DURATION_WALK);
        addAnimation(assetManager, INDEX_WALK_NW,6, 2, 8, 4, DURATION_WALK);
        addAnimation(assetManager, INDEX_WALK_N,0, 3, 8, 4, DURATION_WALK);
        addAnimation(assetManager, INDEX_WALK_NE,1, 3, 8, 4, DURATION_WALK);
        addAnimation(assetManager, INDEX_WALK_E, 2, 3, 8, 4, DURATION_WALK);
        addAnimation(assetManager, INDEX_WALK_SE, 3, 3, 8, 4, DURATION_WALK);
    }

    private void addAnimation(AssetManager assetManager, int index, int startCol, int startRow, int increment, int count, float duration) {
        setAnimation(index, new DecalAnimation(assetManager, getType().getPath(), SPRITE_SHEET_COLS, SPRITE_SHEET_ROWS, SPRITE_SHEET_FRAME_WIDTH, SPRITE_SHEET_FRAME_HEIGHT, startCol, startRow, increment, count, duration, DEFAULT_WIDTH, DEFAULT_HEIGHT, BILLBOARD_ENABLED));
    }

    public Type getType() {
        return this.type;
    }

    @Override
    public void reset() {

        //call parent
        super.reset();

        //set default index
        setIndex(INDEX_IDLE_E);
    }

    @Override
    public boolean isFacing(Vector3 location) {

        switch (getDirection()) {
            case DIRECTION_E:
                return (location.x > getCol());

            case DIRECTION_W:
                return (location.x < getCol());

            case DIRECTION_N:
                return (location.y > getRow());

            case DIRECTION_S:
                return (location.y < getRow());

            default:
                return false;
        }
    }

    @Override
    public void updateIndex(Vector3 position) {

        int index = -1;

        switch (getStatus()) {

            case Idle:
                index = getAnimationIndex(this, position, true);
                break;

            case Hurt:
                index = INDEX_PAIN;
                break;

            case Die:
                index = INDEX_DIE;
                break;

            case Alert:
                index = INDEX_ALERT;
                break;

            case Shoot:
                index = INDEX_SHOOT;
                break;

            case Walk:
                index = getAnimationIndex(this, position, false);
                break;

            case Pause:
                index = INDEX_PAUSE;
                break;
        }

        //if index is different we will assign it
        if (index >= 0 && index != getIndex())
            setIndex(index);
    }

    protected static int getAnimationIndex(Enemy enemy, Vector3 position, boolean idle) {

        //where is the player in relation to this enemy
        boolean n = ((int)position.y > (int)enemy.getRow());
        boolean s = ((int)position.y < (int)enemy.getRow());
        boolean w = ((int)position.x < (int)enemy.getCol());
        boolean e = ((int)position.x > (int)enemy.getCol());

        //update the animation depending on where the enemy is facing
        switch (enemy.getDirection()) {

            case DIRECTION_S:
                if (n && w)
                    return (idle) ? INDEX_IDLE_NE : INDEX_WALK_NE;
                if (n && e)
                    return (idle) ? INDEX_IDLE_NW : INDEX_WALK_NW;
                if (s && w)
                    return (idle) ? INDEX_IDLE_SE : INDEX_WALK_SE;
                if (s && e)
                    return (idle) ? INDEX_IDLE_SW : INDEX_WALK_SW;
                if (n)
                    return (idle) ? INDEX_IDLE_N : INDEX_WALK_N;
                if (w)
                    return (idle) ? INDEX_IDLE_E : INDEX_WALK_E;
                if (e)
                    return (idle) ? INDEX_IDLE_W : INDEX_WALK_W;
                if (s)
                    return (idle) ? INDEX_IDLE_S : INDEX_WALK_S;
                break;

            case DIRECTION_N:
                if (n && w)
                    return (idle) ? INDEX_IDLE_SW : INDEX_WALK_SW;
                if (n && e)
                    return (idle) ? INDEX_IDLE_SE : INDEX_WALK_SE;
                if (s && w)
                    return (idle) ? INDEX_IDLE_NW : INDEX_WALK_NW;
                if (s && e)
                    return (idle) ? INDEX_IDLE_NE : INDEX_WALK_NE;
                if (n)
                    return (idle) ? INDEX_IDLE_S : INDEX_WALK_S;
                if (s)
                    return (idle) ? INDEX_IDLE_N : INDEX_WALK_N;
                if (e)
                    return (idle) ? INDEX_IDLE_E : INDEX_WALK_E;
                if (w)
                    return (idle) ? INDEX_IDLE_W : INDEX_WALK_W;
                break;

            case DIRECTION_W:
                if (n && w)
                    return (idle) ? INDEX_IDLE_SE : INDEX_WALK_SE;
                if (n && e)
                    return (idle) ? INDEX_IDLE_NE : INDEX_WALK_NE;
                if (s && w)
                    return (idle) ? INDEX_IDLE_SW : INDEX_WALK_SW;
                if (s && e)
                    return (idle) ? INDEX_IDLE_NW : INDEX_WALK_NW;
                if (n)
                    return (idle) ? INDEX_IDLE_E : INDEX_WALK_E;
                if (s)
                    return (idle) ? INDEX_IDLE_W : INDEX_WALK_W;
                if (e)
                    return (idle) ? INDEX_IDLE_N : INDEX_WALK_N;
                if (w)
                    return (idle) ? INDEX_IDLE_S : INDEX_WALK_S;
                break;

            case DIRECTION_E:
                if (n && w)
                    return (idle) ? INDEX_IDLE_NW : INDEX_WALK_NW;
                if (n && e)
                    return (idle) ? INDEX_IDLE_SW : INDEX_WALK_SW;
                if (s && w)
                    return (idle) ? INDEX_IDLE_NE : INDEX_WALK_NE;
                if (s && e)
                    return (idle) ? INDEX_IDLE_SE : INDEX_WALK_SE;
                if (n)
                    return (idle) ? INDEX_IDLE_W : INDEX_WALK_W;
                if (s)
                    return (idle) ? INDEX_IDLE_E : INDEX_WALK_E;
                if (e)
                    return (idle) ? INDEX_IDLE_S : INDEX_WALK_S;
                if (w)
                    return (idle) ? INDEX_IDLE_N : INDEX_WALK_N;
                break;
        }

        return -1;
    }
}