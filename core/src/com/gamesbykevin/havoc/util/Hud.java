package com.gamesbykevin.havoc.util;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;

import static com.gamesbykevin.havoc.GameMain.getSizeHeight;
import static com.gamesbykevin.havoc.GameMain.getSizeWidth;
import static com.gamesbykevin.havoc.assets.AssetManagerHelper.*;

public class Hud {

    public static final int SHEET_WIDTH = 58;
    public static final int SHEET_HEIGHT = 72;

    public static final float HUD_RATIO = 0.5f;
    public static final int HUD_WIDTH =  (int)(SHEET_WIDTH  * HUD_RATIO);
    public static final int HUD_HEIGHT = (int)(SHEET_HEIGHT * HUD_RATIO);

    public static final int HUD_PAD = (int)(5 * HUD_RATIO);
    public static final int HUD_BULLET_X = getSizeWidth() - (3 * HUD_WIDTH) - (3 * HUD_PAD);
    public static final int HUD_BULLET_Y = getSizeHeight() - (HUD_HEIGHT);
    public static final int HUD_HEALTH_X = (3 * HUD_PAD);
    public static final int HUD_HEALTH_Y = getSizeHeight() - HUD_HEIGHT - (HUD_PAD * 2);

    private static final int INDEX_DOT = -4;
    private static final int INDEX_KEY = -3;
    private static final int INDEX_COLON = -2;
    private static final int INDEX_PERCENT = -1;

    public static final int DIGITS_3 = 3;
    public static final int DIGITS_2 = 2;
    public static final int DIGITS_1 = 1;

    public static void renderTime(AssetManager assetManager, Batch batch, int minutes, int seconds, int milli, int startX, int startY, int width, int height) {

        if (minutes > 0) {
            renderNumberDigits3(assetManager, batch, minutes, false, startX, startY, width, height);
            startX += (width + HUD_PAD) * DIGITS_3;
            render(assetManager, batch, INDEX_COLON, startX, startY, width, height);
            startX += (width + HUD_PAD);
        } else {
            renderNumberDigits1(assetManager, batch, 0, false, startX, startY, width, height);
            startX += (width + HUD_PAD) * DIGITS_1;
            render(assetManager, batch, INDEX_COLON, startX, startY, width, height);
            startX += (width + HUD_PAD);
        }

        renderNumberDigits2(assetManager, batch, seconds, false, startX, startY, width, height);
        startX += (width + HUD_PAD) * DIGITS_2;
        render(assetManager, batch, INDEX_DOT, startX, startY, width, height);
        startX += (width + HUD_PAD);
        renderNumberDigits3(assetManager, batch, milli, false, startX, startY, width, height);
    }

    public static void renderKey(AssetManager assetManager, Batch batch) {
        render(assetManager, batch, INDEX_KEY, HUD_HEALTH_X, HUD_HEALTH_Y - (SHEET_HEIGHT * 1), SHEET_WIDTH, SHEET_HEIGHT);
    }

    public static void renderNumberDigits1(AssetManager assetManager, Batch batch, final int number, boolean percent, int startX, int startY, int width, int height) {

        //render the sprite
        render(assetManager, batch, number, startX, startY, width, height);

        //move to the next coordinate
        startX += width + HUD_PAD;

        //is there a percent sign on the end?
        if (percent)
            render(assetManager, batch, INDEX_PERCENT, startX, startY, width, height);
    }

    public static void renderNumberDigits2(AssetManager assetManager, Batch batch, final int number, boolean percent, int startX, int startY, int width, int height) {

        for (int i = 0; i < DIGITS_2; i++) {

            int index = -1;

            switch (i) {

                case 0:
                    index = (number % 100) / 10;
                    break;

                case 1:
                    index = (number % 10);
                    break;
            }

            //if less than 0 we display as 999
            if (number < 0)
                index = 9;

            //render the sprite
            render(assetManager, batch, index, startX, startY, width, height);

            //move to the next coordinate
            startX += width + HUD_PAD;
        }

        //is there a percent sign on the end?
        if (percent)
            render(assetManager, batch, INDEX_PERCENT, startX, startY, width, height);
    }

    public static void renderNumberDigits3(AssetManager assetManager, Batch batch, final int number, boolean percent, int startX, int startY, int width, int height) {

        for (int i = 0; i < DIGITS_3; i++) {

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

            //render the sprite
            render(assetManager, batch, index, startX, startY, width, height);

            //move to the next coordinate
            startX += width + HUD_PAD;
        }

        //is there a percent sign on the end?
        if (percent)
            render(assetManager, batch, INDEX_PERCENT, startX, startY, width, height);
    }

    private static void render(AssetManager assetManager, Batch batch, int index, int x, int y, int width, int height) {

        int col = 0, row = 0;

        //where is the texture on our sprite sheet
        switch (index) {

            case INDEX_DOT:
                col = 1;
                row = 3;
                break;

            case INDEX_KEY:
                col = 2;
                row = 2;
                break;

            case INDEX_COLON:
                col = 0;
                row = 3;
                break;

            case INDEX_PERCENT:
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

        batch.draw(assetManager.get(ASSET_SHEET_HUD, Texture.class), x, y, width, height, col * SHEET_WIDTH, row * SHEET_HEIGHT, SHEET_WIDTH, SHEET_HEIGHT, false, false);
    }
}