package com.gamesbykevin.havoc.enemies;

import com.badlogic.gdx.graphics.g3d.decals.DecalBatch;
import com.gamesbykevin.havoc.animation.DecalAnimation;

public class Enemy {

    private DecalAnimation[] animations;

    private static final float DURATION = 275f;

    private int index = 1;

    private float health = 100f;

    //where is the enemy located
    private float col, row;

    private boolean dead = false;

    public Enemy() {

        this.animations = new DecalAnimation[3];

        //dead
        this.animations[0] = new DecalAnimation("enemies/enemy1/", "", ".png", 7, 4, DURATION);

        //idle
        this.animations[1] = new DecalAnimation("enemies/enemy1/", "", ".png", 0, 4, DURATION);

        //attack
        this.animations[2] = new DecalAnimation("enemies/enemy1/", "", ".png", 4, 3, DURATION);
    }

    public void reset() {
        setHealth(100f);
        setDead(false);
        setIndex(1);
        getAnimation().reset();
    }

    public void update() {

        if (!isDead()) {
            if (getHealth() <= 0) {
                setDead(true);
                setIndex(0);
                getAnimation().reset();
            } else {

                if (getIndex() == 1 && getAnimation().isFinish()) {
                    setIndex(2);
                    getAnimation().reset();
                } else if (getIndex() == 2 && getAnimation().isFinish()) {
                    setIndex(1);
                    getAnimation().reset();
                }
            }
        }

        getAnimation().update();

        //update the location
        getAnimation().setPosition(getCol(), getRow(), 0);
    }

    public boolean isDead() {
        return this.dead;
    }

    public void setDead(boolean dead) {
        this.dead = dead;
    }

    public float getCol() {
        return this.col;
    }

    public void setCol(float col) {
        this.col = col;
    }

    public float getRow() {
        return this.row;
    }

    public void setRow(float row) {
        this.row = row;
    }

    public float getHealth() {
        return this.health;
    }

    public void setHealth(float health) {
        this.health = health;
    }

    public int getIndex() {
        return this.index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public DecalAnimation getAnimation() {
        return this.animations[getIndex()];
    }

    public void render(DecalBatch decalBatch) {
        decalBatch.add(getAnimation().getDecal());
    }
}