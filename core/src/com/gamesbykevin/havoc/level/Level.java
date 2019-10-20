package com.gamesbykevin.havoc.level;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.decals.CameraGroupStrategy;
import com.badlogic.gdx.graphics.g3d.decals.DecalBatch;
import com.badlogic.gdx.math.Vector3;
import com.gamesbykevin.havoc.collectables.Collectibles;
import com.gamesbykevin.havoc.decals.DecalCustom;
import com.gamesbykevin.havoc.decals.Door;
import com.gamesbykevin.havoc.enemies.Enemies;
import com.gamesbykevin.havoc.entities.Entities;
import com.gamesbykevin.havoc.maze.Maze;
import com.gamesbykevin.havoc.maze.Room;
import com.gamesbykevin.havoc.maze.algorithm.*;
import com.gamesbykevin.havoc.obstacles.Obstacles;

import java.util.ArrayList;
import java.util.List;

import static com.gamesbykevin.havoc.level.LevelHelper.*;
import static com.gamesbykevin.havoc.level.RoomHelper.ROOM_SIZE;
import static com.gamesbykevin.havoc.maze.MazeHelper.calculateCost;
import static com.gamesbykevin.havoc.maze.MazeHelper.locateGoal;

public class Level {

    //how big is our maze
    public static final int MAZE_COLS = 3;
    public static final int MAZE_ROWS = 3;

    //our randomly created maze
    private Maze maze;

    //our 3d camera
    private PerspectiveCamera camera3d;

    //needed to render multiple decals
    private DecalBatch decalBatch;

    //this is where we will contain our walls
    private List<DecalCustom> decals;

    //contain the floor / ceiling
    private List<DecalCustom> backgrounds;

    //for collision detection
    private boolean[][] walls;
    private boolean[][] doors;
    private boolean[][] free;

    //contains our doors
    private Door[][] doorDecals;

    //our enemies are contained here
    private Entities enemies;

    //obstacles in the level
    private Entities obstacles;

    //items for pickup
    private Entities collectibles;

    //the start of the maze
    public static final int START_COL = 0;
    public static final int START_ROW = 0;

    public Level() {

        //create our enemies container
        getEnemies();

        //set the start location
        resetPosition();

        //create the batch
        getDecalBatch();
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

    public Maze getMaze() {
        return this.maze;
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
            this.doorDecals = new Door[(getMaze().getRows() * ROOM_SIZE) + 1][(getMaze().getCols() * ROOM_SIZE) + 1];

        return doorDecals;
    }

    public void setWall(int col, int row, boolean value) {
        getWalls()[row][col] = value;
    }

    public boolean hasWall(int col, int row) {
        return hasValue(getWalls(), col, row);
    }

    private boolean[][] getWalls() {
        return this.walls;
    }

    public void setFree(int col, int row, boolean value) {

        if (row < 0 || row >= getFree().length)
            return;
        if (col < 0 || col >= getFree()[0].length)
            return;

        getFree()[row][col] = value;
    }

    public boolean hasFree(int col, int row) {
        return hasValue(getFree(), col, row);
    }

    private boolean[][] getFree() {
        return this.free;
    }

    public void setDoor(int col, int row, boolean value) {
        getDoors()[row][col] = value;
    }

    public boolean hasDoor(int col, int row) {
        return hasValue(getDoors(), col, row);
    }

    private boolean[][] getDoors() {
        return this.doors;
    }

    private boolean hasValue(boolean[][] array, int col, int row) {

        if (row < 0 || row >= array.length)
            return false;
        if (col < 0 || col >= array[0].length)
            return false;

        return array[row][col];
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
            this.camera3d.position.set((ROOM_SIZE / 2) + .5f, (ROOM_SIZE / 2) + .5f,0);
            this.camera3d.position.z = 1.50f;
            this.camera3d.rotate(Vector3.X, 90);
        }

        return this.camera3d;
    }

