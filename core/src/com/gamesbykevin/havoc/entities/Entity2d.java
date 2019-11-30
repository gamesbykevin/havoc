package com.gamesbykevin.havoc.entities;

import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g3d.decals.DecalBatch;
import com.gamesbykevin.havoc.animation.SpriteAnimation;

public abstract class Entity2d extends Entity {

    private SpriteAnimation[] animations;

    public Entity2d(int count) {
        super();
        this.animations = new SpriteAnimation[count];
    }

    protected SpriteAnimation[] getAnimations() {
        return this.animations;
    }

    public SpriteAnimation getAnimation() {
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
    public void render(PerspectiveCamera camera, DecalBatch decalBatch, Batch batch) {
        batch.draw(getAnimation().getImage(), getCol(), getRow());
    }
}