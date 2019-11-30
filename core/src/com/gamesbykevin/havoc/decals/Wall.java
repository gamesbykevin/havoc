package com.gamesbykevin.havoc.decals;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Wall extends DecalCustom {

    protected Wall(TextureRegion texture, Side side) {
        super(texture, Type.Wall, side, TEXTURE_WIDTH, TEXTURE_HEIGHT);
    }

    @Override
    public void update() {

    }
}