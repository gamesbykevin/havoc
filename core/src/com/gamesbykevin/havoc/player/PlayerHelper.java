package com.gamesbykevin.havoc.player;

import com.badlogic.gdx.math.Vector3;
import com.gamesbykevin.havoc.collectables.Collectible;
import com.gamesbykevin.havoc.decals.Door;
import com.gamesbykevin.havoc.entities.Entity;
import com.gamesbykevin.havoc.input.MyController;
import com.gamesbykevin.havoc.level.Level;
import com.gamesbykevin.havoc.player.weapon.*;

import static com.gamesbykevin.havoc.MyGdxGame.SIZE_HEIGHT;
import static com.gamesbykevin.havoc.MyGdxGame.SIZE_WIDTH;
import static com.gamesbykevin.havoc.input.MyController.*;
import static com.gamesbykevin.havoc.player.weapon.WeaponHelper.AMMO_LARGE;
import static com.gamesbykevin.havoc.player.weapon.WeaponHelper.AMMO_SMALL;

public class PlayerHelper {

    //distance from wall to detect collision
    public static final float WALL_DISTANCE = .175f;

    //how close we have to be to open a door
    public static final float DOOR_DISTANCE = 1.0f;

    //if we aren't moving the joystick enough we will ignore
    private static final float DEADZONE_IGNORE = .2f;

    //where to render our hud items
    public static final float HUD_RATIO = .5f;
    public static final int HUD_NUMBER_WIDTH = (int)(58 * HUD_RATIO);
    public static final int HUD_NUMBER_HEIGHT = (int)(70 * HUD_RATIO);
    public static final int HUD_KEY_WIDTH = (int)(20 * HUD_RATIO);
    public static final int HUD_KEY_HEIGHT = (int)(54 * HUD_RATIO);

    public static final int HUD_NUMBER_PAD = (int)(5 * HUD_RATIO);
    public static final int HUD_BULLET_X = SIZE_WIDTH - (3 * HUD_NUMBER_WIDTH) - (3 * HUD_NUMBER_PAD);
    public static final int HUD_BULLET_Y = SIZE_HEIGHT - (HUD_NUMBER_HEIGHT);
    public static final int HUD_HEALTH_X = (3 * HUD_NUMBER_PAD);
    public static final int HUD_HEALTH_Y = SIZE_HEIGHT - HUD_NUMBER_HEIGHT - (HUD_NUMBER_PAD * 2);

    public static final int HEALTH_SMALL = 10;
    public static final int HEALTH_LARGE = 25;

