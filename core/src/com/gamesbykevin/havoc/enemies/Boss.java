package com.gamesbykevin.havoc.enemies;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.math.Vector3;
import com.gamesbykevin.havoc.animation.DecalAnimation;
import com.gamesbykevin.havoc.assets.AudioHelper;

import static com.gamesbykevin.havoc.assets.AssetManagerHelper.*;
import static com.gamesbykevin.havoc.player.Player.HEALTH_MAX;

public final class Boss extends Enemy {

    //how much damage
    public static final int DAMAGE_MAX_BOSS = 15;

    //how much health does the boss have compared to the soldier
    public static final float HEALTH_MAX_RATIO = 4.0f;

    //different animations
    public static final int INDEX_IDLE = 0;
    public static final int INDEX_WALK = 1;
    public static final int INDEX_SHOOT = 2;
    public static final int INDEX_DIE = 3;
    public static final int INDEX_PAUSE = 4;
    public static final int INDEX_ALERT = 5;

    //how many animations are there?
    public static final int ANIMATION_COUNT = 6;

    //animation delay
    protected static final float DURATION_IDLE = 1000f;
    protected static final float DURATION_WALK = 250f;
    protected static final float DURATION_SHOOT = 300f;
    protected static final float DURATION_ALERT = 200f;
    protected static final float DURATION_DIE = 375f;
    protected static final float DURATION_PAUSE = 500f;

    public enum Type {
        boss_1(ASSET_DIR_BOSS + "boss_1/", DAMAGE_MAX_BOSS),
        boss_2(ASSET_DIR_BOSS + "boss_2/", DAMAGE_MAX_BOSS),
        boss_3(ASSET_DIR_BOSS + "boss_3/", DAMAGE_MAX_BOSS),
        boss_4(ASSET_DIR_BOSS + "boss_4/", DAMAGE_MAX_BOSS),
        boss_5(ASSET_DIR_BOSS + "boss_5/", DAMAGE_MAX_BOSS),
        boss_6(ASSET_DIR_BOSS + "boss_6/", DAMAGE_MAX_BOSS),
        boss_7(ASSET_DIR_BOSS + "boss_7/", DAMAGE_MAX_BOSS),
        boss_8(ASSET_DIR_BOSS + "boss_8/", DAMAGE_MAX_BOSS),
        boss_9(ASSET_DIR_BOSS + "boss_9/", DAMAGE_MAX_BOSS),
        boss_10(ASSET_DIR_BOSS + "boss_10/", DAMAGE_MAX_BOSS),
        boss_11(ASSET_DIR_BOSS + "boss_11/", DAMAGE_MAX_BOSS),
        boss_12(ASSET_DIR_BOSS + "boss_12/", DAMAGE_MAX_BOSS),
        boss_13(ASSET_DIR_BOSS + "boss_13/", DAMAGE_MAX_BOSS),
        boss_14(ASSET_DIR_BOSS + "boss_14/", DAMAGE_MAX_BOSS);

        public final String path;
        public final int damageMax;

        Type(String path, int damageMax) {
            this.path = path;
            this.damageMax = damageMax;
        }
    }

    public Boss(AssetManager assetManager, Type type) {

        //call parent
        super(ANIMATION_COUNT);

        //assign the damage
        setDamageMax(type.damageMax);

        switch (type) {
            case boss_1:
            case boss_2:
            case boss_3:
            case boss_4:
            case boss_5:
            case boss_6:
            case boss_7:
            case boss_8:
            case boss_9:
            case boss_10:
            case boss_11:
            case boss_12:
            case boss_13:
            case boss_14:
            default:
                super.setShoot(AudioHelper.Sfx.EnemyWeaponShoot7);
                break;
        }

        //setup animations
        getAnimations()[INDEX_IDLE] = new DecalAnimation(assetManager, type.path, FILENAME_WALK, ASSET_EXT_BMP, 2, 1, DURATION_IDLE);
        getAnimations()[INDEX_WALK] = new DecalAnimation(assetManager, type.path, FILENAME_WALK, ASSET_EXT_BMP, 1, 4, DURATION_WALK);
        getAnimations()[INDEX_SHOOT] = new DecalAnimation(assetManager, type.path, FILENAME_SHOOT, ASSET_EXT_BMP, 1, 3, DURATION_SHOOT);
        getAnimations()[INDEX_ALERT] = new DecalAnimation(assetManager, type.path, FILENAME_SHOOT, ASSET_EXT_BMP, 1, 3, DURATION_ALERT);
        getAnimations()[INDEX_DIE] = new DecalAnimation(assetManager, type.path, FILENAME_DIE, ASSET_EXT_BMP, 1, 4, DURATION_DIE);
        getAnimations()[INDEX_PAUSE] = new DecalAnimation(assetManager, type.path, FILENAME_SHOOT, ASSET_EXT_BMP, 1, 1, DURATION_PAUSE);
    }

    @Override
    public void reset() {

        //call parent
        super.reset();

        //set the max health differently for bosses
        setHealth(HEALTH_MAX * HEALTH_MAX_RATIO);

        //set default index
        setIndex(INDEX_IDLE);
    }

    @Override
    public void updateIndex(Vector3 position) {

        int index = -1;

        if (isIdle()) {
            index = INDEX_IDLE;
        } else if (isWalk()) {
            index = INDEX_WALK;
        } else if (isDie()) {
            index = INDEX_DIE;
        } else if (isShoot()) {
            index = INDEX_SHOOT;
        } else if (isPause()) {
            index = INDEX_PAUSE;
        } else if (isAlert()) {
            index = INDEX_ALERT;
        }

        //if index is different we will assign it
        if (index != getIndex())
            setIndex(index);
    }

    @Override
    public void setStatus(Status status) {

        //the boss will ignore being hurt
        switch (status) {
            case Hurt:
                return;
        }

        super.setStatus(status);
    }

    @Override
    protected boolean isFacing(Vector3 location) {

        //boss is always facing the specified location
        return true;
    }
}