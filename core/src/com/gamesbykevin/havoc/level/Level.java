package com.gamesbykevin.havoc.level;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.decals.DecalBatch;
import com.gamesbykevin.havoc.collectibles.CollectibleHelper;
import com.gamesbykevin.havoc.collectibles.Collectibles;
import com.gamesbykevin.havoc.decals.DecalCustom;
import com.gamesbykevin.havoc.decals.Door;
import com.gamesbykevin.havoc.decals.Square;
import com.gamesbykevin.havoc.dungeon.Dungeon;
import com.gamesbykevin.havoc.enemies.Enemies;
import com.gamesbykevin.havoc.entities.Entities;
import com.gamesbykevin.havoc.obstacles.Obstacles;
import com.gamesbykevin.havoc.player.Player;
import com.gamesbykevin.havoc.util.Disposable;
import com.gamesbykevin.havoc.util.Restart;
import com.gamesbykevin.havoc.decals.MyGroupStrategy;

import java.util.ArrayList;
import java.util.List;

import static com.gamesbykevin.havoc.dungeon.DungeonHelper.DUNGEON_SIZE;
import static com.gamesbykevin.havoc.dungeon.LeafHelper.LEAF_DIMENSION_MIN;
import static com.gamesbykevin.havoc.dungeon.RoomHelper.ROOM_DIMENSION_MAX;
import static com.gamesbykevin.havoc.input.MyControllerHelper.updateLocation;
import static com.gamesbykevin.havoc.level.LevelHelper.*;
import static com.gamesbykevin.havoc.player.PlayerHelper.checkCollision;

public class Level implements Disposable, Restart {

    //our randomly created dungeon
    private Dungeon dungeon;

    //needed to render multiple decals
    private DecalBatch decalBatch;

    //contain the floor / ceiling
    private List<DecalCustom> backgrounds;

    //contains our doors
    private Door[][] doorDecals;

    //array of all wall decals
    private Square[][] walls;

    //our enemies are contained here
    private Entities enemies;

    //obstacles in the level
    private Entities obstacles;

    //items for pickup
    private Entities collectibles;

    //reference to the player
    private final Player player;

    //how far away can we render?
    public static final int RENDER_RANGE = LEAF_DIMENSION_MIN;

    //where we hold our assets
    private final AssetManager assetManager;

    //do we reset?
    private boolean reset = false;

    //is the game paused
    private boolean paused = false;

    public Level(AssetManager assetManager, Player player) {

        //store reference to our asset manager
        this.assetManager = assetManager;

        //store our player reference
        this.player = player;

        //create the batch now that we have the player reference
        getDecalBatch();

        //how big should the dungeon be?
        int size = DUNGEON_SIZE;

        //dungeon must meet a minimum size
        if (size < ROOM_DIMENSION_MAX * 3)
            size = ROOM_DIMENSION_MAX * 3;

        //create and generate the dungeon
        this.dungeon = new Dungeon(this, size, size);
        getWalls();
    }

    public boolean isPaused() {
        return this.paused;
    }

    public void setPaused(boolean paused) {
        this.paused = paused;
    }

    @Override
    public void reset() {
        setPaused(false);
        getEnemies().reset();
        getObstacles().reset();
        getCollectibles().reset();
        getPlayer().reset();
        getPlayer().getWeapons().reset();
        getPlayer().getController().reset();

        //reset door decals as well
        for (int row = 0; row < getDoorDecals().length; row++) {
            for (int col = 0; col < getDoorDecals()[0].length; col++) {

                if (getDoorDecal(col, row) != null)
                    getDoorDecal(col, row).reset();
            }
        }

        //reset wall decals as well
        for (int row = 0; row < getWalls().length; row++) {
            for (int col = 0; col < getWalls()[0].length; col++) {

                if (getWall(col, row) != null)
                    getWall(col, row).reset();
            }
        }

        //we reset so change flag
        setReset(false);
    }

    public boolean isReset() {
        return this.reset;
    }

    public void setReset(boolean reset) {
        this.reset = reset;
    }

    public AssetManager getAssetManager() {
        return this.assetManager;
    }

    public Player getPlayer() {
        return this.player;
    }

    public Dungeon getDungeon() {
        return this.dungeon;
    }

    public Entities getCollectibles() {

        if (this.collectibles == null)
            this.collectibles = new Collectibles(this);

        return this.collectibles;
    }

    public Entities getObstacles() {

        if (this.obstacles == null)
            this.obstacles = new Obstacles(this);

        return this.obstacles;
    }

    public Entities getEnemies() {

        if (this.enemies == null)
            this.enemies = new Enemies(this);

        return this.enemies;
    }

    public DecalBatch getDecalBatch() {

        if (this.decalBatch == null)
            this.decalBatch = new DecalBatch(new MyGroupStrategy(getPlayer().getCamera3d()));

        return this.decalBatch;
    }

    public List<DecalCustom> getBackgrounds() {

        if (this.backgrounds == null)
            this.backgrounds = new ArrayList<>();

        return this.backgrounds;
    }

    public void setDoorDecal(Door door, float col, float row) {
        setDoorDecal(door, (int)col, (int)row);
    }

    public void setDoorDecal(Door door, int col, int row) {
        getDoorDecals()[row][col] = door;
    }

    public Door getDoorDecal(float col, float row) {
        return getDoorDecal((int)col, (int)row);
    }

    public Door getDoorDecal(int col, int row) {

        if (row < 0 || row >= getDoorDecals().length)
            return null;
        if (col < 0 || col >= getDoorDecals()[0].length)
            return null;

        return getDoorDecals()[row][col];
    }

