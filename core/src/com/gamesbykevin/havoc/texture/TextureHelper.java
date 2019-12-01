package com.gamesbykevin.havoc.texture;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.gamesbykevin.havoc.decals.Background;
import com.gamesbykevin.havoc.decals.DecalCustom;
import com.gamesbykevin.havoc.decals.DecalCustom.Side;
import com.gamesbykevin.havoc.dungeon.Cell;
import com.gamesbykevin.havoc.dungeon.Dungeon;
import com.gamesbykevin.havoc.dungeon.Room;
import com.gamesbykevin.havoc.level.Level;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.gamesbykevin.havoc.decals.Background.createDecalBackground;
import static com.gamesbykevin.havoc.dungeon.Dungeon.getRandom;
import static com.gamesbykevin.havoc.dungeon.DungeonHelper.getCount;

public class TextureHelper {

    //locations of some of our textures

    public static final String PATH_DOOR = "door/door.bmp";
    public static final String PATH_DOOR_LOCKED = "door/door_locked_1.bmp";
    public static final String PATH_SIDE = "door/door_side.bmp";
    public static final String PATH_DOOR_GOAL = "goal/door.bmp";
    public static final String PATH_WALL_GOAL = "goal/wall.bmp";
    public static final String PATH_SWITCH_OFF = "goal/switch_off.bmp";
    public static final String PATH_SWITCH_ON = "goal/switch_on.bmp";

    private static TextureRegion TEXTURE_DOOR;
    private static TextureRegion TEXTURE_DOOR_LOCKED;
    private static TextureRegion TEXTURE_SIDE;
    private static TextureRegion TEXTURE_HALLWAY;

    //how many tiles can we choose from for the floor ceiling?
    public static final int TILES_BACKGROUND_LIGHT = 34;
    public static final int TILES_BACKGROUND_DARK = 32;

    //how many tiles can we choose from for the walls
    public static final int TILES_WALL = 159;
    public static final int TILES_HALLWAY = 38;

    private static TextureRegion getTextureRegion(String path) {
        return new TextureRegion(new Texture(Gdx.files.internal(path)));
    }

    private static TextureRegion getTextureHallway() {

        if (TEXTURE_HALLWAY == null)
            TEXTURE_HALLWAY = getTextureRegion("door/hallway/hallway(" + getRandom().nextInt(TILES_HALLWAY) + ").bmp");

        return TEXTURE_HALLWAY;
    }

    private static TextureRegion getTextureDoorLocked() {

        if (TEXTURE_DOOR_LOCKED == null)
            TEXTURE_DOOR_LOCKED = getTextureRegion(PATH_DOOR_LOCKED);

        return TEXTURE_DOOR_LOCKED;
    }

    private static TextureRegion getTextureDoor() {

        if (TEXTURE_DOOR == null)
            TEXTURE_DOOR = getTextureRegion(PATH_DOOR);

        return TEXTURE_DOOR;
    }

    private static TextureRegion getTextureSide() {

        if (TEXTURE_SIDE == null)
            TEXTURE_SIDE = getTextureRegion(PATH_SIDE);

        return TEXTURE_SIDE;
    }

    private static TextureRegion getBackgroundRandomLight() {
        return getBackgroundLight(getRandom().nextInt(TILES_BACKGROUND_LIGHT));
    }

    private static TextureRegion getBackgroundLight(int index) {
        return getTextureRegion("background/light/tile" + index + ".bmp");
    }

    private static TextureRegion getBackgroundRandomDark() {
        return getBackgroundDark(getRandom().nextInt(TILES_BACKGROUND_DARK));
    }

    private static TextureRegion getBackgroundDark(int index) {
        return getTextureRegion("background/dark/tile" + index + ".bmp");
    }

    private static TextureRegion getWall(int index) {
        return getTextureRegion("walls/tile (" + index + ").bmp");
    }

    private static TextureRegion getWallGoal() {
        return getTextureRegion(PATH_WALL_GOAL);
    }

    private static TextureRegion getDoorGoal() {
        return getTextureRegion(PATH_DOOR_GOAL);
    }

    private static TextureRegion getSwitchGoal() {
        return getTextureRegion(PATH_SWITCH_OFF);
    }

