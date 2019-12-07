package com.gamesbykevin.havoc.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.gamesbykevin.havoc.util.Restart;

import static com.gamesbykevin.havoc.MyGdxGame.SIZE_HEIGHT;
import static com.gamesbykevin.havoc.MyGdxGame.SIZE_WIDTH;
import static com.gamesbykevin.havoc.input.MyControllerHelper.*;

public class MyController implements InputProcessor, Disposable, Restart {

    //how fast can we move
    public static final float SPEED_WALK = .1f;

    //how fast can the player turn by default
    public static final float DEFAULT_SPEED_ROTATE = 2.5f;

    //our rotation and rotation angle
    private float rotation = 0;

    //the least/most we can turn
    public static final float ROTATION_ANGLE_MIN = 0.0f;
    public static final float ROTATION_ANGLE_MAX = 360.0f;

    //our input listener
    private Stage stage;

    //what are we doing?
    private boolean moveForward = false;
    private boolean moveBackward = false;
    private boolean strafeLeft = false;
    private boolean strafeRight = false;
    private boolean turnRight = false;
    private boolean turnLeft = false;
    private boolean shooting = false;
    private boolean action = false;
    private boolean change = false;

    //was the screen touched?
    private boolean touch = false;

    //camera to render controls
    private OrthographicCamera camera2d;

    //track the joystick position
    private float knobPercentX = 0, knobPercentY = 0;

    //do we display the controller?
    private final boolean display;

    public MyController(AssetManager assetManager) {

        //setup our controller ui when needed
        switch (Gdx.app.getType()) {

            case Android:
            case iOS:
            case WebGL:
            case HeadlessDesktop:
                display = true;
                break;

            case Desktop:
            default:
                display = false;
                break;
        }


        if (isDisplay())
            setupController(assetManager, this);

        //make sure we are capturing input correct
        setInput();

        //create our camera
        getCamera2d();
    }

    @Override
    public void reset() {

        //flag everything false
        setMoveForward(false);
        setMoveBackward(false);
        setShooting(false);
        setAction(false);
        setChange(false);
        setTurnLeft(false);
        setTurnRight(false);
        setStrafeLeft(false);
        setStrafeRight(false);
        setTouch(false);
        setKnobPercentX(0);
        setKnobPercentY(0);
        setRotation(0);
    }

    @Override
    public void dispose() {

        if (this.stage != null)
            this.stage.dispose();

        this.stage = null;
        this.camera2d = null;
    }

    private boolean isDisplay() {
        return this.display;
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

    public OrthographicCamera getCamera2d() {

        //create camera if null
        if (this.camera2d == null) {
            this.camera2d = new OrthographicCamera();
            this.camera2d.setToOrtho(false, SIZE_WIDTH, SIZE_HEIGHT);
        }

        return this.camera2d;
    }

    public void setInput() {
        if (isDisplay()) {
            Gdx.input.setInputProcessor(getStage());
        } else {
            Gdx.input.setInputProcessor(this);
        }
    }

    public float getRotation() {
        return this.rotation;
    }

    public void setRotation(float rotation) {
        this.rotation = rotation;
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
        setTouch(true);
        return false;
    }

    @Override
    public boolean touchDown (int x, int y, int pointer, int button) {
        setTouch(true);
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
        setTouch(true);
        return false;
    }

    @Override
    public boolean scrolled (int amount) {
        return false;
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

    public boolean isTouch() {
        return this.touch;
    }

    public void setTouch(boolean touch) {
        this.touch = touch;
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