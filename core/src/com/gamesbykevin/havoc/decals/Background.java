package com.gamesbykevin.havoc.decals;

import com.gamesbykevin.havoc.animation.DecalAnimation;

public class Background extends DecalCustom {

    public static final float TEXTURE_WIDTH = 16;
    public static final float TEXTURE_HEIGHT = 16;

    public static final float HEIGHT_CEILING = .5f;
    public static final float HEIGHT_FLOOR = -.5f;

    protected Background(DecalAnimation animation) {
        super(Side.None, animation);
    }

    @Override
    public void update() {

        //update animation
        getAnimation().update();
    }

    @Override
    public void reset() {
        //call parent
        super.reset();
    }
}