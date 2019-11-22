package com.gamesbykevin.havoc.dungeon;

import com.gamesbykevin.havoc.astar.Node;

import java.util.ArrayList;
import java.util.List;

import static com.gamesbykevin.havoc.dungeon.Dungeon.getRandom;
import static com.gamesbykevin.havoc.dungeon.Cell.Type;
import static com.gamesbykevin.havoc.dungeon.LeafHelper.*;
import static com.gamesbykevin.havoc.dungeon.RoomHelper.*;

public class DungeonHelper {

    //how big is the dungeon
    public static final int DUNGEON_SIZE = 100;

    public static int getCount(Dungeon dungeon, String id) {

        int count = 0;

        for (int row = 0; row < dungeon.getRows(); row++) {
            for (int col = 0; col < dungeon.getCols(); col++) {
                if (dungeon.getCells()[row][col].hasId(id))
                    count++;
            }
        }

        //return our count
        return count;
    }

    protected static void divide(Dungeon dungeon) {

        //parent leaf
        Leaf parent = new Leaf(0, 0, dungeon.getCols(), dungeon.getRows());

        //add the parent leaf to the list
        dungeon.getLeafs().add(parent);

        //start dividing
        divide(dungeon, parent);
    }

    //binary space partitioning
    private static void divide(Dungeon dungeon, Leaf parent) {

        //parents have children
        parent.setChildren(true);

        //if we don't meet these dimension requirements we won't be able to split up the rooms
        final int minSplitWidth = LEAF_DIMENSION_MIN * 2;
        final int minSplitHeight = LEAF_DIMENSION_MIN * 2;

        //determine if we can split in either direction
        boolean vertical = (parent.getH() >= minSplitHeight);
        boolean horizontal = (parent.getW() >= minSplitWidth);

        //we can't split anymore if the room is not big enough so we will place a room here
        if (!vertical && !horizontal) {

            //pick a random size for the room
            int cols = getRandom().nextInt(ROOM_DIMENSION_MAX - ROOM_DIMENSION_MIN + 1) + ROOM_DIMENSION_MIN;
            int rows = getRandom().nextInt(ROOM_DIMENSION_MAX - ROOM_DIMENSION_MIN + 1) + ROOM_DIMENSION_MIN;

            //pick a random location for the room in this section
            int startX = getRandom().nextInt(parent.getW() - cols)  + parent.getX();
            int startY = getRandom().nextInt(parent.getH() - rows) + parent.getY();

            //since we stopped dividing this leaf will have no children
            parent.setChildren(false);

            //assign the room in the leaf
            parent.setRoom(new Room(startX, startY, cols, rows));

            //now we can create a room
            placeRoom(dungeon, parent.getRoom(), (parent.getX() == 0 && parent.getY() == 0));

            //no need to continue
            return;
        }

        //if we can split both ways, pick one at random
        if (vertical && horizontal) {
            vertical = getRandom().nextBoolean();
            horizontal = !vertical;
        }

        if (vertical) {

            //decide where to divide
            int minRow = LEAF_DIMENSION_MIN;
            int maxRow = parent.getH() - LEAF_DIMENSION_MIN;
            int splitHeight = (maxRow - minRow > 0) ? getRandom().nextInt(maxRow - minRow) + minRow : minRow;

            //flag that this parent has children
            parent.setChildren(true);

            //create the child leafs
            Leaf leaf1 = new Leaf(parent.getX(), parent.getY(), parent.getW(), splitHeight, parent);
            Leaf leaf2 = new Leaf(parent.getX(), parent.getY() + splitHeight, parent.getW(), parent.getH() - splitHeight, parent);

            //add to our list so we can connect later
            dungeon.getLeafs().add(leaf1);
            dungeon.getLeafs().add(leaf2);

            //continue dividing
            divide(dungeon, leaf1);
            divide(dungeon, leaf2);

        } else if (horizontal) {

            //decide where to divide
            int minCol = LEAF_DIMENSION_MIN;
            int maxCol = parent.getW() - LEAF_DIMENSION_MIN;
            int splitWidth = (maxCol - minCol > 0) ? getRandom().nextInt(maxCol - minCol) + minCol : minCol;

            //flag that this parent has children
            parent.setChildren(true);

            //create the child leafs
            Leaf leaf1 = new Leaf(parent.getX(), parent.getY(), splitWidth, parent.getH(), parent);
            Leaf leaf2 = new Leaf(parent.getX() + splitWidth, parent.getY(), parent.getW() - splitWidth, parent.getH(), parent);

            //add to our list so we can connect later
            dungeon.getLeafs().add(leaf1);
            dungeon.getLeafs().add(leaf2);

            //continue dividing
            divide(dungeon, leaf1);
            divide(dungeon, leaf2);
        }
    }

