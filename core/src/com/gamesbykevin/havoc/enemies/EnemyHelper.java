package com.gamesbykevin.havoc.enemies;

import com.gamesbykevin.havoc.astar.Node;
import com.gamesbykevin.havoc.decals.Door;
import com.gamesbykevin.havoc.level.Level;

import static com.gamesbykevin.havoc.enemies.Enemy.*;
import static com.gamesbykevin.havoc.entities.Entities.OFFSET;

public class EnemyHelper {

    //possible directions to face
    public static final int DIRECTION_S = 1;
    public static final int DIRECTION_W = 2;
    public static final int DIRECTION_N = 3;
    public static final int DIRECTION_E = 4;

    protected static void patrol(Enemy enemy, Level level) {

        //we can't patrol if we are hurt or paused
        if (enemy.isHurt() || enemy.isPause())
            return;

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
                    enemy.setStatus(Status.Idle);
                } else {
                    enemy.setStatus(Status.Walk);
                }

            } else {
                enemy.setStatus(Status.Walk);
            }
        }

        //if the player is in the way, the enemy will stand idle
        if (enemy.hasCollision(level.getPlayer().getCamera3d().position.x, level.getPlayer().getCamera3d().position.y))
            enemy.setStatus(Status.Idle);

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
                enemy.setStatus(Status.Idle);
            }

            if (enemy.getPathIndex() >= enemy.getPathPatrol().size()) {
                enemy.setPathIndex(enemy.getPathPatrol().size() - 1);
                enemy.setAscending(!enemy.isAscending());

                //we reached the end, so keep the enemy idle for a short time
                enemy.setStatus(Status.Idle);
            }

            node = enemy.getPathPatrol().get(enemy.getPathIndex());
        }

        if (enemy.isWalk()) {

            //determine which direction to face
            if (enemy.getCol() < node.getCol() && colDiff > enemy.getSpeed()) {
                enemy.setDirection(DIRECTION_E);
            } else if (enemy.getCol() > node.getCol() && colDiff > enemy.getSpeed()) {
                enemy.setDirection(DIRECTION_W);
            } else if (enemy.getRow() < node.getRow() && rowDiff > enemy.getSpeed()) {
                enemy.setDirection(DIRECTION_N);
            } else if (enemy.getRow() > node.getRow() && rowDiff > enemy.getSpeed()) {
                enemy.setDirection(DIRECTION_S);
            }

            //move in the direction we are facing
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
        }
    }

    protected static void update(Enemy enemy, Level level, double distance) {

        //if idle or walking
        if (enemy.isIdle() || enemy.isWalk()) {

            //if enemy is facing the player and we can shoot, then don't wait
            if (enemy.isFacing(level.getPlayer().getCamera3d().position) && enemy.canShoot(distance, level)) {
                enemy.setStatus(Status.Shoot);
                chase(level, enemy);
            }
        }

        //we only want to update when the animation is finished
        if (!enemy.getAnimation().isFinish())
            return;

        if (enemy.isHurt()) {

            //are we close enough to check if we can attack?
            if (!enemy.isObstructed(level)) {
                enemy.setStatus(Status.Shoot);
                chase(level, enemy);
            } else {
                enemy.setStatus(Status.Idle);
            }

        } else if (enemy.isShoot() || enemy.isAlert()) {

            //if the enemy finished shooting flag the player as hurt
            level.getPlayer().setHurt(true);
            level.getPlayer().setHealth(level.getPlayer().getHealth() - enemy.getDamage());

            //if close enough the enemy will remain alert
            if (enemy.canShoot(distance, level)) {
                enemy.setStatus(Status.Pause);
                chase(level, enemy);
            } else {
                enemy.setStatus(Status.Idle);
            }

        } else if (enemy.isPause()) {

            //if close enough and our view isn't blocked
            if (enemy.canShoot(distance, level)) {
                enemy.setStatus(Status.Alert);
                chase(level, enemy);
            } else {
                enemy.setStatus(Status.Idle);
            }

        } else if (enemy.isIdle() || enemy.isWalk()) {

            //start by setting idle
            enemy.setStatus(Status.Idle);

            //if the player is shooting and the enemy has a clear view of them, start shooting
            if (level.getPlayer().getController().isShooting() && !enemy.isObstructed(level)) {
                enemy.setStatus(Status.Shoot);
                chase(level, enemy);
            }

            //if we are idle but we have a patrol path, start walking again
            if (enemy.isIdle() && enemy.getPathPatrol() != null && !enemy.getPathPatrol().isEmpty())
                enemy.setStatus(Status.Walk);
        }

        //reset the animation
        enemy.getAnimation().reset();
    }

    public static void chase(Level level, Enemy enemy) {

        //change the speed since we are chasing
        enemy.setSpeed(SPEED_CHASE);

        //calculate the path to get to the player
        calculatePath(level, enemy, enemy.getCol(), enemy.getRow(), level.getPlayer().getCamera3d().position.x, level.getPlayer().getCamera3d().position.y);
    }

    public static void calculatePath(Level level, Enemy enemy, float startCol, float startRow, float finishCol, float finishRow) {

        startCol += OFFSET;
        startRow += OFFSET;
        finishCol += OFFSET;
        finishRow += OFFSET;

        if (!level.getDungeon().hasMap(startCol, startRow)) {
            if (startCol < finishCol && level.getDungeon().hasMap(startCol + OFFSET, startRow)) {
                startCol++;
            } else if (startCol > finishCol && level.getDungeon().hasMap(startCol - OFFSET, startRow)) {
                startCol--;
            } else if (startRow < finishRow && level.getDungeon().hasMap(startCol, startRow + OFFSET)) {
                startRow++;
            } else if (startRow > finishRow && level.getDungeon().hasMap(startCol, startRow - OFFSET)) {
                startRow--;
            }
        }

        if (!level.getDungeon().hasMap(finishCol, finishRow)) {
            if (finishCol < startCol && level.getDungeon().hasMap(finishCol + OFFSET, finishRow)) {
                finishCol++;
            } else if (finishCol > startCol && level.getDungeon().hasMap(finishCol - OFFSET, finishRow)) {
                finishCol--;
            } else if (finishRow < startRow && level.getDungeon().hasMap(finishCol, finishRow + OFFSET)) {
                finishRow++;
            } else if (finishRow > startRow && level.getDungeon().hasMap(finishCol, finishRow - OFFSET)) {
                finishRow--;
            }
        }

        //clear the path, if it exists
        if (enemy.getPathPatrol() != null)
            enemy.getPathPatrol().clear();

        //let's move towards the player when able to
        level.getDungeon().getAStar().setDiagonal(false);
        level.getDungeon().getAStar().calculate((int)startCol, (int)startRow, (int)finishCol, (int)finishRow);

        //set the path for the enemy to follow
        enemy.setPathPatrol(level.getDungeon().getAStar().getPath());
    }
}