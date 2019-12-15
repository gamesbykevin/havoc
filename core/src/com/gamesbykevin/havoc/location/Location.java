package com.gamesbykevin.havoc.location;

import com.gamesbykevin.havoc.util.Disposable;

public abstract class Location implements Disposable {

    //location
    private float col, row;

    public Location() {
        this(0,0);
    }

    public Location(float col, float row) {
        setCol(col);
        setRow(row);
    }

    public float getCol() {
        return this.col;
    }

    public void setCol(float col) {
        this.col = col;
    }

    public float getRow() {
        return this.row;
    }

    public void setRow(float row) {
        this.row = row;
    }
}