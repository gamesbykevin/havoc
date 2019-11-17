package com.gamesbykevin.havoc.enemies;

import com.gamesbykevin.havoc.dungeon.Cell;
import com.gamesbykevin.havoc.dungeon.Leaf;
import com.gamesbykevin.havoc.entities.Entities;
import com.gamesbykevin.havoc.entities.Entity;
import com.gamesbykevin.havoc.level.Level;

import java.util.List;

import static com.gamesbykevin.havoc.dungeon.Dungeon.getRandom;
import static com.gamesbykevin.havoc.dungeon.LeafHelper.getLeafRooms;

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

        //list of valid leaves for spawning
        List<Leaf> leaves = getLeafRooms(getLevel().getDungeon());

        while (!leaves.isEmpty()) {

            int randomIndex = getRandom().nextInt(leaves.size());

            //get random leaf
            Leaf leaf = leaves.get(randomIndex);

            //remove from the list
            leaves.remove(randomIndex);

            List<Cell> options = getLocationOptions(leaf.getRoom());

            int count = 0;

            //pick random number of enemies
            int limit = getRandom().nextInt(ENEMIES_PER_ROOM_MAX) + 1;

            //how many enemies per room
            while (!options.isEmpty() && count < limit) {

                //pick random index
                int index = getRandom().nextInt(options.size());

                //get the location
                Cell location = options.get(index);

                //check if there are any other items
                if (!hasEntityLocation(location.getCol(), location.getRow())) {

                    //add enemy at the location
                    add(new Enemy(), location.getCol(), location.getRow());

                    //increase the count
                    count++;
                }

                //remove the option from the list
                options.remove(index);
            }

            //clear the list
            options.clear();
            options = null;
        }
    }
}