package com.gamesbykevin.havoc.maze.algorithm;

import com.gamesbykevin.havoc.maze.Location;
import com.gamesbykevin.havoc.maze.Maze;
import com.gamesbykevin.havoc.maze.Room;

import java.util.ArrayList;
import java.util.List;

import static com.gamesbykevin.havoc.maze.MazeHelper.*;

public class Recursive extends Maze {

    public Recursive(int cols, int rows) {

        //call parent
        super(cols, rows);
    }

    @Override
    public void generate() {

        List<Location> history = new ArrayList<>();

        Room room = null;

        //continue until the maze has been generated
        while (!hasSingleGroup()) {

            //if null, start
            if (room == null)
                room = getRoom(getRandom().nextInt(getCols()), getRandom().nextInt(getRows()));

            //connect the room
            int direction = connectRoom(this, room);

            //if we weren't successful, we need to back track
            if (direction == DIRECTION_NONE) {

                //get previous location
                if (!history.isEmpty()) {
                    Location location = history.get(history.size() - 1);
                    history.remove(history.size() - 1);
                    room = getRoom(location);
                }

            } else {

                //get the random room
                room = getRoom(room, direction);

                //if there are additional directions we will need to track back here eventually
                if (!getDirections(this, room).isEmpty())
                    history.add(new Location(room.getCol(), room.getRow()));
            }
        }

        //maze has been generated
        setGenerated(true);
    }

    private boolean hasSingleGroup() {

        Room room = getRoom(0, 0);

        for (int row = 0; row < getRows(); row++) {
            for (int col = 0; col < getCols(); col++) {

                //no match, so there is more than 1 group
                if (!getRoom(col, row).getGroup().equals(room.getGroup()))
                    return false;
            }
        }

        //only 1 group found
        return true;
    }
}