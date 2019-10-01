package com.gamesbykevin.havoc.level;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.gamesbykevin.havoc.decals.DecalCustom;
import com.gamesbykevin.havoc.decals.DecalCustom.Type;
import com.gamesbykevin.havoc.maze.Maze;
import com.gamesbykevin.havoc.maze.Room;

import static com.gamesbykevin.havoc.decals.Background.createDecalBackground;
import static com.gamesbykevin.havoc.decals.DecalCustom.*;

public class LevelHelper {

    //how big is each room
    public static final int ROOM_SIZE = 8;

    //render decals within the specified range
    public static final int RENDER_RANGE = 30;

    //chance we add a door or secret
    public static final float DOOR_PROBABILITY = .7f;

    //how many tiles can we choose from for the floor ceiling?
    public static final int TILES_FLOOR_CEILING = 27;

    //how many tiles can we choose from for the walls
    public static final int TILES_WALL = 152;

    //how deep is the door placed
    public static final float DOOR_DEPTH = .5f;

    //how deep is the secret placed
    public static final float SECRET_DEPTH = .1f;

    //how to access the door
    public static final String PATH_DOOR = "door/door.bmp";
    public static final String PATH_SIDE = "door/door_side.bmp";

    private static TextureRegion getTextureRegion(String path) {
        return new TextureRegion(new Texture(Gdx.files.internal(path)));
    }

    private static TextureRegion getRandomBackground() {
        return getCeiling(Maze.getRandom().nextInt(TILES_FLOOR_CEILING) + 1);
    }

    private static TextureRegion getCeiling(int index) {
        return getTextureRegion("floor_ceiling/tile" + index + ".bmp");
    }

    private static TextureRegion getRandomWall() {
        return getWall(Maze.getRandom().nextInt(TILES_WALL) + 1);
    }

    private static TextureRegion getWall(int index) {
        return getTextureRegion("walls/tile (" + index + ").bmp");
    }

    protected static void createDecals(Level level) {

        TextureRegion textureRegionCeiling = getRandomBackground();
        TextureRegion textureRegionFloor = getRandomBackground();

        for (int col = 0; col < level.getMaze().getCols(); col++) {
            for (int row = 0; row < level.getMaze().getRows(); row++) {

                Room room = level.getMaze().getRoom(col, row);

                int roomColStart = ROOM_SIZE * col;
                int roomRowStart = ROOM_SIZE * row;

                TextureRegion wall = getRandomWall();

                for (int roomRow = roomRowStart; roomRow < roomRowStart + ROOM_SIZE; roomRow++) {

                    if (room.hasWest()) {
                        addWall(level, Side.East, Type.Wall, wall, roomColStart, roomRow, false);

                        if (roomRow == roomRowStart) {
                            addWall(level, Side.North, Type.Wall, wall, roomColStart, roomRow, false);
                            addWall(level, Side.South, Type.Wall, wall, roomColStart, roomRow, false);
                        }
                    }

                    if (room.hasEast()) {
                        addWall(level, Side.West, Type.Wall, wall, roomColStart + ROOM_SIZE, roomRow, false);

                        if (room.hasEast() && roomRow == roomRowStart + ROOM_SIZE - 1) {
                            addWall(level, Side.North, Type.Wall, wall, roomColStart + ROOM_SIZE, roomRow, false);
                            addWall(level, Side.South, Type.Wall, wall, roomColStart + ROOM_SIZE, roomRow, false);
                        }
                    }
                }

                for (int roomCol = roomColStart; roomCol < roomColStart + ROOM_SIZE; roomCol++) {

                    if (room.hasSouth()) {
                        addWall(level, Side.North, Type.Wall, wall, roomCol, roomRowStart, false);

                        if (roomCol == roomColStart) {
                            addWall(level, Side.West, Type.Wall, wall, roomCol, roomRowStart, false);
                            addWall(level, Side.East, Type.Wall, wall, roomCol, roomRowStart, false);
                        }
                    }

                    if (room.hasNorth()) {
                        addWall(level, Side.South, Type.Wall, wall, roomCol, roomRowStart + ROOM_SIZE, false);

                        if (roomCol == roomColStart + ROOM_SIZE - 1) {
                            addWall(level, Side.West, Type.Wall, wall, roomCol, roomRowStart + ROOM_SIZE, false);
                            addWall(level, Side.East, Type.Wall, wall, roomCol, roomRowStart + ROOM_SIZE, false);
                        }
                    }
                }

                //add floor and ceiling
                for (int roomRow = roomRowStart; roomRow <= roomRowStart + ROOM_SIZE; roomRow++) {
                    for (int roomCol = roomColStart; roomCol <= roomColStart + ROOM_SIZE; roomCol++) {
                        level.getDecals().add(createDecalBackground(roomCol - (WALL_WIDTH / 2), roomRow, textureRegionFloor, true));
                        level.getDecals().add(createDecalBackground(roomCol - (WALL_WIDTH / 2), roomRow, textureRegionCeiling, false));
                    }
                }

                //textures for the doors
                TextureRegion door = getTextureRegion(PATH_DOOR);
                TextureRegion side = getTextureRegion(PATH_SIDE);

                if (!room.hasSouth() && Math.random() <= DOOR_PROBABILITY) {
                    if (room.hasWest() && room.hasEast() && room.hasNorth()) {
                        addDoorSouth(level, wall, side, door, roomColStart, roomRowStart, true);
                    } else {
                        addDoorSouth(level, wall, side, door, roomColStart, roomRowStart, false);
                    }
                }

                if (!room.hasWest() && Math.random() <= DOOR_PROBABILITY) {
                    if (room.hasEast() && room.hasNorth() && room.hasSouth()) {
                        addDoorWest(level, wall, side, door, roomColStart, roomRowStart, true);
                    } else {
                        addDoorWest(level, wall, side, door, roomColStart, roomRowStart, false);
                    }
                }
            }
        }
    }

