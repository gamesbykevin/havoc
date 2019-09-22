package com.gamesbykevin.havoc.animation;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public class SpriteAnimation {

    //list of images in our animation
    private Texture[] images;

    //current animation
    private int index;

    //how long to display each frame
    private float frameDuration;

    //default duration for each frame
    public static float FRAME_DURATION_DEFAULT = 100f;

    //how many milliseconds in 1 second
    public static float MILLISECONDS_PER_SECOND = 1000f;

    //how much time has elapsed
    private float elapsed;

    //do we repeat the animation
    private boolean loop = false;

    public SpriteAnimation(final String path, final String filename, final String extension, final int count) {

        //create our array of images
        this.images = new Texture[count];

        //load all the images
        for (int i = 0; i < this.images.length; i++) {
            this.images[i] = new Texture(Gdx.files.internal(path + filename + (i+1) + extension));
        }

        setIndex(0);
        setLoop(false);
        setElapsed(0);
        setFrameDuration(FRAME_DURATION_DEFAULT);
    }

    public int getIndex() {
        return this.index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public float getFrameDuration() {
        return this.frameDuration;
    }

    public void setFrameDuration(float frameDuration) {
        this.frameDuration = frameDuration;
    }

    public float getElapsed() {
        return this.elapsed;
    }

    public void setElapsed(float elapsed) {
        this.elapsed = elapsed;
    }

    public boolean isLoop() {
        return this.loop;
    }

    public void setLoop(boolean loop) {
        this.loop = loop;
    }

    private Texture[] getImages() {
        return this.images;
    }

    public Texture getImage() {
        return getImages()[getIndex()];
    }

    public void reset() {
        setIndex(0);
    }

    public void update() {

        //track the amount of time elapsed
        setElapsed(getElapsed() + (Gdx.graphics.getDeltaTime() * MILLISECONDS_PER_SECOND));

        //don't continue if not enough time lapsed
        if (getElapsed() < getFrameDuration())
            return;

        //reset the time elapsed
        setElapsed(0);

        //change frame
        setIndex(getIndex() + 1);

        //stay in bounds if at the end of our animation
        if (getIndex() >= getImages().length) {

            //set to last frame
            setIndex(getImages().length - 1);

            //reset the index if looping
            if (isLoop())
                reset();
        }
    }
}