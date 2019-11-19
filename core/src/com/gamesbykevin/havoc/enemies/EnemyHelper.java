package com.gamesbykevin.havoc.enemies;

import com.badlogic.gdx.math.Vector3;
import com.gamesbykevin.havoc.astar.Node;
import com.gamesbykevin.havoc.decals.Door;
import com.gamesbykevin.havoc.dungeon.Dungeon;
import com.gamesbykevin.havoc.level.Level;

import static com.gamesbykevin.havoc.enemies.Enemy.*;

public class EnemyHelper {

    protected static void updateIdle(Vector3 position, Enemy enemy) {

        float xDiff = Math.abs(position.x - enemy.getCol());
        float yDiff = Math.abs(position.y - enemy.getRow());

        //where is the player in relation to this enemy
        boolean n = ((int)position.y > (int)enemy.getRow());
        boolean s = ((int)position.y < (int)enemy.getRow());
        boolean w = ((int)position.x < (int)enemy.getCol());
        boolean e = ((int)position.x > (int)enemy.getCol());

        int index = -1;

        //update the animation depending on where the enemy is facing
        switch (enemy.getDirection()) {

            case DIRECTION_S:
                if (n && yDiff > xDiff)
                    index = INDEX_IDLE_N;
                if (s && yDiff > xDiff)
                    index = INDEX_IDLE_S;
                if (w && xDiff > yDiff)
                    index = INDEX_IDLE_E;
                if (e && xDiff > yDiff)
                    index = INDEX_IDLE_W;
                break;

            case DIRECTION_N:
                if (n && yDiff > xDiff)
                    index = INDEX_IDLE_S;
                if (s && yDiff > xDiff)
                    index = INDEX_IDLE_N;
                if (w)
                    index = INDEX_IDLE_W;
                if (e)
                    index = INDEX_IDLE_E;
                break;

            case DIRECTION_W:
                if (n && yDiff > xDiff)
                    index = INDEX_IDLE_E;
                if (s && yDiff > xDiff)
                    index = INDEX_IDLE_W;
                if (w)
                    index = INDEX_IDLE_S;
                if (e)
                    index = INDEX_IDLE_N;
                break;

            case DIRECTION_E:
                if (n && yDiff > xDiff)
                    index = INDEX_IDLE_W;
                if (s && yDiff > xDiff)
                    index = INDEX_IDLE_E;
                if (w)
                    index = INDEX_IDLE_N;
                if (e)
                    index = INDEX_IDLE_S;
                break;
        }

        if (index >= 0 && index != enemy.getIndex())
            enemy.setIndex(index);
    }

