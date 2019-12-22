package com.gamesbykevin.havoc.decals;

import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.decals.DecalBatch;
import com.gamesbykevin.havoc.location.Location;
import com.gamesbykevin.havoc.util.Disposable;

import static com.gamesbykevin.havoc.decals.Wall.*;

public class Square extends Location implements Disposable {

    //list of walls for this square
    private Wall[] walls;

    //used for collision
    public static final float COLLISION_RADIUS = .5f;

    public Square(float col, float row) {
        super(col, row);
        this.walls = new Wall[4];
    }

    public void addWalls(TextureRegion textureRegion) {
        addWallNorth(textureRegion);
        addWallSouth(textureRegion);
        addWallWest(textureRegion);
        addWallEast(textureRegion);
    }

    public void addWallNorth(TextureRegion textureRegion) {
        addWall(textureRegion, SIDE_NORTH);
    }

    public void addWallSouth(TextureRegion textureRegion) {
        addWall(textureRegion, SIDE_SOUTH);
    }

    public void addWallWest(TextureRegion textureRegion) {
        addWall(textureRegion, SIDE_WEST);
    }

    public void addWallEast(TextureRegion textureRegion) {
        addWall(textureRegion, SIDE_EAST);
    }

    private void addWall(TextureRegion textureRegion, int side) {
        getWalls()[side] = DecalCustom.createDecalWall(getCol(), getRow(), textureRegion, side);
    }

    public boolean hasCollisionNorth(float col, float row) {
        return hasCollision(col, row, SIDE_NORTH);
    }

    public boolean hasCollisionSouth(float col, float row) {
        return hasCollision(col, row, SIDE_SOUTH);
    }

    public boolean hasCollisionWest(float col, float row) {
        return hasCollision(col, row, SIDE_WEST);
    }

    public boolean hasCollisionEast(float col, float row) {
        return hasCollision(col, row, SIDE_EAST);
    }

    private boolean hasCollision(float col, float row, int side) {

        //if no wall exists there
        if (getWalls()[side] == null)
            return false;

        switch (side) {

            case SIDE_NORTH:
                return getWalls()[side].hasCollisionNorth(col, row);

            case SIDE_SOUTH:
                return getWalls()[side].hasCollisionSouth(col, row);

            case SIDE_WEST:
                return getWalls()[side].hasCollisionWest(col, row);

            case SIDE_EAST:
                return getWalls()[side].hasCollisionEast(col, row);
        }

        return false;
    }

    public Wall[] getWalls() {
        return this.walls;
    }

    public int render(DecalBatch batch, PerspectiveCamera camera) {

        int count = 0;

        for (int i = 0; i < getWalls().length; i++) {

            if (getWalls()[i] == null)
                continue;

            if (getWalls()[i].isBillboard())
                getWalls()[i].getDecal().lookAt(camera.position, camera.up);

            count++;
            batch.add(getWalls()[i].getDecal());
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
}