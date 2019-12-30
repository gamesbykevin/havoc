package com.gamesbykevin.havoc.screen;

import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.gamesbykevin.havoc.MyGdxGame;

import static com.gamesbykevin.havoc.preferences.AppPreferences.hasLevelCompleted;
import static com.gamesbykevin.havoc.screen.ScreenHelper.SCREEN_GAME;
import static com.gamesbykevin.havoc.util.Language.getMyBundle;

public class LevelSelectScreen extends CustomSelectScreen {

    //how big is each button
    private static final float BUTTON_SIZE = 100f;

    //how many columns per row
    private static final int COLUMNS = 4;

    //provide some space between each selection
    private static final int PADDING = 20;

    //how many levels are there?
    public static final int LEVEL_COUNT = 10;

    public LevelSelectScreen(MyGdxGame game) {
        super(game);
        super.setButtonSize(BUTTON_SIZE);
        super.setColumns(COLUMNS);
        super.setPadding(PADDING);
        super.setTotal(LEVEL_COUNT);
    }

    @Override
    public void show() {
        super.show();
        getScroll().layout();
        getScroll().setScrollY(getScrollY());
    }

    @Override
    public void resume() {
        super.resume();
    }

    @Override
    public void handleClick(int index) {

        //remember our position
        setScrollY(getScroll().getScrollY());

        //switch to the game screen
        getGame().setPaused(false);
        getGame().getScreenHelper().changeScreen(SCREEN_GAME);
    }

    @Override
    public String getButtonText(int index) {

        if (hasLevelCompleted(index)) {
            return getMyBundle().get("levelSelectScreenSolved");
        } else {
            return (index + 1) + "";
        }
    }

    @Override
    public void setButtonTextFontSize(TextButton button, int index) {
        //do anything here?
    }

    @Override
    public String getTitleText() {
        return getMyBundle().get("levelSelectScreenTitleText");
    }
}