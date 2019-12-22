package com.gamesbykevin.havoc.assets;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.gamesbykevin.havoc.decals.Background;
import com.gamesbykevin.havoc.decals.DecalCustom;
import com.gamesbykevin.havoc.decals.Square;
import com.gamesbykevin.havoc.dungeon.Cell;
import com.gamesbykevin.havoc.dungeon.Dungeon;
import com.gamesbykevin.havoc.dungeon.Room;
import com.gamesbykevin.havoc.level.Level;

import java.util.HashMap;

import static com.gamesbykevin.havoc.assets.AssetManagerHelper.*;
import static com.gamesbykevin.havoc.decals.Background.createDecalBackground;
import static com.gamesbykevin.havoc.decals.Wall.*;
import static com.gamesbykevin.havoc.dungeon.Dungeon.getRandom;

public class TextureHelper {

    private static HashMap<String, TextureRegion> TEXTURE_REGIONS;

    private static TextureRegion TEXTURE_REGION_LOCKED;
    private static TextureRegion TEXTURE_REGION_DOOR;
    private static TextureRegion TEXTURE_REGION_DOOR_SIDE;
    private static TextureRegion TEXTURE_REGION_WALL_GOAL;
    private static TextureRegion TEXTURE_REGION_DOOR_GOAL;
    private static TextureRegion TEXTURE_REGION_SWITCH_OFF;
    private static TextureRegion TEXTURE_REGION_HALLWAY;
    private static TextureRegion TEXTURE_REGION_FLOOR;
    private static TextureRegion TEXTURE_REGION_CEILING;

    //used to progress
    public static float COUNT = 0;
    public static float TOTAL = 1;
    public static int COL;
    public static int ROW;

    //how many updates to make
    public static int PROGRESS_COUNT = 100;

    public static void dispose() {

        if (TEXTURE_REGIONS != null)
            TEXTURE_REGIONS.clear();

        TEXTURE_REGIONS = null;
        TEXTURE_REGION_LOCKED = null;
        TEXTURE_REGION_DOOR = null;
        TEXTURE_REGION_DOOR_SIDE = null;
        TEXTURE_REGION_WALL_GOAL = null;
        TEXTURE_REGION_DOOR_GOAL = null;
        TEXTURE_REGION_SWITCH_OFF = null;
        TEXTURE_REGION_HALLWAY = null;
        TEXTURE_REGION_FLOOR = null;
        TEXTURE_REGION_CEILING = null;
    }

    public static void addTextures(Level level) {

        for (int i = 0; i < PROGRESS_COUNT; i++) {
            addTexture(level);
        }
    }

