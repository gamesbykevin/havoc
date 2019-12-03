package com.gamesbykevin.havoc.collectables;

import com.gamesbykevin.havoc.animation.DecalAnimation;
import com.gamesbykevin.havoc.entities.Entity3d;
import com.gamesbykevin.havoc.level.Level;

import static com.gamesbykevin.havoc.texture.TextureHelper.PARENT_DIR_IMAGES;

public final class Collectible extends Entity3d {

    //where the sprite image is located
    public static final String ASSET_DIR = PARENT_DIR_IMAGES + "collect/";

    public static final String ASSET_EXT = ".bmp";

    private final Collectibles.Type type;

    //each obstacle has a single animation sprite
    private static final int SPRITES = 1;

    public Collectible(Collectibles.Type type) {

        //call parent
        super(SPRITES);

        //default solid to true
        setSolid(true);

        //store the type of collectible
        this.type = type;

        //animations are a single frame
        getAnimations()[0] = new DecalAnimation(Collectibles.getTextures().get(type));
    }

    public Collectibles.Type getType() {
        return this.type;
    }

    @Override
    public void reset() {
        setSolid(true);
        setCol(getStartCol());
        setRow(getStartRow());
        getAnimation().reset();
    }

    @Override
    public void update(Level level) {
        //don't do anything here?
    }
}