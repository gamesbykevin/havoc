package com.gamesbykevin.havoc.maze.algorithm;

import com.gamesbykevin.havoc.maze.Maze;
import com.gamesbykevin.havoc.maze.Room;

import java.util.ArrayList;
import java.util.List;

import static com.gamesbykevin.havoc.maze.MazeHelper.*;

public class BinaryTree extends Maze {

    public BinaryTree(int cols, int rows) {
        super(cols, rows);
    }

    @Override
    public void generate() {

        List<Integer> directions = new ArrayList<>();

        for (int row = 0; row < getRows(); row++) {
            for (int col = 0; col < getCols(); col++) {

                //get the current room
                Room room = getRoom(col, row);

                //remove any existing directions
                directions.clear();

                if (room.getCol() < getCols() - 1)
                    directions.add(DIRECTION_EAST);
                if (room.getCol() == getCols() - 1)
                    directions.add(DIRECTION_WEST);
                if (room.getRow() < getRows() - 1)
                    directions.add(DIRECTION_NORTH);

                //pick random direction
                int direction = directions.get(getRandom().nextInt(directions.size()));

                //connect the rooms together
                connectRooms(this, room, getRoom(room, direction));
            }
        }

        //connect any remaining groups
        connectRemaining(this);

        //maze has been generated
        setGenerated(true);
    }
}