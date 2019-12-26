package com.gamesbykevin.havoc.animation;

import java.awt.*;

public class SpriteAnimation extends Animation {

    //path to the texture image
    private String texturePath;

    //which coordinates from the image do we render
    private Rectangle[] coordinates;

    public SpriteAnimation(String path, int width, int height, float duration) {
        this(path, 1, 1, width, height, 0, 0, 1, 1, duration);
    }

    public SpriteAnimation(
            String texturePath,
            int cols,
            int rows,
            int width,
            int height,
            int startCol,
            int startRow,
            int increment,
            int count,
            float duration) {

        super(count, duration);

        //create our array to hold the render coordinates
        this.coordinates = new Rectangle[count];

        //store the path
        this.texturePath = texturePath;

        int index = 0;

        //if we need to skip every few images depending on where the sprites are on the sheet
        int skip = increment;

        //load the textures
        for (int row = startRow; row < rows; row++) {
            for (int col = startCol; col < cols; col++) {

                if (index >= getCoordinates().length)
                    break;

                if (skip >= increment) {
                    skip = 1;
                } else {
                    skip++;
                    continue;
                }

                int x = col * width;
                int y = row * height;

                //we now have our coordinates for this index
                getCoordinates()[index] = new Rectangle(x, y, width, height);

                //move to the next index
                index++;
            }

            //now that we are at the end, we need to start back at 0
            startCol = 0;
        }
    }

    public String getTexturePath() {
        return this.texturePath;
    }

    public Rectangle getCoordinate() {
        return getCoordinates()[getIndex()];
    }

    private Rectangle[] getCoordinates() {
        return this.coordinates;
    }

    @Override
    public void dispose() {
        this.texturePath = null;
        this.coordinates = null;
    }
}