package com.gamesbykevin.havoc.player.weapon;

import com.gamesbykevin.havoc.animation.SpriteAnimation;

import static com.gamesbykevin.havoc.player.weapon.WeaponHelper.WEAPONS_DIR;

public class Shotgun extends Weapon {

    private static final String DIR = WEAPONS_DIR + "shotgun/";
    private static final String FILE_NAME = "shotgun_f";
    private static final String EXTENSION = ".png";

    public Shotgun() {

        super(Type.Shotgun);

        //create our resting animation
        this.resting = new SpriteAnimation(DIR, FILE_NAME, EXTENSION, 0, 1);

        //animation when about to attack
        this.starting = new SpriteAnimation(DIR, FILE_NAME, EXTENSION, 0, 1);

        //attacking animation
        this.attacking = new SpriteAnimation(DIR, FILE_NAME, EXTENSION, 1, 16);

        //stop attacking animation
        this.stopping = new SpriteAnimation(DIR, FILE_NAME, EXTENSION, 17, 2);
    }
}
