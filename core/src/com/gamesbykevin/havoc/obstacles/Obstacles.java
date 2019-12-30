package com.gamesbykevin.havoc.obstacles;

import com.gamesbykevin.havoc.dungeon.Leaf;
import com.gamesbykevin.havoc.dungeon.Room;
import com.gamesbykevin.havoc.entities.Entities;
import com.gamesbykevin.havoc.entities.Entity;
import com.gamesbykevin.havoc.level.Level;

import static com.gamesbykevin.havoc.dungeon.Dungeon.getRandom;
import static com.gamesbykevin.havoc.level.Level.RENDER_RANGE;
import static com.gamesbykevin.havoc.obstacles.ObstacleHelper.*;
import static com.gamesbykevin.havoc.util.Distance.getDistance;

public final class Obstacles extends Entities {

    public Obstacles(Level level) {
        super(level);
    }

    @Override
    public void spawn() {

        for (int i = 0; i < getLevel().getDungeon().getLeafs().size(); i++) {

            Leaf leaf = getLevel().getDungeon().getLeafs().get(i);

            //only want leafs containing rooms
            if (leaf.getRoom() == null)
                continue;

            //add lights
            addLights(leaf.getRoom());

            //assign random type next to the walls
            if (getRandom().nextBoolean())
                addNextToWalls(leaf.getRoom());
        }
    }

    private void addNextToWalls(Room room) {

        Obstacle.Type type;

        //assign random obstacle type
        switch (getRandom().nextInt(7)) {

            case 0:
            default:
                type = getRandomTypePlant();
                break;

            case 1:
                type = getRandomTypeCage();
                break;

            case 2:
                type = getRandomTypeFlag();
                break;

            case 3:
                type = getRandomTypeStatue();
                break;

            case 4:
                type = getRandomTypePillar();
                break;

            case 5:
                type = getRandomTypeGrass();
                break;

            case 6:
                type = getRandomTypeOther();
                break;
        }


        //how frequent do we add an obstacle
        int offset = getRandom().nextInt(3) + 2;

        boolean doorWest = room.hasWestDoor(getLevel().getDungeon());
        boolean doorEast = room.hasEastDoor(getLevel().getDungeon());
        boolean doorNorth = room.hasNorthDoor(getLevel().getDungeon());
        boolean doorSouth = room.hasSouthDoor(getLevel().getDungeon());

        for (int row = room.getY(); row < room.getY() + room.getH(); row++) {
            if (row % offset != 0)
                continue;
            if (!doorWest)
                add(type, room.getX() + 1, row);
            if (!doorEast)
                add(type, room.getX() + room.getW() - 2, row);
        }

        for (int col = room.getX(); col < room.getX() + room.getW(); col++) {
            if (col % offset != 0)
                continue;
            if (!doorNorth)
                add(type, col, room.getY() + room.getH() - 2);
            if (!doorSouth)
                add(type, col, room.getY() + 1);
        }
    }

    private void addLights(Room room) {

        //frequency of lights
        int frequency = getRandom().nextInt(4) + 2;

        Obstacle.Type type = getRandomTypeLight();

        int middleCol = room.getX() + (room.getW() / 2);
        int middleRow = room.getY() + (room.getH() / 2);

        switch (getRandom().nextInt(5)) {

            //lights in 1 line
            case 0:
                for (int col = room.getX(); col < room.getX() + room.getW(); col++) {

                    if (col % frequency == 0)
                        continue;

                    add(type, col, middleRow);
                }
                break;

                //lights in 1 line
            case 1:
                for (int row = room.getY(); row < room.getY() + room.getH(); row++) {

                    if (row % frequency == 0)
                        continue;

                    add(type, middleCol, row);
                }
                break;

                //scattered lights throughout the room
            case 2:
                for (int col = room.getX(); col < room.getX() + room.getW(); col++) {
                    for (int row = room.getY(); row < room.getY() + room.getH(); row++) {

                        if (col % frequency != 0 || row % frequency != 0)
                            continue;

                        add(type, col, row);
                    }
                }
                break;

                //lights across the middle col and middle row
            case 3:
                for (int col = room.getX(); col < room.getX() + room.getW(); col++) {

                    if (col % frequency == 0)
                        continue;

                    add(type, col, middleRow);
                }

                for (int row = room.getY(); row < room.getY() + room.getH(); row++) {

                    if (row % frequency == 0)
                        continue;

                    add(type, middleCol, row);
                }
                break;

                //add lights in corners
            case 4:
                int offset = 4;
                add(type, room.getX() + offset, room.getY() + offset);
                add(type, room.getX() + (room.getW() - 1) - offset, room.getY() + (room.getH() - 1) - offset);
                add(type, room.getX() + (room.getW() - 1) - offset, room.getY() + offset);
                add(type, room.getX() + offset, room.getY() + (room.getH() - 1) - offset);
                break;
        }
    }

    private void add(Obstacle.Type type, int col, int row) {

        //we won't add if already occupied
        if (hasEntityLocation(col, row))
            return;
        if (nearInteract(col, row))
            return;

        //create our obstacle
        Obstacle obstacle = new Obstacle(getLevel().getAssetManager(), type);

        //then add to entity list
        add(obstacle, col, row);

        //update dungeon map at location
        getLevel().getDungeon().updateMap(col, row);
    }

    private boolean nearInteract(int col, int row) {

        //can't place it next to a door
        for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {

                if (x != 0 && y != 0)
                    continue;

                if (getLevel().getDungeon().hasInteract(col + x, row + y))
                    return true;
            }
        }

        return false;
    }

    @Override
    public boolean hasCollision(float x, float y) {
        //dungeon map is updated based on obstacle position, otherwise we could have a performance issue here
        return false;
    }

    @Override
    public void update() {
        //do we need to update any of the obstacles?
    }

    @Override
    public void dispose() {
        super.dispose();
    }

    @Override
    public int render() {

        int count = 0;

        for (int i = 0; i < getEntityList().size(); i++) {

            //get the current entity
            Entity entity = getEntityList().get(i);

            //how far is the player from the entity
            double distance = getDistance(entity, getLevel().getPlayer());

            //don't render if not solid and too close to the player
            if (distance < DISTANCE_RENDER_IGNORE)
                continue;

            //don't render if too far away
            if (distance > RENDER_RANGE)
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