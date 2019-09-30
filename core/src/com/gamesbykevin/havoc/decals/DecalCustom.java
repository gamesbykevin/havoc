package com.gamesbykevin.havoc.decals;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.decals.Decal;
import com.badlogic.gdx.graphics.g3d.decals.DecalMaterial;

import static com.gamesbykevin.havoc.level.LevelHelper.DOOR_DEPTH;

public abstract class DecalCustom {

    //size of a block
    public static final float WALL_WIDTH = 1.0f;
    public static final float WALL_HEIGHT = 1.0f;

    //the location of this decal as it relates to the level
    private int col, row;

    public enum Type {
        Wall,
        Door,
        Background,
        Nothing
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

    public DecalCustom(TextureRegion textureRegion, Type type, Side side) {
        this.decal = Decal.newDecal(WALL_WIDTH, WALL_HEIGHT, textureRegion);
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

    public static Door createDecalDoor(float col, float row, TextureRegion texture, Side side) {
        Door door = new Door(texture, side);
        door.setCol((int)col);
        door.setRow((int)row);

        switch (door.getSide()) {
            case West:
            case East:
                door.setStart(row + (WALL_HEIGHT / 2));
                door.setDestination(row - (WALL_HEIGHT / 2));
                break;

            case South:
            case North:
                door.setStart(col + (WALL_WIDTH / 2));
                door.setDestination(col - (WALL_WIDTH / 2));
                break;
        }

        decalSetup(door.getDecal(), col, row, side);

        switch (door.getSide()) {
            case West:
                door.getDecal().getPosition().x += DOOR_DEPTH;
                break;

            case East:
                door.getDecal().getPosition().x -= DOOR_DEPTH;
                break;

            case South:
                door.getDecal().getPosition().y += DOOR_DEPTH;
                break;

            case North:
                door.getDecal().getPosition().y -= DOOR_DEPTH;
                break;
        }

        return door;
    }

    private static void decalSetup(Decal decal, float col, float row, Side side) {

        //shift coordinates so they render correct
        float newCol = col + (WALL_WIDTH / 2);
        float newRow = row + WALL_HEIGHT;

        switch (side) {

            case South:
                decal.setPosition(newCol, newRow - WALL_HEIGHT, 0);
                decal.rotateX(90);
                break;

            case North:
                decal.setPosition(newCol, newRow , 0);
                decal.rotateY(90);
                decal.rotateZ(-270);
                decal.rotateY(90);
                break;

            case West:
                decal.setPosition(newCol - (WALL_WIDTH / 2), newRow - (WALL_HEIGHT / 2), 0);
                decal.rotateX(90);
                decal.rotateY(90);
                break;

            case East:
                decal.setPosition(newCol + (WALL_WIDTH / 2), newRow - (WALL_HEIGHT / 2), 0);
                decal.rotateX(90);
                decal.rotateY(90);
                break;
        }
    }
}