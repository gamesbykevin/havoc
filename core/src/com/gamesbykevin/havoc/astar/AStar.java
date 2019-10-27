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

            //index of lowest cost node
            int index = -1;

            //lowest cost to beat
            int cost = 0;

            //get the node with the lowest score
            for (int i = 0; i < getOpen().size(); i++) {

                if (index < 0 || getOpen().get(i).getCost() < cost) {

                    //this is the best node
                    index = i;

                    //and this is the lowest cost
                    cost = getOpen().get(i).getCost();
                }
            }

            //get the lowest cost node
            Node currentNode = getOpen().get(index);

            //remove node from the open list
            getOpen().remove(index);

            //and add it to the closed
            getClosed().add(currentNode);

            //no need to continue now that we got our path
            if (currentNode.getCol() == endCol && currentNode.getRow() == endRow)
                break;

            for (int x = -1; x <= 1; x++) {
                for (int y = -1; y <= 1; y++) {

                    //skip diagonal direction and center
                    if (x != 0 && y != 0)
                        continue;
                    if (x == 0 && y == 0)
                        continue;

                    //skip if out of bounds
                    if (currentNode.getRow() + y < 0 || currentNode.getRow() + y >= getMap().length)
                        continue;
                    if (currentNode.getCol() + x < 0 || currentNode.getCol() + x >= getMap()[0].length)
                        continue;

                    //ignore the location if its part of the closed list
                    if (getClosedNode(currentNode.getCol() + x, currentNode.getRow() + y) != null)
                        continue;

                    //ignore if location is not allowed
                    if (!getMap()[currentNode.getRow() + y][currentNode.getCol() + x])
                        continue;

                    //create the new node
                    Node newNode = new Node(currentNode.getCol() + x, currentNode.getRow() + y);

                    //mark current node as the parent
                    newNode.setParent(currentNode);

                    //increase by 1 for the distance cost of the new node
                    newNode.setDistance(currentNode.getCost() + 1);

                    //calculate our distance from the end
                    newNode.calculateHeuristic(endCol, endRow);

                    //get the existing open node (if it exists)
                    Node tmp = getOpenNode(newNode);

                    //add the node if not already in the open list
                    if (tmp == null) {

                        getOpen().add(newNode);

                    } else {

                        //if the distance cost is less than the existing
                        if (newNode.getDistance() < tmp.getDistance()) {
                            tmp.setParent(currentNode);
                            tmp.setDistance(newNode.getDistance());
                            tmp.calculateHeuristic(endCol, endRow);
                        }
                    }
                }
            }
        }

        //we added the target to the closed list now follow it to get the solution path
        calculatePath();
    }

    private void calculatePath() {

        //clear the path so we can create it
        getPath().clear();

        //get the latest node added to the closed list
        Node node = getClosed().get(getClosed().size() - 1);

        //add the node to the path
        getPath().add(node);

        while (true) {

            //get the parent node
            node = getClosedNode(node.getParent());

            //if not found we have our path
            if (node == null)
                break;

            //add the parent to the list
            getPath().add(0, node);
        }

        //clear the lists
        getOpen().clear();
        getClosed().clear();
    }

    private Node getOpenNode(Node node) {
        return getOpenNode(node.getCol(), node.getRow());
    }

    private Node getOpenNode(int col, int row) {

        for (int i = 0; i < getOpen().size(); i++) {

            Node node = getOpen().get(i);

            if (node.getRow() == row && node.getCol() == col)
                return node;
        }

        return null;
    }

    private Node getClosedNode(long id) {

        for (int i = 0; i < getClosed().size(); i++) {

            Node node = getClosed().get(i);

            if (node.getId() == id)
                return node;
        }

        return null;
    }

    private Node getClosedNode(Node node) {
        return getClosedNode(node.getCol(), node.getRow());
    }

    private Node getClosedNode(int col, int row) {

        for (int i = 0; i < getClosed().size(); i++) {

            Node node = getClosed().get(i);

            if (node.getRow() == row && node.getCol() == col)
                return node;
        }

        return null;
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