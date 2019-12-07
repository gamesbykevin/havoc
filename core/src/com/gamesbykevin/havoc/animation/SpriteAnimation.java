package com.gamesbykevin.havoc.animation;

public class SpriteAnimation extends Animation {

    //paths to the textures
    private String[] texturePaths;

    public SpriteAnimation(String path, String filename, String extension, int startIndex, int count, float duration) {

        super(count, duration);

        //create our array
        this.texturePaths = new String[count];

        //load all the images
        for (int i = 0; i < getTexturePaths().length; i++) {
            getTexturePaths()[i] = path + filename + (startIndex + (i+1)) + extension;
        }
    }

    public String[] getTexturePaths() {
        return this.texturePaths;
    }

    public String getTexturePath() {
        return getTexturePaths()[getIndex()];
    }

    @Override
    public void dispose() {
        this.texturePaths = null;
    }
}