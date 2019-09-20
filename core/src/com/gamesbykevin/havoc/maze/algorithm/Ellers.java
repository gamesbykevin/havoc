package com.gamesbykevin.havoc.maze.algorithm;

import com.gamesbykevin.havoc.maze.Maze;
import com.gamesbykevin.havoc.maze.MazeHelper;
import com.gamesbykevin.havoc.maze.Room;

import static com.gamesbykevin.havoc.maze.MazeHelper.connectRemaining;
import static com.gamesbykevin.havoc.maze.MazeHelper.connectRooms;

public class Ellers extends Maze {

    public Ellers(int cols, int rows) {
        super(cols, rows);
    }

    @Override
    public void generate() {

        for (int row = 0; row < getRows(); row++) {

            //create horizontal connections
            for (int col = 0; col < getCols() - 1; col++) {

                if (getRandom().nextBoolean()) {

                    //connect the 2 rooms together
                    connectRooms(this, getRoom(col, row), getRoom(col + 1, row));
                }
            }

            //make sure we stay in bounds
            if (row < getRows() - 1) {

                int index = 0;

                for (int col = 0; col < getCols(); col++) {

                    Room room1 = getRoom(col, row);
                    Room room2 = getRoom(col + 1, row);

                    //if the groups are different, we need to make a vertical connection
                    if (room2 == null || !room1.getGroup().equals(room2.getGroup())) {

                        int range = col - index;
                        int offset = 0;

                        //if we have a range, pick a random one
                        if (range > 0)
                            offset = getRandom().nextInt(range);

                        int col1 = index + offset;

                        //connect the neighboring rooms
                        connectRooms(this, getRoom(col1, row), getRoom(col1, row + 1));

                        //move to the next column
                        index = col + 1;
                    }
                }

            } else {

                //in the last row we have to connect any rooms that aren't already connected
                for (int col = 0; col < getCols(); col++) {

                    Room room = getRoom(col, row);

                    //if this room is by itself we will join it
                    if (getGroupCount(room.getGroup()) < 2) {

                        //which room do we merge together
                        if (getRandom().nextBoolean() && col < getCols() - 1) {
                            connectRooms(this, room, getRoom(col + 1, row));
                        } else {
                            connectRooms(this, room, getRoom(col, row - 1));
                        }
                    }
                }
            }
        }

        //connect any remaining loose ends
        connectRemaining(this);

        //flag that the maze has been generated
        super.setGenerated(true);
    }

    private int getGroupCount(String group) {

        int count = 0;

        for (int row = 0; row < getRows(); row++) {
            for (int col = 0; col < getCols(); col++) {

                Room room = getRoom(col, row);

                if (room.getGroup() == null)
                    continue;

                //if the room matches increase the count
                if (room.getGroup().equals(group))
                    count++;
            }
        }

        return count;
    }

    private void updateGroup(String group1, String group2) {

        for (int row = 0; row < getRows(); row++) {
            for (int col = 0; col < getCols(); col++) {

                Room room = getRoom(col, row);

                if (room.getGroup() == null)
                    continue;

                //if the room equals the old, update to the new
                if (room.getGroup().equals(group1))
                    room.setGroup(group2);
            }
        }
    }
}