package com.gamesbykevin.havoc.input;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector3;
import com.gamesbykevin.havoc.MyGdxGame;
import com.gamesbykevin.havoc.util.Disposable;

import static com.gamesbykevin.havoc.MyGdxGame.EXIT;
import static com.gamesbykevin.havoc.screen.ScreenHelper.*;

public class ScreenController implements InputProcessor, Disposable {

    //keep reference to our game
    private final MyGdxGame game;

    //how many fingers are on the screen
    private int count = 0;

    //keep track where we touch the screen
    private Vector3 position;

    public ScreenController(MyGdxGame game) {
        this.game = game;
    }

    public Vector3 getPosition() {

        if (this.position == null)
            this.position = new Vector3();

        return this.position;
    }

    @Override
    public void dispose() {
        this.position =  null;
    }

    private MyGdxGame getGame() {
        return this.game;
    }

    @Override
    public boolean keyDown(int keycode) {

        if (EXIT)
            return false;

        //game can't be paused when checking for input
        if (getGame().isPaused())
            return false;

        return false;
    }

    @Override
    public boolean keyUp(int keycode) {

        if (EXIT)
            return false;

        //game can't be paused when checking for input
        if (getGame().isPaused())
            return false;

        if (keycode == Input.Keys.ESCAPE || keycode == Input.Keys.BACK) {
            switch (getGame().getScreenHelper().getScreenIndex()) {

                case SCREEN_GAME:
                case SCREEN_SELECT_LEVEL:
                case SCREEN_OPTIONS:

                    //go back to the menu
                    getGame().getScreenHelper().changeScreen(SCREEN_MENU);
                    break;
            }
        }

        return false;
    }

    @Override
    public boolean keyTyped(char keycode) {

        if (EXIT)
            return false;

        //game can't be paused when checking for input
        if (getGame().isPaused())
            return false;

        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {

        if (EXIT)
            return false;

        //game can't be paused when checking for input
        if (getGame().isPaused())
            return false;

        //keep track of fingers on screen
        count++;

        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {

        if (EXIT)
            return false;

        //after touching the screen the game is not paused
        if (getGame().isPaused()) {
            getGame().setPaused(false);
            return false;
        }

        //keep track of fingers on screen
        count--;

        //we can't have less than 0 fingers on the screen
        if (count < 0)
            count = 0;

        //assign our coordinates
        getPosition().x = screenX;
        getPosition().y = screenY;

        if (count == 0) {

            switch(getGame().getScreenHelper().getScreenIndex()) {

                case SCREEN_GAME:

                    //figure out where we touched
                    getGame().getScreenHelper().getScreen().getCamera().unproject(getPosition());
                    break;
            }
        }

        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {

        if (EXIT)
            return false;

        //game can't be paused when checking for input
        if (getGame().isPaused())
            return false;

        return false;
    }

    @Override
    public boolean mouseMoved(int x, int y) {

        if (EXIT)
            return false;

        //game can't be paused when checking for input
        if (getGame().isPaused())
            return false;

        return false;
    }

    @Override
    public boolean scrolled(int amount) {

        if (EXIT)
            return false;

        //game can't be paused when checking for input
        if (getGame().isPaused())
            return false;

        return false;
    }
}