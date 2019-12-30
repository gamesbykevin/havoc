package com.gamesbykevin.havoc.screen;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.gamesbykevin.havoc.MyGdxGame;
import de.golfgl.gdxgamesvcs.GameServiceException;

import static com.gamesbykevin.havoc.MyGdxGame.EXIT;

public abstract class TemplateScreen extends ParentScreen {

    //links to social media
    public static final String URL_YOUTUBE = "https://youtube.com/gamesbykevin";
    public static final String URL_FACEBOOK = "https://facebook.com/gamesbykevin";
    public static final String URL_TWITTER = "https://twitter.com/gamesbykevin";
    public static final String URL_INSTAGRAM = "https://instagram.com/gamesbykevin";

    //items to show our menu
    private Stage stage;
    private Skin skin;

    //size of our buttons
    protected static final int BUTTON_WIDTH = (int)(SCREEN_WIDTH * .25f);
    protected static final int BUTTON_HEIGHT = 50;

    //padding for each button
    protected static final int BUTTON_PADDING = 10;

    //how big are our social media icons
    protected static final int SOCIAL_ICON_SIZE = 96;

    //how much space between icons
    protected static final int SOCIAL_ICON_PADDING = 15;

    //did we prompt user when they hit the back button
    private boolean prompt = false;

    //needed to capture multiple input from keyboard/mobile as well as the stage
    private InputMultiplexer inputMultiplexer;

    //game title logo
    private Texture logo;

    //paths to menu assets
    public static final String PATH_SKIN                = PATH_PARENT_DIR + "skin/skin.json";
    public static final String PATH_ATLAS               = PATH_PARENT_DIR + "skin/skin.atlas";
    public static final String PATH_LOGO                = PATH_PARENT_DIR + "logo.png";
    public static final String PATH_SOCIAL_TWITTER      = PATH_PARENT_DIR + "social_icons/twitter.png";
    public static final String PATH_SOCIAL_YOUTUBE      = PATH_PARENT_DIR + "social_icons/youtube.png";
    public static final String PATH_SOCIAL_FACEBOOK     = PATH_PARENT_DIR + "social_icons/facebook.png";
    public static final String PATH_SOCIAL_INSTAGRAM    = PATH_PARENT_DIR + "social_icons/instagram.png";
    public static final String PATH_SOCIAL_ACHIEVEMENT  = PATH_PARENT_DIR + "social_icons/achievement.png";
    public static final String PATH_SOCIAL_LEADERBOARD  = PATH_PARENT_DIR + "social_icons/leaderboard.png";

    //logo padding from the top of the screen
    public static final int PADDING_LOGO = 10;

    //where to render that logo
    private final float logoX, logoY;

    public TemplateScreen(MyGdxGame game) {

        //call parent
        super(game);

        //create our input multiplexer
        this.inputMultiplexer = new InputMultiplexer();

        //load our atlas and skin simultaneously
        this.skin = new Skin(Gdx.files.internal(PATH_SKIN), new TextureAtlas(PATH_ATLAS));

        Viewport viewport = new StretchViewport(SCREEN_WIDTH, SCREEN_HEIGHT, getCamera());
        viewport.apply();

        //create the stage
        this.stage = new Stage(viewport) {

            //handle back button
            @Override
            public boolean keyDown(int keyCode) {

                if (EXIT)
                    return false;

                if (keyCode == Input.Keys.BACK || keyCode == Input.Keys.ESCAPE) {
                    if (getGame().getScreenHelper().getScreenIndex() == ScreenHelper.SCREEN_OPTIONS) {
                        getGame().getScreenHelper().changeScreen(ScreenHelper.SCREEN_MENU);
                        setPrompt(false);
                    } else if (getGame().getScreenHelper().getScreenIndex() == ScreenHelper.SCREEN_MENU) {

                        if (hasPrompt()) {

                            //if user was already prompted, lose
                            MyGdxGame.exit(getGame());

                        } else {

                            setPrompt(true);
                        }
                    }
                }
                return super.keyDown(keyCode);
            }
        };

        //create our game title logo and place center near top of the screen
        this.logo = new Texture(Gdx.files.internal(PATH_LOGO));
        this.logoX = (SCREEN_WIDTH / 2) - (getLogo().getWidth() / 2);
        this.logoY = SCREEN_HEIGHT - getLogo().getHeight() - PADDING_LOGO;
    }

    public boolean hasPrompt() {
        return this.prompt;
    }

    public void setPrompt(boolean prompt) {
        this.prompt = prompt;
    }

    public float getLogoX() {
        return this.logoX;
    }

    public float getLogoY() {
        return this.logoY;
    }

    public Texture getLogo() {
        return this.logo;
    }

