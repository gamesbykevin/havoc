package com.gamesbykevin.havoc.collectibles;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.gamesbykevin.havoc.animation.DecalAnimation;
import com.gamesbykevin.havoc.dungeon.Cell;
import com.gamesbykevin.havoc.dungeon.Leaf;
import com.gamesbykevin.havoc.dungeon.Room;
import com.gamesbykevin.havoc.entities.Entities;
import com.gamesbykevin.havoc.entities.Entity;
import com.gamesbykevin.havoc.entities.Entity3d;
import com.gamesbykevin.havoc.level.Level;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.gamesbykevin.havoc.assets.AssetManagerHelper.ASSET_DIR_COLLECTIBLES;
import static com.gamesbykevin.havoc.assets.AssetManagerHelper.ASSET_EXT_BMP;
import static com.gamesbykevin.havoc.dungeon.Dungeon.getRandom;
import static com.gamesbykevin.havoc.dungeon.LeafHelper.getLeafRooms;

public final class Collectibles extends Entities {

    //different type of collectibles
    public enum Type {
        ammo, ammo_crate, buzzsaw, glock, health_large, health_small, impact, magnum, shotgun, smg, key,
    }

    //the total number of allowed will depend on the number of rooms
    public static final float MAX_RATIO_HEALTH_SMALL    = .2f;
    public static final float MAX_RATIO_HEALTH_LARGE    = .1f;
    public static final float MAX_RATIO_AMMO            = .4f;
    public static final float MAX_RATIO_AMMO_CRATE      = .25f;

    //how many collectibles per room
    public static final int COLLECTIBLES_PER_ROOM_MAX = 2;

    //spawn ammo here for enemies
    private static final int SPAWN_AMMO_COL = -1;
    private static final int SPAWN_AMMO_ROW = -1;

    //list of textures to be re-used
    private static HashMap<Type, TextureRegion> TEXTURES;

    public Collectibles(Level level) {
        super(level);

        //load the collectible textures
        getTextures(level.getAssetManager());
    }

    public static HashMap<Type, TextureRegion> getTextures(AssetManager assetManager) {

        if (TEXTURES == null) {
            TEXTURES = new HashMap<>();
            for (Type type : Type.values()) {
                TEXTURES.put(type, new TextureRegion(assetManager.get(ASSET_DIR_COLLECTIBLES + type.toString() + ASSET_EXT_BMP, Texture.class)));
            }
        }

        //return our instance
        return TEXTURES;
    }

    @Override
    public void spawn() {

        //spawn ammo for every enemy created, so we can use that ammo when they die
        for (int i = 0; i < getLevel().getEnemies().getEntityList().size(); i++) {
            spawnAmmo();
        }

        //create a new list of collectibles
        List<Type> collectibles = new ArrayList<>();
        collectibles.add(Type.ammo);
        collectibles.add(Type.ammo_crate);
        collectibles.add(Type.health_small);
        collectibles.add(Type.health_large);

        //list of weapons the player can collect
        List<Type> weapons = new ArrayList<>();
        weapons.add(Type.smg);
        weapons.add(Type.impact);
        weapons.add(Type.magnum);
        weapons.add(Type.buzzsaw);
        weapons.add(Type.shotgun);

        //keep track how much has been added
        int countAmmo = 0;
        int countAmmoCrate = 0;
        int countHealthSmall = 0;
        int countHealthLarge = 0;

        //list of valid leaves containing rooms for spawning
        List<Leaf> leaves = getLeafRooms(getLevel().getDungeon());

        //the max number allowed will depend on the number of rooms
        int maxAmmo = (int)(MAX_RATIO_AMMO * leaves.size());
        int maxAmmoCrate = (int)(MAX_RATIO_AMMO_CRATE * leaves.size());
        int maxHealthSmall = (int)(MAX_RATIO_HEALTH_SMALL * leaves.size());
        int maxHealthLarge = (int)(MAX_RATIO_HEALTH_LARGE * leaves.size());

        //list of options where the collectibles can be placed
        List<Cell> options = null;

        //continue until we check every room
        while (!leaves.isEmpty()) {

            //if there is nothing left to add
            if (collectibles.isEmpty() && weapons.isEmpty())
                break;

            //choose random leaf
            final int randomIndex = getRandom().nextInt(leaves.size());

            //get random leaf
            Leaf leaf = leaves.get(randomIndex);

            //remove it from the list
            leaves.remove(randomIndex);

            //get a list of options to place the collectibles
            options = getLocationOptions(leaf.getRoom());

            //skip if nothing available
            if (options.isEmpty())
                continue;

            int count = 0;

            //keep track of what we spawn in the room
            boolean roomAmmo = false;
            boolean roomAmmoCrate = false;
            boolean roomHealthSmall = false;
            boolean roomHealthLarge = false;

            //pick random number limit of collectibles allowed for the current room
            final int limit = getRandom().nextInt(COLLECTIBLES_PER_ROOM_MAX) + 1;

            //continue until we reach limit or there are no more options
            while (!options.isEmpty() && count < limit) {

                //if there is nothing left to add
                if (collectibles.isEmpty() && weapons.isEmpty())
                    break;

                //pick random index
                final int index = getRandom().nextInt(options.size());

                //get the location
                Cell cell = options.get(index);

                //type of collectible
                Type type = null;

                //secret rooms have weapons placed in them
                if (leaf.getRoom().isSecret() && !weapons.isEmpty()) {

                    //pick a random index
                    int tmpIndex = getRandom().nextInt(weapons.size());

                    //get the type
                    type = weapons.get(tmpIndex);

                    //remove from the list
                    weapons.remove(tmpIndex);

                    //make this the limit so we place only 1 weapon per secret room
                    count = limit;

                } else if (!collectibles.isEmpty()) {

                    //pick a random index
                    int tmpIndex = getRandom().nextInt(collectibles.size());

                    //get the type
                    type = collectibles.get(tmpIndex);

                    switch (type) {

                        case ammo:
                            if (!roomAmmo && !roomAmmoCrate) {
                                roomAmmo = true;
                                countAmmo++;

                                if (countAmmo >= maxAmmo)
                                    collectibles.remove(tmpIndex);
                            }
                            break;

                        case ammo_crate:
                            if (!roomAmmo && !roomAmmoCrate) {
                                roomAmmoCrate = true;
                                countAmmoCrate++;

                                if (countAmmoCrate >= maxAmmoCrate)
                                    collectibles.remove(tmpIndex);
                            }
                            break;

                        case health_small:
                            if (!roomHealthSmall && !roomHealthLarge) {
                                roomHealthSmall = true;
                                countHealthSmall++;

                                if (countHealthSmall >= maxHealthSmall)
                                    collectibles.remove(tmpIndex);
                            }
                            break;

                        case health_large:
                            if (!roomHealthSmall && !roomHealthLarge) {
                                roomHealthLarge = true;
                                countHealthLarge++;

                                if (countHealthLarge >= maxHealthLarge)
                                    collectibles.remove(tmpIndex);
                            }
                            break;
                    }
                }

                //if we have a type we will create the collectible
                if (type != null)
                    add(type, cell);

                //increase the count
                count++;

                //remove the option from the list
                options.remove(index);
            }

            //clear the list
            options.clear();
        }

        if (options != null)
            options.clear();

        if (collectibles != null)
            collectibles.clear();

        if (weapons != null)
            weapons.clear();

        options = null;
        collectibles = null;
        weapons = null;
    }

