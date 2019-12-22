package com.gamesbykevin.havoc.assets;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.gamesbykevin.havoc.collectibles.Collectibles;
import com.gamesbykevin.havoc.enemies.Boss;
import com.gamesbykevin.havoc.enemies.Soldier;
import com.gamesbykevin.havoc.obstacles.Obstacle;
import com.gamesbykevin.havoc.weapon.WeaponHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.gamesbykevin.havoc.assets.AudioHelper.Sfx.*;
import static com.gamesbykevin.havoc.dungeon.Dungeon.getRandom;
import static com.gamesbykevin.havoc.obstacles.ObstacleHelper.*;

public class AssetManagerHelper {

    //list of paths to each asset
    private static List<String> PATHS;

    //how many different textures can we choose from for the floor ceiling?
    public static final int TILES_BACKGROUND_LIGHT = 34;
    public static final int TILES_BACKGROUND_DARK = 32;

    //how many different textures can we choose from for the walls
    private static final int TILES_WALL = 159;
    private static final int TILES_HALLWAY = 38;

    //how many wall textures will we allow to be loaded
    private static final int COUNT_WALLS = 10;

    //how many different enemy types will we allow to be loaded
    private static final int COUNT_BOSS = 1;

    //how many different enemy types will we allow to be loaded
    private static final int COUNT_SOLDIER = 2;

    //here we will load multiple textures for the wall
    private static List<String> PATHS_WALL;

    //we will pick 1 texture for the hallways
    private static List<String> PATHS_HALLWAY;

    //here we will load 2 random textures for the floor and ceiling
    private static List<String> PATHS_BACKGROUND;

    //here we will store the types we want to load textures for
    private static List<Boss.Type> TYPE_BOSS;
    private static List<Soldier.Type> TYPE_SOLDIER;
    private static List<Obstacle.Type> TYPE_OBSTACLE;
    private static Obstacle.Type TYPE_LIGHT;

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

    //where our sound effects are located
    public static final String PARENT_DIR_SOUND_ENEMY   = PARENT_DIR_SOUND + "enemy/";
    public static final String PARENT_DIR_SOUND_HERO    = PARENT_DIR_SOUND + "hero/";
    public static final String PARENT_DIR_SOUND_ITEM    = PARENT_DIR_SOUND + "item/";
    public static final String PARENT_DIR_SOUND_LEVEL   = PARENT_DIR_SOUND + "level/";
    public static final String PARENT_DIR_SOUND_WEAPON  = PARENT_DIR_SOUND + "weapon/";

    public static final String PARENT_DIR_SOUND_ENEMY_HURT   = PARENT_DIR_SOUND_ENEMY + "hurt/";
    public static final String PARENT_DIR_SOUND_ENEMY_DEAD   = PARENT_DIR_SOUND_ENEMY + "dead/";
    public static final String PARENT_DIR_SOUND_ENEMY_ALERT   = PARENT_DIR_SOUND_ENEMY + "alert/";
    public static final String PARENT_DIR_SOUND_ENEMY_SHOOT   = PARENT_DIR_SOUND_ENEMY + "shoot/";


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
    public static final String PATH_ENEMY_ALERT_1 = PARENT_DIR_SOUND_ENEMY_ALERT + "alert1" + ASSET_EXT_MP3;
    public static final String PATH_ENEMY_ALERT_2 = PARENT_DIR_SOUND_ENEMY_ALERT + "alert2" + ASSET_EXT_MP3;
    public static final String PATH_ENEMY_ALERT_3 = PARENT_DIR_SOUND_ENEMY_ALERT + "alert3" + ASSET_EXT_MP3;
    public static final String PATH_ENEMY_ALERT_4 = PARENT_DIR_SOUND_ENEMY_ALERT + "alert4" + ASSET_EXT_MP3;
    public static final String PATH_ENEMY_ALERT_5 = PARENT_DIR_SOUND_ENEMY_ALERT + "alert5" + ASSET_EXT_MP3;
    public static final String PATH_ENEMY_ALERT_6 = PARENT_DIR_SOUND_ENEMY_ALERT + "alert6" + ASSET_EXT_MP3;
    public static final String PATH_ENEMY_ALERT_7 = PARENT_DIR_SOUND_ENEMY_ALERT + "alert7" + ASSET_EXT_MP3;

