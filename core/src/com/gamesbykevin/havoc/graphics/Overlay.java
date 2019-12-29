package com.gamesbykevin.havoc.graphics;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import static com.gamesbykevin.havoc.GameMain.FRAME_MS;
import static com.gamesbykevin.havoc.screen.ParentScreen.SCREEN_HEIGHT;
import static com.gamesbykevin.havoc.screen.ParentScreen.SCREEN_WIDTH;
import static com.gamesbykevin.havoc.util.Language.getMyBundle;

public class Overlay {

    //how visible is our overlay
    public static final float OVERLAY_TRANSPARENCY_PAUSED = .6f;

    //what text to display
    public static final String OVERLAY_TEXT_PAUSED = getMyBundle().get("overlayPaused");

    //texture for a single black pixel used for our overlay
    private Texture pixelMapTexture;

    //font to draw text on screen
    private BitmapFont bitmapFont;

    //where do we render the text
    private float textX, textY;

    //font metrics
    private GlyphLayout glyphLayout;

    //do we display the overlay
    private boolean display = true;

    //how much time has lapsed
    private long lapsed;

    //what is our time limit
    private long duration;

    public Overlay(final String text, final float transparency) {
        this(text, transparency, -1);
    }

    public Overlay(final String text, final float transparency, final long duration) {

        //create a single pixel
        Pixmap pixmap = new Pixmap( 1, 1, Pixmap.Format.RGBA8888 );
        pixmap.setColor( 0, 0, 0, transparency);
        pixmap.fillRectangle(0, 0, 1, 1);

        //create new texture referencing the pixel
        this.pixelMapTexture = new Texture( pixmap );

        //we no longer need this
        pixmap.dispose();
        pixmap = null;

        //create our bitmap font object
        this.bitmapFont = new BitmapFont();

        //font will be white
        this.bitmapFont.setColor(1, 1, 1, 1);

        //change font size
        this.bitmapFont.getData().setScale(1.25f);

        //create our layout so we can calculate the font metrics
        this.glyphLayout = new GlyphLayout(this.bitmapFont, text);

        //reference the font metrics so we can place our text in the middle of the screen
        setTextX((SCREEN_WIDTH / 2) - (getGlyphLayout().width / 2));
        setTextY((float)(SCREEN_HEIGHT * .75) - (getGlyphLayout().height / 2));

        //default true
        setDisplay(true);

        //unlimited duration
        setDuration(duration);
    }

    public long getLapsed() {
        return this.lapsed;
    }

    public void setLapsed(long lapsed) {
        this.lapsed = lapsed;
    }

    public long getDuration() {
        return this.duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public boolean isDisplay() {
        return display;
    }

    public void setDisplay(boolean display) {
        this.display = display;
    }

    private BitmapFont getBitmapFont() {
        return this.bitmapFont;
    }

    private GlyphLayout getGlyphLayout() {
        return this.glyphLayout;
    }

    public void setTextX(float textX) {
        this.textX = textX;
    }

    public void setTextY(float textY) {
        this.textY = textY;
    }

    public float getTextX() {
        return this.textX;
    }

    public float getTextY() {
        return this.textY;
    }

    private Texture getPixelMapTexture() {
        return this.pixelMapTexture;
    }

    public void reset() {
        setLapsed(0);
    }

    public void draw(SpriteBatch batch) {

        //draw overlay
        if (isDisplay()) {

            batch.draw(getPixelMapTexture(), 0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
            getBitmapFont().draw(batch, getGlyphLayout(), getTextX(), getTextY());

            if (getDuration() > 0) {
                setLapsed((long)(FRAME_MS + getLapsed()));

                if (getLapsed() >= getDuration())
                    setDisplay(false);
            }
        }
    }

    public void dispose() {

        if (pixelMapTexture != null)
            pixelMapTexture.dispose();
        if (bitmapFont != null)
            bitmapFont.dispose();

        this.pixelMapTexture = null;
        this.bitmapFont = null;
        this.glyphLayout = null;
    }
}