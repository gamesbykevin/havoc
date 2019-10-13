package com.gamesbykevin.havoc.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

public class MyControllerHelper {

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

    //location of our assets
    private static final String CONTROL_PATH_SHOOT = "controls/shoot.png";
    private static final String CONTROL_PATH_ACTION = "controls/action.png";
    private static final String CONTROL_PATH_CHANGE = "controls/change.png";

    //how to setup our touch pad
    private static final float TOUCHPAD_X = 10;
    private static final float TOUCHPAD_Y = 10;
    private static final float TOUCHPAD_SIZE = 200;
    private static final float TOUCHPAD_KNOB_RATIO = 0.40f;
    private static final float TOUCHPAD_DEADZONE_RATIO = 0.03f;
    private static final String TOUCHPAD_PATH_BACKGROUND = "controls/joystick.png";
    private static final String TOUCHPAD_PATH_KNOB = "controls/knob.png";
    private static final String TOUCHPAD_NAME_BACKGROUND = "touchBackground";
    private static final String TOUCHPAD_NAME_KNOB = "touchKnob";

    //size of each button
    public static final int BUTTON_SIZE = 80;

    //space between the buttons
    public static final int PADDING = 5;

    protected static void setupController(final MyController controller) {

        Skin skin = new Skin();
        skin.add(TOUCHPAD_NAME_BACKGROUND, new Texture(TOUCHPAD_PATH_BACKGROUND));
        skin.add(TOUCHPAD_NAME_KNOB, new Texture(TOUCHPAD_PATH_KNOB));
        Touchpad.TouchpadStyle style = new Touchpad.TouchpadStyle();
        Drawable knob = skin.getDrawable(TOUCHPAD_NAME_KNOB);
        knob.setMinHeight(TOUCHPAD_SIZE * TOUCHPAD_KNOB_RATIO);
        knob.setMinWidth(TOUCHPAD_SIZE * TOUCHPAD_KNOB_RATIO);

        style.background = skin.getDrawable(TOUCHPAD_NAME_BACKGROUND);
        style.knob = knob;

        Touchpad touchpad = new Touchpad(TOUCHPAD_SIZE * TOUCHPAD_DEADZONE_RATIO, style);
        touchpad.setBounds(TOUCHPAD_X, TOUCHPAD_Y, TOUCHPAD_SIZE, TOUCHPAD_SIZE);

        touchpad.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                controller.setKnobPercentX(((Touchpad)actor).getKnobPercentX());
                controller.setKnobPercentY(((Touchpad)actor).getKnobPercentY());
            }
        });

        controller.getStage().addActor(touchpad);

        Table tableButtons = new Table();
        tableButtons.setFillParent(true);
        tableButtons.right().bottom().pad(PADDING);

        Image tmpChange = new Image(new Texture(Gdx.files.internal(CONTROL_PATH_CHANGE)));
        tmpChange.addListener(new InputListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                controller.setChange(true);
                updateFlag(controller, KEY_SHOOT, false);
                super.touchUp(event, x, y, pointer, button);
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                updateFlag(controller, KEY_SHOOT, false);
                return true;
            }

            @Override
            public void touchDragged(InputEvent event, float x, float y, int pointer) {
                updateFlag(controller, KEY_SHOOT, false);
                super.touchDragged(event, x, y, pointer);
            }
        });

        Image tmpShoot = new Image(new Texture(Gdx.files.internal(CONTROL_PATH_SHOOT)));
        tmpShoot.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                updateFlag(controller, KEY_SHOOT, true);
                updateFlag(controller, KEY_CHANGE, false);
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                updateFlag(controller, KEY_SHOOT, false);
                updateFlag(controller, KEY_CHANGE, false);
                super.touchUp(event, x, y, pointer, button);
            }

            @Override
            public void touchDragged(InputEvent event, float x, float y, int pointer) {
                updateFlag(controller, KEY_SHOOT, true);
                updateFlag(controller, KEY_CHANGE, false);
                super.touchDragged(event, x, y, pointer);
            }
        });

        Image tmpAction = new Image(new Texture(Gdx.files.internal(CONTROL_PATH_ACTION)));
        tmpAction.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                controller.setAction(true);
                super.touchUp(event, x, y, pointer, button);
            }
        });

        tableButtons.add();
        tableButtons.add(tmpChange).width(BUTTON_SIZE).height(BUTTON_SIZE).pad(PADDING);
        tableButtons.row();
        tableButtons.add(tmpAction).width(BUTTON_SIZE).height(BUTTON_SIZE).pad(PADDING);
        tableButtons.add(tmpShoot).width(BUTTON_SIZE).height(BUTTON_SIZE).pad(PADDING);
        tableButtons.row();

        controller.getStage().addActor(tableButtons);
    }

    protected static void updateFlag(MyController controller, int keycode, boolean flag) {

        switch (keycode) {
            case KEY_MOVE_BACKWARD:
                controller.setMoveBackward(flag);
                break;

            case KEY_MOVE_FORWARD:
                controller.setMoveForward(flag);
                break;

            case KEY_MOVE_RUNNING:
                controller.setRunning(flag);
                break;

            case KEY_STRAFE_LEFT:
                controller.setStrafeLeft(flag);
                break;

            case KEY_STRAFE_RIGHT:
                controller.setStrafeRight(flag);
                break;

            case KEY_TURN_LEFT:
                controller.setTurnLeft(flag);
                break;

            case KEY_TURN_RIGHT:
                controller.setTurnRight(flag);
                break;

            case KEY_ACTION:
                controller.setAction(!flag);
                break;

            case KEY_SHOOT:
                controller.setShooting(flag);
                break;

            case KEY_CHANGE:
                controller.setChange(flag);
                break;
        }
    }
}