    private static void addTexture(Level level) {

        //our texture assets are here
        AssetManager assetManager = level.getAssetManager();

        //if null figure out the totals
        if (TEXTURE_REGIONS == null || TEXTURE_REGIONS.isEmpty()) {
            COUNT = 0;
            TOTAL = level.getDungeon().getCols() * level.getDungeon().getRows();
            COL = 0;
            ROW = 0;

            //load map of textures for our level
            getTextureRegions(level);
            getTextureRegionHallway(assetManager);
            getTextureRegionLocked(assetManager);
            getTextureRegionDoor(assetManager);
            getTextureRegionDoorSide(assetManager);
            getTextureRegionWallGoal(assetManager);
            getTextureRegionDoorGoal(assetManager);
            getTextureRegionSwitchOff(assetManager);
            getTextureRegionFloor(assetManager);
            getTextureRegionCeiling(assetManager);
        }

        //don't continue if finished
        if (COUNT >= TOTAL)
            return;

        //get our dungeon
        Dungeon dungeon = level.getDungeon();

        //get the leaf for the goal room
        Room room = dungeon.getLeafs().get(dungeon.getGoalLeafIndex()).getRoom();

        //get the current cell
        Cell current = dungeon.getCell(COL, ROW);

        //continue searching until we find a
        while (!current.isWall() && !current.isGoal() && !current.isDoor()) {

            //keep making progress
            COL++;
            COUNT++;

            //if at the end, move to the next row
            if (COL >= dungeon.getCols()) {
                COL = 0;
                ROW++;
            }

            //get the next cell
            current = dungeon.getCell(COL, ROW);

            if (current == null)
                return;
        }

        final int col = COL;
        final int row = ROW;

        //is this location part of the goal
        boolean goal = (col >= room.getX() && col < room.getX() + room.getW() && row >= room.getY() && row < room.getY() + room.getH());

        //do we add a door texture
        TextureRegion door = null;

        boolean secret = false;

        if (current.isWall()) {

            TextureRegion texture = (goal) ? getTextureRegionWallGoal(assetManager) : getTextureRegions(level).get(current.getId());

            boolean west = dungeon.hasMap(col - 1, row) || dungeon.getLevel().getObstacles().hasCollision(col - 1, row);
            boolean east = dungeon.hasMap(col + 1, row) || dungeon.getLevel().getObstacles().hasCollision(col + 1, row);
            boolean north = dungeon.hasMap(col, row + 1) || dungeon.getLevel().getObstacles().hasCollision(col, row + 1);
            boolean south = dungeon.hasMap(col, row - 1) || dungeon.getLevel().getObstacles().hasCollision(col, row - 1);

            //we need at least 1 to add a square with a wall(s)
            if (west || east || north || south) {

                //create our square
                Square square = new Square(col, row);

                if (west)
                    square.addWallWest(getWallTexture(texture, dungeon.getCell(col - 1, row), current));
                if (east)
                    square.addWallEast(getWallTexture(texture, dungeon.getCell(col + 1, row), current));
                if (north)
                    square.addWallNorth(getWallTexture(texture, dungeon.getCell(col, row + 1), current));
                if (south)
                    square.addWallSouth(getWallTexture(texture, dungeon.getCell(col,row - 1), current));

                //assign the square to our wall
                level.setWall(col, row, square);
            }

        } else if (current.isGoal()) {
            Square square = new Square(col, row);
            square.addWalls(getTextureRegionSwitchOff(assetManager));
            level.setWall(col, row, square);
        } else if (current.isDoor()) {
            if (current.isLocked()) {
                door = getTextureRegionLocked(assetManager);
            } else if (current.isSecret()) {
                door = getTextureRegions(level).get(current.getId());
                secret = true;
            } else {
                door = (goal) ? getTextureRegionDoorGoal(assetManager) : getTextureRegionDoor(assetManager);
            }
        }

        //do we add a door
        if (door != null) {
            if (dungeon.hasMap(col - 1, row)) {

                if (current.hasId(dungeon.getCell(col - 1, row))) {
                    level.setDoorDecal(DecalCustom.createDecalDoor(col, row, door, SIDE_WEST, secret), col, row);
                } else if (current.hasId(dungeon.getCell(col + 1, row))) {
                    level.setDoorDecal(DecalCustom.createDecalDoor(col, row, door, SIDE_EAST, secret), col, row);
                }

            } else if (dungeon.hasMap(col, row - 1)) {

                if (current.hasId(dungeon.getCell(col, row - 1))) {
                    level.setDoorDecal(DecalCustom.createDecalDoor(col, row, door, SIDE_SOUTH, secret), col, row);
                } else if (current.hasId(dungeon.getCell(col, row + 1))) {
                    level.setDoorDecal(DecalCustom.createDecalDoor(col, row, door, SIDE_NORTH, secret), col, row);
                }
            }
        }

        //keep making progress
        COL++;
        COUNT++;

        //if at the end, move to the next row
        if (COL >= dungeon.getCols()) {
            COL = 0;
            ROW++;
        }

        //if we are at the end let's add the backgrounds
        if (ROW >= dungeon.getRows()) {
            for (int y = (int) -Background.TEXTURE_HEIGHT; y <= dungeon.getRows() + Background.TEXTURE_HEIGHT; y += Background.TEXTURE_HEIGHT) {
                for (int x = (int) -Background.TEXTURE_WIDTH; x <= dungeon.getCols() + Background.TEXTURE_WIDTH; x += Background.TEXTURE_WIDTH) {
                    level.getBackgrounds().add(createDecalBackground(x, y, getTextureRegionFloor(assetManager), true));
                    level.getBackgrounds().add(createDecalBackground(x, y, getTextureRegionCeiling(assetManager), false));
                }
            }
        }
    }

    //wall texture will depend on neighbor
    private static TextureRegion getWallTexture(TextureRegion texture, Cell neighbor, Cell current) {

        if (neighbor.isDoor())
            return getTextureRegionDoorSide(null);
        if (current.getId() != neighbor.getId())
            return getTextureRegionHallway(null);

        return texture;
    }

