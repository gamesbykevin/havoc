package com.gamesbykevin.havoc.maze;

import java.util.Random;

public abstract class Maze implements IMaze {

    //how big is the maze?
    private final int cols, rows;

    //where do we start?
    private int startCol = 0, startRow = 0;

    //where do we end?
    private int goalCol = 0, goalRow = 0;

    //the rooms in our maze
    private Room[][] rooms;

    //has this maze been generated?
    private boolean generated = false;

    //object used to make random decisions
    private static Random RANDOM;

    //seed used to generate random numbers etc...
    private final long seed;

    //different directions
    public static final int DIRECTION_NONE = 0;
    public static final int DIRECTION_NORTH = 1;
    public static final int DIRECTION_SOUTH = 2;
    public static final int DIRECTION_WEST = 3;
    public static final int DIRECTION_EAST = 4;

    public Maze(int cols, int rows) {
        this(cols, rows, System.currentTimeMillis());
    }

    public Maze(int cols, int rows, long seed) {

        //store the size of the maze
        this.cols = cols;
        this.rows = rows;

        //store the seed and create the random object
        this.seed = seed;
        RANDOM = new Random(seed);

        //create our array of rooms
        int count = 0;
        this.rooms = new Room[rows][cols];
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                this.rooms[row][col] = new Room(col, row);
                this.rooms[row][col].setGroup(count + "");
                count++;
            }
        }
    }

    public long getSeed() {
        return this.seed;
    }

    public static Random getRandom() {
        return RANDOM;
    }

    public boolean isGenerated() {
        return this.generated;
    }

    public void setGenerated(boolean generated) {
        this.generated = generated;
    }

    public boolean hasBounds(Room room, int direction) {

        switch (direction) {
            case DIRECTION_EAST:
                return hasBounds(room.getCol() + 1, room.getRow());
            case DIRECTION_WEST:
                return hasBounds(room.getCol() - 1, room.getRow());
            case DIRECTION_NORTH:
                return hasBounds(room.getCol(), room.getRow() + 1);
            case DIRECTION_SOUTH:
                return hasBounds(room.getCol(), room.getRow() - 1);
        }

        return false;
    }

    private boolean hasBounds(int col, int row) {

        //if we are not in bounds return false
        if (col < 0)
            return false;
        if (row < 0)
            return false;
        if (col >= getRooms()[0].length)
            return false;
        if (row >= getRooms().length)
            return false;

        //we are in bounds
        return true;
    }

    public Room getRoom(Room room, int direction) {

        switch (direction) {
            case DIRECTION_WEST:
                return getRoom(room.getCol() - 1, room.getRow());
            case DIRECTION_EAST:
                return getRoom(room.getCol() + 1, room.getRow());
            case DIRECTION_NORTH:
                return getRoom(room.getCol(), room.getRow() + 1);
            case DIRECTION_SOUTH:
                return getRoom(room.getCol(), room.getRow() - 1);
        }

        //room not found
        return null;
    }

    public Room getRoom(Location location) {
        return getRoom(location.col, location.row);
    }

    public Room getRoom(int col, int row) {

        if (!hasBounds(col, row))
            return null;

        return getRooms()[row][col];
    }

    public Room[][] getRooms() {
        return this.rooms;
    }

    public int getStartCol() {
        return this.startCol;
    }

    public void setStartCol(int startCol) {
        this.startCol = startCol;
    }

    public int getStartRow() {
        return this.startRow;
    }

    public void setStartRow(int startRow) {
        this.startRow = startRow;
    }

    public int getGoalCol() {
        return this.goalCol;
    }

    public void setGoalCol(int goalCol) {
        this.goalCol = goalCol;
    }

    public int getGoalRow() {
        return this.goalRow;
    }

    public void setGoalRow(int goalRow) {
        this.goalRow = goalRow;
    }

    public int getCols() {
        return cols;
    }

    public int getRows() {
        return rows;
    }
}