    /**
     * create a new maze
     */
    public void generateMaze() {

        switch ((int)(Math.random() * 7)) {

            case 0:
                this.maze = new BinaryTree(MAZE_COLS, MAZE_ROWS);
                break;

            case 1:
                this.maze = new Ellers(MAZE_COLS, MAZE_ROWS);
                break;

            case 2:
                this.maze = new HuntKill(MAZE_COLS, MAZE_ROWS);
                break;

            case 3:
                this.maze = new Kruskal(MAZE_COLS, MAZE_ROWS);
                break;

            case 4:
                this.maze = new Recursive(MAZE_COLS, MAZE_ROWS);
                break;

            case 5:
                this.maze = new Sidewinder(MAZE_COLS, MAZE_ROWS);
                break;

            case 6:
                this.maze = new AldousBroder(MAZE_COLS, MAZE_ROWS);
                break;
        }

        //generate the maze
        getMaze().generate();

        //set the starting point
        getMaze().setStartCol(START_COL);
        getMaze().setStartRow(START_ROW);

        //calculate cost
        calculateCost(getMaze());

        //locate the goal
        locateGoal(getMaze());

        //create the arrays for our collision detection etc...
        this.walls = new boolean[(getMaze().getRows() * ROOM_SIZE) + 1][(getMaze().getCols() * ROOM_SIZE) + 1];
        this.doors = new boolean[(getMaze().getRows() * ROOM_SIZE) + 1][(getMaze().getCols() * ROOM_SIZE) + 1];
        this.free = new boolean[(getMaze().getRows() * ROOM_SIZE) + 1][(getMaze().getCols() * ROOM_SIZE) + 1];

        //default to false
        for (int row = 0 ; row < getWalls().length; row++) {
            for (int col = 0; col < getWalls()[0].length; col++) {
                getWalls()[row][col] = false;
                getDoors()[row][col] = false;
                getFree()[row][col] = false;
            }
        }

        //add the decals and boundaries for our maze
        createDecals(this);
    }

    private boolean hasRange(DecalCustom decal, float minCol, float maxCol, float minRow, float maxRow, float minColRoom, float maxColRoom, float minRowRoom, float maxRowRoom) {

        //nw & sw
        if (decal.getCol() < minColRoom && (decal.getRow() > maxRowRoom || decal.getRow() < minRowRoom))
            return false;

        //ne & se
        if (decal.getCol() > maxColRoom && (decal.getRow() > maxRowRoom || decal.getRow() < minRowRoom))
            return false;

        //west & east
        if (decal.getCol() < minCol || decal.getCol() > maxCol)
            return false;

        //north & south
        if (decal.getRow() < minRow || decal.getRow() > maxRow)
            return false;

        return true;
    }

    private void drawDecals() {

        //figure out which room we are in
        int roomCol = (int)(getCamera3d().position.x / ROOM_SIZE);
        int roomRow = (int)(getCamera3d().position.y / ROOM_SIZE);

        //get the current room
        Room room = getMaze().getRoom(roomCol, roomRow);

        //get the bounds of the current room
        float minColRoom = roomCol * ROOM_SIZE;
        float maxColRoom = minColRoom + ROOM_SIZE;
        float minRowRoom = roomRow * ROOM_SIZE;
        float maxRowRoom = minRowRoom + ROOM_SIZE;

        //adjust the render range
        float minCol = (room.hasWest()) ? roomCol * ROOM_SIZE : (getCamera3d().position.x - (RENDER_RANGE / 2));
        float maxCol = (room.hasEast()) ? (roomCol * ROOM_SIZE) + ROOM_SIZE : (getCamera3d().position.x + (RENDER_RANGE / 2));
        float minRow = (room.hasSouth()) ? (roomRow * ROOM_SIZE) : (getCamera3d().position.y - (RENDER_RANGE / 2));
        float maxRow = (room.hasNorth()) ? (roomRow * ROOM_SIZE) + ROOM_SIZE : (getCamera3d().position.y + (RENDER_RANGE / 2));

        int count = 0;

        for (int i = 0; i < getDecals().size(); i++) {

            DecalCustom decal = getDecals().get(i);

            //only render if in range
            if (!hasRange(decal, minCol, maxCol, minRow, maxRow, minColRoom, maxColRoom, minRowRoom, maxRowRoom))
                continue;

            if (decal.isBillboard())
                decal.getDecal().lookAt(getCamera3d().position, getCamera3d().up);

            count++;
            getDecalBatch().add(decal.getDecal());
        }

        //render the backgrounds
        for (int i = 0; i < getBackgrounds().size(); i++) {
            DecalCustom decal = getBackgrounds().get(i);

            if (decal.getCol() < getCamera3d().position.x - RENDER_RANGE || decal.getCol() > getCamera3d().position.x + RENDER_RANGE)
                continue;
            if (decal.getRow() < getCamera3d().position.y - RENDER_RANGE || decal.getRow() > getCamera3d().position.y + RENDER_RANGE)
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
                if (!hasRange(decal, minCol, maxCol, minRow, maxRow, minColRoom, maxColRoom, minRowRoom, maxRowRoom))
                    continue;

                if (decal.isBillboard())
                    decal.getDecal().lookAt(getCamera3d().position, getCamera3d().up);

                count++;
                getDecalBatch().add(decal.getDecal());
            }
        }

        System.out.println("decal count: " + count);

        //render the enemies
        getEnemies().render(getDecalBatch(), getCamera3d(), minCol, maxCol, minRow, maxRow);

        //render the obstacles
        getObstacles().render(getDecalBatch(), getCamera3d(), minCol, maxCol, minRow, maxRow);

        //render the collectibles
        getCollectibles().render(getDecalBatch(), getCamera3d(), minCol, maxCol, minRow, maxRow);

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