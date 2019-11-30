package com.gamesbykevin.havoc.level;

import com.badlogic.gdx.graphics.g3d.decals.CameraGroupStrategy;
import com.badlogic.gdx.graphics.g3d.decals.DecalBatch;
import com.gamesbykevin.havoc.collectables.Collectibles;
import com.gamesbykevin.havoc.decals.DecalCustom;
import com.gamesbykevin.havoc.decals.Door;
import com.gamesbykevin.havoc.dungeon.Dungeon;
import com.gamesbykevin.havoc.enemies.Enemies;
import com.gamesbykevin.havoc.entities.Entities;
import com.gamesbykevin.havoc.input.MyController;
import com.gamesbykevin.havoc.obstacles.Obstacles;
import com.gamesbykevin.havoc.player.Player;
import com.gamesbykevin.havoc.util.Disposable;

import java.util.ArrayList;
import java.util.List;

import static com.gamesbykevin.havoc.dungeon.DungeonHelper.DUNGEON_SIZE;
import static com.gamesbykevin.havoc.dungeon.LeafHelper.LEAF_DIMENSION_MIN;
import static com.gamesbykevin.havoc.dungeon.RoomHelper.ROOM_DIMENSION_MAX;
import static com.gamesbykevin.havoc.level.LevelHelper.*;
import static com.gamesbykevin.havoc.player.PlayerHelper.checkCollision;
import static com.gamesbykevin.havoc.texture.TextureHelper.addTextures;

public class Level implements Disposable {

    //our randomly created dungeon
    private Dungeon dungeon;

    //needed to render multiple decals
    private DecalBatch decalBatch;

    //this is where we will contain our walls
    private List<DecalCustom> decals;

    //contain the floor / ceiling
    private List<DecalCustom> backgrounds;

    //contains our doors
    private Door[][] doorDecals;

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

    public Level(Player player) {

        //store our player reference
        this.player = player;

        //how big should the dungeon be?
        int size = DUNGEON_SIZE;

        //dungeon must meet a minimum size
        if (size < ROOM_DIMENSION_MAX * 3)
            size = ROOM_DIMENSION_MAX * 3;

        //create and generate the dungeon
        this.dungeon = new Dungeon(this, size, size);
        getDungeon().generate();

        //flag the players start location
        getPlayer().setStart(getDungeon().getStartCol(), getDungeon().getStartRow());

        //create the obstacles
        getObstacles().spawn();

        //create the enemies
        getEnemies().spawn();

        //create the collectibles
        getCollectibles().spawn();

        //add textures
        addTextures(this);

        //create the batch
        getDecalBatch();
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
            this.decalBatch = new DecalBatch(new CameraGroupStrategy(getPlayer().getCamera3d()));

        return this.decalBatch;
    }

    public List<DecalCustom> getBackgrounds() {

        if (this.backgrounds == null)
            this.backgrounds = new ArrayList<>();

        return this.backgrounds;
    }

    public List<DecalCustom> getDecals() {

        if (this.decals == null)
            this.decals = new ArrayList<>();

        return this.decals;
    }

    public void setDoorDecal(Door door, int col, int row) {
        getDoorDecals()[row][col] = door;
    }

    public Door getDoorDecal(int col, int row) {

        if (row < 0 || row >= getDoorDecals().length)
            return null;
        if (col < 0 || col >= getDoorDecals()[0].length)
            return null;

        return getDoorDecals()[row][col];
    }

    protected Door[][] getDoorDecals() {

        if (this.doorDecals == null)
            this.doorDecals = new Door[getDungeon().getRows()][getDungeon().getCols()];

        return doorDecals;
    }

    private void update() {

        //update the players location
        updateLocation(getPlayer());

        //check for collision
        checkCollision(this);

        //update the level
        updateLevel(this);

        //update the player
        getPlayer().update();
    }

    public void render() {

        //update the camera
        getPlayer().getCamera3d().update();

        //update our decals, etc...
        update();

        int count = 0;

        //draw the walls
        count += renderWalls(getDecals(), getDecalBatch(), getPlayer().getCamera3d());

        //render the backgrounds
        count += renderBackground(getBackgrounds(), getPlayer().getCamera3d().position, getDecalBatch());

        //render the doors
        count += renderDoorDecals(getDoorDecals(), getDecalBatch(), getPlayer().getCamera3d());

        //render the enemies
        count += getEnemies().render(false);

        //render the obstacles
        count += getObstacles().render(false);

        //render the collectibles
        count += getCollectibles().render(true);

        if (count > 400)
            System.out.println("Rendered: " + count);

        //call flush at the end to draw
        getDecalBatch().flush();
    }

    @Override
    public void dispose() {

        if (this.dungeon != null)
            this.dungeon.dispose();
        if (this.decalBatch != null)
            this.decalBatch.dispose();
        if (this.decals != null) {
            for (int i = 0; i < this.decals.size(); i++) {
                if (this.decals.get(i) != null) {
                    this.decals.get(i).dispose();
                    this.decals.set(i, null);
                }
            }

            this.decals.clear();
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
        this.decals = null;
        this.backgrounds = null;
        this.enemies = null;
        this.obstacles = null;
        this.collectibles = null;
        this.doorDecals = null;
    }
}