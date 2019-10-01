package com.gamesbykevin.havoc.level;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.decals.CameraGroupStrategy;
import com.badlogic.gdx.graphics.g3d.decals.DecalBatch;
import com.badlogic.gdx.math.Vector3;
import com.gamesbykevin.havoc.decals.DecalCustom;
import com.gamesbykevin.havoc.decals.Door;
import com.gamesbykevin.havoc.maze.Maze;
import com.gamesbykevin.havoc.maze.algorithm.*;

import java.util.ArrayList;
import java.util.List;

import static com.gamesbykevin.havoc.level.LevelHelper.*;
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

    //this is where we will contain our wall / floor / ceiling
    private List<DecalCustom> decals;

    //for collision detection
    private boolean[][] walls;

    //contains our doors
    private Door[][] doorDecals;

    //for collision detection
    private boolean[][] doors;

    //which doors are open
    private boolean[][] doorsOpen;

    public Level() {

        //set our start of the level
        getCamera3d().near = .1f;
        getCamera3d().far = RENDER_RANGE;
        getCamera3d().position.set((ROOM_SIZE / 2),(ROOM_SIZE / 2),0);
        getCamera3d().position.z = 0;
        getCamera3d().rotate(Vector3.X, 90);

        //create the batch
        getDecalBatch();
    }

    public Maze getMaze() {
        return this.maze;
    }

    private DecalBatch getDecalBatch() {

        if (this.decalBatch == null)
            this.decalBatch = new DecalBatch(new CameraGroupStrategy(getCamera3d()));

        return this.decalBatch;
    }

    public List<DecalCustom> getDecals() {

        if (this.decals == null)
            this.decals = new ArrayList<>();

        return this.decals;
    }

    public Door[][] getDoorDecals() {

        if (this.doorDecals == null)
            this.doorDecals = new Door[(getMaze().getRows() * ROOM_SIZE) + 1][(getMaze().getCols() * ROOM_SIZE) + 1];

        return doorDecals;
    }

    public boolean[][] getWalls() {
        return this.walls;
    }

    public boolean[][] getDoors() {
        return this.doors;
    }

    public boolean[][] getDoorsOpen() {
        return this.doorsOpen;
    }

    public PerspectiveCamera getCamera3d() {

        if (this.camera3d == null) {

            float w = Gdx.graphics.getWidth();
            float h = Gdx.graphics.getHeight();

            this.camera3d = new PerspectiveCamera(67, 1, h/w);
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

        System.out.println(getMaze().toString());

        //generate the maze
        getMaze().generate();

        //calculate cost
        calculateCost(getMaze());

        //locate the goal
        locateGoal(getMaze());

        //create the arrays for our collision detection
        this.walls = new boolean[(getMaze().getRows() * ROOM_SIZE) + 1][(getMaze().getCols() * ROOM_SIZE) + 1];
        this.doors = new boolean[(getMaze().getRows() * ROOM_SIZE) + 1][(getMaze().getCols() * ROOM_SIZE) + 1];
        this.doorsOpen = new boolean[(getMaze().getRows() * ROOM_SIZE) + 1][(getMaze().getCols() * ROOM_SIZE) + 1];

        //default to false
        for (int row = 0 ; row < getWalls().length; row++) {
            for (int col = 0; col < getWalls()[0].length; col++) {
                getWalls()[row][col] = false;
                getDoors()[row][col] = false;
                getDoorsOpen()[row][col] = false;
            }
        }

        //add the decals and boundaries for our maze
        createDecals(this);
    }

    private void drawDecals() {

        int locX = (int)getCamera3d().position.x;
        int locY = (int)getCamera3d().position.y;

        for (int i = 0; i < getDecals().size(); i++) {

            DecalCustom decal = getDecals().get(i);

            if (Math.abs(decal.getCol() - locX) > RENDER_RANGE || Math.abs(decal.getRow() - locY) > RENDER_RANGE)
                continue;

            if (decal.isBillboard())
                decal.getDecal().lookAt(getCamera3d().position, getCamera3d().up);

            getDecalBatch().add(decal.getDecal());
        }

        for (int col = 0; col < getDoorDecals()[0].length; col++) {
            for (int row = 0; row < getDoorDecals().length; row++) {

                DecalCustom decal = getDoorDecals()[row][col];

                if (decal == null)
                    continue;

                if (Math.abs(decal.getCol() - locX) > RENDER_RANGE || Math.abs(decal.getRow() - locY) > RENDER_RANGE)
                    continue;

                if (decal.isBillboard())
                    decal.getDecal().lookAt(getCamera3d().position, getCamera3d().up);

                getDecalBatch().add(decal.getDecal());
            }
        }

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

                //flag if the door is open
                getDoorsOpen()[row][col] = door.isOpen();
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