package com.gamesbykevin.havoc.dungeon;

import com.gamesbykevin.havoc.dungeon.Cell.Type;

import java.util.ArrayList;
import java.util.List;

import static com.gamesbykevin.havoc.dungeon.Dungeon.getRandom;
import static com.gamesbykevin.havoc.dungeon.LeafHelper.getLeafRooms;

public class RoomHelper {

    public static final int ROOM_DIMENSION_MIN = 14;
    public static final int ROOM_DIMENSION_MAX = 20;

    //split the room in half
    protected static void setupSplit(Dungeon dungeon, Room room) {

        if (getRandom().nextBoolean()) {

            //split horizontally
            int row = room.getY() + (room.getH() / 2);

            //place door here
            int col = getRandom().nextInt(room.getW() - 4) + room.getX() + 3;

            for (int x = room.getX(); x < room.getX() + room.getW(); x++) {

                if (col == x) {
                    dungeon.getCell(x, row).setType(Type.Door);
                } else {
                    dungeon.getCell(x, row).setType(Type.Wall);
                }
            }

        } else {

            //split vertically
            int col = room.getX() + (room.getW() / 2);

            //place door here
            int row = getRandom().nextInt(room.getH() - 4) + room.getY() + 3;

            for (int y = room.getY(); y < room.getY() + room.getH(); y++) {

                if (row == y) {
                    dungeon.getCell(col, y).setType(Type.Door);
                } else {
                    dungeon.getCell(col, y).setType(Type.Wall);
                }
            }
        }
    }

    //make room look like a diamond
    protected static void setupDiamond(Dungeon dungeon, Room room) {

        int middleCol = room.getX() + (room.getW() / 2);
        int middleRow = room.getY() + (room.getH() / 2);

        int endCol = room.getX() + room.getW() - 1;
        int endRow = room.getY() + room.getH() - 1;

        for (int row = room.getY(); row <= endRow; row++) {
            for (int col = room.getX(); col <= endCol; col++) {

                int minCol, maxCol;
                int minRow, maxRow;

                if (row < middleRow) {
                    minCol = middleCol - (row - room.getY());
                    maxCol = middleCol + (row - room.getY());
                } else if (row > middleRow) {
                    minCol = middleCol - (endRow - row);
                    maxCol = middleCol + (endRow - row);
                } else {
                    minCol = room.getX();
                    maxCol = endCol;
                }

                if (col < middleCol) {
                    minRow = middleRow - (col - room.getX());
                    maxRow = middleRow + (col - room.getX());
                } else if (col > middleCol) {
                    minRow = middleRow - (endCol - col);
                    maxRow = middleRow + (endCol - col);
                } else {
                    minRow = room.getY();
                    maxRow = endRow;
                }

                //will we mark it unvisited?
                boolean unvisited = false;

                if (col <= minCol && row <= minRow)
                    unvisited = true;
                if (col <= minCol && row >= maxRow)
                    unvisited = true;

                if (col >= maxCol && row <= minRow)
                    unvisited = true;
                if (col >= maxCol && row >= maxRow)
                    unvisited = true;

                if (unvisited)
                    dungeon.getCell(col, row).setType(Type.Wall);
            }
        }

        //update map now that the room is setup
        dungeon.updateMap();
    }

