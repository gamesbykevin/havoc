package com.gamesbykevin.havoc.enemies;

import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.decals.DecalBatch;
import com.badlogic.gdx.math.Vector3;
import com.gamesbykevin.havoc.decals.Door;
import com.gamesbykevin.havoc.level.Level;
import com.gamesbykevin.havoc.player.weapon.Weapon;

import java.util.ArrayList;
import java.util.List;

import static com.gamesbykevin.havoc.decals.DecalCustom.TEXTURE_HEIGHT;
import static com.gamesbykevin.havoc.decals.DecalCustom.TEXTURE_WIDTH;
import static com.gamesbykevin.havoc.level.LevelHelper.getDistance;
import static com.gamesbykevin.havoc.player.Player.PLAYER_COLLISION;

public class Enemies {

    private List<Enemy> enemies;

    //how many times do we check for collision
    private static final int ATTEMPT_LIMIT = 200;

    //how close does the bullet need to be for collision detection
    private static final double BULLET_DISTANCE = 1.25d;

    public Enemies() {
        this.enemies = new ArrayList<>();
    }

    public void add(float col, float row) {
        Enemy enemy = new Enemy();
        enemy.setCol(col);
        enemy.setRow(row);
        enemy.setCol(enemy.getCol() - (TEXTURE_WIDTH / 2));
        enemy.setRow(enemy.getRow() - (TEXTURE_HEIGHT / 2));
        getEnemies().add(enemy);
    }

    private List<Enemy> getEnemies() {
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
            if (level.hasWall((int)col, (int)row))
                return;

            if (level.hasDoor((int)col, (int)row)) {

                //get the door at the current location
                Door door = level.getDoorDecal((int)col, (int)row);

                //if the door is closed then we hit the door
                if (door != null && !door.isOpen())
                    return;
            }

            //keep track of the attempts
            attempts++;
        }
    }

    public void reset() {
        for (int i = 0; i < getEnemies().size(); i++) {
            getEnemies().get(i).reset();
        }
    }

    public void render(DecalBatch decalBatch, PerspectiveCamera camera3d) {

        for (int i = 0; i < getEnemies().size(); i++) {

            //get the current enemy
            Enemy enemy = getEnemies().get(i);

            //update the enemy
            enemy.update(camera3d);

            //render like a billboard
            enemy.getAnimation().getDecal().lookAt(camera3d.position, camera3d.up);

            //add to the batch to be rendered
            enemy.render(decalBatch);
        }
    }
}