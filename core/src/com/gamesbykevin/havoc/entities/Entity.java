package com.gamesbykevin.havoc.entities;

import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.decals.DecalBatch;
import com.gamesbykevin.havoc.animation.DecalAnimation;

import static com.gamesbykevin.havoc.entities.Entities.getDistance;

public abstract class Entity {

    //is the entity solid? for collision detection
    private boolean solid;

    //location of the entity
    private float col, row;

    //the index of the current animation
    private int index = 0;

    //array of animations for our entity
    private DecalAnimation[] animations;

    //how close can the player get to an object
    public static final double PLAYER_COLLISION = 0.75d;

    public Entity(int count) {

        //create array of animations
        this.animations = new DecalAnimation[count];

        //now create and load the animations accordingly
        createAnimations();
    }

    protected DecalAnimation[] getAnimations() {
        return this.animations;
    }

    public DecalAnimation getAnimation() {
        return getAnimations()[getIndex()];
    }

    public int getIndex() {
        return this.index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public boolean isSolid() {
        return this.solid;
    }

    public void setSolid(boolean solid) {
        this.solid = solid;
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

    //logic to reset the entity
    public abstract void reset();

    //logic to update the entity
    public abstract void update(PerspectiveCamera camera3d);

    //create the animations for the entity
    public abstract void createAnimations();

    public boolean hasCollision(float x, float y) {

        //skip if not solid
        if (!isSolid())
            return false;

        //if close enough we have collision
        if (getDistance(getCol(), getRow(), x, y) <= PLAYER_COLLISION)
            return true;

        //no collision
        return false;
    }

    public void render(DecalBatch decalBatch) {
        decalBatch.add(getAnimation().getDecal());
    }
}