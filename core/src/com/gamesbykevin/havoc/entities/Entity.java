package com.gamesbykevin.havoc.entities;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g3d.decals.DecalBatch;
import com.gamesbykevin.havoc.level.Level;
import com.gamesbykevin.havoc.util.Disposable;
import com.gamesbykevin.havoc.util.Restart;

import static com.gamesbykevin.havoc.util.Distance.getDistance;

public abstract class Entity implements Disposable, Restart {

    //is the entity solid? for collision detection and rendering etc...
    private boolean solid;

    //location of the entity
    private float col, row;

    //location where the entity starts
    private float startCol, startRow;

    //location where the entity ends
    private float finishCol, finishRow;

    //the index of the current animation
    private int index = 0;

    //how close can the player get to an object
    private static final double PLAYER_COLLISION = 0.9d;

    protected Entity() {
        //default constructor
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

    public float getStartCol() {
        return this.startCol;
    }

    public void setStartCol(float startCol) {
        this.startCol = startCol;
    }

    public float getStartRow() {
        return this.startRow;
    }

    public void setStartRow(float startRow) {
        this.startRow = startRow;
    }

    public float getFinishCol() {
        return this.finishCol;
    }

    public void setFinishCol(float finishCol) {
        this.finishCol = finishCol;
    }

    public float getFinishRow() {
        return this.finishRow;
    }

    public void setFinishRow(float finishRow) {
        this.finishRow = finishRow;
    }

    //logic to reset the entity
    @Override
    public abstract void reset();

    //logic to update the entity
    public abstract void update(Level level);

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

    //implement logic to render an entity
    public abstract void render(AssetManager assetManager, PerspectiveCamera camera, DecalBatch decalBatch, Batch batch);
}