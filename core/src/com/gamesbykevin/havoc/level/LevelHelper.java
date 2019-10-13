package com.gamesbykevin.havoc.level;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.gamesbykevin.havoc.decals.Background;
import com.gamesbykevin.havoc.decals.DecalCustom;
import com.gamesbykevin.havoc.decals.DecalCustom.Type;
import com.gamesbykevin.havoc.maze.Maze;
import com.gamesbykevin.havoc.maze.Room;

import static com.gamesbykevin.havoc.decals.Background.createDecalBackground;
import static com.gamesbykevin.havoc.decals.DecalCustom.*;
import static com.gamesbykevin.havoc.level.RoomHelper.*;
import static com.gamesbykevin.havoc.maze.Maze.getRandom;

public class LevelHelper {

    //render decals within the specified range
    public static final int RENDER_RANGE = 50;

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

    private static TextureRegion TEXTURE_DOOR;
    private static TextureRegion TEXTURE_SIDE;

    private static TextureRegion getTextureRegion(String path) {
        return new TextureRegion(new Texture(Gdx.files.internal(path)));
    }

    private static TextureRegion getRandomBackground() {
        return getCeiling(getRandom().nextInt(TILES_FLOOR_CEILING) + 1);
    }

    protected static TextureRegion getTextureDoor() {

        if (TEXTURE_DOOR == null)
            TEXTURE_DOOR = getTextureRegion(PATH_DOOR);

        return TEXTURE_DOOR;
    }

    protected static TextureRegion getTextureSide() {

        if (TEXTURE_SIDE == null)
            TEXTURE_SIDE = getTextureRegion(PATH_SIDE);

        return TEXTURE_SIDE;
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

                //the goal will have a unique wall
                TextureRegion wall = (goal) ? textureRegionWallGoal : getRandomWall();

                if (goal) {

                    //add our switch in the middle of the room
                    addHorizontal(level, textureRegionSwitchGoal, roomColStart + (ROOM_SIZE / 2), roomRowStart + (ROOM_SIZE / 2));
                    addVertical(level, textureRegionSwitchGoal, roomColStart + (ROOM_SIZE / 2), roomRowStart + (ROOM_SIZE / 2));

                    //add walls around the room
                    addEmptyRoom(level, room, wall, roomColStart, roomRowStart);

                    //any opening in the goal will have a door
                    if (!room.hasWest())
                        createDoorVertical(level, wall, getDoorGoal(), roomColStart, roomRowStart);
                    if (!room.hasEast())
                        createDoorVertical(level, wall, getDoorGoal(), roomColStart + ROOM_SIZE - 1, roomRowStart);
                    if (!room.hasNorth())
                        createDoorHorizontal(level, wall, getDoorGoal(), roomColStart, roomRowStart + ROOM_SIZE - 1);
                    if (!room.hasSouth())
                        createDoorHorizontal(level, wall, getDoorGoal(), roomColStart, roomRowStart);

                } else {

                    //decide at random how the current room is going to look
                    if (Maze.getRandom().nextBoolean()) {
                        addMiniRooms(level, room, wall, roomColStart, roomRowStart);
                    } else if (Maze.getRandom().nextBoolean()) {
                        addHallways(level, room, wall, roomColStart, roomRowStart);
                    } else {
                        addEmptyRoom(level, room, wall, roomColStart, roomRowStart);
                    }

                    if (!room.hasNorth()) {
                        if (row + 1 != level.getMaze().getGoalRow() || col != level.getMaze().getGoalCol())
                            createDoorHorizontal(level, wall, getTextureDoor(), roomColStart, roomRowStart + ROOM_SIZE - 1);
                    }

                    if (!room.hasEast()) {
                        if (row != level.getMaze().getGoalRow() || col + 1 != level.getMaze().getGoalCol())
                            createDoorVertical(level, wall, getTextureDoor(), roomColStart + ROOM_SIZE - 1, roomRowStart);
                    }
                }
            }
        }

        //add floors and ceiling
        for (int col = 0; col <= (level.getMaze().getCols() * ROOM_SIZE) + ROOM_SIZE; col += Background.TEXTURE_WIDTH) {
            for (int row = 0; row <= (level.getMaze().getRows() * ROOM_SIZE) + ROOM_SIZE; row += Background.TEXTURE_HEIGHT) {
                //level.getDecals().add(createDecalBackground(col, row, textureRegionFloor, true));
                //level.getDecals().add(createDecalBackground(col, row, textureRegionCeiling, false));
            }
        }

        //spawn enemies etc... in the rooms
        for (int col = 0; col < level.getMaze().getCols(); col++) {
            for (int row = 0; row < level.getMaze().getRows(); row++) {

                //where does the room start
                int roomColStart = ROOM_SIZE * col;
                int roomRowStart = ROOM_SIZE * row;

                boolean start = (col == level.getMaze().getStartCol() && row == level.getMaze().getStartRow());
                boolean goal = (col == level.getMaze().getGoalCol() && row == level.getMaze().getGoalRow());

                //add for each room but avoid the start and goal
                if (!start && !goal) {
                    //addEnemy(level, roomColStart, roomRowStart);
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

    protected static void addWall(Level level, Side side, Type type, TextureRegion textureRegion, final float col, final float row, boolean secret) {

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