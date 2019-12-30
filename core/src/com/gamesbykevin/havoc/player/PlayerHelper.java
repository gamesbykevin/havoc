package com.gamesbykevin.havoc.player;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.gamesbykevin.havoc.assets.AudioHelper;
import com.gamesbykevin.havoc.decals.Door;
import com.gamesbykevin.havoc.decals.Square;
import com.gamesbykevin.havoc.level.Level;
import com.gamesbykevin.havoc.util.Hud;

import static com.gamesbykevin.havoc.GameEngine.*;
import static com.gamesbykevin.havoc.assets.AssetManagerHelper.PATH_HURT;
import static com.gamesbykevin.havoc.assets.AudioHelper.playSfx;
import static com.gamesbykevin.havoc.input.MyController.SPEED_WALK;
import static com.gamesbykevin.havoc.util.Hud.SHEET_HEIGHT;
import static com.gamesbykevin.havoc.util.Hud.SHEET_WIDTH;

public class PlayerHelper {

    public static final String TEXT_NOTIFY_LEVEL_COMPLETE = "level completed";
    public static final String TEXT_NOTIFY_SMG = "found smg auto rifle";
    public static final String TEXT_NOTIFY_IMPACT = "found impact cannon";
    public static final String TEXT_NOTIFY_MAGNUM = "found magnum";
    public static final String TEXT_NOTIFY_BUZZ = "found buzz saw gun";
    public static final String TEXT_NOTIFY_SHOTGUN = "found shotgun";
    public static final String TEXT_NOTIFY_AMMO = "ammo added";
    public static final String TEXT_NOTIFY_HEALTH_LARGE = "large health pack found";
    public static final String TEXT_NOTIFY_HEALTH_SMALL = "health pack found";
    public static final String TEXT_NOTIFY_KEY = "key found";
    public static final String TEXT_NOTIFY_LOCKED = "door locked, find the key";
    public static final String TEXT_NOTIFY_SECRET = "secret room";
    public static final String TEXT_NOTIFY_DEAD = "you died";
    public static final String TEXT_NOTIFY_LOW_HEALTH = "low health";
    public static final String TEXT_STAT_TIME = "Time";
    public static final String TEXT_STAT_ENEMY = "Enemy";
    public static final String TEXT_STAT_ITEM = "Item";
    public static final String TEXT_STAT_SECRET = "Secret";

    //if we aren't moving the joystick enough we will ignore
    public static final float DEAD_ZONE_IGNORE = .2f;

    public static final int HEALTH_SMALL = 10;
    public static final int HEALTH_LARGE = 25;

    //how fast do we fall to the ground
    public static final float VELOCITY_Z = 0.0075f;

    //the lowest the player can fall to the ground
    public static final float HEIGHT_MIN_Z = -0.25f;

    //where we render our stats
    public static final int STATS_X = 50;
    public static final int STATS_Y_ENEMY = 75;
    public static final int STATS_Y_ITEM = 175;
    public static final int STATS_Y_SECRET = 275;
    public static final int STATS_Y_TIMER = 375;
    public static final int STATS_Y_DIFF = 50;
    public static final int STATS_X_DIFF = 75;

    public static void checkCollision(Level level) {

        Player player = level.getPlayer();

        //if collision at new y position move it back
        if (checkCollision(level, player.getPrevious().x, player.getCamera3d().position.y)) {

            //if there is collision we want to place back at the correct integer position
            if ((int)(player.getPrevious().y) != (int)(player.getPrevious().y + (SPEED_WALK * 3))) {
                player.getCamera3d().position.y = (int)(player.getPrevious().y + (SPEED_WALK * 3));
            } else {
                player.getCamera3d().position.y = (int)player.getPrevious().y;
            }
        }

        //if collision at new x position move it back
        if (checkCollision(level, player.getCamera3d().position.x, player.getPrevious().y)) {

            //if there is collision we want to place back at the correct integer position
            if ((int)(player.getPrevious().x) != (int)(player.getPrevious().x + (SPEED_WALK * 3))) {
                player.getCamera3d().position.x = (int)(player.getPrevious().x + (SPEED_WALK * 3));
            } else {
                player.getCamera3d().position.x = (int)player.getPrevious().x;
            }
        }
    }

