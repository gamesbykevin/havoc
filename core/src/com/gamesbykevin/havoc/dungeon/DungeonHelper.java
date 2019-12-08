package com.gamesbykevin.havoc.dungeon;

import com.gamesbykevin.havoc.astar.Node;
import com.gamesbykevin.havoc.collectibles.Collectibles;

import java.util.ArrayList;
import java.util.List;

import static com.gamesbykevin.havoc.dungeon.Dungeon.getRandom;
import static com.gamesbykevin.havoc.dungeon.Cell.Type;
import static com.gamesbykevin.havoc.dungeon.LeafHelper.*;
import static com.gamesbykevin.havoc.dungeon.RoomHelper.*;

public class DungeonHelper {

    //how big is the dungeon
    public static final int DUNGEON_SIZE = ROOM_DIMENSION_MAX * 5;

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

        //we need to figure out which rooms we are going to connect together
        Room room1 = null, room2 = null;
        List<Room> rooms1, rooms2;

        //the start and end door of those rooms to be connected
        Cell start = null, goal = null;

        boolean horizontal = (sibling1.getX() != sibling2.getX());

        if (horizontal) {
            rooms1 = getRooms(dungeon.getLeafs(), (sibling1.getX() > sibling2.getX()) ? sibling1 : sibling2);
            rooms2 = getRooms(dungeon.getLeafs(), (sibling1.getX() > sibling2.getX()) ? sibling2 : sibling1);
        } else {
            rooms1 = getRooms(dungeon.getLeafs(), (sibling1.getY() > sibling2.getY()) ? sibling1 : sibling2);
            rooms2 = getRooms(dungeon.getLeafs(), (sibling1.getY() > sibling2.getY()) ? sibling2 : sibling1);
        }

        //the distance to beat
        double distance = -1;

        //compare rooms to find which 2 are closest together
        for (int i = 0; i < rooms1.size(); i++) {
            for (int j = 0; j < rooms2.size(); j++) {

                Room tmp1 = rooms1.get(i);
                Room tmp2 = rooms2.get(j);

                //calculate the distance
                double dist = (horizontal) ? getDistanceHorizontal(tmp1, tmp2) : getDistanceVertical(tmp1, tmp2);

                //if these 2 rooms are closest we have a winner
                if (distance < 0 || dist < distance) {
                    room1 = tmp1;
                    room2 = tmp2;
                    distance = dist;
                }
            }
        }

        int diff = -1;

        int offset = 3;

        //where do we connect
        if (horizontal) {

            Room roomW = (room1.getX() < room2.getX()) ? room1 : room2;
            Room roomE = (room1.getX() < room2.getX()) ? room2 : room1;

            //we prefer to connect rooms at the center
            int middleW = roomW.getY() + (roomW.getH() / 2);
            int middleE = roomW.getY() + (roomE.getH() / 2);

            for (int rowW = roomW.getY() + offset; rowW < roomW.getY() + roomW.getH() - offset; rowW++) {
                for (int rowE = roomE.getY() + offset; rowE < roomE.getY() + roomE.getH() - offset; rowE++) {

                    Cell cell1 = dungeon.getCells()[rowW][roomW.getX() + roomW.getW() - 1];
                    Cell cell2 = dungeon.getCells()[rowE][roomE.getX()];

                    if (!isDoorValid(dungeon, cell1) || !isDoorValid(dungeon, cell2))
                        continue;

                    //calculate the difference
                    int difference = Math.abs(rowW - rowE);

                    //penalize for being away from the center
                    //difference += Math.abs(rowE - middleE);
                    //difference += Math.abs(rowW - middleW);

                    if (diff < 0 || difference < diff) {
                        start = cell1;
                        goal = cell2;
                        diff = difference;
                    }
                }
            }

        } else if (sibling1.getY() != sibling2.getY()) {

            Room roomN = (room1.getY() < room2.getY()) ? room2 : room1;
            Room roomS = (room1.getY() < room2.getY()) ? room1 : room2;

            //we prefer to connect rooms at the center
            int middleN = roomN.getX() + (roomN.getW() / 2);
            int middleS = roomS.getX() + (roomS.getW() / 2);

            for (int colN = roomN.getX() + offset; colN < roomN.getX() + roomN.getW() - offset; colN++) {
                for (int colS = roomS.getX() + offset; colS < roomS.getX() + roomS.getW() - offset; colS++) {

                    Cell cell1 = dungeon.getCells()[roomN.getY()][colN];
                    Cell cell2 = dungeon.getCells()[roomS.getY() + roomS.getH() - 1][colS];

                    if (!isDoorValid(dungeon, cell1) || !isDoorValid(dungeon, cell2))
                        continue;

                    //calculate the difference
                    int difference = Math.abs(colN - colS);

                    //penalize for being away from the center
                    //difference += Math.abs(colN - middleN);
                    //difference += Math.abs(colS - middleS);

                    if (diff < 0 || difference < diff) {
                        start = cell1;
                        goal = cell2;
                        diff = difference;
                    }
                }
            }
        }

