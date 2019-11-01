package com.gamesbykevin.havoc.level;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.gamesbykevin.havoc.decals.Background;
import com.gamesbykevin.havoc.decals.DecalCustom.Side;
import com.gamesbykevin.havoc.decals.DecalCustom.Type;
import com.gamesbykevin.havoc.maze.Maze;
import com.gamesbykevin.havoc.maze.Room;

import java.util.ArrayList;
import java.util.List;

import static com.gamesbykevin.havoc.decals.Background.createDecalBackground;
import static com.gamesbykevin.havoc.level.LevelHelper.addWall;
import static com.gamesbykevin.havoc.level.RoomHelper.ROOM_SIZE;
import static com.gamesbykevin.havoc.maze.Maze.getRandom;

public class TextureHelper {

    //locations of some of our textures
    public static final String PATH_DOOR = "door/door.bmp";
    public static final String PATH_DOOR_LOCKED = "door/door_locked_1.bmp";
    public static final String PATH_SIDE = "door/door_side.bmp";
    public static final String PATH_DOOR_GOAL = "goal/door.bmp";
    public static final String PATH_WALL_GOAL = "goal/wall.bmp";
    public static final String PATH_SWITCH_OFF = "goal/switch_off.bmp";
    public static final String PATH_SWITCH_ON = "goal/switch_on.bmp";

    private static TextureRegion TEXTURE_DOOR;
    private static TextureRegion TEXTURE_DOOR_LOCKED;
    private static TextureRegion TEXTURE_SIDE;

    //how many tiles can we choose from for the floor ceiling?
    public static final int TILES_BACKGROUND_LIGHT = 29;
    public static final int TILES_BACKGROUND_DARK = 44;

    //how many tiles can we choose from for the walls
    public static final int TILES_WALL = 233;

    //chance we add a secret door
    public static final float DOOR_PROBABILITY = 0.7f;

    private static TextureRegion getTextureRegion(String path) {
        return new TextureRegion(new Texture(Gdx.files.internal(path)));
    }

