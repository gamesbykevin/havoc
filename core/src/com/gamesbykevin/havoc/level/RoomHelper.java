package com.gamesbykevin.havoc.level;

import com.gamesbykevin.havoc.maze.Maze;
import com.gamesbykevin.havoc.maze.Room;

public class RoomHelper {

    //how big is each room
    public static final int ROOM_SIZE = 25;

    //the size of the smaller rooms
    public static final int ROOM_SIZE_SMALL = 10;

    protected static void addHallways(Level level, Room room, int roomColStart, int roomRowStart) {

        //the hallways only go across the middle
        int middleCol = roomColStart + (ROOM_SIZE / 2);
        int middleRow = roomRowStart + (ROOM_SIZE / 2);

        //do we create a hall in these directions?
        boolean hallW = true;
        boolean hallE = true;
        boolean hallN = true;
        boolean hallS = true;

        //if there is a wall blocking us determine at random if we create a hallway
        if (room.hasWest())
            hallW = Maze.getRandom().nextBoolean();
        if (room.hasEast())
            hallE = Maze.getRandom().nextBoolean();
        if (room.hasNorth())
            hallN = Maze.getRandom().nextBoolean();
        if (room.hasSouth())
            hallS = Maze.getRandom().nextBoolean();

        //we add walls across the side if open
        for (int roomRow = roomRowStart; roomRow < roomRowStart + ROOM_SIZE; roomRow++) {

            if (roomRow == middleRow)
                continue;

            if (roomRow < middleRow && hallS || roomRow > middleRow && hallN) {
                level.setWall(middleCol - 1, roomRow, true);
                level.setWall(middleCol + 1, roomRow, true);
            }

            if (!room.hasEast())
                level.setWall(roomColStart + ROOM_SIZE - 1, roomRow, true);
            if (!room.hasWest())
                level.setWall(roomColStart, roomRow, true);
        }

        //we add walls across the side if open
        for (int roomCol = roomColStart; roomCol < roomColStart + ROOM_SIZE; roomCol++) {

            if (roomCol == middleCol)
                continue;

            if (roomCol < middleCol && hallW || roomCol > middleCol && hallE) {
                level.setWall(roomCol, middleRow - 1, true);
                level.setWall(roomCol, middleRow + 1, true);
            }

            if (!room.hasNorth())
                level.setWall(roomCol, roomRowStart + ROOM_SIZE - 1, true);
            if (!room.hasSouth())
                level.setWall(roomCol, roomRowStart, true);
        }

        if (!hallN)
            level.setWall(middleCol, middleRow + 1, true);
        if (!hallS)
            level.setWall(middleCol, middleRow - 1, true);
        if (!hallW)
            level.setWall(middleCol - 1, middleRow, true);
        if (!hallE)
            level.setWall(middleCol + 1, middleRow, true);

        //if hallway and no exit we add a wall at the end
        if (hallN && room.hasNorth())
            level.setWall(middleCol, roomRowStart + ROOM_SIZE - 1, true);
        if (hallS && room.hasSouth())
            level.setWall(middleCol, roomRowStart, true);
        if (hallW && room.hasWest())
            level.setWall(roomColStart, middleRow, true);
        if (hallE && room.hasEast())
            level.setWall(roomColStart + ROOM_SIZE - 1, middleRow, true);
    }

    private static void addMiniRoom(Level level, int roomColStart, int roomRowStart, int middleCol, int middleRow) {

        int middle = roomColStart + (ROOM_SIZE_SMALL / 2);

        boolean nw = (roomColStart < middleCol && roomColStart > middleRow);
        boolean ne = (roomColStart > middleCol && roomColStart > middleRow);
        boolean sw = (roomColStart < middleCol && roomColStart < middleRow);
        boolean se = (roomColStart > middleCol && roomColStart < middleRow);

        for (int col = roomColStart; col < roomColStart + ROOM_SIZE_SMALL; col++) {
            level.setWall(col, roomRowStart, true);
            level.setWall(col, roomRowStart + ROOM_SIZE_SMALL - 1, true);
        }

        for (int row = roomRowStart; row < roomRowStart + ROOM_SIZE_SMALL; row++) {
            level.setWall(roomColStart, row, true);
            level.setWall(roomColStart + ROOM_SIZE_SMALL - 1, row, true);
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

        int middleCol = roomColStart + (ROOM_SIZE / 2);
        int middleRow = roomRowStart + (ROOM_SIZE / 2);

        if (Maze.getRandom().nextBoolean()) {

            //sw
            addMiniRoom(level, roomColStart, roomRowStart, middleCol, middleRow);

            //se
            addMiniRoom(level, roomColStart + ROOM_SIZE - ROOM_SIZE_SMALL, roomRowStart, middleCol, middleRow);

            //nw
            addMiniRoom(level, roomColStart, roomRowStart + ROOM_SIZE - ROOM_SIZE_SMALL, middleCol, middleRow);

            //ne
            addMiniRoom(level, roomColStart + ROOM_SIZE - ROOM_SIZE_SMALL, roomRowStart + ROOM_SIZE - ROOM_SIZE_SMALL, middleCol, middleRow);

        } else {

            if (Maze.getRandom().nextBoolean()) {

                //sw
                addMiniRoom(level, roomColStart, roomRowStart, middleCol, middleRow);

                //ne
                addMiniRoom(level, roomColStart + ROOM_SIZE - ROOM_SIZE_SMALL, roomRowStart + ROOM_SIZE - ROOM_SIZE_SMALL, middleCol, middleRow);

            } else if (Maze.getRandom().nextBoolean()) {

                //nw
                addMiniRoom(level, roomColStart, roomRowStart + ROOM_SIZE - ROOM_SIZE_SMALL, middleCol, middleRow);

                //se
                addMiniRoom(level, roomColStart + ROOM_SIZE - ROOM_SIZE_SMALL, roomRowStart, middleCol, middleRow);
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

    protected static void createDoorVertical(Level level, int col, int roomRowStart) {

        int middleRow = roomRowStart + (ROOM_SIZE / 2);

        for (int row = roomRowStart; row < roomRowStart + ROOM_SIZE; row++) {

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

    protected static void createDoorHorizontal(Level level, int roomColStart, int row) {

        int middleCol = roomColStart + (ROOM_SIZE / 2);

        for (int col = roomColStart; col < roomColStart + ROOM_SIZE; col++) {

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
}