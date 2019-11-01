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

    //different type of collectibles
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
        key,
    }

    //how many of these types can we spawn?
    public static final int MAX_HEALTH_SMALL = 10;
    public static final int MAX_HEALTH_LARGE = 3;
    public static final int MAX_AMMO = 10;
    public static final int MAX_AMMO_CRATE = 5;

    //how many collectibles per room
    public static final int COLLECTIBLES_PER_ROOM_MAX = 2;

    public Collectibles(Level level) {
        super(level);
    }

    public void addKey(float col, float row) {

        Collectible.TYPE = Type.key;
        Collectible collectible = new Collectible(TYPE);
        collectible.setSolid(true);
        add(collectible, col, row);
    }

    @Override
    public void spawn() {

        //create list of options to place the entity(ies)
        List<Location> options = new ArrayList<>();

        //create a new list of collectibles
        List<Type> collectibles = new ArrayList<>();
        collectibles.add(Type.ammo);
        collectibles.add(Type.ammo_crate);
        collectibles.add(Type.health_small);
        collectibles.add(Type.health_large);
        //collectibles.add(Type.glock);
        collectibles.add(Type.smg);
        collectibles.add(Type.impact);
        collectibles.add(Type.magnum);
        collectibles.add(Type.buzzsaw);
        collectibles.add(Type.shotgun);

        int countAmmo = 0;
        int countAmmCrate = 0;
        int countHealthSmall = 0;
        int countHealthLarge = 0;

        List<Location> rooms = new ArrayList<>();

        for (int col = 0; col < getLevel().getMaze().getCols(); col++) {
            for (int row = 0; row < getLevel().getMaze().getRows(); row++) {

                //skip the start/goal of the level
                if (col == getLevel().getMaze().getStartCol() && row == getLevel().getMaze().getStartRow())
                    continue;
                if (col == getLevel().getMaze().getGoalCol() && row == getLevel().getMaze().getGoalRow())
                    continue;

                //add room as option for adding collectibles
                rooms.add(new Location(col, row));
            }
        }

        while (!rooms.isEmpty()) {

            int randomIndex = Maze.getRandom().nextInt(rooms.size());

            //pick a random location
            Location tmp = rooms.get(randomIndex);
            int col = tmp.col;
            int row = tmp.row;

            //remove the location
            rooms.remove(randomIndex);

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

                if (collectibles.isEmpty())
                    break;

                //pick random index
                int index = Maze.getRandom().nextInt(options.size());

                //get the location
                Location location = options.get(index);

                //check if there are any other items
                if (!hasEntityLocation(location)) {

                    //pick a random collectible
                    int collectibleIndex = Maze.getRandom().nextInt(collectibles.size());

                    //get the collectible
                    TYPE = collectibles.get(collectibleIndex);

                    //some collectibles should only exist once
                    switch (collectibles.get(collectibleIndex)) {

                        //these can only be added once
                        case glock:
                        case impact:
                        case buzzsaw:
                        case magnum:
                        case smg:
                        case shotgun:
                            collectibles.remove(collectibleIndex);
                            break;

                        case ammo:
                            countAmmo++;

                            if (countAmmo >= MAX_AMMO)
                                collectibles.remove(collectibleIndex);
                            break;

                        case ammo_crate:
                            countAmmCrate++;

                            if (countAmmCrate >= MAX_AMMO_CRATE)
                                collectibles.remove(collectibleIndex);
                            break;

                        case health_small:
                            countHealthSmall++;

                            if (countHealthSmall >= MAX_HEALTH_SMALL)
                                collectibles.remove(collectibleIndex);
                            break;

                        case health_large:
                            countHealthLarge++;

                            if (countHealthLarge >= MAX_HEALTH_LARGE)
                                collectibles.remove(collectibleIndex);
                            break;
                    }

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
    public int render(DecalBatch decalBatch, PerspectiveCamera camera3d, float minCol, float maxCol, float minRow, float maxRow) {

        int count = 0;

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
            if (getDistance(entity, camera3d.position) >= RENDER_RANGE)
                continue;

            //render like a billboard
            entity.getAnimation().getDecal().lookAt(camera3d.position, camera3d.up);

            //add to the batch to be rendered
            entity.render(decalBatch);

            count++;
        }

        //return total number of items rendered
        return count;
    }
}