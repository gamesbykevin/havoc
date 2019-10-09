package com.gamesbykevin.havoc.player.weapon;

import com.gamesbykevin.havoc.animation.SpriteAnimation;

import static com.gamesbykevin.havoc.player.weapon.WeaponHelper.BULLETS_MAX_LANCE;
import static com.gamesbykevin.havoc.player.weapon.WeaponHelper.WEAPONS_DIR;

public class Lance extends Weapon {

    private static final String DIR = WEAPONS_DIR + "thermic_lance/";
    private static final String FILE_NAME = "thermic_lance_f";
    private static final String EXTENSION = ".png";

    private static final float DEFAULT_DAMAGE = 3f;

    private static final float DEFAULT_RANGE = 1f;

    public Lance() {

        super(Type.Lance);

        //assign the attack range
        setRange(DEFAULT_RANGE);

        //set the bullet count
        setBullets(BULLETS_MAX_LANCE);

        //how much damage can a single bullet do?
        setDamage(DEFAULT_DAMAGE);

        //create our resting animation
        this.resting = new SpriteAnimation(DIR, FILE_NAME, EXTENSION, 0, 1);

        //animation when about to attack
        this.starting = new SpriteAnimation(DIR, FILE_NAME, EXTENSION, 1, 3);

        //attacking animation
        this.attacking = new SpriteAnimation(DIR, FILE_NAME, EXTENSION, 4, 2);

        //stop attacking animation
        this.stopping = new SpriteAnimation(DIR, FILE_NAME, EXTENSION, 16, 4);
    }
}