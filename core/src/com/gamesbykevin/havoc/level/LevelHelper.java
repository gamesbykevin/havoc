package com.gamesbykevin.havoc.level;

import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.decals.DecalBatch;
import com.badlogic.gdx.math.Vector3;
import com.gamesbykevin.havoc.decals.DecalCustom;
import com.gamesbykevin.havoc.decals.Door;
import com.gamesbykevin.havoc.dungeon.Cell;

import java.util.List;

import static com.gamesbykevin.havoc.level.Level.RENDER_RANGE;
import static com.gamesbykevin.havoc.util.Distance.getDistance;

public class LevelHelper {

    //how close we have to be to open a door
    public static final float DOOR_DISTANCE = 1.0f;

    protected static int renderWalls(List<DecalCustom> walls, DecalBatch batch, PerspectiveCamera camera) {

        int count = 0;

        for (int i = 0; i < walls.size(); i++) {

            DecalCustom decal = walls.get(i);

            //only render if in range
            if (getDistance(decal, camera.position) > RENDER_RANGE)
                continue;

            if (decal.isBillboard())
                decal.getDecal().lookAt(camera.position, camera.up);

            count++;
            batch.add(decal.getDecal());
        }

        //return number of items rendered
        return count;
    }

    protected static int renderBackground(List<DecalCustom> backgrounds, Vector3 position, DecalBatch batch) {

        int count = 0;

        //render the backgrounds
        for (int i = 0; i < backgrounds.size(); i++) {
            DecalCustom decal = backgrounds.get(i);

            //only render if in range
            if (getDistance(decal, position) > RENDER_RANGE * 1.25)
                continue;

            count++;
            batch.add(decal.getDecal());
        }

        //return number of items rendered
        return count;
    }

    protected static int renderDoorDecals(Door[][] decals, DecalBatch batch, PerspectiveCamera camera) {

        int count = 0;

        for (int col = 0; col < decals[0].length; col++) {
            for (int row = 0; row < decals.length; row++) {

                DecalCustom decal = decals[row][col];

                if (decal == null)
                    continue;

                //only render if in range
                if (getDistance(decal, camera.position) > RENDER_RANGE)
                    continue;

                if (decal.isBillboard())
                    decal.getDecal().lookAt(camera.position, camera.up);

                count++;
                batch.add(decal.getDecal());
            }
        }

        //return number of items rendered
        return count;
    }

    public static boolean updateInteract(Level level, Vector3 position, boolean key) {

        boolean goal = false;

        for (float colDiff = -DOOR_DISTANCE; colDiff <= DOOR_DISTANCE; colDiff += DOOR_DISTANCE) {
            for (float rowDiff = -DOOR_DISTANCE; rowDiff <= DOOR_DISTANCE; rowDiff += DOOR_DISTANCE) {

                int col = (int)(position.x + colDiff);
                int row = (int)(position.y + rowDiff);

                //if we can't interact at this location skip to the next
                if (!level.getDungeon().hasInteract(col, row))
                    continue;

                //get the current cell
                Cell cell = level.getDungeon().getCell(col, row);

                //is the cell a door
                if (cell.isDoor()) {

                    //make sure if it's locked that we have a key
                    if (!cell.isLocked() || (cell.isLocked() && key)) {

                        Door door = level.getDoorDecal(col, row);

                        //we can only open the door if it exists and it's closed
                        if (door != null && door.isClosed()) {

                            //open the door
                            door.open();

                            //if there is a linked door
                            if (cell != null && cell.getLink() != null) {

                                //open the linked door as long as long as we can
                                if (!cell.getLink().isLocked())
                                    level.getDoorDecal(cell.getLink().getCol(), cell.getLink().getRow()).open();
                            }
                        }
                    }

                } else if (cell.isGoal()) {
                    System.out.println("LEVEL COMPLETE!!!!!!!!!!!!!!!!!!!");
                    goal = true;
                    /*
                    player.getController().setRotation(0);
                    level.resetPosition();
                    level.getEnemies().reset();
                    */
                }
            }
        }

        //did the player interact with the goal?
        return goal;
    }

    protected static void updateLevel(Level level) {

        updateDoors(level.getDoorDecals());

        //if we are performing action check if we can open a door or hit a switch
        if (level.getPlayer().getController().isAction()) {

            //interact with the level
            boolean goal = updateInteract(level, level.getPlayer().getCamera3d().position, level.getPlayer().hasKey());

            //set action back to false
            level.getPlayer().getController().setAction(false);
        }
    }

    private static void updateDoors(Door[][] doors) {

        for (int col = 0; col < doors[0].length; col++) {
            for (int row = 0; row < doors.length; row++) {

                if (doors[row][col] == null)
                    continue;

                //update the door
                doors[row][col].update();
            }
        }
    }

    public static boolean isDoorOpen(Level level, int col, int row) {

        //get the door
        Door door = level.getDoorDecal(col, row);

        //if the door is not there return false
        if (door == null)
            return false;

        //is the door open?
        return door.isOpen();
    }
}