    protected static void updateLocation(MyController controller) {

        double xMove = 0;
        double yMove = 0;

        //determine the speed of the player
        if (controller.isRunning()) {
            controller.setSpeed(SPEED_RUN);
        } else {
            controller.setSpeed(SPEED_WALK);
        }

        //which way are we turning?
        float rotationA = 0;

        if (controller.isMoveForward())
            yMove++;
        if (controller.isMoveBackward())
            yMove--;

        if (controller.isStrafeLeft())
            xMove--;
        if (controller.isStrafeRight())
            xMove++;

        if (controller.isTurnLeft()) {
            rotationA = controller.getSpeedRotate();
        } else if (controller.isTurnRight()) {
            rotationA = -controller.getSpeedRotate();
        }

        //if we aren't moving let's check the joystick
        if (yMove == 0 && xMove == 0) {

            //make sure we are moving enough
            if (controller.getKnobPercentY() < -DEADZONE_IGNORE) {
                yMove = controller.getKnobPercentY() + DEADZONE_IGNORE;
            } else if (controller.getKnobPercentY() > DEADZONE_IGNORE) {
                yMove = controller.getKnobPercentY() - DEADZONE_IGNORE;
            }

            //make sure we are moving enough
            if (controller.getKnobPercentX() < -DEADZONE_IGNORE) {
                rotationA = controller.getSpeedRotate() * (-controller.getKnobPercentX() - DEADZONE_IGNORE);
            } else if (controller.getKnobPercentX() > DEADZONE_IGNORE) {
                rotationA = controller.getSpeedRotate() * (-controller.getKnobPercentX() + DEADZONE_IGNORE);
            }
        }

        //convert to radians
        double angle = Math.toRadians(controller.getRotation());

        //calculate distance moved
        double xa = (xMove * Math.cos(angle)) - (yMove * Math.sin(angle));
        xa *= controller.getSpeed();
        double ya = (yMove * Math.cos(angle)) + (xMove * Math.sin(angle));
        ya *= controller.getSpeed();

        //store previous position
        if (xMove != 0 || yMove != 0) {
            controller.getPreviousPosition().x = controller.getCamera3d().position.x;
            controller.getPreviousPosition().y = controller.getCamera3d().position.y;
            controller.getPreviousPosition().z = controller.getCamera3d().position.z;
        }

        //update camera location?
        controller.getCamera3d().position.x += xa;
        controller.getCamera3d().position.y += ya;
        controller.getCamera3d().rotate(Vector3.Z, rotationA);

        /*
        if (controller.isMoveForward() || controller.isMoveBackward()) {

            controller.getCamera3d().position.z += VELOCITY_Z;

            if (controller.getCamera3d().position.z <= MIN_Z) {
                controller.getCamera3d().position.z = MIN_Z;
                VELOCITY_Z = -VELOCITY_Z;
            } else if (controller.getCamera3d().position.z >= MAX_Z) {
                controller.getCamera3d().position.z = MAX_Z;
                VELOCITY_Z = -VELOCITY_Z;
            }
        }
        */

        if (rotationA != 0) {

            //add rotation angle to overall rotation
            controller.setRotation(controller.getRotation() + rotationA);

            //keep radian value from getting to large/small
            if (controller.getRotation() > 360)
                controller.setRotation(0);
            if (controller.getRotation() < 0)
                controller.setRotation(360);
        }
    }

    protected static void checkCollision(MyController controller) {

        //reset position if there is a collision
        if (checkCollision(controller, controller.getPreviousPosition().x, controller.getCamera3d().position.y))
            controller.getCamera3d().position.y = controller.getPreviousPosition().y;
        if (checkCollision(controller, controller.getCamera3d().position.x, controller.getPreviousPosition().y))
            controller.getCamera3d().position.x = controller.getPreviousPosition().x;
    }

    private static boolean checkBounds(Level level, float row, float col) {
        return checkBounds(level, (int)row, (int)col);
    }

    private static boolean checkBounds(Level level, int row, int col) {

        //if not true we aren't in open space
        if (!level.getDungeon().hasMap(col, row))
            return true;

        //is there a door/switch here
        if (level.getDungeon().hasInteract(col, row)) {

            Door door = level.getDoorDecal(col, row);

            //door isn't open
            if (!door.isOpen()) {
                return true;
            } else {
                door.setLapsed(0);
            }
        }

        return false;
    }

    private static boolean checkCollision(MyController controller, final float x, final float y) {

        //which direction are we heading
        final float xDiff = x - controller.getPreviousPosition().x;
        final float yDiff = y - controller.getPreviousPosition().y;

        //and then what specific column in the room
        float roomCol = controller.getLevel().getCamera3d().position.x;
        float roomRow = controller.getLevel().getCamera3d().position.y;

        if (checkBounds(controller.getLevel(), roomRow, roomCol))
            return true;

        if (yDiff != 0) {
            if (checkBounds(controller.getLevel(), roomRow + WALL_DISTANCE, roomCol))
                return true;
            if (checkBounds(controller.getLevel(), roomRow - WALL_DISTANCE, roomCol))
                return true;
        }

        if (xDiff != 0) {
            if (checkBounds(controller.getLevel(), roomRow, roomCol + WALL_DISTANCE))
                return true;
            if (checkBounds(controller.getLevel(), roomRow, roomCol - WALL_DISTANCE))
                return true;
        }

        if (yDiff != 0 || xDiff != 0) {
            if (checkBounds(controller.getLevel(), roomRow + WALL_DISTANCE, roomCol + WALL_DISTANCE))
                return true;
            if (checkBounds(controller.getLevel(), roomRow - WALL_DISTANCE, roomCol + WALL_DISTANCE))
                return true;
            if (checkBounds(controller.getLevel(), roomRow + WALL_DISTANCE, roomCol - WALL_DISTANCE))
                return true;
            if (checkBounds(controller.getLevel(), roomRow - WALL_DISTANCE, roomCol - WALL_DISTANCE))
                return true;
        }

        //if collision with an enemy return true
        if (controller.getLevel().getEnemies().hasCollision(x, y))
            return true;

        //if collision with an obstacle return true
        if (controller.getLevel().getObstacles().hasCollision(x, y))
            return true;

        //no collision
        return false;
    }