    public void spawnAmmo() {
        //render off screen for now
        add(Type.ammo, SPAWN_AMMO_COL, SPAWN_AMMO_ROW);
    }

    public void displayAmmo(Entity entity) {

        //look for a collectible to display in the game
        for (int i = 0; i < getEntityList().size(); i++) {

            if (getEntityList().get(i).getCol() != SPAWN_AMMO_COL && getEntityList().get(i).getRow() != SPAWN_AMMO_ROW)
                continue;

            Entity3d tmp = (Entity3d)getEntityList().get(i);

            //search for a nearby spot to place the ammo
            for (float x = 0; x <= OFFSET * 3; x += OFFSET) {
                for (float y = 0; y <= OFFSET * 3; y += OFFSET) {

                    //avoid this location
                    if (x == OFFSET || y == OFFSET)
                        continue;

                    if (hasEntityLocation((int)(entity.getCol() + x), (int)(entity.getRow() + y)) || hasEntityLocation(entity.getCol() + x, entity.getRow() + y))
                        continue;

                    tmp.setCol(entity.getCol() + x);
                    tmp.setRow(entity.getRow() + y);
                    tmp.getAnimation().reset();
                    tmp.getAnimation().setPosition(entity.getCol() + x, entity.getRow() + y, 0);
                    return;
                }
            }
        }
    }

    public void add(Type type, Cell cell) {
        this.add(type, cell.getCol(), cell.getRow());
    }

    public void add(Type type, float col, float row) {

        //create the collectible and make it solid so we can collect it
        Collectible collectible = new Collectible(type);

        //setup the single animation
        collectible.getAnimations()[0] = new DecalAnimation(Collectibles.getTextures(getLevel().getAssetManager()).get(type));

        //reset the animation
        collectible.getAnimation().reset();

        //add at the location
        super.add(collectible, col, row);
    }

    private List<Cell> getLocationOptions(Room room) {

        List<Cell> options = new ArrayList<>();

        int colStart = room.getX() + 3;
        int rowStart = room.getY() + 3;

        int colMiddle = room.getX() + (room.getW() / 2);
        int rowMiddle = room.getY() + (room.getH() / 2);

        int colEnd = room.getX() + room.getW() - 3;
        int rowEnd = room.getY() + room.getH() - 3;

        addTmp(getLevel(), options, colStart, rowStart);
        addTmp(getLevel(), options, colStart, rowMiddle);
        addTmp(getLevel(), options, colStart, rowEnd);

        addTmp(getLevel(), options, colMiddle, rowStart);
        addTmp(getLevel(), options, colMiddle, rowMiddle);
        addTmp(getLevel(), options, colMiddle, rowEnd);

        addTmp(getLevel(), options, colEnd, rowStart);
        addTmp(getLevel(), options, colEnd, rowMiddle);
        addTmp(getLevel(), options, colEnd, rowEnd);

        //return our list of options
        return options;
    }

    private void addTmp(Level level, List<Cell> options, int col, int row) {

        //only add if available
        if (isAvailable(col, row))
            options.add(level.getDungeon().getCell(col, row));
    }

    private boolean isAvailable(int col, int row) {

        //we need it to be an open space and no door / switch
        if (!getLevel().getDungeon().hasMap(col, row) || getLevel().getDungeon().hasInteract(col, row))
            return false;

        return true;
    }

    @Override
    public void update() {
        //update anything here
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

    @Override
    public void dispose() {
        super.dispose();

        if (TEXTURES != null) {
            for (Type type : Type.values()) {

                if (TEXTURES.get(type) != null) {
                    TEXTURES.get(type).getTexture().dispose();
                    TEXTURES.put(type, null);
                }
            }
            TEXTURES.clear();
        }

        TEXTURES = null;
    }
}