package com.gamesbykevin.havoc.animation;

import com.badlogic.gdx.Gdx;
import com.gamesbykevin.havoc.util.Disposable;
import com.gamesbykevin.havoc.util.Timer;

public abstract class Animation implements Disposable {

    //current animation
    private int index;

    //do we repeat the animation
    private boolean loop = false;

    //did the animation finish
    private boolean finish = false;

    //keep track of total
    private final int count;

    //timer to tell us when to switch frames
    private Timer timer;

    public Animation(int count, float duration) {

        this.count = count;
        this.timer = new Timer(duration);

        setIndex(0);
        setLoop(false);
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

    public Timer getTimer() {
        return this.timer;
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
        getTimer().reset();
    }

    public void update() {

        //no need to update if finished
        if (isFinish())
            return;

        //track the amount of time elapsed
        getTimer().update();

        //don't continue if time hasn't expired yet
        if (!getTimer().isExpired())
            return;

        //reset the timer
        getTimer().reset();

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