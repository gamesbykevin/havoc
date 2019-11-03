package com.gamesbykevin.havoc.level;

import com.gamesbykevin.havoc.astar.Node;
import com.gamesbykevin.havoc.collectables.Collectibles;
import com.gamesbykevin.havoc.maze.Location;
import com.gamesbykevin.havoc.maze.Maze;
import com.gamesbykevin.havoc.maze.Room;

import java.util.ArrayList;
import java.util.List;

import static com.gamesbykevin.havoc.level.LevelHelper.getLocationOptions;
import static com.gamesbykevin.havoc.maze.Maze.*;

public class RoomHelper {

    //how big is each room
    public static final int ROOM_SIZE = 19;

    //the size of the smaller rooms
    public static final int ROOM_SIZE_SMALL = 8;

    //when we split the room we will do it slightly from the middle
    public static final int SPLIT_ROOM_OFFSET = 3;

    //how wide are the hallways
    public static final int[] ROOM_SIZE_HALL = {5, 7, 9, 11};

    //how many doors do we skip before we attempt to lock a door?
    public static final int DOOR_COUNT_MIN = 3;

    protected static void addHallways(Level level, Room room, int roomColStart, int roomRowStart) {

        //do we create a hall in these directions?
        boolean hallW = (!room.hasWest() || Maze.getRandom().nextBoolean());
        boolean hallE = (!room.hasEast() || Maze.getRandom().nextBoolean());
        boolean hallN = (!room.hasNorth() || Maze.getRandom().nextBoolean());
        boolean hallS = (!room.hasSouth() || Maze.getRandom().nextBoolean());

        //how big is the hallway
        int hallSize = ROOM_SIZE_HALL[Maze.getRandom().nextInt(ROOM_SIZE_HALL.length)];

        //the hallways only go across the middle
        int middleCol = roomColStart + (ROOM_SIZE / 2);
        int middleRow = roomRowStart + (ROOM_SIZE / 2);

        //we add walls across the side if open
        for (int roomRow = roomRowStart; roomRow < roomRowStart + ROOM_SIZE; roomRow++) {

            if (roomRow < middleRow - (hallSize / 2) && hallS) {
                level.setWall(middleCol - (hallSize / 2), roomRow, true);
                level.setWall(middleCol + (hallSize / 2), roomRow, true);
            }

            if (roomRow > middleRow + (hallSize / 2) && hallN) {
                level.setWall(middleCol - (hallSize / 2), roomRow, true);
                level.setWall(middleCol + (hallSize / 2), roomRow, true);
            }

            if (!hallE)
                level.setWall(middleCol + (hallSize / 2), roomRow, true);
            if (!hallW)
                level.setWall(middleCol - (hallSize / 2), roomRow, true);

            //if we created a hall, but have a wall
            if (hallW && room.hasWest())
                level.setWall(roomColStart, roomRow, true);
            if (hallE && room.hasEast())
                level.setWall(roomColStart + ROOM_SIZE - 1, roomRow, true);
        }

        //we add walls across the side if open
        for (int roomCol = roomColStart; roomCol < roomColStart + ROOM_SIZE; roomCol++) {

            if (roomCol < middleCol - (hallSize / 2) && hallW) {
                level.setWall(roomCol, middleRow - (hallSize / 2), true);
                level.setWall(roomCol, middleRow + (hallSize / 2), true);
            }

            if (roomCol > middleCol + (hallSize / 2) && hallE) {
                level.setWall(roomCol, middleRow - (hallSize / 2), true);
                level.setWall(roomCol, middleRow + (hallSize / 2), true);
            }

            if (!hallN)
                level.setWall(roomCol, middleRow + (hallSize / 2), true);
            if (!hallS)
                level.setWall(roomCol, middleRow - (hallSize / 2), true);

            //if we created a hall, but have a wall
            if (hallN && room.hasNorth())
                level.setWall(roomCol, roomRowStart + ROOM_SIZE - 1, true);
            if (hallS && room.hasSouth())
                level.setWall(roomCol, roomRowStart, true);
        }

        //make sure proper sides are closed
        for (int roomCol = roomColStart; roomCol < roomColStart + ROOM_SIZE; roomCol++) {

            if (room.hasNorth())
                level.setWall(roomCol, roomRowStart + ROOM_SIZE - 1, true);
            if (room.hasSouth())
                level.setWall(roomCol, roomRowStart, true);
        }

        for (int roomRow = roomRowStart; roomRow < roomRowStart + ROOM_SIZE; roomRow++) {

            if (room.hasWest())
                level.setWall(roomColStart, roomRow, true);
            if (room.hasEast())
                level.setWall(roomColStart + ROOM_SIZE - 1, roomRow, true);
        }
    }

