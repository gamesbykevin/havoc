package com.gamesbykevin.havoc.player.weapon;

import com.gamesbykevin.havoc.animation.SpriteAnimation;

import static com.gamesbykevin.havoc.player.weapon.WeaponHelper.BULLETS_MAX_GLOCK;
import static com.gamesbykevin.havoc.player.weapon.WeaponHelper.WEAPONS_DIR;

public class Glock extends Weapon {

    private static final String DIR = WEAPONS_DIR + "glock_handgun/";
    private static final String FILE_NAME = "glock_handgun_f";
    private static final String EXTENSION = ".png";

    private static final float DEFAULT_DAMAGE = 10f;

    private static final float DEFAULT_RANGE = 30f;

    public Glock() {

        super(Type.Glock);

        //assign the attack range
        setRange(DEFAULT_RANGE);

        //set the bullet count
        setBullets(BULLETS_MAX_GLOCK);

        //how much damage can a single bullet do?
        setDamage(DEFAULT_DAMAGE);

        //create our resting animation
        this.resting = new SpriteAnimation(DIR, FILE_NAME, EXTENSION, 0, 1);

        //animation when about to attack
        this.starting = new SpriteAnimation(DIR, FILE_NAME, EXTENSION, 0, 3);

        //attacking animation
        this.attacking = new SpriteAnimation(DIR, FILE_NAME, EXTENSION, 2, 8);

        //stop attacking animation
        this.stopping = new SpriteAnimation(DIR, FILE_NAME, EXTENSION, 9, 1);
    }
}