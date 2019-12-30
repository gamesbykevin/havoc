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
import static com.gamesbykevin.havoc.dungeon.LeafHelper.getLeafGoal;
import static com.gamesbykevin.havoc.dungeon.LeafHelper.getLeafRooms;
import static com.gamesbykevin.havoc.dungeon.RoomHelper.ROOM_DIMENSION_MAX;
import static com.gamesbykevin.havoc.enemies.EnemyHelper.*;
import static com.gamesbykevin.havoc.level.Level.RENDER_RANGE;
import static com.gamesbykevin.havoc.util.Distance.getDistance;

public final class Enemies extends Entities {

    //how many enemies in each room
    public static final int ENEMIES_PER_ROOM_MAX = 3;
    public static final int ENEMIES_PER_ROOM_MIN = 1;

    //how close do we need to be to play the sound effect
    public static final float ENEMY_DISTANCE_SFX_RATIO = 1.25f;

    //different timers
    private Timer timerHurt, timerAlert, timerDead, timerShoot, timerChase;

    //how long until update again
    public static final float DURATION_HURT = 750f;
    public static final float DURATION_ALERT = 2750f;
    public static final float DURATION_DEAD = 500f;
    public static final float DURATION_SHOOT = 400f;
    public static final float DURATION_CHASE = 850f;

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

        //optional locations to place the enemies
        List<Cell> options = null;

        while (!leaves.isEmpty()) {

            int randomIndex = getRandom().nextInt(leaves.size());

            //get random leaf
            Leaf leaf = leaves.get(randomIndex);

            //remove from the list
            leaves.remove(randomIndex);

            //get a list of options to place the enemy
            options = getLocationOptions(leaf.getRoom(), -1, -1);

            int middleCol = leaf.getRoom().getX() + (leaf.getRoom().getW() / 2);
            int middleRow = leaf.getRoom().getY() + (leaf.getRoom().getH() / 2);

            int count = 0;

            //pick random number of enemies
            int limit = getRandom().nextInt(ENEMIES_PER_ROOM_MAX - ENEMIES_PER_ROOM_MIN) + ENEMIES_PER_ROOM_MIN;

            //how many enemies per room
            while (!options.isEmpty() && count < limit) {

                //pick random index
                int index = getRandom().nextInt(options.size());

                //get the random location
                Cell location = options.get(index);

                //spawn enemy here
                spawnEnemy(leaf, location, middleCol, middleRow, false);

                //increase the count
                count++;

                //remove the option from the list
                options.remove(index);
            }
        }

        //decide at random if we are to add a boss to the goal room
        if (getRandom().nextBoolean()) {

            //get the leaf for the goal room
            Leaf leaf = getLeafGoal(getLevel().getDungeon());

            //center of the room
            int middleCol = leaf.getRoom().getX() + (leaf.getRoom().getW() / 2);
            int middleRow = leaf.getRoom().getY() + (leaf.getRoom().getH() / 2);

            //did we succeed placing around the goal
            boolean success = false;

            //check to see if we can spawn around the goal
            for (int row = -1; row <= 1; row++) {
                for (int col = -1; col <= 1; col++) {

                    if (success)
                        break;

                    if (row == 0 && col == 0)
                        continue;

                    int spawnCol = getLevel().getDungeon().getGoalCol() + col;
                    int spawnRow = getLevel().getDungeon().getGoalRow() + row;

                    //if available we will spawn here
                    if (!hasEntityLocation(spawnCol, spawnRow)) {
                        spawnEnemy(leaf, spawnCol, spawnRow, middleCol, middleRow, true);
                        success = true;
                    }
                }
            }

            //if we couldn't find a good spawn location, spawn anywhere else in the room
            if (!success) {

                //populate our list of options
                options = getLocationOptions(leaf.getRoom(), null);

                //pick random index
                int index = getRandom().nextInt(options.size());

                //spawn enemy here
                spawnEnemy(leaf, options.get(index), middleCol, middleRow, true);
            }
        }

        options.clear();
        options = null;

