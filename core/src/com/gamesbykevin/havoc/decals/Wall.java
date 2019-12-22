package com.gamesbykevin.havoc.decals;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Wall extends DecalCustom {

    //each side of the wall
    public static final int SIDE_NORTH = 0;
    public static final int SIDE_SOUTH = 1;
    public static final int SIDE_WEST = 2;
    public static final int SIDE_EAST = 3;

    protected Wall(TextureRegion texture, int side) {
        super(texture, side, TEXTURE_WIDTH, TEXTURE_HEIGHT);
    }

    @Override
    public void update() {
        //do anything here?
    }
}