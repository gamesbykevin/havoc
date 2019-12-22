package com.gamesbykevin.havoc.decals;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.decals.Decal;
import com.gamesbykevin.havoc.location.Location;
import com.gamesbykevin.havoc.util.Disposable;

import static com.gamesbykevin.havoc.decals.Door.DOOR_DEPTH;
import static com.gamesbykevin.havoc.decals.Door.SECRET_DEPTH;
import static com.gamesbykevin.havoc.decals.Square.COLLISION_RADIUS;
import static com.gamesbykevin.havoc.decals.Wall.*;
import static com.gamesbykevin.havoc.util.Distance.getDistance;

public abstract class DecalCustom extends Location implements Disposable {

    //size of a block
    public static final float TEXTURE_WIDTH = 1.0f;
    public static final float TEXTURE_HEIGHT = 1.0f;

    //the side this decal is
    private final int side;

    //billboard will always be facing the camera
    private boolean billboard = false;

    //our decal for rendering
    private Decal decal;

    //check for collision around this decal
    private static final float COLLISION = COLLISION_RADIUS + .25f;

    protected DecalCustom(TextureRegion textureRegion, int side, float textureWidth, float textureHeight) {
        this.decal = Decal.newDecal(textureWidth, textureHeight, textureRegion);
        this.side = side;
    }

    public Decal getDecal() {
        return this.decal;
    }

    public abstract void update();

    public boolean isBillboard() {
        return this.billboard;
    }

    public void setBillboard(boolean billboard) {
        this.billboard = billboard;
    }

    public int getSide() {
        return this.side;
    }

    protected static Wall createDecalWall(float col, float row, TextureRegion texture, int side) {
        Wall wall = new Wall(texture, side);
        wall.setCol(col);
        wall.setRow(row);
        decalSetup(wall.getDecal(), col, row, side);
        return wall;
    }

    public static Door createDecalDoor(float col, float row, TextureRegion texture, int side, boolean secret) {

        Door door = new Door(texture, side, secret);
        door.setCol(col);
        door.setRow(row);

        //setup the door
        decalSetup(door.getDecal(), col, row, side);

        //set the start and finish so the door knows how far to open and close
        switch (door.getSide()) {
            case SIDE_WEST:
            case SIDE_EAST:
                door.setStart(row);
                door.setDestination(row - 1f);
                break;

            case SIDE_NORTH:
            case SIDE_SOUTH:
                door.setStart(col);
                door.setDestination(col - 1f);
                break;
        }

        //depth of the door will be different for a secret door
        float depth = (secret) ? SECRET_DEPTH : DOOR_DEPTH;

        //increase the depth of the door so it is noticeable
        switch (door.getSide()) {
            case SIDE_WEST:
                door.getDecal().getPosition().x += depth;
                break;

            case SIDE_EAST:
                door.getDecal().getPosition().x -= depth;
                break;

            case SIDE_SOUTH:
                door.getDecal().getPosition().y += depth;
                break;

            case SIDE_NORTH:
                door.getDecal().getPosition().y -= depth;
                break;
        }

        return door;
    }

    private static void decalSetup(Decal decal, float col, float row, int side) {

        switch (side) {

            case SIDE_SOUTH:
                decal.setPosition(col, row - COLLISION_RADIUS, 0);
                decal.rotateX(90);
                break;

            case SIDE_NORTH:
                decal.setPosition(col, row + COLLISION_RADIUS, 0);
                decal.rotateY(90);
                decal.rotateZ(-270);
                decal.rotateY(90);
                break;

            case SIDE_WEST:
                decal.setPosition(col - COLLISION_RADIUS, row, 0);
                decal.rotateX(90);
                decal.rotateY(90);
                break;

            case SIDE_EAST:
                decal.setPosition(col + COLLISION_RADIUS, row, 0);
                decal.rotateX(90);
                decal.rotateY(90);
                break;
        }
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

    public boolean hasCollision(float col, float row) {

        if (hasCollisionWest(col, row))
            return true;
        if (hasCollisionEast(col, row))
            return true;
        if (hasCollisionNorth(col, row))
            return true;
        if (hasCollisionSouth(col, row))
            return true;

        return false;
    }

    private boolean hasCollision(float col, float row, int side) {

        switch (side) {

            case SIDE_NORTH:
                return (getDistance(col, row, getCol(), getRow() + COLLISION_RADIUS) < COLLISION);

            case SIDE_SOUTH:
                return (getDistance(col, row, getCol(), getRow() - COLLISION_RADIUS) < COLLISION);

            case SIDE_WEST:
                return (getDistance(col, row, getCol() - COLLISION_RADIUS, getRow()) < COLLISION);

            case SIDE_EAST:
                return (getDistance(col, row, getCol() + COLLISION_RADIUS, getRow()) < COLLISION);
        }

        return false;
    }

    @Override
    public void dispose() {
        this.decal = null;
    }
}