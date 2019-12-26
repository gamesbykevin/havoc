package com.gamesbykevin.havoc.decals;

import com.gamesbykevin.havoc.animation.DecalAnimation;

public class Wall extends DecalCustom {

    protected Wall(Side side, DecalAnimation animation) {
        super(side, animation);
    }

    @Override
    public void update() {

        //update animation
        getAnimation().update();

        if (getAnimation().isFinish())
            getAnimation().reset();
    }

    @Override
    public void reset() {
        //call parent
        super.reset();
    }
}