package com.gamesbykevin.havoc.obstacles;

import com.gamesbykevin.havoc.animation.DecalAnimation;
import com.gamesbykevin.havoc.entities.Entity;
import com.gamesbykevin.havoc.level.Level;

public final class Obstacle extends Entity {

    //where the sprite image is located
    public static final String ASSET_DIR = "obstacles/";

    public static final String ASSET_EXT = ".bmp";

    //the type of obstacle
    public static Obstacles.Type TYPE;

    //each obstacle has a single animation sprite
    private static final int SPRITES = 1;

    protected Obstacle() {

        super(SPRITES);

        switch (TYPE) {
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
    }

    @Override
    public void reset() {
        //do we need to do anything here
    }

    @Override
    public void update(Level level) {
        //do we need to do anything here
    }

    @Override
    public void createAnimations() {

        //animations are a single frame
        getAnimations()[0] = new DecalAnimation(ASSET_DIR + TYPE.toString() + ASSET_EXT);
    }
}