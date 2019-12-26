package com.gamesbykevin.havoc.assets;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.gamesbykevin.havoc.collectibles.Collectibles;
import com.gamesbykevin.havoc.enemies.Boss;
import com.gamesbykevin.havoc.enemies.Soldier;
import com.gamesbykevin.havoc.weapon.WeaponHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.gamesbykevin.havoc.assets.AudioHelper.Sfx.*;
import static com.gamesbykevin.havoc.dungeon.Dungeon.getRandom;

public class AssetManagerHelper {

    //list of paths to each asset
    private static List<String> PATHS;

    //how many different textures can we choose from for the floor ceiling?
    public static final int TILES_BACKGROUND_LIGHT = 34;
    public static final int TILES_BACKGROUND_DARK = 32;

    //how many different enemy types will we allow to be loaded
    private static final int COUNT_BOSS = 1;

    //how many different enemy types will we allow to be loaded
    private static final int COUNT_SOLDIER = 2;

    //here we will load 2 random textures for the floor and ceiling
    private static List<String> PATHS_BACKGROUND;

    //here we will store the types we want to load textures for
    private static List<Boss.Type> TYPE_BOSS;
    private static List<Soldier.Type> TYPE_SOLDIER;

    //different file extensions
    public static final String ASSET_EXT_BMP = ".bmp";
    public static final String ASSET_EXT_PNG = ".png";
    public static final String ASSET_EXT_OGG = ".ogg";
    public static final String ASSET_EXT_WAV = ".wav";
    public static final String ASSET_EXT_MP3 = ".mp3";

    //parent directory of our images
    public static final String PARENT_DIR_IMAGES = "images/";

    //parent directory of our audio
    public static final String PARENT_DIR_AUDIO = "audio/";

    //directory of sound effects
    public static final String PARENT_DIR_SOUND = PARENT_DIR_AUDIO + "sound/";

    //name of our sprite sheet
    public static final String SPRITE_SHEET = "/sheet.png";

    //parent directories for our sound effects
    public static final String PARENT_DIR_SOUND_ENEMY   = PARENT_DIR_SOUND + "enemy/";
    public static final String PARENT_DIR_SOUND_HERO    = PARENT_DIR_SOUND + "hero/";
    public static final String PARENT_DIR_SOUND_ITEM    = PARENT_DIR_SOUND + "item/";
    public static final String PARENT_DIR_SOUND_LEVEL   = PARENT_DIR_SOUND + "level/";
    public static final String PARENT_DIR_SOUND_WEAPON  = PARENT_DIR_SOUND + "weapon/";

    //direct path to sound effects for the hero weapon shooting
    public static final String PATH_SHOOT_GLOCK = PARENT_DIR_SOUND_WEAPON + "glock" + ASSET_EXT_OGG;
    public static final String PATH_SHOOT_SHOTGUN = PARENT_DIR_SOUND_WEAPON + "shotgun" + ASSET_EXT_WAV;
    public static final String PATH_SHOOT_IMPACT = PARENT_DIR_SOUND_WEAPON + "impact" + ASSET_EXT_WAV;
    public static final String PATH_SHOOT_SMG = PARENT_DIR_SOUND_WEAPON + "smg" + ASSET_EXT_WAV;
    public static final String PATH_SHOOT_MAGNUM = PARENT_DIR_SOUND_WEAPON + "magnum" + ASSET_EXT_OGG;
    public static final String PATH_SHOOT_BUZZ = PARENT_DIR_SOUND_WEAPON + "buzz" + ASSET_EXT_OGG;
    public static final String PATH_SHOOT_LANCE = PARENT_DIR_SOUND_WEAPON + "lance" + ASSET_EXT_OGG;

    public static final String PATH_SHOOT_EMPTY = PARENT_DIR_SOUND_WEAPON + "empty" + ASSET_EXT_OGG;
    public static final String PATH_CHANGE = PARENT_DIR_SOUND_WEAPON + "change" + ASSET_EXT_OGG;

