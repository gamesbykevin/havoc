package com.gamesbykevin.havoc.maze;

import java.util.ArrayList;
import java.util.List;

import static com.gamesbykevin.havoc.maze.Maze.*;

public class MazeHelper {

    public static void connectRooms(Maze maze, Room room1, Room room2) {

        if (room1.getCol() > room2.getCol()) {
            room1.setWest(false);
            room2.setEast(false);
        } else if (room1.getCol() < room2.getCol()) {
            room1.setEast(false);
            room2.setWest(false);
        } else if (room1.getRow() > room2.getRow()) {
            room1.setSouth(false);
            room2.setNorth(false);
        } else if (room1.getRow() < room2.getRow()) {
            room1.setNorth(false);
            room2.setSouth(false);
        }

        //make both rooms part of the same group
        updateGroup(maze, room1, room2);
    }

    public static int connectRoom(Maze maze, Room room) {

        //get the list of directions we can go
        List<Integer> directions = getDirections(maze, room);

        int index = -1;

        if (!directions.isEmpty()) {

            //pick random direction
            index = maze.getRandom().nextInt(directions.size());

            //get the room in the random direction
            Room tmp = maze.getRoom(room, directions.get(index));

            //connect the rooms together
            connectRooms(maze, tmp, room);

        } else {

            //if no directions available
            return DIRECTION_NONE;
        }

        //return result
        return directions.get(index);
    }

    public static void connectRemaining(Maze maze) {

        boolean completed = false;

        //connect the different groups if they remain
        while (!completed) {

            completed = true;

            for (int row = 0; row < maze.getRows(); row++) {
                for (int col = 0; col < maze.getCols(); col++) {

                    Room room = maze.getRoom(col, row);

                    //let's see if we connected a room
                    int direction = connectRoom(maze, room);

                    //if we connected a room, we aren't done just yet
                    if (direction != DIRECTION_NONE)
                        completed = false;
                }
            }
        }
    }

    public static List<Integer> getDirections(Maze maze, Room room) {

        List<Integer> directions = new ArrayList<>();

        if (maze.hasBounds(room, DIRECTION_WEST) && !maze.getRoom(room, DIRECTION_WEST).getGroup().equals(room.getGroup()))
            directions.add(DIRECTION_WEST);
        if (maze.hasBounds(room, DIRECTION_EAST) && !maze.getRoom(room, DIRECTION_EAST).getGroup().equals(room.getGroup()))
            directions.add(DIRECTION_EAST);
        if (maze.hasBounds(room, DIRECTION_SOUTH) && !maze.getRoom(room, DIRECTION_SOUTH).getGroup().equals(room.getGroup()))
            directions.add(DIRECTION_SOUTH);
        if (maze.hasBounds(room, DIRECTION_NORTH) && !maze.getRoom(room, DIRECTION_NORTH).getGroup().equals(room.getGroup()))
            directions.add(DIRECTION_NORTH);

        return directions;
    }

    private static void updateGroup(Maze maze, Room room1, Room room2) {
        updateGroup(maze, room1.getGroup(), room2.getGroup());
    }

    private static void updateGroup(Maze maze, String group1, String group2) {

        for (int row = 0; row < maze.getRows(); row++) {
            for (int col = 0; col < maze.getCols(); col++) {

                Room room = maze.getRoom(col, row);

                if (room.getGroup() == null)
                    continue;

                //if the room equals the old, update to the new
                if (room.getGroup().equals(group1))
                    room.setGroup(group2);
            }
        }
    }

    public static void locateGoal(Maze maze) {

        Room room = null;

        for (int row = 0; row < maze.getRows(); row++) {
            for (int col = 0; col < maze.getCols(); col++) {

                Room tmp = maze.getRoom(col, row);

                //look for the best cost
                if (room == null || tmp.getCost() > room.getCost())
                    room = tmp;
            }
        }

        //highest cost is the goal
        maze.setGoalCol(room.getCol());
        maze.setGoalRow(room.getRow());
    }

    public static void calculateCost(Maze maze) {

        //flag all rooms as not visited
        for (int row = 0; row < maze.getRows(); row++) {
            for (int col = 0; col < maze.getCols(); col++) {
                maze.getRoom(col, row).setVisited(false);
            }
        }

        //begin at the start location
        Room room = maze.getRoom(maze.getStartCol(), maze.getStartRow());

        //the cost of the start will be 0
        room.setCost(0);

        //flag the room as visited
        room.setVisited(true);

        List<Room> rooms = new ArrayList<>();
        rooms.add(room);

        while (!rooms.isEmpty()) {

            //get the first room
            room = rooms.get(0);

            //if we don't have a wall and have not visited the room next door
            if (!room.hasEast() && !maze.getRoom(room, DIRECTION_EAST).isVisited())
                updateCost(maze, rooms, room, DIRECTION_EAST);
            if (!room.hasWest() && !maze.getRoom(room, DIRECTION_WEST).isVisited())
                updateCost(maze, rooms, room, DIRECTION_WEST);
            if (!room.hasNorth() && !maze.getRoom(room, DIRECTION_NORTH).isVisited())
                updateCost(maze, rooms, room, DIRECTION_NORTH);
            if (!room.hasSouth() && !maze.getRoom(room, DIRECTION_SOUTH).isVisited())
                updateCost(maze, rooms, room, DIRECTION_SOUTH);

            //we checked all directions remove from list
            rooms.remove(0);
        }
    }

    private static void updateCost(Maze maze, List<Room> rooms, Room room, int direction) {

        //get the neighbor room
        Room tmp = maze.getRoom(room, direction);

        //flag as visited
        tmp.setVisited(true);

        //increase the cost by 1
        tmp.setCost(room.getCost() + 1);

        //add the new room to the list
        rooms.add(tmp);
    }
}