    //sound effects for enemy hurt
    public static final String PATH_ENEMY_HURT_1 = PARENT_DIR_SOUND_ENEMY_HURT + "hurt1" + ASSET_EXT_MP3;
    public static final String PATH_ENEMY_HURT_2 = PARENT_DIR_SOUND_ENEMY_HURT + "hurt2" + ASSET_EXT_MP3;
    public static final String PATH_ENEMY_HURT_3 = PARENT_DIR_SOUND_ENEMY_HURT + "hurt3" + ASSET_EXT_MP3;
    public static final String PATH_ENEMY_HURT_4 = PARENT_DIR_SOUND_ENEMY_HURT + "hurt4" + ASSET_EXT_MP3;
    public static final String PATH_ENEMY_HURT_5 = PARENT_DIR_SOUND_ENEMY_HURT + "hurt5" + ASSET_EXT_MP3;
    public static final String PATH_ENEMY_HURT_6 = PARENT_DIR_SOUND_ENEMY_HURT + "hurt6" + ASSET_EXT_MP3;
    public static final String PATH_ENEMY_HURT_7 = PARENT_DIR_SOUND_ENEMY_HURT + "hurt7" + ASSET_EXT_MP3;

    //sound effects for enemy dead
    public static final String PATH_ENEMY_DEAD_1 = PARENT_DIR_SOUND_ENEMY_DEAD + "dead1" + ASSET_EXT_WAV;
    public static final String PATH_ENEMY_DEAD_2 = PARENT_DIR_SOUND_ENEMY_DEAD + "dead2" + ASSET_EXT_WAV;
    public static final String PATH_ENEMY_DEAD_3 = PARENT_DIR_SOUND_ENEMY_DEAD + "dead3" + ASSET_EXT_OGG;
    public static final String PATH_ENEMY_DEAD_4 = PARENT_DIR_SOUND_ENEMY_DEAD + "dead4" + ASSET_EXT_OGG;
    public static final String PATH_ENEMY_DEAD_5 = PARENT_DIR_SOUND_ENEMY_DEAD + "dead5" + ASSET_EXT_OGG;
    public static final String PATH_ENEMY_DEAD_6 = PARENT_DIR_SOUND_ENEMY_DEAD + "dead6" + ASSET_EXT_OGG;
    public static final String PATH_ENEMY_DEAD_7 = PARENT_DIR_SOUND_ENEMY_DEAD + "dead7" + ASSET_EXT_OGG;

    //sound effects for enemy shooting
    public static final String PATH_ENEMY_SHOOT_1 = PARENT_DIR_SOUND_ENEMY_SHOOT + "weapon1" + ASSET_EXT_OGG;
    public static final String PATH_ENEMY_SHOOT_2 = PARENT_DIR_SOUND_ENEMY_SHOOT + "weapon2" + ASSET_EXT_OGG;
    public static final String PATH_ENEMY_SHOOT_3 = PARENT_DIR_SOUND_ENEMY_SHOOT + "weapon3" + ASSET_EXT_OGG;
    public static final String PATH_ENEMY_SHOOT_4 = PARENT_DIR_SOUND_ENEMY_SHOOT + "weapon4" + ASSET_EXT_OGG;
    public static final String PATH_ENEMY_SHOOT_5 = PARENT_DIR_SOUND_ENEMY_SHOOT + "weapon5" + ASSET_EXT_OGG;
    public static final String PATH_ENEMY_SHOOT_6 = PARENT_DIR_SOUND_ENEMY_SHOOT + "weapon6" + ASSET_EXT_OGG;
    public static final String PATH_ENEMY_SHOOT_7 = PARENT_DIR_SOUND_ENEMY_SHOOT + "weapon7" + ASSET_EXT_OGG;