    private static void addMiniRoom(Level level, int roomColStart, int roomRowStart, int doorCol, int doorRow, int roomSize, boolean addDoor) {

        //flag our door if we are to add one
        if (addDoor)
            level.setDoor(doorCol, doorRow, true);

        for (int col = roomColStart; col < roomColStart + roomSize; col++) {
            if (addDoor && doorCol == col && doorRow == roomRowStart) {
                level.setWall(col, roomRowStart + roomSize - 1, true);
                continue;
            } else if (addDoor && doorCol == col && doorRow == roomRowStart + roomSize - 1) {
                level.setWall(col, roomRowStart, true);
                continue;
            } else {
                level.setWall(col, roomRowStart, true);
                level.setWall(col, roomRowStart + roomSize - 1, true);
            }
        }

        for (int row = roomRowStart; row < roomRowStart + roomSize; row++) {
            level.setWall(roomColStart, row, true);
            level.setWall(roomColStart + roomSize - 1, row, true);
        }
    }

    protected static void addMiniRooms(Level level, Room room, int roomColStart, int roomRowStart) {

        //add walls where necessary
        for (int roomRow = roomRowStart; roomRow < roomRowStart + ROOM_SIZE; roomRow++) {

            if (room.hasWest())
                level.setWall(roomColStart, roomRow, true);
            if (room.hasEast())
                level.setWall(roomColStart + ROOM_SIZE - 1, roomRow, true);
        }

        for (int roomCol = roomColStart; roomCol < roomColStart + ROOM_SIZE; roomCol++) {

            if (room.hasNorth())
                level.setWall(roomCol, roomRowStart + ROOM_SIZE - 1, true);
            if (room.hasSouth())
                level.setWall(roomCol, roomRowStart, true);
        }

        int colWest = roomColStart;
        int colEast = roomColStart + ROOM_SIZE - ROOM_SIZE_SMALL;
        int rowSouth = roomRowStart;
        int rowNorth = roomRowStart + ROOM_SIZE - ROOM_SIZE_SMALL;

        int doorColWest = colWest + (ROOM_SIZE_SMALL / 2);
        int doorColEast = colEast + (ROOM_SIZE_SMALL / 2);
        int doorRowNorth = rowNorth;
        int doorRowSouth = rowSouth + ROOM_SIZE_SMALL - 1;

        if (Maze.getRandom().nextBoolean()) {

            //sw
            addMiniRoom(level, colWest, rowSouth, doorColWest, doorRowSouth, ROOM_SIZE_SMALL, true);

            //se
            addMiniRoom(level, colEast, rowSouth, doorColEast, doorRowSouth, ROOM_SIZE_SMALL, true);

            //nw
            addMiniRoom(level, colWest, rowNorth, doorColWest, doorRowNorth, ROOM_SIZE_SMALL, true);

            //ne
            addMiniRoom(level, colEast, rowNorth, doorColEast, doorRowNorth, ROOM_SIZE_SMALL, true);

        } else {

            if (Maze.getRandom().nextBoolean()) {

                //sw
                addMiniRoom(level, colWest, rowSouth, doorColWest, doorRowSouth, ROOM_SIZE_SMALL, Maze.getRandom().nextBoolean());

                //se
                addMiniRoom(level, colEast, rowSouth, doorColEast, doorRowSouth, ROOM_SIZE_SMALL, Maze.getRandom().nextBoolean());

                //nw
                addMiniRoom(level, colWest, rowNorth, doorColWest, doorRowNorth, ROOM_SIZE_SMALL, Maze.getRandom().nextBoolean());

                //ne
                addMiniRoom(level, colEast, rowNorth, doorColEast, doorRowNorth, ROOM_SIZE_SMALL, Maze.getRandom().nextBoolean());

            } else {

                List<Integer> options = new ArrayList<>();
                options.add(DIRECTION_NORTH);
                options.add(Maze.DIRECTION_SOUTH);
                options.add(Maze.DIRECTION_EAST);
                options.add(Maze.DIRECTION_WEST);

                //how many mini rooms do we add
                int count = (Maze.getRandom().nextInt(options.size()) + 1);

                //how many possible options
                int max = options.size();

                //continue
                while (options.size() > max - count) {

                    //pick a random index
                    int index = Maze.getRandom().nextInt(options.size());

                    switch (options.get(index)) {
                        case DIRECTION_NORTH:
                            addMiniRoom(level, colWest, rowNorth, doorColWest, doorRowNorth, ROOM_SIZE_SMALL, Maze.getRandom().nextBoolean());
                            break;

                        case DIRECTION_SOUTH:
                            addMiniRoom(level, colEast, rowSouth, doorColEast, doorRowSouth, ROOM_SIZE_SMALL, Maze.getRandom().nextBoolean());
                            break;

                        case DIRECTION_WEST:
                            addMiniRoom(level, colWest, rowSouth, doorColWest, doorRowSouth, ROOM_SIZE_SMALL, Maze.getRandom().nextBoolean());
                            break;

                        case DIRECTION_EAST:
                            addMiniRoom(level, colEast, rowNorth, doorColEast, doorRowNorth, ROOM_SIZE_SMALL, Maze.getRandom().nextBoolean());
                            break;
                    }

                    options.remove(index);
                }
            }
        }
    }

