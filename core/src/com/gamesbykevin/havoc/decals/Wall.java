package com.gamesbykevin.havoc.decals;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Wall extends DecalCustom {

    public Wall(TextureRegion texture, Side side) {
        super(texture, Type.Wall, side);
    }

    @Override
    public void update() {

    }
}