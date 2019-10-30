package com.gamesbykevin.havoc.level;

import com.gamesbykevin.havoc.collectables.Collectibles;
import com.gamesbykevin.havoc.maze.Location;
import com.gamesbykevin.havoc.maze.Maze;
import com.gamesbykevin.havoc.maze.Room;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static com.gamesbykevin.havoc.level.LevelHelper.getLocationOptions;
import static com.gamesbykevin.havoc.maze.Maze.*;

public class RoomHelper {

    //how big is each room
    public static final int ROOM_SIZE = 25;

    //the size of the smaller rooms
    public static final int ROOM_SIZE_SMALL = 10;

    //we split the room slightly off from the middle
    public static final int SPLIT_ROOM_OFFSET = 3;

    //how wide are the hallways
    public static final int[] ROOM_SIZE_HALL = {3, 5, 7, 9, 11, 13};

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

        //set the free spaces for our level
        level.setMap();

        //calculate the shortest path from start to finish
        level.getAStar().calculate(
                (level.getMaze().getStartCol() * ROOM_SIZE) + (ROOM_SIZE / 2),
                (level.getMaze().getStartRow() * ROOM_SIZE) + (ROOM_SIZE / 2),
                (level.getMaze().getGoalCol() * ROOM_SIZE) + (ROOM_SIZE / 2),
                (level.getMaze().getGoalRow() * ROOM_SIZE) + (ROOM_SIZE / 2)
        );

        //create a list of rooms on our path to the goal
        List<Location> options = new ArrayList<>();

        for (int i = 0; i < level.getAStar().getPath().size(); i++) {

            int col = (level.getAStar().getPath().get(i).getCol() / ROOM_SIZE);
            int row = (level.getAStar().getPath().get(i).getRow() / ROOM_SIZE);

            boolean found = false;

            for (int x = 0; x < options.size(); x++) {

                if (options.get(x).col == col && options.get(x).row == row) {
                    found = true;
                    break;
                }
            }

            if (!found)
                options.add(new Location(col, row));
        }

        //pick a random room location skipping the 1st and last location
        int index = Maze.getRandom().nextInt(options.size() - 3) + 2;

        //get the room
        Location location = options.get(index);

        //get the neighbor next to it
        Location neighbor = options.get(index + 1);

        //figure out where the col, row is
        int roomColStart = location.col * ROOM_SIZE;
        int roomRowStart = location.row * ROOM_SIZE;

        //determine where to lock the door at
        if (location.col < neighbor.col) {
            level.setLockDoorCol(roomColStart + ROOM_SIZE - 1);
            level.setLockDoorRow(roomRowStart + (ROOM_SIZE / 2));
        } else if (location.col > neighbor.col) {
            level.setLockDoorCol(roomColStart);
            level.setLockDoorRow(roomRowStart + (ROOM_SIZE / 2));
        } else if (location.row < neighbor.row) {
            level.setLockDoorCol(roomColStart + (ROOM_SIZE / 2));
            level.setLockDoorRow(roomRowStart + ROOM_SIZE - 1);
        } else if (location.row > neighbor.row) {
            level.setLockDoorCol(roomColStart + (ROOM_SIZE / 2));
            level.setLockDoorRow(roomRowStart);
        }

        //clear list
        options.clear();

        int randomCol = 0;
        int randomRow = 0;

        Room room = level.getMaze().getRoom(location);

        //find a random room to place the key
        for (int col = 0; col < level.getMaze().getCols(); col++) {
            for (int row = 0; row < level.getMaze().getRows(); row++) {

                //if the cost is less then we can get to the room
                if (level.getMaze().getRoom(col, row).getCost() < room.getCost()) {
                    options.add(new Location(col, row));
                }
            }
        }

        //pick a random room location to place the key
        index = Maze.getRandom().nextInt(options.size());
        randomCol = options.get(index).col;
        randomRow = options.get(index).row;

        //clear list again for re-use
        options.clear();

        //get list of open spaces
        options = getLocationOptions(level, randomCol * ROOM_SIZE, randomRow * ROOM_SIZE, options);

        //we pick a random location and add a key
        location = options.get(Maze.getRandom().nextInt(options.size()));

        System.out.println("key: " + location.col + ", " + location.row);

        //add a key at this location
        ((Collectibles) level.getCollectibles()).addKey(location.col, location.row);
    }
}