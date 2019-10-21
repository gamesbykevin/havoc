package com.gamesbykevin.havoc.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.gamesbykevin.havoc.level.Level;

import static com.gamesbykevin.havoc.MyGdxGame.SIZE_HEIGHT;
import static com.gamesbykevin.havoc.MyGdxGame.SIZE_WIDTH;
import static com.gamesbykevin.havoc.input.MyControllerHelper.*;

public class MyController implements InputProcessor {

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

    //track the joystick position
    private float knobPercentX = 0, knobPercentY = 0;

    public MyController(Level level) {

        //reference our level for collision detection
        this.level = level;

        //track previous position in maze
        this.previousPosition = new Vector3();
        getPreviousPosition().x = getCamera3d().position.x;
        getPreviousPosition().y = getCamera3d().position.y;

        //setup our controller ui
        setupController(this);

        //make sure we are capturing input correct
        setInput();

        //how fast can we turn?
        setSpeedRotate(DEFAULT_SPEED_ROTATE);

        //create our camera
        getCamera2d();
    }

    public void setKnobPercentX(float knobPercentX) {
        this.knobPercentX = knobPercentX;
    }

    public void setKnobPercentY(float knobPercentY) {
        this.knobPercentY = knobPercentY;
    }

    public float getKnobPercentX() {
        return this.knobPercentX;
    }

    public float getKnobPercentY() {
        return this.knobPercentY;
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

        //Gdx.input.setInputProcessor(getStage());

        switch (Gdx.app.getType()) {
            case Android:
            case iOS:
                Gdx.input.setInputProcessor(this.stage);
                break;

            default:
                Gdx.input.setInputProcessor(this);
                break;
        }
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
        updateFlag(this, keycode, true);
        return false;
    }

    @Override
    public boolean keyUp (int keycode) {
        updateFlag(this, keycode, false);
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

    public PerspectiveCamera getCamera3d() {
        return this.level.getCamera3d();
    }

    public Vector3 getPreviousPosition() {
        return this.previousPosition;
    }

    public boolean isStrafeLeft() {
        return this.strafeLeft;
    }

    public boolean isStrafeRight() {
        return this.strafeRight;
    }

    public boolean isTurnRight() {
        return this.turnRight;
    }

    public boolean isTurnLeft() {
        return this.turnLeft;
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

    public void setStrafeLeft(boolean strafeLeft) {
        this.strafeLeft = strafeLeft;
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

    public void setTurnRight(boolean turnRight) {
        this.turnRight = turnRight;
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

        return this.stage;
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