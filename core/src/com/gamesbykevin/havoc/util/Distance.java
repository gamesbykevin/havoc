package com.gamesbykevin.havoc.util;

import com.badlogic.gdx.graphics.g3d.decals.Decal;
import com.badlogic.gdx.math.Vector3;
import com.gamesbykevin.havoc.decals.DecalCustom;
import com.gamesbykevin.havoc.dungeon.Cell;
import com.gamesbykevin.havoc.entities.Entity;
import com.gamesbykevin.havoc.player.Player;

public class Distance {

    public static double getDistance(Entity entity1, Entity entity2) {
        return getDistance(entity1.getCol(), entity1.getRow(), entity2.getCol(), entity2.getRow());
    }

    public static double getDistance(DecalCustom decalCustom, Vector3 position) {
        return getDistance(decalCustom.getCol(), decalCustom.getRow(), position.x, position.y);
    }

    public static double getDistance(Entity entity, Vector3 position) {
        return getDistance(entity.getCol(), entity.getRow(), position.x, position.y);
    }

    public static double getDistance(Cell cell1, Cell cell2) {
        return getDistance(cell1.getCol(), cell1.getRow(), cell2.getCol(), cell2.getRow());
    }

    public static double getDistance(Entity entity, float x2, float y2) {
        return getDistance(entity.getCol(), entity.getRow(), x2, y2);
    }

    public static double getDistance(Player player, Entity entity) {
        return getDistance(player.getCamera3d().position.x, player.getCamera3d().position.y, entity.getCol(), entity.getRow());
    }

    public static double getDistance(Player player, float x2, float y2) {
        return getDistance(player.getCamera3d().position.x, player.getCamera3d().position.y, x2, y2);
    }

    public static double getDistance(float x1, float y1, float x2, float y2) {
        return Math.sqrt(Math.pow(x2 - x1, 2) + (Math.pow(y2 - y1, 2)));
    }
}