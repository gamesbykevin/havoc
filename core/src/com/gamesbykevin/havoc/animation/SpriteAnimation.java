package com.gamesbykevin.havoc.animation;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

import static com.gamesbykevin.havoc.animation.Animation.FRAME_DURATION_DEFAULT;

public class SpriteAnimation extends Animation {

    //list of images in our animation
    private Texture[] images;

    public SpriteAnimation(final String path, final String filename, final String extension, final int startIndex, final int count) {
        this(path, filename, extension, startIndex, count, FRAME_DURATION_DEFAULT);
    }

    public SpriteAnimation(final String path, final String filename, final String extension, final int startIndex, final int count, final float duration) {

        super(count, duration);

        //create our array of images
        this.images = new Texture[count];

        //load all the images
        for (int i = 0; i < this.images.length; i++) {
            this.images[i] = new Texture(Gdx.files.internal(path + filename + (startIndex + (i+1)) + extension));
        }

    }

    private Texture[] getImages() {
        return this.images;
    }

    public Texture getImage() {
        return getImages()[getIndex()];
    }

}