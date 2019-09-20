package com.gamesbykevin.havoc.maze.algorithm;

import com.gamesbykevin.havoc.maze.Maze;
import com.gamesbykevin.havoc.maze.Room;

import java.util.ArrayList;
import java.util.List;

import static com.gamesbykevin.havoc.maze.MazeHelper.connectRooms;

public class HuntKill extends Maze {

    public HuntKill(int cols, int rows) {
        super(cols, rows);
    }

    @Override
    public void generate() {

        Room room = null;

        //create list of potential locations
        List<Location> locations = new ArrayList<>();

        while (!isGenerated()) {

            //start at a random location
            if (room == null)
                room = getRoom(getRandom().nextInt(getCols()), getRandom().nextInt(getRows()));

            //clear the list
            locations.clear();

            //locate items nearby
            if (hasBounds(room, DIRECTION_WEST) && !getRoom(room, DIRECTION_WEST).getGroup().equals(room.getGroup()))
                locations.add(new Location(room, DIRECTION_WEST));
            if (hasBounds(room, DIRECTION_EAST) && !getRoom(room, DIRECTION_EAST).getGroup().equals(room.getGroup()))
                locations.add(new Location(room, DIRECTION_EAST));
            if (hasBounds(room, DIRECTION_SOUTH) && !getRoom(room, DIRECTION_SOUTH).getGroup().equals(room.getGroup()))
                locations.add(new Location(room, DIRECTION_SOUTH));
            if (hasBounds(room, DIRECTION_NORTH) && !getRoom(room, DIRECTION_NORTH).getGroup().equals(room.getGroup()))
                locations.add(new Location(room, DIRECTION_NORTH));

            if (!locations.isEmpty()) {

                //pick a random location
                Location location = locations.get(getRandom().nextInt(locations.size()));

                //connect the rooms together
                connectRooms(this, room, getRoom(location));

                //move to the new room
                room = getRoom(location);

            } else {

                boolean generated = true;

                //hunt for the next unvisited cell
                for (int row = 0; row < getRows(); row++) {

                    for (int col = 0; col < getCols(); col++) {

                        //we found a new room, start there
                        if (!getRoom(col, row).getGroup().equals(room.getGroup())) {
                            generated = false;
                            room = getRoom(col, row);
                            break;
                        }
                    }

                    if (!generated)
                        break;
                }

                //has the level been successfully generated
                setGenerated(generated);
            }
        }
    }
}