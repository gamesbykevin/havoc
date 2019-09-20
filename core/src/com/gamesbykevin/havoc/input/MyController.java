package com.gamesbykevin.havoc.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.viewport.StretchViewport;

import java.util.HashMap;

import static com.gamesbykevin.havoc.MyGdxGame.SIZE_HEIGHT;
import static com.gamesbykevin.havoc.MyGdxGame.SIZE_WIDTH;

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

    //list of keys pressed
    private HashMap<Integer, Boolean> keys;

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
    private final PerspectiveCamera camera3d;

    //our input listener
    private Stage stage;

    private Vector3 touchPos = new Vector3();

    //move camera up/down while we are walking/running
    private float minZ = 0f;
    private float maxZ = .25f;
    private float vz = 0.01f;

    //different control inputs
    public static final int MOVE_FORWARD = Input.Keys.W;
    public static final int MOVE_BACKWARD = Input.Keys.S;
    public static final int MOVE_RUN = Input.Keys.SHIFT_LEFT;
    public static final int STRAFE_LEFT = Input.Keys.A;
    public static final int STRAFE_RIGHT = Input.Keys.D;
    public static final int TURN_LEFT = Input.Keys.R;
    public static final int TURN_RIGHT = Input.Keys.T;

    public MyController(PerspectiveCamera camera3d) {
        this.keys = new HashMap<>();
        this.camera3d = camera3d;
        this.previousPosition = new Vector3();

        this.stage = new Stage(new StretchViewport(SIZE_WIDTH, SIZE_HEIGHT));

        addButton(BUTTON_RIGHT_X, BUTTON_RIGHT_Y, BUTTON_RIGHT_W, BUTTON_RIGHT_H, TURN_RIGHT, TURN_LEFT);
        addButton(BUTTON_LEFT_X, BUTTON_LEFT_Y, BUTTON_LEFT_W, BUTTON_LEFT_H, TURN_LEFT, TURN_RIGHT);
        addButton(BUTTON_UP_X, BUTTON_UP_Y, BUTTON_UP_W, BUTTON_UP_H, MOVE_FORWARD, MOVE_BACKWARD);
        addButton(BUTTON_DOWN_X, BUTTON_DOWN_Y, BUTTON_DOWN_W, BUTTON_DOWN_H, MOVE_BACKWARD, MOVE_FORWARD);

        //make sure we are capturing input correct
        setInput();

        setSpeedRotate(DEFAULT_SPEED_ROTATE);
    }

    public void setInput() {

        Gdx.input.setInputProcessor(this.stage);
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
                getKeys().put(keyEnable, true);
                getKeys().remove(keyDisable);
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                getKeys().remove(keyEnable);
                getKeys().remove(keyDisable);
                super.touchUp(event, x, y, pointer, button);
            }

            @Override
            public void touchDragged(InputEvent event, float x, float y, int pointer) {
                getKeys().put(keyEnable, true);
                getKeys().remove(keyDisable);
                super.touchDragged(event, x, y, pointer);
            }
        });

        //add to the stage
        stage.addActor(img);
    }

    public void update() {

        int xMove = 0;
        int yMove = 0;

        if (getKeys().containsKey(MOVE_RUN)) {
            setSpeed(SPEED_RUN);
        } else {
            setSpeed(SPEED_WALK);
        }

        if (getKeys().containsKey(MOVE_FORWARD))
            yMove++;
        if (getKeys().containsKey(MOVE_BACKWARD))
            yMove--;

        if (getKeys().containsKey(STRAFE_LEFT))
            xMove--;
        if (getKeys().containsKey(STRAFE_RIGHT))
            xMove++;

        float rotationA = 0;

        if (getKeys().containsKey(TURN_LEFT)) {
            rotationA = getSpeedRotate();
        } else if (getKeys().containsKey(TURN_RIGHT)) {
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
            getPreviousPosition().x = camera3d.position.x;
            getPreviousPosition().y = camera3d.position.y;
            getPreviousPosition().z = camera3d.position.z;
        }

        //update camera location?
        camera3d.position.x += xa;
        camera3d.position.y += ya;
        camera3d.rotate(Vector3.Z, rotationA);

        if (getKeys().containsKey(MOVE_FORWARD) || getKeys().containsKey(MOVE_BACKWARD)) {

            camera3d.position.z += vz;

            if (camera3d.position.z <= minZ) {
                camera3d.position.z = minZ;
                vz = -vz;
            } else if (camera3d.position.z >= maxZ) {
                camera3d.position.z = maxZ;
                vz = -vz;
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

        //System.out.println(camera.position);
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

    public HashMap<Integer, Boolean> getKeys() {
        return this.keys;
    }

    @Override
    public boolean keyDown (int keycode) {
        getKeys().put(keycode, true);
        return false;
    }

    @Override
    public boolean keyUp (int keycode) {
        getKeys().remove(keycode);
        //generate = true;
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
        return this.camera3d;
    }

    public Vector3 getPreviousPosition() {
        return this.previousPosition;
    }
}