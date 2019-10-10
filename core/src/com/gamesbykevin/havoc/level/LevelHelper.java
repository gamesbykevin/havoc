package com.gamesbykevin.havoc.level;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.gamesbykevin.havoc.decals.Background;
import com.gamesbykevin.havoc.decals.DecalCustom;
import com.gamesbykevin.havoc.decals.DecalCustom.Type;
import com.gamesbykevin.havoc.enemies.Enemy;
import com.gamesbykevin.havoc.maze.Room;

import static com.gamesbykevin.havoc.decals.Background.createDecalBackground;
import static com.gamesbykevin.havoc.decals.DecalCustom.*;
import static com.gamesbykevin.havoc.maze.Maze.getRandom;

public class LevelHelper {

    //how big is each room
    public static final int ROOM_SIZE = 12;

    //render decals within the specified range
    public static final int RENDER_RANGE = 30;

    //chance we add a door or secret
    public static final float DOOR_PROBABILITY = .6f;

    //how many tiles can we choose from for the floor ceiling?
    public static final int TILES_FLOOR_CEILING = 73;

    //how many tiles can we choose from for the walls
    public static final int TILES_WALL = 180;

    //how deep is the door placed
    public static final float DOOR_DEPTH = .5f;

    //how deep is the secret placed
    public static final float SECRET_DEPTH = .075f;

    //locations of some of our textures
    public static final String PATH_DOOR = "door/door.bmp";
    public static final String PATH_SIDE = "door/door_side.bmp";
    public static final String PATH_DOOR_GOAL = "goal/door.bmp";
    public static final String PATH_WALL_GOAL = "goal/wall.bmp";
    public static final String PATH_SWITCH_OFF = "goal/switch_off.bmp";
    public static final String PATH_SWITCH_ON = "goal/switch_on.bmp";

    private static TextureRegion getTextureRegion(String path) {
        return new TextureRegion(new Texture(Gdx.files.internal(path)));
    }

    private static TextureRegion getRandomBackground() {
        return getCeiling(getRandom().nextInt(TILES_FLOOR_CEILING) + 1);
    }

    private static TextureRegion getCeiling(int index) {
        return getTextureRegion("floor_ceiling/tile" + index + ".bmp");
    }

    private static TextureRegion getRandomWall() {
        return getWall(getRandom().nextInt(TILES_WALL) + 1);
    }

    private static TextureRegion getWall(int index) {
        return getTextureRegion("walls/tile (" + index + ").bmp");
    }

    private static TextureRegion getWallGoal() {
        return getTextureRegion(PATH_WALL_GOAL);
    }

    private static TextureRegion getDoorGoal() {
        return getTextureRegion(PATH_DOOR_GOAL);
    }

    private static TextureRegion getSwitchGoal() {
        return getTextureRegion(PATH_SWITCH_OFF);
    }