    //sound effects for items
    public static final String PATH_ADD_AMMO = PARENT_DIR_SOUND_ITEM + "ammo" + ASSET_EXT_WAV;
    public static final String PATH_ADD_HEALTH_SMALL = PARENT_DIR_SOUND_ITEM + "healthSmall" + ASSET_EXT_WAV;
    public static final String PATH_ADD_HEALTH_LARGE = PARENT_DIR_SOUND_ITEM + "healthLarge" + ASSET_EXT_WAV;
    public static final String PATH_ADD_KEY = PARENT_DIR_SOUND_ITEM + "key" + ASSET_EXT_MP3;

    //sound effects for level
    public static final String PATH_LEVEL_SECRET = PARENT_DIR_SOUND_LEVEL + "secret" + ASSET_EXT_MP3;
    public static final String PATH_LEVEL_OPEN = PARENT_DIR_SOUND_LEVEL + "open" + ASSET_EXT_WAV;
    public static final String PATH_LEVEL_CLOSE = PARENT_DIR_SOUND_LEVEL + "close" + ASSET_EXT_WAV;
    public static final String PATH_LEVEL_LOCK = PARENT_DIR_SOUND_LEVEL + "locked" + ASSET_EXT_MP3;
    public static final String PATH_LEVEL_SWITCH = PARENT_DIR_SOUND_LEVEL + "switch" + ASSET_EXT_WAV;

    //sound effects for hero
    public static final String PATH_HERO_BRING_PAIN = PARENT_DIR_SOUND_HERO + "bringThePain" + ASSET_EXT_MP3;
    public static final String PATH_HERO_GET_SOME_1 = PARENT_DIR_SOUND_HERO + "comeGetSome1" + ASSET_EXT_MP3;
    public static final String PATH_HERO_GET_SOME_2 = PARENT_DIR_SOUND_HERO + "comeGetSome2" + ASSET_EXT_MP3;
    public static final String PATH_HERO_GET_SOME_3 = PARENT_DIR_SOUND_HERO + "comeGetSome3" + ASSET_EXT_MP3;
    public static final String PATH_HERO_GET_SOME_4 = PARENT_DIR_SOUND_HERO + "comeGetSome4" + ASSET_EXT_MP3;
    public static final String PATH_HERO_TAKE_OUT = PARENT_DIR_SOUND_HERO + "takeEmOut" + ASSET_EXT_MP3;
    public static final String PATH_HERO_TRASH_1 = PARENT_DIR_SOUND_HERO + "takeOutTrash1" + ASSET_EXT_MP3;
    public static final String PATH_HERO_TRASH_2 = PARENT_DIR_SOUND_HERO + "takeOutTrash2" + ASSET_EXT_MP3;
    public static final String PATH_HERO_TAKE_DOWN = PARENT_DIR_SOUND_HERO + "takeThemDown" + ASSET_EXT_MP3;
    public static final String PATH_HERO_DEAD = PARENT_DIR_SOUND_HERO + "dead" + ASSET_EXT_MP3;

    //sound effects for enemy alert
    public static final String PARENT_DIR_SOUND_ENEMY_ALERT = PARENT_DIR_SOUND_ENEMY + "alert/";
    public static final String PATH_ENEMY_ALERT_1 = PARENT_DIR_SOUND_ENEMY_ALERT + "alert1" + ASSET_EXT_MP3;
    public static final String PATH_ENEMY_ALERT_2 = PARENT_DIR_SOUND_ENEMY_ALERT + "alert2" + ASSET_EXT_MP3;
    public static final String PATH_ENEMY_ALERT_3 = PARENT_DIR_SOUND_ENEMY_ALERT + "alert3" + ASSET_EXT_MP3;
    public static final String PATH_ENEMY_ALERT_4 = PARENT_DIR_SOUND_ENEMY_ALERT + "alert4" + ASSET_EXT_MP3;
    public static final String PATH_ENEMY_ALERT_5 = PARENT_DIR_SOUND_ENEMY_ALERT + "alert5" + ASSET_EXT_MP3;
    public static final String PATH_ENEMY_ALERT_6 = PARENT_DIR_SOUND_ENEMY_ALERT + "alert6" + ASSET_EXT_MP3;
    public static final String PATH_ENEMY_ALERT_7 = PARENT_DIR_SOUND_ENEMY_ALERT + "alert7" + ASSET_EXT_MP3;

