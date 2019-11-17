package com.gamesbykevin.havoc.dungeon;

import java.util.ArrayList;
import java.util.List;

import static com.gamesbykevin.havoc.dungeon.RoomHelper.ROOM_DIMENSION_MAX;

public class LeafHelper {

    //what is the smallest allowed size of a leaf, before we spawn a room inside
    public static final int LEAF_DIMENSION_MIN = ROOM_DIMENSION_MAX + 1;

    //find the room furthest north
    protected static List<Room> getRoomsNorth(List<Leaf> leafs, Leaf parent) {
        return getRooms(leafs, parent, 2);
    }

    //find the room furthest south
    protected static List<Room> getRoomsSouth(List<Leaf> leafs, Leaf parent) {
        return getRooms(leafs, parent, 3);
    }

    //find the room furthest west
    protected static List<Room> getRoomsWest(List<Leaf> leafs, Leaf parent) {
        return getRooms(leafs, parent, 1);
    }

    //find the room furthest east
    protected static List<Room> getRoomsEast(List<Leaf> leafs, Leaf parent) {
        return getRooms(leafs, parent, 0);
    }

    private static List<Room> getRooms(List<Leaf> leafs, Leaf parent, int direction) {

        List<Room> rooms = new ArrayList<>();

        //if this doesn't have children it is the child room
        if (!parent.hasChildren()) {
            rooms.add(parent.getRoom());
            return rooms;
        }

        for (int i = 0; i < leafs.size(); i++) {

            Leaf child = leafs.get(i);

            //only interested in leafs with rooms
            if (child.hasChildren())
                continue;

            //child has to belong to the parent for us to continue
            if (!hasParent(leafs, parent, child))
                continue;

            //add the child room to the list of rooms
            rooms.add(child.getRoom());
        }

        return rooms;
    }

    protected static boolean hasParent(List<Leaf> leafs, Leaf parent, Leaf child) {

        //we can't do any further, return false
        if (child.getParentId() == null)
            return false;

        //we found our parent, return true
        if (parent.hasId(child.getParentId()))
            return true;

        //keep searching for the parent
        for (int i = 0; i < leafs.size(); i++) {

            Leaf leaf = leafs.get(i);

            //if parent matches child, we make a recursive call
            if (leaf.hasId(child.getParentId())) {
                return hasParent(leafs, parent, leaf);
            }
        }

        //we don't have the parent
        return false;
    }

    //here we check our neighbors to determine if a door can be placed here
    protected static boolean isDoorValid(Dungeon dungeon, Cell cell) {

        if (cell.getType() != Cell.Type.Wall)
            return false;

        if (cell.getCol() <= 0 || cell.getCol() >= dungeon.getCols() - 1)
            return false;
        if (cell.getRow() <= 0 || cell.getRow() >= dungeon.getRows() - 1)
            return false;

        boolean valid = false;

        if (dungeon.getCells()[cell.getRow()][cell.getCol() - 1].getType() == Cell.Type.Wall && dungeon.getCells()[cell.getRow()][cell.getCol() + 1].getType() == Cell.Type.Wall)
            valid = true;
        if (dungeon.getCells()[cell.getRow() - 1][cell.getCol()].getType() == Cell.Type.Wall && dungeon.getCells()[cell.getRow() + 1][cell.getCol()].getType() == Cell.Type.Wall)
            valid = true;

        if (!valid)
            return false;

        int countOpen = 0;
        int countWall = 0;

        for (int col = -1; col <= 1; col++) {
            for (int row = -1; row <= 1; row++) {

                if ((col != 0 && row != 0) || (col == 0 && row == 0))
                    continue;

                Cell tmp = dungeon.getCells()[cell.getRow() + row][cell.getCol() - col];

                //we only want to check cells that are part of the same room
                if (!cell.hasId(tmp))
                    continue;

                //count the open and wall #
                switch (tmp.getType()) {

                    case Open:
                        countOpen++;
                        break;

                    case Wall:
                        countWall++;
                        break;
                }
            }
        }

        //wall should have 1 open space next to it and at least 2 walls
        if (countOpen == 1 && countWall == 2)
            return true;

        return false;
    }

    protected static double getDistanceVertical(Room north, Room south) {
        return getDistance(north.getX() + (north.getW() / 2), north.getY() + north.getH() - 1, south.getX() + (south.getW() / 2), south.getY());
    }

    protected static double getDistanceHorizontal(Room west, Room east) {
        return getDistance(west.getX(), west.getY() + (west.getH() / 2), east.getX() + east.getW() - 1, east.getY() + (east.getH() / 2));
    }

    protected static double getDistance(int x1, int y1, int x2, int y2) {
        return Math.sqrt(((y2 - y1) * (y2 - y1)) + ((x2 - x1) * (x2 - x1)));
    }

    public static List<Leaf> getLeafRooms(Dungeon dungeon) {

        //list of valid leaves to spawn collectibles
        List<Leaf> leaves = new ArrayList<>();

        for (int i = 0; i < dungeon.getLeafs().size(); i++) {

            Leaf leaf = dungeon.getLeafs().get(i);

            //only want leafs containing rooms
            if (leaf.getRoom() == null)
                continue;

            //skip the starting room
            if (leaf.getRoom().contains(dungeon.getStartCol(), dungeon.getStartRow()))
                continue;

            //skip the goal room
            if (leaf.getRoom().contains(dungeon.getGoalCol(), dungeon.getGoalRow()))
                continue;

            //add the leaf to the possible options
            leaves.add(leaf);
        }

        //return our list
        return leaves;
    }
}