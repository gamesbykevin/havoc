package com.gamesbykevin.havoc.screen;

import com.badlogic.gdx.assets.AssetManager;
import com.gamesbykevin.havoc.GameEngine;
import com.gamesbykevin.havoc.MyGdxGame;

public class GameScreen extends TemplateScreen {

    //our game engine
    private GameEngine engine;

    //does the game have focus
    private boolean gameFocus = false;

    public GameScreen(MyGdxGame game) {
        super(game);

        //create new instance
        this.engine = new GameEngine();
    }

    @Override
    public void show() {

        //call parent
        super.show();

        //capture the game input
        captureInput();
    }

    @Override
    public void pause() {

        //call parent
        super.pause();

        //pause the parent
        getGame().setPaused(true);

        //flag the game paused
        getEngine().setPaused(true);

        //the game no longer has focus
        setGameFocus(false);
    }

    @Override
    public void resume() {

        //call parent
        super.resume();

        //game doesn't have focus
        setGameFocus(false);
    }

    @Override
    public void dispose() {

        //call parent
        super.dispose();

        if (this.engine != null)
            this.engine.dispose();
        this.engine = null;
    }

    @Override
    public void resize(int width, int height) {

        //call parent
        super.resize(width, height);

        //make sure game is updated
        getEngine().resize(width, height);
    }

    @Override
    public void render(float delta) {

        //render & update the game (if not paused)
        getEngine().render();

        if (getGame().isPaused()) {

            //render the transparent overlay
            getBatch().begin();
            getOverlay().draw(getBatch());
            getBatch().end();

        } else {

            //if we don't have focus on the game
            if (!isGameFocus() && getEngine().isCreated()) {

                //continue with game updates
                getEngine().resume(getInputMultiplexer());

                //we now have focus
                setGameFocus(true);
            }
        }
    }

    public GameEngine getEngine() {
        return this.engine;
    }

    public boolean isGameFocus() {
        return this.gameFocus;
    }

    public void setGameFocus(boolean gameFocus) {
        this.gameFocus = gameFocus;
    }
}