        //reset the enemies
        reset();
    }

    private void spawnEnemy(Leaf leaf, Cell location, int middleCol, int middleRow, boolean boss) {
        spawnEnemy(leaf, location.getCol(), location.getRow(), middleCol, middleRow, boss);
    }

    private void spawnEnemy(Leaf leaf, float col, float row, int middleCol, int middleRow, boolean boss) {

        //the enemy created
        Enemy enemy;

        //will the enemy patrol?
        boolean patrol = getRandom().nextBoolean();

        //create our enemy
        if (boss) {

            //create the boss if by chance this room is limited to 1 enemy
            enemy = new Boss(getLevel().getAssetManager(), getTypeBoss().get(getRandom().nextInt(getTypeBoss().size())));

            //boss will never patrol
            patrol = false;

        } else {

            //create the soldier
            enemy = new Soldier(getLevel().getAssetManager(), getTypeSoldier().get(getRandom().nextInt(getTypeSoldier().size())));
        }

        //assign location
        enemy.setCol(col);
        enemy.setRow(row);

        //change the facing direction
        if (col < middleCol) {
            enemy.setDirection(DIRECTION_E);
        } else if (col > middleCol) {
            enemy.setDirection(DIRECTION_W);
        } else {
            if (row < middleRow) {
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
            List<Cell> tmpList = getLocationOptions(leaf.getRoom(), col, row);

            //pick the location furthest away so the enemy can patrol the room
            for (int i = 0; i < tmpList.size(); i++) {

                Cell tmp = tmpList.get(i);

                //calculate distance from enemy spawn point
                double dist = getDistance(col, row, tmp);

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
        add(enemy, col, row);

        //set the destination if we have one
        if (winner != null) {
            enemy.setFinishCol(winner.getCol());
            enemy.setFinishRow(winner.getRow());
        }
    }

    protected List<Cell> getLocationOptions(Room room, Cell target) {
        return getLocationOptions(room, target.getCol(), target.getRow());
    }

    protected List<Cell> getLocationOptions(Room room, float targetCol, float targetRow) {

        List<Cell> options = new ArrayList<>();

        int centerX = room.getX() + (room.getW() / 2);
        int centerY = room.getY() + (room.getH() / 2);

        //north south east west
        addOption(options, getLevel().getDungeon().getCell(room.getX() + 2, centerY), targetCol, targetRow);
        addOption(options, getLevel().getDungeon().getCell(room.getX() + room.getW() - 3, centerY), targetCol, targetRow);
        addOption(options, getLevel().getDungeon().getCell(centerX, room.getY() + 2), targetCol, targetRow);
        addOption(options, getLevel().getDungeon().getCell(centerX, room.getY() + room.getH() - 3), targetCol, targetRow);

        //center or close to it
        for (int row = -2; row <= 2; row++) {
            for (int col = -2; col <= 2; col++) {
                addOption(options, getLevel().getDungeon().getCell(centerX + col, centerY + row), targetCol, targetRow);
            }
        }

        //if no options add on all sides
        if (options.isEmpty()) {

            for (int col = room.getX() + 2; col < room.getX() + room.getW() - 2; col++) {
                addOption(options, getLevel().getDungeon().getCell(col, room.getY() + 2), targetCol, targetRow);
                addOption(options, getLevel().getDungeon().getCell(col, room.getY() + room.getH() - 3), targetCol, targetRow);
            }

            for (int row = room.getY() + 2; row < room.getY() + room.getH() - 2; row++) {
                addOption(options, getLevel().getDungeon().getCell(room.getX() + 2, row), targetCol, targetRow);
                addOption(options, getLevel().getDungeon().getCell(room.getX() + room.getW() - 3, row), targetCol, targetRow);
            }
        }

        //return our options
        return options;
    }

    private void addOption(List<Cell> options, Cell option, float col, float row) {

        if (option.getCol() == col && option.getRow() == row)
            return;

        if (!hasEntityLocation(option.getCol(), option.getRow()))
            options.add(option);
    }

    public Timer getTimerHurt() {

        if (this.timerHurt == null)
            this.timerHurt = new Timer(DURATION_HURT);

        return this.timerHurt;
    }

    public Timer getTimerAlert() {

        if (this.timerAlert == null)
            this.timerAlert = new Timer(DURATION_ALERT);

        return this.timerAlert;
    }

    public Timer getTimerDead() {

        if (this.timerDead == null)
            this.timerDead = new Timer(DURATION_DEAD);

        return this.timerDead;
    }

    public Timer getTimerShoot() {

        if (this.timerShoot == null)
            this.timerShoot = new Timer(DURATION_SHOOT);

        return this.timerShoot;
    }

    public Timer getTimerChase() {

        if (this.timerChase == null)
            this.timerChase = new Timer(DURATION_CHASE);

        return this.timerChase;
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
        getTimerChase().reset();
    }

    private void updateTimers() {
        getTimerShoot().update();
        getTimerHurt().update();
        getTimerDead().update();
        getTimerAlert().update();
        getTimerChase().update();
    }

    @Override
    public void update() {

        if (getLevel().getPlayer().isGoal())
            return;

        //update timers
        updateTimers();

        //shoot sound effect
        Sfx shoot = null, dead = null, alert = null, hurt = null;

        //did we calculate chase yet?
        boolean chase = false;

        //sort the enemies so the closest one to the player is first
        sortEnemies(getEntityList(), getLevel().getPlayer());

        //update the enemies
        for (int i = 0; i < getEntityList().size(); i++) {

            Enemy enemy = (Enemy)getEntityList().get(i);

            //are the enemies close to the player in order for the player to hear the sound effects?
            boolean near = (getDistance(getLevel().getPlayer(), enemy) < ROOM_DIMENSION_MAX * ENEMY_DISTANCE_SFX_RATIO);

            //we need to play shoot sfx at the right time when the animation finishes
            boolean check = false;

            //make sure we are near enough to play a sound effect
            if (near) {

                //check if any of the following happened
                if (shoot == null && getTimerShoot().isExpired() && (enemy.isShoot() || enemy.isAlert()))
                    check = true;
                if (hurt == null && getTimerHurt().isExpired() && enemy.isHurt())
                    hurt = enemy.getHurt();
                if (dead == null && getTimerDead().isExpired() && enemy.getHealth() <= 0 && !enemy.isDie())
                    dead = enemy.getDead();
                if (alert == null && getTimerAlert().isExpired() && enemy.isAlert())
                    alert = enemy.getAlert();
            }

            //if we haven't chased anyone yet and the enemy is flagged to chase
            if (!chase && getTimerChase().isExpired() && enemy.isChase() && !enemy.isDie()) {

                //let's also make sure we reached our current node location before calculating another path
                if (enemy.getCol() == (int)enemy.getCol() && enemy.getRow() == (int)enemy.getRow()) {

                    //calculate path to chase the player
                    chase(getLevel(), enemy);

                    //turn flag off
                    enemy.setChase(false);

                    //flag we chased so we don't do it for the other enemies
                    chase = true;

                    //reset timer
                    getTimerChase().reset();
                }
            }

            //update the current entity
            enemy.update(getLevel());

            //if close enough to hear the gun shoot
            if (near) {

                if (check && shoot == null && !enemy.isShoot())
                    shoot = enemy.getShoot();
            }

            //if the enemy is near and shooting, need to notify enemies nearby
            if (near && enemy.isShoot())
                notifyNeighbors(getLevel(), getEntityList(), i);
        }

        //play the appropriate sound effects
        if (!getLevel().getPlayer().isDead()) {

            if (hurt != null) {
                playSfx(getLevel().getAssetManager(), hurt);
                getTimerHurt().reset();
            }

            if (alert != null && shoot == null) {
                playSfx(getLevel().getAssetManager(), alert);
                getTimerAlert().reset();
            }

            if (dead != null) {
                playSfx(getLevel().getAssetManager(), dead);
                getTimerDead().reset();
            }

            if (shoot != null) {
                playSfx(getLevel().getAssetManager(), shoot);
                getTimerShoot().reset();
            }
        }
    }

    @Override
    public void dispose() {
        super.dispose();
        this.timerAlert = null;
        this.timerDead = null;
        this.timerHurt = null;
        this.timerShoot = null;
    }

    @Override
    public int render() {

        int count = 0;

        for (int i = 0; i < getEntityList().size(); i++) {

            //get the current entity
            Entity entity = getEntityList().get(i);

            //how far away from the player
            double distance = getDistance(entity, getLevel().getPlayer());

            //don't bother drawing if dead and too close
            if (!entity.isSolid() && distance < DISTANCE_RENDER_IGNORE)
                continue;

            //don't render if too far away
            if (distance > RENDER_RANGE)
                continue;

            //render the entity
            entity.render(
                    getLevel().getAssetManager(),
                    getLevel().getPlayer().getCamera3d(),
                    getLevel().getDecalBatch(),
                    getLevel().getPlayer().getController().getStage().getBatch()
            );

            //keep track of how many items we rendered
            count++;
        }

        //return the count
        return count;
    }
}