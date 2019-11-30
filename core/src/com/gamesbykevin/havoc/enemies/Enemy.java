package com.gamesbykevin.havoc.enemies;

import com.badlogic.gdx.math.Vector3;
import com.gamesbykevin.havoc.animation.DecalAnimation;
import com.gamesbykevin.havoc.astar.Node;
import com.gamesbykevin.havoc.collectables.Collectibles;
import com.gamesbykevin.havoc.entities.Entity3d;
import com.gamesbykevin.havoc.level.Level;

import java.util.ArrayList;
import java.util.List;

import static com.gamesbykevin.havoc.dungeon.RoomHelper.ROOM_DIMENSION_MAX;
import static com.gamesbykevin.havoc.enemies.EnemyHelper.*;
import static com.gamesbykevin.havoc.entities.Entities.OFFSET;
import static com.gamesbykevin.havoc.util.Distance.getDistance;

public final class Enemy extends Entity3d {

    //what direction is the enemy facing
    private int direction;

    //how much damage can the enemy do?
    private int damage;

    //how fast can the enemy move
    public static final float VELOCITY_SPEED = .0075f;

    //how close to notice the player
    public static final float RANGE_NOTICE = 6f;

    //how close do we need to be for the player to update
    public static final float RANGE_UPDATE = ROOM_DIMENSION_MAX * 3;

    //what is the health
    private float health = 100f;

    //how many animations are there?
    public static final int ANIMATION_COUNT = 20;

    //where does the enemy start
    private final float startCol, startRow;

    //the path the enemy will patrol on
    private List<Node> pathPatrol;

    //path to the player
    private List<Node> pathPlayer;

    //where are we on our path target
    private int pathIndex = 0;

    //order for our target path
    private boolean ascending = true;

    public Enemy(Type type, float startCol, float startRow) {
        super(ANIMATION_COUNT);

        //store the starting position
        this.startCol = startCol;
        this.startRow = startRow;

        //set the allowed damage
        setDamage(type.damage);

        getAnimations()[INDEX_DIE] = new DecalAnimation(type.path, type.filename + "_die", ".bmp", 1, 4, DURATION_DIE);

        getAnimations()[INDEX_PAIN] = new DecalAnimation(type.path, type.filename + "_pain", ".bmp", 1, 2, DURATION_PAIN);

        getAnimations()[INDEX_IDLE_S] = new DecalAnimation(type.path, type.filename + "_s_", ".bmp", 1, 1, DURATION_IDLE);
        getAnimations()[INDEX_IDLE_SW] = new DecalAnimation(type.path, type.filename + "_s_", ".bmp", 2, 1, DURATION_IDLE);
        getAnimations()[INDEX_IDLE_W] = new DecalAnimation(type.path, type.filename + "_s_", ".bmp", 3, 1, DURATION_IDLE);
        getAnimations()[INDEX_IDLE_NW] = new DecalAnimation(type.path, type.filename + "_s_", ".bmp", 4, 1, DURATION_IDLE);
        getAnimations()[INDEX_IDLE_N] = new DecalAnimation(type.path, type.filename + "_s_", ".bmp", 5, 1, DURATION_IDLE);
        getAnimations()[INDEX_IDLE_NE] = new DecalAnimation(type.path, type.filename + "_s_", ".bmp", 6, 1, DURATION_IDLE);
        getAnimations()[INDEX_IDLE_E] = new DecalAnimation(type.path, type.filename + "_s_", ".bmp", 7, 1, DURATION_IDLE);
        getAnimations()[INDEX_IDLE_SE] = new DecalAnimation(type.path, type.filename + "_s_", ".bmp", 8, 1, DURATION_IDLE);

        getAnimations()[INDEX_SHOOT] = new DecalAnimation(type.path, type.filename + "_shoot", ".bmp", 1, 3, DURATION_SHOOT);
        getAnimations()[INDEX_ALERT] = new DecalAnimation(type.path, type.filename + "_shoot", ".bmp", 2, 2, DURATION_ALERT);

        getAnimations()[INDEX_WALK_S] = new DecalAnimation(type.path, type.filename + "_w", "_1.bmp", 1, 4, DURATION_WALK);
        getAnimations()[INDEX_WALK_SW] = new DecalAnimation(type.path, type.filename + "_w", "_2.bmp", 1, 4, DURATION_WALK);
        getAnimations()[INDEX_WALK_W] = new DecalAnimation(type.path, type.filename + "_w", "_3.bmp", 1, 4, DURATION_WALK);
        getAnimations()[INDEX_WALK_NW] = new DecalAnimation(type.path, type.filename + "_w", "_4.bmp", 1, 4, DURATION_WALK);
        getAnimations()[INDEX_WALK_N] = new DecalAnimation(type.path, type.filename + "_w", "_5.bmp", 1, 4, DURATION_WALK);
        getAnimations()[INDEX_WALK_NE] = new DecalAnimation(type.path, type.filename + "_w", "_6.bmp", 1, 4, DURATION_WALK);
        getAnimations()[INDEX_WALK_E] = new DecalAnimation(type.path, type.filename + "_w", "_7.bmp", 1, 4, DURATION_WALK);
        getAnimations()[INDEX_WALK_SE] = new DecalAnimation(type.path, type.filename + "_w", "_8.bmp", 1, 4, DURATION_WALK);

        //reset the enemy
        reset();
    }

