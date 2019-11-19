package com.gamesbykevin.havoc.enemies;

import com.gamesbykevin.havoc.animation.DecalAnimation;
import com.gamesbykevin.havoc.astar.Node;
import com.gamesbykevin.havoc.decals.Door;
import com.gamesbykevin.havoc.dungeon.RoomHelper;
import com.gamesbykevin.havoc.entities.Entity;
import com.gamesbykevin.havoc.level.Level;

import java.util.ArrayList;
import java.util.List;

import static com.gamesbykevin.havoc.enemies.EnemyHelper.*;
import static com.gamesbykevin.havoc.entities.Entities.OFFSET;
import static com.gamesbykevin.havoc.entities.Entities.getDistance;

public final class Enemy extends Entity {

    public enum Type {
        BrownMguard("enemies/brown/mguard/", "mguard"),
        BrownMnco("enemies/brown/mNCO/", "mNCO"),
        BrownMofficer("enemies/brown/mofficer/", "mofficer"),
        BrownPguard("enemies/brown/pguard/", "pguard"),
        BrownPnco("enemies/brown/pNCO/", "pNCO"),
        BrownPofficer("enemies/brown/pofficer/", "pofficer"),
        BrownRguard("enemies/brown/rguard/", "rguard"),
        OtherMguard("enemies/other/mguard/", "mguard"),
        OtherMnco("enemies/other/mNCO/", "mNCO"),
        OtherMofficer("enemies/other/mofficer/", "mofficer"),
        OtherPguard("enemies/other/pguard/", "pguard"),
        OtherPnco("enemies/other/pNCO/", "pNCO"),
        OtherPofficer("enemies/other/pofficer/", "pofficer"),
        OtherRguard("enemies/other/rguard/", "rguard");

        private final String path, filename;

        Type(String path, String filename) {
            this.path = path;
            this.filename = filename;
        }
    }

    //what direction is the enemy facing
    private int direction;

    //possible directions to face
    public static final int DIRECTION_S = 1;
    public static final int DIRECTION_W = 2;
    public static final int DIRECTION_N = 3;
    public static final int DIRECTION_E = 4;

    //animation delay
    private static final float DURATION_IDLE = 1000f;
    private static final float DURATION_PAIN = 175f;
    private static final float DURATION_WALK = 150f;
    private static final float DURATION_DIE = 125f;
    private static final float DURATION_SHOOT = 200f;

    //how fast can the enemy move
    public static final float VELOCITY_SPEED_SLOW = .015f;
    public static final float VELOCITY_SPEED_MED  = .025f;
    public static final float VELOCITY_SPEED_FAST = .035f;

    //how fast is the enemy moving
    private float speed;

    //how close to notice the player
    private static final float RANGE_NOTICE = 7f;

    //how close to update the enemy
    private static final float RANGE_UPDATE = RoomHelper.ROOM_DIMENSION_MAX;

    //what is the health
    private float health = 100f;

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

    //how many animations are there?
    public static final int ANIMATION_COUNT = 19;

    //where does the enemy start
    private final float startCol, startRow;

    //what type of enemy is this
    public static Type TYPE;

    //is the enemy alert?
    private boolean alert = false;

    //the target location for the enemy to walk to
    private List<Node> pathTarget;

    //where are we on our path target
    private int pathIndex = 0;

    //order for our target path
    private boolean ascending = true;

    public Enemy(float startCol, float startRow) {
        super(ANIMATION_COUNT);

        //store the starting position
        this.startCol = startCol;
        this.startRow = startRow;

        //reset the enemy
        reset();
    }

