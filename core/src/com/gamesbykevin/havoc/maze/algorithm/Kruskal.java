package com.gamesbykevin.havoc.maze.algorithm;

import com.gamesbykevin.havoc.maze.Location;
import com.gamesbykevin.havoc.maze.Maze;
import com.gamesbykevin.havoc.maze.Room;

import java.util.ArrayList;
import java.util.List;

import static com.gamesbykevin.havoc.maze.MazeHelper.*;

public class Kruskal extends Maze {

    public Kruskal(int cols, int rows) {
        super(cols, rows);
    }

    @Override
    public void generate() {

        List<Location> options = new ArrayList<>();

        for (int row = 0; row < getRows(); row++) {
            for (int col = 0; col < getCols(); col++) {
                options.add(new Location(col, row));
            }
        }

        //continue until all rooms are part of the same group
        while (!options.isEmpty()) {

            //pick random location
            int index = getRandom().nextInt(options.size());
            Location location = options.get(index);

            //get the room at the location
            Room room = getRoom(location);

            //connect this room to a neighbor
            connectRoom(this, room);

            //remove from the list
            options.remove(index);
        }

        //connect any remaining groups
        connectRemaining(this);

        //flag generated
        setGenerated(true);
    }
}