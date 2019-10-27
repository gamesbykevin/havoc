package com.gamesbykevin.havoc.collectables;

import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.decals.DecalBatch;
import com.badlogic.gdx.math.Vector3;
import com.gamesbykevin.havoc.entities.Entities;
import com.gamesbykevin.havoc.entities.Entity;
import com.gamesbykevin.havoc.level.Level;
import com.gamesbykevin.havoc.maze.Location;
import com.gamesbykevin.havoc.maze.Maze;

import java.util.ArrayList;
import java.util.List;

import static com.gamesbykevin.havoc.collectables.Collectible.TYPE;
import static com.gamesbykevin.havoc.level.LevelHelper.RENDER_RANGE;
import static com.gamesbykevin.havoc.level.LevelHelper.getLocationOptions;
import static com.gamesbykevin.havoc.level.RoomHelper.ROOM_SIZE;

public final class Collectibles extends Entities {

    public enum Type {
        ammo,
        ammo_crate,
        buzzsaw,
        glock,
        health_large,
        health_small,
        impact,
        magnum,
        shotgun,
        smg,
        key_1,
        key_2
    }

    //how many collectibles per room
    public static final int COLLECTIBLES_PER_ROOM_MAX = 2;

    public Collectibles(Level level) {
        super(level);
    }

    @Override
    public void spawn() {

        //create list of options to place the enemy(ies)
        List<Location> options = new ArrayList<>();

        for (int col = 0; col < getLevel().getMaze().getCols(); col++) {
            for (int row = 0; row < getLevel().getMaze().getRows(); row++) {

                //skip the start/goal of the level
                if (col == getLevel().getMaze().getStartCol() && row == getLevel().getMaze().getStartRow())
                    continue;
                if (col == getLevel().getMaze().getGoalCol() && row == getLevel().getMaze().getGoalRow())
                    continue;

                //where we are starting for the current location
                int startCol = (col * ROOM_SIZE);
                int startRow = (row * ROOM_SIZE);

                //get our available options
                options = getLocationOptions(getLevel(), startCol, startRow, options);

                int count = 0;

                //pick random number of enemies
                int limit = Maze.getRandom().nextInt(COLLECTIBLES_PER_ROOM_MAX) + 1;

                //continue until we reach limit or no move options
                while (!options.isEmpty() && count < limit) {

                    //pick random index
                    int index = Maze.getRandom().nextInt(options.size());

                    //get the location
                    Location location = options.get(index);

                    //check if there are any other items
                    if (!hasEntityLocation(location)) {

                        //pick a random collectible, but skip the keys
                        TYPE = Type.values()[Maze.getRandom().nextInt(Type.values().length - 2)];

                        Collectible collectible = new Collectible(TYPE);
                        collectible.setSolid(true);

                        //add at the location
                        add(collectible, location);

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

    @Override
    public boolean hasCollision(float x, float y) {

        for (int i = 0; i < getEntityList().size(); i++) {

            //get the current entity
            Entity entity = getEntityList().get(i);

            //skip if not solid
            if (!entity.isSolid())
                continue;

            //if we have collision return true
            if (entity.hasCollision(x, y))
                return true;
        }

        //no collision
        return false;
    }

    //logic to render the entities
    @Override
    public void render(DecalBatch decalBatch, PerspectiveCamera camera3d, float minCol, float maxCol, float minRow, float maxRow) {

        for (int i = 0; i < getEntityList().size(); i++) {

            //get the current entity
            Entity entity = getEntityList().get(i);

            //update the entity
            entity.update(camera3d);

            //don't render if not solid
            if (!entity.isSolid())
                continue;

            //get the position of the entity
            Vector3 position = entity.getAnimation().getDecal().getPosition();

            //if too far away there is no reason to render
            if (position.x < minCol || position.x > maxCol)
                continue;
            if (position.y < minRow || position.y > maxRow)
                continue;

            //if entity is not close enough we won't render
            if (getDistance(entity, camera3d.position) >= (RENDER_RANGE / 2))
                continue;

            //render like a billboard
            entity.getAnimation().getDecal().lookAt(camera3d.position, camera3d.up);

            //add to the batch to be rendered
            entity.render(decalBatch);
        }
    }
}