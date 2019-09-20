package com.gamesbykevin.havoc;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.decals.CameraGroupStrategy;
import com.badlogic.gdx.graphics.g3d.decals.Decal;
import com.badlogic.gdx.graphics.g3d.decals.DecalBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.gamesbykevin.havoc.input.MyController;
import com.gamesbykevin.havoc.maze.Maze;
import com.gamesbykevin.havoc.maze.Room;
import com.gamesbykevin.havoc.maze.algorithm.*;

import java.util.ArrayList;

import static com.gamesbykevin.havoc.input.MyController.*;
import static com.gamesbykevin.havoc.maze.MazeHelper.calculateCost;
import static com.gamesbykevin.havoc.maze.MazeHelper.locateGoal;

public class MyGdxGame extends ApplicationAdapter {

	public static final int SIZE_WIDTH = 800;
	public static final int SIZE_HEIGHT = 480;

	SpriteBatch spriteBatch;
//	private Texture up, down, left, right;
	private Sprite up, down, left, right;

	DecalBatch decalBatch;
	ArrayList<Decal> decals = new ArrayList<>();
	PerspectiveCamera camera3d;
	OrthographicCamera camera2d;
	private Viewport viewport;

	FPSLogger logger = new FPSLogger();

	private Maze maze;

	private static boolean generate = true;

	private MyController controller;

	public static final int ROOM_SIZE = 7;

	public static final int MAZE_COLS = 15;
	public static final int MAZE_ROWS = 15;

	public void create () {

		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();

		this.camera2d = new OrthographicCamera();
		this.camera2d.setToOrtho(false, SIZE_WIDTH, SIZE_HEIGHT);

		this.camera3d = new PerspectiveCamera(67, 1, h / w);
		this.camera3d.near = .1f;
		this.camera3d.far = 300f;
		this.camera3d.position.set(2,2,0);

		this.camera3d.position.z = 0;
		this.camera3d.rotate(Vector3.X, 90);

		//render 3d
		decalBatch = new DecalBatch(new CameraGroupStrategy(this.camera3d));

		//render 2d
		spriteBatch = new SpriteBatch();

		createMaze();

		controller = new MyController(camera3d);
	}

