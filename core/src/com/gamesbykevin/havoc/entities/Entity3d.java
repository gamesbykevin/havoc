package com.gamesbykevin.havoc.entities;

import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g3d.decals.DecalBatch;
import com.gamesbykevin.havoc.animation.DecalAnimation;

import static com.gamesbykevin.havoc.level.Level.RENDER_RANGE;

public abstract class Entity3d extends Entity {

    //array of animations for our entity
    private DecalAnimation[] animations;

    public Entity3d(int count) {
        super();
        this.animations = new DecalAnimation[count];
    }

    protected DecalAnimation[] getAnimations() {
        return this.animations;
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
    public void render(PerspectiveCamera camera, DecalBatch decalBatch, Batch batch) {

        //render like a billboard
        getAnimation().getDecal().lookAt(camera.position, camera.up);

        //add to batch
        decalBatch.add(getAnimation().getDecal());
    }
}