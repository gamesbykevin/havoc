package com.gamesbykevin.havoc.screen;

import com.gamesbykevin.havoc.MyGdxGame;
import com.gamesbykevin.havoc.util.Disposable;

import java.util.HashMap;

public class ScreenHelper implements Disposable {

    /**
     * Our different screens
     */
    public static final int SCREEN_SPLASH = 0;
    public static final int SCREEN_MENU = 1;
    public static final int SCREEN_OPTIONS = 2;
    public static final int SCREEN_GAME = 3;
    public static final int SCREEN_SELECT_LEVEL = 4;

    //what screen are we on?
    private int screenIndex = SCREEN_SPLASH;

    //hash map containing our screens
    private HashMap<Integer, TemplateScreen> screens;

    //our game reference
    private final MyGdxGame game;

    public ScreenHelper(MyGdxGame game) {

        //store game reference
        this.game = game;

        //set splash screen as start
        this.changeScreen(SCREEN_SPLASH);
    }

    private MyGdxGame getGame() {
        return this.game;
    }

    private HashMap<Integer, TemplateScreen> getScreens() {

        if (this.screens == null)
            this.screens = new HashMap<>();

        return this.screens;
    }

    public void setScreenIndex(final int screenIndex) {
        this.screenIndex = screenIndex;
    }

    public int getScreenIndex() {
        return this.screenIndex;
    }

    public TemplateScreen getScreen() {
        return getScreen(getScreenIndex());
    }

    public TemplateScreen getScreen(int screenIndex) {

        //get the screen from our hash map
        TemplateScreen screen = getScreens().get(screenIndex);

        //if the screen does not exist, we will create it
        if (screen == null) {

            switch (screenIndex) {

                case SCREEN_SPLASH:
                    screen = new SplashScreen(getGame());
                    break;

                case SCREEN_MENU:
                    screen = new MenuScreen(getGame());
                    break;

                case SCREEN_GAME:
                    screen = new GameScreen(getGame());
                    break;

                case SCREEN_OPTIONS:
                    screen = new OptionsScreen(getGame());
                    break;

                case SCREEN_SELECT_LEVEL:
                    screen = new LevelSelectScreen(getGame());
                    break;
            }

            //add the screen to our hash map
            getScreens().put(screenIndex, screen);
        }

        //return our screen
        return screen;
    }

    public void changeScreen(int screenIndex) {

        //if leaving the game screen, recycle assets
        if (getScreenIndex() == SCREEN_GAME && screenIndex != SCREEN_GAME) {
            this.screens.get(getScreenIndex()).dispose();
            this.screens.put(getScreenIndex(), null);
        }

        //get the new screen
        TemplateScreen templateScreen = getScreen(screenIndex);

        //set prompt to false every time we change the screen
        templateScreen.setPrompt(false);

        //set the screen accordingly
        getGame().setScreen(templateScreen);

        //if no exceptions are thrown let's officially change the screen index
        setScreenIndex(screenIndex);
    }

    @Override
    public void dispose() {

        if (this.screens != null) {
            for (Integer integer : this.screens.keySet()) {
                if (this.screens.get(integer) != null)
                    this.screens.get(integer).dispose();
                this.screens.put(integer, null);
            }

            this.screens.clear();
        }

        this.screens = null;
    }
}