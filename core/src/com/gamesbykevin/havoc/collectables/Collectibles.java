package com.gamesbykevin.havoc.collectables;

import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.decals.DecalBatch;
import com.badlogic.gdx.math.Vector3;
import com.gamesbykevin.havoc.dungeon.Cell;
import com.gamesbykevin.havoc.dungeon.Leaf;
import com.gamesbykevin.havoc.entities.Entities;
import com.gamesbykevin.havoc.entities.Entity;
import com.gamesbykevin.havoc.level.Level;

import java.util.ArrayList;
import java.util.List;

import static com.gamesbykevin.havoc.collectables.Collectible.TYPE;
import static com.gamesbykevin.havoc.dungeon.Dungeon.getRandom;
import static com.gamesbykevin.havoc.dungeon.LeafHelper.getLeafRooms;
import static com.gamesbykevin.havoc.level.Level.RENDER_RANGE;

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

        //create a new list of collectibles
        List<Type> collectibles = new ArrayList<>();
        collectibles.add(Type.ammo);
        collectibles.add(Type.ammo_crate);
        collectibles.add(Type.health_small);
        collectibles.add(Type.health_large);

        //player spawns with this by default
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
            int limit = getRandom().nextInt(COLLECTIBLES_PER_ROOM_MAX) + 1;

            //continue until we reach limit or no move options
            while (!options.isEmpty() && count < limit) {

                if (collectibles.isEmpty())
                    break;

                //pick random index
                int index = getRandom().nextInt(options.size());

                //get the location
                Cell cell = options.get(index);

                //check if there are any other items
                if (!hasEntityLocation(cell.getCol(), cell.getRow())) {

                    //pick a random collectible
                    int collectibleIndex = getRandom().nextInt(collectibles.size());

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
                    add(collectible, cell.getCol(), cell.getRow());

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

            //keep track of total rendered
            count++;
        }

        //return total number of items rendered
        return count;
    }
}