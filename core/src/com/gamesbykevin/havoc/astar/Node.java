package com.gamesbykevin.havoc.astar;

public class Node {

    //location
    private final int col, row;

    //unique id of this node
    private final long id;

    //what is the parent of this node
    private long parent = 0;

    //cost from start to this node
    private int distance = 0;

    //cost from this node to finish
    private int heuristic = 0;

    public Node(int col, int row) {

        //pick a random id for this node
        this.id = System.nanoTime();

        //store the location
        this.col = col;
        this.row = row;
    }

    public int getCost() {
        return getDistance() + getHeuristic();
    }

    public int getDistance() {
        return this.distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public void calculateHeuristic(int col, int row) {

        int x = 0;
        int y = 0;

        if (getCol() > col) {
            x = getCol() - col;
        } else {
            x = col - getCol();
        }

        if (getRow() > row) {
            y = getRow() - row;
        } else {
            y = row - getRow();
        }

        //pythagorean theorem
        this.heuristic = (x * x) + (y * y);
    }

    public int getHeuristic() {
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