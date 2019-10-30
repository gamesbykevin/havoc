package com.gamesbykevin.havoc.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.gamesbykevin.havoc.input.MyController;
import com.gamesbykevin.havoc.player.weapon.*;

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

    //do we have the key
    private boolean key = false;

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
        getWeapons()[INDEX_LANCE] = new Lance();
        getWeapons()[INDEX_GLOCK] = new Glock();

        for (int i = 0; i < getWeapons().length; i++) {
            reset(getWeapons()[i]);
        }

        //start with glock
        setWeaponIndex(1);

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

        int index = -1;

        switch (type) {
            case Smg:
                index = INDEX_SMG;
                break;
            case Shotgun:
                index = INDEX_SHOTGUN;
                break;
            case Magnum:
                index = INDEX_MAGNUM;
                break;
            case Impact:
                index = INDEX_IMPACT;
                break;
            case Glock:
                index = INDEX_GLOCK;
                break;
            case Buzz:
                index = INDEX_BUZZ;
                break;
        }

        //if we don't have the weapon we will create and add to the list
        if (getWeapons()[index] == null) {
            switch (type) {
                case Buzz:
                    getWeapons()[index] = new Buzzsaw();
                    break;
                case Glock:
                    getWeapons()[index] = new Glock();
                    break;
                case Impact:
                    getWeapons()[index] = new Impact();
                    break;
                case Magnum:
                    getWeapons()[index] = new Magnum();
                    break;
                case Shotgun:
                    getWeapons()[index] = new Shotgun();
                    break;
                case Smg:
                    getWeapons()[index] = new Smg();
                    break;
            }

            setWeaponIndex(index);
            reset(getWeapon());
            getController().setChange(true);

        } else {

            //if weapon exists we will add the bullets
            getWeapons()[index].setBullets(getWeapons()[index].getBullets() + AMMO_LARGE);
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

    public boolean hasKey() {
        return this.key;
    }

    public void setKey(boolean key) {
        this.key = key;
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

        //render key?
        if (hasKey())
            getController().getStage().getBatch().draw(this.numbers[10], HUD_HEALTH_X, HUD_HEALTH_Y - (HUD_KEY_HEIGHT * 1), HUD_KEY_WIDTH, HUD_KEY_HEIGHT);

        //done rendering
        getController().getStage().getBatch().end();
    }

    private final void renderNumber(final int number, int renderX, int renderY, int width, int height, int padding) {

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