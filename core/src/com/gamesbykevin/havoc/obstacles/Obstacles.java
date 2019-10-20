package com.gamesbykevin.havoc.obstacles;

import com.gamesbykevin.havoc.entities.Entities;
import com.gamesbykevin.havoc.entities.Entity;
import com.gamesbykevin.havoc.level.Level;
import com.gamesbykevin.havoc.maze.Location;
import com.gamesbykevin.havoc.maze.Maze;

import java.util.ArrayList;
import java.util.List;

import static com.gamesbykevin.havoc.entities.Entity.PLAYER_COLLISION;
import static com.gamesbykevin.havoc.level.LevelHelper.getLocationOptions;
import static com.gamesbykevin.havoc.level.RoomHelper.ROOM_SIZE;

public final class Obstacles extends Entities {

    public enum Type {
        BluePot, BluePotPlant, BronzeCol, Chandelier, DogFood, FloorLamp, GreenC, RedC,
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
                int startCol = (col * ROOM_SIZE);
                int startRow = (row * ROOM_SIZE);

                //get our available options
                options = getLocationOptions(getLevel(), startCol, startRow, options);

                //pick random index
                int index = Maze.getRandom().nextInt(options.size());

                //get the location
                Location location = options.get(index);

                //check if there are any other items
                if (!hasEntityLocation(location)) {

                    //add entity
                    Obstacle.TYPE = Type.SpecimenPod1;
                    add(new Obstacle(), location);
                }

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