    private static HashMap<String, TextureRegion> getTextureRegions(Level level) {

        if (TEXTURE_REGIONS == null) {

            TEXTURE_REGIONS = new HashMap<>();

            String tmpId = null;

            Dungeon dungeon = level.getDungeon();
            AssetManager assetManager = level.getAssetManager();

            for (int row = 0; row < dungeon.getRows(); row++) {
                for (int col = 0; col < dungeon.getCols(); col++) {

                    //true means this space is open, so there should be no walls
                    if (dungeon.hasMap(col, row))
                        continue;

                    //if there is only 1, make it part of the same group as this will be part of the hallways
                    if (dungeon.getCell(col, row).isHallway()) {

                        if (tmpId == null)
                            tmpId = dungeon.getCell(col, row).getId();

                        dungeon.getCell(col, row).setId(dungeon.getCell(col, row));

                        //if it already exists, skip to the next
                        if (TEXTURE_REGIONS.get(dungeon.getCell(col, row).getId()) != null)
                            continue;

                        //put it in the hash map
                        TEXTURE_REGIONS.put(dungeon.getCell(col, row).getId(), getTextureRegionHallway(assetManager));

                    } else {

                        //if texture already exists, skip to the next
                        if (TEXTURE_REGIONS.get(dungeon.getCell(col, row).getId()) != null)
                            continue;

                        int index = getRandom().nextInt(getPathsWall().size());
                        TEXTURE_REGIONS.put(dungeon.getCell(col, row).getId(), new TextureRegion(assetManager.get(getPathsWall().get(index), Texture.class)));
                    }
                }
            }
        }

        return TEXTURE_REGIONS;
    }

    private static TextureRegion getTextureRegion(AssetManager assetManager, String path) {
        return new TextureRegion(assetManager.get(path, Texture.class));
    }

    private static void getTextureRegionBackgrounds(AssetManager assetManager) {

        if (TEXTURE_REGION_FLOOR == null || TEXTURE_REGION_CEILING == null) {
            if (getRandom().nextBoolean()) {
                TEXTURE_REGION_FLOOR = new TextureRegion(assetManager.get(getPathsBackground().get(0), Texture.class));
                TEXTURE_REGION_CEILING = new TextureRegion(assetManager.get(getPathsBackground().get(1), Texture.class));
            } else {
                TEXTURE_REGION_FLOOR = new TextureRegion(assetManager.get(getPathsBackground().get(1), Texture.class));
                TEXTURE_REGION_CEILING = new TextureRegion(assetManager.get(getPathsBackground().get(0), Texture.class));
            }
        }
    }

    private static TextureRegion getTextureRegionCeiling(AssetManager assetManager) {

        if (TEXTURE_REGION_CEILING == null)
            getTextureRegionBackgrounds(assetManager);

        return TEXTURE_REGION_CEILING;
    }

    private static TextureRegion getTextureRegionFloor(AssetManager assetManager) {

        if (TEXTURE_REGION_FLOOR == null)
            getTextureRegionBackgrounds(assetManager);

        return TEXTURE_REGION_FLOOR;
    }

    private static TextureRegion getTextureRegionHallway(AssetManager assetManager) {

        if (TEXTURE_REGION_HALLWAY == null)
            TEXTURE_REGION_HALLWAY = new TextureRegion(assetManager.get(getPathsHallway().get(0), Texture.class));

        return TEXTURE_REGION_HALLWAY;
    }

    private static TextureRegion getTextureRegionLocked(AssetManager assetManager) {

        if (TEXTURE_REGION_LOCKED == null)
            TEXTURE_REGION_LOCKED = getTextureRegion(assetManager, PATH_DOOR_LOCKED);

        return TEXTURE_REGION_LOCKED;
    }

    private static TextureRegion getTextureRegionDoor(AssetManager assetManager) {

        if (TEXTURE_REGION_DOOR == null)
            TEXTURE_REGION_DOOR = getTextureRegion(assetManager, PATH_DOOR);

        return TEXTURE_REGION_DOOR;
    }

    private static TextureRegion getTextureRegionDoorSide(AssetManager assetManager) {

        if (TEXTURE_REGION_DOOR_SIDE == null)
            TEXTURE_REGION_DOOR_SIDE = getTextureRegion(assetManager, PATH_SIDE);

        return TEXTURE_REGION_DOOR_SIDE;
    }

    private static TextureRegion getTextureRegionWallGoal(AssetManager assetManager) {

        if (TEXTURE_REGION_WALL_GOAL == null)
            TEXTURE_REGION_WALL_GOAL = getTextureRegion(assetManager, PATH_WALL_GOAL);

        return TEXTURE_REGION_WALL_GOAL;
    }

    private static TextureRegion getTextureRegionDoorGoal(AssetManager assetManager) {

        if (TEXTURE_REGION_DOOR_GOAL == null)
            TEXTURE_REGION_DOOR_GOAL = getTextureRegion(assetManager, PATH_DOOR_GOAL);

        return TEXTURE_REGION_DOOR_GOAL;
    }

    private static TextureRegion getTextureRegionSwitchOff(AssetManager assetManager) {

        if (TEXTURE_REGION_SWITCH_OFF == null)
            TEXTURE_REGION_SWITCH_OFF = getTextureRegion(assetManager, PATH_SWITCH_OFF);

        return TEXTURE_REGION_SWITCH_OFF;
    }
}