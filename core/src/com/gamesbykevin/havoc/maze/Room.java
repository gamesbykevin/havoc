package com.gamesbykevin.havoc.maze;

public class Room {

    //where do we have walls in our room?
    private boolean west;
    private boolean east;
    private boolean north;
    private boolean south;

    //has the room been visited?
    private boolean visited;

    //what is the cost of the room?
    private int cost;

    //in case we need to group the rooms together
    private String group;

    public static final int DEFAULT_COST = 0;

    private final int col, row;

    public Room(int col, int row) {

        //store the room location
        this.col = col;
        this.row = row;

        setWest(true);
        setEast(true);
        setNorth(true);
        setSouth(true);
        setCost(DEFAULT_COST);
        setVisited(false);
    }

    public int getCol() {
        return this.col;
    }

    public int getRow() {
        return this.row;
    }

    public String getGroup() {
        return this.group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public boolean isVisited() {
        return this.visited;
    }

    public void setVisited(boolean visited) {
        this.visited = visited;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public boolean hasWest() {
        return this.west;
    }

    public boolean hasEast() {
        return this.east;
    }

    public boolean hasNorth() {
        return this.north;
    }

    public boolean hasSouth() {
        return this.south;
    }

    public void setEast(boolean east) {
        this.east = east;
    }

    public void setWest(boolean west) {
        this.west = west;
    }

    public void setNorth(boolean north) {
        this.north = north;
    }

    public void setSouth(boolean south) {
        this.south = south;
    }
}