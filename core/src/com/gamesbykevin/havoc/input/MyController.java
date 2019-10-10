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

import static com.gamesbykevin.havoc.MyGdxGame.SIZE_HEIGHT;
import static com.gamesbykevin.havoc.MyGdxGame.SIZE_WIDTH;

public class MyController implements InputProcessor {

    //space between the buttons
    public static final int PADDING = 5;

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
    public static final float MIN_Z = 0f;
    public static final float MAX_Z = .25f;
    public static float VELOCITY_Z = 0.01f;

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
        getPreviousPosition().x = getCamera3d().position.x;
        getPreviousPosition().y = getCamera3d().position.y;

        Table tablePad = new Table();
        tablePad.setFillParent(true);
        tablePad.left().bottom().pad(PADDING);

        Image forward = new Image(new Texture(Gdx.files.internal("controls/forward.png")));
        Image backward = new Image(new Texture(Gdx.files.internal("controls/backward.png")));
        Image strafeLeft = new Image(new Texture(Gdx.files.internal("controls/strafeLeft.png")));
        Image strafeRight = new Image(new Texture(Gdx.files.internal("controls/strafeRight.png")));

        createPadListener(forward, KEY_MOVE_FORWARD, KEY_MOVE_BACKWARD);
        createPadListener(backward, KEY_MOVE_BACKWARD, KEY_MOVE_FORWARD);
        createPadListener(strafeLeft, KEY_STRAFE_LEFT, KEY_STRAFE_RIGHT);
        createPadListener(strafeRight, KEY_STRAFE_RIGHT, KEY_STRAFE_LEFT);

        tablePad.add().colspan(1);
        tablePad.add(forward).colspan(1);
        tablePad.add().colspan(1);
        tablePad.row();
        tablePad.add(strafeLeft).colspan(1);
        tablePad.add().colspan(1);
        tablePad.add(strafeRight).colspan(1);
        tablePad.row();
        tablePad.add().colspan(1);
        tablePad.add(backward).colspan(1).row();
        tablePad.add().colspan(1);

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
                updateFlag(KEY_SHOOT, false);
                return true;
            }

            @Override
            public void touchDragged(InputEvent event, float x, float y, int pointer) {
                updateFlag(KEY_SHOOT, false);
                super.touchDragged(event, x, y, pointer);
            }
        });

        Image shoot = new Image(new Texture(Gdx.files.internal("controls/shoot.png")));
        addListener(shoot, KEY_SHOOT, KEY_CHANGE);

        Image tmpAction = new Image(new Texture(Gdx.files.internal("controls/action.png")));
        tmpAction.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                setAction(true);
                super.touchUp(event, x, y, pointer, button);
            }
        });

        Image turnLeft = new Image(new Texture(Gdx.files.internal("controls/left.png")));
        addListener(turnLeft, KEY_TURN_LEFT, KEY_TURN_RIGHT);
        Image turnRight = new Image(new Texture(Gdx.files.internal("controls/right.png")));
        addListener(turnRight, KEY_TURN_RIGHT, KEY_TURN_LEFT);

        tableButtons.add();
        tableButtons.add(change).pad(PADDING);
        tableButtons.row();
        tableButtons.add(tmpAction).pad(PADDING);
        tableButtons.add(shoot).pad(PADDING);
        tableButtons.row();
        tableButtons.add(turnLeft).pad(PADDING);
        tableButtons.add(turnRight).pad(PADDING);

        getStage().addActor(tableButtons);

        //make sure we are capturing input correct
        setInput();

        //how fast can we turn?
        setSpeedRotate(DEFAULT_SPEED_ROTATE);

        //create our camera
        getCamera2d();
    }

    private void createPadListener(Image image, int keyEnabled, int keyDisabled) {

        image.addListener(new InputListener(){

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                updateFlag(keyEnabled, true);
                updateFlag(keyDisabled, false);
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                updateFlag(keyEnabled, false);
                super.touchUp(event, x, y, pointer, button);
            }

            @Override
            public void touchDragged(InputEvent event, float x, float y, int pointer) {
                super.touchDragged(event, x, y, pointer);
            }
        });
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
                setAction(!flag);
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