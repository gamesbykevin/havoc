package com.gamesbykevin.havoc.decals;

import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.decals.Decal;
import com.badlogic.gdx.graphics.g3d.decals.DecalBatch;
import com.gamesbykevin.havoc.animation.DecalAnimation;
import com.gamesbykevin.havoc.dungeon.Cell;
import com.gamesbykevin.havoc.location.Location;
import com.gamesbykevin.havoc.util.Disposable;
import com.gamesbykevin.havoc.util.Restart;

import static com.gamesbykevin.havoc.decals.Background.*;
import static com.gamesbykevin.havoc.decals.Door.DOOR_DEPTH;
import static com.gamesbykevin.havoc.decals.Door.SECRET_DEPTH;
import static com.gamesbykevin.havoc.decals.Square.COLLISION_RADIUS;
import static com.gamesbykevin.havoc.util.Distance.getDistance;

public abstract class DecalCustom extends Location implements Disposable, Restart {

    //the side this decal is
    private final Side side;

    //our decal animation for rendering
    private DecalAnimation decalAnimation;

    //check for collision around this decal
    private static final float COLLISION = COLLISION_RADIUS + .2f;

    public enum Side {
        North, South, West, East, None
    }

    protected DecalCustom(Side side, DecalAnimation decalAnimation) {

        //what side?
        this.side = side;

        //store our reference and reset the animation (just in case)
        this.decalAnimation = decalAnimation;
        this.decalAnimation.reset();
    }

    public DecalAnimation getAnimation() {
        return this.decalAnimation;
    }

    public Decal getDecal() {
        return getAnimation().getDecal();
    }

    public abstract void update();

    public Side getSide() {
        return this.side;
    }

    protected static Wall createDecalWall(Side side, DecalAnimation animation, Cell cell) {
        Wall wall = new Wall(side, animation);
        wall.setCol(cell.getCol());
        wall.setRow(cell.getRow());
        decalSetup(wall);
        return wall;
    }

    public static Background createDecalBackground(DecalAnimation animation, float col, float row, boolean floor) {
        Background background = new Background(animation);
        background.setCol(col);
        background.setRow(row);

        //update all decals
        for (int i = 0; i < background.getAnimation().getCount(); i++) {
            background.getAnimation().getDecal(i).setPosition(col, row, (floor) ? HEIGHT_FLOOR : HEIGHT_CEILING);
        }
        return background;
    }

    public static Door createDecalDoor(Side side, DecalAnimation animation, Cell cell) {

        Door door = new Door(side, animation, cell);
        door.setCol(cell.getCol());
        door.setRow(cell.getRow());
        decalSetup(door);

        //set the start and finish so the door knows how far to open and close
        switch (door.getSide()) {

            case West:
            case East:
                door.setStart(door.getRow());
                door.setDestination(door.getRow() - 1f);
                break;

            case North:
            case South:
                door.setStart(door.getCol());
                door.setDestination(door.getCol() - 1f);
                break;
        }

        //depth of the door will be different for a secret door
        float depth = (cell.isSecret()) ? SECRET_DEPTH : DOOR_DEPTH;

        //update all decals
        for (int i = 0; i < door.getAnimation().getCount(); i++) {

            Decal decal = door.getAnimation().getDecal(i);

            //increase the depth of the door so it is noticeable
            switch (door.getSide()) {
                case West:
                    decal.getPosition().x += depth;
                    break;

                case East:
                    decal.getPosition().x -= depth;
                    break;

                case South:
                    decal.getPosition().y += depth;
                    break;

                case North:
                    decal.getPosition().y -= depth;
                    break;
            }
        }

        return door;
    }

    private static void decalSetup(DecalCustom decalCustom) {

        //update all decals
        for (int i = 0; i < decalCustom.getAnimation().getCount(); i++) {

            Decal decal = decalCustom.getAnimation().getDecal(i);

            switch (decalCustom.getSide()) {

                case South:
                    decal.setPosition(decalCustom.getCol(), decalCustom.getRow() - COLLISION_RADIUS, 0);
                    decal.rotateX(90);
                    break;

                case North:
                    decal.setPosition(decalCustom.getCol(), decalCustom.getRow() + COLLISION_RADIUS, 0);
                    decal.rotateY(90);
                    decal.rotateZ(-270);
                    decal.rotateY(90);
                    break;

                case West:
                    decal.setPosition(decalCustom.getCol() - COLLISION_RADIUS, decalCustom.getRow(), 0);
                    decal.rotateX(90);
                    decal.rotateY(90);
                    break;

                case East:
                    decal.setPosition(decalCustom.getCol() + COLLISION_RADIUS, decalCustom.getRow(), 0);
                    decal.rotateX(90);
                    decal.rotateY(90);
                    break;
            }
        }
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

    private boolean hasCollision(float col, float row, Side side) {

        switch (side) {

            case North:
                return (getDistance(col, row, getCol(), getRow() + COLLISION_RADIUS) < COLLISION);

            case South:
                return (getDistance(col, row, getCol(), getRow() - COLLISION_RADIUS) < COLLISION);

            case West:
                return (getDistance(col, row, getCol() - COLLISION_RADIUS, getRow()) < COLLISION);

            case East:
                return (getDistance(col, row, getCol() + COLLISION_RADIUS, getRow()) < COLLISION);
        }

        return false;
    }

    @Override
    public void dispose() {
        if (this.decalAnimation != null)
            this.decalAnimation.dispose();

        this.decalAnimation = null;
    }

    @Override
    public void reset() {

        //reset the animation
        getAnimation().reset();
    }

    public void render(PerspectiveCamera camera, DecalBatch batch) {
        getAnimation().render(camera, batch);
    }
}