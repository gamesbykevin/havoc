package com.gamesbykevin.havoc.obstacles;

import com.gamesbykevin.havoc.entities.Entities;
import com.gamesbykevin.havoc.entities.Entity;
import com.gamesbykevin.havoc.level.Level;
import com.gamesbykevin.havoc.maze.Location;
import com.gamesbykevin.havoc.maze.Maze;

import java.util.ArrayList;
import java.util.List;

import static com.gamesbykevin.havoc.entities.Entity.PLAYER_COLLISION;
import static com.gamesbykevin.havoc.level.RoomHelper.ROOM_SIZE;

public final class Obstacles extends Entities {

    public enum Type {
        BluePot, BluePotPlant, BronzeCol, Chandalier, DogFood, FloorLamp, GreenC, RedC,
        SilverCol, SpecimenPod1, SpecimenPod2, SpecimenPod3, Well, WellBlood, WellWater
    }

    //total number of obstacles
    private static final int COUNT = Type.values().length;

    public Obstacles(Level level) {
        super(level);
    }

    @Override
    public void spawn() {

        //create list of options to place the enemy(ies)
        List<Location> options = new ArrayList<>();

        for (int col = 0; col < getLevel().getMaze().getCols(); col++) {
            for (int row = 0; row < getLevel().getMaze().getRows(); row++) {

                //skip the starting room
                if (col == getLevel().getMaze().getStartCol() && row == getLevel().getMaze().getStartRow())
                    continue;

                //where we are starting for the current location
                int sCol = (col * ROOM_SIZE);
                int sRow = (row * ROOM_SIZE);

                for (int startCol = sCol; startCol < sCol + ROOM_SIZE; startCol++) {
                    for (int startRow = sRow; startRow < sRow + ROOM_SIZE; startRow++) {

                        if (getLevel().hasFree(startCol, startRow))
                            options.add(new Location(startCol, startRow));
                    }
                }

                //pick random index
                int index = Maze.getRandom().nextInt(options.size());

                Location location = options.get(index);

                Obstacle.TYPE = Type.SpecimenPod1;
                getEntityList().add(new Obstacle(location.col, location.row));

                options.remove(index);

                //clear the list
                options.clear();
            }
        }

        options.clear();
        options = null;
    }

    @Override
    public boolean hasCollision(float x, float y) {

        for (int i = 0; i < getEntityList().size(); i++) {

            //get the current entity
            Entity entity = getEntityList().get(i);

            if (getDistance(entity, x, y) < PLAYER_COLLISION)
                return true;
        }

        return false;
    }
}