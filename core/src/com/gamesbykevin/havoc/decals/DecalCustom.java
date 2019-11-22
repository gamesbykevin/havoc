package com.gamesbykevin.havoc.decals;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.decals.Decal;

public abstract class DecalCustom {

    //size of a block
    public static final float TEXTURE_WIDTH = 1.0f;
    public static final float TEXTURE_HEIGHT = 1.0f;

    //how deep is the door placed
    public static final float DOOR_DEPTH = .5f;

    //the depth will be close to the walls
    public static final float SECRET_DEPTH = .075f;

    //the location of this decal as it relates to the level
    private int col, row;

    public enum Type {
        Wall,
        Door,
        Background
    }

    //what type of decal is this?
    private final Type type;

    //which side is the decal
    public enum Side {
        East, West, North, South
    }

    //the side
    private final Side side;

    //billboard will always be facing the camera
    private boolean billboard = false;

    private final Decal decal;

    public DecalCustom(TextureRegion textureRegion, Type type, Side side, float textureWidth, float textureHeight) {
        this.decal = Decal.newDecal(textureWidth, textureHeight, textureRegion);
        this.type = type;
        this.side = side;
    }

    public Decal getDecal() {
        return this.decal;
    }

    public abstract void update();

    public int getCol() {
        return this.col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public int getRow() {
        return this.row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public boolean isBillboard() {
        return this.billboard;
    }

    public void setBillboard(boolean billboard) {
        this.billboard = billboard;
    }

    public Side getSide() {
        return this.side;
    }

    public Type getType() {
        return this.type;
    }

    public static Wall createDecalWall(float col, float row, TextureRegion texture, Side side) {
        Wall wall = new Wall(texture, side);
        wall.setCol((int)col);
        wall.setRow((int)row);
        decalSetup(wall.getDecal(), col, row, side);
        return wall;
    }

    public static Door createDecalDoor(float col, float row, TextureRegion texture, Side side, boolean secret) {
        Door door = new Door(texture, side, secret);
        door.setCol((int)col);
        door.setRow((int)row);

        //set the start and finish so the door knows how far to open and close
        switch (door.getSide()) {
            case West:
            case East:
                door.setStart(row + (TEXTURE_HEIGHT / 2));
                door.setDestination(row - (TEXTURE_HEIGHT / 2));
                break;

            case North:
            case South:
                door.setStart(col + (TEXTURE_WIDTH / 2));
                door.setDestination(col - (TEXTURE_WIDTH / 2));
                break;
        }

        //setup the door
        decalSetup(door.getDecal(), col, row, side);

        //depth of the door will be different for a secret door
        float depth = (secret) ? SECRET_DEPTH : DOOR_DEPTH;

        //increase the depth of the door so it is noticeable
        switch (door.getSide()) {
            case West:
                door.getDecal().getPosition().x += depth;
                break;

            case East:
                door.getDecal().getPosition().x -= depth;
                break;

            case South:
                door.getDecal().getPosition().y += depth;
                break;

            case North:
                door.getDecal().getPosition().y -= depth;
                break;
        }

        return door;
    }

    private static void decalSetup(Decal decal, float col, float row, Side side) {

        //shift coordinates so they render correct
        float newCol = col + (TEXTURE_WIDTH / 2);
        float newRow = row + TEXTURE_HEIGHT;

        switch (side) {

            case South:
                decal.setPosition(newCol, newRow - TEXTURE_HEIGHT, 0);
                decal.rotateX(90);
                break;

            case North:
                decal.setPosition(newCol, newRow , 0);
                decal.rotateY(90);
                decal.rotateZ(-270);
                decal.rotateY(90);
                break;

            case West:
                decal.setPosition(newCol - (TEXTURE_WIDTH / 2), newRow - (TEXTURE_HEIGHT / 2), 0);
                decal.rotateX(90);
                decal.rotateY(90);
                break;

            case East:
                decal.setPosition(newCol + (TEXTURE_WIDTH / 2), newRow - (TEXTURE_HEIGHT / 2), 0);
                decal.rotateX(90);
                decal.rotateY(90);
                break;
        }
    }
}