package com.gamesbykevin.havoc.obstacles;

import com.gamesbykevin.havoc.animation.DecalAnimation;
import com.gamesbykevin.havoc.entities.Entity;
import com.gamesbykevin.havoc.entities.Entity3d;
import com.gamesbykevin.havoc.level.Level;

public final class Obstacle extends Entity3d {

    //where the sprite image is located
    public static final String ASSET_DIR = "obstacles/";

    public static final String ASSET_EXT = ".bmp";

    //each obstacle has a single animation sprite
    private static final int SPRITES = 1;

    protected Obstacle(Obstacles.Type type) {

        super(SPRITES);

        switch (type) {
            case Light1:
            case Light2:
            case Light3:
            case Light4:
            case Light5:
            case Light6:
            case Light7:
                setSolid(false);
                break;

            default:
                setSolid(true);
                break;
        }

        //animations are a single frame
        getAnimations()[0] = new DecalAnimation(Obstacles.getTextures().get(type));
    }

    @Override
    public void reset() {
        //do we need to do anything here
    }

    @Override
    public void update(Level level) {
        //do we need to do anything here
    }
}