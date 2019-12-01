package com.gamesbykevin.havoc.enemies;

import com.badlogic.gdx.math.Vector3;
import com.gamesbykevin.havoc.astar.Node;
import com.gamesbykevin.havoc.decals.Door;
import com.gamesbykevin.havoc.dungeon.Dungeon;
import com.gamesbykevin.havoc.entities.Entity;
import com.gamesbykevin.havoc.level.Level;

import static com.gamesbykevin.havoc.enemies.Enemy.*;
import static com.gamesbykevin.havoc.level.LevelHelper.isDoorOpen;

public class EnemyHelper {

    public static final int DAMAGE_DOCTOR = 2;
    public static final int DAMAGE_GUARD = 3;
    public static final int DAMAGE_SERGEANT = 4;
    public static final int DAMAGE_LIEUTENANT = 5;
    public static final int DAMAGE_OFFICER = 6;
    public static final int DAMAGE_MAJOR = 7;

    public enum Type {
        doctor_1("enemies/doctor_1/", DAMAGE_DOCTOR),
        doctor_2("enemies/doctor_2/", DAMAGE_DOCTOR),
        guard_1("enemies/guard_1/", DAMAGE_GUARD),
        guard_2("enemies/guard_2/", DAMAGE_GUARD),
        guard_3("enemies/guard_3/", DAMAGE_GUARD),
        guard_4("enemies/guard_4/", DAMAGE_GUARD),
        guard_5("enemies/guard_5/", DAMAGE_GUARD),
        guard_6("enemies/guard_6/", DAMAGE_GUARD),
        guard_7("enemies/guard_7/", DAMAGE_GUARD),
        guard_8("enemies/guard_8/", DAMAGE_GUARD),
        guard_9("enemies/guard_9/", DAMAGE_GUARD),
        guard_10("enemies/guard_10/", DAMAGE_GUARD),
        lieutenant_1("enemies/lieutenant_1/", DAMAGE_LIEUTENANT),
        lieutenant_2("enemies/lieutenant_2/", DAMAGE_LIEUTENANT),
        lieutenant_3("enemies/lieutenant_3/", DAMAGE_LIEUTENANT),
        lieutenant_4("enemies/lieutenant_4/", DAMAGE_LIEUTENANT),
        lieutenant_5("enemies/lieutenant_5/", DAMAGE_LIEUTENANT),
        major_1("enemies/major_1/", DAMAGE_MAJOR),
        major_2("enemies/major_2/", DAMAGE_MAJOR),
        officer_1("enemies/officer_1/", DAMAGE_OFFICER),
        officer_2("enemies/officer_2/", DAMAGE_OFFICER),
        officer_3("enemies/officer_3/", DAMAGE_OFFICER),
        officer_4("enemies/officer_4/", DAMAGE_OFFICER),
        officer_5("enemies/officer_5/", DAMAGE_OFFICER),
        sergeant_1("enemies/sergeant_1/", DAMAGE_SERGEANT),
        sergeant_2("enemies/sergeant_2/", DAMAGE_SERGEANT),
        sergeant_3("enemies/sergeant_3/", DAMAGE_SERGEANT),
        sergeant_4("enemies/sergeant_4/", DAMAGE_SERGEANT),
        sergeant_5("enemies/sergeant_5/", DAMAGE_SERGEANT);

        public final String path;

        //how much damage can the enemy do?
        public final int damage;

        Type(String path, int damage) {
            this.path = path;
            this.damage = damage;
        }
    }

    //possible directions to face
    public static final int DIRECTION_S = 1;
    public static final int DIRECTION_W = 2;
    public static final int DIRECTION_N = 3;
    public static final int DIRECTION_E = 4;

    //animation delay
    protected static final float DURATION_IDLE = 1000f;
    protected static final float DURATION_PAIN = 200f;
    protected static final float DURATION_WALK = 300f;
    protected static final float DURATION_DIE = 200f;
    protected static final float DURATION_SHOOT = 450f;
    protected static final float DURATION_ALERT = 275f;
    protected static final float DURATION_PAUSE = 1750f;

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

    protected static void updateIdle(Vector3 position, Enemy enemy) {

        //get the appropriate animation index
        int index = getAnimationIndex(enemy, position, true);

        if (index >= 0 && index != enemy.getIndex())
            enemy.setIndex(index);
    }

    protected static void updateWalk(Vector3 position, Enemy enemy) {

        //get the appropriate animation index
        int index = getAnimationIndex(enemy, position, false);

        if (index >= 0 && index != enemy.getIndex())
            enemy.setIndex(index);
    }

