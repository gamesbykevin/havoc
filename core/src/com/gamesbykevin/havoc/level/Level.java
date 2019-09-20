package com.gamesbykevin.havoc.level;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.decals.CameraGroupStrategy;
import com.badlogic.gdx.graphics.g3d.decals.Decal;
import com.badlogic.gdx.graphics.g3d.decals.DecalBatch;
import com.badlogic.gdx.math.Vector3;
import com.gamesbykevin.havoc.maze.Maze;
import com.gamesbykevin.havoc.maze.Room;
import com.gamesbykevin.havoc.maze.algorithm.*;

import java.util.ArrayList;

import static com.gamesbykevin.havoc.maze.MazeHelper.calculateCost;
import static com.gamesbykevin.havoc.maze.MazeHelper.locateGoal;

public class Level {

    //how big is each room
    public static final int ROOM_SIZE = 5;

    //how big is our maze
    public static final int MAZE_COLS = 5;
    public static final int MAZE_ROWS = 5;

    //our randomly created maze
    private Maze maze;

    //our 3d camera
    private PerspectiveCamera camera3d;

    //needed to render multiple decals
    private DecalBatch decalBatch;

    //this is where we will contain our walls
    private ArrayList<Decal> decals;

    public Level() {

        //set our start of the level
        getCamera3d().near = .1f;
        getCamera3d().far = 300f;
        getCamera3d().position.set(2,2,0);
        getCamera3d().position.z = 0;
        getCamera3d().rotate(Vector3.X, 90);

        //create the batch
        getDecalBatch();
    }

    public Maze getMaze() {
        return this.maze;
    }

    public DecalBatch getDecalBatch() {

        if (this.decalBatch == null)
            this.decalBatch = new DecalBatch(new CameraGroupStrategy(getCamera3d()));

        return this.decalBatch;
    }

    public ArrayList<Decal> getDecals() {

        if (this.decals == null)
            this.decals = new ArrayList<>();

        return this.decals;
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

        //create our decals
        createDecals();
    }

    private void createDecals() {

        //remove any existing decals
        getDecals().clear();

        TextureRegion[] textures = {new TextureRegion(new Texture(Gdx.files.internal("egg.png"))),
                new TextureRegion(new Texture(Gdx.files.internal("wheel.png"))),
                new TextureRegion(new Texture(Gdx.files.internal("badlogic.jpg")))};

        int w = 1;
        int h = 1;

        for (int col = 0; col < getMaze().getCols(); col++) {
            for (int row = 0; row < getMaze().getRows(); row++) {

                //if (col != 0 || row != 0)
                //   continue;

                Room room = getMaze().getRoom(col, row);

                Room east = getMaze().getRoom(col + 1, row);
                Room north = getMaze().getRoom(col, row + 1);

                int index = 1;

                if (col == getMaze().getStartCol() && row == getMaze().getStartRow())
                    index = 0;
                if (col == getMaze().getGoalCol() && row == getMaze().getGoalRow())
                    index = 2;

                int roomColStart = ROOM_SIZE * col;
                int roomRowStart = ROOM_SIZE * row;

                for (int roomRow = roomRowStart; roomRow < roomRowStart + ROOM_SIZE; roomRow++) {

                    //west wall
                    if (room.hasWest())
                        addBox(w,h, roomColStart, roomRow, textures[index]);

                    //east wall
                    if (room.hasEast() && (east == null || !east.hasWest()))
                        addBox(w,h, roomColStart + (ROOM_SIZE-1), roomRow, textures[index]);
                }

                for (int roomCol = roomColStart; roomCol < roomColStart + ROOM_SIZE; roomCol++) {

                    //north wall
                    if (room.hasNorth() && (north == null || !north.hasSouth()))
                        addBox(w,h, roomCol, roomRowStart + (ROOM_SIZE-1), textures[index]);

                    //south wall
                    if (room.hasSouth())
                        addBox(w,h, roomCol, roomRowStart, textures[index]);
                }
            }
        }
    }

    private void addBox(int w, int h, int col, int row, TextureRegion texture) {

        Decal west = Decal.newDecal(w, h, texture);
        west.setPosition(col - ((float) w / 2), row, 0);
        west.rotateY(90);
        getDecals().add(west);

        Decal e = Decal.newDecal(w, h, texture);
        e.setPosition(col + ((float) w / 2), row, 0);
        e.rotateY(270);
        getDecals().add(e);

        Decal n = Decal.newDecal(w, h, texture);
        n.setPosition(col, row + ((float) h / 2), 0);
        n.rotateX(90);
        getDecals().add(n);

        Decal s = Decal.newDecal(w, h, texture);
        s.setPosition(col, row - ((float) h / 2), 0);
        s.rotateX(270);
        getDecals().add(s);
    }

    private void drawDecals() {

        //billboard will have the decal always facing the camera
        boolean billboard = false;

        for (int i = 0; i < getDecals().size(); i++) {
            Decal decal = getDecals().get(i);

            if (billboard) {
                // billboarding for ortho cam :)
                // dir.set(-camera.direction.x, -camera.direction.y, -camera.direction.z);
                // decal.setRotation(dir, Vector3.Y);

                // billboarding for perspective cam
                decal.lookAt(getCamera3d().position, getCamera3d().up);
            }

            getDecalBatch().add(decal);
        }

        getDecalBatch().flush();
    }

    public void render() {

        //update the camera
        getCamera3d().update();

        //draw our 3d walls
        drawDecals();
    }
}