    public Square getWall(int col, int row) {

        if (col < 0 || row < 0)
            return null;
        if (col >= getWalls()[0].length || row >= getWalls().length)
            return null;

        return getWalls()[row][col];
    }

    public void setWall(float col, float row, Square square) {
        setWall((int)col, (int)row, square);
    }

    public void setWall(int col, int row, Square square) {
        getWalls()[row][col] = square;
    }

    protected Square[][] getWalls() {

        if (this.walls == null)
            this.walls = new Square[getDungeon().getRows()][getDungeon().getCols()];

        return this.walls;
    }

    public Door[][] getDoorDecals() {

        if (this.doorDecals == null)
            this.doorDecals = new Door[getDungeon().getRows()][getDungeon().getCols()];

        return doorDecals;
    }

    private void update() {

        //update the players location
        updateLocation(getPlayer());

        //check if any collectibles have been collected
        CollectibleHelper.check(this);

        //check for collision
        checkCollision(this);

        //update these
        updateLevel(this);
        getPlayer().update(this);
        getPlayer().getWeapons().update();
        getEnemies().update();
        getObstacles().update();
        getCollectibles().update();
    }

    //need to determine the range here so we don't render too many objects
    public void render() {

        //if we are flagged to reset, reset everything
        if (isReset()) {
            reset();
            return;
        }

        //where are we facing
        float rotation = getPlayer().getController().getRotation();

        //get the camera
        PerspectiveCamera camera = getPlayer().getCamera3d();

        //calculate the min/max col/row so we render only items that we can see
        int rowMin = (int)(camera.position.y - RENDER_RANGE);
        int rowMax = (int)(camera.position.y + RENDER_RANGE);
        int colMin = (int)(camera.position.x - RENDER_RANGE);
        int colMax = (int)(camera.position.x + RENDER_RANGE);

        //we don't want to render items that are behind us
        if (rotation <= 45 || rotation >= 315)
            rowMin = (int) (camera.position.y - 1);

        if (rotation <= 315 && rotation >= 225)
            colMin = (int) (camera.position.x - 1);

        if (rotation <= 225 && rotation >= 135)
            rowMax = (int) (camera.position.y + 1);

        if (rotation <= 135 && rotation >= 45)
            colMax = (int) (camera.position.x + 1);

        //can we reduce the range
        colMax = getRangeColMax(camera, this, colMax, rowMin, rowMax);
        colMin = getRangeColMin(camera, this, colMin, rowMin, rowMax);
        rowMax = getRangeRowMax(camera, this, rowMax, colMin, colMax);
        rowMin = getRangeRowMin(camera, this, rowMin, colMin, colMax);

        if (rowMin < 0)
            rowMin = 0;
        if (colMin < 0)
            colMin = 0;
        if (rowMax >= walls.length)
            rowMax = walls.length - 1;
        if (colMax >= walls[0].length)
            colMax = walls[0].length - 1;

        //now that we know the range we can now render the objects
        render(colMin, colMax, rowMin, rowMax);
    }

    //now we can render the objects within range
    private void render(int colMin, int colMax, int rowMin, int rowMax) {

        //update game components, etc...
        if (!isPaused())
            update();

        //update the camera
        getPlayer().getCamera3d().update();

        int count = 0;

        //draw the walls
        count += renderWalls(getWalls(), getDecalBatch(), getPlayer().getCamera3d(), colMin, colMax, rowMin, rowMax);

        //render the backgrounds
        count += renderBackground(getBackgrounds(), getPlayer().getCamera3d().position, getDecalBatch());

        //render the doors
        count += renderDoorDecals(getDoorDecals(), getDecalBatch(), getPlayer().getCamera3d(), colMin, colMax, rowMin, rowMax);

        //render the enemies
        count += getEnemies().render(colMin, colMax, rowMin, rowMax);

        //render the obstacles
        count += getObstacles().render(colMin, colMax, rowMin, rowMax);

        //render the collectibles
        count += getCollectibles().render(colMin, colMax, rowMin, rowMax);

        if (count > 300)
            System.out.println(count);

        //call flush at the end to draw
        getDecalBatch().flush();
    }

    @Override
    public void dispose() {

        if (this.dungeon != null)
            this.dungeon.dispose();
        if (this.decalBatch != null)
            this.decalBatch.dispose();

        if (this.walls != null) {
            for (int row = 0; row < this.walls.length; row++) {
                for (int col = 0; col < this.walls[0].length; col++) {
                    if (this.walls[row][col] != null) {
                        this.walls[row][col].dispose();
                        this.walls[row][col] = null;
                    }
                }
            }
        }

        if (this.backgrounds != null) {
            for (int i = 0; i < this.backgrounds.size(); i++) {
                if (this.backgrounds.get(i) != null) {
                    this.backgrounds.get(i).dispose();
                    this.backgrounds.set(i, null);
                }
            }

            this.backgrounds.clear();
        }

        if (this.enemies != null)
            this.enemies.dispose();
        if (this.obstacles != null)
            this.obstacles.dispose();
        if (this.collectibles != null)
            this.collectibles.dispose();

        if (this.doorDecals != null) {
            for (int row = 0; row < this.doorDecals.length; row++) {
                for (int col = 0; col < this.doorDecals[0].length; col++) {

                    if (this.doorDecals[row][col] != null)
                        this.doorDecals[row][col].dispose();

                    this.doorDecals[row][col] = null;
                }
            }
        }

        this.dungeon = null;
        this.decalBatch = null;
        this.walls = null;
        this.backgrounds = null;
        this.enemies = null;
        this.obstacles = null;
        this.collectibles = null;
        this.doorDecals = null;
    }
}