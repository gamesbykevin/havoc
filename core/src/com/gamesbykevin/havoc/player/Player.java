package com.gamesbykevin.havoc.player;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.gamesbykevin.havoc.input.MyController;
import com.gamesbykevin.havoc.player.weapon.*;

import java.util.ArrayList;
import java.util.List;

import static com.gamesbykevin.havoc.player.PlayerHelper.*;
import static com.gamesbykevin.havoc.player.weapon.WeaponHelper.reset;
import static com.gamesbykevin.havoc.player.weapon.WeaponHelper.updateWeapon;

public final class Player {

    //our weapons
    private List<Weapon> weapons;

    //used to render images fast
    private SpriteBatch spriteBatch;

    //current selected weapon
    private int weaponIndex = 0;

    //controller reference
    private final MyController controller;

    //how close can the player get to an object
    public static final double PLAYER_COLLISION = 0.75d;

    public Player(MyController controller) {

        //store reference to controller
        this.controller = controller;

        //add weapon to our list
        getWeapons().add(new Lance());
        getWeapons().add(new Glock());
        getWeapons().add(new Smg());
        getWeapons().add(new Impact());
        getWeapons().add(new Magnum());
        getWeapons().add(new Shotgun());
        getWeapons().add(new Buzzsaw());

        for (int i = 0; i < getWeapons().size(); i++) {
            reset(getWeapons().get(i));
        }

        //start with lance
        setWeaponIndex(5);
    }

    public MyController getController() {
        return this.controller;
    }

    public Weapon getWeapon() {
        return getWeapons().get(getWeaponIndex());
    }

    public void update() {

        //update the weapon
        updateWeapon(this);

        //update the players location
        updateLocation(getController());

        //check for collision
        checkCollision(getController());

        //update the level
        updateLevel(this);
    }

    public void setWeaponIndex(int weaponIndex) {
        this.weaponIndex = weaponIndex;

        if (getWeaponIndex() >= getWeapons().size())
            this.weaponIndex = 0;
    }

    public int getWeaponIndex() {
        return this.weaponIndex;
    }

    protected List<Weapon> getWeapons() {

        if (this.weapons == null)
            this.weapons = new ArrayList<>();

        return this.weapons;
    }

    public SpriteBatch getSpriteBatch() {

        if (this.spriteBatch == null)
            this.spriteBatch = new SpriteBatch();

        return this.spriteBatch;
    }

    public void render() {
        getSpriteBatch().setProjectionMatrix(getController().getCamera2d().combined);
        getController().getStage().getBatch().begin();
        getWeapons().get(getWeaponIndex()).render(getController().getStage().getBatch());
        getController().getStage().getBatch().end();
    }
}