package com.gamesbykevin.havoc.enemies;

import com.gamesbykevin.havoc.dungeon.Cell;
import com.gamesbykevin.havoc.dungeon.Leaf;
import com.gamesbykevin.havoc.dungeon.Room;
import com.gamesbykevin.havoc.entities.Entities;
import com.gamesbykevin.havoc.entities.Entity;
import com.gamesbykevin.havoc.level.Level;

import java.util.ArrayList;
import java.util.List;

import static com.gamesbykevin.havoc.dungeon.Dungeon.getRandom;
import static com.gamesbykevin.havoc.dungeon.LeafHelper.getLeafRooms;
import static com.gamesbykevin.havoc.enemies.Enemy.RANGE_NOTICE;
import static com.gamesbykevin.havoc.enemies.EnemyHelper.*;
import static com.gamesbykevin.havoc.util.Distance.getDistance;

public final class Enemies extends Entities {

    //how many enemies in each room
    public static final int ENEMIES_PER_ROOM_MAX = 3;

    public Enemies(Level level) {
        super(level);
    }

    public boolean hasCollision(float x, float y) {

        for (int i = 0; i < getEntityList().size(); i++) {

            Entity entity = getEntityList().get(i);

            //skip if dead
            if (!entity.isSolid())
                continue;

            //if we have collision return true
            if (entity.hasCollision(x, y))
                return true;
        }

        //no collision
        return false;
    }

    //add enemies
    @Override
    public void spawn() {

        //list of valid leaves for spawning
        List<Leaf> leaves = getLeafRooms(getLevel().getDungeon());

        //make sure map is updated and we won't allow diagonal movement
        getLevel().getDungeon().getAStar().setDiagonal(false);

        while (!leaves.isEmpty()) {

            int randomIndex = getRandom().nextInt(leaves.size());

            //get random leaf
            Leaf leaf = leaves.get(randomIndex);

            //remove from the list
            leaves.remove(randomIndex);

            List<Cell> options = getLocationOptions(leaf.getRoom(), null);

            int middleCol = leaf.getRoom().getX() + (leaf.getRoom().getW() / 2);
            int middleRow = leaf.getRoom().getY() + (leaf.getRoom().getH() / 2);

            int count = 0;

            //pick random number of enemies
            int limit = getRandom().nextInt(ENEMIES_PER_ROOM_MAX) + 1;

            //how many enemies per room
            while (!options.isEmpty() && count < limit) {

                //pick random index
                int index = getRandom().nextInt(options.size());

                //get the random location
                Cell location = options.get(index);

                //create our enemy
                Enemy enemy = new Enemy(Type.values()[getRandom().nextInt(Type.values().length)], location.getCol(), location.getRow());

                //start the enemy out as idle
                enemy.setIndex(INDEX_IDLE_E);

                //change the facing direction
                if (location.getCol() < middleCol) {
                    enemy.setDirection(DIRECTION_E);
                } else if (location.getCol() > middleCol) {
                    enemy.setDirection(DIRECTION_W);
                } else {
                    if (location.getRow() < middleRow) {
                        enemy.setDirection(DIRECTION_N);
                    } else {
                        enemy.setDirection(DIRECTION_S);
                    }
                }

                //determine at random if the enemy will patrol the room
                if (getRandom().nextBoolean()) {

                    //shortest distance
                    double distance = -1;

                    //pick random location in the room for the enemy to walk to
                    List<Cell> tmpList = getLocationOptions(leaf.getRoom(), location);

                    Cell winner = null;

                    //pick the location furthest away so the enemy can patrol the room
                    for (int i = 0; i < tmpList.size(); i++) {

                        Cell tmp = tmpList.get(i);

                        //calculate distance from enemy spawn point
                        double dist = getDistance(location, tmp);

                        //if the distance is longer this is the distance to beat
                        if (distance < 0 || dist > distance) {
                            distance = dist;
                            winner = tmp;
                        }
                    }

                    tmpList.clear();
                    tmpList = null;

                    //set the path to the target
                    getLevel().getDungeon().getAStar().calculate(location.getCol(), location.getRow(), winner.getCol(), winner.getRow());
                    enemy.setPathPatrol(getLevel().getDungeon().getAStar().getPath());
                }

                //add enemy at the location
                add(enemy, enemy.getStartCol(), enemy.getStartRow());

                //increase the count
                count++;

                //remove the option from the list
                options.remove(index);
            }

            //clear the list
            options.clear();
            options = null;
        }

        //update the map
        getLevel().getDungeon().updateMap();
    }

