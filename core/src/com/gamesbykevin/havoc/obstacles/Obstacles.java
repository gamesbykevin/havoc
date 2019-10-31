package com.gamesbykevin.havoc.obstacles;

import com.gamesbykevin.havoc.entities.Entities;
import com.gamesbykevin.havoc.entities.Entity;
import com.gamesbykevin.havoc.level.Level;
import com.gamesbykevin.havoc.maze.Maze;
import com.gamesbykevin.havoc.maze.Room;

import static com.gamesbykevin.havoc.level.RoomHelper.ROOM_SIZE;
import static com.gamesbykevin.havoc.level.RoomHelper.ROOM_SIZE_SMALL;
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
        Candle, Fountain,
        spear1, spear2,
        flag1, flag2,
        barrel1, barrel2,
        pots1, pots2,
        oven1, oven2,
        DogFood, table,
        FloorLamp1, FloorLamp2,
        SpecimenPod1, SpecimenPod2, SpecimenPod3
    }

    //how close can we get to the obstacle
    private static final float OBSTACLE_COLLISION = .35f;

    public Obstacles(Level level) {
        super(level);
    }

    @Override
    public void spawn() {

        for (int col = 0; col < getLevel().getMaze().getCols(); col++) {
            for (int row = 0; row < getLevel().getMaze().getRows(); row++) {

                //skip the starting room
                if (col == getLevel().getMaze().getStartCol() && row == getLevel().getMaze().getStartRow())
                    continue;

                Room room = getLevel().getMaze().getRoom(col, row);

                //where we are starting for the current location
                int startCol = (col * ROOM_SIZE);
                int startRow = (row * ROOM_SIZE);

                /*
                switch (Maze.getRandom().nextInt(3)) {

                    //corners
                    case 0:
                        addCorners(startCol, startRow);
                        break;

                    //add pillars
                    case 1:
                        addPillars(startCol, startRow, room);
                        break;

                    //add lights
                    case 2:
                        addLights(startCol, startRow);
                        break;
                }
                */

                //add pillars
                addPillars(startCol, startRow, room);

                //add lights
                addLights(startCol, startRow);

                //add items on the 4 corners
                addCorners(startCol, startRow);

                //assign random type next to the walls
                addNextToWalls(startCol, startRow, room);
            }
        }
    }

    private void addCorners(int startCol, int startRow) {

        //assign random obstacle type
        assignRandomType();

        for (int offset = ROOM_SIZE_SMALL - 1; offset >= 1; offset--) {

            if (!hasEntityLocationCorner(startCol + offset, startRow + offset) &&
                    !hasEntityLocationCorner(startCol + offset, startRow + ROOM_SIZE - (offset + 1)) &&
                    !hasEntityLocationCorner(startCol + ROOM_SIZE - (offset + 1), startRow + ROOM_SIZE - (offset + 1)) &&
                    !hasEntityLocationCorner(startCol + ROOM_SIZE - (offset + 1), startRow + offset)
            ) {
                add(new Obstacle(), startCol + offset, startRow + offset);
                add(new Obstacle(), startCol + offset, startRow + ROOM_SIZE - (offset + 1));
                add(new Obstacle(), startCol + ROOM_SIZE - (offset + 1), startRow + ROOM_SIZE - (offset + 1));
                add(new Obstacle(), startCol + ROOM_SIZE - (offset + 1), startRow + offset);
                break;
            }
        }
    }

    private void addNextToWalls(int startCol, int startRow, Room room) {

        //assign random obstacle type
        assignRandomType();

        //how frequent do we add an obstacle
        int offset = Maze.getRandom().nextInt(4) + 1;

        if (room.hasWest()) {
            for (int row = startRow; row < startRow + ROOM_SIZE; row++) {

                if (row % offset != 0)
                    continue;

                if (hasEntityLocation(startCol + 1, row))
                    continue;

                add(new Obstacle(), startCol + 1, row);
            }
        }

        if (room.hasEast()) {
            for (int row = startRow; row < startRow + ROOM_SIZE; row++) {

                if (row % offset != 0)
                    continue;

                if (hasEntityLocation(startCol + ROOM_SIZE - 2, row))
                    continue;

                add(new Obstacle(), startCol + ROOM_SIZE - 2, row);
            }
        }

        if (room.hasNorth()) {
            for (int col = startCol; col < startCol + ROOM_SIZE; col++) {

                if (col % offset != 0)
                    continue;

                if (hasEntityLocation(col, startRow + ROOM_SIZE - 2))
                    continue;

                add(new Obstacle(), col, startRow + ROOM_SIZE - 2);
            }
        }

        if (room.hasSouth()) {
            for (int col = startCol; col < startCol + ROOM_SIZE; col++) {

                if (col % offset != 0)
                    continue;

                if (hasEntityLocation(col, startRow + 1))
                    continue;

                add(new Obstacle(), col, startRow + 1);
            }
        }
    }

    private void addPillars(int startCol, int startRow, Room room) {

        assignRandomPillar();

        if (room.hasWest() && room.hasEast()) {

            //row we will place obstacles at
            int tmpRow = startRow + (ROOM_SIZE / 2);

            //add pillars horizontally
            addPillarHorizontal(startCol, tmpRow);

        } else if (room.hasNorth() && room.hasSouth()) {

            //column we will place obstacles at
            int tmpCol = startCol + (ROOM_SIZE / 2);

            //add pillar vertically
            addPillarVertical(startRow, tmpCol);

        } else {

            if (Maze.getRandom().nextBoolean()) {

                //row we will place obstacles at
                int tmpRow = startRow + (ROOM_SIZE / 2);

                //add pillars horizontally
                addPillarHorizontal(startCol, tmpRow + 1);
                addPillarHorizontal(startCol, tmpRow - 1);

            } else {

                //column we will place obstacles at
                int tmpCol = startCol + (ROOM_SIZE / 2);

                //add pillar vertically
                addPillarVertical(startRow, tmpCol + 1);
                addPillarVertical(startRow, tmpCol - 1);
            }
        }
    }

    private void addLights(int startCol, int startRow) {

        //frequency of lights
        int frequency = Maze.getRandom().nextInt(4) + 1;

        assignRandomLight();

        switch (Maze.getRandom().nextInt(4)) {

            case 0:
                for (int tmpCol = startCol; tmpCol < startCol + ROOM_SIZE; tmpCol++) {

                    if (tmpCol % frequency == 0)
                        continue;

                    if (!hasEntityLocation(tmpCol, startRow + (ROOM_SIZE / 2)))
                        add(new Obstacle(), tmpCol, startRow + (ROOM_SIZE / 2));
                }
                break;

            case 1:
                for (int tmpRow = startRow; tmpRow < startRow + ROOM_SIZE; tmpRow++) {

                    if (tmpRow % frequency == 0)
                        continue;

                    if (!hasEntityLocation(startCol + (ROOM_SIZE / 2), tmpRow))
                        add(new Obstacle(), startCol + (ROOM_SIZE / 2), tmpRow);
                }
                break;

            case 2:
                for (int tmpCol = startCol; tmpCol < startCol + ROOM_SIZE; tmpCol++) {

                    for (int tmpRow = startRow; tmpRow < startRow + ROOM_SIZE; tmpRow++) {

                        if (tmpCol % frequency != 0 || tmpRow % frequency != 0)
                            continue;

                        if (!hasEntityLocation(tmpCol, tmpRow))
                            add(new Obstacle(), tmpCol, tmpRow);
                    }
                }
                break;

            case 3:
                int offset = (ROOM_SIZE_SMALL / 2);
                if (!hasEntityLocation(startCol + offset, startRow + offset))
                    add(new Obstacle(), startCol + offset, startRow + offset);
                if (!hasEntityLocation(startCol + ROOM_SIZE - offset, startRow + ROOM_SIZE - offset))
                    add(new Obstacle(), startCol + ROOM_SIZE - offset, startRow + ROOM_SIZE - offset);
                if (!hasEntityLocation(startCol + ROOM_SIZE - offset, startRow + offset))
                    add(new Obstacle(), startCol + ROOM_SIZE - offset, startRow + offset);
                if (!hasEntityLocation(startCol + offset, startRow + ROOM_SIZE - offset))
                    add(new Obstacle(), startCol + offset, startRow + ROOM_SIZE - offset);
                break;
        }
    }

    private boolean hasEntityLocationCorner(int col, int row) {

        if (hasEntityLocation(col, row))
            return true;

        //don't place next to a door
        if (getLevel().hasDoor(col + 1, row))
            return true;
        if (getLevel().hasDoor(col - 1, row))
            return true;
        if (getLevel().hasDoor(col, row + 1))
            return true;
        if (getLevel().hasDoor(col, row - 1))
            return true;

        return false;
    }

    private void addPillarVertical(int startRow, int tmpCol) {

        for (int tmpRow = startRow; tmpRow < startRow + ROOM_SIZE; tmpRow++) {

            if (tmpRow % 2 == 0)
                continue;

            if (hasEntityLocation(tmpCol, tmpRow))
                continue;

            //can't place it next to a door
            if (getLevel().hasDoor(tmpCol + 1, tmpRow))
                continue;
            if (getLevel().hasDoor(tmpCol - 1, tmpRow))
                continue;
            if (getLevel().hasDoor(tmpCol, tmpRow + 1))
                continue;
            if (getLevel().hasDoor(tmpCol, tmpRow - 1))
                continue;

            //we won't place on the corner where there is no free space
            if (!getLevel().hasFree(tmpCol - 1, tmpRow - 1))
                continue;
            if (!getLevel().hasFree(tmpCol + 1, tmpRow + 1))
                continue;
            if (!getLevel().hasFree(tmpCol + 1, tmpRow - 1))
                continue;
            if (!getLevel().hasFree(tmpCol - 1, tmpRow + 1))
                continue;

            //placing we should have open space on the sides
            if (!getLevel().hasFree(tmpCol - 1, tmpRow) || !getLevel().hasFree(tmpCol + 1, tmpRow))
                continue;

            add(new Obstacle(), tmpCol, tmpRow);
        }
    }

    private void addPillarHorizontal(int startCol, int tmpRow) {

        for (int tmpCol = startCol; tmpCol < startCol + ROOM_SIZE; tmpCol++) {

            if (tmpCol % 2 == 0)
                continue;

            if (hasEntityLocation(tmpCol, tmpRow))
                continue;

            if (getLevel().hasDoor(tmpCol + 1, tmpRow))
                continue;
            if (getLevel().hasDoor(tmpCol - 1, tmpRow))
                continue;
            if (getLevel().hasDoor(tmpCol, tmpRow + 1))
                continue;
            if (getLevel().hasDoor(tmpCol, tmpRow - 1))
                continue;

            //we won't place on the corner where there is no free space
            if (!getLevel().hasFree(tmpCol - 1, tmpRow - 1))
                continue;
            if (!getLevel().hasFree(tmpCol + 1, tmpRow + 1))
                continue;
            if (!getLevel().hasFree(tmpCol + 1, tmpRow - 1))
                continue;
            if (!getLevel().hasFree(tmpCol - 1, tmpRow + 1))
                continue;

            //placing we should have open space on the sides
            if (!getLevel().hasFree(tmpCol, tmpRow - 1) || !getLevel().hasFree(tmpCol, tmpRow + 1))
                continue;

            add(new Obstacle(), tmpCol, tmpRow);
        }
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