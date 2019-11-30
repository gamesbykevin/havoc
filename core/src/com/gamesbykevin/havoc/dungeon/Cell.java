package com.gamesbykevin.havoc.dungeon;

import com.gamesbykevin.havoc.guid.GUID;
import com.gamesbykevin.havoc.util.Disposable;

public class Cell implements Disposable {

    //location of cell
    private int col, row;

    //cells part of a room will have the same id
    private String id;

    //is this cell connected to another?
    private Cell link;

    //what is this cell
    private Type type;

    //the cost
    private int cost = -1;

    public enum Type {
        Wall,
        Open,
        Door,
        DoorLocked,
        Secret,
        Goal,
        Unvisited
    }

    public Cell(int col, int row, Type type) {
        this.col = col;
        this.row = row;
        this.id = GUID.generate();
        this.type = type;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public int getCol() {
        return this.col;
    }

    public int getRow() {
        return this.row;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public String getId() {
        return this.id;
    }

    public boolean hasId(Cell cell) {
        return hasId(cell.getId());
    }

    public boolean hasId(String id) {
        return getId().equals(id);
    }

    public void setId(Cell cell) {
        setId(cell.getId());
    }

    public void setId(String id) {
        this.id = id;
    }

    private Type getType() {
        return this.type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Cell getLink() {
        return this.link;
    }

    public void setLink(Cell link) {
        this.link = link;
    }

    public boolean isLocked() {
        switch (getType()) {
            case DoorLocked:
                return true;

            default:
                return false;
        }
    }

    public boolean isSecret() {
        switch (getType()) {
            case Secret:
                return true;

            default:
                return false;
        }
    }

    public boolean isGoal() {

        switch (getType()) {
            case Goal:
                return true;

            default:
                return false;
        }
    }

    public boolean isWall() {
        switch (getType()) {
            case Wall:
                return true;

            default:
                return false;
        }
    }

    public boolean isDoor() {
        switch (getType()) {
            case DoorLocked:
            case Door:
            case Secret:
                return true;

            default:
                return false;
        }
    }

    public boolean isOpen() {
        switch (getType()) {
            case Open:
                return true;

            default:
                return false;
        }
    }

    public boolean isUnvisited() {
        switch (getType()) {
            case Unvisited:
                return true;

            default:
                return false;
        }
    }

    public void print() {
        switch (getType()) {

            case Secret:
                System.out.print("r");
                break;

            case Door:
            case DoorLocked:
                System.out.print("Z");
                break;

            case Goal:
                System.out.print("g");
                break;

            case Wall:
                System.out.print(" ");
                break;

            case Open:
                System.out.print("o");
                break;

            case Unvisited:
                System.out.print("_");
                break;
        }
    }

    @Override
    public void dispose() {
        this.id = null;
        this.type = null;
        this.link = null;
    }
}