	private void createDecals() {

		//remove any existing decals
		decals.clear();

		TextureRegion[] textures = {new TextureRegion(new Texture(Gdx.files.internal("egg.png"))),
				new TextureRegion(new Texture(Gdx.files.internal("wheel.png"))),
				new TextureRegion(new Texture(Gdx.files.internal("badlogic.jpg")))};

		Texture tmpUp = new Texture(Gdx.files.internal("controls/up.png"));
		Texture tmpDown = new Texture(Gdx.files.internal("controls/down.png"));
		Texture tmpLeft = new Texture(Gdx.files.internal("controls/left.png"));
		Texture tmpRight = new Texture(Gdx.files.internal("controls/right.png"));

		this.up = new Sprite(tmpUp, 0, 0, tmpUp.getWidth(), tmpUp.getHeight());
		this.down = new Sprite(tmpDown, 0, 0, tmpDown.getWidth(), tmpDown.getHeight());
		this.left = new Sprite(tmpLeft, 0, 0, tmpLeft.getWidth(), tmpLeft.getHeight());
		this.right = new Sprite(tmpRight, 0, 0, tmpRight.getWidth(), tmpRight.getHeight());

		int w = 1;
		int h = 1;

		for (int col = 0; col < maze.getCols(); col++) {
			for (int row = 0; row < maze.getRows(); row++) {

				//if (col != 0 || row != 0)
				//   continue;

				Room room = maze.getRoom(col, row);

				Room east = maze.getRoom(col + 1, row);
				Room north = maze.getRoom(col, row + 1);

				int index = 1;

				if (col == maze.getStartCol() && row == maze.getStartRow())
					index = 0;
				if (col == maze.getGoalCol() && row == maze.getGoalRow())
					index = 2;

				/*
				System.out.println("W - " + room.hasWest());
				System.out.println("E - " + room.hasEast());
				System.out.println("N - " + room.hasNorth());
				System.out.println("S - " + room.hasSouth());
				*/


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
		decals.add(west);

		Decal e = Decal.newDecal(w, h, texture);
		e.setPosition(col + ((float) w / 2), row, 0);
		e.rotateY(270);
		decals.add(e);

		Decal n = Decal.newDecal(w, h, texture);
		n.setPosition(col, row + ((float) h / 2), 0);
		n.rotateX(90);
		decals.add(n);

		Decal s = Decal.newDecal(w, h, texture);
		s.setPosition(col, row - ((float) h / 2), 0);
		s.rotateX(270);
		decals.add(s);
	}

	private void createMaze() {

		if (generate) {

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

			System.out.println(this.maze.toString());

			//generate the maze
			this.maze.generate();

			//calculate cost
			calculateCost(maze);

			//locate the goal
			locateGoal(maze);

			generate = false;

			createDecals();
		}
	}

	public void render () {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);

		//camera.up.set(0, 1, 0);
		//camera.direction.set(0, 0, -3);
		//camera.rotate(30f, 0, 0, 3);

        controller.update();

        if (checkCollision()) {
			camera3d.position.x = controller.getPreviousPosition().x;
			camera3d.position.y = controller.getPreviousPosition().y;
			camera3d.position.z = controller.getPreviousPosition().z;
		}

		//camera.rotate(Vector3.Z, 1);// for pitch;
		//camera.position.z = 0;
		//camera.rotate(Vector3.X, 90);
		//camera.rotate(camera.direction.crs(Vector3.Y), 1f);// for yaw

		camera3d.update();

		createMaze();

		drawDecals();

		drawControls();

		//drawMaze();
	}

	private boolean checkCollision() {

		final float x = controller.getCamera3d().position.x;
		final float y = controller.getCamera3d().position.y;

		//figure out which room we are in
		int col = (int)(x / ROOM_SIZE);
		int row = (int)(y / ROOM_SIZE);

		int roomCol = col * ROOM_SIZE;
		int roomRow = row * ROOM_SIZE;

		//get the current room
		Room room = maze.getRoom(col, row);

		if (room == null)
			return true;

		if (room.hasWest() && x < roomCol + 1)
			return true;
		if (room.hasEast() && x > roomCol + ROOM_SIZE - 1)
			return true;
		if (room.hasNorth() && y > roomRow + ROOM_SIZE - 1)
			return true;
		if (room.hasSouth() && y < roomRow + 1)
			return true;

		return false;
	}

	@Override
	public void dispose () {
		decalBatch.dispose();
	}

	private void drawMaze() {

		int w = 1;
		int h = 1;
		ShapeRenderer debugRenderer = new ShapeRenderer();
		Gdx.gl.glLineWidth(2);
		debugRenderer.setProjectionMatrix(camera2d.combined);
		debugRenderer.begin(ShapeRenderer.ShapeType.Line);

		int xoffset = -5;
		int yoffset = -7;

		for (int col = 0; col < maze.getCols(); col++) {
			for (int row = 0; row < maze.getRows(); row++) {
				Room room = maze.getRoom(col, row);

				if (col == maze.getGoalCol() && row == maze.getGoalRow()) {
					debugRenderer.setColor(Color.GREEN);
				} else if (col == maze.getStartCol() && row == maze.getStartRow()) {
					debugRenderer.setColor(Color.BLUE);
				} else {
					debugRenderer.setColor(Color.RED);
				}

				int x = (room.getCol() * w) + xoffset;
				int y = (room.getRow() * h) + yoffset;

				if (room.hasWest())
					debugRenderer.line(x, y, x, y + h);
				if (room.hasEast())
					debugRenderer.line(x + w, y, x + w, y + h);
				if (room.hasNorth())
					debugRenderer.line(x, y + h, x + w, y + h);
				if (room.hasSouth())
					debugRenderer.line(x, y, x + w, y);
			}
		}

		debugRenderer.end();
	}

	private void drawDecals() {

		//billboard will have the decal always facing the camera
		boolean billboard = false;

		for (int i = 0; i < decals.size(); i++) {
			Decal decal = decals.get(i);

			if (billboard) {
				// billboarding for ortho cam :)
				// dir.set(-camera.direction.x, -camera.direction.y, -camera.direction.z);
				// decal.setRotation(dir, Vector3.Y);

				// billboarding for perspective cam
				decal.lookAt(camera3d.position, camera3d.up);
			}
			decalBatch.add(decal);
		}
		decalBatch.flush();
		//logger.log();
	}

	private void drawControls() {

		//good practice to update the camera at least once per frame
		this.camera2d.update();

		//set the screen projection coordinates
		spriteBatch.setProjectionMatrix(this.camera2d.combined);

		spriteBatch.begin();
		spriteBatch.draw(left, BUTTON_LEFT_X, BUTTON_LEFT_Y, BUTTON_LEFT_W, BUTTON_LEFT_H);
		spriteBatch.draw(right, BUTTON_RIGHT_X, BUTTON_RIGHT_Y, BUTTON_RIGHT_W, BUTTON_RIGHT_H);
		spriteBatch.draw(up, BUTTON_UP_X, BUTTON_UP_Y, BUTTON_UP_W, BUTTON_UP_H);
		spriteBatch.draw(down, BUTTON_DOWN_X, BUTTON_DOWN_Y, BUTTON_DOWN_W, BUTTON_DOWN_H);
		spriteBatch.end();
	}
}