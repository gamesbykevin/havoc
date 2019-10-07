package com.gamesbykevin.havoc.player.weapon;

import com.gamesbykevin.havoc.animation.SpriteAnimation;

import static com.gamesbykevin.havoc.player.weapon.WeaponHelper.WEAPONS_DIR;

public class Impact extends Weapon {

    private static final String DIR = WEAPONS_DIR + "impact_cannon/";
    private static final String FILE_NAME = "impact_cannon_f";
    private static final String EXTENSION = ".png";

    public Impact() {

        super(Type.Impact);

        //create our resting animation
        this.resting = new SpriteAnimation(DIR, FILE_NAME, EXTENSION, 0, 1);

        //animation when about to attack
        this.starting = new SpriteAnimation(DIR, FILE_NAME, EXTENSION, 0, 2);

        //attacking animation
        this.attacking = new SpriteAnimation(DIR, FILE_NAME, EXTENSION, 2, 6);

        //stop attacking animation
        this.stopping = new SpriteAnimation(DIR, FILE_NAME, EXTENSION, 8, 6);
    }
}