    private static boolean checkCollision(Level level, final float x, final float y) {

        Player player = level.getPlayer();

        //which direction are we heading?
        final float xDiff = x - player.getPrevious().x;
        final float yDiff = y - player.getPrevious().y;

        //and then what specific column in the room
        int col = (int)player.getCamera3d().position.x;
        int row = (int)player.getCamera3d().position.y;

        //check the area around the current location for collision
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {

                //if this location is open, check if there is a door
                if (level.getDungeon().hasMap(col + i, row + j)) {

                    //can we interact with this location?
                    if (level.getDungeon().hasInteract(col + i, row + j)) {

                        Door door = level.getDoorDecal(col + i, row + j);

                        //if the door is there and isn't open
                        if (door != null && !door.isOpen()) {
                            if (xDiff < 0 && door.hasCollisionEast(x, y))
                                return true;
                            if (xDiff > 0 && door.hasCollisionWest(x, y))
                                return true;
                            if (yDiff < 0 && door.hasCollisionNorth(x, y))
                                return true;
                            if (yDiff > 0 && door.hasCollisionSouth(x, y))
                                return true;
                        }
                    }

                } else {

                    //get the square wall
                    Square wall = level.getWall(col + i, row + j);

                    //if there is a wall check for collision
                    if (wall != null) {
                        if (xDiff < 0 && wall.hasCollisionEast(x, y))
                            return true;
                        if (xDiff > 0 && wall.hasCollisionWest(x, y))
                            return true;
                        if (yDiff < 0 && wall.hasCollisionNorth(x, y))
                            return true;
                        if (yDiff > 0 && wall.hasCollisionSouth(x, y))
                            return true;
                    }
                }
            }
        }

        //if collision with an enemy return true
        if (level.getEnemies().hasCollision(x, y))
            return true;

        //no collision
        return false;
    }

    protected static void renderStats(AssetManager assetManager, Batch batch, Player player) {

        //draw our background
        batch.draw(assetManager.get(PATH_HURT, Texture.class), 0, 0, getSizeWidth(), getSizeHeight());

        player.getFontStats().draw(batch, TEXT_STAT_SECRET, STATS_X, STATS_Y_SECRET);

        if (player.getTimerStatSecret().isExpired()) {
            Hud.renderNumberDigits3(assetManager, batch, player.getStatSecret(), true, STATS_X + STATS_X_DIFF, STATS_Y_SECRET - STATS_Y_DIFF, SHEET_WIDTH, SHEET_HEIGHT);
        } else {
            Hud.renderNumberDigits3(assetManager, batch, getRandom().nextInt(900) + 100, true, STATS_X + STATS_X_DIFF, STATS_Y_SECRET - STATS_Y_DIFF, SHEET_WIDTH, SHEET_HEIGHT);
            player.getTimerStatSecret().update();

            if (player.getTimerStatSecret().isExpired())
                playSfx(assetManager, AudioHelper.Sfx.ItemKey);
        }

        player.getFontStats().draw(batch, TEXT_STAT_ITEM, STATS_X, STATS_Y_ITEM);

        if (player.getTimerStatItem().isExpired()) {
            Hud.renderNumberDigits3(assetManager, batch, player.getStatItem(), true, STATS_X + STATS_X_DIFF, STATS_Y_ITEM - STATS_Y_DIFF, SHEET_WIDTH, SHEET_HEIGHT);
        } else {
            Hud.renderNumberDigits3(assetManager, batch, getRandom().nextInt(900) + 100, true, STATS_X + STATS_X_DIFF, STATS_Y_ITEM - STATS_Y_DIFF, SHEET_WIDTH, SHEET_HEIGHT);
            player.getTimerStatItem().update();

            if (player.getTimerStatItem().isExpired())
                playSfx(assetManager, AudioHelper.Sfx.ItemKey);
        }

        player.getFontStats().draw(batch, TEXT_STAT_ENEMY, STATS_X, STATS_Y_ENEMY);

        if (player.getTimerStatEnemy().isExpired()) {
            Hud.renderNumberDigits3(assetManager, batch, player.getStatEnemy(), true, STATS_X + STATS_X_DIFF, STATS_Y_ENEMY - STATS_Y_DIFF, SHEET_WIDTH, SHEET_HEIGHT);
        } else {
            Hud.renderNumberDigits3(assetManager, batch, getRandom().nextInt(900) + 100, true, STATS_X + STATS_X_DIFF, STATS_Y_ENEMY - STATS_Y_DIFF, SHEET_WIDTH, SHEET_HEIGHT);
            player.getTimerStatEnemy().update();

            if (player.getTimerStatEnemy().isExpired())
                playSfx(assetManager, AudioHelper.Sfx.ItemKey);
        }

        player.getFontStats().draw(batch, TEXT_STAT_TIME, STATS_X, STATS_Y_TIMER);

        if (player.getTimerStatTime().isExpired()) {
            Hud.renderTime(assetManager, batch, player.getMinutes(), player.getSeconds(), player.getMilliseconds(), STATS_X + STATS_X_DIFF, STATS_Y_TIMER - STATS_Y_DIFF, SHEET_WIDTH, SHEET_HEIGHT);
        } else {
            Hud.renderTime(assetManager, batch, getRandom().nextInt(900) + 100, getRandom().nextInt(90) + 10, getRandom().nextInt(900) + 100, STATS_X + STATS_X_DIFF, STATS_Y_TIMER - STATS_Y_DIFF, SHEET_WIDTH, SHEET_HEIGHT);
            player.getTimerStatTime().update();

            if (player.getTimerStatTime().isExpired())
                playSfx(assetManager, AudioHelper.Sfx.ItemKey);
        }
    }
}