    //sound effects for enemy hurt
    public static final String PARENT_DIR_SOUND_ENEMY_HURT = PARENT_DIR_SOUND_ENEMY + "hurt/";
    public static final String PATH_ENEMY_HURT_1 = PARENT_DIR_SOUND_ENEMY_HURT + "hurt1" + ASSET_EXT_MP3;
    public static final String PATH_ENEMY_HURT_2 = PARENT_DIR_SOUND_ENEMY_HURT + "hurt2" + ASSET_EXT_MP3;
    public static final String PATH_ENEMY_HURT_3 = PARENT_DIR_SOUND_ENEMY_HURT + "hurt3" + ASSET_EXT_MP3;
    public static final String PATH_ENEMY_HURT_4 = PARENT_DIR_SOUND_ENEMY_HURT + "hurt4" + ASSET_EXT_MP3;
    public static final String PATH_ENEMY_HURT_5 = PARENT_DIR_SOUND_ENEMY_HURT + "hurt5" + ASSET_EXT_MP3;
    public static final String PATH_ENEMY_HURT_6 = PARENT_DIR_SOUND_ENEMY_HURT + "hurt6" + ASSET_EXT_MP3;
    public static final String PATH_ENEMY_HURT_7 = PARENT_DIR_SOUND_ENEMY_HURT + "hurt7" + ASSET_EXT_MP3;

    //sound effects for enemy dead
    public static final String PARENT_DIR_SOUND_ENEMY_DEAD = PARENT_DIR_SOUND_ENEMY + "dead/";
    public static final String PATH_ENEMY_DEAD_1 = PARENT_DIR_SOUND_ENEMY_DEAD + "dead1" + ASSET_EXT_WAV;
    public static final String PATH_ENEMY_DEAD_2 = PARENT_DIR_SOUND_ENEMY_DEAD + "dead2" + ASSET_EXT_WAV;
    public static final String PATH_ENEMY_DEAD_3 = PARENT_DIR_SOUND_ENEMY_DEAD + "dead3" + ASSET_EXT_OGG;
    public static final String PATH_ENEMY_DEAD_4 = PARENT_DIR_SOUND_ENEMY_DEAD + "dead4" + ASSET_EXT_OGG;
    public static final String PATH_ENEMY_DEAD_5 = PARENT_DIR_SOUND_ENEMY_DEAD + "dead5" + ASSET_EXT_OGG;
    public static final String PATH_ENEMY_DEAD_6 = PARENT_DIR_SOUND_ENEMY_DEAD + "dead6" + ASSET_EXT_OGG;
    public static final String PATH_ENEMY_DEAD_7 = PARENT_DIR_SOUND_ENEMY_DEAD + "dead7" + ASSET_EXT_OGG;

