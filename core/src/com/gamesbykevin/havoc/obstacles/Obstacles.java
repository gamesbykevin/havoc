package com.gamesbykevin.havoc.obstacles;

import com.gamesbykevin.havoc.entities.Entities;
import com.gamesbykevin.havoc.entities.Entity;
import com.gamesbykevin.havoc.level.Level;
import com.gamesbykevin.havoc.maze.Maze;
import com.gamesbykevin.havoc.maze.Room;

import static com.gamesbykevin.havoc.level.RoomHelper.ROOM_SIZE;
import static com.gamesbykevin.havoc.level.RoomHelper.ROOM_SIZE_SMALL;

public final class Obstacles extends Entities {

    public enum Type {
        BluePot, BluePotPlant, BronzeCol, Chandelier, DogFood, FloorLamp, GreenC, RedC,
        SilverCol, SpecimenPod1, SpecimenPod2, SpecimenPod3, Well, WellBlood, WellWater
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

                switch (Maze.getRandom().nextInt(3)) {

                    //corners
                    case 0:
                    default:

                        int index = Maze.getRandom().nextInt(10);
                        switch (index) {
                            case 0:
                                Obstacle.TYPE = Type.SpecimenPod1;
                                break;

                            case 1:
                                Obstacle.TYPE = Type.SpecimenPod2;
                                break;

                            case 2:
                                Obstacle.TYPE = Type.SpecimenPod3;
                                break;

                            case 3:
                                Obstacle.TYPE = Type.FloorLamp;
                                break;

                            case 4:
                                Obstacle.TYPE = Type.BluePot;
                                break;

                            case 5:
                                Obstacle.TYPE = Type.BluePotPlant;
                                break;

                            case 6:
                                Obstacle.TYPE = Type.DogFood;
                                break;

                            case 7:
                                Obstacle.TYPE = Type.Well;
                                break;

                            case 8:
                                Obstacle.TYPE = Type.WellBlood;
                                break;

                            case 9:
                                Obstacle.TYPE = Type.WellWater;
                                break;
                        }

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
                        break;

                    //add pillars
                    case 1:

                        //pick a random pillar
                        Obstacle.TYPE = (Maze.getRandom().nextBoolean()) ? Type.BronzeCol : Type.SilverCol;

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
                        break;

                    //add lights
                    case 2:

                        //frequency of lights
                        int frequency = Maze.getRandom().nextInt(3) + 2;

                        //pick random light
                        switch (Maze.getRandom().nextInt(3)) {

                            case 0:
                                Obstacle.TYPE = Type.Chandelier;
                                break;

                            case 1:
                                Obstacle.TYPE = Type.GreenC;
                                break;

                            case 2:
                                Obstacle.TYPE = Type.RedC;
                                break;
                        }

                        switch (Maze.getRandom().nextInt(3)) {

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
                        break;
                }
            }
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