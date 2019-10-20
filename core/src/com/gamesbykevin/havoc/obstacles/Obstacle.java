package com.gamesbykevin.havoc.obstacles;

import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.gamesbykevin.havoc.animation.DecalAnimation;
import com.gamesbykevin.havoc.entities.Entity;

public final class Obstacle extends Entity {

    //where the sprite image is located
    public static final String ASSET_DIR = "obstacles/%s.bmp";

    //the type of obstacle
    public static Obstacles.Type TYPE;

    //each obstacle has a single animation sprite
    private static final int SPRITES = 1;

    protected Obstacle() {

        super(SPRITES);

        switch (TYPE) {
            case GreenC:
            case RedC:
            case Chandelier:
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
    public void update(PerspectiveCamera camera3d) {
        //do we need to do anything here
    }

    @Override
    public void createAnimations() {

        //animations are a single frame
        getAnimations()[0] = new DecalAnimation(String.format(ASSET_DIR, TYPE.toString()));
    }
}