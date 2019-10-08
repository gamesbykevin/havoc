package com.gamesbykevin.havoc.enemies;

import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.decals.DecalBatch;
import com.badlogic.gdx.math.Vector3;
import com.gamesbykevin.havoc.decals.Door;
import com.gamesbykevin.havoc.level.Level;
import com.gamesbykevin.havoc.player.weapon.Weapon;

import java.util.ArrayList;
import java.util.List;

public class Enemies {

    private List<Enemy> enemies;

    //how many times do we check for collision
    private static final int ATTEMPT_LIMIT = 200;

    //how close does the bullet need to be for collision detection
    private static final double BULLET_DISTANCE = 1.5d;

    //how close can the player get to an enemy
    private static final double PLAYER_COLLISION = 0.75d;

    public Enemies() {
        this.enemies = new ArrayList<>();
    }

    public List<Enemy> getEnemies() {
        return this.enemies;
    }

    public boolean hasCollision(float x, float y) {

        for (int i = 0; i < getEnemies().size(); i++) {

            Enemy enemy = getEnemies().get(i);

            //skip if dead
            if (enemy.isDead())
                continue;

            if (getDistance(enemy.getCol(), enemy.getRow(), x, y) <= PLAYER_COLLISION)
                return true;
        }

        return false;
    }

    public void checkAttack(Weapon weapon, Level level, double angle, Vector3 position, float speed) {

        //start position of attack
        float col = position.x;
        float row = position.y;

        //calculate the distance moved
        double xa = (0 * Math.cos(angle)) - (1 * Math.sin(angle));
        double ya = (1 * Math.cos(angle)) + (0 * Math.sin(angle));
        xa *= speed;
        ya *= speed;

        int attempts = 0;

        //do we have range
        boolean range = false;

        while (attempts < ATTEMPT_LIMIT) {

            for (int i = 0; i < getEnemies().size(); i++) {

                Enemy enemy = getEnemies().get(i);

                //skip if dead
                if (enemy.isDead())
                    continue;

                //how far are we from the enemy
                double playerDistance = getDistance(enemy.getCol(), enemy.getRow(), position.x, position.y);

                //if too far away to attack skip this enemy
                if (playerDistance >= weapon.getRange())
                    continue;

                //flag that we have range with at least 1 enemy
                range = true;

                //calculate distance
                double dist = getDistance(enemy.getCol(), enemy.getRow(), col, row);

                //if close enough, we hit the enemy
                if (dist <= BULLET_DISTANCE) {
                    System.out.println("HIT ENEMY!!!");
                    enemy.setHealth(enemy.getHealth() - weapon.getDamage());
                    return;
                }
            }

            //if we don't have range with any enemies skip this
            if (!range)
                break;

            //move to the next position
            col += xa;
            row += ya;

            //check if we hit a wall
            if (level.hasWall((int)col, (int)row)) {
                System.out.println("Hit wall");
                return;
            } else if (level.hasDoor((int)col, (int)row)) {

                //get the door at the current location
                Door door = level.getDoorDecal((int)col, (int)row);

                //if the door is closed then we hit the door
                if (door != null && !door.isOpen()) {
                    System.out.println("Hit door");
                    return;
                }
            }

            //keep track of the attempts
            attempts++;
        }
    }

    public static double getDistance(float x1, float y1, float x2, float y2) {
        return Math.sqrt(Math.pow(x2 - x1, 2) + (Math.pow(y2 - y1, 2)));
    }

    public void reset() {
        for (int i = 0; i < getEnemies().size(); i++) {
            getEnemies().get(i).reset();
        }
    }

    public void render(DecalBatch decalBatch, PerspectiveCamera camera3d) {

        for (int i = 0; i < getEnemies().size(); i++) {
            Enemy enemy = getEnemies().get(i);
            enemy.update();
            enemy.getAnimation().getDecal().lookAt(camera3d.position, camera3d.up);
            enemy.render(decalBatch);
        }
    }
}