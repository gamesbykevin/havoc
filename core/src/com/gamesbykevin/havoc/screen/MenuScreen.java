package com.gamesbykevin.havoc.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.gamesbykevin.havoc.MyGdxGame;
import com.gamesbykevin.havoc.exception.ScreenException;

import static com.gamesbykevin.havoc.util.Language.getMyBundle;
import static com.gamesbykevin.havoc.screen.ScreenHelper.SCREEN_OPTIONS;
import static com.gamesbykevin.havoc.screen.ScreenHelper.SCREEN_SELECT_LEVEL;

public class MenuScreen extends TemplateScreen {

    public static final String URL_MORE = "http://gamesbykevin.com";
    public static final String URL_RATE = "https://play.google.com/store/apps/details?id=com.gamesbykevin.havoc";

    public MenuScreen(MyGdxGame game) {

        //call parent
        super(game);
    }

    @Override
    public void show() {

        //call parent
        super.show();

        //if session is not active, then we need to login
        if (getGame().getGsClient() != null && !getGame().getGsClient().isSessionActive())
            getGame().getGsClient().logIn();

        //capture the menu input
        captureInput();

        //Create Table
        Table table = new Table();

        //Set table to fill stage
        table.setFillParent(true);

        //Set alignment of contents in the table.
        table.center();

        //Create buttons
        TextButton buttonPlay = new TextButton(getMyBundle().get("menuScreenPlay"), getSkin());
        TextButton buttonOptions = new TextButton(getMyBundle().get("menuScreenOptions"), getSkin());
        TextButton buttonRate = new TextButton(getMyBundle().get("menuScreenRate"), getSkin());
        TextButton buttonMore = new TextButton(getMyBundle().get("menuScreenMore"), getSkin());
        TextButton buttonExit = new TextButton(getMyBundle().get("menuScreenExit"), getSkin());

        //Add listeners to buttons
        buttonPlay.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                try {

                    //now switch screens
                    getGame().getScreenHelper().changeScreen(SCREEN_SELECT_LEVEL);

                } catch (ScreenException ex) {
                    ex.printStackTrace();
                }
            }
        });

        buttonOptions.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                try {
                    getGame().getScreenHelper().changeScreen(SCREEN_OPTIONS);
                } catch (ScreenException ex) {
                    ex.printStackTrace();
                }
            }
        });

        buttonRate.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.net.openURI(URL_RATE);
            }
        });

        buttonMore.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.net.openURI(URL_MORE);
            }
        });

        buttonExit.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {

                //exit the game
                MyGdxGame.exit(getGame());
            }
        });

        //Add buttons to table
        table.add(buttonPlay).width(BUTTON_WIDTH).height(BUTTON_HEIGHT).pad(BUTTON_PADDING);
        table.add();
        table.add(buttonOptions).width(BUTTON_WIDTH).height(BUTTON_HEIGHT).pad(BUTTON_PADDING);
        table.add();
        table.add(buttonRate).width(BUTTON_WIDTH).height(BUTTON_HEIGHT).pad(BUTTON_PADDING);
        table.row();
        table.add(buttonMore).width(BUTTON_WIDTH).height(BUTTON_HEIGHT).pad(BUTTON_PADDING);

        switch(Gdx.app.getType()) {

            //only the desktop application we can exit
            case Desktop:
                table.add();
                table.add(buttonExit).width(BUTTON_WIDTH).height(BUTTON_HEIGHT).pad(BUTTON_PADDING);
                break;
        }

        //Add table to stage
        getStage().clear();
        getStage().addActor(table);

        //add our social media icons
        super.addSocialIcons();
    }

    @Override
    public void resume() {
        super.resume();
    }

    @Override
    public void render(float delta) {

        //clear the screen
        super.clearScreen();

        //act the stage
        getStage().act();

        //now render
        getStage().getBatch().begin();
        super.drawBackground(getStage().getBatch());
        super.drawLogo(getStage().getBatch());
        getStage().getBatch().end();

        //draw the remaining of the stage
        getStage().draw();
    }

    @Override
    public void dispose() {
        super.dispose();
    }
}