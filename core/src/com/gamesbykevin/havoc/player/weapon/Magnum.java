package com.gamesbykevin.havoc.player.weapon;

import com.gamesbykevin.havoc.animation.SpriteAnimation;

import static com.gamesbykevin.havoc.player.weapon.WeaponHelper.BULLETS_MAX_MAGNUM;
import static com.gamesbykevin.havoc.player.weapon.WeaponHelper.WEAPONS_DIR;

public class Magnum extends Weapon {

    private static final String DIR = WEAPONS_DIR + "magnum/";
    private static final String FILE_NAME = "magnum_f";
    private static final String EXTENSION = ".png";

    public Magnum() {

        super(Type.Magnum);

        //set the bullet count
        setBullets(BULLETS_MAX_MAGNUM);

        //create our resting animation
        this.resting = new SpriteAnimation(DIR, FILE_NAME, EXTENSION, 0, 1);

        //animation when about to attack
        this.starting = new SpriteAnimation(DIR, FILE_NAME, EXTENSION, 0, 4);

        //attacking animation
        this.attacking = new SpriteAnimation(DIR, FILE_NAME, EXTENSION, 4, 11);

        //stop attacking animation
        this.stopping = new SpriteAnimation(DIR, FILE_NAME, EXTENSION, 14, 1);
    }
}