    protected static TextureRegion getTextureDoorLocked() {

        if (TEXTURE_DOOR_LOCKED == null)
            TEXTURE_DOOR_LOCKED = getTextureRegion(PATH_DOOR_LOCKED);

        return TEXTURE_DOOR_LOCKED;
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

    private static TextureRegion getBackgroundRandomLight() {
        return getBackgroundLight(Maze.getRandom().nextInt(TILES_BACKGROUND_LIGHT));
    }

    private static TextureRegion getBackgroundLight(int index) {
        return getTextureRegion("background/light/tile" + index + ".bmp");
    }

    private static TextureRegion getBackgroundRandomDark() {
        return getBackgroundDark(Maze.getRandom().nextInt(TILES_BACKGROUND_DARK));
    }

    private static TextureRegion getBackgroundDark(int index) {
        return getTextureRegion("background/dark/tile" + index + ".bmp");
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

    protected static void addTextures(Level level) {

        List<Integer> walls = new ArrayList<>();
        for (int i = 1; i < TILES_WALL; i++) {
            walls.add(i);
        }

        for (int col = 0; col < level.getMaze().getCols(); col++) {

            for (int row = 0; row < level.getMaze().getRows(); row++) {

                //get the current room
                Room room = level.getMaze().getRoom(col, row);

                if (col == level.getMaze().getStartCol() && row == level.getMaze().getStartRow())
                    room = null;

                //is this room a goal
                boolean goal = (col == level.getMaze().getGoalCol() && row == level.getMaze().getGoalRow());

                //where do we start in this room
                int startCol = col * ROOM_SIZE;
                int startRow = row * ROOM_SIZE;

                //texture for the wall
                TextureRegion texture;

                //pick random texture
                if (!goal) {
                    int index = Maze.getRandom().nextInt(walls.size());
                    int indexWall = walls.get(index);
                    texture = getWall(indexWall);
                    walls.remove(index);
                } else {
                    texture = getWallGoal();
                }

                addTextures(level, startCol, startRow, texture, room, goal);
            }
        }

        walls.clear();
        walls = null;
    }

    private static void addTextures(Level level, int startCol, int startRow, TextureRegion texture, Room room, boolean goal) {

        for (int col = startCol; col < startCol + ROOM_SIZE; col++) {
            for (int row = startRow; row < startRow + ROOM_SIZE; row++) {

                boolean wallN = level.hasWall(col, row + 1);
                boolean wallE = level.hasWall(col + 1, row);
                boolean wallS = level.hasWall(col, row - 1);
                boolean wallW = level.hasWall(col - 1, row);

                boolean doorN = level.hasDoor(col, row + 1);
                boolean doorE = level.hasDoor(col + 1, row);
                boolean doorS = level.hasDoor(col, row - 1);
                boolean doorW = level.hasDoor(col - 1, row);

                boolean freeN = level.hasFree(col, row + 1);
                boolean freeE = level.hasFree(col + 1, row);
                boolean freeS = level.hasFree(col, row - 1);
                boolean freeW = level.hasFree(col - 1, row);

                if (level.hasWall(col, row)) {

                    if (goal && col == startCol + (ROOM_SIZE / 2) && row == startRow + (ROOM_SIZE / 2)) {

                        TextureRegion textureRegion = getSwitchGoal();
                        addWall(level, Side.North, Type.Wall, textureRegion, col, row, false);
                        addWall(level, Side.South, Type.Wall, textureRegion, col, row, false);
                        addWall(level, Side.West, Type.Wall, textureRegion, col, row, false);
                        addWall(level, Side.East, Type.Wall, textureRegion, col, row, false);

                    } else {

                        if (!wallN && !doorN && freeN)
                            addWall(level, Side.North, Type.Wall, texture, col, row, false);
                        if (!wallW && !doorW && freeW)
                            addWall(level, Side.West, Type.Wall, texture, col, row, false);
                        if (!wallS && !doorS && freeS)
                            addWall(level, Side.South, Type.Wall, texture, col, row, false);
                        if (!wallE && !doorE && freeE)
                            addWall(level, Side.East, Type.Wall, texture, col, row, false);

                        if (doorN)
                            addWall(level, Side.North, Type.Wall, getTextureSide(), col, row, false);
                        if (doorS)
                            addWall(level, Side.South, Type.Wall, getTextureSide(), col, row, false);
                        if (doorW)
                            addWall(level, Side.West, Type.Wall, getTextureSide(), col, row, false);
                        if (doorE)
                            addWall(level, Side.East, Type.Wall, getTextureSide(), col, row, false);
                    }

                } else if (level.hasDoor(col, row)) {

                    boolean secret = false;

                    //if this isn't the goal, let's see if we can make the door a secret
                    if (!goal && (col == startCol || col == startCol + ROOM_SIZE - 1 || row == startRow || row == startRow + ROOM_SIZE - 1))
                        secret = hasSecret(room);

                    if (secret) {

                        if (wallS && wallN)
                            addWall(level, (!room.hasEast()) ? Side.East : Side.West, Type.Door, texture, col, row, true);
                        if (wallW && wallE)
                            addWall(level, (!room.hasNorth()) ? Side.North : Side.South, Type.Door, texture, col, row, true);

                    } else {

                        TextureRegion textureRegion;

                        if (goal) {
                            textureRegion = getDoorGoal();
                        } else {
                            if (level.hasLocked(col, row)) {
                                textureRegion = getTextureDoorLocked();
                            } else {
                                textureRegion = getTextureDoor();
                            }
                        }

                        if (wallS && wallN)
                            addWall(level, Side.East, Type.Door, textureRegion, col, row, false);
                        if (wallW && wallE)
                            addWall(level, Side.South, Type.Door, textureRegion, col, row, false);
                    }
                }
            }
        }
    }

    private static boolean hasSecret(Room room) {

        //if not exist, return false
        if (room == null)
            return false;

        boolean secret = false;
        int count = 0;

        //count the number of walls
        if (room.hasSouth())
            count++;
        if (room.hasWest())
            count++;
        if (room.hasEast())
            count++;
        if (room.hasNorth())
            count++;

        //if there are 3 walls decide at random if secret
        if (count == 3)
            secret = (getRandom().nextFloat() < DOOR_PROBABILITY);

        if (secret)
            System.out.println("col=" + room.getCol() + ", row=" + room.getRow());

        return secret;
    }

    //add floors and ceiling
    protected static void addBackground(Level level) {

        //texture for top and bottom
        TextureRegion ceiling, floor;

        //choose the floor and ceiling at random
        if (Maze.getRandom().nextBoolean()) {
            ceiling = getBackgroundRandomDark();
            floor = getBackgroundRandomLight();
        } else {
            ceiling = getBackgroundRandomLight();
            floor = getBackgroundRandomDark();
        }

        int cols = (int)((level.getMaze().getCols() * ROOM_SIZE) / Background.TEXTURE_WIDTH) + 1;
        int rows = (int)((level.getMaze().getRows() * ROOM_SIZE) / Background.TEXTURE_HEIGHT) + 1;

        for (int col = 0; col < cols; col++) {
            for (int row = 0; row < rows; row++) {
                level.getBackgrounds().add(createDecalBackground(col * Background.TEXTURE_WIDTH, row * Background.TEXTURE_HEIGHT, floor, true));
                level.getBackgrounds().add(createDecalBackground(col * Background.TEXTURE_WIDTH, row * Background.TEXTURE_HEIGHT, ceiling, false));
            }
        }
    }
}