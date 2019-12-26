package com.gamesbykevin.havoc.assets;

import com.badlogic.gdx.assets.AssetManager;
import com.gamesbykevin.havoc.animation.DecalAnimation;
import com.gamesbykevin.havoc.decals.Background;
import com.gamesbykevin.havoc.decals.Square;
import com.gamesbykevin.havoc.dungeon.Cell;
import com.gamesbykevin.havoc.dungeon.Dungeon;
import com.gamesbykevin.havoc.level.Level;
import com.gamesbykevin.havoc.location.Location;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.gamesbykevin.havoc.animation.DecalAnimation.*;
import static com.gamesbykevin.havoc.assets.AssetManagerHelper.*;
import static com.gamesbykevin.havoc.decals.Background.*;
import static com.gamesbykevin.havoc.dungeon.Dungeon.getRandom;
import static com.gamesbykevin.havoc.dungeon.LeafHelper.getLeafGoal;

public class TextureHelper {

    //used to progress
    public static float COUNT = 0;
    public static float TOTAL = 1;
    public static int COL;
    public static int ROW;

    //default setup for the decal animation
    private static final int TEXTURE_PIXEL_WIDTH = 64;
    private static final int TEXTURE_PIXEL_HEIGHT = 64;

    //how many updates to make
    public static int PROGRESS_COUNT = 200;

    //list of textures to choose from
    private static HashMap<String, Location> OPTIONS;

    //texture to use for the hallway(s)
    private static Location OPTION_HALLWAY;

    //needed to pick random location
    private static final int SPRITE_SHEET_WALL_COLS = 10;
    private static final int SPRITE_SHEET_WALL_ROWS = 10;
    private static final int SPRITE_SHEET_HALLWAY_COLS = 6;
    private static final int SPRITE_SHEET_HALLWAY_ROWS = 5;

    private static Location getOptionHallway() {

        if (OPTION_HALLWAY == null)
            OPTION_HALLWAY = new Location(getRandom().nextInt(SPRITE_SHEET_HALLWAY_COLS), getRandom().nextInt(SPRITE_SHEET_HALLWAY_ROWS));

        return OPTION_HALLWAY;
    }

    private static HashMap<String, Location> getOptions(Dungeon dungeon) {

        if (OPTIONS == null)
            OPTIONS = new HashMap<>();

        if (OPTIONS.isEmpty()) {

            List<Location> locations = new ArrayList<>();

            for (int y = 0; y < dungeon.getRows(); y++) {
                for (int x = 0; x < dungeon.getCols(); x++) {

                    Cell cell = dungeon.getCell(x, y);

                    //skip if the cell is to not have a texture
                    if (cell == null || cell.isHallway() || cell.isUnvisited() || cell.isOpen())
                        continue;

                    Cell w = dungeon.getCell(x - 1, y);
                    Cell e = dungeon.getCell(x + 1, y);
                    Cell n = dungeon.getCell(x, y + 1);
                    Cell s = dungeon.getCell(x, y - 1);

                    //if surrounded by walls skip it
                    if ((w == null || w.isWall()) && (e == null || e.isWall()) && (n == null || n.isWall()) && (s == null || s.isWall()))
                        continue;

                    //skip if we already have an entry
                    if (OPTIONS.get(cell.getId()) != null)
                        continue;

                    //populate with values if empty
                    if (locations.isEmpty()) {
                        for (int row = 0; row < SPRITE_SHEET_WALL_ROWS; row++) {
                            for (int col = 0; col < SPRITE_SHEET_WALL_COLS; col++) {
                                locations.add(new Location(col, row));
                            }
                        }
                    }

                    //pick a random texutre
                    int index = getRandom().nextInt(locations.size());

                    //assign all cells with this id the same texture
                    OPTIONS.put(cell.getId(), locations.get(index));

                    //don't pick it again
                    locations.remove(index);
                }
            }

            locations.clear();
            locations = null;
        }

        return OPTIONS;
    }

    public static void addTextures(Level level) {

        for (int i = 0; i < PROGRESS_COUNT; i++) {
            addTexture(level);
        }
    }

