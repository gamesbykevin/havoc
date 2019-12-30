package com.gamesbykevin.havoc.graphics;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.gamesbykevin.havoc.util.Disposable;
import com.gamesbykevin.havoc.util.Restart;
import com.gamesbykevin.havoc.util.Timer;

import static com.gamesbykevin.havoc.screen.ParentScreen.SCREEN_HEIGHT;
import static com.gamesbykevin.havoc.screen.ParentScreen.SCREEN_WIDTH;
import static com.gamesbykevin.havoc.util.Language.getMyBundle;

public class Overlay implements Restart, Disposable {

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

    //text to display
    private final String text;

    //do we display the overlay
    private boolean display = true;

    //timer for the overlay
    private Timer timer;

    //what is the font size
    public static final float FONT_SIZE = 1.0f;

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

        //font will be white
        getBitmapFont().setColor(1, 1, 1, 1);

        //change font size
        getBitmapFont().getData().setScale(FONT_SIZE);

        //create our layout so we can calculate the font metrics
        GlyphLayout glyphLayout = new GlyphLayout(getBitmapFont(), text);

        //reference the font metrics so we can place our text in the middle of the screen
        setTextX((SCREEN_WIDTH / 2) - (glyphLayout.width / 2));
        setTextY((SCREEN_HEIGHT / 2) - (glyphLayout.height / 2));

        glyphLayout = null;

        //default true
        setDisplay(true);

        //store our text
        this.text = text;

        //create our timer
        this.timer = new Timer(duration);
    }

    public Timer getTimer() {
        return this.timer;
    }

    public String getText() {
        return this.text;
    }

    public boolean isDisplay() {
        return display;
    }

    public void setDisplay(boolean display) {
        this.display = display;
    }

    private BitmapFont getBitmapFont() {

        if (this.bitmapFont == null)
            this.bitmapFont = new BitmapFont();

        return this.bitmapFont;
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

    @Override
    public void reset() {
        getTimer().reset();
    }

    @Override
    public void dispose() {

        if (pixelMapTexture != null)
            pixelMapTexture.dispose();
        if (bitmapFont != null)
            bitmapFont.dispose();

        this.pixelMapTexture = null;
        this.bitmapFont = null;
        this.timer = null;
    }

    public void draw(SpriteBatch batch) {

        //draw overlay
        if (isDisplay()) {

            //render the overlay
            batch.draw(getPixelMapTexture(), 0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);

            //render the text
            getBitmapFont().draw(batch, getText(), getTextX(), getTextY());

            //if we specified a duration let's see if time expired
            if (getTimer().getDuration() > 0) {

                //if time ran out, hide the overlay
                if (getTimer().isExpired())
                    setDisplay(false);

                //update the timer
                getTimer().update();
            }
        }
    }
}