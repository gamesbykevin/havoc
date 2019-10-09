package com.gamesbykevin.havoc.enemies;

import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.decals.DecalBatch;
import com.gamesbykevin.havoc.animation.DecalAnimation;

import static com.gamesbykevin.havoc.level.LevelHelper.getDistance;

public class Enemy {

    private DecalAnimation[] animations;

    //animation delay
    private static final float DURATION = 250f;

    //how close to notice the player
    private static final float RANGE = 7f;

    private int index = 1;

    private float health = 100f;

    //where is the enemy located
    private float col, row;

    //is the enemy dead
    private boolean dead = false;

    //different animations
    public static final int INDEX_IDLE = 1;
    public static final int INDEX_ATTACK = 2;
    public static final int INDEX_DEAD = 0;

    public Enemy() {

        //our animations
        this.animations = new DecalAnimation[3];
        this.animations[INDEX_DEAD] = new DecalAnimation("enemies/enemy1/", "", ".png", 7, 4, DURATION);
        this.animations[INDEX_IDLE] = new DecalAnimation("enemies/enemy1/", "", ".png", 0, 4, DURATION);
        this.animations[INDEX_ATTACK] = new DecalAnimation("enemies/enemy1/", "", ".png", 4, 3, DURATION);

        reset();
    }

    public void reset() {
        setHealth(100f);
        setDead(false);
        setIndex(INDEX_IDLE);
        getAnimation().reset();
        getAnimation().setPosition(getCol(), getRow(), 0);
    }

    public void update(PerspectiveCamera camera3d) {

        if (!isDead()) {

            if (getHealth() <= 0) {

                setDead(true);
                setIndex(INDEX_DEAD);
                getAnimation().reset();

            } else {

                //calculate distance
                double dist = getDistance(getCol(), getRow(), camera3d.position.x, camera3d.position.y);

                //if the enemy isn't close enough
                if (dist > RANGE) {
                    getAnimation().reset();
                    getAnimation().setPosition(getCol(), getRow(), 0);
                    return;
                }

                if (getAnimation().isFinish()) {

                    switch (getIndex()) {
                        case INDEX_IDLE:
                            setIndex(INDEX_ATTACK);
                            getAnimation().reset();
                            break;

                        case INDEX_ATTACK:
                            setIndex(INDEX_IDLE);
                            getAnimation().reset();
                            break;
                    }
                }
            }
        }

        //update the animation
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