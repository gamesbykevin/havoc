package com.gamesbykevin.havoc.enemies;

import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.decals.DecalBatch;
import com.badlogic.gdx.math.Vector3;
import com.gamesbykevin.havoc.player.weapon.Magnum;
import com.gamesbykevin.havoc.player.weapon.Weapon;

import java.util.ArrayList;
import java.util.List;

public class Enemies {

    private List<Enemy> enemies;

    public Enemies() {

        this.enemies = new ArrayList<>();
    }

    public List<Enemy> getEnemies() {
        return this.enemies;
    }

    public void checkAttack(Weapon weapon, double angle, Vector3 position, float speed) {

        float col = position.x;
        float row = position.y;

        //calculate distance moved
        double xa = (0 * Math.cos(angle)) - (1 * Math.sin(angle));
        xa *= speed;

        double ya = (1 * Math.cos(angle)) + (0 * Math.sin(angle));
        ya *= speed;

        boolean loop = true;

        int checks = 0;

        while (loop) {

            for (int i = 0; i < getEnemies().size(); i++) {
                Enemy enemy = getEnemies().get(i);

                //calculate distance
                double dist = Math.sqrt((Math.pow(enemy.getCol() - col, 2)) + (Math.pow(enemy.getRow() - row, 2)));

                if (dist <= 1.5) {
                    enemy.setHealth(enemy.getHealth() - weapon.getDamage());
                    loop = false;
                    break;
                }
            }

            col += xa;
            row += ya;

            checks++;

            if (checks >= 100) {
                loop = false;
                break;
            }
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