    protected static void createDecals(Level level) {

        //choose the floor and ceiling
        TextureRegion textureRegionCeiling = getRandomBackground();
        TextureRegion textureRegionFloor = getRandomBackground();

        //goal will have specific textures on the walls
        TextureRegion textureRegionWallGoal = getWallGoal();

        //hit the switch to complete the level
        TextureRegion textureRegionSwitchGoal = getSwitchGoal();

        for (int col = 0; col < level.getMaze().getCols(); col++) {
            for (int row = 0; row < level.getMaze().getRows(); row++) {

                //is this room the goal?
                boolean goal = (col == level.getMaze().getGoalCol() && row == level.getMaze().getGoalRow());

                //get the current room
                Room room = level.getMaze().getRoom(col, row);

                //where does the room start
                int roomColStart = ROOM_SIZE * col;
                int roomRowStart = ROOM_SIZE * row;

                TextureRegion wall = (goal) ? textureRegionWallGoal : getRandomWall();

                //if this is the goal room add switch in the middle of room
                if (goal) {
                    addWall(level, Side.North, Type.Wall, textureRegionSwitchGoal, roomColStart + (ROOM_SIZE / 2), roomRowStart + (ROOM_SIZE / 2), false);
                    addWall(level, Side.South, Type.Wall, textureRegionSwitchGoal, roomColStart + (ROOM_SIZE / 2), roomRowStart + (ROOM_SIZE / 2), false);
                    addWall(level, Side.West, Type.Wall, textureRegionSwitchGoal, roomColStart + (ROOM_SIZE / 2), roomRowStart + (ROOM_SIZE / 2), false);
                    addWall(level, Side.East, Type.Wall, textureRegionSwitchGoal, roomColStart + (ROOM_SIZE / 2), roomRowStart + (ROOM_SIZE / 2), false);
                }

                int endRow = roomRowStart + ROOM_SIZE;

                for (int roomRow = roomRowStart; roomRow < endRow; roomRow++) {

                    if (room.hasWest()) {
                        addWall(level, Side.East, Type.Wall, wall, roomColStart, roomRow, false);

                        if (roomRow == roomRowStart) {
                            addWall(level, Side.North, Type.Wall, wall, roomColStart, roomRow, false);
                            addWall(level, Side.South, Type.Wall, wall, roomColStart, roomRow, false);
                        }
                    }

                    if (room.hasEast()) {
                        addWall(level, Side.West, Type.Wall, wall, roomColStart + ROOM_SIZE, roomRow, false);

                        if (roomRow == endRow - 1) {
                            addWall(level, Side.North, Type.Wall, wall, roomColStart + ROOM_SIZE, roomRow, false);
                            addWall(level, Side.South, Type.Wall, wall, roomColStart + ROOM_SIZE, roomRow, false);
                        }
                    }
                }

                int endCol = roomColStart + ROOM_SIZE;

                for (int roomCol = roomColStart; roomCol < endCol; roomCol++) {

                    if (room.hasSouth()) {
                        addWall(level, Side.North, Type.Wall, wall, roomCol, roomRowStart, false);

                        if (roomCol == roomColStart) {
                            addWall(level, Side.West, Type.Wall, wall, roomCol, roomRowStart, false);
                            addWall(level, Side.East, Type.Wall, wall, roomCol, roomRowStart, false);
                        }
                    }

                    if (room.hasNorth()) {
                        addWall(level, Side.South, Type.Wall, wall, roomCol, roomRowStart + ROOM_SIZE, false);

                        if (roomCol == endCol - 1) {
                            addWall(level, Side.West, Type.Wall, wall, roomCol, roomRowStart + ROOM_SIZE, false);
                            addWall(level, Side.East, Type.Wall, wall, roomCol, roomRowStart + ROOM_SIZE, false);
                        }
                    }
                }

                //textures for the doors
                TextureRegion door = (goal) ? getDoorGoal() : getTextureRegion(PATH_DOOR);
                TextureRegion side = getTextureRegion(PATH_SIDE);

                //for the goal every opening will be enclosed by a door
                if (goal) {

                    if (!room.hasSouth())
                        addDoorSouth(level, wall, side, door, roomColStart, roomRowStart, false);
                    if (!room.hasNorth())
                        addDoorSouth(level, wall, side, door, roomColStart, roomRowStart + ROOM_SIZE, false);
                    if (!room.hasWest())
                        addDoorWest(level, wall, side, door, roomColStart, roomRowStart, false);
                    if (!room.hasEast())
                        addDoorWest(level, wall, side, door, roomColStart + ROOM_SIZE, roomRowStart, false);

                } else {

                    if (!room.hasSouth() && Math.random() <= DOOR_PROBABILITY && (row - 1 != level.getMaze().getGoalRow() || col != level.getMaze().getGoalCol())) {
                        if (room.hasWest() && room.hasEast() && room.hasNorth()) {
                            addDoorSouth(level, wall, side, door, roomColStart, roomRowStart, true);
                        } else {
                            addDoorSouth(level, wall, side, door, roomColStart, roomRowStart, false);
                        }
                    }

                    if (!room.hasWest() && Math.random() <= DOOR_PROBABILITY && (row != level.getMaze().getGoalRow() || col - 1 != level.getMaze().getGoalCol())) {
                        if (room.hasEast() && room.hasNorth() && room.hasSouth()) {
                            addDoorWest(level, wall, side, door, roomColStart, roomRowStart, true);
                        } else {
                            addDoorWest(level, wall, side, door, roomColStart, roomRowStart, false);
                        }
                    }
                }
            }
        }

        //add floors and ceiling
        for (int col = 0; col < level.getMaze().getCols() * ROOM_SIZE; col += Background.TEXTURE_WIDTH) {
            for (int row = 0; row < level.getMaze().getRows() * ROOM_SIZE; row += Background.TEXTURE_HEIGHT) {
                level.getDecals().add(createDecalBackground(col, row, textureRegionFloor, true));
                level.getDecals().add(createDecalBackground(col, row, textureRegionCeiling, false));
            }
        }

        //spawn enemies in the rooms
        for (int col = 0; col < level.getMaze().getCols(); col++) {
            for (int row = 0; row < level.getMaze().getRows(); row++) {

                //where does the room start
                int roomColStart = ROOM_SIZE * col;
                int roomRowStart = ROOM_SIZE * row;

                boolean start = (col == level.getMaze().getStartCol() && row == level.getMaze().getStartRow());
                boolean goal = (col == level.getMaze().getGoalCol() && row == level.getMaze().getGoalRow());

                //add for each room but avoid the start and goal
                if (!start && !goal) {
                    addEnemy(level, roomColStart, roomRowStart);
                    addEnemy(level, roomColStart, roomRowStart);
                    addEnemy(level, roomColStart, roomRowStart);
                    addObstacle(level, roomColStart, roomRowStart);
                    //addObstacle(level, roomColStart, roomRowStart);
                    //addObstacle(level, roomColStart, roomRowStart);
                }
            }
        }
    }

