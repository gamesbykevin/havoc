package com.gamesbykevin.havoc.level;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.decals.CameraGroupStrategy;
import com.badlogic.gdx.graphics.g3d.decals.DecalBatch;
import com.badlogic.gdx.math.Vector3;
import com.gamesbykevin.havoc.decals.DecalCustom;
import com.gamesbykevin.havoc.maze.Maze;
import com.gamesbykevin.havoc.maze.algorithm.*;

import java.util.ArrayList;
import java.util.List;

import static com.gamesbykevin.havoc.level.LevelHelper.ROOM_SIZE;
import static com.gamesbykevin.havoc.level.LevelHelper.createDecals;
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
    private List<DecalCustom> wallDecals;

    //contains our doors
    private List<DecalCustom> doorDecals;

    //contains our backgrounds
    private List<DecalCustom> backgroundDecals;

    //for collision detection
    private DecalCustom.Type[][] bounds;

    public Level() {

        //set our start of the level
        getCamera3d().near = .1f;
        getCamera3d().far = 300f;
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

    public List<DecalCustom> getWallDecals() {

        if (this.wallDecals == null)
            this.wallDecals = new ArrayList<>();

        return this.wallDecals;
    }

    public List<DecalCustom> getBackgroundDecals() {

        if (this.backgroundDecals == null)
            this.backgroundDecals = new ArrayList<>();

        return backgroundDecals;
    }

    public List<DecalCustom> getDoorDecals() {

        if (this.doorDecals == null)
            this.doorDecals = new ArrayList<>();

        return doorDecals;
    }

    public DecalCustom.Type[][] getBounds() {
        return this.bounds;
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

        //create the array of bounds
        this.bounds = new DecalCustom.Type[(getMaze().getRows() * ROOM_SIZE) + 1][(getMaze().getCols() * ROOM_SIZE) + 1];

        //add the decals and boundaries for our maze
        createDecals(this);
    }

    private void drawDecals() {

        for (int i = 0; i < getWallDecals().size(); i++) {

            DecalCustom decal = getWallDecals().get(i);

            if (decal.isBillboard())
                decal.getDecal().lookAt(getCamera3d().position, getCamera3d().up);

            getDecalBatch().add(decal.getDecal());
        }

        for (int i = 0; i < getDoorDecals().size(); i++) {

            DecalCustom decal = getDoorDecals().get(i);

            if (decal.isBillboard())
                decal.getDecal().lookAt(getCamera3d().position, getCamera3d().up);

            getDecalBatch().add(decal.getDecal());
        }

        for (int i = 0; i < getBackgroundDecals().size(); i++) {

            DecalCustom decal = getBackgroundDecals().get(i);

            if (decal.isBillboard())
                decal.getDecal().lookAt(getCamera3d().position, getCamera3d().up);

            getDecalBatch().add(decal.getDecal());
        }

        getDecalBatch().flush();
    }

    private void updateDecals() {

        for (int i = 0; i < getWallDecals().size(); i++) {
            getWallDecals().get(i).update();
        }

        for (int i = 0; i < getDoorDecals().size(); i++) {
            getDoorDecals().get(i).update();
        }

        for (int i = 0; i < getBackgroundDecals().size(); i++) {
            getBackgroundDecals().get(i).update();
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