package com.gamesbykevin.havoc.entities;

import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.decals.DecalBatch;
import com.badlogic.gdx.math.Vector3;
import com.gamesbykevin.havoc.level.Level;
import com.gamesbykevin.havoc.maze.Location;

import java.util.ArrayList;
import java.util.List;

import static com.gamesbykevin.havoc.level.LevelHelper.RENDER_RANGE;

public abstract class Entities {

    //list of entities in the game
    private List<Entity> entityList;

    //store reference to the level
    private final Level level;

    //we offset the position
    public static final float OFFSET = .5f;

    public Entities(Level level) {
        this.level = level;
        this.entityList = new ArrayList<>();
    }

    public void reset() {
        for (int i = 0; i < getEntityList().size(); i++) {
            getEntityList().get(i).reset();
        }
    }

    protected Level getLevel() {
        return this.level;
    }

    public List<Entity> getEntityList() {
        return this.entityList;
    }

    public static double getDistance(Entity entity, Vector3 position) {
        return getDistance(entity.getCol(), entity.getRow(), position.x, position.y);
    }

    public static double getDistance(Entity entity, float x2, float y2) {
        return getDistance(entity.getCol(), entity.getRow(), x2, y2);
    }

    public static double getDistance(float x1, float y1, float x2, float y2) {
        return Math.sqrt(Math.pow(x2 - x1, 2) + (Math.pow(y2 - y1, 2)));
    }

    //how do we spawn items
    public abstract void spawn();

    protected void add(Entity entity, float col, float row) {
        entity.setCol(col + OFFSET);
        entity.setRow(row + OFFSET);
        entity.getAnimation().setPosition(entity.getCol(), entity.getRow(), 0);
        getEntityList().add(entity);
    }

    //add to the list at the specified location
    protected void add(Entity entity, Location location) {
        add(entity, location.col, location.row);
    }

    //do we have collision with any of the objects
    public abstract boolean hasCollision(float x, float y);

    protected boolean hasEntityLocation(Location location) {
        return hasEntityLocation(location.col, location.row);
    }

    //check if we have collision with any of our entity lists
    protected boolean hasEntityLocation(float x, float y) {

        if (getLevel().getCollectibles().hasCollision(x, y))
            return true;
        if (getLevel().getEnemies().hasCollision(x, y))
            return true;
        if (getLevel().getObstacles().hasCollision(x, y))
            return true;
        if (getLevel().hasDoor((int)x, (int)y))
            return true;
        if (!getLevel().hasFree((int)x, (int)y))
            return true;

        //this location is not used
        return false;
    }

    //logic to render the entities
    public void render(DecalBatch decalBatch, PerspectiveCamera camera3d, float minCol, float maxCol, float minRow, float maxRow) {

        for (int i = 0; i < getEntityList().size(); i++) {

            //get the current entity
            Entity entity = getEntityList().get(i);

            //update the entity
            entity.update(camera3d);

            //get the position of the entity
            Vector3 position = entity.getAnimation().getDecal().getPosition();

            //if too far away there is no reason to render
            if (position.x < minCol || position.x > maxCol)
                continue;
            if (position.y < minRow || position.y > maxRow)
                continue;

            //if entity is not close enough we won't render
            if (getDistance(entity, camera3d.position) >= (RENDER_RANGE / 2))
                continue;

            //render like a billboard
            entity.getAnimation().getDecal().lookAt(camera3d.position, camera3d.up);

            //add to the batch to be rendered
            entity.render(decalBatch);
        }
    }
}