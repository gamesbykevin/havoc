package com.gamesbykevin.havoc.obstacles;

import com.gamesbykevin.havoc.dungeon.Leaf;
import com.gamesbykevin.havoc.dungeon.Room;
import com.gamesbykevin.havoc.entities.Entities;
import com.gamesbykevin.havoc.entities.Entity;
import com.gamesbykevin.havoc.level.Level;

import static com.gamesbykevin.havoc.dungeon.Dungeon.getRandom;
import static com.gamesbykevin.havoc.obstacles.ObstacleHelper.*;

public final class Obstacles extends Entities {

    public enum Type {
        Light1, Light2, Light3, Light4, Light5, Light6, Light7,
        Grass1, Grass2, Grass3, Grass4, Grass5, Grass6, Grass7, Grass8, Grass9, Grass10, Grass11,
        pillar1, pillar2, pillar3, pillar4, pillar5,
        random1, random2, random3, random4,
        cage1, cage2, cage3, cage4,
        statue1, statue2, statue3, statue4, statue5,
        well1, well2, well3, well4, well5,
        BluePotEmpty, BluePotLargeEmpty, BluePotPlant1, BluePotPlant2, YellowPotPlant,
        Candle,
        spear1, spear2,
        flag1, flag2,
        barrel1, barrel2,
        pots1, pots2,
        DogFood, table,
        FloorLamp1, FloorLamp2,
        SpecimenPod1, SpecimenPod2
    }

    //how close can we get to the obstacle
    private static final float OBSTACLE_COLLISION = .35f;

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

            //add pillars
            if (getRandom().nextBoolean())
                addPillars(leaf.getRoom());

            //add lights
            addLights(leaf.getRoom());

            //add items on the 4 corners
            addCorners(leaf.getRoom());