    protected static void splitRoom(Level level, Room room, int roomColStart, int roomRowStart) {

        //add walls around the room
        addEmptyRoom(level, room, roomColStart, roomRowStart);

        if (room.hasWest() && room.hasEast()) {
            createDoorHorizontal(level, roomColStart, roomColStart + ROOM_SIZE, roomRowStart + (ROOM_SIZE / 2));
        } else if (room.hasNorth() && room.hasSouth()) {
            createDoorVertical(level, roomColStart + (ROOM_SIZE / 2), roomRowStart, roomRowStart + ROOM_SIZE);
        } else {

            if (Maze.getRandom().nextBoolean()) {
                if (Maze.getRandom().nextBoolean()) {
                    createDoorVertical(level, roomColStart + (ROOM_SIZE / 2) - SPLIT_ROOM_OFFSET, roomRowStart, roomRowStart + ROOM_SIZE);
                } else {
                    createDoorVertical(level, roomColStart + (ROOM_SIZE / 2) + SPLIT_ROOM_OFFSET, roomRowStart, roomRowStart + ROOM_SIZE);
                }
            } else {
                if (Maze.getRandom().nextBoolean()) {
                    createDoorHorizontal(level, roomColStart, roomColStart + ROOM_SIZE, roomRowStart + (ROOM_SIZE / 2) - SPLIT_ROOM_OFFSET);
                } else {
                    createDoorHorizontal(level, roomColStart, roomColStart + ROOM_SIZE, roomRowStart + (ROOM_SIZE / 2) + SPLIT_ROOM_OFFSET);
                }
            }
        }
    }

    protected static void addDiamondRoom(Level level, Room room, int roomColStart, int roomRowStart) {

        int middleRow = roomRowStart + (ROOM_SIZE / 2);
        int middleCol = roomColStart + (ROOM_SIZE / 2);

        int endRow = (roomRowStart + ROOM_SIZE - 1);
        int endCol = (roomColStart + ROOM_SIZE - 1);

        for (int roomRow = roomRowStart; roomRow <= endRow; roomRow++) {
            for (int roomCol = roomColStart; roomCol <= endCol; roomCol++) {

                int minCol, maxCol;
                int minRow, maxRow;

                if (roomRow < middleRow) {
                    minCol = middleCol - (roomRow - roomRowStart);
                    maxCol = middleCol + (roomRow - roomRowStart);
                } else if (roomRow > middleRow) {
                    minCol = middleCol - (endRow - roomRow);
                    maxCol = middleCol + (endRow - roomRow);
                } else {
                    minCol = roomColStart;
                    maxCol = endCol;
                }

                if (roomCol < middleCol) {
                    minRow = middleRow - (roomCol - roomColStart);
                    maxRow = middleRow + (roomCol - roomColStart);
                } else if (roomCol > middleCol) {
                    minRow = middleRow - (endCol - roomCol);
                    maxRow = middleRow + (endCol - roomCol);
                } else {
                    minRow = roomRowStart;
                    maxRow = endRow;
                }

                if (roomCol <= minCol && roomRow <= minRow)
                    level.setWall(roomCol, roomRow, true);
                if (roomCol <= minCol && roomRow >= maxRow)
                    level.setWall(roomCol, roomRow, true);

                if (roomCol >= maxCol && roomRow <= minRow)
                    level.setWall(roomCol, roomRow, true);
                if (roomCol >= maxCol && roomRow >= maxRow)
                    level.setWall(roomCol, roomRow, true);

                //make sure we don't block an opening
                if (roomRow == middleRow) {
                    if (!room.hasWest() && roomCol == roomColStart)
                        level.setWall(roomCol, roomRow, false);
                    if (!room.hasEast() && roomCol == endCol)
                        level.setWall(roomCol, roomRow, false);
                }

                if (roomCol == middleCol) {
                    if (!room.hasNorth() && roomRow == endRow)
                        level.setWall(roomCol, roomRow, false);
                    if (!room.hasSouth() && roomRow == roomRowStart)
                        level.setWall(roomCol, roomRow, false);
                }
            }
        }
    }

