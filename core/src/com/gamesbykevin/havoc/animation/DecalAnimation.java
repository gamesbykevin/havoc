package com.gamesbykevin.havoc.animation;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.decals.Decal;

public class DecalAnimation extends Animation {

    private Decal[] decals;

    private static final float DEFAULT_WIDTH = 1.0f;
    private static final float DEFAULT_HEIGHT = 1.0f;

    public DecalAnimation(TextureRegion textureRegion) {
        super(1, 0);
        this.decals = new Decal[1];
        this.decals[0] = Decal.newDecal(DEFAULT_WIDTH, DEFAULT_HEIGHT, textureRegion, true);
    }

    public DecalAnimation(String path, String filename, String extension, int startIndex, int count, float duration) {

        super(count, duration);

        //create our array
        this.decals = new Decal[count];

        //load the textures
        for (int i = 0; i < getDecals().length; i++) {
            Texture texture = new Texture(Gdx.files.internal(path + filename + (startIndex + i) + extension));
            getDecals()[i] = Decal.newDecal(DEFAULT_WIDTH, DEFAULT_HEIGHT, new TextureRegion(texture), true);
        }
    }

    public void setPosition(float x, float y, float z) {

        for (int i = 0; i < getDecals().length; i++) {
            getDecals()[i].setPosition(x, y, z);
        }
    }

    private Decal[] getDecals() {
        return this.decals;
    }

    public Decal getDecal() {
        return getDecals()[getIndex()];
    }

    @Override
    public void dispose() {

        if (this.decals != null) {
            for (int i = 0; i < this.decals.length; i++) {
                if (this.decals[i] != null)
                    this.decals[i] = null;
            }
        }

        this.decals = null;
    }
}