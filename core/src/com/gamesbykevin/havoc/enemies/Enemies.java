package com.gamesbykevin.havoc.enemies;

import com.gamesbykevin.havoc.entities.Entities;
import com.gamesbykevin.havoc.entities.Entity;
import com.gamesbykevin.havoc.level.Level;
import com.gamesbykevin.havoc.maze.Location;
import com.gamesbykevin.havoc.maze.Maze;

import java.util.ArrayList;
import java.util.List;

import static com.gamesbykevin.havoc.level.RoomHelper.ROOM_SIZE;

public final class Enemies extends Entities {

    //how many enemies in each room
    public static final int ENEMIES_PER_ROOM = 3;

    public Enemies(Level level) {
        super(level);
    }

    private void add(Location location) {
        Enemy enemy = new Enemy();
        enemy.setCol(location.col);
        enemy.setRow(location.row);
        getEntityList().add(enemy);
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
                int sCol = (col * ROOM_SIZE);
                int sRow = (row * ROOM_SIZE);

                for (int startCol = sCol; startCol < sCol + ROOM_SIZE; startCol++) {
                    for (int startRow = sRow; startRow < sRow + ROOM_SIZE; startRow++) {

                        if (getLevel().hasFree(startCol, startRow))
                            options.add(new Location(startCol, startRow));
                    }
                }

                int count = 0;

                //how many enemies per room
                while (!options.isEmpty() && count < ENEMIES_PER_ROOM) {

                    //pick random index
                    int index = Maze.getRandom().nextInt(options.size());

                    //add enemy to the location
                    add(options.get(index));

                    //remove the option from the list
                    options.remove(index);

                    //increase the count
                    count++;
                }

                //clear the list
                options.clear();
            }
        }

        options.clear();
        options = null;
    }
}