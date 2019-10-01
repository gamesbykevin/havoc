package com.gamesbykevin.havoc.decals;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Background extends DecalCustom {

    public Background(TextureRegion texture, Side side) {
        super(texture, Type.Background, side);
    }

    @Override
    public void update() {

    }

    public static Background createDecalBackground(float col, float row, TextureRegion texture, boolean floor) {
        Background decal = new Background(texture, null);
        decal.setCol((int)col);
        decal.setRow((int)row);
        decal.getDecal().setPosition(col, row, (floor) ? -.5f : .5f);
        return decal;
    }
}