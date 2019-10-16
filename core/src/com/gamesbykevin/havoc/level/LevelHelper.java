package com.gamesbykevin.havoc.level;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.gamesbykevin.havoc.decals.Background;
import com.gamesbykevin.havoc.decals.DecalCustom;
import com.gamesbykevin.havoc.decals.DecalCustom.Type;
import com.gamesbykevin.havoc.maze.Maze;
import com.gamesbykevin.havoc.maze.Room;

import static com.gamesbykevin.havoc.decals.Background.createDecalBackground;
import static com.gamesbykevin.havoc.decals.DecalCustom.*;
import static com.gamesbykevin.havoc.level.RoomHelper.*;
import static com.gamesbykevin.havoc.level.TextureHelper.addBackground;
import static com.gamesbykevin.havoc.level.TextureHelper.addTextures;
import static com.gamesbykevin.havoc.maze.Maze.*;

public class LevelHelper {

    //render decals within the specified range
    public static final int RENDER_RANGE = (int)(ROOM_SIZE * 1.5);

    //how deep is the door placed
    public static final float DOOR_DEPTH = .5f;

    //how deep is the secret placed
    public static final float SECRET_DEPTH = .075f;

    protected static void createDecals(Level level) {

        for (int col = 0; col < level.getMaze().getCols(); col++) {
            for (int row = 0; row < level.getMaze().getRows(); row++) {

                //is this room the goal?
                boolean goal = (col == level.getMaze().getGoalCol() && row == level.getMaze().getGoalRow());

                //get the current room
                Room room = level.getMaze().getRoom(col, row);

                //where does the room start
                int roomColStart = ROOM_SIZE * col;
                int roomRowStart = ROOM_SIZE * row;

                if (goal) {

                    //add switch in the middle of the room
                    level.setWall(roomColStart + (ROOM_SIZE / 2), roomRowStart + (ROOM_SIZE / 2), true);

                    //add walls around the room
                    addEmptyRoom(level, room, roomColStart, roomRowStart);

                    //any opening in the goal will have a door
                    if (!room.hasWest())
                        createDoorVertical(level, roomColStart, roomRowStart);
                    if (!room.hasEast())
                        createDoorVertical(level, roomColStart + ROOM_SIZE - 1, roomRowStart);
                    if (!room.hasNorth())
                        createDoorHorizontal(level, roomColStart, roomRowStart + ROOM_SIZE - 1);
                    if (!room.hasSouth())
                        createDoorHorizontal(level, roomColStart, roomRowStart);

                } else {

                    if (Maze.getRandom().nextBoolean()) {
                        addMiniRooms(level, room, roomColStart, roomRowStart);
                    } else if (Maze.getRandom().nextBoolean() || Maze.getRandom().nextBoolean()) {
                        addHallways(level, room, roomColStart, roomRowStart);
                    } else {
                        addEmptyRoom(level, room, roomColStart, roomRowStart);
                    }

                    //if there is an opening and the neighbor room isn't the goal
                    if (!room.hasNorth() && (row + 1 != level.getMaze().getGoalRow() || col != level.getMaze().getGoalCol()))
                        createDoorHorizontal(level, roomColStart, roomRowStart + ROOM_SIZE - 1);

                    //if there is an opening and the neighbor room isn't the goal
                    if (!room.hasEast() && (row != level.getMaze().getGoalRow() || col + 1 != level.getMaze().getGoalCol()))
                        createDoorVertical(level, roomColStart + ROOM_SIZE - 1, roomRowStart);
                }
            }
        }

        //check for free space
        checkFreeSpace(level);

        //add wall textures to the level
        addTextures(level);

        //add floor / ceiling
        //addBackground(level);


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
                    addEnemy(level, roomColStart, roomRowStart);
                    addEnemy(level, roomColStart, roomRowStart);
                    addEnemy(level, roomColStart, roomRowStart);
                    //addObstacle(level, roomColStart, roomRowStart);
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