    protected static void checkCollectible(MyController controller, Player player) {

        Vector3 position = controller.getCamera3d().position;

        //check the whole list
        for (int i = 0; i < controller.getLevel().getCollectibles().getEntityList().size(); i++) {

            //get the current entity
            Entity entity = controller.getLevel().getCollectibles().getEntityList().get(i);

            //skip if not solid
            if (!entity.isSolid())
                continue;

            //if we have collision
            if (entity.hasCollision(position.x, position.y)) {

                //flag false so we can't collect again
                entity.setSolid(false);

                //check the collectible
                Collectible collectible = (Collectible)entity;

                switch (collectible.getType()) {

                    case smg:
                        player.addWeapon(Weapon.Type.Smg);
                        break;

                    case glock:
                        player.addWeapon(Weapon.Type.Glock);
                        break;

                    case impact:
                        player.addWeapon(Weapon.Type.Impact);
                        break;

                    case magnum:
                        player.addWeapon(Weapon.Type.Magnum);
                        break;

                    case buzzsaw:
                        player.addWeapon(Weapon.Type.Buzz);
                        break;

                    case shotgun:
                        player.addWeapon(Weapon.Type.Shotgun);
                        break;

                    case ammo:
                        player.getWeapon().setBullets(player.getWeapon().getBullets() + AMMO_SMALL);
                        break;

                    case ammo_crate:
                        player.getWeapon().setBullets(player.getWeapon().getBullets() + AMMO_LARGE);
                        break;

                    case health_large:
                        player.setHealth(player.getHealth() + HEALTH_LARGE);
                        break;

                    case health_small:
                        player.setHealth(player.getHealth() + HEALTH_SMALL);
                        break;

                    case key:
                        player.setKey(true);
                        break;
                }
            }
        }
    }

    protected static void updateLevel(Player player) {

        //if we are performing action check if we can open a door or hit a switch
        if (player.getController().isAction()) {

            //level instance
            Level level = player.getController().getLevel();

            for (float colDiff = -DOOR_DISTANCE; colDiff <= DOOR_DISTANCE; colDiff += DOOR_DISTANCE) {
                for (float rowDiff = -DOOR_DISTANCE; rowDiff <= DOOR_DISTANCE; rowDiff += DOOR_DISTANCE) {

                    //avoid checking diagonally
                    if (colDiff != 0 && rowDiff != 0)
                        continue;

                    int col = (int)(level.getCamera3d().position.x + colDiff);
                    int row = (int)(level.getCamera3d().position.y + rowDiff);

                    //if we can't interact skip to the next
                    if (!level.getDungeon().hasInteract(col, row))
                        continue;

                    switch (level.getDungeon().getCells()[row][col].getType()) {
                        case Door:
                        case DoorLocked:
                        case Secret:
                            Door door = level.getDoorDecal(col, row);

                            //we can only open the door if it exists and it's closed
                            if (door != null && door.isClosed()) {

                                //also make sure if it's locked that we have a key
                                if (!door.isLocked() || (door.isLocked() && player.hasKey())) {
                                    door.setClosed(false);
                                    door.setOpening(true);
                                    door.setLapsed(0);
                                }
                            }
                            break;

                        case Goal:
                            player.getController().setRotation(0);
                            level.resetPosition();
                            level.getEnemies().reset();
                            break;
                    }
                }
            }

            //set action back to false
            player.getController().setAction(false);
        }
    }
}