    //sound effects for enemy shooting
    public static final String PARENT_DIR_SOUND_ENEMY_SHOOT = PARENT_DIR_SOUND_ENEMY + "shoot/";
    public static final String PATH_ENEMY_SHOOT_1 = PARENT_DIR_SOUND_ENEMY_SHOOT + "weapon1" + ASSET_EXT_OGG;
    public static final String PATH_ENEMY_SHOOT_2 = PARENT_DIR_SOUND_ENEMY_SHOOT + "weapon2" + ASSET_EXT_OGG;
    public static final String PATH_ENEMY_SHOOT_3 = PARENT_DIR_SOUND_ENEMY_SHOOT + "weapon3" + ASSET_EXT_OGG;
    public static final String PATH_ENEMY_SHOOT_4 = PARENT_DIR_SOUND_ENEMY_SHOOT + "weapon4" + ASSET_EXT_OGG;
    public static final String PATH_ENEMY_SHOOT_5 = PARENT_DIR_SOUND_ENEMY_SHOOT + "weapon5" + ASSET_EXT_OGG;
    public static final String PATH_ENEMY_SHOOT_6 = PARENT_DIR_SOUND_ENEMY_SHOOT + "weapon6" + ASSET_EXT_OGG;
    public static final String PATH_ENEMY_SHOOT_7 = PARENT_DIR_SOUND_ENEMY_SHOOT + "weapon7" + ASSET_EXT_OGG;

    //where the sprite image(s) is located
    public static final String ASSET_SHEET_OBSTACLES        = PARENT_DIR_IMAGES + "obstacles" + SPRITE_SHEET;
    public static final String ASSET_SHEET_COLLECTIBLES     = PARENT_DIR_IMAGES + "collect" + SPRITE_SHEET;
    public static final String ASSET_SHEET_HUD              = PARENT_DIR_IMAGES + "hud" + SPRITE_SHEET;
    public static final String ASSET_SHEET_STANDARD         = PARENT_DIR_IMAGES + "standard" + SPRITE_SHEET;
    public static final String ASSET_SHEET_WALLS            = PARENT_DIR_IMAGES + "walls" + SPRITE_SHEET;
    public static final String ASSET_SHEET_HALLWAYS         = PARENT_DIR_IMAGES + "hallway" + SPRITE_SHEET;

    public static final String ASSET_DIR_BACKGROUND         = PARENT_DIR_IMAGES + "background/";
    public static final String ASSET_DIR_BACKGROUND_LIGHT   = ASSET_DIR_BACKGROUND + "light/";
    public static final String ASSET_DIR_BACKGROUND_DARK    = ASSET_DIR_BACKGROUND + "dark/";
    public static final String ASSET_DIR_CONTROLS           = PARENT_DIR_IMAGES + "controls/";
    public static final String ASSET_DIR_ENEMIES            = PARENT_DIR_IMAGES + "enemies/";
    public static final String ASSET_DIR_BOSS               = ASSET_DIR_ENEMIES + "boss/";
    public static final String ASSET_DIR_SOLDIER            = ASSET_DIR_ENEMIES + "soldier/";
    public static final String ASSET_DIR_WEAPONS            = PARENT_DIR_IMAGES + "weapons/";

    //bulk of file names start with this
    public static final String FILENAME_BACKGROUND = "background_";

    public static final String PATH_HURT = PARENT_DIR_IMAGES + "/hud/" + "hurt" + ASSET_EXT_PNG;
    public static final String PATH_COLLECT = PARENT_DIR_IMAGES + "/hud/" + "collect" + ASSET_EXT_PNG;

    //path locations to the controller images
    public static final String PATH_CONTROL_SHOOT = ASSET_DIR_CONTROLS + "shoot" + ASSET_EXT_PNG;
    public static final String PATH_CONTROL_ACTION = ASSET_DIR_CONTROLS + "action" + ASSET_EXT_PNG;
    public static final String PATH_CONTROL_CHANGE = ASSET_DIR_CONTROLS + "change" + ASSET_EXT_PNG;
    public static final String PATH_TOUCH_PAD_BACKGROUND = ASSET_DIR_CONTROLS + "joystick" + ASSET_EXT_PNG;
    public static final String PATH_TOUCH_PAD_KNOB = ASSET_DIR_CONTROLS + "knob" + ASSET_EXT_PNG;

    public static List<String> getPaths() {

        if (PATHS == null)
            PATHS = new ArrayList<>();

        return PATHS;
    }

    public static List<String> getPathsBackground() {

        if (PATHS_BACKGROUND == null)
            PATHS_BACKGROUND = new ArrayList<>();

        return PATHS_BACKGROUND;
    }

