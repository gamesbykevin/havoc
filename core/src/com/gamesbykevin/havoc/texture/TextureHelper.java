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
    public static final int TILES_WALL = 162;
    public static final int TILES_HALLWAY = 38;

    //chance we add a secret door
    public static final float DOOR_PROBABILITY = 0.7f;

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

                //if this is a door
                switch (current.getType()) {

                    case Goal:
                        level.getDecals().add(DecalCustom.createDecalWall(col, row, getSwitchGoal(), Side.West));
                        level.getDecals().add(DecalCustom.createDecalWall(col, row, getSwitchGoal(), Side.East));
                        level.getDecals().add(DecalCustom.createDecalWall(col, row, getSwitchGoal(), Side.North));
                        level.getDecals().add(DecalCustom.createDecalWall(col, row, getSwitchGoal(), Side.South));
                        break;

                    case Door:
                        door = (goal) ? getDoorGoal() : getTextureDoor();
                        break;

                    case DoorLocked:
                        door = getTextureDoorLocked();
                        break;

                    case Secret:
                        door = textures.get(current.getId());
                        break;

                    case Wall:
                        TextureRegion texture = (goal) ? getWallGoal() : textures.get(current.getId());

                        if (col > 0 && dungeon.hasMap(col - 1, row)) {
                            Cell west = dungeon.getCells()[row][col - 1];
                            if (west.getType() == Cell.Type.Door) {
                                level.getDecals().add(DecalCustom.createDecalWall(col, row, getTextureSide(), Side.West));
                            } else {
                                if (current.getId() != west.getId()) {
                                    level.getDecals().add(DecalCustom.createDecalWall(col, row, getTextureHallway(), Side.West));
                                } else {
                                    level.getDecals().add(DecalCustom.createDecalWall(col, row, texture, Side.West));
                                }
                            }
                        }

                        if (col < dungeon.getCols() - 1 && dungeon.hasMap(col + 1, row)) {
                            Cell east = dungeon.getCells()[row][col + 1];
                            if (east.getType() == Cell.Type.Door) {
                                level.getDecals().add(DecalCustom.createDecalWall(col, row, getTextureSide(), Side.East));
                            } else {
                                if (current.getId() != east.getId()) {
                                    level.getDecals().add(DecalCustom.createDecalWall(col, row, getTextureHallway(), Side.East));
                                } else {
                                    level.getDecals().add(DecalCustom.createDecalWall(col, row, texture, Side.East));
                                }
                            }
                        }

                        if (row > 0 && dungeon.hasMap(col, row - 1)) {
                            Cell south = dungeon.getCells()[row - 1][col];
                            if (south.getType() == Cell.Type.Door) {
                                level.getDecals().add(DecalCustom.createDecalWall(col, row, getTextureSide(), Side.South));
                            } else {
                                if (current.getId() != south.getId()) {
                                    level.getDecals().add(DecalCustom.createDecalWall(col, row, getTextureHallway(), Side.South));
                                } else {
                                    level.getDecals().add(DecalCustom.createDecalWall(col, row, texture, Side.South));
                                }
                            }
                        }

                        if (row < dungeon.getRows() - 1 && dungeon.hasMap(col, row + 1)) {
                            Cell north = dungeon.getCells()[row + 1][col];
                            if (north.getType() == Cell.Type.Door) {
                                level.getDecals().add(DecalCustom.createDecalWall(col, row, getTextureSide(), Side.North));
                            } else {
                                if (current.getId() != north.getId()) {
                                    level.getDecals().add(DecalCustom.createDecalWall(col, row, getTextureHallway(), Side.North));
                                } else {
                                    level.getDecals().add(DecalCustom.createDecalWall(col, row, texture, Side.North));
                                }
                            }
                        }
                        break;

                    case Open:
                        break;

                    default:
                        door = null;
                        break;
                }

                //do we add a door
                if (door != null) {
                    if (col > 0 && dungeon.hasMap(col - 1, row)) {
                        level.setDoorDecal(DecalCustom.createDecalDoor(col, row, door, Side.West, false), col, row);
                    } else if (row > 0 && dungeon.hasMap(col, row - 1)) {
                        level.setDoorDecal(DecalCustom.createDecalDoor(col, row, door, Side.South, false), col, row);
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

        for (int row = 0; row < dungeon.getRows(); row += Background.TEXTURE_HEIGHT) {
            for (int col = 0; col < dungeon.getCols(); col += Background.TEXTURE_WIDTH) {
                level.getBackgrounds().add(createDecalBackground(col, row, floor, true));
                //level.getBackgrounds().add(createDecalBackground(col, row, ceiling, false));
            }
        }

        //we are done with these objects
        textures.clear();
        textures = null;
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