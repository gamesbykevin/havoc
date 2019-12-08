package com.gamesbykevin.havoc.obstacles;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.gamesbykevin.havoc.animation.DecalAnimation;
import com.gamesbykevin.havoc.dungeon.Leaf;
import com.gamesbykevin.havoc.dungeon.Room;
import com.gamesbykevin.havoc.entities.Entities;
import com.gamesbykevin.havoc.entities.Entity;
import com.gamesbykevin.havoc.level.Level;

import java.util.HashMap;

import static com.gamesbykevin.havoc.assets.AssetManagerHelper.ASSET_DIR_OBSTACLES;
import static com.gamesbykevin.havoc.assets.AssetManagerHelper.ASSET_EXT_BMP;
import static com.gamesbykevin.havoc.dungeon.Dungeon.getRandom;
import static com.gamesbykevin.havoc.obstacles.ObstacleHelper.*;
import static com.gamesbykevin.havoc.util.Distance.getDistance;

public final class Obstacles extends Entities {

    public enum Type {
        Light1, Light2, Light3, Light4, Light5, Light6, Light7,
        Grass1, Grass2, Grass3, Grass4, Grass5, Grass6, Grass7, Grass8, Grass9, Grass10, Grass11,
        pillar1, pillar2, pillar3, pillar4, pillar5,
        random1, random2, random3, random4,
        cage1, cage2, cage3, cage4,
        statue1, statue2, statue3, statue4, statue5,
        BluePotLargeEmpty, BluePotPlant1, BluePotPlant2, YellowPotPlant,
        Candle,
        spear1, spear2,
        flag1, flag2,
        barrel1, barrel2,
        pots1, pots2,
        table,
        FloorLamp1, FloorLamp2
    }

    //how close can we get to the obstacle
    private static final float OBSTACLE_COLLISION = .33f;

    //list of textures so we don't have to load the same texture repeatedly
    private static HashMap<Type, TextureRegion> TEXTURES;

    public Obstacles(Level level) {
        super(level);
    }

    @Override
    public void spawn() {

        for (int i = 0; i < getLevel().getDungeon().getLeafs().size(); i++) {

            Leaf leaf = getLevel().getDungeon().getLeafs().get(i);

            //only want leafs containing rooms
            if (leaf.getRoom() == null)
                continue;

            //add lights
            addLights(leaf.getRoom());

            //assign random type next to the walls
            if (getRandom().nextBoolean())
                addNextToWalls(leaf.getRoom());
        }

        //clean up lists
        recycle();
    }

    public static HashMap<Type, TextureRegion> getTextures(AssetManager assetManager) {

        if (TEXTURES == null) {
            TEXTURES = new HashMap<>();
            for (Type type : Type.values()) {
                TEXTURES.put(type, new TextureRegion(assetManager.get(ASSET_DIR_OBSTACLES + type.toString() + ASSET_EXT_BMP, Texture.class)));
            }
        }

        //return our list of textures
        return TEXTURES;
    }

    private void addNextToWalls(Room room) {

        //assign random obstacle type
        Obstacles.Type type = assignRandomType();

        //how frequent do we add an obstacle
        int offset = getRandom().nextInt(4) + 1;

        boolean doorWest = room.hasWestDoor(getLevel().getDungeon());
        boolean doorEast = room.hasEastDoor(getLevel().getDungeon());
        boolean doorNorth = room.hasNorthDoor(getLevel().getDungeon());
        boolean doorSouth = room.hasSouthDoor(getLevel().getDungeon());

        for (int row = room.getY(); row < room.getY() + room.getH(); row++) {
            if (row % offset != 0)
                continue;
            if (!doorWest)
                add(type, room.getX() + 1, row);
            if (!doorEast)
                add(type, room.getX() + room.getW() - 2, row);
        }

        for (int col = room.getX(); col < room.getX() + room.getW(); col++) {
            if (col % offset != 0)
                continue;
            if (!doorNorth)
                add(type, col, room.getY() + room.getH() - 2);
            if (!doorSouth)
                add(type, col, room.getY() + 1);
        }
    }

    private void addLights(Room room) {

        //frequency of lights
        int frequency = getRandom().nextInt(4) + 2;

        Obstacles.Type type = getRandomLight();

        int middleCol = room.getX() + (room.getW() / 2);
        int middleRow = room.getY() + (room.getH() / 2);

        switch (getRandom().nextInt(5)) {

            //lights in 1 line
            case 0:
                for (int col = room.getX(); col < room.getX() + room.getW(); col++) {

                    if (col % frequency == 0)
                        continue;

                    add(type, col, middleRow);
                }
                break;

                //lights in 1 line
            case 1:
                for (int row = room.getY(); row < room.getY() + room.getH(); row++) {

                    if (row % frequency == 0)
                        continue;

                    add(type, middleCol, row);
                }
                break;

                //scattered lights throughout the room
            case 2:
                for (int col = room.getX(); col < room.getX() + room.getW(); col++) {
                    for (int row = room.getY(); row < room.getY() + room.getH(); row++) {

                        if (col % frequency != 0 || row % frequency != 0)
                            continue;

                        add(type, col, row);
                    }
                }
                break;

                //lights across the middle col and middle row
            case 3:
                for (int col = room.getX(); col < room.getX() + room.getW(); col++) {

                    if (col % frequency == 0)
                        continue;

                    add(type, col, middleRow);
                }

                for (int row = room.getY(); row < room.getY() + room.getH(); row++) {

                    if (row % frequency == 0)
                        continue;

                    add(type, middleCol, row);
                }
                break;

                //add lights in corners
            case 4:
                int offset = 4;
                add(type, room.getX() + offset, room.getY() + offset);
                add(type, room.getX() + (room.getW() - 1) - offset, room.getY() + (room.getH() - 1) - offset);
                add(type, room.getX() + (room.getW() - 1) - offset, room.getY() + offset);
                add(type, room.getX() + offset, room.getY() + (room.getH() - 1) - offset);
                break;
        }
    }

    private void add(Obstacles.Type type, int col, int row) {

        //we won't add if already occupied
        if (hasEntityLocation(col, row))
            return;
        if (nearInteract(col, row))
            return;

        //create our obstacle
        Obstacle obstacle = new Obstacle(type);

        //animation obstacles are a single frame
        obstacle.getAnimations()[0] = new DecalAnimation(Obstacles.getTextures(getLevel().getAssetManager()).get(type));

        //then add to entity list
        add(obstacle, col, row);
    }

    private boolean nearInteract(int col, int row) {

        //can't place it next to a door
        for (int x = -2; x <= 2; x++) {
            for (int y = -2; y <= 2; y++) {

                if (x != 0 && y != 0)
                    continue;

                if (getLevel().getDungeon().hasInteract(col + x, row + y))
                    return true;
            }
        }

        return false;
    }

    @Override
    public boolean hasCollision(float x, float y) {

        for (int i = 0; i < getEntityList().size(); i++) {

            //get the current entity
            Entity entity = getEntityList().get(i);

            if (!entity.isSolid())
                continue;

            if (getDistance(entity, x, y) < OBSTACLE_COLLISION)
                return true;
        }

        return false;
    }

    @Override
    public void update() {
        //do we need to update any of the obstacles?
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