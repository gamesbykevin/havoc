package com.gamesbykevin.havoc.decals;

import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.decals.DecalBatch;
import com.gamesbykevin.havoc.animation.DecalAnimation;
import com.gamesbykevin.havoc.dungeon.Cell;
import com.gamesbykevin.havoc.location.Location;
import com.gamesbykevin.havoc.util.Disposable;
import com.gamesbykevin.havoc.util.Restart;

import static com.gamesbykevin.havoc.decals.Wall.*;

public class Square extends Location implements Disposable, Restart {

    //list of walls for this square
    private Wall[] walls;

    //used for collision
    public static final float COLLISION_RADIUS = .5f;

    //each side of the wall
    public static final int INDEX_SIDE_NORTH = 0;
    public static final int INDEX_SIDE_SOUTH = 1;
    public static final int INDEX_SIDE_WEST = 2;
    public static final int INDEX_SIDE_EAST = 3;

    public Square(float col, float row) {
        super(col, row);
        this.walls = new Wall[4];
    }

    private int getIndex(Side side) {

        switch (side) {

            case None:
            default:
                return -1;

            case East:
                return INDEX_SIDE_EAST;

            case West:
                return INDEX_SIDE_WEST;

            case North:
                return INDEX_SIDE_NORTH;

            case South:
                return INDEX_SIDE_SOUTH;
        }
    }

    public void addWallNorth(DecalAnimation animation, Cell cell) {
        addWall(Side.North, animation, cell);
    }

    public void addWallSouth(DecalAnimation animation, Cell cell) {
        addWall(Side.South, animation, cell);
    }

    public void addWallWest(DecalAnimation animation, Cell cell) {
        addWall(Side.West, animation, cell);
    }

    public void addWallEast(DecalAnimation animation, Cell cell) {
        addWall(Side.East, animation, cell);
    }

    private void addWall(Side side, DecalAnimation animation, Cell cell) {
        getWalls()[getIndex(side)] = DecalCustom.createDecalWall(side, animation,cell);
    }

    public boolean hasCollisionNorth(float col, float row) {
        return hasCollision(col, row, Side.North);
    }

    public boolean hasCollisionSouth(float col, float row) {
        return hasCollision(col, row, Side.South);
    }

    public boolean hasCollisionWest(float col, float row) {
        return hasCollision(col, row, Side.West);
    }

    public boolean hasCollisionEast(float col, float row) {
        return hasCollision(col, row, Side.East);
    }

    private boolean hasCollision(float col, float row, Side side) {

        //if no wall exists there
        if (getWalls()[getIndex(side)] == null)
            return false;

        switch (side) {

            case North:
                return getWalls()[getIndex(side)].hasCollisionNorth(col, row);

            case South:
                return getWalls()[getIndex(side)].hasCollisionSouth(col, row);

            case West:
                return getWalls()[getIndex(side)].hasCollisionWest(col, row);

            case East:
                return getWalls()[getIndex(side)].hasCollisionEast(col, row);
        }

        return false;
    }

    public Wall[] getWalls() {
        return this.walls;
    }

    public void update() {

        for (int i = 0; i < getWalls().length; i++) {

            if (getWalls()[i] == null)
                continue;

            getWalls()[i].update();
        }
    }

    public int render(DecalBatch batch, PerspectiveCamera camera) {

        int count = 0;

        for (int i = 0; i < getWalls().length; i++) {

            if (getWalls()[i] == null)
                continue;

            //render the wall
            getWalls()[i].render(camera, batch);

            //keep track of the number of items rendered
            count++;
        }

        return count;
    }

    @Override
    public void dispose() {

        if (this.walls != null) {
            for (int i = 0; i < this.walls.length; i++) {
                if (this.walls[i] != null) {
                    this.walls[i].dispose();
                    this.walls[i] = null;
                }
            }
        }

        this.walls = null;
    }

    @Override
    public void reset() {
        for (int i = 0; i < getWalls().length; i++) {
            if (getWalls()[i] != null)
                getWalls()[i].reset();
        }
    }
}