package com.gamesbykevin.havoc.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.gamesbykevin.havoc.level.Level;
import com.gamesbykevin.havoc.maze.Room;

import static com.gamesbykevin.havoc.MyGdxGame.SIZE_HEIGHT;
import static com.gamesbykevin.havoc.MyGdxGame.SIZE_WIDTH;
import static com.gamesbykevin.havoc.level.Level.ROOM_SIZE;

public class MyController implements InputProcessor {

    public static final int BUTTON_LEFT_X = 20;
    public static final int BUTTON_LEFT_Y = 20;
    public static final int BUTTON_LEFT_W = 76;
    public static final int BUTTON_LEFT_H = 61;

    public static final int BUTTON_RIGHT_X = 120;
    public static final int BUTTON_RIGHT_Y = BUTTON_LEFT_Y;
    public static final int BUTTON_RIGHT_W = 75;
    public static final int BUTTON_RIGHT_H = 61;

    public static final int BUTTON_UP_X = 700;
    public static final int BUTTON_UP_Y = 120;
    public static final int BUTTON_UP_W = 61;
    public static final int BUTTON_UP_H = 76;

    public static final int BUTTON_DOWN_X = BUTTON_UP_X;
    public static final int BUTTON_DOWN_Y = 20;
    public static final int BUTTON_DOWN_W = 61;
    public static final int BUTTON_DOWN_H = 75;

    public static final float SPEED_WALK = .1f;
    public static final float SPEED_RUN = .25f;

    //how fast can the player move
    private float speed = SPEED_WALK;

    //how fast can the player turn by default
    public static final float DEFAULT_SPEED_ROTATE = 3f;

    //track how fast the player can turn
    private float speedRotate = DEFAULT_SPEED_ROTATE;

    //our rotation and rotation angle
    private float rotation = 0;

    //track our previous position
    private Vector3 previousPosition;

    //our 3d camera reference containing our game location
    private final Level level;

    //our input listener
    private Stage stage;

    //move camera up/down while we are walking/running
    private static final float MIN_Z = 0f;
    private static final float MAX_Z = .25f;
    private static float VELOCITY_Z = 0.01f;

    //different control inputs
    public static final int KEY_MOVE_FORWARD = Input.Keys.W;
    public static final int KEY_MOVE_BACKWARD = Input.Keys.S;
    public static final int KEY_MOVE_RUNNING = Input.Keys.SHIFT_LEFT;
    public static final int KEY_STRAFE_LEFT = Input.Keys.A;
    public static final int KEY_STRAFE_RIGHT = Input.Keys.D;
    public static final int KEY_TURN_LEFT = Input.Keys.R;
    public static final int KEY_TURN_RIGHT = Input.Keys.T;

    //what are we doing?
    private boolean moveForward = false;
    private boolean moveBackward = false;
    private boolean strafeLeft = false;
    private boolean strafeRight = false;
    private boolean turnRight = false;
    private boolean turnLeft = false;
    private boolean running = false;

    //images for our controls
    private Sprite up, down, left, right;

    //used to render a number of sprites
    private SpriteBatch spriteBatch;

    //camera to render controls
    private OrthographicCamera camera2d;

    public MyController(Level level) {
        this.level = level;
        this.previousPosition = new Vector3();

        addButton(BUTTON_RIGHT_X, BUTTON_RIGHT_Y, BUTTON_RIGHT_W, BUTTON_RIGHT_H, KEY_TURN_RIGHT, KEY_TURN_LEFT);
        addButton(BUTTON_LEFT_X, BUTTON_LEFT_Y, BUTTON_LEFT_W, BUTTON_LEFT_H, KEY_TURN_LEFT, KEY_TURN_RIGHT);
        addButton(BUTTON_UP_X, BUTTON_UP_Y, BUTTON_UP_W, BUTTON_UP_H, KEY_MOVE_FORWARD, KEY_MOVE_BACKWARD);
        addButton(BUTTON_DOWN_X, BUTTON_DOWN_Y, BUTTON_DOWN_W, BUTTON_DOWN_H, KEY_MOVE_BACKWARD, KEY_MOVE_FORWARD);

        //make sure we are capturing input correct
        setInput();

        //how fast can we turn?
        setSpeedRotate(DEFAULT_SPEED_ROTATE);

        //our texture images
        Texture tmpUp = new Texture(Gdx.files.internal("controls/up.png"));
        Texture tmpDown = new Texture(Gdx.files.internal("controls/down.png"));
        Texture tmpLeft = new Texture(Gdx.files.internal("controls/left.png"));
        Texture tmpRight = new Texture(Gdx.files.internal("controls/right.png"));

        //create our sprites
        this.up = new Sprite(tmpUp, 0, 0, tmpUp.getWidth(), tmpUp.getHeight());
        this.down = new Sprite(tmpDown, 0, 0, tmpDown.getWidth(), tmpDown.getHeight());
        this.left = new Sprite(tmpLeft, 0, 0, tmpLeft.getWidth(), tmpLeft.getHeight());
        this.right = new Sprite(tmpRight, 0, 0, tmpRight.getWidth(), tmpRight.getHeight());

        //create our camera
        getCamera2d();
    }

