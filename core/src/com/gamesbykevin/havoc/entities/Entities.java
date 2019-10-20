package com.gamesbykevin.havoc.entities;

import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.decals.DecalBatch;
import com.badlogic.gdx.math.Vector3;
import com.gamesbykevin.havoc.level.Level;

import java.util.ArrayList;
import java.util.List;

import static com.gamesbykevin.havoc.level.LevelHelper.RENDER_RANGE;

public abstract class Entities {

    //list of entities in the game
    private List<Entity> entityList;

    //store reference to the level
    private final Level level;

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

    //do we have collision with any of the objects
    public abstract boolean hasCollision(float x, float y);

    //logic to render the entities
    public void render(DecalBatch decalBatch, PerspectiveCamera camera3d) {

        for (int i = 0; i < getEntityList().size(); i++) {

            //get the current entity
            Entity entity = getEntityList().get(i);

            //update the entity
            entity.update(camera3d);

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