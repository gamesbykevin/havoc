package com.gamesbykevin.havoc.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.gamesbykevin.havoc.GameEngine;
import com.gamesbykevin.havoc.MyGdxGame;
import com.gamesbykevin.havoc.gameservices.Achievement;
import com.gamesbykevin.havoc.gameservices.Leaderboard;

import static com.gamesbykevin.havoc.preferences.AppPreferences.setLevelCompleted;
import static com.gamesbykevin.havoc.screen.ScreenHelper.SCREEN_MENU;
import static com.gamesbykevin.havoc.util.Language.*;

public class GameScreen extends TemplateScreen {

    //our game engine
    private GameEngine engine;

    //does the game have focus
    private boolean gameFocus = false;

    //which level is selected
    private int indexLevel = -1;

    public GameScreen(MyGdxGame game) {
        super(game);
    }

    public void createEngine(int indexLevel) {
        this.engine = new GameEngine(indexLevel);
        this.indexLevel = indexLevel;
    }

    public int getIndexLevel() {
        return this.indexLevel;
    }

    @Override
    public void show() {

        //call parent
        super.show();

        //capture the game input
        captureInput();

        //these platforms need a back button
        switch(Gdx.app.getType()) {

            case WebGL:
            case Applet:
            case Desktop:
            case HeadlessDesktop:

                //create our back button
                TextButton buttonBack = new TextButton(getTranslatedText(KEY_OPTIONS_BACK), getSkin());

                //Add listeners to buttons
                buttonBack.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        getGame().getScreenHelper().changeScreen(SCREEN_MENU);
                    }
                });

                //Create Table
                Table table = new Table();

                //Set table to fill stage
                table.setFillParent(true);

                //Set alignment of contents in the table.
                table.right();

                //add button to the table
                table.add(buttonBack).width(BUTTON_WIDTH).height(BUTTON_HEIGHT).pad(BUTTON_PADDING * 2);

                //Add table to stage
                getStage().clear();
                getStage().addActor(table);
                break;
        }
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
        if (getEngine() != null)
            getEngine().resize(width, height);
    }

    @Override
    public void render(float delta) {

        boolean goal = false;

        //check if the player reached the goal once it's created
        if (getEngine().isCreated())
            goal = getEngine().getPlayer().isGoal();

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

            } else {

                //if we are at the goal, provide the user a way to go back and check for trophy(ies), achievement(s), etc...
                if (getEngine().isCreated() && getEngine().getPlayer().isGoal()) {

                    //if we just reached the goal
                    if (!goal) {

                        //save on the app preferences
                        setLevelCompleted(getIndexLevel(), true);

                        //unlock achievement
                        Achievement.unlock(getGame().getGsClient(), getEngine().getLevel(), getIndexLevel());

                        //submit time to leader board
                        Leaderboard.submit(getGame().getGsClient(), getEngine().getLevel(), getIndexLevel());
                    }

                    //act the stage
                    getStage().act();

                    //draw the remaining of the stage
                    getStage().draw();
                }
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