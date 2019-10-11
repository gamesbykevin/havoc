package com.gamesbykevin.havoc.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.gamesbykevin.havoc.input.MyController;
import com.gamesbykevin.havoc.player.weapon.*;

import java.util.ArrayList;
import java.util.List;

import static com.gamesbykevin.havoc.player.PlayerHelper.*;
import static com.gamesbykevin.havoc.player.weapon.WeaponHelper.*;

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

        //start rendering
        getController().getStage().getBatch().begin();

        //render weapon animation
        getWeapons().get(getWeaponIndex()).render(getController().getStage().getBatch());

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