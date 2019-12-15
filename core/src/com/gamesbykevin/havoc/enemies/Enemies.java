package com.gamesbykevin.havoc.enemies;

import com.gamesbykevin.havoc.dungeon.Cell;
import com.gamesbykevin.havoc.dungeon.Leaf;
import com.gamesbykevin.havoc.dungeon.Room;
import com.gamesbykevin.havoc.entities.Entities;
import com.gamesbykevin.havoc.entities.Entity;
import com.gamesbykevin.havoc.level.Level;
import com.gamesbykevin.havoc.util.Timer;

import java.util.ArrayList;
import java.util.List;

import static com.gamesbykevin.havoc.assets.AssetManagerHelper.getTypeBoss;
import static com.gamesbykevin.havoc.assets.AssetManagerHelper.getTypeSoldier;
import static com.gamesbykevin.havoc.assets.AudioHelper.*;
import static com.gamesbykevin.havoc.dungeon.Dungeon.getRandom;
import static com.gamesbykevin.havoc.dungeon.LeafHelper.getLeafRooms;
import static com.gamesbykevin.havoc.dungeon.RoomHelper.ROOM_DIMENSION_MAX;
import static com.gamesbykevin.havoc.enemies.EnemyHelper.*;
import static com.gamesbykevin.havoc.util.Distance.getDistance;

public final class Enemies extends Entities {

    //how many enemies in each room
    public static final int ENEMIES_PER_ROOM_MAX = 3;

    //how close do we need to be to play the sound effect
    public static final float ENEMY_DISTANCE_SFX_RATIO = 0.5f;

    //different timers for playing sound effects
    private Timer timerHurt, timerAlert, timerDead, timerShoot;

    //how long until we can play the sfx again?
    public static final float DURATION_HURT = 500f;
    public static final float DURATION_ALERT = 2750f;
    public static final float DURATION_DEAD = 500f;
    public static final float DURATION_SHOOT = 400f;

    public Enemies(Level level) {
        super(level);

        //create our timers
        this.timerAlert = new Timer(DURATION_ALERT);
        this.timerHurt = new Timer(DURATION_HURT);
        this.timerDead = new Timer(DURATION_DEAD);
        this.timerShoot = new Timer(DURATION_SHOOT);
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

                //the enemy created
                Enemy enemy = null;

                //will the enemy patrol?
                boolean patrol = getRandom().nextBoolean();

                //create our enemy
                if (getRandom().nextBoolean() && limit == 1) {

                    //create the boss if by chance this room is limited to 1 enemy
                    enemy = new Boss(getLevel().getAssetManager(), getTypeBoss().get(getRandom().nextInt(getTypeBoss().size())));

                    //boss will never patrol
                    patrol = false;

                } else {

                    //create the soldier
                    enemy = new Soldier(getLevel().getAssetManager(), getTypeSoldier().get(getRandom().nextInt(getTypeSoldier().size())));
                }

                //assign location
                enemy.setCol(location.getCol());
                enemy.setRow(location.getRow());

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

                //set the direction default when we reset
                enemy.setDirectionDefault(enemy.getDirection());

                //for patrol
                Cell winner = null;

                //determine at random if the enemy will patrol the room
                if (patrol) {

                    //shortest distance
                    double distance = -1;

                    //pick random location in the room for the enemy to walk to
                    List<Cell> tmpList = getLocationOptions(leaf.getRoom(), location);

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
                }

                //add enemy at the location
                add(enemy, location.getCol(), location.getRow());

                //set the destination if we have one
                if (winner != null) {
                    enemy.setFinishCol(winner.getCol());
                    enemy.setFinishRow(winner.getRow());
                }

                //increase the count
                count++;

                //remove the option from the list
                options.remove(index);
            }

