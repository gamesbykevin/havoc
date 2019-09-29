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
    public static final int ROOM_SIZE = 7;

    //how many tiles can we choose from for the floor ceiling?
    public static final int TILES_FLOOR_CEILING = 27;

    //how many tiles can we choose from for the walls
    public static final int TILES_WALL = 152;

    //how deep is the door placed
    public static final float DOOR_DEPTH = .5f;

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

        //default to false
        for (int row = 0 ; row < level.getBounds().length; row++) {
            for (int col = 0; col < level.getBounds()[0].length; col++) {
                level.getBounds()[row][col] = Type.Nothing;
            }
        }

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
                        addWall(level, Side.East, Type.Wall, wall, roomColStart, roomRow);

                        if (roomRow == roomRowStart) {
                            addWall(level, Side.North, Type.Wall, wall, roomColStart, roomRow);
                            addWall(level, Side.South, Type.Wall, wall, roomColStart, roomRow);
                        }
                    }

                    if (room.hasEast()) {
                        addWall(level, Side.West, Type.Wall, wall, roomColStart + ROOM_SIZE, roomRow);

                        if (room.hasEast() && roomRow == roomRowStart + ROOM_SIZE - 1) {
                            addWall(level, Side.North, Type.Wall, wall, roomColStart + ROOM_SIZE, roomRow);
                            addWall(level, Side.South, Type.Wall, wall, roomColStart + ROOM_SIZE, roomRow);
                        }
                    }
                }

                for (int roomCol = roomColStart; roomCol < roomColStart + ROOM_SIZE; roomCol++) {

                    if (room.hasSouth()) {
                        addWall(level, Side.North, Type.Wall, wall, roomCol, roomRowStart);

                        if (roomCol == roomColStart) {
                            addWall(level, Side.West, Type.Wall, wall, roomCol, roomRowStart);
                            addWall(level, Side.East, Type.Wall, wall, roomCol, roomRowStart);
                        }
                    }

                    if (room.hasNorth()) {
                        addWall(level, Side.South, Type.Wall, wall, roomCol, roomRowStart + ROOM_SIZE);

                        if (roomCol == roomColStart + ROOM_SIZE - 1) {
                            addWall(level, Side.West, Type.Wall, wall, roomCol, roomRowStart + ROOM_SIZE);
                            addWall(level, Side.East, Type.Wall, wall, roomCol, roomRowStart + ROOM_SIZE);
                        }
                    }
                }

                for (int roomRow = roomRowStart; roomRow <= roomRowStart + ROOM_SIZE; roomRow++) {
                    for (int roomCol = roomColStart; roomCol <= roomColStart + ROOM_SIZE; roomCol++) {
                        level.getBackgroundDecals().add(createDecalBackground(roomCol - (WALL_WIDTH / 2), roomRow, textureRegionFloor, true));
                        level.getBackgroundDecals().add(createDecalBackground(roomCol - (WALL_WIDTH / 2), roomRow, textureRegionCeiling, false));
                    }
                }

                //textures for the doors
                TextureRegion door = getTextureRegion(PATH_DOOR);
                TextureRegion side = getTextureRegion(PATH_SIDE);

                if (!room.hasSouth() && Math.random() > .5) {

                    int middle = roomColStart + (ROOM_SIZE / 2) + 1;

                    for (int roomCol = roomColStart; roomCol < roomColStart + ROOM_SIZE; roomCol++) {

                        if (roomCol == middle) {
                            addWall(level, Side.North, Type.Door, door, roomCol, roomRowStart);
                            addWall(level, Side.South, Type.Door, door, roomCol, roomRowStart);
                        } else if (roomCol == middle - 1 || roomCol == middle + 1) {
                            addWall(level, Side.North, Type.Wall, wall, roomCol, roomRowStart);
                            addWall(level, Side.South, Type.Wall, wall, roomCol, roomRowStart);
                            addWall(level, Side.West, Type.Wall, side, roomCol, roomRowStart);
                            addWall(level, Side.East, Type.Wall, side, roomCol, roomRowStart);
                        } else if (roomCol == roomColStart || roomCol == roomColStart + ROOM_SIZE - 1) {
                            addWall(level, Side.North, Type.Wall, wall, roomCol, roomRowStart);
                            addWall(level, Side.South, Type.Wall, wall, roomCol, roomRowStart);
                            addWall(level, Side.West, Type.Wall, wall, roomCol, roomRowStart);
                            addWall(level, Side.East, Type.Wall, wall, roomCol, roomRowStart);
                        } else {
                            addWall(level, Side.North, Type.Wall, wall, roomCol, roomRowStart);
                            addWall(level, Side.South, Type.Wall, wall, roomCol, roomRowStart);
                        }
                    }
                }

                if (!room.hasWest() && Math.random() > .5) {

                    int middle = roomRowStart + (ROOM_SIZE / 2) + 1;

                    for (int roomRow = roomRowStart; roomRow < roomRowStart + ROOM_SIZE; roomRow++) {

                        if (roomRow == middle) {
                            addWall(level, Side.West, Type.Door, door, roomColStart, roomRow);
                            addWall(level, Side.East, Type.Door, door, roomColStart, roomRow);
                        } else if (roomRow == middle - 1 || roomRow == middle + 1) {
                            addWall(level, Side.West, Type.Wall, wall, roomColStart, roomRow);
                            addWall(level, Side.East, Type.Wall, wall, roomColStart, roomRow);
                            addWall(level, Side.North, Type.Wall, side, roomColStart, roomRow);
                            addWall(level, Side.South, Type.Wall, side, roomColStart, roomRow);
                        } else if (roomRow == roomRowStart || roomRow == roomRowStart + ROOM_SIZE - 1) {
                            addWall(level, Side.East, Type.Wall, wall, roomColStart, roomRow);
                            addWall(level, Side.West, Type.Wall, wall, roomColStart, roomRow);
                            addWall(level, Side.North, Type.Wall, wall, roomColStart, roomRow);
                            addWall(level, Side.South, Type.Wall, wall, roomColStart, roomRow);
                        } else {
                            addWall(level, Side.West, Type.Wall, wall, roomColStart, roomRow);
                            addWall(level, Side.East, Type.Wall, wall, roomColStart, roomRow);
                        }
                    }
                }
            }
        }
    }

    private static void addWall(Level level, Side side, Type type, TextureRegion textureRegion, final float col, final float row) {

        //flag the bounds
        level.getBounds()[(int)row][(int)col] = type;

        //add decal to be rendered
        switch (type) {
            case Wall:
                level.getWallDecals().add(DecalCustom.createDecalWall(col, row, textureRegion, side));
                break;

            case Door:
                level.getDoorDecals().add(DecalCustom.createDecalDoor(col, row, textureRegion, side));
                break;
        }
    }
}