    private static int getAnimationIndex(Enemy enemy, Vector3 position, boolean idle) {

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

    protected static boolean isFacing(Vector3 location, Enemy enemy) {

        switch (enemy.getDirection()) {
            case DIRECTION_E:
                return (location.x > enemy.getCol());

            case DIRECTION_W:
                return (location.x < enemy.getCol());

            case DIRECTION_N:
                return (location.y > enemy.getRow());

            case DIRECTION_S:
                return (location.y < enemy.getRow());

            default:
                return false;
        }
    }

    protected static boolean isObstructed(Level level, Vector3 location, Entity entity) {

        int x = (int)entity.getCol();
        int y = (int)entity.getRow();

        //get the dungeon
        Dungeon dungeon = level.getDungeon();

        //make sure there is a clear vision to the location
        if (!dungeon.hasMap(x, y))
            return true;

        //if door isn't open, we are obstructed
        if (dungeon.hasInteract(x, y) && !isDoorOpen(level, x, y))
            return true;

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

    public static boolean isIdle(Entity entity) {
        return isIdle(entity.getIndex());
    }

    public static boolean isIdle(int index) {

        switch (index) {
            case INDEX_IDLE_N:
            case INDEX_IDLE_S:
            case INDEX_IDLE_W:
            case INDEX_IDLE_E:
            case INDEX_IDLE_NW:
            case INDEX_IDLE_NE:
            case INDEX_IDLE_SE:
            case INDEX_IDLE_SW:
                return true;

            default:
                return false;
        }
    }

    public static boolean isHurt(Entity entity) {
        return isHurt(entity.getIndex());
    }

    public static boolean isHurt(int index) {

        switch (index) {
            case INDEX_PAIN:
                return true;

            default:
                return false;
        }
    }

    public static boolean isDead(Entity entity) {
        return isDead(entity.getIndex());
    }

    public static boolean isDead(int index) {

        switch (index) {
            case INDEX_DIE:
                return true;

            default:
                return false;
        }
    }

    public static boolean isShooting(Entity entity) {
        return isShooting(entity.getIndex());
    }

    public static boolean isShooting(int index) {

        switch (index) {
            case INDEX_SHOOT:
                return true;

            default:
                return false;
        }
    }

    public static boolean isAlert(Entity entity) {
        return isAlert(entity.getIndex());
    }

    public static boolean isAlert(int index) {

        switch (index) {
            case INDEX_ALERT:
                return true;

            default:
                return false;
        }
    }

    public static boolean isPaused(Entity entity) {
        return isPaused(entity.getIndex());
    }

    public static boolean isPaused(int index) {

        switch (index) {
            case INDEX_PAUSE:
                return true;

            default:
                return false;
        }
    }

    public static boolean isWalking(Entity entity) {
        return isWalking(entity.getIndex());
    }

    public static boolean isWalking(int index) {

        switch (index) {
            case INDEX_WALK_N:
            case INDEX_WALK_S:
            case INDEX_WALK_W:
            case INDEX_WALK_E:
            case INDEX_WALK_NW:
            case INDEX_WALK_NE:
            case INDEX_WALK_SE:
            case INDEX_WALK_SW:
                return true;

            default:
                return false;
        }
    }

    protected static void patrol(Enemy enemy, Level level, Vector3 position) {

        //current node we are targeting
        Node node = enemy.getPathPatrol().get(enemy.getPathIndex());

        //is there a door here?
        if (level.getDungeon().hasInteract(node.getCol(), node.getRow())) {

            //get the door
            Door door = level.getDoorDecal(node.getCol(), node.getRow());

            if (door != null) {

                //open the door (if possible)
                door.open();

                //enemy will remain idle until the door is open
                if (!door.isOpen()) {
                    updateIdle(position, enemy);
                } else {
                    updateWalk(position, enemy);
                }

            } else {
                updateWalk(position, enemy);
            }
        }

        //how far are we from our next node
        float colDiff = Math.abs(node.getCol() - enemy.getCol());
        float rowDiff = Math.abs(node.getRow() - enemy.getRow());

        //if we are at the target we go to the next node
        if (colDiff < VELOCITY_SPEED && rowDiff < VELOCITY_SPEED) {

            //change the index
            enemy.setPathIndex(enemy.isAscending() ? enemy.getPathIndex() + 1 : enemy.getPathIndex() - 1);

            //make sure we stay in bounds
            if (enemy.getPathIndex() < 0) {
                enemy.setPathIndex(1);
                enemy.setAscending(!enemy.isAscending());

                //we reached the end, so keep the enemy idle for a short time
                updateIdle(position, enemy);
            }

            if (enemy.getPathIndex() >= enemy.getPathPatrol().size()) {
                enemy.setPathIndex(enemy.getPathPatrol().size() - 1);
                enemy.setAscending(!enemy.isAscending());

                //we reached the end, so keep the enemy idle for a short time
                updateIdle(position, enemy);
            }

            //get the new node
            node = enemy.getPathPatrol().get(enemy.getPathIndex());
        }

        if (isWalking(enemy)) {

            //determine which direction to face
            if (enemy.getCol() < node.getCol() && colDiff > VELOCITY_SPEED) {
                enemy.setDirection(DIRECTION_E);
            } else if (enemy.getCol() > node.getCol() && colDiff > VELOCITY_SPEED) {
                enemy.setDirection(DIRECTION_W);
            } else if (enemy.getRow() < node.getRow() && rowDiff > VELOCITY_SPEED) {
                enemy.setDirection(DIRECTION_N);
            } else if (enemy.getRow() > node.getRow() && rowDiff > VELOCITY_SPEED) {
                enemy.setDirection(DIRECTION_S);
            }

            //move in the direction we are facing
            switch (enemy.getDirection()) {

                case DIRECTION_E:
                    enemy.setCol(enemy.getCol() + VELOCITY_SPEED);
                    break;

                case DIRECTION_N:
                    enemy.setRow(enemy.getRow() + VELOCITY_SPEED);
                    break;

                case DIRECTION_S:
                    enemy.setRow(enemy.getRow() - VELOCITY_SPEED);
                    break;

                case DIRECTION_W:
                    enemy.setCol(enemy.getCol() - VELOCITY_SPEED);
                    break;
            }

            //update walking animation
            updateWalk(position, enemy);
        }
    }
}