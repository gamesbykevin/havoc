package com.gamesbykevin.havoc.dungeon;

import java.util.ArrayList;
import java.util.List;

import static com.gamesbykevin.havoc.dungeon.RoomHelper.ROOM_DIMENSION_MAX;
import static com.gamesbykevin.havoc.util.Distance.getDistance;

public class LeafHelper {

    //what is the smallest allowed size of a leaf, before we spawn a room inside
    public static final int LEAF_DIMENSION_MIN = ROOM_DIMENSION_MAX + 1;

    protected static List<Room> getRooms(List<Leaf> leafs, Leaf parent) {

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

        //we want to turn a wall into a door
        if (!cell.isWall())
            return false;

        //door can't be on the edge
        if (cell.getCol() <= 0 || cell.getCol() >= dungeon.getCols() - 1)
            return false;
        if (cell.getRow() <= 0 || cell.getRow() >= dungeon.getRows() - 1)
            return false;

        boolean valid = false;

        //there has to be walls on opposite sides
        if (dungeon.getCells()[cell.getRow()][cell.getCol() - 1].isWall() && dungeon.getCells()[cell.getRow()][cell.getCol() + 1].isWall())
            valid = true;
        if (dungeon.getCells()[cell.getRow() - 1][cell.getCol()].isWall() && dungeon.getCells()[cell.getRow() + 1][cell.getCol()].isWall())
            valid = true;

        if (!valid)
            return false;

        int countOpen = 0;

        for (int col = -1; col <= 1; col++) {
            for (int row = -1; row <= 1; row++) {

                if ((col != 0 && row != 0) || (col == 0 && row == 0))
                    continue;

                Cell tmp = dungeon.getCells()[cell.getRow() + row][cell.getCol() - col];

                //we only want to check cells that are part of the same room
                if (!cell.hasId(tmp))
                    continue;

                //count the open and wall #
                if (tmp.isOpen())
                    countOpen++;
            }
        }

        //wall should have 1 open space
        if (countOpen == 1)
            return true;

        return false;
    }

    protected static double getDistanceVertical(Room north, Room south) {
        return getDistance(north.getX() + (north.getW() / 2), north.getY() + north.getH() - 1, south.getX() + (south.getW() / 2), south.getY());
    }

    protected static double getDistanceHorizontal(Room west, Room east) {
        return getDistance(west.getX(), west.getY() + (west.getH() / 2), east.getX() + east.getW() - 1, east.getY() + (east.getH() / 2));
    }

    public static List<Leaf> getLeafRooms(Dungeon dungeon) {

        //list of valid leaves to spawn collectibles
        List<Leaf> leaves = new ArrayList<>();

        for (int i = 0; i < dungeon.getLeafs().size(); i++) {

            Leaf leaf = dungeon.getLeafs().get(i);

            //only want leafs containing rooms
            if (leaf.hasChildren() || leaf.getRoom() == null)
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

    protected static Leaf getLeaf(Dungeon dungeon, String id) {

        for (int i = 0; i < dungeon.getLeafs().size(); i++) {

            Leaf leaf = dungeon.getLeafs().get(i);

            if (leaf.hasId(id))
                return leaf;
        }

        //leaf not found
        return null;
    }

    protected static boolean isRoom(List<Leaf> leaves, Cell cell) {
        return isRoom(leaves, cell.getCol(), cell.getRow());
    }

    protected static boolean isRoom(List<Leaf> leaves, float x, float y) {

        for (int i = 0; i < leaves.size(); i++) {

            Leaf leaf = leaves.get(i);

            //we only want to check the rooms
            if (leaf.hasChildren() || leaf.getRoom() == null)
                continue;

            if (leaf.getRoom().contains(x, y))
                return true;
        }

        return false;
    }
}