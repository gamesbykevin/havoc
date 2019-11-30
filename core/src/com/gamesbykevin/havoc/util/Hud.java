package com.gamesbykevin.havoc.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;

import static com.gamesbykevin.havoc.MyGdxGame.SIZE_HEIGHT;
import static com.gamesbykevin.havoc.MyGdxGame.SIZE_WIDTH;
import static com.gamesbykevin.havoc.player.PlayerHelper.*;
import static com.gamesbykevin.havoc.player.PlayerHelper.HUD_KEY_HEIGHT;

public class Hud {

    public static final int HUD_NUMBER_WIDTH = (int)(58 * HUD_RATIO);
    public static final int HUD_NUMBER_HEIGHT = (int)(70 * HUD_RATIO);

    public static final int HUD_NUMBER_PAD = (int)(5 * HUD_RATIO);
    public static final int HUD_BULLET_X = SIZE_WIDTH - (3 * HUD_NUMBER_WIDTH) - (3 * HUD_NUMBER_PAD);
    public static final int HUD_BULLET_Y = SIZE_HEIGHT - (HUD_NUMBER_HEIGHT);
    public static final int HUD_HEALTH_X = (3 * HUD_NUMBER_PAD);
    public static final int HUD_HEALTH_Y = SIZE_HEIGHT - HUD_NUMBER_HEIGHT - (HUD_NUMBER_PAD * 2);

    //array of numbers
    private static Texture[] NUMBERS;

    //load the textures
    public static void load() {

        //create our array of number images
        NUMBERS = new Texture[13];

        //load our textures
        for (int i = 0; i < NUMBERS.length; i++) {

            if (i < 10) {
                NUMBERS[i] = new Texture(Gdx.files.internal("hud/" + i + ".png"));
            } else if (i == 10) {
                NUMBERS[i] = new Texture(Gdx.files.internal("hud/key_1_small.png"));
            } else if (i == 11) {
                NUMBERS[i] = new Texture(Gdx.files.internal("hud/key_2_small.png"));
            } else if (i == 12) {
                NUMBERS[i] = new Texture(Gdx.files.internal("hud/percent.png"));
            }
        }
    }

    public static final void renderNumber(Batch batch, final int number, int renderX, int renderY, int width, int height, int padding) {

        if (NUMBERS == null)
            Hud.load();

        float x = renderX;

        for (int i = 0; i < 4; i++) {

            int index = 12;

            switch (i) {
                case 0:
                    index = (number / 100);
                    break;

                case 1:
                    index = (number % 100) / 10;
                    break;

                case 2:
                    index = (number % 10);
                    break;
            }

            //if less than 0 we display as 999
            if (number < 0)
                index = 9;

            //render the number
            batch.draw(NUMBERS[index], x, renderY, width, height);

            //move to the next coordinate
            x += width + padding;
        }
    }

    public static void renderKey(Batch batch) {
        batch.draw(NUMBERS[10], HUD_HEALTH_X, HUD_HEALTH_Y - (HUD_KEY_HEIGHT * 1), HUD_KEY_WIDTH, HUD_KEY_HEIGHT);
    }
}