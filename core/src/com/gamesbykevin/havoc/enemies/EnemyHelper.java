package com.gamesbykevin.havoc.enemies;

import com.gamesbykevin.havoc.astar.Node;
import com.gamesbykevin.havoc.decals.Door;
import com.gamesbykevin.havoc.level.Level;

import static com.gamesbykevin.havoc.enemies.Enemy.*;

public class EnemyHelper {

    //possible directions to face
    public static final int DIRECTION_S = 1;
    public static final int DIRECTION_W = 2;
    public static final int DIRECTION_N = 3;
    public static final int DIRECTION_E = 4;

    protected static void patrol(Enemy enemy, Level level) {

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
                enemy.setStatus(Status.Idle);
            }

            if (enemy.getPathIndex() >= enemy.getPathPatrol().size()) {
                enemy.setPathIndex(enemy.getPathPatrol().size() - 1);
                enemy.setAscending(!enemy.isAscending());
            }

            //get the new node
            node = enemy.getPathPatrol().get(enemy.getPathIndex());
        }

        if (enemy.isWalk()) {

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
        }
    }

    protected static void update(Enemy enemy, Level level, double distance) {

        //we only want to update when the animation is finished
        if (!enemy.getAnimation().isFinish())
            return;

        if (enemy.isHurt()) {

            //are we close enough to check if we can attack?
            if (!enemy.isObstructed(level)) {
                enemy.setStatus(Status.Shoot);
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
            } else {
                enemy.setStatus(Status.Idle);
            }

        } else if (enemy.isPause()) {

            //if close enough and our view isn't blocked
            if (enemy.canShoot(distance, level))
                enemy.setStatus(Status.Alert);

        } else if (enemy.isIdle() || enemy.isWalk()) {

            //are we close enough to check if we can attack?
            if (distance < RANGE_NOTICE) {

                //check to see if the enemy is facing the player
                if (enemy.isFacing(level.getPlayer().getCamera3d().position)) {

                    //if nothing is blocking our view, then the enemy can see the player
                    if (!enemy.isObstructed(level))
                        enemy.setStatus(Status.Shoot);
                }

                //if the player is shooting and the enemy has a clear view of them, start shooting
                if (level.getPlayer().getController().isShooting() && !enemy.isObstructed(level))
                    enemy.setStatus(Status.Shoot);

            } else {

                //if not in range we go back to idle
                enemy.setStatus(Status.Idle);
            }

            //if we are idle and we have a path, start walking again
            if (enemy.isIdle() && (enemy.getPathPatrol() != null && !enemy.getPathPatrol().isEmpty()))
                enemy.setStatus(Status.Walk);
        }

        //reset the animation
        enemy.getAnimation().reset();
    }
}