    @Override
    public void createAnimations() {
        getAnimations()[INDEX_DIE] = new DecalAnimation(TYPE.path, TYPE.filename + "_die", ".bmp", 1, 4, DURATION_DIE);

        getAnimations()[INDEX_PAIN] = new DecalAnimation(TYPE.path, TYPE.filename + "_pain", ".bmp", 1, 2, DURATION_PAIN);

        getAnimations()[INDEX_IDLE_S] = new DecalAnimation(TYPE.path, TYPE.filename + "_s_", ".bmp", 1, 1, DURATION_IDLE);
        getAnimations()[INDEX_IDLE_SW] = new DecalAnimation(TYPE.path, TYPE.filename + "_s_", ".bmp", 2, 1, DURATION_IDLE);
        getAnimations()[INDEX_IDLE_W] = new DecalAnimation(TYPE.path, TYPE.filename + "_s_", ".bmp", 3, 1, DURATION_IDLE);
        getAnimations()[INDEX_IDLE_NW] = new DecalAnimation(TYPE.path, TYPE.filename + "_s_", ".bmp", 4, 1, DURATION_IDLE);
        getAnimations()[INDEX_IDLE_N] = new DecalAnimation(TYPE.path, TYPE.filename + "_s_", ".bmp", 5, 1, DURATION_IDLE);
        getAnimations()[INDEX_IDLE_NE] = new DecalAnimation(TYPE.path, TYPE.filename + "_s_", ".bmp", 6, 1, DURATION_IDLE);
        getAnimations()[INDEX_IDLE_E] = new DecalAnimation(TYPE.path, TYPE.filename + "_s_", ".bmp", 7, 1, DURATION_IDLE);
        getAnimations()[INDEX_IDLE_SE] = new DecalAnimation(TYPE.path, TYPE.filename + "_s_", ".bmp", 8, 1, DURATION_IDLE);

        getAnimations()[INDEX_SHOOT] = new DecalAnimation(TYPE.path, TYPE.filename + "_shoot", ".bmp", 1, 3, DURATION_SHOOT);

        getAnimations()[INDEX_WALK_S] = new DecalAnimation(TYPE.path, TYPE.filename + "_w", "_1.bmp", 1, 4, DURATION_WALK);
        getAnimations()[INDEX_WALK_SW] = new DecalAnimation(TYPE.path, TYPE.filename + "_w", "_2.bmp", 1, 4, DURATION_WALK);
        getAnimations()[INDEX_WALK_W] = new DecalAnimation(TYPE.path, TYPE.filename + "_w", "_3.bmp", 1, 4, DURATION_WALK);
        getAnimations()[INDEX_WALK_NW] = new DecalAnimation(TYPE.path, TYPE.filename + "_w", "_4.bmp", 1, 4, DURATION_WALK);
        getAnimations()[INDEX_WALK_N] = new DecalAnimation(TYPE.path, TYPE.filename + "_w", "_5.bmp", 1, 4, DURATION_WALK);
        getAnimations()[INDEX_WALK_NE] = new DecalAnimation(TYPE.path, TYPE.filename + "_w", "_6.bmp", 1, 4, DURATION_WALK);
        getAnimations()[INDEX_WALK_E] = new DecalAnimation(TYPE.path, TYPE.filename + "_w", "_7.bmp", 1, 4, DURATION_WALK);
        getAnimations()[INDEX_WALK_SE] = new DecalAnimation(TYPE.path, TYPE.filename + "_w", "_8.bmp", 1, 4, DURATION_WALK);
    }

    @Override
    public void reset() {
        setHealth(100f);
        setSolid(true);
        setAlert(false);
        setIndex(INDEX_WALK_E);
        setDirection(DIRECTION_E);
        setAscending(true);
        setPathIndex(0);
        setCol(getStartCol());
        setRow(getStartRow());
        setSpeed(VELOCITY_SPEED_SLOW);
    }

    @Override
    public void update(Level level) {

        if (isSolid()) {

            if (getHealth() <= 0) {

                //start death animation
                setSolid(false);
                setIndex(INDEX_DIE);

            } else {

                //calculate distance
                double dist = getDistance(this, level.getCamera3d().position);

                //if too far to update
                if (dist > RANGE_UPDATE)
                    return;

                if (isIdle(this))
                    updateIdle(level.getCamera3d().position, this);

                if (getPathTarget() != null && getPathTarget().size() > 0) {

                    //if there is a path, let the enemy patrol
                    patrol(this, level, level.getCamera3d().position);
                }

                //if the animation is finished what do we do next?
                if (getAnimation().isFinish()) {

                    if (isHurt(this) || isAttacking(this)) {
                        updateIdle(level.getCamera3d().position, this);
                    } else if (isIdle(this) || isWalking(this)) {

                        //are we close enough to check if we can attack?
                        if (dist < RANGE_NOTICE) {

                            //check to see if the enemy is facing the player
                            if (isFacing(level.getCamera3d().position, this)) {

                                //if nothing is blocking our view, then the enemy can see the player
                                if (!isObstructed(level.getDungeon(), level.getCamera3d().position, this)) {
                                    //setIndex(INDEX_SHOOT);
                                    //setAlert(true);
                                }
                            }
                        }

                        //if we are idle and we have a path, start walking again
                        if (isIdle(this) && (getPathTarget() != null && !getPathTarget().isEmpty()))
                            updateWalk(level.getCamera3d().position, this);

                        //reset the animation
                        getAnimation().reset();
                    }
                }
            }
        }

        //update the animation
        getAnimation().update();

        //update the location
        getAnimation().setPosition(getCol() + OFFSET, getRow() + OFFSET, 0);
    }

    @Override
    public void setIndex(int index) {
        super.setIndex(index);
        getAnimation().reset();
        getAnimation().setPosition(getCol() + OFFSET, getRow() + OFFSET, 0);
    }

    protected int getPathIndex() {
        return this.pathIndex;
    }

    protected void setPathIndex(int pathIndex) {
        this.pathIndex = pathIndex;
    }

    public List<Node> getPathTarget() {
        return this.pathTarget;
    }

    public void setPathTarget(List<Node> pathTarget) {
        this.pathTarget = new ArrayList<>(pathTarget);
    }

    public float getHealth() {
        return this.health;
    }

    public void setHealth(float health) {
        this.health = health;
    }

    public float getStartCol() {
        return this.startCol;
    }

    public float getStartRow() {
        return this.startRow;
    }

    protected boolean isAscending() {
        return this.ascending;
    }

    public void setAscending(boolean ascending) {
        this.ascending = ascending;
    }

    public boolean isAlert() {
        return this.alert;
    }

    public void setAlert(boolean alert) {
        this.alert = alert;
    }

    public int getDirection() {
        return this.direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public float getSpeed() {
        return this.speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }
}