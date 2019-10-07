package com.gamesbykevin.havoc.player.weapon;

import com.gamesbykevin.havoc.animation.SpriteAnimation;

import static com.gamesbykevin.havoc.player.weapon.WeaponHelper.WEAPONS_DIR;

public class Buzzsaw extends Weapon {

    private static final String DIR = WEAPONS_DIR + "buzzsaw_cannon/";
    private static final String FILE_NAME = "buzzsaw_cannon_f";
    private static final String EXTENSION = ".png";

    public Buzzsaw() {

        super(Type.Buzz);

        //create our resting animation
        this.resting = new SpriteAnimation(DIR, FILE_NAME, EXTENSION, 0, 1);

        //animation when about to attack
        this.starting = new SpriteAnimation(DIR, FILE_NAME, EXTENSION, 0, 3);

        //attacking animation
        this.attacking = new SpriteAnimation(DIR, FILE_NAME, EXTENSION, 3, 12);

        //stop attacking animation
        this.stopping = new SpriteAnimation(DIR, FILE_NAME, EXTENSION, 15, 5);
    }
}