package com.gamesbykevin.havoc.player;

import com.badlogic.gdx.math.Vector3;
import com.gamesbykevin.havoc.decals.DecalCustom;
import com.gamesbykevin.havoc.decals.Door;
import com.gamesbykevin.havoc.input.MyController;
import com.gamesbykevin.havoc.level.Level;

import static com.gamesbykevin.havoc.input.MyController.*;
import static com.gamesbykevin.havoc.input.MyController.VELOCITY_Z;
import static com.gamesbykevin.havoc.level.LevelHelper.ROOM_SIZE;
import static com.gamesbykevin.havoc.player.Player.*;

public class PlayerHelper {

    //distance from wall to detect collision
    public static final float WALL_DISTANCE = .175f;

    //how close we have to be to open a door
    public static final float DOOR_DISTANCE = 1.0f;

    protected static void updateLocation(MyController controller) {

        int xMove = 0;
        int yMove = 0;

        if (controller.isRunning()) {
            controller.setSpeed(SPEED_RUN);
        } else {
            controller.setSpeed(SPEED_WALK);
        }

        if (controller.isMoveForward())
            yMove++;
        if (controller.isMoveBackward())
            yMove--;

        if (controller.isStrafeLeft())
            xMove--;
        if (controller.isStrafeRight())
            xMove++;

        float rotationA = 0;

        if (controller.isTurnLeft()) {
            rotationA = controller.getSpeedRotate();
        } else if (controller.isTurnRight()) {
            rotationA = -controller.getSpeedRotate();
        }

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

        //if true we hit a wall
        if (level.getWalls()[row][col])
            return true;

        //is there a door here
        if (level.getDoors()[row][col]) {

            //door isn't open
            if (!level.getDoorsOpen()[row][col]) {
                return true;
            } else {
                level.getDoorDecals()[row][col].setLapsed(0);
            }
        }

        return false;
    }

    private static boolean checkCollision(MyController controller, final float x, final float y) {

        //which direction are we heading
        final float xDiff = x - controller.getPreviousPosition().x;
        final float yDiff = y - controller.getPreviousPosition().y;

        //figure out which room we are in
        float col = (x / ROOM_SIZE);
        float row = (y / ROOM_SIZE);

        //and then what specific column in the room
        float roomCol = (col * ROOM_SIZE);
        float roomRow = (row * ROOM_SIZE);

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

        //no collision
        return false;
    }

    protected static void updateLevel(Player player) {

        //if we are performing action check if we can open a door
        if (player.getController().isAction()) {

            //figure out which room we are in
            float col = (player.getController().getLevel().getCamera3d().position.x / ROOM_SIZE);
            float row = (player.getController().getLevel().getCamera3d().position.y / ROOM_SIZE);

            /*
            //is this the goal room
            boolean goal = (col == player.getController().getLevel().getMaze().getGoalCol() && row == player.getController().getLevel().getMaze().getGoalRow());

            //if this is the goal, create a new level
            if (goal) {

            }
            */

            //and then what specific column in the room
            float roomCol = (col * ROOM_SIZE);
            float roomRow = (row * ROOM_SIZE);

            boolean match = false;

            Door door = null;

            if (!match && player.getController().getLevel().getDoorDecals()[(int)(roomRow)][(int)(roomCol)] != null) {
                door = player.getController().getLevel().getDoorDecals()[(int)(roomRow)][(int)(roomCol)];
                match = true;
            }

            if (!match && player.getController().getLevel().getDoorDecals()[(int)(roomRow - DOOR_DISTANCE)][(int)(roomCol)] != null) {
                door = player.getController().getLevel().getDoorDecals()[(int)(roomRow - DOOR_DISTANCE)][(int)(roomCol)];
                match = true;
            }

            if (!match && player.getController().getLevel().getDoorDecals()[(int)(roomRow + DOOR_DISTANCE)][(int)(roomCol)] != null) {
                door = player.getController().getLevel().getDoorDecals()[(int)(roomRow + DOOR_DISTANCE)][(int)(roomCol)];
                match = true;
            }

            if (!match && player.getController().getLevel().getDoorDecals()[(int)(roomRow)][(int)(roomCol - DOOR_DISTANCE)] != null) {
                door = player.getController().getLevel().getDoorDecals()[(int)(roomRow)][(int)(roomCol - DOOR_DISTANCE)];
                match = true;
            }
            if (!match && player.getController().getLevel().getDoorDecals()[(int)(roomRow)][(int)(roomCol + DOOR_DISTANCE)] != null) {
                door = player.getController().getLevel().getDoorDecals()[(int)(roomRow)][(int)(roomCol + DOOR_DISTANCE)];
                match = true;
            }

            //flag the door to open
            if (match) {

                //we can only open the door if it's closed
                if (door.isClosed()) {
                    door.setClosed(false);
                    door.setOpening(true);
                    door.setLapsed(0);
                }
            }

            //set to false
            player.getController().setAction(false);
        }
    }

    protected static void updateWeapon(Player player) {

        if (player.getController().isChange()) {

            player.setWeaponIndex(player.getWeaponIndex() + 1);

            if (player.getWeaponIndex() >= player.getAnimations().size())
                player.setWeaponIndex(0);

            player.getController().setChange(false);
            player.getAnimation().reset();

        } else {

            if (player.getController().isShooting()) {

                //update weapon animation
                player.getAnimation().update();

                //continue to loop as long as we are shooting
                player.getAnimation().setLoop(true);

            } else {

                //finish animation
                if (player.getAnimation().getIndex() != 0)
                    player.getAnimation().update();

                //if we aren't shooting stop looping
                player.getAnimation().setLoop(false);
            }
        }

        //if we are walking, move weapon
        if (player.getController().isMoveForward() || player.getController().isMoveBackward()) {

            //move the weapon
            player.setWeaponX(player.getWeaponX() + player.getVelocityX());
            player.setWeaponY(player.getWeaponY() + player.getVelocityY());

            //use the correct velocity
            if (player.getWeaponX() >= WEAPON_X_MAX || player.getWeaponX() <= WEAPON_X_MIN)
                player.setVelocityX(-player.getVelocityX());
            if (player.getWeaponY() >= WEAPON_Y_MAX || player.getWeaponY() <= WEAPON_Y_MIN)
                player.setVelocityY(-player.getVelocityY());

        } else {

            //reset back to the default
            if (player.getWeaponX() != DEFAULT_WEAPON_X || player.getWeaponY() != DEFAULT_WEAPON_Y) {

                //move weapon position back to the default
                if (player.getWeaponX() < DEFAULT_WEAPON_X)
                    player.setWeaponX(player.getWeaponX() + DEFAULT_VELOCITY_X);
                if (player.getWeaponX() > DEFAULT_WEAPON_X)
                    player.setWeaponX(player.getWeaponX() - DEFAULT_VELOCITY_X);
                if (player.getWeaponY() < DEFAULT_WEAPON_Y)
                    player.setWeaponY(player.getWeaponY() + DEFAULT_VELOCITY_Y);
                if (player.getWeaponY() > DEFAULT_WEAPON_Y)
                    player.setWeaponY(player.getWeaponY() - DEFAULT_VELOCITY_Y);
                if (Math.abs(player.getWeaponX() - DEFAULT_WEAPON_X) <= DEFAULT_VELOCITY_X)
                    player.setWeaponX(DEFAULT_WEAPON_X);
                if (Math.abs(player.getWeaponY() - DEFAULT_VELOCITY_Y) <= DEFAULT_VELOCITY_Y)
                    player.setWeaponY(DEFAULT_WEAPON_Y);
            }
        }
    }
}