    @Override
    public void pause() {

        //call parent
        super.pause();

        //flag false
        setPrompt(false);

        //capture the input
        captureInput();
    }

    @Override
    public void resume() {

        //call parent
        super.resume();

        //flag false
        setPrompt(false);

        //make sure we are capturing input
        captureInput();
    }

    @Override
    public void show() {

        //call parent
        super.show();

        //make sure we are capturing input
        captureInput();

        //flag false
        setPrompt(false);
    }

    public void drawLogo(Batch batch) {
        batch.draw(getLogo(), getLogoX(), getLogoY());
    }

    public InputMultiplexer getInputMultiplexer() {
        return this.inputMultiplexer;
    }

    protected void captureInput() {

        if (EXIT)
            return;

        //clear anything we have added
        getInputMultiplexer().clear();

        getInputMultiplexer().addProcessor(getGame().getController());
        getInputMultiplexer().addProcessor(getStage());

        //then set the multiplexer to capture both input
        Gdx.input.setInputProcessor(getInputMultiplexer());

        //catch the back key as well
        Gdx.input.setCatchKey(Input.Keys.BACK, true);
    }

    protected Skin getSkin() {
        return this.skin;
    }

    protected Stage getStage() {
        return this.stage;
    }

    @Override
    public void dispose() {

        //call parent
        super.dispose();

        if (skin != null)
            skin.dispose();
        if (stage != null)
            stage.dispose();

        skin = null;
        stage = null;
    }

    private void addGameServiceIcons(Table parent) {

        //if null, don't add buttons
        if (getGame().getGsClient() == null)
            return;

        //only add for android mobile
        if (Gdx.app.getType() != Application.ApplicationType.Android)
            return;

        //create our buttons
        ImageButton buttonAchievement = new ImageButton(new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal(PATH_SOCIAL_ACHIEVEMENT)))));
        ImageButton buttonLeaderBoard = new ImageButton(new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal(PATH_SOCIAL_LEADERBOARD)))));

        //Add listeners to buttons
        buttonAchievement.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                try {
                    getGame().getGsClient().showAchievements();
                } catch (GameServiceException e) {
                    e.printStackTrace();
                }
            }
        });

        buttonLeaderBoard.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                try {
                    getGame().getGsClient().showLeaderboards(null);
                } catch (GameServiceException e) {
                    e.printStackTrace();
                }
            }
        });

        //add buttons to the child table
        parent.add(buttonAchievement).width(SOCIAL_ICON_SIZE).height(SOCIAL_ICON_SIZE).pad(SOCIAL_ICON_PADDING);
        parent.add(buttonLeaderBoard).width(SOCIAL_ICON_SIZE).height(SOCIAL_ICON_SIZE).pad(SOCIAL_ICON_PADDING);
    }

    public void addSocialIcons() {

        //Create Table
        Table table = new Table();

        //Set table to fill stage
        table.setFillParent(true);

        //Set alignment of contents in the table.
        table.bottom();

        //Create buttons
        ImageButton buttonTwitter = new ImageButton(new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal(PATH_SOCIAL_TWITTER)))));
        ImageButton buttonYoutube = new ImageButton(new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal(PATH_SOCIAL_YOUTUBE)))));
        ImageButton buttonFacebook = new ImageButton(new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal(PATH_SOCIAL_FACEBOOK)))));
        ImageButton buttonInstagram = new ImageButton(new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal(PATH_SOCIAL_INSTAGRAM)))));

        //Add listeners to buttons
        buttonTwitter.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) { Gdx.net.openURI(URL_TWITTER); }
        });

        buttonYoutube.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) { Gdx.net.openURI(URL_YOUTUBE); }
        });

        buttonFacebook.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) { Gdx.net.openURI(URL_FACEBOOK); }
        });

        buttonInstagram.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) { Gdx.net.openURI(URL_INSTAGRAM); }
        });

        //Add buttons to table
        table.add(buttonYoutube).width(SOCIAL_ICON_SIZE).height(SOCIAL_ICON_SIZE).pad(SOCIAL_ICON_PADDING);
        table.add(buttonInstagram).width(SOCIAL_ICON_SIZE).height(SOCIAL_ICON_SIZE).pad(SOCIAL_ICON_PADDING);
        table.add(buttonFacebook).width(SOCIAL_ICON_SIZE).height(SOCIAL_ICON_SIZE).pad(SOCIAL_ICON_PADDING);
        table.add(buttonTwitter).width(SOCIAL_ICON_SIZE).height(SOCIAL_ICON_SIZE).pad(SOCIAL_ICON_PADDING);

        //add google play icons (if valid ...)
        addGameServiceIcons(table);

        //Add table to stage
        getStage().addActor(table);
    }
}