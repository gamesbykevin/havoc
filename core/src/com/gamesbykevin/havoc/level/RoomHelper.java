package com.gamesbykevin.havoc.level;

import com.gamesbykevin.havoc.maze.Location;
import com.gamesbykevin.havoc.maze.Maze;
import com.gamesbykevin.havoc.maze.Room;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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
        boolean hallS = (!room.hasSouth() ||Maze.getRandom().nextBoolean());

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

            if (roomRow >= middleRow - (hallSize / 2) && roomRow <= middleCol + (hallSize / 2)) {
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

            if (roomCol >= middleCol - (hallSize / 2) && roomCol <= middleCol + (hallSize / 2)) {
            }
            if (!hallN)
                level.setWall(roomCol, middleRow + (hallSize / 2),true);
            if (!hallS)
                level.setWall(roomCol, middleRow - (hallSize / 2), true);

            //if we created a hall, but have a wall
            if (hallN && room.hasNorth())
                level.setWall(roomCol, roomRowStart + ROOM_SIZE - 1, true);
            if (hallS && room.hasSouth())
                level.setWall(roomCol, roomRowStart, true);
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
            createDoorHorizontal(level, roomColStart, roomColStart + ROOM_SIZE,  roomRowStart + (ROOM_SIZE / 2));
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
                    createDoorHorizontal(level, roomColStart, roomColStart + ROOM_SIZE,roomRowStart + (ROOM_SIZE / 2) - SPLIT_ROOM_OFFSET);
                } else {
                    createDoorHorizontal(level, roomColStart, roomColStart + ROOM_SIZE, roomRowStart + (ROOM_SIZE / 2) + SPLIT_ROOM_OFFSET);
                }
            }
        }
    }

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
        int col = ROOM_SIZE / 2;
        int row = ROOM_SIZE / 2;

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
}