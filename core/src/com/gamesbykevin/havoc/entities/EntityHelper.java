package com.gamesbykevin.havoc.entities;

import com.badlogic.gdx.math.Vector3;
import com.gamesbykevin.havoc.level.Level;

import static com.gamesbykevin.havoc.dungeon.DungeonHelper.isAvailable;
import static com.gamesbykevin.havoc.entities.Entities.OFFSET;
import static com.gamesbykevin.havoc.util.Distance.getDistance;
import static com.gamesbykevin.havoc.util.Slope.*;

public class EntityHelper {

    public static boolean isObstructed(Level level, Entity entity) {
        return isObstructed(level, entity, level.getPlayer().getCamera3d().position);
    }

    public static boolean isObstructed(Level level, Entity entity, Vector3 position) {
        return isObstructed(level, entity.getCol(), entity.getRow(), position.x, position.y);
    }

    private static boolean isObstructed(Level level, float startX, float startY, float endX, float endY) {

        //get the slope of the line
        final float m = getSlope(startX, startY, endX, endY);

        //get the y-intercept
        final float b = getYintercept(startX, startY, m);

        //if not available, we are obstructed
        if (!isAvailable(level, startX, startY))
            return true;
        if (!isAvailable(level, endX, endY))
            return true;

        //range of spaces to check
        int xMin = (int)(startX < endX ? startX : endX);
        int xMax = (int)(startX > endX ? startX : endX);
        int yMin = (int)(startY < endY ? startY : endY);
        int yMax = (int)(startY > endY ? startY : endY);

        //check the area around the 2 points
        for (int row = yMin; row <= yMax; row++) {
            for (int col = xMin; col <= xMax; col++) {

                //we don't need to check available spaces
                if (isAvailable(level, col + OFFSET, row + OFFSET))
                    continue;

                //if we intersected a wall we are obstructed
                if (intersects(col - OFFSET, row, m, b))
                    return true;
                if (intersects(col, row - OFFSET, m, b))
                    return true;
                if (intersects(col + OFFSET, row, m, b))
                    return true;
                if (intersects(col, row + OFFSET, m, b))
                    return true;
                if (intersects(col - OFFSET, row - OFFSET, m, b))
                    return true;
                if (intersects(col + OFFSET, row + OFFSET, m, b))
                    return true;
            }
        }

        //we aren't obstructed
        return false;
    }

    public static boolean intersects(float col, float row, float m, float b) {

        //calculate the x-coordinate
        float x = solveX(m, row, b);

        //calculate the y-coordinate
        float y = solveY(m, col, b);

        double distance1 = getDistance(col, row, x, row);
        double distance2 = getDistance(col, row, col, y);

        //if the distance from the center is less than a specified distance, there is an intersection
        if (distance1 <= (OFFSET * 3.0f) || distance2 <= (OFFSET * 3.0f))
            return true;

        return false;
    }
}