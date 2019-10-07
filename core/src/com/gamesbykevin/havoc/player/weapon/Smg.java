package com.gamesbykevin.havoc.player.weapon;

import com.gamesbykevin.havoc.animation.SpriteAnimation;

import static com.gamesbykevin.havoc.player.weapon.WeaponHelper.WEAPONS_DIR;

public class Smg extends Weapon {

    private static final String DIR = WEAPONS_DIR + "smg/";
    private static final String FILE_NAME = "smg_f";
    private static final String EXTENSION = ".png";

    public Smg() {

        super(Type.Smg);

        //create our resting animation
        this.resting = new SpriteAnimation(DIR, FILE_NAME, EXTENSION, 0, 1);

        //animation when about to attack
        this.starting = new SpriteAnimation(DIR, FILE_NAME, EXTENSION, 0, 1);

        //attacking animation
        this.attacking = new SpriteAnimation(DIR, FILE_NAME, EXTENSION, 1, 9);

        //stop attacking animation
        this.stopping = new SpriteAnimation(DIR, FILE_NAME, EXTENSION, 9, 2);
    }
}