    //where the sprite image is located
    public static final String ASSET_DIR_OBSTACLES = PARENT_DIR_IMAGES + "obstacles/";
    public static final String ASSET_DIR_COLLECTIBLES = PARENT_DIR_IMAGES + "collect/";
    public static final String ASSET_DIR_WALLS = PARENT_DIR_IMAGES + "walls/";
    public static final String ASSET_DIR_BACKGROUND = PARENT_DIR_IMAGES + "background/";
    public static final String ASSET_DIR_BACKGROUND_LIGHT = ASSET_DIR_BACKGROUND + "light/";
    public static final String ASSET_DIR_BACKGROUND_DARK = ASSET_DIR_BACKGROUND + "dark/";
    public static final String ASSET_DIR_HUD = PARENT_DIR_IMAGES + "hud/";
    public static final String ASSET_DIR_DOOR = PARENT_DIR_IMAGES + "door/";
    public static final String ASSET_DIR_GOAL = PARENT_DIR_IMAGES + "goal/";
    public static final String ASSET_DIR_CONTROLS = PARENT_DIR_IMAGES + "controls/";
    public static final String ASSET_DIR_ENEMIES = PARENT_DIR_IMAGES + "enemies/";
    public static final String ASSET_DIR_BOSS = ASSET_DIR_ENEMIES + "boss/";
    public static final String ASSET_DIR_SOLDIER = ASSET_DIR_ENEMIES + "soldier/";
    public static final String ASSET_DIR_WEAPONS = PARENT_DIR_IMAGES + "weapons/";
    public static final String ASSET_DIR_HALLWAY     = PARENT_DIR_IMAGES + "hallway/";

    //bulk of file names start with this
    public static final String FILENAME_WALL = "wall_";
    public static final String FILENAME_HALLWAY = "hallway_";
    public static final String FILENAME_BACKGROUND = "background_";
    public static final String FILENAME_DIE = "die";
    public static final String FILENAME_PAIN = "pain";
    public static final String FILENAME_IDLE = "s_";
    public static final String FILENAME_SHOOT = "shoot";
    public static final String FILENAME_WALK_1 = "w1_";
    public static final String FILENAME_WALK_2 = "w2_";
    public static final String FILENAME_WALK_3 = "w3_";
    public static final String FILENAME_WALK_4 = "w4_";
    public static final String FILENAME_WALK = "w";

    //paths to direct files
    public static final String PATH_DOOR        = ASSET_DIR_DOOR + "door" + ASSET_EXT_BMP;
    public static final String PATH_DOOR_LOCKED = ASSET_DIR_DOOR + "door_locked_1" + ASSET_EXT_BMP;
    public static final String PATH_SIDE        = ASSET_DIR_DOOR + "door_side" + ASSET_EXT_BMP;
    public static final String PATH_DOOR_GOAL   = ASSET_DIR_GOAL + "door" + ASSET_EXT_BMP;
    public static final String PATH_WALL_GOAL   = ASSET_DIR_GOAL + "wall" + ASSET_EXT_BMP;
    public static final String PATH_SWITCH_OFF  = ASSET_DIR_GOAL + "switch_off" + ASSET_EXT_BMP;
    public static final String PATH_SWITCH_ON   = ASSET_DIR_GOAL + "switch_on" + ASSET_EXT_BMP;
    public static final String PATH_0   = ASSET_DIR_HUD + "0" + ASSET_EXT_PNG;
    public static final String PATH_1   = ASSET_DIR_HUD + "1" + ASSET_EXT_PNG;
    public static final String PATH_2   = ASSET_DIR_HUD + "2" + ASSET_EXT_PNG;
    public static final String PATH_3   = ASSET_DIR_HUD + "3" + ASSET_EXT_PNG;
    public static final String PATH_4   = ASSET_DIR_HUD + "4" + ASSET_EXT_PNG;
    public static final String PATH_5   = ASSET_DIR_HUD + "5" + ASSET_EXT_PNG;
    public static final String PATH_6   = ASSET_DIR_HUD + "6" + ASSET_EXT_PNG;
    public static final String PATH_7   = ASSET_DIR_HUD + "7" + ASSET_EXT_PNG;
    public static final String PATH_8   = ASSET_DIR_HUD + "8" + ASSET_EXT_PNG;
    public static final String PATH_9   = ASSET_DIR_HUD + "9" + ASSET_EXT_PNG;
    public static final String PATH_KEY_1 = ASSET_DIR_HUD + "key_1_small" + ASSET_EXT_PNG;
    public static final String PATH_PERCENT = ASSET_DIR_HUD + "percent" + ASSET_EXT_PNG;
    public static final String PATH_HURT = ASSET_DIR_HUD + "hurt" + ASSET_EXT_PNG;
    public static final String PATH_COLLECT = ASSET_DIR_HUD + "collect" + ASSET_EXT_PNG;
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

