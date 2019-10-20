package com.gamesbykevin.havoc.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.gamesbykevin.havoc.input.MyController;
import com.gamesbykevin.havoc.player.weapon.*;

import java.util.HashMap;

import static com.gamesbykevin.havoc.player.PlayerHelper.*;
import static com.gamesbykevin.havoc.player.weapon.WeaponHelper.*;

public final class Player {

    //our weapons
    private Weapon[] weapons;

    //used to render images fast
    private SpriteBatch spriteBatch;

    //current selected weapon
    private int weaponIndex = 0;

    //controller reference
    private final MyController controller;

    //array of numbers
    private Texture[] numbers;

    private static final int HEALTH_MAX = 100;
    private static final int HEALTH_MIN = 0;

    //what is our health
    private int health = 0;

    public Player(MyController controller) {

        //store reference to controller
        this.controller = controller;

        //create our array of number images
        this.numbers = new Texture[13];

        //load our textures
        for (int i = 0; i < this.numbers.length; i++) {

            if (i < 10) {
                this.numbers[i] = new Texture(Gdx.files.internal("hud/" + i + ".png"));
            } else if (i == 10) {
                this.numbers[i] = new Texture(Gdx.files.internal("hud/key_1_small.png"));
            } else if (i == 11) {
                this.numbers[i] = new Texture(Gdx.files.internal("hud/key_2_small.png"));
            } else if (i == 12) {
                this.numbers[i] = new Texture(Gdx.files.internal("hud/percent.png"));
            }
        }

        //add weapon to our list
        getWeapons()[0] = new Lance();

        /*
        getWeapons().add(new Glock());
        getWeapons().add(new Smg());
        getWeapons().add(new Impact());
        getWeapons().add(new Magnum());
        getWeapons().add(new Shotgun());
        getWeapons().add(new Buzzsaw());
        */

        for (int i = 0; i < getWeapons().length; i++) {
            reset(getWeapons()[i]);
        }

        //start with lance
        setWeaponIndex(0);

        //start out with the max health
        setHealth(HEALTH_MAX);
    }

    public int getHealth() {
        return this.health;
    }

    public void setHealth(int health) {
        this.health = health;

        if (this.health < HEALTH_MIN)
            this.health = HEALTH_MIN;
        if (this.health > HEALTH_MAX)
            this.health = HEALTH_MAX;
    }

    public MyController getController() {
        return this.controller;
    }

    public Weapon getWeapon() {
        return getWeapons()[getWeaponIndex()];
    }

    public void addWeapon(Weapon.Type type) {

        switch (type) {
            case Smg:
                if (getWeapons()[INDEX_SMG] == null) {
                    getWeapons()[INDEX_SMG] = new Smg();
                    setWeaponIndex(INDEX_SMG);
                    getController().setChange(true);
                } else {
                    getWeapons()[INDEX_SMG].setBullets(getWeapons()[INDEX_SMG].getBullets() + AMMO_LARGE);
                }
                break;

            case Shotgun:
                if (getWeapons()[INDEX_SHOTGUN] == null) {
                    getWeapons()[INDEX_SHOTGUN] = new Shotgun();
                    setWeaponIndex(INDEX_SHOTGUN);
                    getController().setChange(true);
                } else {
                    getWeapons()[INDEX_SHOTGUN].setBullets(getWeapons()[INDEX_SHOTGUN].getBullets() + AMMO_LARGE);
                }
                break;

            case Magnum:
                if (getWeapons()[INDEX_MAGNUM] == null) {
                    getWeapons()[INDEX_MAGNUM] = new Magnum();
                    setWeaponIndex(INDEX_MAGNUM);
                    getController().setChange(true);
                } else {
                    getWeapons()[INDEX_MAGNUM].setBullets(getWeapons()[INDEX_MAGNUM].getBullets() + AMMO_LARGE);
                }
                break;

            case Impact:
                if (getWeapons()[INDEX_IMPACT] == null) {
                    getWeapons()[INDEX_IMPACT] = new Impact();
                    setWeaponIndex(INDEX_IMPACT);
                    getController().setChange(true);
                } else {
                    getWeapons()[INDEX_IMPACT].setBullets(getWeapons()[INDEX_IMPACT].getBullets() + AMMO_LARGE);
                }
                break;

            case Glock:
                if (getWeapons()[INDEX_GLOCK] == null) {
                    getWeapons()[INDEX_GLOCK] = new Glock();
                    setWeaponIndex(INDEX_GLOCK);
                    getController().setChange(true);
                } else {
                    getWeapons()[INDEX_GLOCK].setBullets(getWeapons()[INDEX_GLOCK].getBullets() + AMMO_LARGE);
                }
                break;

            case Buzz:
                if (getWeapons()[INDEX_BUZZ] == null) {
                    getWeapons()[INDEX_BUZZ] = new Buzzsaw();
                    setWeaponIndex(INDEX_BUZZ);
                    getController().setChange(true);
                } else {
                    getWeapons()[INDEX_BUZZ].setBullets(getWeapons()[INDEX_BUZZ].getBullets() + AMMO_LARGE);
                }
                break;
        }
    }

