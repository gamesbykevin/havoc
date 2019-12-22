package com.gamesbykevin.havoc.obstacles;

import com.gamesbykevin.havoc.entities.Entity3d;
import com.gamesbykevin.havoc.level.Level;

public final class Obstacle extends Entity3d {

    public enum Type {
        Light1, Light2, Light3, Light4, Light5, Light6, Light7,
        Grass1, Grass2, Grass3, Grass4, Grass5, Grass6, Grass7, Grass8, Grass9, Grass10, Grass11,
        pillar1, pillar2, pillar3, pillar4, pillar5,
        random1, random2, random3, random4,
        cage1, cage2, cage3, cage4,
        statue1, statue2, statue3, statue4, statue5,
        BluePotLargeEmpty, BluePotPlant1, BluePotPlant2, YellowPotPlant,
        Candle,
        spear1, spear2,
        flag1, flag2,
        pots1, pots2,
        table,
        FloorLamp1, FloorLamp2
    }

    //each obstacle has a single animation sprite
    private static final int SPRITES = 1;

    protected Obstacle(Type type) {

        super(SPRITES);

        switch (type) {
            case Light1:
            case Light2:
            case Light3:
            case Light4:
            case Light5:
            case Light6:
            case Light7:
                setSolid(false);
                break;

            default:
                setSolid(true);
                break;
        }
    }

    @Override
    public void reset() {
        //do we need to do anything here
    }

    @Override
    public void update(Level level) {
        //do we need to do anything here
    }
}