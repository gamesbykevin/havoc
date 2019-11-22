package com.gamesbykevin.havoc.dungeon;

import com.gamesbykevin.havoc.guid.GUID;

public class Leaf {

    //location of leaf
    private int x, y;

    //size of leaf
    private int w, h;

    //id of leaf
    private final String id;

    //parent this leaf belongs to
    private String parentId;

    //does this leaf have children?
    private boolean children = false;

    //the room inside this leaf
    private Room room;

    //was this leaf merged with another sibling?
    private boolean merged = false;

    public Leaf(int x, int y, int w, int h) {
        this(x, y, w, h, "");
    }

    public Leaf(int x, int y, int w, int h, Leaf parent) {
        this(x, y, w, h, parent.getId());
    }

    public Leaf(int x, int y, int w, int h, String parentId) {

        //auto generate the id
        this.id = GUID.generate();

        setX(x);
        setY(y);
        setW(w);
        setH(h);
        setParentId(parentId);
        setChildren(true);
        setMerged(false);
    }

    public boolean isMerged() {
        return this.merged;
    }

    public void setMerged(boolean merged) {
        this.merged = merged;
    }

    public Room getRoom() {
        return this.room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public boolean hasChildren() {
        return this.children;
    }

    public void setChildren(boolean children) {
        this.children = children;
    }

    public int getX() {
        return this.x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return this.y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getW() {
        return this.w;
    }

    public void setW(int w) {
        this.w = w;
    }

    public int getH() {
        return this.h;
    }

    public void setH(int h) {
        this.h = h;
    }

    private String getId() {
        return this.id;
    }

    public String getParentId() {
        return this.parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public boolean hasId(Leaf leaf) {
        return hasId(leaf.getId());
    }

    public boolean hasId(String id) {
        return getId().equals(id);
    }
}