package com.gamesbykevin.havoc.collectables;

import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.gamesbykevin.havoc.animation.DecalAnimation;
import com.gamesbykevin.havoc.entities.Entity;

public final class Collectible extends Entity {

    //where the sprite image is located
    public static final String ASSET_DIR = "collect/%s.bmp";

    //the type of obstacle
    public static Collectibles.Type TYPE;

    private final Collectibles.Type type;

    //each obstacle has a single animation sprite
    private static final int SPRITES = 1;

    public Collectible(Collectibles.Type type) {

        //call parent
        super(SPRITES);

        //store the type of collectible
        this.type = type;
    }

    public Collectibles.Type getType() {
        return this.type;
    }

    @Override
    public void reset() {
        setSolid(true);
    }

    @Override
    public void update(PerspectiveCamera camera3d) {
        //don't do anything here
    }

    @Override
    public void createAnimations() {

        //animations are a single frame
        getAnimations()[0] = new DecalAnimation(String.format(ASSET_DIR, TYPE.toString()));
    }
}
