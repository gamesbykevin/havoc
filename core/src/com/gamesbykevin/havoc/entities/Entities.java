package com.gamesbykevin.havoc.entities;

import com.badlogic.gdx.math.Vector3;
import com.gamesbykevin.havoc.dungeon.Cell;
import com.gamesbykevin.havoc.dungeon.Room;
import com.gamesbykevin.havoc.level.Level;
import com.gamesbykevin.havoc.util.Disposable;
import com.gamesbykevin.havoc.util.Restart;

import java.util.ArrayList;
import java.util.List;

import static com.gamesbykevin.havoc.level.Level.RENDER_RANGE;
import static com.gamesbykevin.havoc.util.Distance.getDistance;

public abstract class Entities implements Disposable, Restart {

    //list of entities in the game
    private List<Entity> entityList;

    //store reference to the level
    private final Level level;

    //don't render items that aren't solid and too close to the player
    public static final float DISTANCE_RENDER_IGNORE = 1.0f;

    public Entities(Level level) {
        this.level = level;
        this.entityList = new ArrayList<>();
    }

    @Override
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

    //how to update the entities
    public abstract void update();

    //how do we spawn items
    public abstract void spawn();

    public void add(Entity3d entity, float col, float row) {

        //update the location of our animation
        entity.getAnimation().setPosition(col, row, 0);

        //assign the location
        entity.setCol(col);
        entity.setRow(row);

        //assign the starting location
        entity.setStartCol(col);
        entity.setStartRow(row);
        entity.setFinishCol(col);
        entity.setFinishRow(row);

        //add to the list of entities
        getEntityList().add(entity);

        //update the dungeon map at this location
        getLevel().getDungeon().updateMap((int)col, (int)row);
    }

    public void add(Entity2d entity) {
        getEntityList().add(entity);
    }

    //do we have collision with any of the objects
    public abstract boolean hasCollision(float x, float y);

    //check if we have collision with any of our entity lists
    protected boolean hasEntityLocation(float col, float row) {

        if (getLevel().getObstacles().hasCollision(col, row))
            return true;
        if (!getLevel().getDungeon().hasMap((int)col, (int)row))
            return true;
        if (getLevel().getDungeon().hasInteract((int)col, (int)row))
            return true;
        if (getLevel().getCollectibles().hasCollision(col, row))
            return true;
        if (getLevel().getEnemies().hasCollision(col, row))
            return true;

        //this location is not used
        return false;
    }

    @Override
    public void dispose() {
        if (this.entityList != null) {
            for (int i = 0; i < this.entityList.size(); i++) {
                if (this.entityList.get(i) != null) {
                    this.entityList.get(i).dispose();
                    this.entityList.set(i, null);
                }
            }

            this.entityList.clear();
        }

        this.entityList = null;
    }

    //implement logic to render the entities, return count of entities rendered
    public abstract int render();

    //logic to render the entities
    public int render(boolean hide, int range) {

        int count = 0;

        for (int i = 0; i < getEntityList().size(); i++) {

            //get the current entity
            Entity entity = getEntityList().get(i);

            //if we want to hide entities that are not solid
            if (hide && !entity.isSolid())
                continue;

            //don't render if too far away
            if (range > 0 && getDistance(entity, getLevel().getPlayer()) > range)
                continue;

            //render the entity
            entity.render(
                getLevel().getAssetManager(),
                getLevel().getPlayer().getCamera3d(),
                getLevel().getDecalBatch(),
                getLevel().getPlayer().getController().getStage().getBatch()
            );

            //keep track of how many items we rendered
            count++;
        }

        //return the count
        return count;
    }
}