    protected static void setupMiniRooms(Dungeon dungeon, Room room) {

        int middleCol = room.getX() + (room.getW() / 2);
        int middleRow = room.getY() + (room.getH() / 2);

        switch (getRandom().nextInt(6)) {

            //opposite corners
            case 0:
                addMiniRoomNW(dungeon, room, middleCol, middleRow);
                addMiniRoomSE(dungeon, room, middleCol, middleRow);
                break;

            //opposite corners
            case 1:
                addMiniRoomNE(dungeon, room, middleCol, middleRow);
                addMiniRoomSW(dungeon, room, middleCol, middleRow);
                break;

            //west side
            case 2:
                addMiniRoomNW(dungeon, room, middleCol, middleRow);
                addMiniRoomSW(dungeon, room, middleCol, middleRow);
                break;

            //east side
            case 3:
                addMiniRoomNE(dungeon, room, middleCol, middleRow);
                addMiniRoomSE(dungeon, room, middleCol, middleRow);
                break;

            //north side
            case 4:
                addMiniRoomNE(dungeon, room, middleCol, middleRow);
                addMiniRoomNW(dungeon, room, middleCol, middleRow);
                break;

            //south side
            case 5:
                addMiniRoomSE(dungeon, room, middleCol, middleRow);
                addMiniRoomSW(dungeon, room, middleCol, middleRow);
                break;

            //all 4 corners
            default:
                addMiniRoomNE(dungeon, room, middleCol, middleRow);
                addMiniRoomNW(dungeon, room, middleCol, middleRow);
                addMiniRoomSE(dungeon, room, middleCol, middleRow);
                addMiniRoomSW(dungeon, room, middleCol, middleRow);
                break;
        }
    }

    private static void addMiniRoomNE(Dungeon dungeon, Room room, int middleCol, int middleRow) {
        addRow(dungeon, middleCol + 2, room.getX() + room.getW() - 1, middleRow + 2);
        addCol(dungeon, middleRow + 2, room.getY() + room.getH() - 1, middleCol + 2);
        dungeon.getCell(middleCol + ((room.getX() + room.getW() - middleCol) / 2), middleRow + 2).setType(Type.Door);
    }

    private static void addMiniRoomNW(Dungeon dungeon, Room room, int middleCol, int middleRow) {
        addRow(dungeon, room.getX(), middleCol - 2, middleRow + 2);
        addCol(dungeon, middleRow + 2, room.getY() + room.getH() - 1, middleCol - 2);
        dungeon.getCell(room.getX() + ((middleCol - room.getX()) / 2), middleRow + 2).setType(Type.Door);
    }

    private static void addMiniRoomSE(Dungeon dungeon, Room room, int middleCol, int middleRow) {
        addRow(dungeon, middleCol + 2, room.getX() + room.getW() - 1, middleRow - 2);
        addCol(dungeon, room.getY(), middleRow - 2, middleCol + 2);
        dungeon.getCell(middleCol + ((room.getX() + room.getW() - middleCol) / 2), middleRow - 2).setType(Type.Door);
    }

    private static void addMiniRoomSW(Dungeon dungeon, Room room, int middleCol, int middleRow) {
        addRow(dungeon, room.getX(), middleCol - 2, middleRow - 2);
        addCol(dungeon, room.getY(), middleRow - 2, middleCol - 2);
        dungeon.getCell(room.getX() + ((middleCol - room.getX()) / 2), middleRow - 2).setType(Type.Door);
    }

    private static void addRow(Dungeon dungeon, int startCol, int endCol, int row) {
        for (int col = startCol; col <= endCol; col++) {
            dungeon.getCell(col, row).setType(Type.Wall);
        }
    }

    private static void addCol(Dungeon dungeon, int startRow, int endRow, int col) {
        for (int row = startRow; row <= endRow; row++) {
            dungeon.getCell(col, row).setType(Type.Wall);
        }
    }

