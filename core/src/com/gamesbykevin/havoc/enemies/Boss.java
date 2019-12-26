package com.gamesbykevin.havoc.enemies;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.math.Vector3;
import com.gamesbykevin.havoc.animation.DecalAnimation;
import com.gamesbykevin.havoc.assets.AudioHelper.Sfx;

import static com.gamesbykevin.havoc.animation.DecalAnimation.*;
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

    //size of sprite sheet
    private static final int SPRITE_SHEET_COLS = 4;
    private static final int SPRITE_SHEET_ROWS = 3;

    public enum Type {
        boss_1, boss_2, boss_3, boss_4, boss_5, boss_6, boss_7, boss_8,
        boss_9, boss_10, boss_11, boss_12, boss_13, boss_14;

        private final int damageMax;
        private final Sfx shoot, dead, alert, hurt;
        private final String path;

        Type() {
            this.damageMax = DAMAGE_MAX_BOSS;
            this.shoot = Sfx.EnemyWeaponShoot7;
            this.dead = Sfx.EnemyDead7;
            this.alert = Sfx.EnemyAlert7;
            this.hurt = Sfx.EnemyHurt7;
            this.path = ASSET_DIR_BOSS + name() + SPRITE_SHEET;
        }

        public String getPath() {
            return this.path;
        }

        public int getDamageMax() {
            return this.damageMax;
        }

        public Sfx getShoot() {
            return this.shoot;
        }

        public Sfx getDead() {
            return this.dead;
        }

        public Sfx getAlert() {
            return this.alert;
        }

        public Sfx getHurt() {
            return this.hurt;
        }
    }

    private final Type type;

    public Boss(AssetManager assetManager, Type type) {

        //call parent
        super(ANIMATION_COUNT);

        this.type = type;

        //assign the damage
        setDamageMax(type.getDamageMax());

        //assign sound effects
        super.setShoot(type.getShoot());
        super.setDead(type.getDead());
        super.setAlert(type.getAlert());
        super.setHurt(type.getHurt());

        //setup animations
        addAnimation(assetManager, INDEX_IDLE, 0, 2, 1, 1, DURATION_IDLE);
        addAnimation(assetManager, INDEX_WALK, 3, 1, 1, 4, DURATION_WALK);
        addAnimation(assetManager, INDEX_SHOOT, 0, 1, 1, 3, DURATION_SHOOT);
        addAnimation(assetManager, INDEX_ALERT, 0, 1, 1, 3, DURATION_ALERT);
        addAnimation(assetManager, INDEX_DIE, 0, 0, 1, 4, DURATION_DIE);
        addAnimation(assetManager, INDEX_PAUSE, 0, 2, 1, 1, DURATION_PAUSE);
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