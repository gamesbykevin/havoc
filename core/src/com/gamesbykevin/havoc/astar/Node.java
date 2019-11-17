package com.gamesbykevin.havoc.astar;

import com.gamesbykevin.havoc.guid.GUID;

public class Node {

    //location
    private final int col, row;

    //unique id of this node
    private final String id;

    //what is the parent of this node
    private String parent;

    //cost from start to this node
    private float distance = 0;

    //cost from this node to finish
    private float heuristic = 0;

    public Node(int col, int row) {

        //pick a random id for this node
        this.id = GUID.generate();

        //store the location
        this.col = col;
        this.row = row;
    }

    public float getCost() {
        return getDistance() + getHeuristic();
    }

    public float getDistance() {
        return this.distance;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }

    public void calculateHeuristicManhattan(int col, int row) {
        this.heuristic = (Math.abs(getCol() - col) + Math.abs(getRow() - row));
    }

    public void calculateHeuristicDiagonal(int col, int row) {
        this.heuristic = Math.max(Math.abs(getCol() - col), Math.abs(getRow() - row));
    }

    public float getHeuristic() {
        return this.heuristic;
    }

    public int getCol() {
        return this.col;
    }

    public int getRow() {
        return this.row;
    }

    public void setParent(Node node) {
        setParent(node.getId());
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public String getParent() {
        return this.parent;
    }

    private String getId() {
        return this.id;
    }

    public boolean hasId(String id) {
        return getId().equals(id);
    }
}