    public void update() {

        //update the weapon
        updateWeapon(this);

        //update the players location
        updateLocation(getController());

        //check for collision
        checkCollision(getController());

        //check if we picked up a collectible
        checkCollectible(getController(), this);

        //update the level
        updateLevel(this);
    }

    public void setWeaponIndex(int weaponIndex) {
        this.weaponIndex = weaponIndex;

        if (getWeaponIndex() >= getWeapons().length || getWeaponIndex() < 0)
            this.weaponIndex = 0;

        //make sure we pick an index with an existing weapon
        while (getWeapons()[this.weaponIndex] == null) {
            this.weaponIndex++;

            if (this.weaponIndex >= getWeapons().length)
                this.weaponIndex = 0;
        }
    }

    public int getWeaponIndex() {
        return this.weaponIndex;
    }

    protected Weapon[] getWeapons() {

        if (this.weapons == null)
            this.weapons = new Weapon[Weapon.Type.values().length];

        return this.weapons;
    }

    public SpriteBatch getSpriteBatch() {

        if (this.spriteBatch == null)
            this.spriteBatch = new SpriteBatch();

        return this.spriteBatch;
    }

    public void render() {
        getSpriteBatch().setProjectionMatrix(getController().getCamera2d().combined);

        //start rendering
        getController().getStage().getBatch().begin();

        //render weapon animation
        getWeapons()[getWeaponIndex()].render(getController().getStage().getBatch());

        //render ammo
        renderNumber(getWeapon().getBullets(), HUD_BULLET_X, HUD_BULLET_Y, HUD_NUMBER_WIDTH, HUD_NUMBER_HEIGHT, HUD_NUMBER_PAD);

        //render health
        renderNumber(getHealth(), HUD_HEALTH_X, HUD_HEALTH_Y, HUD_NUMBER_WIDTH, HUD_NUMBER_HEIGHT, HUD_NUMBER_PAD);

        //render keys?

        //done rendering
        getController().getStage().getBatch().end();
    }

    public void renderNumber(final int number, int renderX, int renderY, int width, int height, int padding) {

        float x = renderX;

        for (int i = 0; i < 4; i++) {

            int index = 12;

            switch (i) {
                case 0:
                    index = (number / 100);
                    break;

                case 1:
                    index = (number % 100) / 10;
                    break;

                case 2:
                    index = (number % 10);
                    break;
            }

            //if less than 0 we display as 999
            if (number < 0)
                index = 9;

            //render the number
            getController().getStage().getBatch().draw(this.numbers[index], x, renderY, width, height);

            //move to the next coordinate
            x += width + padding;
        }
    }
}