    //room will be full size with no walls within
    protected static void addEmptyRoom(Level level, Room room, int roomColStart, int roomRowStart) {

        for (int roomRow = roomRowStart; roomRow < roomRowStart + ROOM_SIZE; roomRow++) {

            if (room.hasWest())
                level.setWall(roomColStart, roomRow, true);
            if (room.hasEast())
                level.setWall(roomColStart + ROOM_SIZE - 1, roomRow, true);

            if (roomRow < roomRowStart + ROOM_SIZE_SMALL || roomRow > roomRowStart + ROOM_SIZE - ROOM_SIZE_SMALL) {
                if (!room.hasWest())
                    level.setWall(roomColStart, roomRow, true);
                if (!room.hasEast())
                    level.setWall(roomColStart + ROOM_SIZE - 1, roomRow, true);
            }
        }

        for (int roomCol = roomColStart; roomCol < roomColStart + ROOM_SIZE; roomCol++) {
            if (room.hasSouth())
                level.setWall(roomCol, roomRowStart, true);
            if (room.hasNorth())
                level.setWall(roomCol, roomRowStart + ROOM_SIZE - 1, true);

            if (roomCol < roomColStart + ROOM_SIZE_SMALL || roomCol > roomColStart + ROOM_SIZE - ROOM_SIZE_SMALL - 1) {
                if (!room.hasNorth())
                    level.setWall(roomCol, roomRowStart + ROOM_SIZE - 1, true);
                if (!room.hasSouth())
                    level.setWall(roomCol, roomRowStart, true);
            }
        }
    }

    protected static void createDoorVertical(Level level, int col, int roomRowStart, int roomRowEnd) {

        int middleRow = roomRowStart + (ROOM_SIZE / 2);

        for (int row = roomRowStart; row < roomRowEnd; row++) {

            //skip if a wall already exists
            if (level.hasWall(col, row))
                continue;

            if (row == middleRow) {
                level.setDoor(col, row, true);
            } else {
                level.setWall(col, row, true);
            }
        }
    }

    protected static void createDoorHorizontal(Level level, int roomColStart, int roomColEnd, int row) {

        int middleCol = roomColStart + (ROOM_SIZE / 2);

        for (int col = roomColStart; col < roomColEnd; col++) {

            //skip if a wall already exists
            if (level.hasWall(col, row))
                continue;

            if (col == middleCol) {
                level.setDoor(col, row, true);
            } else {
                level.setWall(col, row, true);
            }
        }
    }

    protected static void checkFreeSpace(Level level) {

        //the starting point is free
        int col = (level.getMaze().getStartCol() * ROOM_SIZE) + (ROOM_SIZE / 2);
        int row = (level.getMaze().getStartRow() * ROOM_SIZE) + (ROOM_SIZE / 2);

        List<Location> options = new ArrayList<>();
        options.add(new Location(col, row));

        while (!options.isEmpty()) {

            //get the location
            Location location = options.get(0);

            //flag it as free
            level.setFree(location.col, location.row, true);

            //remove location from list
            options.remove(0);

            //check to see if we can add our neighbors
            for (int tmpCol = -1; tmpCol <= 1; tmpCol++) {
                for (int tmpRow = -1; tmpRow <= 1; tmpRow++) {

                    if (tmpCol != 0 && tmpRow != 0)
                        continue;

                    if (level.hasWall(location.col + tmpCol, location.row + tmpRow))
                        continue;
                    if (level.hasFree(location.col + tmpCol, location.row + tmpRow))
                        continue;

                    boolean found = false;

                    for (int i = 0; i < options.size(); i++) {
                        if (options.get(i).col == location.col && options.get(i).row == location.row) {
                            found = true;
                            break;
                        }
                    }

                    if (!found)
                        options.add(new Location(location.col + tmpCol, location.row + tmpRow));
                }
            }
        }
    }

