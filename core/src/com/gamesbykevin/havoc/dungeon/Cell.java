package com.gamesbykevin.havoc.dungeon;

import com.gamesbykevin.havoc.guid.GUID;

public class Cell {

    //location of cell
    private int col, row;

    //cells part of a room will have the same id
    private String id;

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

    public Type getType() {
        return this.type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public boolean hasNeighbor(Dungeon dungeon, Type type) {
        if (dungeon.getCells()[getRow() - 1][getCol()].getType() == type)
            return true;
        if (dungeon.getCells()[getRow() + 1][getCol()].getType() == type)
            return true;
        if (dungeon.getCells()[getRow()][getCol() - 1].getType() == type)
            return true;
        if (dungeon.getCells()[getRow()][getCol() + 1].getType() == type)
            return true;

        return false;
    }
}