    protected static void updateWalk(Vector3 position, Enemy enemy) {

        float xDiff = Math.abs(position.x - enemy.getCol());
        float yDiff = Math.abs(position.y - enemy.getRow());

        //where is the player in relation to this enemy
        boolean n = ((int)position.y > (int)enemy.getRow());
        boolean s = ((int)position.y < (int)enemy.getRow());
        boolean w = ((int)position.x < (int)enemy.getCol());
        boolean e = ((int)position.x > (int)enemy.getCol());

        int index = -1;

        //update the animation depending on where the enemy is facing
        switch (enemy.getDirection()) {

            case DIRECTION_S:
                if (n && yDiff > xDiff)
                    index = INDEX_WALK_N;
                if (s && yDiff > xDiff)
                    index = INDEX_WALK_S;
                if (w && xDiff > yDiff)
                    index = INDEX_WALK_E;
                if (e && xDiff > yDiff)
                    index = INDEX_WALK_W;
                break;

            case DIRECTION_N:
                if (n && yDiff > xDiff)
                    index = INDEX_WALK_S;
                if (s && yDiff > xDiff)
                    index = INDEX_WALK_N;
                if (w && xDiff > yDiff)
                    index = INDEX_WALK_W;
                if (e && xDiff > yDiff)
                    index = INDEX_WALK_E;
                break;

            case DIRECTION_W:
                if (n && yDiff > xDiff)
                    index = INDEX_WALK_E;
                if (s && yDiff > xDiff)
                    index = INDEX_WALK_W;
                if (w && xDiff > yDiff)
                    index = INDEX_WALK_S;
                if (e && xDiff > yDiff)
                    index = INDEX_WALK_N;
                break;

            case DIRECTION_E:
                if (n && yDiff > xDiff)
                    index = INDEX_WALK_W;
                if (s && yDiff > xDiff)
                    index = INDEX_WALK_E;
                if (w && xDiff > yDiff)
                    index = INDEX_WALK_N;
                if (e && xDiff > yDiff)
                    index = INDEX_WALK_S;
                break;
        }

        if (index >= 0 && index != enemy.getIndex())
            enemy.setIndex(index);
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

    protected static boolean isObstructed(Dungeon dungeon, Vector3 location, Enemy enemy) {

        int x = (int)enemy.getCol();
        int y = (int)enemy.getRow();

        //make sure there is a clear vision to the location
        if (!dungeon.hasMap(x, y))
            return true;

        int goalX = (int)location.x;
        int goalY = (int)location.y;

        //continue until we get to the location
        while (x != goalX && y != goalY) {

            if (x < location.x)
                x++;
            if (x > location.x)
                x--;
            if (y < location.y)
                y++;
            if (y > location.y)
                y--;

            //if not an open space, this is blocking our view of the player
            if (!dungeon.hasMap(x, y))
                return true;
        }

        //nothing is blocking from viewing the player
        return false;
    }

    protected static boolean isIdle(Enemy enemy) {

        switch (enemy.getIndex()) {
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

    protected static boolean isHurt(Enemy enemy) {

        switch (enemy.getIndex()) {
            case INDEX_PAIN:
                return true;

            default:
                return false;
        }
    }

    protected static boolean isDead(Enemy enemy) {

        switch (enemy.getIndex()) {
            case INDEX_DIE:
                return true;

            default:
                return false;
        }
    }

    protected static boolean isAttacking(Enemy enemy) {

        switch (enemy.getIndex()) {
            case INDEX_SHOOT:
                return true;

            default:
                return false;
        }
    }

    protected static boolean isWalking(Enemy enemy) {

        switch (enemy.getIndex()) {
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
        Node node = enemy.getPathTarget().get(enemy.getPathIndex());

        //is there a door here?
        if (level.getDungeon().hasInteract(node.getCol(), node.getRow())) {

            //get the door
            Door door = level.getDoorDecal(node.getCol(), node.getRow());

            if (door != null) {

                if (door.isClosed()) {
                    door.setClosed(false);
                    door.setOpening(true);
                    door.setLapsed(0);
                }

                //enemy will remain idle until the door is open
                if (!door.isOpen()) {
                    updateIdle(level.getCamera3d().position, enemy);
                } else {
                    updateWalk(level.getCamera3d().position, enemy);
                }

            } else {
                updateWalk(level.getCamera3d().position, enemy);
            }
        }

        //how far are we from our next node
        float colDiff = Math.abs(node.getCol() - enemy.getCol());
        float rowDiff = Math.abs(node.getRow() - enemy.getRow());

        //if we are at the target we go to the next node
        if (colDiff < enemy.getSpeed() && rowDiff < enemy.getSpeed()) {

            //change the index
            enemy.setPathIndex(enemy.isAscending() ? enemy.getPathIndex() + 1 : enemy.getPathIndex() - 1);

            //make sure we stay in bounds
            if (enemy.getPathIndex() < 0) {
                enemy.setPathIndex(1);
                enemy.setAscending(!enemy.isAscending());

                //we reached the end, so keep the enemy idle for a short time
                updateIdle(level.getCamera3d().position, enemy);
            }

            if (enemy.getPathIndex() >= enemy.getPathTarget().size()) {
                enemy.setPathIndex(enemy.getPathTarget().size() - 2);
                enemy.setAscending(!enemy.isAscending());

                //we reached the end, so keep the enemy idle for a short time
                updateIdle(level.getCamera3d().position, enemy);
            }

            //get the new node
            node = enemy.getPathTarget().get(enemy.getPathIndex());
        }

        if (isWalking(enemy)) {

            //which direction should we be heading?
            if (enemy.getCol() < node.getCol() && colDiff > enemy.getSpeed()) {
                enemy.setDirection(DIRECTION_E);
            } else if (enemy.getCol() > node.getCol() && colDiff > enemy.getSpeed()) {
                enemy.setDirection(DIRECTION_W);
            } else if (enemy.getRow() < node.getRow() && rowDiff > enemy.getSpeed()) {
                enemy.setDirection(DIRECTION_N);
            } else if (enemy.getRow() > node.getRow() && rowDiff > enemy.getSpeed()) {
                enemy.setDirection(DIRECTION_S);
            }

            switch (enemy.getDirection()) {

                case DIRECTION_E:
                    enemy.setCol(enemy.getCol() + enemy.getSpeed());
                    break;

                case DIRECTION_N:
                    enemy.setRow(enemy.getRow() + enemy.getSpeed());
                    break;

                case DIRECTION_S:
                    enemy.setRow(enemy.getRow() - enemy.getSpeed());
                    break;

                case DIRECTION_W:
                    enemy.setCol(enemy.getCol() - enemy.getSpeed());
                    break;
            }

            updateWalk(position, enemy);
        }
    }
}