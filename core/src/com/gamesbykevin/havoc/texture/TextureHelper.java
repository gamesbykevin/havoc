package com.gamesbykevin.havoc.texture;

import com.badlogic.gdx.assets.AssetManager;
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

import static com.gamesbykevin.havoc.assets.AssetManagerHelper.*;
import static com.gamesbykevin.havoc.decals.Background.createDecalBackground;
import static com.gamesbykevin.havoc.dungeon.Dungeon.getRandom;
import static com.gamesbykevin.havoc.dungeon.DungeonHelper.getCount;

public class TextureHelper {

    public static void addTextures(Level level) {

        //get our dungeon
        Dungeon dungeon = level.getDungeon();

        //get hallway texture
        TextureRegion textureHallway = new TextureRegion(level.getAssetManager().get(getPathsHallway().get(0), Texture.class));

        //get map of textures for our level
        HashMap<String, TextureRegion> textures = getTextures(dungeon, level.getAssetManager(), textureHallway);

        //get the leaf for the goal room
        Room room = dungeon.getLeafs().get(dungeon.getGoalLeafIndex()).getRoom();

        TextureRegion textureDoorLocked = new TextureRegion(level.getAssetManager().get(PATH_DOOR_LOCKED, Texture.class));
        TextureRegion textureDoor = new TextureRegion(level.getAssetManager().get(PATH_DOOR, Texture.class));
        TextureRegion textureDoorSide = new TextureRegion(level.getAssetManager().get(PATH_SIDE, Texture.class));
        TextureRegion textureWallGoal = new TextureRegion(level.getAssetManager().get(PATH_WALL_GOAL, Texture.class));
        TextureRegion textureDoorGoal = new TextureRegion(level.getAssetManager().get(PATH_DOOR_GOAL, Texture.class));
        TextureRegion textureSwitchOff = new TextureRegion(level.getAssetManager().get(PATH_SWITCH_OFF, Texture.class));

        for (int row = 0; row < dungeon.getRows(); row++) {
            for (int col = 0; col < dungeon.getCols(); col++) {

                //is this location part of the goal
                boolean goal = (col >= room.getX() && col < room.getX() + room.getW() && row >= room.getY() && row < room.getY() + room.getH());

                Cell current = dungeon.getCells()[row][col];

                //do we add a door texture
                TextureRegion door = null;

                boolean secret = false;

                if (current.isWall()) {

                    TextureRegion texture = (goal) ? textureWallGoal : textures.get(current.getId());

                    if (dungeon.hasMap(col - 1, row) || dungeon.getLevel().getObstacles().hasCollision(col - 1, row))
                        addWallDecal(level.getDecals(), texture, textureDoorSide, textureHallway, dungeon.getCells()[row][col - 1], current, col, row, Side.West);

                    if (dungeon.hasMap(col + 1, row) || dungeon.getLevel().getObstacles().hasCollision(col + 1, row))
                        addWallDecal(level.getDecals(), texture, textureDoorSide, textureHallway, dungeon.getCells()[row][col + 1], current, col, row, Side.East);

                    if (dungeon.hasMap(col, row - 1) || dungeon.getLevel().getObstacles().hasCollision(col, row - 1))
                        addWallDecal(level.getDecals(), texture, textureDoorSide, textureHallway, dungeon.getCells()[row - 1][col], current, col, row, Side.South);

                    if (dungeon.hasMap(col, row + 1) || dungeon.getLevel().getObstacles().hasCollision(col, row + 1))
                        addWallDecal(level.getDecals(), texture, textureDoorSide, textureHallway, dungeon.getCells()[row + 1][col], current, col, row, Side.North);

                } else if (current.isGoal()) {
                    level.getDecals().add(DecalCustom.createDecalWall(col, row, textureSwitchOff, Side.West));
                    level.getDecals().add(DecalCustom.createDecalWall(col, row, textureSwitchOff, Side.East));
                    level.getDecals().add(DecalCustom.createDecalWall(col, row, textureSwitchOff, Side.North));
                    level.getDecals().add(DecalCustom.createDecalWall(col, row, textureSwitchOff, Side.South));
                } else if (current.isDoor()) {
                    if (current.isLocked()) {
                        door = textureDoorLocked;
                    } else if (current.isSecret()) {
                        door = textures.get(current.getId());
                        secret = true;
                    } else {
                        door = (goal) ? textureDoorGoal : textureDoor;
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
            floor = new TextureRegion(level.getAssetManager().get(getPathsBackground().get(0), Texture.class));
            ceiling = new TextureRegion(level.getAssetManager().get(getPathsBackground().get(1), Texture.class));
        } else {
            floor = new TextureRegion(level.getAssetManager().get(getPathsBackground().get(1), Texture.class));
            ceiling = new TextureRegion(level.getAssetManager().get(getPathsBackground().get(0), Texture.class));
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

    private static void addWallDecal(List<DecalCustom> decals, TextureRegion texture, TextureRegion textureDoorSide, TextureRegion textureHallway, Cell neighbor, Cell current, int col, int row, Side side) {

        if (neighbor.isDoor()) {
            decals.add(DecalCustom.createDecalWall(col, row, textureDoorSide, side));
        } else {
            if (current.getId() != neighbor.getId()) {
                decals.add(DecalCustom.createDecalWall(col, row, textureHallway, side));
            } else {
                decals.add(DecalCustom.createDecalWall(col, row, texture, side));
            }
        }
    }

    private static HashMap<String, TextureRegion> getTextures(Dungeon dungeon, AssetManager assetManager, TextureRegion textureHallway) {

        HashMap<String, TextureRegion> textures = new HashMap<>();

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
                    textures.put(dungeon.getCells()[row][col].getId(), textureHallway);

                } else {

                    //if texture already exists, skip to the next
                    if (textures.get(dungeon.getCells()[row][col].getId()) != null)
                        continue;

                    int index = getRandom().nextInt(getPathsWall().size());
                    textures.put(dungeon.getCells()[row][col].getId(), new TextureRegion(assetManager.get(getPathsWall().get(index), Texture.class)));
                }
            }
        }

        //return map of textures
        return textures;
    }
}