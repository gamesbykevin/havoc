package com.gamesbykevin.havoc.obstacles;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.decals.Decal;

import static com.gamesbykevin.havoc.decals.DecalCustom.TEXTURE_HEIGHT;
import static com.gamesbykevin.havoc.decals.DecalCustom.TEXTURE_WIDTH;
import static com.gamesbykevin.havoc.obstacles.Obstacles.DEFAULT_HEIGHT;
import static com.gamesbykevin.havoc.obstacles.Obstacles.DEFAULT_WIDTH;

public final class Obstacle {

    //the decal to render
    private Decal decal;

    //what type of obstacle is this?
    private final Obstacles.Type type;

    //where the sprite image is located
    public static final String ASSET_DIR = "obstacles/%s.bmp";

    //do we check for collision
    private final boolean solid;

    //location of the obstacle
    private float col, row;

    protected Obstacle(Obstacles.Type type, float x, float y) {

        //store the location
        this.col = x;
        this.row = y;

        //the type of obstacle
        this.type = type;

        switch (getType()) {
            case GreenC:
            case RedC:
            case Chandalier:
                solid = false;
                break;

            default:
                solid = true;
                break;
        }

        Texture texture = new Texture(Gdx.files.internal(String.format(ASSET_DIR, getType().toString())));
        this.decal = Decal.newDecal(DEFAULT_WIDTH, DEFAULT_HEIGHT, new TextureRegion(texture), true);
        getDecal().setPosition(x - (TEXTURE_WIDTH / 2), y - (TEXTURE_HEIGHT / 2), 0);
    }

    public Decal getDecal() {
        return this.decal;
    }

    public Obstacles.Type getType() {
        return this.type;
    }

    public boolean isSolid() {
        return this.solid;
    }

    public float getCol() {
        return this.col;
    }

    public float getRow() {
        return this.row;
    }
}