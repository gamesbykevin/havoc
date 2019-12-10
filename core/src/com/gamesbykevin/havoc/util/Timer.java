package com.gamesbykevin.havoc.util;

import com.badlogic.gdx.Gdx;

public class Timer implements Restart {

    //how much time has passed
    private float lapsed = 0;

    //how long is the timer
    private float duration = 0;

    //how many milliseconds in 1 second
    public static float MILLISECONDS_PER_SECOND = 1000f;

    public Timer(float duration) {
        setDuration(duration);
        reset();
    }

    @Override
    public void reset() {
        setLapsed(0);
    }

    public float getLapsed() {
        return this.lapsed;
    }

    public void setLapsed(float lapsed) {
        this.lapsed = lapsed;
    }

    public float getDuration() {
        return this.duration;
    }

    public void setDuration(float duration) {
        this.duration = duration;
    }

    public void update() {
        setLapsed(getLapsed() + (Gdx.graphics.getDeltaTime() * MILLISECONDS_PER_SECOND));
    }

    public boolean isExpired() {
        return (getLapsed() >= getDuration());
    }
}