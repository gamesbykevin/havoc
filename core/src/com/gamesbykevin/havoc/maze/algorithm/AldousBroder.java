package com.gamesbykevin.havoc.maze.algorithm;

import com.gamesbykevin.havoc.maze.Maze;
import com.gamesbykevin.havoc.maze.Room;

import java.util.ArrayList;
import java.util.List;

import static com.gamesbykevin.havoc.maze.MazeHelper.connectRooms;

public class AldousBroder extends Maze {

    public AldousBroder(int cols, int rows) {
        super(cols, rows);
    }

    @Override
    public void generate() {

        //pick a random room
        Room room = getRoom(getRandom().nextInt(getCols()), getRandom().nextInt(getRows()));

        //all possible directions
        List<Integer> directions = new ArrayList<>();

        //continue until we visited every room
        while (!hasVisited()) {

            //flag the current room as visited
            room.setVisited(true);

            //reset our list
            directions.clear();

            if (hasBounds(room, DIRECTION_EAST))
                directions.add(DIRECTION_EAST);
            if (hasBounds(room, DIRECTION_WEST))
                directions.add(DIRECTION_WEST);
            if (hasBounds(room, DIRECTION_NORTH))
                directions.add(DIRECTION_NORTH);
            if (hasBounds(room, DIRECTION_SOUTH))
                directions.add(DIRECTION_SOUTH);

            //pick a random direction
            int direction = directions.get(getRandom().nextInt(directions.size()));

            //get the neighboring room
            Room tmp = getRoom(room, direction);

            //if the room hasn't been visited
            if (!tmp.isVisited()) {

                //join the rooms together
                connectRooms(this, tmp, room);
            }

            //make this the current room
            room = tmp;
        }
    }

    private boolean hasVisited() {

        for (int row = 0; row < getRows(); row++) {
            for (int col = 0; col < getCols(); col++) {
                if (!getRoom(col, row).isVisited())
                    return false;
            }
        }

        //every room has been visited
        return true;
    }
}