    public Level getLevel() {
        return this.level;
    }

    public SpriteBatch getSpriteBatch() {

        if (this.spriteBatch == null)
            this.spriteBatch = new SpriteBatch();

        return this.spriteBatch;
    }

    public OrthographicCamera getCamera2d() {

        //create camera if null
        if (this.camera2d == null) {
            this.camera2d = new OrthographicCamera();
            this.camera2d.setToOrtho(false, SIZE_WIDTH, SIZE_HEIGHT);
        }

        return this.camera2d;
    }

    public void setInput() {

        Gdx.input.setInputProcessor(getStage());

        /*
        switch (Gdx.app.getType()) {
            case Android:
            case iOS:
                Gdx.input.setInputProcessor(this.stage);
                break;

            default:
                Gdx.input.setInputProcessor(this);
                break;
        }
        */
    }

    private void addButton(int x, int y, int w, int h, int keyEnable, int keyDisable) {

        Image img = new Image();
        img.setPosition(x, y);
        img.setSize(w, h);
        img.setBounds(x, y, w, h);
        img.addListener(new InputListener() {

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                updateFlag(keyEnable, true);
                updateFlag(keyDisable, false);
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                updateFlag(keyEnable, false);
                updateFlag(keyDisable, false);
                super.touchUp(event, x, y, pointer, button);
            }

            @Override
            public void touchDragged(InputEvent event, float x, float y, int pointer) {
                updateFlag(keyEnable, true);
                updateFlag(keyDisable, false);
                super.touchDragged(event, x, y, pointer);
            }
        });

