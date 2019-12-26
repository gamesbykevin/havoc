package com.gamesbykevin.havoc.obstacles;

import com.badlogic.gdx.assets.AssetManager;
import com.gamesbykevin.havoc.animation.DecalAnimation;
import com.gamesbykevin.havoc.entities.Entity3d;
import com.gamesbykevin.havoc.level.Level;

import static com.gamesbykevin.havoc.animation.DecalAnimation.BILLBOARD_ENABLED;
import static com.gamesbykevin.havoc.assets.AssetManagerHelper.ASSET_SHEET_OBSTACLES;

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

    //size of obstacle sprite sheet
    public static final int SPRITE_SHEET_WIDTH = 64;
    public static final int SPRITE_SHEET_HEIGHT = 64;

    protected Obstacle(AssetManager assetManager, Type type) {

        super(SPRITES);

        //only lights are NOT solid
        setSolid(!isLight(type));

        int col = 0;
        int row = 0;

        //map the animation
        switch (type) {

            case Grass1:
                col = 4;
                row = 1;
                break;

            case Grass2:
                col = 5;
                row = 1;
                break;

            case Grass3:
                col = 6;
                row = 1;
                break;

            case Grass4:
                col = 7;
                row = 1;
                break;

            case Grass5:
                col = 0;
                row = 2;
                break;

            case Grass6:
                col = 1;
                row = 2;
                break;

            case Grass7:
                col = 2;
                row = 2;
                break;

            case Grass8:
                col = 3;
                row = 2;
                break;

            case Grass9:
                col = 4;
                row = 2;
                break;

            case Grass10:
                col = 5;
                row = 2;
                break;

            case Grass11:
                col = 6;
                row = 2;
                break;

            case cage1:
                col = 3;
                row = 0;
                break;

            case cage2:
                col = 4;
                row = 0;
                break;

            case cage3:
                col = 5;
                row = 0;
                break;

            case cage4:
                col = 6;
                row = 0;
                break;

            case flag1:
                col = 0;
                row = 1;
                break;

            case flag2:
                col = 1;
                row = 1;
                break;

            case pots1:
                col = 3;
                row = 4;
                break;

            case pots2:
                col = 4;
                row = 4;
                break;

            case table:
                col = 0;
                row = 6;
                break;

            case Candle:
                col = 7;
                row = 0;
                break;

            case Light1:
                col = 7;
                row = 2;
                break;

            case Light2:
                col = 0;
                row = 3;
                break;

            case Light3:
                col = 1;
                row = 3;
                break;

            case Light4:
                col = 2;
                row = 3;
                break;

            case Light5:
                col = 3;
                row = 3;
                break;

            case Light6:
                col = 4;
                row = 3;
                break;

            case Light7:
                col = 5;
                row = 3;
                break;

            case spear1:
                col = 1;
                row = 5;
                break;

            case spear2:
                col = 2;
                row = 5;
                break;

            case pillar1:
                col = 6;
                row = 3;
                break;

            case pillar2:
                col = 7;
                row = 3;
                break;

            case pillar3:
                col = 0;
                row = 4;
                break;

            case pillar4:
                col = 1;
                row = 4;
                break;

            case pillar5:
                col = 2;
                row = 4;
                break;

            case random1:
                col = 5;
                row = 4;
                break;

            case random2:
                col = 6;
                row = 4;
                break;

            case random3:
                col = 7;
                row = 4;
                break;

            case random4:
                col = 0;
                row = 5;
                break;

            case statue1:
                col = 3;
                row = 5;
                break;

            case statue2:
                col = 4;
                row = 5;
                break;

            case statue3:
                col = 5;
                row = 5;
                break;

            case statue4:
                col = 6;
                row = 5;
                break;

            case statue5:
                col = 7;
                row = 5;
                break;

            case FloorLamp1:
                col = 2;
                row = 1;
                break;

            case FloorLamp2:
                col = 3;
                row = 1;
                break;

            case BluePotPlant1:
                col = 1;
                row = 0;
                break;

            case BluePotPlant2:
                col = 2;
                row = 0;
                break;

            case YellowPotPlant:
                col = 1;
                row = 6;
                break;

            case BluePotLargeEmpty:
                col = 0;
                row = 0;
                break;
        }

        //obstacle animations are a single frame
        setAnimation(0, new DecalAnimation(assetManager, ASSET_SHEET_OBSTACLES, SPRITE_SHEET_WIDTH, SPRITE_SHEET_HEIGHT, col, row, BILLBOARD_ENABLED));
    }

    public boolean isLight(Type type) {

        switch (type) {
            case Light1:
            case Light2:
            case Light3:
            case Light4:
            case Light5:
            case Light6:
            case Light7:
                return true;

            default:
                return false;
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