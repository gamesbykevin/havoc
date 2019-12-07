package com.gamesbykevin.havoc.util;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;

import static com.gamesbykevin.havoc.MyGdxGame.SIZE_HEIGHT;
import static com.gamesbykevin.havoc.MyGdxGame.SIZE_WIDTH;
import static com.gamesbykevin.havoc.assets.AssetManagerHelper.*;
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

    public static final void renderNumber(AssetManager assetManager, Batch batch, final int number, int renderX, int renderY, int width, int height, int padding) {

        float x = renderX;

        for (int i = 0; i < 4; i++) {

            int index = -1;

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
            switch (index) {

                case -1:
                    batch.draw(assetManager.get(PATH_PERCENT, Texture.class), x, renderY, width, height);
                    break;

                case 0:
                    batch.draw(assetManager.get(PATH_0, Texture.class), x, renderY, width, height);
                    break;

                case 1:
                    batch.draw(assetManager.get(PATH_1, Texture.class), x, renderY, width, height);
                    break;

                case 2:
                    batch.draw(assetManager.get(PATH_2, Texture.class), x, renderY, width, height);
                    break;

                case 3:
                    batch.draw(assetManager.get(PATH_3, Texture.class), x, renderY, width, height);
                    break;

                case 4:
                    batch.draw(assetManager.get(PATH_4, Texture.class), x, renderY, width, height);
                    break;

                case 5:
                    batch.draw(assetManager.get(PATH_5, Texture.class), x, renderY, width, height);
                    break;

                case 6:
                    batch.draw(assetManager.get(PATH_6, Texture.class), x, renderY, width, height);
                    break;

                case 7:
                    batch.draw(assetManager.get(PATH_7, Texture.class), x, renderY, width, height);
                    break;

                case 8:
                    batch.draw(assetManager.get(PATH_8, Texture.class), x, renderY, width, height);
                    break;

                case 9:
                    batch.draw(assetManager.get(PATH_9, Texture.class), x, renderY, width, height);
                    break;
            }

            //move to the next coordinate
            x += width + padding;
        }
    }

    public static void renderKey(AssetManager assetManager, Batch batch) {
        batch.draw(assetManager.get(PATH_KEY_1, Texture.class), HUD_HEALTH_X, HUD_HEALTH_Y - (HUD_KEY_HEIGHT * 1), HUD_KEY_WIDTH, HUD_KEY_HEIGHT);
    }
}