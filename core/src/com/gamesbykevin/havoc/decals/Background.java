package com.gamesbykevin.havoc.decals;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Background extends DecalCustom {

    public static final float TEXTURE_WIDTH = 16;
    public static final float TEXTURE_HEIGHT = 16;

    public static final int SIDE_NONE = -1;

    protected Background(TextureRegion texture) {
        super(texture, SIDE_NONE, TEXTURE_WIDTH, TEXTURE_HEIGHT);
    }

    @Override
    public void update() {
        //do we need to update anything here?
    }

    public static Background createDecalBackground(float col, float row, TextureRegion texture, boolean floor) {
        Background decal = new Background(texture);
        decal.setCol((int)col);
        decal.setRow((int)row);
        decal.getDecal().setPosition(col, row, (floor) ? -.5f : .5f);
        return decal;
    }
}