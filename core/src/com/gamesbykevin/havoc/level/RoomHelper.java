package com.gamesbykevin.havoc.level;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.gamesbykevin.havoc.decals.DecalCustom.Type;
import com.gamesbykevin.havoc.decals.DecalCustom.Side;
import com.gamesbykevin.havoc.maze.Maze;
import com.gamesbykevin.havoc.maze.Room;

import static com.gamesbykevin.havoc.level.LevelHelper.*;

public class RoomHelper {

    //how big is each room
    public static final int ROOM_SIZE = 25;

    //the size of the smaller rooms
    public static final int ROOM_SIZE_SMALL = 10;

    protected static void addHallways(Level level, Room room, TextureRegion wall, int roomColStart, int roomRowStart) {

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
                addWall(level, Side.East, Type.Wall, wall, middleCol - 1, roomRow, false);
                addWall(level, Side.West, Type.Wall, wall, middleCol + 1, roomRow, false);
            }

            if (!room.hasEast())
                addWall(level, Side.East, Type.Wall, wall, roomColStart + ROOM_SIZE - 1, roomRow, false);
            if (!room.hasWest())
                addWall(level, Side.West, Type.Wall, wall, roomColStart, roomRow, false);
        }

        //we add walls across the side if open
        for (int roomCol = roomColStart; roomCol < roomColStart + ROOM_SIZE; roomCol++) {

            if (roomCol == middleCol)
                continue;

            if (roomCol < middleCol && hallW || roomCol > middleCol && hallE) {
                addWall(level, Side.North, Type.Wall, wall, roomCol, middleRow - 1, false);
                addWall(level, Side.South, Type.Wall, wall, roomCol, middleRow + 1, false);
            }

            if (!room.hasNorth())
                addWall(level, Side.North, Type.Wall, wall, roomCol, roomRowStart + ROOM_SIZE - 1, false);
            if (!room.hasSouth())
                addWall(level, Side.South, Type.Wall, wall, roomCol, roomRowStart, false);
        }

        if (!hallN)
            addWall(level, Side.South, Type.Wall, wall, middleCol, middleRow + 1, false);
        if (!hallS)
            addWall(level, Side.North, Type.Wall, wall, middleCol, middleRow - 1, false);
        if (!hallW)
            addWall(level, Side.East, Type.Wall, wall, middleCol - 1, middleRow, false);
        if (!hallE)
            addWall(level, Side.West, Type.Wall, wall, middleCol + 1, middleRow, false);

        //if hallway and no exit we add a wall at the end
        if (hallN && room.hasNorth())
            addWall(level, Side.South, Type.Wall, wall, middleCol, roomRowStart + ROOM_SIZE - 1, false);
        if (hallS && room.hasSouth())
            addWall(level, Side.North, Type.Wall, wall, middleCol, roomRowStart, false);
        if (hallW && room.hasWest())
            addWall(level, Side.East, Type.Wall, wall, roomColStart, middleRow, false);
        if (hallE && room.hasEast())
            addWall(level, Side.West, Type.Wall, wall, roomColStart + ROOM_SIZE - 1, middleRow, false);
    }

    private static void addMiniRoom(Level level, TextureRegion wall, int roomColStart, int roomRowStart, int middleCol, int middleRow) {

        //where is this room placed
        boolean nw = (roomColStart < middleCol && roomRowStart > middleRow);
        boolean ne = (roomColStart > middleCol && roomRowStart > middleRow);
        boolean sw = (roomColStart < middleCol && roomRowStart < middleRow);
        boolean se = (roomColStart > middleCol && roomRowStart < middleRow);

        int mid = (ROOM_SIZE_SMALL / 2);

        for (int col = roomColStart; col < roomColStart + ROOM_SIZE_SMALL; col++) {

            if (col == roomColStart + mid) {

                if (nw || ne) {
                    addWall(level, Side.South, Type.Door, getTextureDoor(), col, roomRowStart, false);
                    addVertical(level, wall, col, roomRowStart + ROOM_SIZE_SMALL - 1);
                } else if (sw || se) {
                    addWall(level, Side.South, Type.Door, getTextureDoor(), col, roomRowStart + ROOM_SIZE_SMALL - 1, false);
                    addVertical(level, wall, col, roomRowStart);
                }

            } else if (col == roomColStart + mid - 1 || col == roomColStart + mid + 1) {

                addWall(level, Side.West, Type.Wall, getTextureSide(), col, roomRowStart, false);
                addWall(level, Side.East, Type.Wall, getTextureSide(), col, roomRowStart, false);
                addWall(level, Side.North, Type.Wall, wall, col, roomRowStart, false);
                addWall(level, Side.South, Type.Wall, wall, col, roomRowStart, false);

                addWall(level, Side.West, Type.Wall, getTextureSide(), col, roomRowStart + ROOM_SIZE_SMALL - 1, false);
                addWall(level, Side.East, Type.Wall, getTextureSide(), col, roomRowStart + ROOM_SIZE_SMALL - 1, false);
                addWall(level, Side.North, Type.Wall, wall, col, roomRowStart + ROOM_SIZE_SMALL - 1, false);
                addWall(level, Side.South, Type.Wall, wall, col, roomRowStart + ROOM_SIZE_SMALL - 1, false);

            } else {

                addVertical(level, wall, col, roomRowStart);
                addVertical(level, wall, col, roomRowStart + ROOM_SIZE_SMALL - 1);

            }
        }

        for (int row = roomRowStart; row < roomRowStart + ROOM_SIZE_SMALL; row++) {

            if (row == roomRowStart + mid) {

                if (nw || sw) {
                    addWall(level, Side.West, Type.Door, getTextureDoor(), roomColStart + ROOM_SIZE_SMALL - 1, row, false);
                    addHorizontal(level, wall, roomColStart, row);
                } else if (ne || se) {
                    addWall(level, Side.West, Type.Door, getTextureDoor(), roomColStart, row, false);
                    addHorizontal(level, wall, roomColStart + ROOM_SIZE_SMALL - 1, row);
                }

            } else if (row == roomRowStart + mid - 1 || row == roomRowStart + mid + 1) {

                addWall(level, Side.West, Type.Wall, wall, roomColStart, row, false);
                addWall(level, Side.East, Type.Wall, wall, roomColStart, row, false);
                addWall(level, Side.North, Type.Wall, getTextureSide(), roomColStart, row, false);
                addWall(level, Side.South, Type.Wall, getTextureSide(), roomColStart, row, false);

                addWall(level, Side.West, Type.Wall, wall, roomColStart + ROOM_SIZE_SMALL - 1, row, false);
                addWall(level, Side.East, Type.Wall, wall, roomColStart + ROOM_SIZE_SMALL - 1, row, false);
                addWall(level, Side.North, Type.Wall, getTextureSide(), roomColStart + ROOM_SIZE_SMALL - 1, row, false);
                addWall(level, Side.South, Type.Wall, getTextureSide(), roomColStart + ROOM_SIZE_SMALL - 1, row, false);

            } else {

                addHorizontal(level, wall, roomColStart, row);
                addHorizontal(level, wall, roomColStart + ROOM_SIZE_SMALL - 1, row);

            }
        }
    }

    private static void addCorner(Level level, TextureRegion wall, int roomColStart, int roomRowStart, int middleCol, int middleRow) {

        //where is this room placed
        boolean nw = (roomColStart < middleCol && roomRowStart > middleRow);
        boolean ne = (roomColStart > middleCol && roomRowStart > middleRow);
        boolean sw = (roomColStart < middleCol && roomRowStart < middleRow);
        boolean se = (roomColStart > middleCol && roomRowStart < middleRow);

        for (int col = roomColStart; col < roomColStart + ROOM_SIZE_SMALL; col++) {

            if (nw || ne) {
                addWall(level, Side.South, Type.Wall, wall, col, roomRowStart + ROOM_SIZE_SMALL - 1, false);
            } else if (sw || se) {
                addWall(level, Side.North, Type.Wall, wall, col, roomRowStart, false);
            }

            //add texture on the end
            if (col == roomColStart + ROOM_SIZE_SMALL - 1) {
                if (nw || ne) {
                    addWall(level, Side.East, Type.Wall, wall, col, roomRowStart + ROOM_SIZE_SMALL - 1, false);
                } else if (sw || se) {
                    addWall(level, Side.East, Type.Wall, wall, col, roomRowStart, false);
                }
            }

            if (col == roomColStart) {
                if (nw || ne) {
                    addWall(level, Side.West, Type.Wall, wall, col, roomRowStart + ROOM_SIZE_SMALL - 1, false);
                } else if (sw || se) {
                    addWall(level, Side.West, Type.Wall, wall, col, roomRowStart, false);
                }
            }
        }

        for (int row = roomRowStart; row < roomRowStart + ROOM_SIZE_SMALL; row++) {

            if (nw || sw) {
                addWall(level, Side.East, Type.Wall, wall, roomColStart, row, false);
            } else if (ne || se) {
                addWall(level, Side.West, Type.Wall, wall, roomColStart + ROOM_SIZE_SMALL - 1, row, false);
            }

            //add texture on the end
            if (row == roomRowStart + ROOM_SIZE_SMALL - 1) {
                if (sw) {
                    addWall(level, Side.North, Type.Wall, wall, roomColStart, row, false);
                } else if (se) {
                    addWall(level, Side.North, Type.Wall, wall, roomColStart + ROOM_SIZE_SMALL - 1, row, false);
                }
            }

            if (row == roomRowStart) {
                if (nw) {
                    addWall(level, Side.South, Type.Wall, wall, roomColStart, row, false);
                } else if (ne) {
                    addWall(level, Side.South, Type.Wall, wall, roomColStart + ROOM_SIZE_SMALL - 1, row, false);
                }
            }
        }
    }

    protected static void addMiniRooms(Level level, Room room, TextureRegion wall, int roomColStart, int roomRowStart) {

        int middleCol = roomColStart + (ROOM_SIZE / 2);
        int middleRow = roomRowStart + (ROOM_SIZE / 2);

        //add walls where necessary
        for (int roomRow = roomRowStart; roomRow < roomRowStart + ROOM_SIZE; roomRow++) {

            if (roomRow > roomRowStart + ROOM_SIZE_SMALL - 1 && roomRow < roomRowStart + ROOM_SIZE - ROOM_SIZE_SMALL) {
                if (room.hasWest())
                    addWall(level, Side.East, Type.Wall, wall, roomColStart, roomRow, false);
                if (room.hasEast())
                    addWall(level, Side.West, Type.Wall, wall, roomColStart + ROOM_SIZE, roomRow, false);
            }
        }

        for (int roomCol = roomColStart; roomCol < roomColStart + ROOM_SIZE; roomCol++) {

            if (roomCol >  roomColStart + ROOM_SIZE_SMALL - 1 && roomCol < roomColStart + ROOM_SIZE - ROOM_SIZE_SMALL) {
                if (room.hasNorth())
                    addWall(level, Side.South, Type.Wall, wall, roomCol, roomRowStart + ROOM_SIZE, false);
                if (room.hasSouth())
                    addWall(level, Side.North, Type.Wall, wall, roomCol, roomRowStart, false);
            }
        }

        //south west
        if (Maze.getRandom().nextBoolean()) {
            addMiniRoom(level, wall, roomColStart, roomRowStart, middleCol, middleRow);
        } else {
            addCorner(level, wall, roomColStart, roomRowStart, middleCol, middleRow);
        }

        //south east
        if (Maze.getRandom().nextBoolean()) {
            addMiniRoom(level, wall, roomColStart + ROOM_SIZE - ROOM_SIZE_SMALL, roomRowStart, middleCol, middleRow);
        } else {
            addCorner(level, wall, roomColStart + ROOM_SIZE - ROOM_SIZE_SMALL, roomRowStart, middleCol, middleRow);
        }

        //north west
        if (Maze.getRandom().nextBoolean()) {
            addMiniRoom(level, wall, roomColStart, roomRowStart + ROOM_SIZE - ROOM_SIZE_SMALL, middleCol, middleRow);
        } else {
            addCorner(level, wall, roomColStart, roomRowStart + ROOM_SIZE - ROOM_SIZE_SMALL, middleCol, middleRow);
        }

        //north east
        if (Maze.getRandom().nextBoolean()) {
            addMiniRoom(level, wall, roomColStart + ROOM_SIZE - ROOM_SIZE_SMALL, roomRowStart + ROOM_SIZE - ROOM_SIZE_SMALL, middleCol, middleRow);
        } else {
            addCorner(level, wall, roomColStart + ROOM_SIZE - ROOM_SIZE_SMALL, roomRowStart + ROOM_SIZE - ROOM_SIZE_SMALL, middleCol, middleRow);
        }
    }

    protected static void addEmptyRoom(Level level, Room room, TextureRegion wall, int roomColStart, int roomRowStart) {

        for (int roomRow = roomRowStart; roomRow < roomRowStart + ROOM_SIZE; roomRow++) {

            if (room.hasWest())
                addWall(level, Side.East, Type.Wall, wall, roomColStart, roomRow, false);
            if (room.hasEast())
                addWall(level, Side.West, Type.Wall, wall, roomColStart + ROOM_SIZE - 1, roomRow, false);

            if (roomRow < roomRowStart + ROOM_SIZE_SMALL || roomRow > roomRowStart + ROOM_SIZE - ROOM_SIZE_SMALL - 1) {
                if (!room.hasWest())
                    addWall(level, Side.East, Type.Wall, wall, roomColStart, roomRow, false);
                if (!room.hasEast())
                    addWall(level, Side.West, Type.Wall, wall, roomColStart + ROOM_SIZE - 1, roomRow, false);
            }
        }

        for (int roomCol = roomColStart; roomCol < roomColStart + ROOM_SIZE; roomCol++) {
            if (room.hasSouth())
                addWall(level, Side.North, Type.Wall, wall, roomCol, roomRowStart, false);
            if (room.hasNorth())
                addWall(level, Side.South, Type.Wall, wall, roomCol, roomRowStart + ROOM_SIZE - 1, false);

            if (roomCol < roomColStart + ROOM_SIZE_SMALL || roomCol > roomColStart + ROOM_SIZE - ROOM_SIZE_SMALL - 1) {
                if (!room.hasNorth())
                    addWall(level, Side.South, Type.Wall, wall, roomCol, roomRowStart + ROOM_SIZE - 1, false);
                if (!room.hasSouth())
                    addWall(level, Side.North, Type.Wall, wall, roomCol, roomRowStart, false);
            }
        }

        //add walls on the ends
        if (!room.hasNorth()) {
            addWall(level, Side.East, Type.Wall, wall, roomColStart + ROOM_SIZE_SMALL - 1, roomRowStart + ROOM_SIZE - 1, false);
            addWall(level, Side.West, Type.Wall, wall, roomColStart + ROOM_SIZE - ROOM_SIZE_SMALL, roomRowStart + ROOM_SIZE - 1, false);
        }
        if (!room.hasSouth()) {
            addWall(level, Side.East, Type.Wall, wall, roomColStart + ROOM_SIZE_SMALL - 1, roomRowStart, false);
            addWall(level, Side.West, Type.Wall, wall, roomColStart + ROOM_SIZE - ROOM_SIZE_SMALL, roomRowStart, false);
        }
        if (!room.hasWest()) {
            addWall(level, Side.South, Type.Wall, wall, roomColStart, roomRowStart + ROOM_SIZE - ROOM_SIZE_SMALL, false);
            addWall(level, Side.North, Type.Wall, wall, roomColStart, roomRowStart + ROOM_SIZE_SMALL - 1, false);
        }
        if (!room.hasEast()) {
            addWall(level, Side.South, Type.Wall, wall, roomColStart + ROOM_SIZE - 1, roomRowStart + ROOM_SIZE - ROOM_SIZE_SMALL, false);
            addWall(level, Side.North, Type.Wall, wall, roomColStart + ROOM_SIZE - 1, roomRowStart + ROOM_SIZE_SMALL - 1, false);
        }

        //add walls on the corner
        if (room.hasSouth()) {
            addWall(level, Side.East, Type.Wall, wall, roomColStart + ROOM_SIZE - 1, roomRowStart, false);
            addWall(level, Side.West, Type.Wall, wall, roomColStart, roomRowStart, false);
        }
        if (room.hasNorth()) {
            addWall(level, Side.East, Type.Wall, wall, roomColStart + ROOM_SIZE - 1, roomRowStart + ROOM_SIZE - 1, false);
            addWall(level, Side.West, Type.Wall, wall, roomColStart, roomRowStart + ROOM_SIZE - 1, false);
        }
        if (room.hasWest()) {
            addWall(level, Side.South, Type.Wall, wall, roomColStart, roomRowStart, false);
            addWall(level, Side.North, Type.Wall, wall, roomColStart, roomRowStart + ROOM_SIZE - 1, false);
        }
        if (room.hasEast()) {
            addWall(level, Side.South, Type.Wall, wall, roomColStart + ROOM_SIZE - 1, roomRowStart, false);
            addWall(level, Side.North, Type.Wall, wall, roomColStart + ROOM_SIZE - 1, roomRowStart + ROOM_SIZE - 1, false);
        }
    }

    protected static void addVertical(Level level, TextureRegion texture, int col, int row) {
        addWall(level, Side.North, Type.Wall, texture, col, row, false);
        addWall(level, Side.South, Type.Wall, texture, col, row, false);
    }

    protected static void addHorizontal(Level level, TextureRegion texture, int col, int row) {
        addWall(level, Side.East, Type.Wall, texture, col, row, false);
        addWall(level, Side.West, Type.Wall, texture, col, row, false);
    }

    protected static void createDoorVertical(Level level, TextureRegion wall, TextureRegion door, int col, int roomRowStart) {

        int middleRow = roomRowStart + (ROOM_SIZE / 2);

        for (int row = roomRowStart; row < roomRowStart + ROOM_SIZE; row++) {

            //skip if a wall already exists
            if (level.hasWall(col, row))
                continue;

            if (row == middleRow) {
                addWall(level, Side.West, Type.Door, door, col, row, false);
            } else if (row == middleRow + 1) {
                addHorizontal(level, wall, col, row);
                addWall(level, Side.South, Type.Wall, getTextureSide(), col, row, false);
            } else if (row == middleRow - 1) {
                addHorizontal(level, wall, col, row);
                addWall(level, Side.North, Type.Wall, getTextureSide(), col, row, false);
            } else {
                addHorizontal(level, wall, col, row);
            }
        }
    }

    protected static void createDoorHorizontal(Level level, TextureRegion wall, TextureRegion door, int roomColStart, int row) {

        int middleCol = roomColStart + (ROOM_SIZE / 2);

        for (int col = roomColStart; col < roomColStart + ROOM_SIZE; col++) {

            //skip if a wall already exists
            if (level.hasWall(col, row))
                continue;

            if (col == middleCol) {
                addWall(level, Side.South, Type.Door, door, col, row, false);
            } else if (col == middleCol + 1) {
                addVertical(level, wall, col, row);
                addWall(level, Side.West, Type.Wall, getTextureSide(), col, row, false);
            } else if (col == middleCol - 1) {
                addVertical(level, wall, col, row);
                addWall(level, Side.East, Type.Wall, getTextureSide(), col, row, false);
            } else {
                addVertical(level, wall, col, row);
            }
        }
    }
}