            //clear the list
            options.clear();
            options = null;
        }

        //reset the enemies
        reset();
    }

    protected List<Cell> getLocationOptions(Room room, Cell target) {

        List<Cell> options = new ArrayList<>();

        //all 4 sides
        for (int col = room.getX() + 2; col < room.getX() + room.getW() - 2; col++) {
            addOption(options, getLevel().getDungeon().getCell(col, room.getY() + 2), target);
            addOption(options, getLevel().getDungeon().getCell(col, room.getY() + room.getH() - 3), target);
        }

        for (int row = room.getY() + 2; row < room.getY() + room.getH() - 2; row++) {
            addOption(options, getLevel().getDungeon().getCell(room.getX() + 2, row), target);
            addOption(options, getLevel().getDungeon().getCell(room.getX() + room.getW() - 3, row), target);
        }

        //north south east west
        addOption(options, getLevel().getDungeon().getCell(room.getX() + 2, room.getY() + (room.getH() / 2)), target);
        addOption(options, getLevel().getDungeon().getCell(room.getX() + room.getW() - 3, room.getY() + (room.getH() / 2)), target);
        addOption(options, getLevel().getDungeon().getCell(room.getX() + (room.getW() / 2), room.getY() + 2), target);
        addOption(options, getLevel().getDungeon().getCell(room.getX() + (room.getW() / 2), room.getY() + room.getH() - 3), target);

        //4 corners inner
        for (int i = 2; i < (room.getW() / 2); i++) {
            addOption(options, getLevel().getDungeon().getCell(room.getX() + i, room.getY() + i), target);
            addOption(options, getLevel().getDungeon().getCell(room.getX() + i, room.getY() + room.getH() - i - 1), target);
            addOption(options, getLevel().getDungeon().getCell(room.getX() + room.getW() - i - 1, room.getY() + i), target);
            addOption(options, getLevel().getDungeon().getCell(room.getX() + room.getW() - i - 1, room.getY() + room.getH() - i - 1), target);
        }

        //center
        addOption(options, getLevel().getDungeon().getCell(room.getX() + (room.getW() / 2), room.getY() + (room.getH() / 2)), target);

        //return our options
        return options;
    }

    private void addOption(List<Cell> options, Cell option, Cell target) {

        if (target != null && (option.getCol() == target.getCol() && option.getRow() == target.getRow()))
            return;

        if (!hasEntityLocation(option.getCol(), option.getRow()))
            options.add(option);
    }

    public Timer getTimerHurt() {
        return this.timerHurt;
    }

    public Timer getTimerAlert() {
        return this.timerAlert;
    }

    public Timer getTimerDead() {
        return this.timerDead;
    }

    public Timer getTimerShoot() {
        return this.timerShoot;
    }

    @Override
    public void reset() {

        //call parent
        super.reset();

        //reset the enemies
        for (int i = 0; i < getEntityList().size(); i++) {

            //get the current enemy
            Enemy enemy = (Enemy)getEntityList().get(i);

            //remove the path (if exists)
            if (enemy.getPathPatrol() != null)
                enemy.getPathPatrol().clear();

            //update the patrol path only if the start and finish are different
            if (enemy.getStartCol() == enemy.getFinishCol() && enemy.getStartRow() == enemy.getFinishRow())
                continue;

            //calculate the patrol path
            calculatePath(getLevel(), enemy, enemy.getStartCol(), enemy.getStartRow(), enemy.getFinishCol(), enemy.getFinishRow());
        }

        //reset timers
        getTimerAlert().reset();
        getTimerDead().reset();
        getTimerHurt().reset();
        getTimerShoot().reset();
    }

    @Override
    public void update() {

        //update timers
        getTimerShoot().update();
        getTimerHurt().update();
        getTimerDead().update();
        getTimerAlert().update();

        //shoot sound effect
        Sfx shoot = null;
        boolean hurt = false;
        boolean dead = false;
        boolean alert = false;

        //update the enemies
        for (int i = 0; i < getEntityList().size(); i++) {

            Enemy enemy = (Enemy)getEntityList().get(i);

            boolean near = getDistance(getLevel().getPlayer(), enemy) < ROOM_DIMENSION_MAX * ENEMY_DISTANCE_SFX_RATIO;

            boolean check = false;

            //make sure we are near enough to play a sound effect
            if (near) {

                //check if any of the following happened
                if (shoot == null && getTimerShoot().isExpired() && (enemy.isShoot() || enemy.isAlert()))
                    check = true;
                if (!hurt && getTimerHurt().isExpired() && enemy.isHurt())
                    hurt = true;
                if (!dead && getTimerDead().isExpired() && enemy.getHealth() <= 0 && !enemy.isDie())
                    dead = true;
                if (!alert && getTimerAlert().isExpired() && enemy.isAlert())
                    alert = true;
            }

            //update the current entity
            enemy.update(getLevel());

            if (near) {
                if (check && shoot == null && !enemy.isShoot())
                    shoot = enemy.getShoot();
            }
        }

        if (!getLevel().getPlayer().isDead()) {

            if (hurt) {
                playHurt(getLevel().getAssetManager());
                getTimerHurt().reset();
            }

            if (alert && shoot == null) {
                playAlert(getLevel().getAssetManager());
                getTimerAlert().reset();
            }

            if (dead) {
                playDead(getLevel().getAssetManager());
                getTimerDead().reset();
            }

            if (shoot != null) {
                playSfx(getLevel().getAssetManager(), shoot);
                getTimerShoot().reset();
            }
        }
    }
}