        //let's link the start and goal together
        start.setType(Type.Door);
        start.setLink(goal);
        goal.setType(Type.Door);
        goal.setLink(start);

        //calculate path from start to finish
        dungeon.updateMap();
        dungeon.getAStar().setDiagonal(false);
        dungeon.getAStar().calculate(start.getCol(), start.getRow(), goal.getCol(), goal.getRow());

        //update the dungeon marking the open spaces
        for (int i = 1; i < dungeon.getAStar().getPath().size() - 1; i++) {
            Node node = dungeon.getAStar().getPath().get(i);
            dungeon.getCells()[node.getRow()][node.getCol()].setType(Type.Open);
        }
    }

    protected static void lockDoor(Dungeon dungeon) {

        List<Cell> options = new ArrayList<>();

        //mark goal open so we can calculate path to it
        dungeon.getCells()[dungeon.getGoalRow()][dungeon.getGoalCol()].setType(Type.Open);
        dungeon.updateMap();
        dungeon.getAStar().setDiagonal(false);

        //calculate shortest path to goal
        dungeon.getAStar().calculate(dungeon.getStartCol(), dungeon.getStartRow(), dungeon.getGoalCol(), dungeon.getGoalRow());

        //now that we made our calculation we can set it back to the goal
        dungeon.getCells()[dungeon.getGoalRow()][dungeon.getGoalCol()].setType(Type.Goal);
        dungeon.updateMap();

        //create a list of doors we can possibly lock
        for (int i = 0; i < dungeon.getAStar().getPath().size(); i++) {

            Node node = dungeon.getAStar().getPath().get(i);
            Cell cell = dungeon.getCells()[node.getRow()][node.getCol()];

            if (!cell.isDoor())
                continue;
            if (cell.isSecret() || cell.isGoal())
                continue;

            options.add(cell);
        }

        //if there are no options we can't lock the door
        if (options.isEmpty())
            return;

        //pick random index to lock a door
        int index = -1;

        //minimum cost to place a key
        int costMin;

        //we don't want to place a key/lock at the start
        int offsetStart = 2;

        //if the level is large enough we can avoid locking a door right at the start of the level
        if ((options.size() / 2) > offsetStart)
            offsetStart = (options.size() / 2);

        if (options.size() >= offsetStart) {

            //adjust the minimum cost so we don't place a key at the beginning
            costMin = options.get(offsetStart).getCost();

            //which door do we lock?
            if (options.size() > offsetStart) {
                index = getRandom().nextInt(options.size() - (offsetStart + 1));
            } else {
                index = 0;
            }

            index += (offsetStart + 1);

        } else {

            //lock any random door since the level is too small
            index = getRandom().nextInt(options.size());

            //any cost above -1 is valid to place the key
            costMin = -1;
        }

        //pick a random door to be locked
        Cell choice = options.get(index);

        //clear previous list
        options.clear();

        //look for open cells that meet the cost requirement so we can spawn a key
        for (int row = 0; row < dungeon.getRows(); row++) {
            for (int col = 0; col < dungeon.getCols(); col++) {

                Cell tmp = dungeon.getCells()[row][col];

                //only place key on an open cell
                if (!tmp.isOpen())
                    continue;

                //only spawn keys in rooms, not hallways
                if (!isRoom(dungeon.getLeafs(), tmp))
                    continue;

                //keep away from the start location
                if (tmp.getCol() == dungeon.getStartCol() || tmp.getRow() == dungeon.getStartRow())
                    continue;

                //the cost has to be lower than the locked door and meet our cost requirement
                if (tmp.getCost() < choice.getCost() && tmp.getCost() > costMin)
                    options.add(tmp);
            }
        }

        //we will only lock a door and place a key if options are available
        if (!options.isEmpty()) {

            //make ur choice a locked door
            choice.setType(Type.DoorLocked);

            //now we pick a random place for the key
            index = getRandom().nextInt(options.size());
            choice = options.get(index);

            //and add the key to the chosen location
            ((Collectibles)dungeon.getLevel().getCollectibles()).add(Collectibles.Type.key, choice.getCol(), choice.getRow());
        }

        //cleanup
        options.clear();
        options = null;
    }
}