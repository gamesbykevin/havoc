package com.gamesbykevin.havoc.screen;

import com.gamesbykevin.havoc.GameMain;
import com.gamesbykevin.havoc.MyGdxGame;

public class GameScreen extends TemplateScreen {

    //our game engine
    private GameMain engine;

    public GameScreen(MyGdxGame game) {
        super(game);

        //create new instance
        this.engine = new GameMain();
    }

    @Override
    public void show() {
        super.show();

        //capture the game input
        captureInput();
    }

    @Override
    public void pause() {
        super.pause();
        getGame().setPaused(true);
    }

    @Override
    public void resume() {
        super.resume();
        getEngine().resume();
    }

    @Override
    public void dispose() {
        super.dispose();

        if (this.engine != null) {
            this.engine.dispose();
            this.engine = null;
        }
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        getEngine().resize(width, height);
    }

    @Override
    public void render(float delta) {

        //render/update the game
        getEngine().render();

        //if paused show the transparent overlay
        if (getGame().isPaused()) {
            getBatch().begin();
            getOverlay().draw(getBatch());
            getBatch().end();
        }
    }

    public GameMain getEngine() {
        return this.engine;
    }
}