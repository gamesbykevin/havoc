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

            options = getLocationOptions(leaf.getRoom(), null);

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

            //populate our list of options
            options = getLocationOptions(leaf.getRoom(), null);

            //pick random index
            int index = getRandom().nextInt(options.size());

            int middleCol = leaf.getRoom().getX() + (leaf.getRoom().getW() / 2);
            int middleRow = leaf.getRoom().getY() + (leaf.getRoom().getH() / 2);

            //spawn enemy here
            spawnEnemy(leaf, options.get(index), middleCol, middleRow, true);
        }

        options.clear();
        options = null;

        //reset the enemies
        reset();
    }

    private void spawnEnemy(Leaf leaf, Cell location, int middleCol, int middleRow, boolean boss) {

        //the enemy created
        Enemy enemy = null;

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

                //calculate path to chase the player
                chase(getLevel(), enemy);

                //turn flag off
                enemy.setChase(false);

                //flag we chased so we don't do it for the other enemies
                chase = true;

                //reset timer
                getTimerChase().reset();
            }

            //update the current entity
            enemy.update(getLevel());

            //if close enough to hear the gun shoot
            if (near) {

                if (check && shoot == null && !enemy.isShoot())
                    shoot = enemy.getShoot();
            }

            //if an enemy is shooting we need to check other enemies nearby and notify them
            if (check)
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

        //update the entities accordingly
        update();

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