package com.gamesbykevin.havoc.collectibles;

import com.gamesbykevin.havoc.entities.Entity3d;
import com.gamesbykevin.havoc.level.Level;

public final class Collectible extends Entity3d {

    private final Collectibles.Type type;

    //each obstacle has a single animation sprite
    private static final int SPRITES = 1;

    public Collectible(Collectibles.Type type) {

        //call parent
        super(SPRITES);

        //default solid to true
        setSolid(true);

        //store the type of collectible
        this.type = type;
    }

    public Collectibles.Type getType() {
        return this.type;
    }

    @Override
    public void reset() {
        setSolid(true);
        setCol(getStartCol());
        setRow(getStartRow());
        getAnimation().reset();
    }

    @Override
    public void update(Level level) {
        //don't do anything here?
    }
}