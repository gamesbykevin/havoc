package com.gamesbykevin.havoc.maze.algorithm;

import com.gamesbykevin.havoc.maze.Maze;
import com.gamesbykevin.havoc.maze.Room;

public class Sidewinder extends Maze {

    public Sidewinder(int cols, int rows) {
        super(cols, rows);
    }

    @Override
    public void generate() {

        int start = 0;

        for (int row = 0; row < getRows(); row++) {
            for (int col = 0; col < getCols(); col++) {

                Room room = getRoom(col, row);

                if (col < getCols() - 1) {

                    if (getRandom().nextBoolean()) {
                        room.setEast(false);
                        getRoom(col + 1, row).setWest(false);
                    } else {

                        if (row < getRows() - 1) {
                            int range = col - start;
                            int offset = 0;

                            if (range > 0)
                                offset = getRandom().nextInt(range);

                            //connect the rooms
                            getRoom(start + offset, row).setNorth(false);
                            getRoom(start + offset, row + 1).setSouth(false);
                        } else {
                            room.setEast(false);
                            getRoom(col + 1, row).setWest(false);
                        }

                        start = col + 1;
                    }

                } else {

                    if (row < getRows() - 1) {
                        int range = col - start;
                        int offset = 0;

                        if (range > 0)
                            offset = getRandom().nextInt(range);

                        //connect the rooms
                        getRoom(start + offset, row).setNorth(false);
                        getRoom(start + offset, row + 1).setSouth(false);
                    }

                    start = 0;
                }
            }
        }
    }
}
