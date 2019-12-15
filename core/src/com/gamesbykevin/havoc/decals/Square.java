package com.gamesbykevin.havoc.decals;

import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.decals.DecalBatch;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;
import com.gamesbykevin.havoc.location.Location;
import com.gamesbykevin.havoc.util.Disposable;

import static com.gamesbykevin.havoc.decals.Wall.*;
import static com.gamesbykevin.havoc.entities.Entities.OFFSET;

public class Square extends Location implements Disposable {

    //list of walls for this square
    private Wall[] walls;

    //used for collision
    public static final float COLLISION_RADIUS = .5f;

    //used for collision
    private Vector3 center;

    public Square(float col, float row) {
        super(col, row);
        this.walls = new Wall[4];
        this.center = new Vector3(getCol() + OFFSET, getRow() + OFFSET, OFFSET);
    }

    public Vector3 getCenter() {
        return this.center;
    }

    public boolean hasCollision(Ray ray) {
        return Intersector.intersectRaySphere(ray, getCenter(), COLLISION_RADIUS, null);
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
        this.center = null;
    }
}