package com.gamesbykevin.havoc.player.weapon;

import com.gamesbykevin.havoc.animation.SpriteAnimation;

import static com.gamesbykevin.havoc.player.weapon.WeaponHelper.BULLETS_MAX_IMPACT;
import static com.gamesbykevin.havoc.player.weapon.WeaponHelper.WEAPONS_DIR;

public class Impact extends Weapon {

    private static final String DIR = WEAPONS_DIR + "impact_cannon/";
    private static final String FILE_NAME = "impact_cannon_f";
    private static final String EXTENSION = ".png";

    private static final float DEFAULT_DAMAGE = 8f;

    private static final float DEFAULT_RANGE = 20f;

    public Impact() {

        super(Type.Impact, BULLETS_MAX_IMPACT);

        //assign the attack range
        setRange(DEFAULT_RANGE);

        //set the bullet count
        setBullets(BULLETS_MAX_IMPACT);

        //how much damage can a single bullet do?
        setDamage(DEFAULT_DAMAGE);

        //create our resting animation
        this.resting = new SpriteAnimation(DIR, FILE_NAME, EXTENSION, 0, 1);

        //animation when about to attack
        this.starting = new SpriteAnimation(DIR, FILE_NAME, EXTENSION, 0, 2);

        //attacking animation
        this.attacking = new SpriteAnimation(DIR, FILE_NAME, EXTENSION, 2, 2);

        //stop attacking animation
        this.stopping = new SpriteAnimation(DIR, FILE_NAME, EXTENSION, 8, 6);
    }
}
