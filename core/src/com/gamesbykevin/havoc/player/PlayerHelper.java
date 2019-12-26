package com.gamesbykevin.havoc.player;

import com.gamesbykevin.havoc.decals.Door;
import com.gamesbykevin.havoc.decals.Square;
import com.gamesbykevin.havoc.level.Level;

public class PlayerHelper {

    //if we aren't moving the joystick enough we will ignore
    public static final float DEAD_ZONE_IGNORE = .2f;

    public static final int HEALTH_SMALL = 10;
    public static final int HEALTH_LARGE = 25;

    //how fast do we fall to the ground
    public static final float VELOCITY_Z = 0.0075f;

    //the lowest the player can fall to the ground
    public static final float HEIGHT_MIN_Z = -0.25f;

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

        //if collision with an obstacle return true
        if (level.getObstacles().hasCollision(x, y))
            return true;

        //no collision
        return false;
    }
}