    protected static void setupBox(Dungeon dungeon, Room room) {

        int rowN = getRowN(room);
        int rowS = getRowS(room);

        int colW = getColW(room);
        int colE = getColE(room);

        //do we place a door
        boolean door = (getRandom().nextBoolean());

        int middleCol = room.getX() + (room.getW() / 2);
        int middleRow = room.getY() + (room.getH() / 2);

        for (int row = room.getY() + 1; row < room.getY() + room.getH() - 1; row++) {
            for (int col = room.getX() + 1; col < room.getX() + room.getW() - 1; col++) {

                //center of the box
                if (col > colW && col < colE && row > rowS && row < rowN) {
                    dungeon.getCell(col, row).setType(door ? Type.Open : Type.Wall);
                } else {
                    if (row == rowN || row == rowS) {
                        if (col >= colW && col <= colE)
                            dungeon.getCell(col, row).setType(Type.Wall);
                    }

                    if (col == colW || col == colE) {
                        if (row >= rowS && row <= rowN)
                            dungeon.getCell(col, row).setType(Type.Wall);
                    }
                }
            }
        }

        //do we add a door
        if (door) {

            //pick a random side for a door
            switch (getRandom().nextInt(4)) {
                case 0:
                    dungeon.getCell(middleCol, rowN).setType(Type.Door);
                    break;

                case 1:
                    dungeon.getCell(middleCol, rowS).setType(Type.Door);
                    break;

                case 2:
                    dungeon.getCell(colW, middleRow).setType(Type.Door);
                    break;

                case 3:
                    dungeon.getCell(colE, middleRow).setType(Type.Door);
                    break;
            }
        }

    }

    protected static void setupHalfBox(Dungeon dungeon, Room room) {

        int rowN = getRowN(room);
        int rowS = getRowS(room);

        int colW = getColW(room);
        int colE = getColE(room);

        for (int row = room.getY(); row < room.getY() + room.getH(); row++) {
            for (int col = room.getX(); col < room.getX() + room.getW(); col++) {

                boolean horizontal = false;

                if (row == rowS || row == rowN) {
                    if (col >= colW && col < room.getX() + (room.getW() / 2) - 1)
                        dungeon.getCell(col, row).setType(Type.Wall);
                    if (col > room.getX() + (room.getW() / 2) + 1 && col <= colE)
                        dungeon.getCell(col, row).setType(Type.Wall);
                } else if (row > rowS && row < rowN && (col == colW || col == colE)) {
                    dungeon.getCell(col, row).setType(Type.Wall);
                }
            }
        }
    }

    protected static void setupThreeSides(Dungeon dungeon, Room room) {

        int rowN = getRowN(room);
        int rowS = getRowS(room);

        int colW = getColW(room);
        int colE = getColE(room);

        //pick a random side for the opening
        int side = getRandom().nextInt(4);

        for (int row = room.getY(); row < room.getY() + room.getH(); row++) {
            for (int col = room.getX(); col < room.getX() + room.getW(); col++) {

                if (row == rowN && side != 0) {
                    if (col >= colW && col <= colE)
                        dungeon.getCell(col, row).setType(Type.Wall);
                }

                if (row == rowS && side != 1) {
                    if (col >= colW && col <= colE)
                        dungeon.getCell(col, row).setType(Type.Wall);
                }

                if (col == colW && side != 2) {
                    if (row >= rowS && row <= rowN)
                        dungeon.getCell(col, row).setType(Type.Wall);
                }

                if (col == colE && side != 3) {
                    if (row >= rowS && row <= rowN)
                        dungeon.getCell(col, row).setType(Type.Wall);
                }
            }
        }
    }

    protected static void setupFourPillars(Dungeon dungeon, Room room) {

        int rowN = getRowN(room);
        int rowS = getRowS(room);

        int colW = getColW(room);
        int colE = getColE(room);

        //size of each pillar
        int size = 2;

        for (int row = room.getY(); row < room.getY() + room.getH(); row++) {
            for (int col = room.getX(); col < room.getX() + room.getW(); col++) {

                boolean north = (row <= rowN && row >= rowN - size);
                boolean south = (row >= rowS && row <= rowS + size);
                boolean east = (col <= colE && col >= colE - size);
                boolean west = (col >= colW && col <= colW + size);

                if (north && west || north && east || south && west || south && east)
                    dungeon.getCell(col, row).setType(Type.Wall);
            }
        }
    }

