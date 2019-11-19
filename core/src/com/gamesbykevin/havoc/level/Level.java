package com.gamesbykevin.havoc.level;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.decals.CameraGroupStrategy;
import com.badlogic.gdx.graphics.g3d.decals.DecalBatch;
import com.badlogic.gdx.math.Vector3;
import com.gamesbykevin.havoc.collectables.Collectibles;
import com.gamesbykevin.havoc.decals.DecalCustom;
import com.gamesbykevin.havoc.decals.Door;
import com.gamesbykevin.havoc.dungeon.Dungeon;
import com.gamesbykevin.havoc.dungeon.Leaf;
import com.gamesbykevin.havoc.dungeon.Room;
import com.gamesbykevin.havoc.enemies.Enemies;
import com.gamesbykevin.havoc.entities.Entities;
import com.gamesbykevin.havoc.obstacles.Obstacles;

import java.util.ArrayList;
import java.util.List;

import static com.gamesbykevin.havoc.dungeon.DungeonHelper.DUNGEON_SIZE;
import static com.gamesbykevin.havoc.dungeon.LeafHelper.LEAF_DIMENSION_MIN;
import static com.gamesbykevin.havoc.texture.TextureHelper.addTextures;

public class Level {

    //our randomly created dungeon
    private Dungeon dungeon;

    //our 3d camera
    private PerspectiveCamera camera3d;

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

    //how far away can we render?
    public static final int RENDER_RANGE = LEAF_DIMENSION_MIN;

    public Level() {

        //create and generate the dungeon
        this.dungeon = new Dungeon(this, DUNGEON_SIZE, DUNGEON_SIZE);
        getDungeon().generate();

        //place some obstacles
        getObstacles().spawn();

        //update the map again based on the obstacles we just spawned
        getDungeon().updateMap();

        //and we add collectibles
        getCollectibles().spawn();

        //add enemies
        getEnemies().spawn();

        //add textures
        addTextures(this);

        //set the start location
        resetPosition();

        //create the batch
        getDecalBatch();
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

    public void resetPosition() {
        getCamera3d(true);
    }

    private DecalBatch getDecalBatch() {

        if (this.decalBatch == null)
            this.decalBatch = new DecalBatch(new CameraGroupStrategy(getCamera3d()));

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

    private Door[][] getDoorDecals() {

        if (this.doorDecals == null)
            this.doorDecals = new Door[getDungeon().getRows()][getDungeon().getCols()];

        return doorDecals;
    }

    public PerspectiveCamera getCamera3d() {
        return getCamera3d(false);
    }

    public PerspectiveCamera getCamera3d(boolean reset) {

        if (this.camera3d == null) {
            float w = Gdx.graphics.getWidth();
            float h = Gdx.graphics.getHeight();
            this.camera3d = new PerspectiveCamera(67, 1, h/w);
            this.camera3d.near = .05f;
            this.camera3d.far = RENDER_RANGE;
        }

        if (reset) {
            this.camera3d.direction.set(0, 0, -1);
            this.camera3d.up.set(0, 1, 0);
            this.camera3d.update();

            //start where we marked start in our maze
            float x = getDungeon().getStartCol();
            float y = getDungeon().getStartRow();

            //this.camera3d.position.set((ROOM_SIZE / 2) + .5f, (ROOM_SIZE / 2) + .5f,0);
            this.camera3d.position.set(x, y,0);
            this.camera3d.position.z = 1.50f;
            this.camera3d.rotate(Vector3.X, 90);
        }

        return this.camera3d;
    }

    private boolean hasRange(DecalCustom decal, float minCol, float maxCol, float minRow, float maxRow) {

        //west & east
        if (decal.getCol() < minCol || decal.getCol() > maxCol)
            return false;

        //north & south
        if (decal.getRow() < minRow || decal.getRow() > maxRow)
            return false;

        return true;
    }

    private void drawDecals() {

        //adjust the render range based on
        float minCol = (getCamera3d().position.x - RENDER_RANGE);
        float maxCol = (getCamera3d().position.x + RENDER_RANGE);
        float minRow = (getCamera3d().position.y - RENDER_RANGE);
        float maxRow = (getCamera3d().position.y + RENDER_RANGE);

        int count = 0;

        for (int i = 0; i < getDungeon().getLeafs().size(); i++) {

            Leaf leaf = getDungeon().getLeafs().get(i);

            //skip if no room exists
            if (leaf.getRoom() == null)
                continue;

            Room room = leaf.getRoom();

            if (!room.contains((int)getCamera3d().position.x, (int)getCamera3d().position.y))
                continue;

            if (!room.hasNorthDoor(getDungeon()))
                maxRow = room.getY() + room.getH();
            if (!room.hasSouthDoor(getDungeon()))
                minRow = room.getY();
            if (!room.hasWestDoor(getDungeon()))
                minCol = room.getX();
            if (!room.hasEastDoor(getDungeon()))
                maxCol = room.getX() + room.getW();
        }

        for (int i = 0; i < getDecals().size(); i++) {

            DecalCustom decal = getDecals().get(i);

            //only render if in range
            if (!hasRange(decal, minCol, maxCol, minRow, maxRow))
                continue;

            if (decal.isBillboard())
                decal.getDecal().lookAt(getCamera3d().position, getCamera3d().up);

            count++;
            getDecalBatch().add(decal.getDecal());
        }

        //render the backgrounds
        for (int i = 0; i < getBackgrounds().size(); i++) {
            DecalCustom decal = getBackgrounds().get(i);

            //only render if in range
            if (!hasRange(decal, minCol - RENDER_RANGE, maxCol + RENDER_RANGE, minRow - RENDER_RANGE, maxRow + RENDER_RANGE))
                continue;

            count++;
            getDecalBatch().add(decal.getDecal());
        }

        for (int col = 0; col < getDoorDecals()[0].length; col++) {
            for (int row = 0; row < getDoorDecals().length; row++) {

                DecalCustom decal = getDoorDecal(col, row);

                if (decal == null)
                    continue;

                //only render if in range
                if (!hasRange(decal, minCol, maxCol, minRow, maxRow))
                    continue;

                if (decal.isBillboard())
                    decal.getDecal().lookAt(getCamera3d().position, getCamera3d().up);

                count++;
                getDecalBatch().add(decal.getDecal());
            }
        }

        //render the enemies
        count += getEnemies().render(getDecalBatch(), getCamera3d(), minCol, maxCol, minRow, maxRow);

        //render the obstacles
        count += getObstacles().render(getDecalBatch(), getCamera3d(), minCol, maxCol, minRow, maxRow);

        //render the collectibles
        count += getCollectibles().render(getDecalBatch(), getCamera3d(), minCol, maxCol, minRow, maxRow);

        //System.out.println("decal count: " + count);

        //call flush at the end to draw
        getDecalBatch().flush();
    }

    private void updateDecals() {

        for (int col = 0; col < getDoorDecals()[0].length; col++) {
            for (int row = 0; row < getDoorDecals().length; row++) {

                Door door = getDoorDecals()[row][col];

                if (door == null)
                    continue;

                //update the door
                door.update();
            }
        }
    }

    public void render() {

        //update the camera
        getCamera3d().update();

        //update our decals
        updateDecals();

        //draw our 3d walls
        drawDecals();
    }
}