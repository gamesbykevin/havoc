package com.gamesbykevin.havoc.assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

import static com.gamesbykevin.havoc.preferences.AppPreferences.hasEnabledMusic;
import static com.gamesbykevin.havoc.preferences.AppPreferences.hasEnabledSfx;

//audio to play while on the menu screens
public class ScreenAudio {

    //sound effect when selecting item from the menu
    private static Sound SELECT;

    private static Music MENU;

    public static final String PATH_SELECT = "audio/sound/weapon/empty.ogg";
    public static final String PATH_MENU = "audio/music/menu.mp3";

    public static void recycle() {

        try {

            if (MENU != null)
                MENU.dispose();
            if (SELECT != null)
                SELECT.dispose();

            MENU = null;
            SELECT = null;

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void stopAll() {

        try {
            if (MENU != null)
                MENU.stop();

            if (SELECT != null)
                SELECT.stop();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void playMenu() {

        if (!hasEnabledMusic())
            return;

        if (MENU == null)
            MENU = Gdx.audio.newMusic(Gdx.files.internal(PATH_MENU));

        if (MENU.isPlaying())
            return;

        MENU.setLooping(true);
        MENU.play();
    }

    public static void playSelect() {

        if (!hasEnabledSfx())
            return;

        if (SELECT == null)
            SELECT = Gdx.audio.newSound(Gdx.files.internal(PATH_SELECT));

        SELECT.play();
    }
}