package com.gamesbykevin.havoc.astar;

public class Node {

    //location
    private final int col, row;

    //unique id of this node
    private final long id;

    //what is the parent of this node
    private long parent = -1;

    //cost from start to this node
    private float distance = 0;

    //cost from this node to finish
    private float heuristic = 0;

    public Node(int col, int row) {

        //pick a random id for this node
        this.id = System.nanoTime();

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

    public void setParent(long parent) {
        this.parent = parent;
    }

    public long getParent() {
        return this.parent;
    }

    public long getId() {
        return this.id;
    }

}