package com.gamesbykevin.havoc.player;

import com.badlogic.gdx.math.Vector3;
import com.gamesbykevin.havoc.decals.Door;
import com.gamesbykevin.havoc.level.Level;

public class PlayerHelper {

    //distance from wall to detect collision
    public static final float WALL_DISTANCE = .25f;

    //if we aren't moving the joystick enough we will ignore
    public static final float DEADZONE_IGNORE = .2f;

    //where to render our hud items
    public static final float HUD_RATIO = .5f;
    public static final int HUD_KEY_WIDTH = (int)(20 * HUD_RATIO);
    public static final int HUD_KEY_HEIGHT = (int)(54 * HUD_RATIO);

    public static final int HEALTH_SMALL = 10;
    public static final int HEALTH_LARGE = 25;

    public static void checkCollision(Level level) {

        Player player = level.getPlayer();

        final float x = player.getPrevious().x;
        final float y = player.getPrevious().y;

        //reset position if there is a collision
        if (checkCollision(level, x, player.getCamera3d().position.y))
            player.getCamera3d().position.y = y;
        if (checkCollision(level, player.getCamera3d().position.x, y))
            player.getCamera3d().position.x = x;
    }

    private static boolean checkCollision(Level level, final float x, final float y) {

        Player player = level.getPlayer();

        //which direction are we heading
        final float xDiff = x - player.getPrevious().x;
        final float yDiff = y - player.getPrevious().y;

        //and then what specific column in the room
        final float roomCol = player.getCamera3d().position.x;
        final float roomRow = player.getCamera3d().position.y;

        if (yDiff > 0) {
            if (checkBounds(level, roomRow + WALL_DISTANCE, roomCol))
                return true;
        } else if (yDiff < 0) {
            if (checkBounds(level, roomRow - WALL_DISTANCE, roomCol))
                return true;
        }

        if (xDiff > 0) {
            if (checkBounds(level, roomRow, roomCol + WALL_DISTANCE))
                return true;
        } else if (xDiff < 0) {
            if (checkBounds(level, roomRow, roomCol - WALL_DISTANCE))
                return true;
        }

        //if collision with an enemy return true
        if (level.getEnemies().hasCollision(x, y))
            return true;

        //if collision with an obstacle return true
        if (level.getObstacles().hasCollision(x, y))
            return true;

        //no collision
        return false;
    }

    private static boolean checkBounds(Level level, float row, float col) {
        return checkBounds(level, (int)row, (int)col);
    }

    private static boolean checkBounds(Level level, int row, int col) {

        //if not true we aren't in open space
        if (!level.getDungeon().hasMap(col, row))
            return true;

        //is there a door/switch here
        if (level.getDungeon().hasInteract(col, row)) {

            Door door = level.getDoorDecal(col, row);

            //door isn't open
            if (!door.isOpen()) {
                return true;
            } else {
                door.setLapsed(0);
            }
        }

        return false;
    }
}