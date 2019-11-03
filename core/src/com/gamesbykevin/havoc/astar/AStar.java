package com.gamesbykevin.havoc.astar;

import java.util.ArrayList;
import java.util.List;

public class AStar {

    private List<Node> open, closed;
    private List<Node> path;

    //the game map with true representing the obstacles
    private boolean[][] map;

    //do we allow diagonal movement
    private boolean diagonal = false;

    //how much does each move cost
    public static final float MOVE_COST_NORMAL = 1.0f;
    public static final float MOVE_COST_DIAGONAL = 1.4f;

    public AStar() {

        //create our lists
        this.open = new ArrayList<>();
        this.closed = new ArrayList<>();

        //this will contain the path to our goal
        this.path = new ArrayList<>();
    }

    public boolean isDiagonal() {
        return this.diagonal;
    }

    public void setDiagonal(boolean diagonal) {
        this.diagonal = diagonal;
    }

    public void calculate(int startCol, int startRow, int endCol, int endRow) {

        //clear the lists
        getOpen().clear();
        getClosed().clear();
        getPath().clear();

        //create our starting node with a distance cost of 0
        Node node = new Node(startCol, startRow);
        node.setDistance(0);

        //add the start to our list of open nodes
        getOpen().add(node);

        //continue while we have open nodes
        while (!getOpen().isEmpty()) {

            //the lowest cost node
            Node lowest = null;

            int index = -1;

            //get the node with the lowest score on our open list
            for (int i = 0; i < getOpen().size(); i++) {

                //get the current node
                Node current = getOpen().get(i);

                //if the cost is lower, this is the best node (currently)
                if (lowest == null || current.getCost() < lowest.getCost()) {
                    lowest = current;
                    index = i;
                }
            }

            //now that we have the lowest cost node, remove it from the open list
            getOpen().remove(index);

            //and add it to the closed list
            getClosed().add(lowest);

            //when we add the target to the closed list we are done looping
            if (lowest.getCol() == endCol && lowest.getRow() == endRow)
                break;

            //generate the successors for the lowest cost node
            checkSuccessor(lowest, 1, 0, endCol, endRow);
            checkSuccessor(lowest, -1, 0, endCol, endRow);
            checkSuccessor(lowest, 0, 1, endCol, endRow);
            checkSuccessor(lowest, 0, -1, endCol, endRow);

            //if diagonal movement is allowed
            if (isDiagonal()) {
                checkSuccessor(lowest, 1, -1, endCol, endRow);
                checkSuccessor(lowest, 1, 1, endCol, endRow);
                checkSuccessor(lowest, -1, 1, endCol, endRow);
                checkSuccessor(lowest, -1, -1, endCol, endRow);
            }
        }

        //create the solution path
        calculatePath();
    }

    private void checkSuccessor(Node lowest, int col, int row, int endCol, int endRow) {

        //skip the current location
        if (col == 0 && row == 0)
            return;

        //if we aren't checking diagonal then skip this one
        if (!isDiagonal() && (col != 0 && row != 0))
            return;

        int newCol = lowest.getCol() + col;
        int newRow = lowest.getRow() + row;

        //if the location is out of bounds we will skip it
        if (newRow < 0 || newRow >= getMap().length)
            return;
        if (newCol < 0 || newCol >= getMap()[0].length)
            return;

        //is the location free on our map? if not we will skip it
        if (!getMap()[newRow][newCol])
            return;

        boolean ignore = false;

        //check the closed list to see if we have this location
        for (int i = 0; i < getClosed().size(); i++) {

            Node check = getClosed().get(i);

            //we are looking for the same location
            if (check.getCol() != newCol || check.getRow() != newRow)
                continue;

            ignore = true;
            break;
        }

        //skip if we want to ignore
        if (ignore)
            return;

        Node match = null;

        //check the open list to see if we have this location
        for (int i = 0; i < getOpen().size(); i++) {

            Node check = getOpen().get(i);

            //we are looking for the same location
            if (check.getCol() != newCol || check.getRow() != newRow)
                continue;

            //we found a match
            match = check;
            break;
        }

        Node successor = new Node(newCol, newRow);
        successor.setParent(lowest);

        //calculate distance from start differently depending on what direction we are going
        if (col != 0 && row != 0) {
            successor.setDistance(lowest.getDistance() + MOVE_COST_DIAGONAL);
        } else {
            successor.setDistance(lowest.getDistance() + MOVE_COST_NORMAL);
        }

        //calculate the heuristic accordingly
        if (isDiagonal()) {
            successor.calculateHeuristicDiagonal(endCol, endRow);
        } else {
            successor.calculateHeuristicManhattan(endCol, endRow);
        }

        if (match == null) {

            //if it doesn't exist add it to the open list
            getOpen().add(successor);

        } else {

            //else if the successor has a lower distance cost we update the parent and the parent cost
            if (successor.getDistance() < match.getDistance()) {
                match.setParent(lowest);
                match.setDistance(successor.getDistance());
            }
        }
    }

    private void calculatePath() {

        //clear the path so we can create it
        getPath().clear();

        //get the latest node added to the closed list
        Node child = getClosed().get(getClosed().size() - 1);

        //add the node to the path
        getPath().add(child);

        while (true) {

            Node parent = null;

            //look for the parent of the current node
            for (int i = 0; i < getClosed().size(); i++) {

                Node tmp = getClosed().get(i);

                //we found the parent
                if (tmp.getId() == child.getParent()) {
                    parent = tmp;
                    break;
                }
            }

            //if the parent is null we are done
            if (parent == null)
                break;

            //the parent now becomes the child
            child = parent;

            //add the child to the list
            getPath().add(0, child);
        }

        //clear the lists
        getOpen().clear();
        getClosed().clear();
    }

    private List<Node> getOpen() {
        return this.open;
    }

    private List<Node> getClosed() {
        return this.closed;
    }

    public List<Node> getPath() {
        return this.path;
    }

    private boolean[][] getMap() {
        return this.map;
    }

    public void setMap(boolean[][] map) {
        this.map = map;
    }
}