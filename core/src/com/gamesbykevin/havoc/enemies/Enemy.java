package com.gamesbykevin.havoc.enemies;

import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.gamesbykevin.havoc.animation.DecalAnimation;
import com.gamesbykevin.havoc.entities.Entity;

import static com.gamesbykevin.havoc.entities.Entities.getDistance;

public final class Enemy extends Entity {

    //animation delay
    private static final float DURATION = 250f;

    //how close to notice the player
    private static final float RANGE = 7f;

    //what is the health
    private float health = 100f;

    //different animations
    public static final int INDEX_IDLE = 1;
    public static final int INDEX_ATTACK = 2;
    public static final int INDEX_DEAD = 0;

    //how many animations are there?
    public static final int ANIMATION_COUNT = 3;

    public Enemy() {
        super(ANIMATION_COUNT);

        reset();
    }

    @Override
    public void createAnimations() {
        getAnimations()[INDEX_DEAD] = new DecalAnimation("enemies/enemy1/", "", ".png", 7, 4, DURATION);
        getAnimations()[INDEX_IDLE] = new DecalAnimation("enemies/enemy1/", "", ".png", 0, 4, DURATION);
        getAnimations()[INDEX_ATTACK] = new DecalAnimation("enemies/enemy1/", "", ".png", 4, 3, DURATION);
    }

    @Override
    public void reset() {
        setHealth(100f);
        setSolid(true);
        setIndex(INDEX_IDLE);
        getAnimation().reset();
        getAnimation().setPosition(getCol(), getRow(), 0);
    }

    @Override
    public void update(PerspectiveCamera camera3d) {

        if (isSolid()) {

            if (getHealth() <= 0) {

                setSolid(false);
                setIndex(INDEX_DEAD);
                getAnimation().reset();

            } else {

                //calculate distance
                double dist = getDistance(this, camera3d.position);

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

    public float getHealth() {
        return this.health;
    }

    public void setHealth(float health) {
        this.health = health;
    }
}