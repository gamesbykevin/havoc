package com.gamesbykevin.havoc.util;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;

import static com.gamesbykevin.havoc.MyGdxGame.*;
import static com.gamesbykevin.havoc.assets.AssetManagerHelper.*;

public class Hud {

    //where to render our hud items
    public static final float HUD_RATIO = 0.5f;
    public static final int HUD_WIDTH = (int)(58 * HUD_RATIO);
    public static final int HUD_HEIGHT = (int)(72 * HUD_RATIO);

    public static final int HUD_PAD = (int)(5 * HUD_RATIO);
    public static final int HUD_BULLET_X = getSizeWidth() - (3 * HUD_WIDTH) - (3 * HUD_PAD);
    public static final int HUD_BULLET_Y = getSizeHeight() - (HUD_HEIGHT);
    public static final int HUD_HEALTH_X = (3 * HUD_PAD);
    public static final int HUD_HEALTH_Y = getSizeHeight() - HUD_HEIGHT - (HUD_PAD * 2);

    private static final int SHEET_WIDTH = 58;
    private static final int SHEET_HEIGHT = 72;

    public static final void renderNumber(AssetManager assetManager, Batch batch, final int number, int x, int y) {

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

            int col = 0, row = 0;

            //where is the texture on our sprite sheet
            switch (index) {

                case -1:
                    col = 3;
                    row = 2;
                    break;

                case 0:
                    col = 0;
                    row = 0;
                    break;

                case 1:
                    col = 1;
                    row = 0;
                    break;

                case 2:
                    col = 2;
                    row = 0;
                    break;

                case 3:
                    col = 3;
                    row = 0;
                    break;

                case 4:
                    col = 0;
                    row = 1;
                    break;

                case 5:
                    col = 1;
                    row = 1;
                    break;

                case 6:
                    col = 2;
                    row = 1;
                    break;

                case 7:
                    col = 3;
                    row = 1;
                    break;

                case 8:
                    col = 0;
                    row = 2;
                    break;

                case 9:
                    col = 1;
                    row = 2;
                    break;
            }

            batch.draw(assetManager.get(ASSET_SHEET_HUD, Texture.class), x, y, HUD_WIDTH, HUD_HEIGHT, col * SHEET_WIDTH, row * SHEET_HEIGHT, SHEET_WIDTH, SHEET_HEIGHT, false, false);

            //move to the next coordinate
            x += HUD_WIDTH + HUD_PAD;
        }
    }

    public static void renderKey(AssetManager assetManager, Batch batch) {
        batch.draw(assetManager.get(ASSET_SHEET_HUD, Texture.class), HUD_HEALTH_X, HUD_HEALTH_Y - (HUD_HEIGHT * 1), HUD_WIDTH, HUD_HEIGHT, 2 * SHEET_WIDTH, 2 * SHEET_HEIGHT, SHEET_WIDTH, SHEET_HEIGHT, false, false);
    }
}