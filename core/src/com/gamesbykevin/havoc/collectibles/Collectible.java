package com.gamesbykevin.havoc.collectibles;

import com.badlogic.gdx.assets.AssetManager;
import com.gamesbykevin.havoc.animation.DecalAnimation;
import com.gamesbykevin.havoc.assets.AudioHelper.Sfx;
import com.gamesbykevin.havoc.entities.Entity3d;
import com.gamesbykevin.havoc.level.Level;

import static com.gamesbykevin.havoc.animation.DecalAnimation.BILLBOARD_ENABLED;
import static com.gamesbykevin.havoc.assets.AssetManagerHelper.ASSET_SHEET_COLLECTIBLES;

public final class Collectible extends Entity3d {

    //each obstacle has a single animation sprite
    private static final int SPRITES = 1;

    //how big is the texture
    public static final int DIMENSION = 128;

    //what to play when the player collects this
    private Sfx soundEffect;

    //need to store the type
    private final Collectibles.Type type;

    public Collectible(AssetManager assetManager, Collectibles.Type type) {

        //call parent
        super(SPRITES);

        //remember the type of collectible
        this.type = type;

        //default solid to true
        setSolid(true);

        //location of the texture
        int col = 0, row = 0;

        //choose the appropriate sound effect
        switch (getType()) {

            case shotgun:
                col = 1;
                row = 2;
                setSoundEffect(Sfx.ItemAddAmmo);
                break;

            case magnum:
                col = 0;
                row = 2;
                setSoundEffect(Sfx.ItemAddAmmo);
                break;

            case impact:
                col = 2;
                row = 1;
                setSoundEffect(Sfx.ItemAddAmmo);
                break;

            case smg:
                col = 2;
                row = 2;
                setSoundEffect(Sfx.ItemAddAmmo);
                break;

            case buzzsaw:
                col = 2;
                row = 0;
                setSoundEffect(Sfx.ItemAddAmmo);
                break;

            case ammo:
                col = 0;
                row = 0;
                setSoundEffect(Sfx.ItemAddAmmo);
                break;

            case ammo_crate:
                col = 1;
                row = 0;
                setSoundEffect(Sfx.ItemAddAmmo);
                break;

            case key:
                col = 3;
                row = 1;
                setSoundEffect(Sfx.ItemKey);
                break;

            case health_large:
                col = 0;
                row = 1;
                setSoundEffect(Sfx.ItemHealthLarge);
                break;

            case health_small:
                col = 1;
                row = 1;
                setSoundEffect(Sfx.ItemHealthSmall);
                break;
        }

        //each collectible has 1 frame animation
        setAnimation(0, new DecalAnimation(assetManager, ASSET_SHEET_COLLECTIBLES, DIMENSION, DIMENSION, col, row, BILLBOARD_ENABLED));

        //make sure we reset
        getAnimation().reset();
    }

    public Collectibles.Type getType() {
        return this.type;
    }

    public void setSoundEffect(Sfx soundEffect) {
        this.soundEffect = soundEffect;
    }

    public Sfx getSoundEffect() {
        return this.soundEffect;
    }

    @Override
    public void reset() {
        setSolid(true);
        setCol(getStartCol());
        setRow(getStartRow());
        getAnimation().reset();
        getAnimation().setPosition(getStartCol(), getStartRow(), 0);
    }

    @Override
    public void update(Level level) {
        //don't do anything here?
    }
}