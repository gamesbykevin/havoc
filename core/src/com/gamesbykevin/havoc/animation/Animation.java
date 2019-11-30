package com.gamesbykevin.havoc.animation;

import com.badlogic.gdx.Gdx;
import com.gamesbykevin.havoc.util.Disposable;

public abstract class Animation implements Disposable {

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

    //did the animation finish
    private boolean finish = false;

    //keep track of total
    private final int count;

    public Animation(int count) {
        this(count, FRAME_DURATION_DEFAULT);
    }

    public Animation(int count, float duration) {

        this.count = count;

        setIndex(0);
        setLoop(false);
        setElapsed(0);
        setFrameDuration(duration);
    }

    public int getCount() {
        return this.count;
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

    public boolean isFinish() {
        return this.finish;
    }

    public void setFinish(boolean finish) {
        this.finish = finish;
    }

    public void reset() {
        setIndex(0);
        setFinish(false);
    }

    public void update() {

        //no need to update if finished
        if (isFinish())
            return;

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
        if (getIndex() >= getCount()) {

            //set to last frame
            setIndex(getCount() - 1);

            //reset the index if looping
            if (isLoop()) {
                reset();
            } else {
                setFinish(true);
            }
        }
    }
}