        //add to the stage
        getStage().addActor(img);
    }

    public void update() {

        int xMove = 0;
        int yMove = 0;

        if (isRunning()) {
            setSpeed(SPEED_RUN);
        } else {
            setSpeed(SPEED_WALK);
        }

        if (isMoveForward())
            yMove++;
        if (isMoveBackward())
            yMove--;

        if (isStrafeLeft())
            xMove--;
        if (isStrafeRight())
            xMove++;

        float rotationA = 0;

        if (isTurnLeft()) {
            rotationA = getSpeedRotate();
        } else if (isTurnRight()) {
            rotationA = -getSpeedRotate();
        }

        double angle = Math.toRadians(getRotation());

        //calculate distance moved
        double xa = (xMove * Math.cos(angle)) - (yMove * Math.sin(angle));
        xa *= getSpeed();

        double ya = (yMove * Math.cos(angle)) + (xMove * Math.sin(angle));
        ya *= getSpeed();

        //store previous position
        if (xMove != 0 || yMove != 0) {
            getPreviousPosition().x = getCamera3d().position.x;
            getPreviousPosition().y = getCamera3d().position.y;
            getPreviousPosition().z = getCamera3d().position.z;
        }

        //update camera location?
        getCamera3d().position.x += xa;
        getCamera3d().position.y += ya;
        getCamera3d().rotate(Vector3.Z, rotationA);

        if (isMoveForward() || isMoveBackward()) {

            getCamera3d().position.z += VELOCITY_Z;

            if (getCamera3d().position.z <= MIN_Z) {
                getCamera3d().position.z = MIN_Z;
                VELOCITY_Z = -VELOCITY_Z;
            } else if (getCamera3d().position.z >= MAX_Z) {
                getCamera3d().position.z = MAX_Z;
                VELOCITY_Z = -VELOCITY_Z;
            }
        }

        if (rotationA != 0) {

            //add rotation angle to overall rotation
            setRotation(getRotation() + rotationA);

            //keep radian value from getting to large/small
            if (getRotation() > 360)
                setRotation(0);
            if (getRotation() < 0)
                setRotation(360);
        }

        if (checkCollision()) {
            getCamera3d().position.x = getPreviousPosition().x;
            getCamera3d().position.y = getPreviousPosition().y;
            getCamera3d().position.z = getPreviousPosition().z;
        }
    }

    private boolean checkCollision() {

        final float x = getCamera3d().position.x;
        final float y = getCamera3d().position.y;

        //figure out which room we are in
        int col = (int)(x / ROOM_SIZE);
        int row = (int)(y / ROOM_SIZE);

        int roomCol = col * ROOM_SIZE;
        int roomRow = row * ROOM_SIZE;

        //get the current room
        Room room = getLevel().getMaze().getRoom(col, row);

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

    public float getRotation() {
        return this.rotation;
    }

    public void setRotation(float rotation) {
        this.rotation = rotation;
    }

    public float getSpeedRotate() {
        return this.speedRotate;
    }

    public void setSpeedRotate(float speedRotate) {
        this.speedRotate = speedRotate;
    }

    public float getSpeed() {
        return this.speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    @Override
    public boolean keyDown (int keycode) {
        updateFlag(keycode, true);
        return false;
    }

    @Override
    public boolean keyUp (int keycode) {
        updateFlag(keycode, false);
        return false;
    }

    @Override
    public boolean keyTyped (char character) {
        return false;
    }

    @Override
    public boolean touchDown (int x, int y, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp (int x, int y, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged (int x, int y, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved (int x, int y) {
        return false;
    }

    @Override
    public boolean scrolled (int amount) {
        return false;
    }

    private void updateFlag(int keycode, boolean flag) {

        switch (keycode) {
            case KEY_MOVE_BACKWARD:
                setMoveBackward(flag);
                break;

            case KEY_MOVE_FORWARD:
                setMoveForward(flag);
                break;

            case KEY_MOVE_RUNNING:
                setRunning(flag);
                break;

            case KEY_STRAFE_LEFT:
                setStrafeLeft(flag);
                break;

            case KEY_STRAFE_RIGHT:
                setStrafeRight(flag);
                break;

            case KEY_TURN_LEFT:
                setTurnLeft(flag);
                break;

            case KEY_TURN_RIGHT:
                setTurnRight(flag);
                break;
        }
    }

    public PerspectiveCamera getCamera3d() {
        return this.level.getCamera3d();
    }

    public Vector3 getPreviousPosition() {
        return this.previousPosition;
    }

    public boolean isMoveForward() {
        return this.moveForward;
    }

    public void setMoveForward(boolean moveForward) {
        this.moveForward = moveForward;
    }

    public boolean isMoveBackward() {
        return this.moveBackward;
    }

    public void setMoveBackward(boolean moveBackward) {
        this.moveBackward = moveBackward;
    }

    public boolean isStrafeLeft() {
        return this.strafeLeft;
    }

    public void setStrafeLeft(boolean strafeLeft) {
        this.strafeLeft = strafeLeft;
    }

    public boolean isStrafeRight() {
        return this.strafeRight;
    }

    public void setStrafeRight(boolean strafeRight) {
        this.strafeRight = strafeRight;
    }

    public boolean isRunning() {
        return this.running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    public boolean isTurnRight() {
        return this.turnRight;
    }

    public void setTurnRight(boolean turnRight) {
        this.turnRight = turnRight;
    }

    public boolean isTurnLeft() {
        return this.turnLeft;
    }

    public void setTurnLeft(boolean turnLeft) {
        this.turnLeft = turnLeft;
    }

    public Stage getStage() {

        if (this.stage == null)
            this.stage = new Stage(new StretchViewport(SIZE_WIDTH, SIZE_HEIGHT));

        return stage;
    }

    public void render() {

        //good practice to update the camera at least once per frame
        getCamera2d().update();

        //set the screen projection coordinates
        getSpriteBatch().setProjectionMatrix(getCamera2d().combined);

        getSpriteBatch().begin();
        getSpriteBatch().draw(left, BUTTON_LEFT_X, BUTTON_LEFT_Y, BUTTON_LEFT_W, BUTTON_LEFT_H);
        getSpriteBatch().draw(right, BUTTON_RIGHT_X, BUTTON_RIGHT_Y, BUTTON_RIGHT_W, BUTTON_RIGHT_H);
        getSpriteBatch().draw(up, BUTTON_UP_X, BUTTON_UP_Y, BUTTON_UP_W, BUTTON_UP_H);
        getSpriteBatch().draw(down, BUTTON_DOWN_X, BUTTON_DOWN_Y, BUTTON_DOWN_W, BUTTON_DOWN_H);
        getSpriteBatch().end();
    }
}