package com.gamesbykevin.havoc.entities;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g3d.decals.DecalBatch;
import com.gamesbykevin.havoc.animation.DecalAnimation;

public abstract class Entity3d extends Entity {

    //array of animations for our entity
    private DecalAnimation[] animations;

    public Entity3d(int count) {
        super();
        this.animations = new DecalAnimation[count];
    }

    private DecalAnimation[] getAnimations() {
        return this.animations;
    }

    public void setAnimation(int index, DecalAnimation decalAnimation) {
        getAnimations()[index] = decalAnimation;
    }

    public DecalAnimation getAnimation() {
        return getAnimations()[getIndex()];
    }

    @Override
    public void dispose() {

        if (this.animations != null) {
            for (int i = 0; i < this.animations.length; i++) {
                if (this.animations[i] != null) {
                    this.animations[i].dispose();
                    this.animations[i] = null;
                }
            }
        }

        this.animations = null;
    }

    @Override
    public void render(AssetManager assetManager, PerspectiveCamera camera, DecalBatch decalBatch, Batch batch) {

        //render the entity
        getAnimation().render(camera, decalBatch);
    }
}