    protected static void setupTwoBoxes(Dungeon dungeon, Room room) {

        int rowN = getRowN(room);
        int rowS = getRowS(room);

        int colW = getColW(room);
        int colE = getColE(room);

        int size = 3;

        boolean horizontal = getRandom().nextBoolean();

        for (int row = room.getY(); row < room.getY() + room.getH(); row++) {
            for (int col = room.getX(); col < room.getX() + room.getW(); col++) {

                if (horizontal) {

                    if (row > rowS && row < rowS + size || row < rowN && row > rowN - size) {
                        if (col > colW && col < colE)
                            dungeon.getCell(col, row).setType(Type.Wall);
                    }

                } else {

                    if (col > colW && col < colW + size || col < colE && col > colE - size)
                        if (row > rowS && row < rowN)
                            dungeon.getCell(col, row).setType(Type.Wall);
                }
            }
        }
    }

    protected static void setupX(Dungeon dungeon, Room room) {

        int rowN = getRowN(room);
        int rowS = getRowS(room);

        int colW = getColW(room);
        int colE = getColE(room);

        int col = colW;

        for (int row = rowS; row <= rowN; row++) {
            if (col <= colE)
                dungeon.getCell(col, row).setType(Type.Wall);
            col++;
        }

        col = colW;

        for (int row = rowN; row >= rowS; row--) {
            if (col <= colE)
                dungeon.getCell(col, row).setType(Type.Wall);
            col++;
        }
    }

    protected static void createSecrets(Dungeon dungeon) {

        //get a list of leaves
        List<Leaf> leaves = getLeafRooms(dungeon);

        for (int i = 0; i < leaves.size(); i++) {

            //let's see how many doors are in the room
            List<Cell> doors = getDoors(dungeon, leaves.get(i).getRoom());

            //we will only make a room a secret if it has only 1 door
            if (doors.size() > 1)
                continue;

            //room only has 1 door so let's get it
            Cell door = doors.get(0);

            Room room = null;

            //find the connected room
            for (int x = 0; x < leaves.size(); x++) {

                if (i == x)
                    continue;

                //get the room
                Room tmp = leaves.get(x).getRoom();

                if (dungeon.getCell(tmp.getX(), tmp.getY()).hasId(door.getLink())) {
                    room = tmp;
                    break;
                }
            }

            if (room != null) {

                //check the room we are linked to, in order to get the linked door
                Cell linkedDoor = getLinkedDoor(dungeon, room, door);

                if (linkedDoor != null) {

                    //mark this door as a secret
                    linkedDoor.setType(Type.Secret);

                    //mark the room as a secret as well
                    leaves.get(i).getRoom().setSecret(true);
                }
            }
        }
    }

    protected static Cell getLinkedDoor(Dungeon dungeon, Room room, Cell door) {

        for (int row = room.getY(); row < room.getY() + room.getH(); row++) {
            for (int col = room.getX(); col < room.getX() + room.getW(); col++) {

                //only check the edges for doors
                if (col != room.getX() && col != room.getX() + room.getW() - 1 && row != room.getY() && row != room.getY() + room.getH() - 1)
                    continue;

                Cell cell = dungeon.getCell(col, row);

                //any of these are doors
                if (cell.isDoor()) {
                    if (cell.hasId(door.getLink()) && door.hasId(cell.getLink()))
                        return cell;
                }
            }
        }

        return null;
    }

    private static List<Cell> getDoors(Dungeon dungeon, Room room) {

        List<Cell> doors = new ArrayList<>();

        for (int row = room.getY(); row < room.getY() + room.getH(); row++) {
            for (int col = room.getX(); col < room.getX() + room.getW(); col++) {

                Cell cell = dungeon.getCell(col, row);

                //we are looking for doors
                if (!cell.isDoor())
                    continue;

                //also make sure it's linked to another door in another room
                if (cell.getLink() != null)
                    doors.add(cell);
            }
        }

        return doors;
    }

    private static int getRowN(Room room) {
        return (room.getY() + room.getH() - 5);
    }

    private static int getRowS(Room room) {
        return (room.getY() + 4);
    }

    private static int getColW(Room room) {
        return (room.getX() + 4);
    }

    private static int getColE(Room room) {
        return (room.getX() + room.getW() - 5);
    }
}