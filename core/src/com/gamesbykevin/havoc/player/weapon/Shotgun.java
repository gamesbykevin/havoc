package com.gamesbykevin.havoc.player.weapon;

import com.gamesbykevin.havoc.animation.SpriteAnimation;

import static com.gamesbykevin.havoc.player.weapon.WeaponHelper.BULLETS_MAX_SHOTGUN;
import static com.gamesbykevin.havoc.player.weapon.WeaponHelper.WEAPONS_DIR;

public class Shotgun extends Weapon {

    private static final String DIR = WEAPONS_DIR + "shotgun/";
    private static final String FILE_NAME = "shotgun_f";
    private static final String EXTENSION = ".png";

    private static final float DEFAULT_DAMAGE = 51f;

    public Shotgun() {

        super(Type.Shotgun);

        //set the bullet count
        setBullets(BULLETS_MAX_SHOTGUN);

        //how much damage can a single bullet do?
        setDamage(DEFAULT_DAMAGE);

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