    @Override
    public void reset() {
        setHealth(100f);
        setSolid(true);
        setIndex(INDEX_IDLE_E);
        setDirection(DIRECTION_E);
        setAscending(true);
        setPathIndex(0);
        setCol(getStartCol());
        setRow(getStartRow());
    }

    @Override
    public void update(Level level) {

        if (isSolid()) {

            //if health is gone start death animation
            if (getHealth() <= 0) {

                //enemy just died, now add ammo for player to collect
                ((Collectibles)level.getCollectibles()).displayAmmo(this);

                //start death animation
                setSolid(false);
                setIndex(INDEX_DIE);

            } else {

                //get the current location
                Vector3 position = level.getPlayer().getCamera3d().position;

                //calculate distance to player from enemy
                double dist = getDistance(this, position);

                //don't continue if too far away
                if (dist > RANGE_UPDATE)
                    return;

                if (dist < RANGE_NOTICE) {

                    //if the enemy isn't hurt
                    if (!isHurt(this)) {

                        //if we are close and the player is shooting and nothing is in our way
                        if (level.getPlayer().getController().isShooting() && !isObstructed(level, position, this))
                            setIndex(INDEX_ALERT);
                    }
                }

                //update these animations because they can change when the player moves
                if (isIdle(this))
                    updateIdle(position, this);
                if (isWalking(this))
                    updateWalk(position, this);

                if (getPathPatrol() != null && getPathPatrol().size() > 0)
                    patrol(this, level, position);

                //if the animation is finished what do we do next?
                if (getAnimation().isFinish()) {

                    if (isHurt(this)) {

                        //are we close enough to check if we can attack?
                        if (!isObstructed(level, position, this)) {
                            setIndex(INDEX_ALERT);
                        } else {
                            updateIdle(position, this);
                        }

                    } else if (isShooting(this)) {

                        //if the enemy finished shooting flag the player as hurt
                        level.getPlayer().setHurt(true);
                        level.getPlayer().setHealth(level.getPlayer().getHealth() - getDamage());

                        //if close enough the enemy will remain alert
                        if (dist < RANGE_NOTICE && !isObstructed(level, position, this)) {
                            setIndex(INDEX_ALERT);
                        } else {
                            updateIdle(position, this);
                        }

                    } else if (isAlert(this)) {

                        //if the enemy finished shooting flag the player as hurt
                        level.getPlayer().setHurt(true);
                        level.getPlayer().setHealth(level.getPlayer().getHealth() - getDamage());

                        //are we close enough to check if we can attack?
                        if (dist < RANGE_NOTICE && !isObstructed(level, position, this)) {
                            setIndex(INDEX_ALERT);
                        } else {
                            updateIdle(position, this);
                        }

                    } else if (isIdle(this) || isWalking(this)) {

                        //are we close enough to check if we can attack?
                        if (dist < RANGE_NOTICE) {

                            //check to see if the enemy is facing the player
                            if (isFacing(position, this)) {

                                //if nothing is blocking our view, then the enemy can see the player
                                if (!isObstructed(level, position, this))
                                    setIndex(INDEX_SHOOT);
                            }

                        } else {

                            //if not in range we go back to idle
                            updateIdle(position, this);
                        }

                        //if we are idle and we have a path, start walking again
                        if (isIdle(this) && (getPathPatrol() != null && !getPathPatrol().isEmpty()))
                            updateWalk(position, this);

                        //reset the animation
                        getAnimation().reset();
                    }
                }
            }
        }

        //update the location for the animation
        getAnimation().setPosition(getCol() + OFFSET, getRow() + OFFSET, 0);

        //update the animation
        getAnimation().update();
    }

    @Override
    public void setIndex(int index) {
        super.setIndex(index);
        getAnimation().reset();
        getAnimation().setPosition(getCol(), getRow(), 0);
    }

    protected int getPathIndex() {
        return this.pathIndex;
    }

    protected void setPathIndex(int pathIndex) {
        this.pathIndex = pathIndex;
    }

    public List<Node> getPathPatrol() {
        return this.pathPatrol;
    }

    public void setPathPatrol(List<Node> pathPatrol) {
        this.pathPatrol = new ArrayList<>(pathPatrol);
    }

    public List<Node> getPathPlayer() {
        return this.pathPlayer;
    }

    public void setPathPlayer(List<Node> pathPlayer) {
        this.pathPlayer = new ArrayList<>(pathPlayer);
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

    public int getDirection() {
        return this.direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public int getDamage() {
        return this.damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }
}