            //assign random type next to the walls
            if (getRandom().nextBoolean())
                addNextToWalls(leaf.getRoom());
        }

        recycle();
    }

    private void addNextToWalls(Room room) {

        //assign random obstacle type
        assignRandomType();

        //how frequent do we add an obstacle
        int offset = getRandom().nextInt(4) + 1;

        boolean doorWest = room.hasWestDoor(getLevel().getDungeon());
        boolean doorEast = room.hasEastDoor(getLevel().getDungeon());
        boolean doorNorth = room.hasNorthDoor(getLevel().getDungeon());
        boolean doorSouth = room.hasSouthDoor(getLevel().getDungeon());

        for (int row = room.getY(); row < room.getY() + room.getH(); row++) {
            if (row % offset != 0)
                continue;
            if (!doorWest)
                add(room.getX() + 1, row);
            if (!doorEast)
                add(room.getX() + room.getW() - 2, row);
        }

        for (int col = room.getX(); col < room.getX() + room.getW(); col++) {
            if (col % offset != 0)
                continue;
            if (!doorNorth)
                add(col, room.getY() + room.getH() - 2);
            if (!doorSouth)
                add(col, room.getY() + 1);
        }
    }

    private void addCorners(Room room) {

        //assign random obstacle type
        assignRandomType();

        int size = (room.getW() > room.getH()) ? (room.getW() / 2) : (room.getH() / 2);

        for (int offset = 0; offset <= size; offset++) {

            if (hasEntityLocation(room.getX() + offset, room.getY() + offset))
                continue;
            if (hasEntityLocation(room.getX() + offset, room.getY() + room.getH() - offset))
                continue;
            if (hasEntityLocation(room.getX() + room.getW() - offset, room.getY() + offset))
                continue;
            if (hasEntityLocation(room.getX() + room.getW() - offset, room.getY() + room.getH() - offset))
                continue;

            add(room.getX() + offset, room.getY() + offset);
            add(room.getX() + offset, room.getY() + room.getH() - offset);
            add(room.getX() + room.getW() - offset, room.getY() + offset);
            add(room.getX() + room.getW() - offset, room.getY() + room.getH() - offset);
            return;
        }
    }

    private void addLights(Room room) {

        //frequency of lights
        int frequency = getRandom().nextInt(4) + 2;

        Obstacle.TYPE = getRandomLight();

        int middleCol = room.getX() + (room.getW() / 2);
        int middleRow = room.getY() + (room.getH() / 2);

        switch (getRandom().nextInt(5)) {

            case 0:
                for (int col = room.getX(); col < room.getX() + room.getW(); col++) {

                    if (col % frequency == 0)
                        continue;

                    if (!hasEntityLocation(col, middleRow))
                        add(new Obstacle(), col, middleRow);
                }
                break;

            case 1:
                for (int row = room.getY(); row < room.getY() + room.getH(); row++) {

                    if (row % frequency == 0)
                        continue;

                    if (!hasEntityLocation(middleCol, row))
                        add(new Obstacle(), middleCol, row);
                }
                break;

            case 2:
                for (int col = room.getX(); col < room.getX() + room.getW(); col++) {
                    for (int row = room.getY(); row < room.getY() + room.getH(); row++) {

                        if (col % frequency != 0 || row % frequency != 0)
                            continue;

                        if (!hasEntityLocation(col, row))
                            add(new Obstacle(), col, row);
                    }
                }
                break;

            case 3:
                for (int col = room.getX(); col < room.getX() + room.getW(); col++) {

                    if (col % frequency == 0)
                        continue;

                    if (!hasEntityLocation(col, middleRow))
                        add(new Obstacle(), col, middleRow);
                }

                for (int row = room.getY(); row < room.getY() + room.getH(); row++) {

                    if (row % frequency == 0)
                        continue;

                    if (!hasEntityLocation(middleCol, row))
                        add(new Obstacle(), middleCol, row);
                }
                break;

            case 4:
                int offset = 4;
                if (!hasEntityLocation(room.getX() + offset, room.getY() + offset))
                    add(new Obstacle(), room.getX() + offset, room.getY() + offset);
                if (!hasEntityLocation(room.getX() + (room.getW() - 1) - offset, room.getY() + (room.getH() - 1) - offset))
                    add(new Obstacle(), room.getX() + (room.getW() - 1) - offset, room.getY() + (room.getH() - 1) - offset);
                if (!hasEntityLocation(room.getX() + (room.getW() - 1) - offset, room.getY() + offset))
                    add(new Obstacle(), room.getX() + (room.getW() - 1) - offset, room.getY() + offset);
                if (!hasEntityLocation(room.getX() + offset, room.getY() + (room.getH() - 1) - offset))
                    add(new Obstacle(), room.getX() + offset, room.getY() + (room.getH() - 1) - offset);
                break;
        }
    }

    private void addPillars(Room room) {

        Obstacle.TYPE = getRandomPillar();

        if (!room.hasWestDoor(getLevel().getDungeon()) && !room.hasEastDoor(getLevel().getDungeon())) {
            addPillarVertical(room, room.getX() + 1);
            addPillarVertical(room, room.getX() + room.getW() - 2);
        } else if (!room.hasSouthDoor(getLevel().getDungeon()) && !room.hasNorthDoor(getLevel().getDungeon())) {
            addPillarHorizontal(room, room.getY() + 1);
            addPillarHorizontal(room, room.getY() + room.getH() - 2);
        } else {
            if (getRandom().nextBoolean()) {
                addPillarHorizontal(room, room.getY() + (room.getH() / 2) + 1);
                addPillarHorizontal(room, room.getY() + (room.getH() / 2) - 1);
            } else {
                addPillarVertical(room, room.getX() + (room.getW() / 2) + 1);
                addPillarVertical(room, room.getX() + (room.getW() / 2) - 1);
            }
        }
    }

    private void addPillarVertical(Room room, int col) {
        for (int row = room.getY(); row < room.getY() + room.getH(); row++) {
            if (row % 2 != 0)
                continue;
            add(col, row);
        }
    }

    private void addPillarHorizontal(Room room, int row) {
        for (int col = room.getX(); col < room.getX() + room.getW(); col++) {
            if (col % 2 != 0)
                continue;
            add(col, row);
        }
    }

    private void add(int col, int row) {
        if (hasEntityLocation(col, row))
            return;
        if (nearInteract(col, row))
            return;
        add(new Obstacle(), col, row);
    }

    private boolean nearInteract(int col, int row) {

        //can't place it next to a door
        for (int x = -2; x <= 2; x++) {
            for (int y = -2; y <= 2; y++) {

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

        for (int i = 0; i < getEntityList().size(); i++) {

            //get the current entity
            Entity entity = getEntityList().get(i);

            if (!entity.isSolid())
                continue;

            if (getDistance(entity, x, y) < OBSTACLE_COLLISION)
                return true;
        }

        return false;
    }
}