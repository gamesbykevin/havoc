package com.gamesbykevin.havoc.maze;

import static com.gamesbykevin.havoc.maze.Maze.*;

public class Location {

    public final int col, row;

    public Location(Room room, int direction) {

        switch (direction) {

            case DIRECTION_WEST:
                this.col = room.getCol() - 1;
                this.row = room.getRow();
                break;

            case DIRECTION_EAST:
                this.col = room.getCol() + 1;
                this.row = room.getRow();
                break;

            case DIRECTION_SOUTH:
                this.col = room.getCol();
                this.row = room.getRow() - 1;
                break;

            case DIRECTION_NORTH:
                this.col = room.getCol();
                this.row = room.getRow() + 1;
                break;

            default:
                this.col = room.getCol();
                this.row = room.getRow();
                break;
        }
    }

    public Location(int col, int row) {
        this.col = col;
        this.row = row;
    }
}