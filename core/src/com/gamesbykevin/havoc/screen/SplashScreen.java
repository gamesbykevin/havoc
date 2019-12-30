package com.gamesbykevin.havoc.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.gamesbykevin.havoc.MyGdxGame;

import java.util.Date;

import static com.gamesbykevin.havoc.GameEngine.getSizeWidth;
import static com.gamesbykevin.havoc.screen.ScreenHelper.SCREEN_MENU;

public class SplashScreen extends TemplateScreen {

    //track how much time lapsed
    private Date date;

    //how long to display the screen
    private static final long DURATION_DELAY = 2000L;

    //our screen images
    private Texture website, loading;

    //how to render our textures
    private float websiteX, websiteY, websiteWidth, websiteHeight;
    private float loadingX, loadingY, loadingWidth, loadingHeight;

    public static final String PATH_LOADING = PATH_PARENT_DIR + "loading.png";
    public static final String PATH_WEBSITE = PATH_PARENT_DIR + "website.png";

    //provide ourselves space
    public static final int PADDING_VERTICAL = 25;
    public static final int PADDING_HORIZONTAL = 50;

    public SplashScreen(MyGdxGame game) {

        //call parent
        super(game);

        //flag false
        MyGdxGame.EXIT = false;

        //create our background
        this.website = new Texture(Gdx.files.internal(PATH_WEBSITE));
        this.loading = new Texture(Gdx.files.internal(PATH_LOADING));

        float ratio;

        //adjust if texture is bigger than the screen
        if (getWebsite().getWidth() > getSizeWidth()) {
            ratio = (float)getSizeWidth() / (float)getWebsite().getWidth();
            setWebsiteWidth(getSizeWidth() - (PADDING_HORIZONTAL * 2));
            setWebsiteHeight(getWebsite().getHeight() * ratio);
        }

        //adjust if texture is bigger than the screen
        if (getLoading().getWidth() > getSizeWidth()) {
            ratio = (float)getSizeWidth() / (float)getLoading().getWidth();
            setLoadingWidth(getSizeWidth() - (PADDING_HORIZONTAL * 2));
            setLoadingHeight(getLoading().getHeight() * ratio);
        }

        //position our image
        setLoadingX((SCREEN_WIDTH / 2) - (getLoadingWidth() / 2));
        setLoadingY((SCREEN_HEIGHT / 2) - ((getLoadingHeight() + getWebsiteHeight() + PADDING_VERTICAL) / 2));

        //position our image
        setWebsiteX((SCREEN_WIDTH / 2) - (getWebsiteWidth() / 2));
        setWebsiteY(getLoadingY() + getLoadingHeight() + PADDING_VERTICAL);
    }

    @Override
    public void resume() {

        //call parent
        super.resume();
    }

    @Override
    public void dispose() {

        //call parent
        super.dispose();

        if (this.website != null) {
            this.website.dispose();
            this.website = null;
        }

        if (this.loading != null) {
            this.loading.dispose();
            this.loading = null;
        }
    }

    public Texture getWebsite() {
        return this.website;
    }

    public Texture getLoading() {
        return this.loading;
    }

    public Date getDate() {

        if (this.date == null)
            this.date = new Date();

        return this.date;
    }

    @Override
    public void render(float delta) {

        //call parent
        super.render(delta);

        //draw the splash screen
        drawBackground();

        //draw our images
        getBatch().draw(getWebsite(), getWebsiteX(), getWebsiteY(), getWebsiteWidth(), getWebsiteHeight());
        getBatch().draw(getLoading(), getLoadingX(), getLoadingY(), getLoadingWidth(), getLoadingHeight());

        //we are done rendering items
        getBatch().end();

        //when enough time has passed go to the next screen
        if (new Date().getTime() - getDate().getTime() >= DURATION_DELAY) {

            //now we can go to our menu screen
            getGame().getScreenHelper().changeScreen(SCREEN_MENU);
        }
    }

    public float getWebsiteX() {
        return this.websiteX;
    }

    public float getWebsiteY() {
        return this.websiteY;
    }

    public float getLoadingX() {
        return this.loadingX;
    }

    public float getLoadingY() {
        return this.loadingY;
    }

    public void setWebsiteX(float websiteX) {
        this.websiteX = websiteX;
    }

    public void setWebsiteY(float websiteY) {
        this.websiteY = websiteY;
    }

    public void setLoadingX(float loadingX) {
        this.loadingX = loadingX;
    }

    public void setLoadingY(float loadingY) {
        this.loadingY = loadingY;
    }

    public float getWebsiteWidth() {
        return this.websiteWidth;
    }

    public void setWebsiteWidth(float websiteWidth) {
        this.websiteWidth = websiteWidth;
    }

    public float getWebsiteHeight() {
        return this.websiteHeight;
    }

    public void setWebsiteHeight(float websiteHeight) {
        this.websiteHeight = websiteHeight;
    }

    public float getLoadingWidth() {
        return this.loadingWidth;
    }

    public void setLoadingWidth(float loadingWidth) {
        this.loadingWidth = loadingWidth;
    }

    public float getLoadingHeight() {
        return this.loadingHeight;
    }

    public void setLoadingHeight(float loadingHeight) {
        this.loadingHeight = loadingHeight;
    }
}