    private static void addObstacle(Level level, int roomColStart, int roomRowStart) {
        int randomCol = roomColStart + getRandom().nextInt(ROOM_SIZE - 4) + 2;
        int randomRow = roomRowStart + getRandom().nextInt(ROOM_SIZE - 4) + 2;
        level.getObstacles().add(randomCol, randomRow);
    }

    private static void addEnemy(Level level, int roomColStart, int roomRowStart) {
        int randomCol = roomColStart + getRandom().nextInt(ROOM_SIZE - 4) + 2;
        int randomRow = roomRowStart + getRandom().nextInt(ROOM_SIZE - 4) + 2;
        level.getEnemies().add(randomCol, randomRow);
    }

    private static void addDoorWest(Level level, TextureRegion wall, TextureRegion side, TextureRegion door, int roomColStart, int roomRowStart, boolean secret) {

        int middle = roomRowStart + (ROOM_SIZE / 2);

        for (int roomRow = roomRowStart; roomRow < roomRowStart + ROOM_SIZE; roomRow++) {

            if (roomRow == middle) {
                addWall(level, Side.West, Type.Door, (secret) ? wall : door, roomColStart, roomRow, secret);
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

            //create and flag wall here
            case Wall:
                level.getDecals().add(DecalCustom.createDecalWall(col, row, textureRegion, side));
                level.setWall((int)col, (int)row, true);
                break;

            //create and flag door here
            case Door:
                level.setDoorDecal(DecalCustom.createDecalDoor(col, row, textureRegion, side, secret), (int)col, (int)row);
                level.setDoor((int)col, (int)row, true);
                break;
        }
    }

    public static double getDistance(float x1, float y1, float x2, float y2) {
        return Math.sqrt(Math.pow(x2 - x1, 2) + (Math.pow(y2 - y1, 2)));
    }
}