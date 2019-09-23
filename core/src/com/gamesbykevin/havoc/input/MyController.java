package com.gamesbykevin.havoc.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.gamesbykevin.havoc.level.Level;
import com.gamesbykevin.havoc.maze.Room;

import static com.gamesbykevin.havoc.MyGdxGame.SIZE_HEIGHT;
import static com.gamesbykevin.havoc.MyGdxGame.SIZE_WIDTH;
import static com.gamesbykevin.havoc.level.Level.ROOM_SIZE;

public class MyController implements InputProcessor {

    //space between the buttons
    public static final int PADDING = 5;

    public static final float MAX_COORDINATE = 110f;
    public static final float MIN_COORDINATE = 48f;

    //how fast can we move
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
    public static final int KEY_TURN_LEFT = Input.Keys.LEFT;
    public static final int KEY_TURN_RIGHT = Input.Keys.RIGHT;
    public static final int KEY_SHOOT = Input.Keys.ENTER;
    public static final int KEY_ACTION = Input.Keys.SPACE;
    public static final int KEY_CHANGE = Input.Keys.NUM_1;

    //what are we doing?
    private boolean moveForward = false;
    private boolean moveBackward = false;
    private boolean strafeLeft = false;
    private boolean strafeRight = false;
    private boolean turnRight = false;
    private boolean turnLeft = false;
    private boolean running = false;
    private boolean shooting = false;
    private boolean action = false;
    private boolean change = false;

    //camera to render controls
    private OrthographicCamera camera2d;

    public MyController(Level level) {

        //reference our level for collision detection
        this.level = level;

        //track previous position in maze
        this.previousPosition = new Vector3();

        Table tablePad = new Table();
        tablePad.setFillParent(true);
        tablePad.left().bottom().pad(PADDING);

        Image pad = new Image(new Texture(Gdx.files.internal("controls/pad.png")));
        pad.addListener(new InputListener(){

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                checkPad(x, y, true);
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                checkPad(x, y, false);
                super.touchUp(event, x, y, pointer, button);
            }

            @Override
            public void touchDragged(InputEvent event, float x, float y, int pointer) {
                checkPad(x, y, true);
                super.touchDragged(event, x, y, pointer);
            }
        });

        tablePad.add(pad).row();

        getStage().addActor(tablePad);

        Table tableButtons = new Table();
        tableButtons.setFillParent(true);
        tableButtons.right().bottom().pad(PADDING);

        Image change = new Image(new Texture(Gdx.files.internal("controls/change.png")));
        change.addListener(new InputListener() {

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                setChange(true);
                updateFlag(KEY_SHOOT, false);
                super.touchUp(event, x, y, pointer, button);
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                setChange(false);
                updateFlag(KEY_SHOOT, false);
                return true;
            }

            @Override
            public void touchDragged(InputEvent event, float x, float y, int pointer) {
                setChange(false);
                updateFlag(KEY_SHOOT, false);
                super.touchDragged(event, x, y, pointer);
            }
        });

        Image shoot = new Image(new Texture(Gdx.files.internal("controls/shoot.png")));
        addListener(shoot, KEY_SHOOT, KEY_ACTION);
        Image tmpAction = new Image(new Texture(Gdx.files.internal("controls/action.png")));
        addListener(tmpAction, KEY_ACTION, KEY_SHOOT);
        Image strafeL = new Image(new Texture(Gdx.files.internal("controls/left.png")));
        addListener(strafeL, KEY_TURN_LEFT, KEY_TURN_RIGHT);
        Image strafeR = new Image(new Texture(Gdx.files.internal("controls/right.png")));
        addListener(strafeR, KEY_TURN_RIGHT, KEY_TURN_LEFT);

        tableButtons.add();
        tableButtons.add(change).pad(PADDING);
        tableButtons.row();
        tableButtons.add(tmpAction).pad(PADDING);
        tableButtons.add(shoot).pad(PADDING);
        tableButtons.row();
        tableButtons.add(strafeL).pad(PADDING);
        tableButtons.add(strafeR).pad(PADDING);

        getStage().addActor(tableButtons);

        //make sure we are capturing input correct
        setInput();

        //how fast can we turn?
        setSpeedRotate(DEFAULT_SPEED_ROTATE);

        //create our camera
        getCamera2d();
    }

    private void checkPad(float x, float y, boolean flag) {

        if (y >= MIN_COORDINATE && y <= MAX_COORDINATE) {
            if (x <= MIN_COORDINATE) {
                updateFlag(KEY_STRAFE_LEFT, flag);
                updateFlag(KEY_STRAFE_RIGHT, false);
            }
            if (x >= MAX_COORDINATE) {
                updateFlag(KEY_STRAFE_RIGHT, flag);
                updateFlag(KEY_STRAFE_LEFT, false);
            }
        }

        if (x >= MIN_COORDINATE && x <= MAX_COORDINATE) {

            if (y <= MIN_COORDINATE) {
                updateFlag(KEY_MOVE_BACKWARD, flag);
                updateFlag(KEY_MOVE_FORWARD, false);
            }
            if (y >= MAX_COORDINATE) {
                updateFlag(KEY_MOVE_FORWARD, flag);
                updateFlag(KEY_MOVE_BACKWARD, false);
            }
        }
    }

    public Level getLevel() {
        return this.level;
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

    private void addListener(Image img, int keyEnable, int keyDisable) {

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

            //move back to previous position
            getCamera3d().position.x = getPreviousPosition().x;
            getCamera3d().position.y = getPreviousPosition().y;
            getCamera3d().position.z = getPreviousPosition().z;

            //stop moving as well
            setMoveForward(false);
            setMoveBackward(false);
            setStrafeLeft(false);
            setStrafeRight(false);
        }
    }

    private boolean checkCollision() {

        //where are we currently located
        final float x = getCamera3d().position.x;
        final float y = getCamera3d().position.y;

        //player can't leave the maze
        if (x < 1 || y < 1)
            return true;
        if (x >= ROOM_SIZE * getLevel().getMaze().getCols() || y >= ROOM_SIZE * getLevel().getMaze().getRows())
            return true;

        //figure out which room we are in
        int col = (int)(x / ROOM_SIZE);
        int row = (int)(y / ROOM_SIZE);

        int roomCol = col * ROOM_SIZE;
        int roomRow = row * ROOM_SIZE;

        //get the current room
        Room room = getLevel().getMaze().getRoom(col, row);

        //if room not found we have collision
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

            case KEY_ACTION:
                setAction(flag);
                break;

            case KEY_SHOOT:
                setShooting(flag);
                break;

            case KEY_CHANGE:
                setChange(flag);
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

    public boolean isShooting() {
        return this.shooting;
    }

    public void setShooting(boolean shooting) {
        this.shooting = shooting;
    }

    public boolean isAction() {
        return this.action;
    }

    public void setAction(boolean action) {
        this.action = action;
    }

    public boolean isChange() {
        return this.change;
    }

    public void setChange(boolean change) {
        this.change = change;
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
        getStage().getBatch().setProjectionMatrix(getCamera2d().combined);

        //draw the buttons on the stage
        getStage().draw();
    }
}