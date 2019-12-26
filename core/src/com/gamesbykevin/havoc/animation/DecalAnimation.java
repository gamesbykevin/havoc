package com.gamesbykevin.havoc.animation;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.decals.Decal;
import com.badlogic.gdx.graphics.g3d.decals.DecalBatch;
import com.badlogic.gdx.math.Vector3;

public class DecalAnimation extends Animation {

    private Decal[] decals;

    public static final float DEFAULT_WIDTH = 1.0f;
    public static final float DEFAULT_HEIGHT = 1.0f;

    //defaults when adding a single frame
    private static final int NO_COLS = -1;
    private static final int NO_ROWS = -1;
    public static final int DEFAULT_INCREMENT = 1;
    public static final int DEFAULT_COUNT = 1;
    public static final float DEFAULT_DURATION = 0;

    public static final boolean BILLBOARD_ENABLED = true;
    public static final boolean BILLBOARD_DISABLED = false;

    //do we render facing us?
    private boolean billboard = false;

    //adding a single frame animation
    public DecalAnimation(AssetManager assetManager, String path, int width, int height, int startCol, int startRow, boolean billboard) {
        this(assetManager, path, NO_COLS, NO_ROWS, width, height, startCol, startRow, DEFAULT_INCREMENT, DEFAULT_COUNT, DEFAULT_DURATION, DEFAULT_WIDTH, DEFAULT_HEIGHT, billboard);
    }

    //adding a single frame animation
    public DecalAnimation(AssetManager assetManager, String path, int width, int height, float decalWidth, float decalHeight, boolean billboard) {
        this(assetManager, path, NO_COLS, NO_ROWS, width, height, 0, 0, DEFAULT_INCREMENT, DEFAULT_COUNT, DEFAULT_DURATION, decalWidth, decalHeight, billboard);
    }

    public DecalAnimation(AssetManager assetManager, String path, int cols, int rows, int width, int height, int startCol, int startRow, int increment, int count, float duration, float decalWidth, float decalHeight, boolean billboard) {

        super(count, duration);

        setBillboard(billboard);

        //create our array
        this.decals = new Decal[count];

        Texture texture = assetManager.get(path, Texture.class);

        int index = 0;

        if (cols == NO_COLS || rows == NO_ROWS) {

            //add single animation
            addDecal(texture, startCol, startRow, width, height, index, decalWidth, decalHeight);

        } else {

            int skip = increment;

            //load the animations within this texture
            for (int row = startRow; row < rows; row++) {
                for (int col = startCol; col < cols; col++) {

                    if (index >= getDecals().length)
                        break;

                    if (skip >= increment) {
                        skip = 1;
                    } else {
                        skip++;
                        continue;
                    }

                    addDecal(texture, col, row, width, height, index, decalWidth, decalHeight);

                    //move to the next index
                    index++;
                }

                //now that we are at the end, we need to start back at 0
                startCol = 0;
            }
        }
    }

    private void addDecal(Texture texture, int col, int row, int width, int height, int index, float decalWidth, float decalHeight) {

        //which part of the texture are we interested in
        int x = col * width;
        int y = row * height;

        //create our specific texture region from a single texture
        TextureRegion textureRegion = new TextureRegion(texture, x, y, width, height);

        //add it to the array as a decal
        getDecals()[index] = Decal.newDecal(decalWidth, decalHeight, textureRegion, true);
    }

    public boolean isBillboard() {
        return this.billboard;
    }

    public void setBillboard(boolean billboard) {
        this.billboard = billboard;
    }

    public void setPosition(Vector3 position) {
        setPosition(position.x, position.y, position.z);
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
        return getDecal(getIndex());
    }

    public Decal getDecal(int index) {
        return getDecals()[index];
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

    public void render(PerspectiveCamera camera, DecalBatch decalBatch) {

        //render always facing us?
        if (isBillboard())
            getDecal().lookAt(camera.position, camera.up);

        //add to batch
        decalBatch.add(getDecal());
    }
}