    public static List<Boss.Type> getTypeBoss() {

        if (TYPE_BOSS == null)
            TYPE_BOSS = new ArrayList<>();

        return TYPE_BOSS;
    }

    public static List<Soldier.Type> getTypeSoldier() {

        if (TYPE_SOLDIER == null)
            TYPE_SOLDIER = new ArrayList<>();

        return TYPE_SOLDIER;
    }

    public static void dispose(AssetManager assetManager) {
        if (PATHS_BACKGROUND != null)
            PATHS_BACKGROUND.clear();
        if (TYPE_BOSS != null)
            TYPE_BOSS.clear();
        if (TYPE_SOLDIER != null)
            TYPE_SOLDIER.clear();

        PATHS_BACKGROUND = null;
        TYPE_BOSS = null;
        TYPE_SOLDIER = null;

        if (PATHS != null) {

            for (int index = 0; index < PATHS.size(); index++) {
                try {
                    assetManager.unload(PATHS.get(index));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            PATHS.clear();
        }

        PATHS = null;
    }

    private static void load(AssetManager assetManager, String path, Class assetClass) {

        //make sure we aren't adding the same path more than once
        for (int i = 0; i < getPaths().size(); i++) {

            if (getPaths().get(i).equals(path))
                return;
        }

        assetManager.load(path, assetClass);
        getPaths().add(path);
    }

    //load all the assets
    public static void load(AssetManager assetManager) {

        //unload any assets (if they exist)
        dispose(assetManager);

        //load select obstacles
        load(assetManager, ASSET_SHEET_OBSTACLES, Texture.class);

        //load collectibles and their sound effects
        load(assetManager, ASSET_SHEET_COLLECTIBLES, Texture.class);
        for (Collectibles.Type type : Collectibles.Type.values()) {
            load(assetManager, type.sound.getPath(), Sound.class);
        }

        //load controller images
        loadController(assetManager);

        //load standard textures used in every level
        load(assetManager, ASSET_SHEET_STANDARD, Texture.class);

        //load the sheet with all the wall textures
        load(assetManager, ASSET_SHEET_WALLS, Texture.class);

        //load the sheet with all the hallway textures
        load(assetManager, ASSET_SHEET_HALLWAYS, Texture.class);

        //load hud images
        loadHud(assetManager);

        //load weapon textures and sound effects
        for (WeaponHelper.Type type : WeaponHelper.Type.values()) {
            load(assetManager, type.getPath(), Texture.class);
            load(assetManager, type.getShoot().getPath(), Sound.class);
        }

        //load the floor / ceiling images
        loadBackground(assetManager);

        //load the enemy sprites
        loadEnemies(assetManager);

        //load the rest of the audio
        loadAudio(assetManager);
    }

    private static void loadAudio(AssetManager assetManager) {
        load(assetManager, WeaponFireEmpty.getPath(), Sound.class);
        load(assetManager, WeaponChange.getPath(), Sound.class);
        load(assetManager, LevelSecret.getPath(), Sound.class);
        load(assetManager, LevelOpen.getPath(), Sound.class);
        load(assetManager, LevelClose.getPath(), Sound.class);
        load(assetManager, LevelSwitch.getPath(), Sound.class);
        load(assetManager, LevelLocked.getPath(), Sound.class);
        load(assetManager, HeroBringPain.getPath(), Sound.class);
        load(assetManager, HeroGetSome1.getPath(), Sound.class);
        load(assetManager, HeroGetSome2.getPath(), Sound.class);
        load(assetManager, HeroGetSome3.getPath(), Sound.class);
        load(assetManager, HeroGetSome4.getPath(), Sound.class);
        load(assetManager, HeroTakeOut.getPath(), Sound.class);
        load(assetManager, HeroTrash1.getPath(), Sound.class);
        load(assetManager, HeroTrash2.getPath(), Sound.class);
        load(assetManager, HeroTakeDown.getPath(), Sound.class);
        load(assetManager, HeroDead.getPath(), Sound.class);
    }

    private static void loadController(AssetManager assetManager) {
        load(assetManager, PATH_CONTROL_SHOOT, Texture.class);
        load(assetManager, PATH_CONTROL_ACTION, Texture.class);
        load(assetManager, PATH_CONTROL_CHANGE, Texture.class);
        load(assetManager, PATH_TOUCH_PAD_BACKGROUND, Texture.class);
        load(assetManager, PATH_TOUCH_PAD_KNOB, Texture.class);
    }

    private static void loadBackground(AssetManager assetManager) {

        //clear the list and add a floor / ceiling
        getPathsBackground().clear();
        getPathsBackground().add(ASSET_DIR_BACKGROUND_LIGHT + FILENAME_BACKGROUND + getRandom().nextInt(TILES_BACKGROUND_LIGHT) + ASSET_EXT_BMP);
        getPathsBackground().add(ASSET_DIR_BACKGROUND_DARK + FILENAME_BACKGROUND + getRandom().nextInt(TILES_BACKGROUND_DARK) + ASSET_EXT_BMP);

        //now add lists to the asset manager queue for loading
        for (int i = 0; i < getPathsBackground().size(); i++) {
            assetManager.load(getPathsBackground().get(i), Texture.class);
        }
    }

    private static void loadEnemies(AssetManager assetManager) {

        //clear the list
        getTypeBoss().clear();

        //list to choose boss from
        List<Boss.Type> bossType = new ArrayList(Arrays.asList(Boss.Type.values()));

        //continue until we meet the allowed count
        while (getTypeBoss().size() < COUNT_BOSS && !bossType.isEmpty()) {

            //pick random index from list
            int index = getRandom().nextInt(bossType.size());

            //add to list
            getTypeBoss().add(bossType.get(index));

            //remove from options list
            bossType.remove(index);
        }

        //clear the list
        getTypeSoldier().clear();

        //list to choose soldier from
        List<Soldier.Type> soldierType = new ArrayList(Arrays.asList(Soldier.Type.values()));

        //continue until we meet the allowed count
        while (getTypeSoldier().size() < COUNT_SOLDIER && !soldierType.isEmpty()) {

            //pick random index from list
            int index = getRandom().nextInt(soldierType.size());

            //add to list
            getTypeSoldier().add(soldierType.get(index));

            //remove from options list
            soldierType.remove(index);
        }

        for (int i = 0; i < getTypeBoss().size(); i++) {
            loadBoss(assetManager, getTypeBoss().get(i));
        }

        for (int i = 0; i < getTypeSoldier().size(); i++) {
            loadSoldier(assetManager, getTypeSoldier().get(i));
        }
    }

    private static void loadHud(AssetManager assetManager) {
        load(assetManager, PATH_HURT, Texture.class);
        load(assetManager, PATH_COLLECT, Texture.class);
        load(assetManager, ASSET_SHEET_HUD, Texture.class);
    }

    private static void loadSoldier(AssetManager assetManager, Soldier.Type type) {
        load(assetManager, ASSET_DIR_SOLDIER + type.name() + SPRITE_SHEET, Texture.class);
        load(assetManager, type.getAlert().getPath(), Sound.class);
        load(assetManager, type.getHurt().getPath(), Sound.class);
        load(assetManager, type.getDead().getPath(), Sound.class);
        load(assetManager, type.getShoot().getPath(), Sound.class);
    }

    private static void loadBoss(AssetManager assetManager, Boss.Type type) {
        load(assetManager, ASSET_DIR_BOSS + type.name() + SPRITE_SHEET, Texture.class);
        load(assetManager, type.getAlert().getPath(), Sound.class);
        load(assetManager, type.getHurt().getPath(), Sound.class);
        load(assetManager, type.getDead().getPath(), Sound.class);
        load(assetManager, type.getShoot().getPath(), Sound.class);
    }
}