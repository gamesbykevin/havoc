package com.gamesbykevin.havoc.dungeon;

import com.gamesbykevin.havoc.util.Disposable;

public class Room implements Disposable {

    //location of room
    private int x, y;

    //size of room
    private int w, h;

    //is this a secret room?
    private boolean secret = false;

    //is this room the goal
    private boolean goal = false;

    public Room(int x, int y, int w, int h) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        setGoal(false);
        setSecret(false);
    }

    public boolean contains(float x, float y) {

        if (x < getX() || y < getY())
            return false;
        if (x >= getX() + getW() || y >= getY() + getH())
            return false;

        return true;
    }

    public boolean isSecret() {
        return this.secret;
    }

    public void setSecret(boolean secret) {
        this.secret = secret;
    }

    public boolean isGoal() {
        return this.goal;
    }

    public void setGoal(boolean goal) {
        this.goal = goal;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public int getW() {
        return this.w;
    }

    public int getH() {
        return this.h;
    }

    public boolean hasWestDoor(Dungeon dungeon) {
        return hasDoorV(dungeon, getX());
    }

    public boolean hasEastDoor(Dungeon dungeon) {
        return hasDoorV(dungeon, getX() + getW() - 1);
    }

    public boolean hasNorthDoor(Dungeon dungeon) {
        return hasDoorH(dungeon, getY() + getH() - 1);
    }

    public boolean hasSouthDoor(Dungeon dungeon) {
        return hasDoorH(dungeon, getY());
    }

    private boolean hasDoorV(Dungeon dungeon, int col) {
        for (int row = getY() - 1; row < getY() + getH() + 1; row++) {
            if (col < 0 || row < 0)
                continue;
            if (col >= dungeon.getCols() || row >= dungeon.getRows())
                continue;
            if (dungeon.getCell(col, row).isDoor())
                return true;
        }
        return false;
    }

    private boolean hasDoorH(Dungeon dungeon, int row) {
        for (int col = getX() - 1; col < getX() + getW() + 1; col++) {
            if (col < 0 || row < 0)
                continue;
            if (col >= dungeon.getCols() || row >= dungeon.getRows())
                continue;
            if (dungeon.getCell(col, row).isDoor())
                return true;
        }
        return false;
    }

    @Override
    public void dispose() {
        //dispose anything here?
    }
}