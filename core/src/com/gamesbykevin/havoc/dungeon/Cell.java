package com.gamesbykevin.havoc.dungeon;

import com.gamesbykevin.havoc.guid.GUID;
import com.gamesbykevin.havoc.location.Location;
import com.gamesbykevin.havoc.util.Disposable;

public class Cell extends Location implements Disposable {

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
        Hallway,
        Door,
        DoorLocked,
        Secret,
        Goal,
        Unvisited
    }

    public Cell(int col, int row, Type type) {
        super(col, row);
        this.id = GUID.generate();
        this.type = type;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public String getId() {
        return this.id;
    }

    public boolean hasId(Cell cell) {
        return hasId(cell.getId());
    }

    private boolean hasId(String id) {
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

    public boolean isHallway(){
        switch (getType()) {
            case Hallway:
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