    public static void addTextures(Level level) {

        //get our dungeon
        Dungeon dungeon = level.getDungeon();

        //get map of textures for our level
        HashMap<String, TextureRegion> textures = getTextures(dungeon);

        //get the leaf for the goal room
        Room room = dungeon.getLeafs().get(dungeon.getGoalLeafIndex()).getRoom();

        for (int row = 0; row < dungeon.getRows(); row++) {
            for (int col = 0; col < dungeon.getCols(); col++) {

                //is this location part of the goal
                boolean goal = (col >= room.getX() && col < room.getX() + room.getW() && row >= room.getY() && row < room.getY() + room.getH());

                Cell current = dungeon.getCells()[row][col];

                //do we add a door texture
                TextureRegion door = null;

                boolean secret = false;

                if (current.isWall()) {
                    TextureRegion texture = (goal) ? getWallGoal() : textures.get(current.getId());

                    if (dungeon.hasMap(col - 1, row) || dungeon.getLevel().getObstacles().hasCollision(col - 1, row))
                        addWallDecal(level.getDecals(), texture, dungeon.getCells()[row][col - 1], current, col, row, Side.West);

                    if (dungeon.hasMap(col + 1, row) || dungeon.getLevel().getObstacles().hasCollision(col + 1, row))
                        addWallDecal(level.getDecals(), texture, dungeon.getCells()[row][col + 1], current, col, row, Side.East);

                    if (dungeon.hasMap(col, row - 1) || dungeon.getLevel().getObstacles().hasCollision(col, row - 1))
                        addWallDecal(level.getDecals(), texture, dungeon.getCells()[row - 1][col], current, col, row, Side.South);

                    if (dungeon.hasMap(col, row + 1) || dungeon.getLevel().getObstacles().hasCollision(col, row + 1))
                        addWallDecal(level.getDecals(), texture, dungeon.getCells()[row + 1][col], current, col, row, Side.North);

                } else if (current.isGoal()) {
                    level.getDecals().add(DecalCustom.createDecalWall(col, row, getSwitchGoal(), Side.West));
                    level.getDecals().add(DecalCustom.createDecalWall(col, row, getSwitchGoal(), Side.East));
                    level.getDecals().add(DecalCustom.createDecalWall(col, row, getSwitchGoal(), Side.North));
                    level.getDecals().add(DecalCustom.createDecalWall(col, row, getSwitchGoal(), Side.South));
                } else if (current.isDoor()) {
                    if (current.isLocked()) {
                        door = getTextureDoorLocked();
                    } else if (current.isSecret()) {
                        door = textures.get(current.getId());
                        secret = true;
                    } else {
                        door = (goal) ? getDoorGoal() : getTextureDoor();
                    }
                }

                //do we add a door
                if (door != null) {
                    if (dungeon.hasMap(col - 1, row)) {

                        if (current.hasId(dungeon.getCells()[row][col - 1])) {
                            level.setDoorDecal(DecalCustom.createDecalDoor(col, row, door, Side.West, secret), col, row);
                        } else if (current.hasId(dungeon.getCells()[row][col + 1])) {
                            level.setDoorDecal(DecalCustom.createDecalDoor(col, row, door, Side.East, secret), col, row);
                        }

                    } else if (dungeon.hasMap(col, row - 1)) {

                        if (current.hasId(dungeon.getCells()[row - 1][col])) {
                            level.setDoorDecal(DecalCustom.createDecalDoor(col, row, door, Side.South, secret), col, row);
                        } else if (current.hasId(dungeon.getCells()[row + 1][col])) {
                            level.setDoorDecal(DecalCustom.createDecalDoor(col, row, door, Side.North, secret), col, row);
                        }
                    }
                }
            }
        }

        TextureRegion floor;
        TextureRegion ceiling;

        //make sure floor/ceiling are different
        if (getRandom().nextBoolean()) {
            floor = getBackgroundRandomDark();
            ceiling = getBackgroundRandomLight();
        } else {
            floor = getBackgroundRandomLight();
            ceiling = getBackgroundRandomDark();
        }

        for (int row = (int)-Background.TEXTURE_HEIGHT; row <= dungeon.getRows() + Background.TEXTURE_HEIGHT; row += Background.TEXTURE_HEIGHT) {
            for (int col = (int)-Background.TEXTURE_WIDTH; col <= dungeon.getCols() + Background.TEXTURE_WIDTH; col += Background.TEXTURE_WIDTH) {
                level.getBackgrounds().add(createDecalBackground(col, row, floor, true));
                level.getBackgrounds().add(createDecalBackground(col, row, ceiling, false));
            }
        }

        //we are done with these objects
        textures.clear();
        textures = null;
    }

    private static void addWallDecal(List<DecalCustom> decals, TextureRegion texture, Cell neighbor, Cell current, int col, int row, Side side) {

        if (neighbor.isDoor()) {
            decals.add(DecalCustom.createDecalWall(col, row, getTextureSide(), side));
        } else {
            if (current.getId() != neighbor.getId()) {
                decals.add(DecalCustom.createDecalWall(col, row, getTextureHallway(), side));
            } else {
                decals.add(DecalCustom.createDecalWall(col, row, texture, side));
            }
        }
    }

    private static HashMap<String, TextureRegion> getTextures(Dungeon dungeon) {

        HashMap<String, TextureRegion> textures = new HashMap<>();

        //list of optional textures
        List<Integer> options = new ArrayList<>();
        for (int i = 0; i < TILES_WALL; i++) {
            options.add(i);
        }

        String tmpId = null;

        for (int row = 0; row < dungeon.getRows(); row++) {
            for (int col = 0; col < dungeon.getCols(); col++) {

                //true means this space is open, so there should be no walls
                if (dungeon.hasMap(col, row))
                    continue;

                //if there is only 1, make it part of the same group
                if (getCount(dungeon, dungeon.getCells()[row][col].getId()) < 2) {

                    if (tmpId == null)
                        tmpId = dungeon.getCells()[row][col].getId();

                    dungeon.getCells()[row][col].setId(dungeon.getCells()[row][col]);

                    //if it already exists, skip to the next
                    if (textures.get(dungeon.getCells()[row][col].getId()) != null)
                        continue;

                    //put it in the hash map
                    textures.put(dungeon.getCells()[row][col].getId(), getTextureHallway());

                } else {

                    //if it already exists, skip to the next
                    if (textures.get(dungeon.getCells()[row][col].getId()) != null)
                        continue;

                    int index = getRandom().nextInt(options.size());
                    textures.put(dungeon.getCells()[row][col].getId(), getWall(options.get(index)));
                    options.remove(index);

                    //re-populate the list if empty so we don't run out of textures
                    if (options.isEmpty()) {
                        for (int i = 0; i < TILES_WALL; i++) {
                            options.add(i);
                        }
                    }
                }
            }
        }

        options.clear();
        options = null;

        //return map of textures
        return textures;
    }

    public static void recycle() {
        TEXTURE_DOOR = null;
        TEXTURE_DOOR_LOCKED = null;
        TEXTURE_SIDE = null;
        TEXTURE_HALLWAY = null;
    }
}