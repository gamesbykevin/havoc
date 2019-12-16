package com.gamesbykevin.havoc.enemies;

import com.badlogic.gdx.math.Vector3;
import com.gamesbykevin.havoc.assets.AudioHelper;
import com.gamesbykevin.havoc.astar.Node;
import com.gamesbykevin.havoc.collectibles.Collectibles;
import com.gamesbykevin.havoc.entities.Entity3d;
import com.gamesbykevin.havoc.level.Level;

import java.util.ArrayList;
import java.util.List;

import static com.gamesbykevin.havoc.dungeon.RoomHelper.ROOM_DIMENSION_MAX;
import static com.gamesbykevin.havoc.enemies.EnemyHelper.*;
import static com.gamesbykevin.havoc.entities.Entities.OFFSET;
import static com.gamesbykevin.havoc.entities.EntityHelper.isObstructed;
import static com.gamesbykevin.havoc.player.Player.HEALTH_MAX;
import static com.gamesbykevin.havoc.util.Distance.getDistance;

public abstract class Enemy extends Entity3d {

    //current direction is the enemy facing
    private int direction;

    //direction when we reset
    private int directionDefault = DIRECTION_E;

    //how much damage can the enemy do?
    private int damageMax;

    //how close can the player get to the enemy
    private static final float DISTANCE_COLLISION = 0.75f;

    //how fast can the enemy move
    public static final float SPEED_DEFAULT = 0.01f;

    //how fast does the enemy move when chasing
    public static final float SPEED_CHASE = 0.0275f;

    //how fast can the enemy move?
    private float speed;

    //how close to notice the player
    public static final float RANGE_NOTICE = (ROOM_DIMENSION_MAX * .7f);

    //how close do we need to be for the player to update
    public static final float RANGE_UPDATE = ROOM_DIMENSION_MAX * 3;

    //how much damage to be applied based on the distance
    public static final float DAMAGE_RATIO_NEAR = 1.0f;
    public static final float DAMAGE_RATIO_CLOSE = 0.75f;
    public static final float DAMAGE_RATIO_FAR = 0.5f;
    public static final float DAMAGE_RATIO_FURTHEST = 0.25f;

    //how close to determine the damage ratio
    public static final float DAMAGE_RANGE_NEAR = RANGE_NOTICE * .5f;
    public static final float DAMAGE_RANGE_CLOSE = RANGE_NOTICE * .75f;
    public static final float DAMAGE_RANGE_FAR = RANGE_NOTICE * 1.2f;

    //what is the health
    private float health = 100f;

    //the path the enemy will patrol on
    private List<Node> pathPatrol;

    //where are we on our path target
    private int pathIndex = 0;

    //order for our target path
    private boolean ascending = true;

    //what is the enemy doing
    public enum Status {
        Die, Hurt, Shoot, Walk, Alert, Pause, Idle
    }

    //shooting sound effect
    private AudioHelper.Sfx shoot = AudioHelper.Sfx.EnemyWeaponShoot1;

    //status of the player
    private Status status;

    public Enemy(int count) {
        super(count);
    }

    @Override
    public void reset() {
        setIndex(0);
        setSpeed(SPEED_DEFAULT);
        setDirection(getDirectionDefault());
        setPathIndex(0);
        setCol(getStartCol());
        setRow(getStartRow());
        setHealth(HEALTH_MAX);
        setSolid(true);
        setAscending(true);
        setStatus(Status.Idle);
    }

    @Override
    public void update(Level level) {

        //if solid, if not the enemy is dead
        if (isSolid()) {

            //get the current player position
            Vector3 position = level.getPlayer().getCamera3d().position;

            //check if enemy died
            if (getHealth() <= 0) {

                //enemy just died, now add ammo for player to collect
                ((Collectibles)level.getCollectibles()).displayAmmo(this);

                //start death animation
                setSolid(false);
                setStatus(Status.Die);

            } else {

                //calculate distance to player from enemy
                double distance = getDistance(this, position);

                //don't continue if too far away
                if (distance > RANGE_UPDATE)
                    return;

                //if there is a path, the enemy will patrol
                if (getPathPatrol() != null && getPathPatrol().size() > 0)
                    patrol(this, level);

                //update the enemy accordingly
                EnemyHelper.update(this, level, distance);
            }

            //ensure correct index is assigned
            updateIndex(position);
        }

        //update the location for the animation
        getAnimation().setPosition(getCol() + OFFSET, getRow() + OFFSET, 0);

        //update the animation
        getAnimation().update();
    }

    public int getDirectionDefault() {
        return this.directionDefault;
    }

    public void setDirectionDefault(int directionDefault) {
        this.directionDefault = directionDefault;
    }

    public AudioHelper.Sfx getShoot() {
        return this.shoot;
    }

    public void setShoot(AudioHelper.Sfx shoot) {
        this.shoot = shoot;
    }

    //is the enemy walking, attacking, etc...
    public abstract void updateIndex(Vector3 position);

    public boolean canShoot(Level level) {
        return canShoot(level, getDistance(this, level.getPlayer().getCamera3d()));
    }

    //is the enemy able to shoot
    public boolean canShoot(Level level, double distance) {

        //if too far away, they can't shoot
        if (distance > RANGE_NOTICE)
            return false;

        //if obstructed, they can't shoot
        if (isObstructed(level, this))
            return false;

        //make sure there isn't a door here
        if (level.getDungeon().hasInteract(this))
            return false;

        //we can shoot
        return true;
    }

    @Override
    public boolean hasCollision(float x, float y) {

        //skip if not solid
        if (!isSolid())
            return false;

        //if close enough we have collision
        if (getDistance(getCol(), getRow(), x, y) <= DISTANCE_COLLISION)
            return true;

        //no collision
        return false;
    }

    @Override
    public void setIndex(int index) {
        super.setIndex(index);
        getAnimation().reset();
        getAnimation().setPosition(getCol(), getRow(), 0);
    }

    public float getSpeed() {
        return this.speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public int getPathIndex() {
        return this.pathIndex;
    }

    protected void setPathIndex(int pathIndex) {
        this.pathIndex = pathIndex;
    }

    public List<Node> getPathPatrol() {
        return this.pathPatrol;
    }

    public void setPathPatrol(List<Node> pathPatrol) {
        setPathIndex(0);
        setAscending(true);
        this.pathPatrol = new ArrayList<>(pathPatrol);
    }

    public float getHealth() {
        return this.health;
    }

    public void setHealth(float health) {
        this.health = health;
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

    public int getDamageMax() {
        return this.damageMax;
    }

    public void setDamageMax(int damageMax) {
        this.damageMax = damageMax;
    }

    public Status getStatus() {
        return this.status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public boolean isDie() {
        return getStatus() == Status.Die;
    }

    public boolean isHurt() {
        return getStatus() == Status.Hurt;
    }

    public boolean isShoot() {
        return getStatus() == Status.Shoot;
    }

    public boolean isWalk() {
        return getStatus() == Status.Walk;
    }

    public boolean isAlert() {
        return getStatus() == Status.Alert;
    }

    public boolean isPause() {
        return getStatus() == Status.Pause;
    }

    public boolean isIdle() {
        return getStatus() == Status.Idle;
    }

    protected boolean isFacing(Vector3 location) {

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
}