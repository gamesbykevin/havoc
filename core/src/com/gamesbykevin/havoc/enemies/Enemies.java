package com.gamesbykevin.havoc.enemies;

import com.gamesbykevin.havoc.entities.Entities;
import com.gamesbykevin.havoc.entities.Entity;
import com.gamesbykevin.havoc.level.Level;
import com.gamesbykevin.havoc.maze.Location;
import com.gamesbykevin.havoc.maze.Maze;

import java.util.ArrayList;
import java.util.List;

import static com.gamesbykevin.havoc.level.LevelHelper.getLocationOptions;
import static com.gamesbykevin.havoc.level.RoomHelper.ROOM_SIZE;

public final class Enemies extends Entities {

    //how many enemies in each room
    public static final int ENEMIES_PER_ROOM_MAX = 4;

    public Enemies(Level level) {
        super(level);
    }

    public boolean hasCollision(float x, float y) {

        for (int i = 0; i < getEntityList().size(); i++) {

            Entity entity = getEntityList().get(i);

            //skip if dead
            if (!entity.isSolid())
                continue;

            //if we have collision return true
            if (entity.hasCollision(x, y))
                return true;
        }

        //no collision
        return false;
    }

    //add enemies
    @Override
    public void spawn() {

        //create list of options to place the enemy(ies)
        List<Location> options = new ArrayList<>();

        for (int col = 0; col < getLevel().getMaze().getCols(); col++) {
            for (int row = 0; row < getLevel().getMaze().getRows(); row++) {

                //skip the start of the level
                if (col == getLevel().getMaze().getStartCol() && row == getLevel().getMaze().getStartRow())
                    continue;

                //where we are starting for the current location
                int startCol = (col * ROOM_SIZE);
                int startRow = (row * ROOM_SIZE);

                //get our available options
                options = getLocationOptions(getLevel(), startCol, startRow, options);

                int count = 0;

                //pick random number of enemies
                int limit = Maze.getRandom().nextInt(ENEMIES_PER_ROOM_MAX) + 1;

                //how many enemies per room
                while (!options.isEmpty() && count < limit) {

                    //pick random index
                    int index = Maze.getRandom().nextInt(options.size());

                    //get the location
                    Location location = options.get(index);

                    //check if there are any other items
                    if (!hasEntityLocation(location)) {

                        //add enemy at the location
                        add(new Enemy(), location);

                        //increase the count
                        count++;
                    }

                    //remove the option from the list
                    options.remove(index);
                }

                //clear the list
                options.clear();
            }
        }

        options.clear();
        options = null;
    }
}