    private static void addDoorWest(Level level, TextureRegion wall, TextureRegion side, TextureRegion door, int roomColStart, int roomRowStart, boolean secret) {

        int middle = roomRowStart + (ROOM_SIZE / 2);

        for (int roomRow = roomRowStart; roomRow < roomRowStart + ROOM_SIZE; roomRow++) {

            if (roomRow == middle) {
                addWall(level, Side.West, Type.Door, (secret) ? wall : door, roomColStart, roomRow, secret);
                //addWall(level, Side.East, Type.Door, (secret) ? wall : door, roomColStart, roomRow, secret);
            } else if (roomRow == middle - 1 || roomRow == middle + 1) {
                addWall(level, Side.West, Type.Wall, wall, roomColStart, roomRow, false);
                addWall(level, Side.East, Type.Wall, wall, roomColStart, roomRow, false);
                addWall(level, Side.North, Type.Wall, side, roomColStart, roomRow, false);
                addWall(level, Side.South, Type.Wall, side, roomColStart, roomRow, false);
            } else if (roomRow == roomRowStart || roomRow == roomRowStart + ROOM_SIZE - 1) {
                addWall(level, Side.East, Type.Wall, wall, roomColStart, roomRow, false);
                addWall(level, Side.West, Type.Wall, wall, roomColStart, roomRow, false);
                addWall(level, Side.North, Type.Wall, wall, roomColStart, roomRow, false);
                addWall(level, Side.South, Type.Wall, wall, roomColStart, roomRow, false);
            } else {
                addWall(level, Side.West, Type.Wall, wall, roomColStart, roomRow, false);
                addWall(level, Side.East, Type.Wall, wall, roomColStart, roomRow, false);
            }
        }
    }

    private static void addDoorSouth(Level level, TextureRegion wall, TextureRegion side, TextureRegion door, int roomColStart, int roomRowStart, boolean secret) {

        int middle = roomColStart + (ROOM_SIZE / 2);

        for (int roomCol = roomColStart; roomCol < roomColStart + ROOM_SIZE; roomCol++) {

            if (roomCol == middle) {
                //addWall(level, Side.North, Type.Door, (secret) ? wall : door, roomCol, roomRowStart, secret);
                addWall(level, Side.South, Type.Door, (secret) ? wall : door, roomCol, roomRowStart, secret);
            } else if (roomCol == middle - 1 || roomCol == middle + 1) {
                addWall(level, Side.North, Type.Wall, wall, roomCol, roomRowStart, false);
                addWall(level, Side.South, Type.Wall, wall, roomCol, roomRowStart, false);
                addWall(level, Side.West, Type.Wall, side, roomCol, roomRowStart, false);
                addWall(level, Side.East, Type.Wall, side, roomCol, roomRowStart, false);
            } else if (roomCol == roomColStart || roomCol == roomColStart + ROOM_SIZE - 1) {
                addWall(level, Side.North, Type.Wall, wall, roomCol, roomRowStart, false);
                addWall(level, Side.South, Type.Wall, wall, roomCol, roomRowStart, false);
                addWall(level, Side.West, Type.Wall, wall, roomCol, roomRowStart, false);
                addWall(level, Side.East, Type.Wall, wall, roomCol, roomRowStart, false);
            } else {
                addWall(level, Side.North, Type.Wall, wall, roomCol, roomRowStart, false);
                addWall(level, Side.South, Type.Wall, wall, roomCol, roomRowStart, false);
            }
        }
    }

    private static void addWall(Level level, Side side, Type type, TextureRegion textureRegion, final float col, final float row, boolean secret) {

        //add decal to be rendered
        switch (type) {
            case Wall:
                level.getDecals().add(DecalCustom.createDecalWall(col, row, textureRegion, side));

                //flag wall here
                level.getWalls()[(int)row][(int)col] = true;
                break;

            case Door:
                level.getDoorDecals()[(int)row][(int)col] = DecalCustom.createDecalDoor(col, row, textureRegion, side, secret);

                //flag door here
                level.getDoors()[(int)row][(int)col] = true;
                break;
        }
    }
}