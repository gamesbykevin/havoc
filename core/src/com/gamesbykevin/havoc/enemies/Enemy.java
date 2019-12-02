package com.gamesbykevin.havoc.enemies;

import com.badlogic.gdx.math.Vector3;
import com.gamesbykevin.havoc.astar.Node;
import com.gamesbykevin.havoc.collectables.Collectibles;
import com.gamesbykevin.havoc.dungeon.Dungeon;
import com.gamesbykevin.havoc.entities.Entity3d;
import com.gamesbykevin.havoc.level.Level;

import java.util.ArrayList;
import java.util.List;

import static com.gamesbykevin.havoc.dungeon.RoomHelper.ROOM_DIMENSION_MAX;
import static com.gamesbykevin.havoc.enemies.EnemyHelper.*;
import static com.gamesbykevin.havoc.entities.Entities.OFFSET;
import static com.gamesbykevin.havoc.level.LevelHelper.isDoorOpen;
import static com.gamesbykevin.havoc.util.Distance.getDistance;

public abstract class Enemy extends Entity3d {

    //what direction is the enemy facing
    private int direction;

    //how much damage can the enemy do?
    private int damage;

    //how fast can the enemy move
    public static final float VELOCITY_SPEED = .01f;

    //how close to notice the player
    public static final float RANGE_NOTICE = 6f;

    //how close do we need to be for the player to update
    public static final float RANGE_UPDATE = ROOM_DIMENSION_MAX * 3;

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

    //status of the player
    private Status status;

    public Enemy(int count) {
        super(count);
    }

    @Override
    public void reset() {
        setIndex(0);
        setDirection(DIRECTION_E);
        setPathIndex(0);
        setCol(getStartCol());
        setRow(getStartRow());
        setHealth(100f);
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

                //if there is a patrol path, the enemy will patrol
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

    //is the enemy walking, attacking, etc...
    public abstract void updateIndex(Vector3 position);

    //is the enemy able to shoot
    public boolean canShoot(double distance, Level level) {
        return (distance < RANGE_NOTICE && !isObstructed(level));
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

    public int getDamage() {
        return this.damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
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

    public boolean isObstructed(Level level) {

        int x = (int)getCol();
        int y = (int)getRow();

        //get the dungeon
        Dungeon dungeon = level.getDungeon();

        //make sure there is a clear vision to the location
        if (!dungeon.hasMap(x, y))
            return true;

        //if door isn't open, we are obstructed
        if (dungeon.hasInteract(x, y) && !isDoorOpen(level, x, y))
            return true;

        Vector3 location = level.getPlayer().getCamera3d().position;

        int goalX = (int)location.x;
        int goalY = (int)location.y;

        //continue until we get to the location
        while (x != goalX || y != goalY) {

            if (x < goalX)
                x++;
            if (x > goalX)
                x--;

            //if not an open space then it is obstructed
            if (!dungeon.hasMap(x, y))
                return true;

            //if door isn't open, we are obstructed
            if (dungeon.hasInteract(x, y) && !isDoorOpen(level, x, y))
                return true;

            if (y < goalY)
                y++;
            if (y > goalY)
                y--;

            //if not an open space then it is obstructed
            if (!dungeon.hasMap(x, y))
                return true;

            //if door isn't open, we are obstructed
            if (dungeon.hasInteract(x, y) && !isDoorOpen(level, x, y))
                return true;
        }

        //nothing is blocking from viewing the player
        return false;
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