    private static void placeRoom(Dungeon dungeon, Room room, boolean start) {

        //EVERY cell in the room will have the same id
        for (int col = room.getX(); col < room.getX() + room.getW(); col++) {
            for (int row = room.getY(); row < room.getY() + room.getH(); row++) {

                //room will be surrounded by walls
                if (col == room.getX() || col == room.getX() + room.getW() - 1 || row == room.getY() || row == room.getY() + room.getH() - 1) {
                    dungeon.getCells()[row][col].setType(Type.Wall);
                } else {
                    dungeon.getCells()[row][col].setType(Type.Open);
                }

                //every cell in this room will have the same id
                dungeon.getCells()[row][col].setId(dungeon.getCells()[room.getY()][room.getX()]);
            }
        }

        if (!start) {

            switch (getRandom().nextInt(10)) {

                case 0:
                    setupSplit(dungeon, room);
                    break;

                case 1:
                    setupDiamond(dungeon, room);
                    break;

                case 2:
                    setupMiniRooms(dungeon, room);
                    break;

                case 3:
                    setupBox(dungeon, room);
                    break;

                case 4:
                    setupHalfBox(dungeon, room);
                    break;

                case 5:
                    setupThreeSides(dungeon, room);
                    break;

                case 6:
                    setupFourPillars(dungeon, room);
                    break;

                case 7:
                    setupTwoBoxes(dungeon, room);
                    break;

                case 8:
                    setupX(dungeon, room);
                    break;

                case 9:
                default:
                    //empty room
                    break;
            }
        }
    }

    //connect rooms together
    protected static void connect(Dungeon dungeon) {

        //make sure map is updated accordingly
        dungeon.updateMap();

        List<Leaf> children = new ArrayList<>();

        //start the initial list by adding all children
        for (int i = 0; i < dungeon.getLeafs().size(); i++) {

            Leaf child = dungeon.getLeafs().get(i);

            //we want to start with the ultimate child
            if (child.hasChildren())
                continue;

            children.add(child);
        }

        //start connecting the children together
        connect(dungeon, children);
    }

    private static void connect(Dungeon dungeon, List<Leaf> children) {

        //no need to continue if empty
        if (children.isEmpty())
            return;

        //create a list of parents
        List<Leaf> parents = new ArrayList<>();

        for (int i = 0; i < children.size(); i++) {

            Leaf sibling1 = children.get(i);

            //don't bother if we already merged
            if (sibling1.isMerged())
                continue;

            //get the parent leaf of the siblings
            Leaf parent = null;

            for (int j = i + 1; j < children.size(); j++) {

                Leaf sibling2 = children.get(j);

                //don't bother if we already merged
                if (sibling2.isMerged())
                    continue;

                //we can't connect self
                if (sibling1.hasId(sibling2))
                    continue;

                //if these don't share the same parent we can't connect
                if (sibling1.getParentId() != sibling2.getParentId() || sibling1.getParentId() == null)
                    continue;

                //we can connect the siblings together
                connectSiblings(dungeon, sibling1, sibling2);

                //get the parent
                parent = getLeaf(dungeon.getLeafs(), sibling1.getParentId());
            }

            //check for match
            boolean match = false;

            //no parent means we possibly didn't merge siblings
            if (parent == null && sibling1.getParentId() != null) {

                //we didn't join the leaf so let's find it's sibling
                for (int x = 0; x < dungeon.getLeafs().size(); x++) {

                    Leaf leaf = dungeon.getLeafs().get(x);

                    if (leaf.getParentId() != sibling1.getParentId() || leaf.hasId(sibling1))
                        continue;

                    //we can connect the siblings together
                    connectSiblings(dungeon, sibling1, leaf);

                    //get the parent
                    parent = getLeaf(dungeon.getLeafs(), sibling1.getParentId());

                    //exit loop
                    break;
                }
            }

            if (parent != null) {

                //make sure it isn't already part of the list
                for (int x = 0; x < parents.size(); x++) {
                    if (parents.get(x).hasId(parent)) {
                        match = true;
                        break;
                    }
                }

                //add the parent to the list if it doesn't exist yet
                if (!match)
                    parents.add(parent);
            }
        }

        //connect the parents together
        connect(dungeon, parents);
    }

    private static Leaf getLeaf(List<Leaf> leafs, String id) {

        for (int i = 0; i < leafs.size(); i++) {

            Leaf leaf = leafs.get(i);

            if (leaf.hasId(id))
                return leaf;
        }

        //not found
        return null;
    }