    private static void addTexture(Level level) {

        //figure out the totals for our progress bar
        if (TOTAL <= 1 && COUNT <= 0) {
            COUNT = 0;
            TOTAL = level.getDungeon().getCols() * level.getDungeon().getRows();
            COL = 0;
            ROW = 0;
        }

        //don't continue if finished
        if (COUNT >= TOTAL)
            return;

        //populate the list of options
        getOptions(level.getDungeon());
        getOptionHallway();

        //get our dungeon
        Dungeon dungeon = level.getDungeon();

        //get the current cell
        Cell current = dungeon.getCell(COL, ROW);

        //continue searching until we find a place to add textures
        while (current.isOpen() || current.isUnvisited() || current.isHallway()) {

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

        //get our neighbors
        Cell neighborW = dungeon.getCell(current.getCol() - 1, current.getRow());
        Cell neighborE = dungeon.getCell(current.getCol() + 1, current.getRow());
        Cell neighborN = dungeon.getCell(current.getCol(), current.getRow() + 1);
        Cell neighborS = dungeon.getCell(current.getCol(), current.getRow() - 1);

        //object containing our walls
        Square square = null;

        if (current.isWall() || current.isGoal()) {

            boolean west = requiresWall(neighborW);
            boolean east = requiresWall(neighborE);
            boolean north = requiresWall(neighborN);
            boolean south = requiresWall(neighborS);

            if (west || east || north || south)
                square = new Square(current.getCol(), current.getRow());

            if (west) {
                if (current.isWall()) {
                    square.addWallWest(getDecalAnimationWall(level, current, neighborW), current);
                } else {
                    square.addWallWest(getDecalAnimationGoal(level.getAssetManager()), current);
                }
            }
            if (east) {
                if (current.isWall()) {
                    square.addWallEast(getDecalAnimationWall(level, current, neighborE), current);
                } else {
                    square.addWallEast(getDecalAnimationGoal(level.getAssetManager()), current);
                }
            }
            if (north) {
                if (current.isWall()) {
                    square.addWallNorth(getDecalAnimationWall(level, current, neighborN), current);
                } else {
                    square.addWallNorth(getDecalAnimationGoal(level.getAssetManager()), current);
                }
            }
            if (south) {
                if (current.isWall()) {
                    square.addWallSouth(getDecalAnimationWall(level, current, neighborS), current);
                } else {
                    square.addWallSouth(getDecalAnimationGoal(level.getAssetManager()), current);
                }
            }

            if (square != null)
                level.setWall(current.getCol(), current.getRow(), square);

        } else if (current.isDoor() || current.isSecret() || current.isLocked()) {

            DecalAnimation animation = getDecalAnimationDoor(level, current);
            Side side = Side.None;
            if (dungeon.hasMap(current.getCol() - 1, current.getRow())) {

                if (current.hasId(dungeon.getCell(current.getCol() - 1, current.getRow()))) {
                    side = Side.West;
                } else if (current.hasId(dungeon.getCell(current.getCol() + 1, current.getRow()))) {
                    side = Side.East;
                }

            } else if (dungeon.hasMap(current.getCol(), current.getRow() - 1)) {
                if (current.hasId(dungeon.getCell(current.getCol(), current.getRow() - 1))) {
                    side = Side.South;
                } else if (current.hasId(dungeon.getCell(current.getCol(), current.getRow() + 1))) {
                    side = Side.North;
                }
            }

            level.setDoorDecal(createDecalDoor(side, animation, current), current.getCol(), current.getRow());

        } else {
            throw new RuntimeException("cell not identified");
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

            int width = (int)(Background.TEXTURE_WIDTH * TEXTURE_PIXEL_WIDTH);
            int height = (int)(Background.TEXTURE_HEIGHT * TEXTURE_PIXEL_HEIGHT);

            for (int y = (int) -Background.TEXTURE_HEIGHT; y <= dungeon.getRows() + Background.TEXTURE_HEIGHT; y += Background.TEXTURE_HEIGHT) {
                for (int x = (int) -Background.TEXTURE_WIDTH; x <= dungeon.getCols() + Background.TEXTURE_WIDTH; x += Background.TEXTURE_WIDTH) {

                    DecalAnimation floor = new DecalAnimation(level.getAssetManager(), getPathsBackground().get(0), width, height, TEXTURE_WIDTH, TEXTURE_HEIGHT, BILLBOARD_DISABLED);
                    DecalAnimation ceiling = new DecalAnimation(level.getAssetManager(), getPathsBackground().get(1), width, height, TEXTURE_WIDTH, TEXTURE_HEIGHT, BILLBOARD_DISABLED);

                    level.getBackgrounds().add(createDecalBackground(floor, x, y, true));
                    level.getBackgrounds().add(createDecalBackground(ceiling, x, y,  false));
                }
            }
        }
    }

    private static boolean requiresWall(Cell cell) {
        return (cell != null && (cell.isHallway() || cell.isDoor() || cell.isOpen() || cell.isLocked() || cell.isSecret()));
    }

    private static DecalAnimation getDecalAnimationDoor(Level level, Cell current) {

        String path = null;
        int spriteCol = 0;
        int spriteRow = 0;

        if (current.isDoor()) {

            path = ASSET_SHEET_STANDARD;

            if (getLeafGoal(level.getDungeon()).getRoom().contains(current)) {
                spriteCol = 0;
                spriteRow = 1;
            } else {
                spriteCol = 0;
                spriteRow = 0;
            }

        } else if (current.isLocked()) {
            path = ASSET_SHEET_STANDARD;
            spriteCol = 1;
            spriteRow = 0;
        } else if (current.isSecret()) {
            path = ASSET_SHEET_WALLS;
            spriteCol = (int)getOptions(null).get(current.getId()).getCol();
            spriteRow = (int)getOptions(null).get(current.getId()).getRow();
        }

        return new DecalAnimation(level.getAssetManager(), path, TEXTURE_PIXEL_WIDTH, TEXTURE_PIXEL_HEIGHT, spriteCol, spriteRow, BILLBOARD_DISABLED);
    }

    private static DecalAnimation getDecalAnimationGoal(AssetManager assetManager) {
        String path = ASSET_SHEET_STANDARD;
        int spriteCol = 1;
        int spriteRow = 1;
        int cols = 3;
        int rows = 3;
        int increment = 1;
        int count  = 2;
        return new DecalAnimation(assetManager, path, cols, rows, TEXTURE_PIXEL_WIDTH, TEXTURE_PIXEL_HEIGHT, spriteCol, spriteRow, increment, count, 1, DEFAULT_WIDTH, DEFAULT_HEIGHT, BILLBOARD_DISABLED);
    }

    private static DecalAnimation getDecalAnimationWall(Level level, Cell current, Cell neighbor) {

        String path = null;
        int spriteCol = 0;
        int spriteRow = 0;

        if (neighbor.isSecret() || neighbor.isLocked() || neighbor.isDoor()) {
            path = ASSET_SHEET_STANDARD;
            spriteCol = 2;
            spriteRow = 0;
        } else if (neighbor.isHallway()) {
            path = ASSET_SHEET_HALLWAYS;
            spriteCol = (int)getOptionHallway().getCol();
            spriteRow = (int)getOptionHallway().getRow();
        } else if (neighbor.isOpen()) {
            if (getLeafGoal(level.getDungeon()).getRoom().contains(current)) {
                path = ASSET_SHEET_STANDARD;
                spriteCol = 0;
                spriteRow = 2;
            } else {
                path = ASSET_SHEET_WALLS;
                spriteCol = (int) getOptions(null).get(current.getId()).getCol();
                spriteRow = (int) getOptions(null).get(current.getId()).getRow();
            }
        }

        return new DecalAnimation(level.getAssetManager(), path, TEXTURE_PIXEL_WIDTH, TEXTURE_PIXEL_HEIGHT, spriteCol, spriteRow, BILLBOARD_DISABLED);
    }
}