    public static List<String> getPathsWall() {

        if (PATHS_WALL == null)
            PATHS_WALL = new ArrayList<>();

        return PATHS_WALL;
    }

    public static List<String> getPathsHallway() {

        if (PATHS_HALLWAY == null)
            PATHS_HALLWAY = new ArrayList<>();

        return PATHS_HALLWAY;
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

    public static List<Obstacle.Type> getTypeObstacle() {

        if (TYPE_OBSTACLE == null)
            TYPE_OBSTACLE = new ArrayList<>();

        return TYPE_OBSTACLE;
    }

    public static Obstacle.Type getTypeLight() {
        return TYPE_LIGHT;
    }

    public static void dispose(AssetManager assetManager) {

        if (PATHS_WALL != null)
            PATHS_WALL.clear();
        if (PATHS_HALLWAY != null)
            PATHS_HALLWAY.clear();
        if (PATHS_BACKGROUND != null)
            PATHS_BACKGROUND.clear();
        if (TYPE_BOSS != null)
            TYPE_BOSS.clear();
        if (TYPE_SOLDIER != null)
            TYPE_SOLDIER.clear();
        if (TYPE_OBSTACLE != null)
            TYPE_OBSTACLE.clear();

        PATHS_WALL = null;
        PATHS_HALLWAY = null;
        PATHS_BACKGROUND = null;
        TYPE_BOSS = null;
        TYPE_SOLDIER = null;
        TYPE_OBSTACLE = null;
        TYPE_LIGHT = null;

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
        loadObstacles(assetManager);

        //load every collectible
        for (Collectibles.Type type : Collectibles.Type.values()) {
            load(assetManager, ASSET_DIR_COLLECTIBLES + type.toString() + ASSET_EXT_BMP, Texture.class);
            load(assetManager, type.sound.getPath(), Sound.class);
        }

        //load controller images
        loadController(assetManager);

        //load wall textures
        loadWallTextures(assetManager);

        //load hud images
        loadHud(assetManager);

        //load weapon
        for (WeaponHelper.Type type : WeaponHelper.Type.values()) {
            for (int index = 1; index <= type.getStopIndexStart() + type.getStopIndexCount(); index++) {
                load(assetManager, type.getDir() + type.getFileName() + index + ASSET_EXT_PNG, Texture.class);
            }

            load(assetManager, type.shoot.getPath(), Sound.class);
        }

        //load the floor / ceiling images
        loadBackground(assetManager);

        //load the enemy sprites
        loadEnemies(assetManager);

        //load the rest of the audio
        loadAudio(assetManager);
    }

    private static void loadObstacles(AssetManager assetManager) {

        //clear list
        getTypeObstacle().clear();

        //pick a random light
        TYPE_LIGHT = getRandomTypeLight();

        //populate with random types
        getTypeObstacle().add(getRandomTypePillar());
        getTypeObstacle().add(getRandomTypeFlag());
        getTypeObstacle().add(getRandomTypeStatue());
        getTypeObstacle().add(getRandomTypeGrass());
        getTypeObstacle().add(getRandomTypeCage());
        getTypeObstacle().add(getRandomTypeOther());

        //load each of the optional obstacles
        for (int i = 0; i < getTypeObstacle().size(); i++) {
            load(assetManager, ASSET_DIR_OBSTACLES + getTypeObstacle().get(i).toString() + ASSET_EXT_BMP, Texture.class);
        }

        load(assetManager, ASSET_DIR_OBSTACLES + TYPE_LIGHT.toString() + ASSET_EXT_BMP, Texture.class);
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

        //clear the list and add a wall / ceiling
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

    private static void loadWallTextures(AssetManager assetManager) {

        load(assetManager, PATH_DOOR_LOCKED, Texture.class);
        load(assetManager, PATH_DOOR, Texture.class);
        load(assetManager, PATH_SIDE, Texture.class);
        load(assetManager, PATH_WALL_GOAL, Texture.class);
        load(assetManager, PATH_DOOR_GOAL, Texture.class);
        load(assetManager, PATH_SWITCH_OFF, Texture.class);
        load(assetManager, PATH_SWITCH_ON, Texture.class);

        //clear the list and add wall paths
        getPathsWall().clear();

        //create optional list to choose from
        List<Integer> options = new ArrayList<>();
        for (int i = 0; i < TILES_WALL; i++) {
            options.add(i);
        }

        //continue until we meet the allowed count
        while (getPathsWall().size() < COUNT_WALLS && !options.isEmpty()) {

            //pick random index from list
            int index = getRandom().nextInt(options.size());

            //add to list of wall paths
            getPathsWall().add(ASSET_DIR_WALLS + FILENAME_WALL + options.get(index) + ASSET_EXT_BMP);

            //remove from options list
            options.remove(index);
        }

        for (int i = 0; i < getPathsWall().size(); i++) {
            assetManager.load(getPathsWall().get(i), Texture.class);
        }

        //clear the list and add a hallway
        getPathsHallway().clear();
        getPathsHallway().add(ASSET_DIR_HALLWAY + FILENAME_HALLWAY + getRandom().nextInt(TILES_HALLWAY) + ASSET_EXT_BMP);

        for (int i = 0; i < getPathsHallway().size(); i++) {
            assetManager.load(getPathsHallway().get(i), Texture.class);
        }
    }

    private static void loadHud(AssetManager assetManager) {
        load(assetManager, PATH_HURT, Texture.class);
        load(assetManager, PATH_COLLECT, Texture.class);
        load(assetManager, PATH_0, Texture.class);
        load(assetManager, PATH_1, Texture.class);
        load(assetManager, PATH_2, Texture.class);
        load(assetManager, PATH_3, Texture.class);
        load(assetManager, PATH_4, Texture.class);
        load(assetManager, PATH_5, Texture.class);
        load(assetManager, PATH_6, Texture.class);
        load(assetManager, PATH_7, Texture.class);
        load(assetManager, PATH_8, Texture.class);
        load(assetManager, PATH_9, Texture.class);
        load(assetManager, PATH_PERCENT, Texture.class);
        load(assetManager, PATH_KEY_1, Texture.class);
    }

    private static void loadSoldier(AssetManager assetManager, Soldier.Type type) {

        for (int i = 1; i <= 4; i++) {
            load(assetManager, type.path + FILENAME_DIE + i + ASSET_EXT_BMP, Texture.class);
        }

        for (int i = 1; i <= 2; i++) {
            load(assetManager, type.path + FILENAME_PAIN + i + ASSET_EXT_BMP, Texture.class);
        }

        for (int i = 1; i <= 8; i++) {
            load(assetManager, type.path + FILENAME_IDLE + i + ASSET_EXT_BMP, Texture.class);
        }

        for (int i = 1; i <= 3; i++) {
            load(assetManager, type.path + FILENAME_SHOOT + i + ASSET_EXT_BMP, Texture.class);
        }

        for (int i = 1; i <= 8; i++) {
            load(assetManager, type.path + FILENAME_WALK_1 + i + ASSET_EXT_BMP, Texture.class);
            load(assetManager, type.path + FILENAME_WALK_2 + i + ASSET_EXT_BMP, Texture.class);
            load(assetManager, type.path + FILENAME_WALK_3 + i + ASSET_EXT_BMP, Texture.class);
            load(assetManager, type.path + FILENAME_WALK_4 + i + ASSET_EXT_BMP, Texture.class);
        }

        load(assetManager, type.alert.getPath(), Sound.class);
        load(assetManager, type.hurt.getPath(), Sound.class);
        load(assetManager, type.dead.getPath(), Sound.class);
        load(assetManager, type.shoot.getPath(), Sound.class);
    }

    private static void loadBoss(AssetManager assetManager, Boss.Type type) {

        for (int i = 1; i <= 4; i++) {
            load(assetManager, type.path + FILENAME_WALK + i + ASSET_EXT_BMP, Texture.class);
        }

        for (int i = 1; i <= 3; i++) {
            load(assetManager, type.path + FILENAME_SHOOT + i + ASSET_EXT_BMP, Texture.class);
        }

        for (int i = 1; i <= 4; i++) {
            load(assetManager, type.path + FILENAME_DIE + i + ASSET_EXT_BMP, Texture.class);
        }

        load(assetManager, type.alert.getPath(), Sound.class);
        load(assetManager, type.hurt.getPath(), Sound.class);
        load(assetManager, type.dead.getPath(), Sound.class);
        load(assetManager, type.shoot.getPath(), Sound.class);
    }
}