    protected List<Cell> getLocationOptions(Room room, Cell target) {

        List<Cell> options = new ArrayList<>();

        //all 4 sides
        for (int col = room.getX(); col < room.getX() + room.getW(); col++) {
            add(options, getLevel().getDungeon().getCells()[room.getY() + 2][col], target);
            add(options, getLevel().getDungeon().getCells()[room.getY() + room.getH() - 3][col], target);
        }

        for (int row = room.getY(); row < room.getY() + room.getH(); row++) {
            add(options, getLevel().getDungeon().getCells()[row][room.getX() + 2], target);
            add(options, getLevel().getDungeon().getCells()[row][room.getX() + room.getW() - 3], target);
        }

        //north south east west
        add(options, getLevel().getDungeon().getCells()[room.getY() + (room.getH() / 2)][room.getX() + 2], target);
        add(options, getLevel().getDungeon().getCells()[room.getY() + (room.getH() / 2)][room.getX() + room.getW() - 3], target);
        add(options, getLevel().getDungeon().getCells()[room.getY() + 2][room.getX() + (room.getW() / 2)], target);
        add(options, getLevel().getDungeon().getCells()[room.getY() + room.getH() - 3][room.getX() + (room.getW() / 2)], target);

        //4 corners inner
        for (int i = 2; i < (room.getW() / 2); i++) {
            add(options, getLevel().getDungeon().getCells()[room.getY() + i][room.getX() + i], target);
            add(options, getLevel().getDungeon().getCells()[room.getY() + room.getH() - i - 1][room.getX() + i], target);
            add(options, getLevel().getDungeon().getCells()[room.getY() + i][room.getX() + room.getW() - i - 1], target);
            add(options, getLevel().getDungeon().getCells()[room.getY() + room.getH() - i - 1][room.getX() + room.getW() - i - 1], target);
        }

        //center
        add(options, getLevel().getDungeon().getCells()[room.getY() + (room.getH() / 2)][room.getX() + (room.getW() / 2)], target);

        //return our options
        return options;
    }

    private void add(List<Cell> options, Cell option, Cell target) {

        if (target != null && (option.getCol() == target.getCol() && option.getRow() == target.getRow()))
            return;

        if (!hasEntityLocation(option.getCol(), option.getRow()))
            options.add(option);
    }

    @Override
    public void update() {

        //update the enemies
        for (int i = 0; i < getEntityList().size(); i++) {

            //get the current entity
            Entity enemy = getEntityList().get(i);

            //update the entity
            enemy.update(getLevel());

            //if alert or attacking or hurt, notify nearby enemies
            if (isShooting(enemy) || isAlert(enemy) || isHurt(enemy) || isPaused(enemy))
                notifyNeighbors(enemy, i);
        }
    }

    private void notifyNeighbors(Entity enemy, int indexIgnore) {

        //update the enemies
        for (int i = 0; i < getEntityList().size(); i++) {

            //ignore self
            if (indexIgnore == i)
                continue;

            Entity entity = getEntityList().get(i);

            //must be idle or walking to notify
            if (!isIdle(entity) && !isWalking(entity))
                continue;

            //if enemy is too far away we will skip
            if (getDistance(enemy, entity) > RANGE_NOTICE)
                continue;

            if (!isObstructed(getLevel(), getLevel().getPlayer().getCamera3d().position, entity)) {

                //if the enemy isn't obstructed we can begin shooting
                entity.setIndex(INDEX_SHOOT);

            } else {

                //if enemy is obstructed and in the same room, let's move so we can see the player and start shooting

            }
        }
    }
}