package com.gamesbykevin.havoc.enemies;

import com.badlogic.gdx.math.Vector3;
import com.gamesbykevin.havoc.animation.DecalAnimation;

import static com.gamesbykevin.havoc.enemies.EnemyHelper.*;
import static com.gamesbykevin.havoc.enemies.EnemyHelper.DIRECTION_E;

public final class Soldier extends Enemy {

    //directory where enemies are
    public static final String DIR = "enemies/soldier/";

    //different amounts of damage for each soldier
    public static final int DAMAGE_DOCTOR = 1;
    public static final int DAMAGE_GUARD = 2;
    public static final int DAMAGE_SERGEANT = 3;
    public static final int DAMAGE_LIEUTENANT = 4;
    public static final int DAMAGE_OFFICER = 5;
    public static final int DAMAGE_MAJOR = 5;

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

    public enum Type {
        doctor_1(DIR +"doctor_1/", DAMAGE_DOCTOR),
        doctor_2(DIR + "doctor_2/", DAMAGE_DOCTOR),
        guard_1(DIR + "guard_1/", DAMAGE_GUARD),
        guard_2(DIR + "guard_2/", DAMAGE_GUARD),
        guard_3(DIR + "guard_3/", DAMAGE_GUARD),
        guard_4(DIR + "guard_4/", DAMAGE_GUARD),
        guard_5(DIR + "guard_5/", DAMAGE_GUARD),
        guard_6(DIR + "guard_6/", DAMAGE_GUARD),
        guard_7(DIR + "guard_7/", DAMAGE_GUARD),
        guard_8(DIR + "guard_8/", DAMAGE_GUARD),
        guard_9(DIR + "guard_9/", DAMAGE_GUARD),
        guard_10(DIR + "guard_10/", DAMAGE_GUARD),
        guard_11(DIR + "guard_11/", DAMAGE_GUARD),
        guard_12(DIR + "guard_12/", DAMAGE_GUARD),
        lieutenant_1(DIR + "lieutenant_1/", DAMAGE_LIEUTENANT),
        lieutenant_2(DIR + "lieutenant_2/", DAMAGE_LIEUTENANT),
        lieutenant_3(DIR + "lieutenant_3/", DAMAGE_LIEUTENANT),
        lieutenant_4(DIR + "lieutenant_4/", DAMAGE_LIEUTENANT),
        lieutenant_5(DIR + "lieutenant_5/", DAMAGE_LIEUTENANT),
        major_1(DIR + "major_1/", DAMAGE_MAJOR),
        major_2(DIR + "major_2/", DAMAGE_MAJOR),
        officer_1(DIR + "officer_1/", DAMAGE_OFFICER),
        officer_2(DIR + "officer_2/", DAMAGE_OFFICER),
        officer_3(DIR + "officer_3/", DAMAGE_OFFICER),
        officer_4(DIR + "officer_4/", DAMAGE_OFFICER),
        officer_5(DIR + "officer_5/", DAMAGE_OFFICER),
        sergeant_1(DIR + "sergeant_1/", DAMAGE_SERGEANT),
        sergeant_2(DIR + "sergeant_2/", DAMAGE_SERGEANT),
        sergeant_3(DIR + "sergeant_3/", DAMAGE_SERGEANT),
        sergeant_4(DIR + "sergeant_4/", DAMAGE_SERGEANT),
        sergeant_5(DIR + "sergeant_5/", DAMAGE_SERGEANT);

        public final String path;

        //how much damage can the enemy do?
        public final int damage;

        Type(String path, int damage) {
            this.path = path;
            this.damage = damage;
        }
    }

    public Soldier(Type type) {

        //call parent
        super(ANIMATION_COUNT);

        //set the allowed damage
        setDamage(type.damage);

        //setup animations
        getAnimations()[INDEX_DIE] = new DecalAnimation(type.path, "die", ".bmp", 1, 4, DURATION_DIE);
        getAnimations()[INDEX_PAIN] = new DecalAnimation(type.path, "pain", ".bmp", 1, 2, DURATION_PAIN);
        getAnimations()[INDEX_IDLE_S] = new DecalAnimation(type.path, "s_", ".bmp", 1, 1, DURATION_IDLE);
        getAnimations()[INDEX_IDLE_SW] = new DecalAnimation(type.path, "s_", ".bmp", 2, 1, DURATION_IDLE);
        getAnimations()[INDEX_IDLE_W] = new DecalAnimation(type.path, "s_", ".bmp", 3, 1, DURATION_IDLE);
        getAnimations()[INDEX_IDLE_NW] = new DecalAnimation(type.path, "s_", ".bmp", 4, 1, DURATION_IDLE);
        getAnimations()[INDEX_IDLE_N] = new DecalAnimation(type.path, "s_", ".bmp", 5, 1, DURATION_IDLE);
        getAnimations()[INDEX_IDLE_NE] = new DecalAnimation(type.path, "s_", ".bmp", 6, 1, DURATION_IDLE);
        getAnimations()[INDEX_IDLE_E] = new DecalAnimation(type.path, "s_", ".bmp", 7, 1, DURATION_IDLE);
        getAnimations()[INDEX_IDLE_SE] = new DecalAnimation(type.path, "s_", ".bmp", 8, 1, DURATION_IDLE);
        getAnimations()[INDEX_SHOOT] = new DecalAnimation(type.path, "shoot", ".bmp", 1, 3, DURATION_SHOOT);
        getAnimations()[INDEX_ALERT] = new DecalAnimation(type.path, "shoot", ".bmp", 2, 2, DURATION_ALERT);
        getAnimations()[INDEX_PAUSE] = new DecalAnimation(type.path, "shoot", ".bmp", 2, 1, DURATION_PAUSE);
        getAnimations()[INDEX_WALK_S] = new DecalAnimation(type.path, "w", "_1.bmp", 1, 4, DURATION_WALK);
        getAnimations()[INDEX_WALK_SW] = new DecalAnimation(type.path, "w", "_2.bmp", 1, 4, DURATION_WALK);
        getAnimations()[INDEX_WALK_W] = new DecalAnimation(type.path, "w", "_3.bmp", 1, 4, DURATION_WALK);
        getAnimations()[INDEX_WALK_NW] = new DecalAnimation(type.path, "w", "_4.bmp", 1, 4, DURATION_WALK);
        getAnimations()[INDEX_WALK_N] = new DecalAnimation(type.path, "w", "_5.bmp", 1, 4, DURATION_WALK);
        getAnimations()[INDEX_WALK_NE] = new DecalAnimation(type.path, "w", "_6.bmp", 1, 4, DURATION_WALK);
        getAnimations()[INDEX_WALK_E] = new DecalAnimation(type.path, "w", "_7.bmp", 1, 4, DURATION_WALK);
        getAnimations()[INDEX_WALK_SE] = new DecalAnimation(type.path, "w", "_8.bmp", 1, 4, DURATION_WALK);
    }

    @Override
    public void reset() {

        //call parent
        super.reset();

        //set default index
        setIndex(INDEX_IDLE_E);
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
        boolean n = ((int)position.y > (int)enemy.getAnimation().getDecal().getPosition().y);//getRow());
        boolean s = ((int)position.y < (int)enemy.getAnimation().getDecal().getPosition().y);//enemy.getRow());
        boolean w = ((int)position.x < (int)enemy.getAnimation().getDecal().getPosition().x);//enemy.getCol());
        boolean e = ((int)position.x > (int)enemy.getAnimation().getDecal().getPosition().x);//enemy.getCol());

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