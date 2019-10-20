package com.gamesbykevin.havoc.level;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.gamesbykevin.havoc.decals.DecalCustom;
import com.gamesbykevin.havoc.decals.DecalCustom.Type;
import com.gamesbykevin.havoc.maze.Location;
import com.gamesbykevin.havoc.maze.Maze;
import com.gamesbykevin.havoc.maze.Room;

import java.util.List;

import static com.gamesbykevin.havoc.decals.DecalCustom.*;
import static com.gamesbykevin.havoc.level.RoomHelper.*;
import static com.gamesbykevin.havoc.level.TextureHelper.addBackground;
import static com.gamesbykevin.havoc.level.TextureHelper.addTextures;
import static com.gamesbykevin.havoc.maze.Maze.*;

public class LevelHelper {

    //render decals within the specified range
    public static final int RENDER_RANGE = (int)(ROOM_SIZE * 2.25);

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
                        createDoorVertical(level, roomColStart, roomRowStart, roomRowStart + ROOM_SIZE);
                    if (!room.hasEast())
                        createDoorVertical(level, roomColStart + ROOM_SIZE - 1, roomRowStart, roomRowStart + ROOM_SIZE);
                    if (!room.hasNorth())
                        createDoorHorizontal(level, roomColStart, roomColStart + ROOM_SIZE, roomRowStart + ROOM_SIZE - 1);
                    if (!room.hasSouth())
                        createDoorHorizontal(level, roomColStart, roomColStart + ROOM_SIZE, roomRowStart);

                } else if (col == level.getMaze().getStartCol() && row == level.getMaze().getStartRow()) {

                    //starting room will be a hallway
                    addHallways(level, room, roomColStart, roomRowStart);

                } else {

                    switch (Maze.getRandom().nextInt(4)) {

                        case 0:
                            addMiniRooms(level, room, roomColStart, roomRowStart);
                            break;

                        case 1:
                            addHallways(level, room, roomColStart, roomRowStart);
                            break;

                        case 2:
                            splitRoom(level, room, roomColStart, roomRowStart);
                            break;

                        case 3:
                            addEmptyRoom(level, room, roomColStart, roomRowStart);
                            break;
                    }

                    //if there is an opening and the neighbor room isn't the goal
                    if (!room.hasNorth() && (row + 1 != level.getMaze().getGoalRow() || col != level.getMaze().getGoalCol()))
                        createDoorHorizontal(level, roomColStart, roomColStart + ROOM_SIZE, roomRowStart + ROOM_SIZE - 1);

                    //if there is an opening and the neighbor room isn't the goal
                    if (!room.hasEast() && (row != level.getMaze().getGoalRow() || col + 1 != level.getMaze().getGoalCol()))
                        createDoorVertical(level, roomColStart + ROOM_SIZE - 1, roomRowStart, roomRowStart + ROOM_SIZE);
                }
            }
        }

        //check for free space
        checkFreeSpace(level);

        //add wall textures to the level
        addTextures(level);

        //add floor / ceiling
        //addBackground(level);

        //add enemies
        level.getEnemies().spawn();

        //now we add obstacles
        level.getObstacles().spawn();

        //and we add collectibles
        level.getCollectibles().spawn();
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

    //return list of valid places that are open
    public static List<Location> getLocationOptions(Level level, int startCol, int startRow, List<Location> options) {

        for (int col = startCol; col < startCol + ROOM_SIZE; col++) {
            for (int row = startRow; row < startRow + ROOM_SIZE; row++) {

                //skip if not freely open
                if (!level.hasFree(col, row))
                    continue;

                //skip if there is a wall
                if (level.hasWall(col, row))
                    continue;

                boolean skip = false;

                //we also want to avoid spaces next to a door otherwise the player could get blocked
                for (int x = -1; x <= 1; x++) {
                    for (int y = -1; y <= 1; y++) {
                        if (level.hasDoor(col + x, row + y) || skip) {
                            skip = true;
                            break;
                        }
                    }
                }

                //if we found a door we skip
                if (skip)
                    continue;

                //we passed all checks, add new location
                options.add(new Location(col, row));
            }
        }

        return options;
    }
}