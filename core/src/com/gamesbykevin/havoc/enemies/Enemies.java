package com.gamesbykevin.havoc.enemies;

import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.decals.DecalBatch;
import com.badlogic.gdx.math.Vector3;
import com.gamesbykevin.havoc.decals.Door;
import com.gamesbykevin.havoc.level.Level;
import com.gamesbykevin.havoc.player.weapon.Weapon;

import java.util.ArrayList;
import java.util.List;

import static com.gamesbykevin.havoc.level.LevelHelper.RENDER_RANGE;
import static com.gamesbykevin.havoc.level.LevelHelper.ROOM_SIZE;

public class Enemies {

    private List<Enemy> enemies;

    //how many times do we check for collision
    private static final int ATTEMPT_LIMIT = 100;

    public Enemies() {

        this.enemies = new ArrayList<>();
    }

    public List<Enemy> getEnemies() {
        return this.enemies;
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

        while (attempts < ATTEMPT_LIMIT) {

            for (int i = 0; i < getEnemies().size(); i++) {

                Enemy enemy = getEnemies().get(i);

                //skip if dead
                if (enemy.isDead())
                    continue;

                //calculate distance
                double dist = Math.sqrt((Math.pow(enemy.getCol() - col, 2)) + (Math.pow(enemy.getRow() - row, 2)));

                //if close enough, we hit the enemy
                if (dist <= 1.5d) {
                    enemy.setHealth(enemy.getHealth() - weapon.getDamage());
                    return;
                }
            }

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