    protected static void lockDoor(Level level) {

        //calculate the shortest path from start to finish
        level.calculate();

        //create a list of doors on our path to the goal
        List<Location> options = new ArrayList<>();

        //count the number of doors
        int count = 0;

        for (int i = 0; i < level.getAStar().getPath().size(); i++) {

            Node node = level.getAStar().getPath().get(i);

            //make sure location is a door
            if (!level.hasDoor(node.getCol(), node.getRow()))
                continue;

            //make sure we avoid the start room and the goal room
            if ((node.getCol() / ROOM_SIZE) == level.getMaze().getStartCol() && (node.getRow() / ROOM_SIZE) == level.getMaze().getStartRow())
                continue;
            if ((node.getCol() / ROOM_SIZE) == level.getMaze().getGoalCol() && (node.getRow() / ROOM_SIZE) == level.getMaze().getGoalRow())
                continue;

            //keep track of door count
            count++;

            //we don't want to lock a door close to the start
            if (count >= DOOR_COUNT_MIN)
                options.add(new Location(node.getCol(), node.getRow()));
        }

        //pick a random door from the list so we can lock it
        int index = Maze.getRandom().nextInt(options.size());
        Location location = options.get(index);
        level.setLocked(location.col, location.row, true);

        //room we locked the door
        int lockedCol = (location.col / ROOM_SIZE);
        int lockedRow = (location.row / ROOM_SIZE);

        //where we start
        int startCol = level.getMaze().getStartCol();
        int startRow = level.getMaze().getStartRow();

        //clear existing list
        options.clear();
        options.add(new Location(startCol, startRow));

        //list of valid locations
        List<Location> valid = new ArrayList<>();

        //continue until we don't have any other options
        while (!options.isEmpty()) {

            Room room = level.getMaze().getRoom(options.get(0));
            options.remove(0);

            if (!room.hasWest())
                checkList(options, valid, room, -1, 0, lockedCol, lockedRow, startCol, startRow);
            if (!room.hasEast())
                checkList(options, valid, room, 1, 0, lockedCol, lockedRow, startCol, startRow);
            if (!room.hasNorth())
                checkList(options, valid, room, 0, 1, lockedCol, lockedRow, startCol, startRow);
            if (!room.hasSouth())
                checkList(options, valid, room, 0, -1, lockedCol, lockedRow, startCol, startRow);
        }

        //pick a random room to place the key
        Location tmp = valid.get(Maze.getRandom().nextInt(valid.size()));
        int randomCol = tmp.col;
        int randomRow = tmp.row;

        valid.clear();
        valid = null;

        //clear list again for re-use
        options.clear();

        //get list of open spaces in the specified room
        options = getLocationOptions(level, randomCol * ROOM_SIZE, randomRow * ROOM_SIZE, options);

        //we pick a random location and add a key
        location = options.get(Maze.getRandom().nextInt(options.size()));

        //add a key at this location
        ((Collectibles) level.getCollectibles()).addKey(location.col, location.row);

        options.clear();
        options = null;
    }

    private static void checkList(List<Location> options, List<Location> valid, Room room, int offCol, int offRow, int lockedCol, int lockedRow, int startCol, int startRow) {

        int newCol = room.getCol() + offCol;
        int newRow = room.getRow() + offRow;

        //don't add these to the list of valid locations
        if (newCol == lockedCol && newRow == lockedRow)
            return;
        if (newCol == startCol && newRow == startRow)
            return;

        boolean match = false;

        //check if we already have the valid location
        for (int i = 0; i < valid.size(); i++) {
            Location tmp = valid.get(i);

            if (newCol == tmp.col && newRow == tmp.row) {
                match = true;
                break;
            }
        }

        //if we don't have it, add to the list
        if (!match) {
            options.add(new Location(newCol, newRow));
            valid.add(new Location(newCol, newRow));
        }
    }
}