    private static void connectSiblings(Dungeon dungeon, Leaf sibling1, Leaf sibling2) {

        //flag that these are now merged
        sibling1.setMerged(true);
        sibling2.setMerged(true);

        //the start and goal
        Cell start = null, goal = null;

        //where do we connect
        if (sibling1.getX() != sibling2.getX()) {

            //two rooms
            Room west = null, east = null;

            List<Room> roomsWest;
            List<Room> roomsEast;

            if (sibling1.getX() > sibling2.getX()) {
                roomsWest = getRoomsWest(dungeon.getLeafs(), sibling1);
                roomsEast = getRoomsEast(dungeon.getLeafs(), sibling2);
            } else {
                roomsWest = getRoomsWest(dungeon.getLeafs(), sibling2);
                roomsEast = getRoomsEast(dungeon.getLeafs(), sibling1);
            }

            //the distance to beat
            double distance = -1;

            //compare rooms to find which 2 are closest together
            for (int i = 0; i < roomsWest.size(); i++) {
                for (int j = 0; j < roomsEast.size(); j++) {

                    Room roomWest = roomsWest.get(i);
                    Room roomEast = roomsEast.get(j);

                    //if these 2 rooms are closest we have a winner
                    if (distance < 0 || getDistanceHorizontal(roomWest, roomEast) < distance) {
                        west = roomWest;
                        east = roomEast;
                        distance = getDistanceHorizontal(roomWest, roomEast);
                    }
                }
            }

            int size = -1;

            for (int rowWest = west.getY(); rowWest < west.getY() + west.getH(); rowWest++) {
                for (int rowEast = east.getY(); rowEast < east.getY() + east.getH(); rowEast++) {

                    Cell cell1 = dungeon.getCells()[rowWest][west.getX()];
                    Cell cell2 = dungeon.getCells()[rowEast][east.getX() + east.getW() - 1];

                    //if the doors are valid let's check the distance
                    if (isDoorValid(dungeon, cell1) && isDoorValid(dungeon, cell2)) {

                        //make a door for now so we can calculate the path
                        cell1.setType(Type.Door);
                        cell2.setType(Type.Door);

                        //calculate path from start to finish
                        dungeon.updateMap();
                        dungeon.getAStar().setDiagonal(false);
                        dungeon.getAStar().calculate(cell1.getCol(), cell1.getRow(), cell2.getCol(), cell2.getRow());

                        //if the path is shorter, we'll use this one then
                        if (size < 0 || dungeon.getAStar().getPath().size() < size) {
                            size = dungeon.getAStar().getPath().size();
                            start = cell1;
                            goal = cell2;
                        }

                        //set back to wall
                        cell1.setType(Type.Wall);
                        cell2.setType(Type.Wall);
                    }
                }
            }

        } else if (sibling1.getY() != sibling2.getY()) {

            //two rooms
            Room north = null, south = null;

            List<Room> roomsNorth;
            List<Room> roomsSouth;

            if (sibling1.getY() > sibling2.getY()) {
                roomsSouth = getRoomsSouth(dungeon.getLeafs(), sibling1);
                roomsNorth = getRoomsNorth(dungeon.getLeafs(), sibling2);
            } else {
                roomsSouth = getRoomsSouth(dungeon.getLeafs(), sibling2);
                roomsNorth = getRoomsNorth(dungeon.getLeafs(), sibling1);
            }

            double distance = -1;

            //determine which 2 rooms to combine
            for (int i = 0; i < roomsNorth.size(); i++) {
                for (int j = 0; j < roomsSouth.size(); j++) {

                    Room roomNorth = roomsNorth.get(i);
                    Room roomSouth = roomsSouth.get(j);

                    //if these 2 rooms are closest we have a winner
                    if (distance < 0 || getDistanceVertical(roomNorth, roomSouth) < distance) {
                        north = roomNorth;
                        south = roomSouth;
                        distance = getDistanceVertical(roomNorth, roomSouth);
                    }
                }
            }

            int size = -1;

            for (int colNorth = north.getX(); colNorth < north.getX() + north.getW(); colNorth++) {
                for (int colSouth = south.getX(); colSouth < south.getX() + south.getW(); colSouth++) {

                    Cell cell1 = dungeon.getCells()[north.getY() + north.getH() - 1][colNorth];
                    Cell cell2 = dungeon.getCells()[south.getY()][colSouth];

                    //if the doors are valid let's check the distance
                    if (isDoorValid(dungeon, cell1) && isDoorValid(dungeon, cell2)) {

                        //make a door (for now)
                        cell1.setType(Type.Door);
                        cell2.setType(Type.Door);

                        //calculate path from start to finish
                        dungeon.updateMap();
                        dungeon.getAStar().setDiagonal(false);
                        dungeon.getAStar().calculate(cell1.getCol(), cell1.getRow(), cell2.getCol(), cell2.getRow());

                        //if the path is shorter, we'll use this one then
                        if (size < 0 || dungeon.getAStar().getPath().size() < size) {
                            size = dungeon.getAStar().getPath().size();
                            start = cell1;
                            goal = cell2;
                        }

                        //set back to wall
                        cell1.setType(Type.Wall);
                        cell2.setType(Type.Wall);
                    }
                }
            }
        }

        //now let's connect the start and goal together
        start.setType(Type.Door);
        start.setLink(goal);
        goal.setType(Type.Door);
        goal.setLink(start);

        //calculate the shortest path
        dungeon.updateMap();
        dungeon.getAStar().setDiagonal(false);
        dungeon.getAStar().calculate(start.getCol(), start.getRow(), goal.getCol(), goal.getRow());

        //update the dungeon
        for (int i = 0; i < dungeon.getAStar().getPath().size(); i++) {
            Node node = dungeon.getAStar().getPath().get(i);

            if (node.getCol() == start.getCol() && node.getRow() == start.getRow())
                continue;
            if (node.getCol() == goal.getCol() && node.getRow() == goal.getRow())
                continue;

            //flag as open on the path
            dungeon.getCells()[node.getRow()][node.getCol()].setType(Type.Open);
        }
    }
}