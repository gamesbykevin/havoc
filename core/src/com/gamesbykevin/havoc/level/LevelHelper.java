package com.gamesbykevin.havoc.level;

import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.decals.DecalBatch;
import com.badlogic.gdx.math.Vector3;
import com.gamesbykevin.havoc.assets.AudioHelper;
import com.gamesbykevin.havoc.decals.DecalCustom;
import com.gamesbykevin.havoc.decals.Door;
import com.gamesbykevin.havoc.dungeon.Cell;
import com.gamesbykevin.havoc.player.Player;

import java.util.List;

import static com.gamesbykevin.havoc.assets.AudioHelper.playSfx;
import static com.gamesbykevin.havoc.decals.Door.DOOR_DISTANCE_SFX_RATIO;
import static com.gamesbykevin.havoc.dungeon.RoomHelper.ROOM_DIMENSION_MAX;
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

                    Door door = level.getDoorDecal(col, row);

                    if (door == null)
                        continue;

                    //is the door closed
                    boolean closed = (door.getState() == Door.State.Closed);

                    //we can only open the door if it's closed
                    if (!closed)
                        continue;

                    if (cell.isLocked()) {

                        if (!key) {

                            //if locked and we don't have a key
                            playSfx(level.getAssetManager(), AudioHelper.Sfx.LevelLocked);
                            continue;
                        }
                    }

                    //open the door
                    door.open();

                    //if there is a linked door
                    if (cell != null && cell.getLink() != null) {

                        //open the linked door as long as it's not locked
                        if (!cell.getLink().isLocked())
                            level.getDoorDecal(cell.getLink().getCol(), cell.getLink().getRow()).open();
                    }

                } else if (cell.isGoal()) {

                    //sound we completed the level
                    playSfx(level.getAssetManager(), AudioHelper.Sfx.LevelSwitch);
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

    private static void updateDoors(Level level) {

        //if opening or closing we only want to play 1 sound effect
        boolean open = false, close = false, secret = false;

        for (int col = 0; col < level.getDoorDecals()[0].length; col++) {
            for (int row = 0; row < level.getDoorDecals().length; row++) {

                //get the current door
                Door door = level.getDoorDecal(col, row);

                if (door == null)
                    continue;

                //is the door currently opened
                boolean opened = false;

                switch (door.getState()) {
                    case Start:

                        //only need to confirm open once
                        if (!open && !door.isSecret()) {
                            if (getDistance(level.getPlayer(), door) <= ROOM_DIMENSION_MAX * DOOR_DISTANCE_SFX_RATIO)
                                open = true;
                        }

                        if (!secret && door.isSecret()) {
                            if (getDistance(level.getPlayer(), door) <= ROOM_DIMENSION_MAX * DOOR_DISTANCE_SFX_RATIO)
                                secret = true;
                        }
                        break;

                    case Open:
                        opened = true;
                        break;
                }

                //update the door
                door.update();

                switch (door.getState()) {

                    case Closing:

                        if (!close && opened) {
                            if (getDistance(level.getPlayer(), door) <= ROOM_DIMENSION_MAX * DOOR_DISTANCE_SFX_RATIO)
                                close = true;
                        }
                        break;
                }
            }
        }

        //only play the sound effects a single time
        if (secret) {
            playSfx(level.getAssetManager(), AudioHelper.Sfx.LevelSecret);
        } else if (open) {
            playSfx(level.getAssetManager(), AudioHelper.Sfx.LevelOpen);
        }

        if (close)
            playSfx(level.getAssetManager(), AudioHelper.Sfx.LevelClose);
    }

    public static boolean isDoorOpen(Level level, float col, float row) {
        return isDoorOpen(level, (int)col, (int)row);
    }

    public static boolean isDoorOpen(Level level, int col, int row) {

        //get the door
        Door door = level.getDoorDecal(col, row);

        //if the door is not there return false
        if (door == null)
            return false;

        switch (door.getState()) {
            case Open:
                return true;

            default:
                return false;
        }
    }


    protected static void updateLevel(Level level) {

        //update the doors in the level
        updateDoors(level);

        Player player = level.getPlayer();

        //if we are performing action check if we can open a door or hit a switch
        if (player.getController().isAction()) {

            //interact with the level
            boolean goal = updateInteract(level, player.getCamera3d().position, player.hasKey());

            //did the player interact with the goal?
            player.setGoal(goal);

            //if the player got the goal calculate the totals
            if (player.isGoal()) {

                //how many enemies are there?
                float enemiesTotal = level.getEnemies().getEntityList().size();

                //how many enemies did we kill?
                float enemiesKilled = 0;
                for (int i = 0; i < level.getEnemies().getEntityList().size(); i++) {
                    if (!level.getEnemies().getEntityList().get(i).isSolid())
                        enemiesKilled++;
                }

                //total # of collectibles
                float collectiblesTotal = level.getCollectibles().getEntityList().size();

                float collectiblesConsumed = 0;
                for (int i = 0; i < level.getCollectibles().getEntityList().size(); i++) {
                    if (!level.getCollectibles().getEntityList().get(i).isSolid())
                        collectiblesConsumed++;
                }

                float secretTotal = 0;
                float secretOpen = 0;

                for (int row = 0; row < level.getDoorDecals().length; row++) {
                    for (int col = 0; col < level.getDoorDecals()[0].length; col++) {

                        Door door = level.getDoorDecal(col, row);

                        if (door == null)
                            continue;

                        if (door.isSecret())
                            secretTotal++;
                        if (door.isOnce())
                            secretOpen++;
                    }
                }

                System.out.println("Enemies: " + enemiesKilled + " of " + enemiesTotal);
                System.out.println("Collectibles: " + collectiblesConsumed + " of " + collectiblesTotal);
                System.out.println("Secrets:" + secretOpen + " of " + secretTotal);
            }

            //set action back to false
            player.getController().setAction(false);
        }
    }
}