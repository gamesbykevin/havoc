package com.gamesbykevin.havoc.obstacles;

import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.decals.Decal;
import com.badlogic.gdx.graphics.g3d.decals.DecalBatch;
import com.gamesbykevin.havoc.maze.Maze;

import java.util.ArrayList;
import java.util.List;

import static com.gamesbykevin.havoc.level.LevelHelper.getDistance;
import static com.gamesbykevin.havoc.player.Player.PLAYER_COLLISION;

public class Obstacles {

    //list of obstacles
    private List<Obstacle> obstacles;

    protected static final float DEFAULT_WIDTH = 1.0f;
    protected static final float DEFAULT_HEIGHT = 1.0f;

    public enum Type {
        BluePot, BluePotPlant, BronzeCol, Chandalier, DogFood, FloorLamp, GreenC, RedC,
        SilverCol, SpecimenPod1, SpecimenPod2, SpecimenPod3, Well, WellBlood, WellWater
    }

    //total number of obstacles
    private static final int COUNT = Type.values().length;

    public Obstacles() {
        getObstacles();
    }

    public void add(float x, float y) {
        add(Type.values()[Maze.getRandom().nextInt(COUNT)], x, y);
    }

    public void add(Type type, float x, float y) {
        getObstacles().add(new Obstacle(type, x, y));
    }

    public boolean hasCollision(float x, float y) {

        for (int i = 0; i < getObstacles().size(); i++) {

            Obstacle obstacle = getObstacles().get(i);

            if (!obstacle.isSolid())
                continue;

            if (getDistance(obstacle.getDecal().getPosition().x, obstacle.getDecal().getPosition().y, x, y) <= PLAYER_COLLISION)
                return true;
        }

        return false;
    }
    private List<Obstacle> getObstacles() {

        if (this.obstacles == null)
            this.obstacles = new ArrayList<>();

        return this.obstacles;
    }

    public void render(DecalBatch decalBatch, PerspectiveCamera camera3d) {

        //render all the obstacles
        for (int i = 0; i < getObstacles().size(); i++) {

            //get the current decal
            Decal decal = getObstacles().get(i).getDecal();

            //render like a billboard
            decal.lookAt(camera3d.position, camera3d.up);

            //add to the batch to be rendered
            decalBatch.add(decal);
        }
    }
}