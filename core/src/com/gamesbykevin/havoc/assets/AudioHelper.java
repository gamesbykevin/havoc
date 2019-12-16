package com.gamesbykevin.havoc.assets;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.gamesbykevin.havoc.weapon.Weapon;

import static com.gamesbykevin.havoc.assets.AssetManagerHelper.*;
import static com.gamesbykevin.havoc.dungeon.Dungeon.getRandom;

public class AudioHelper {

    //how many different hero sound effects are there
    public static final int SFX_HERO_COUNT = 9;

    //how many different enemy alert sound effects
    public static final int SFX_ALERT_COUNT = 7;

    //how many different enemy hurt sound effects
    public static final int SFX_HURT_COUNT = 13;

    //how many different enemy dead sound effects
    public static final int SFX_DEAD_COUNT = 10;

    public enum Sfx {
        WeaponFireEmpty(PATH_SHOOT_EMPTY),
        WeaponFireGlock(PATH_SHOOT_GLOCK),
        WeaponFireShotgun(PATH_SHOOT_SHOTGUN),
        WeaponFireImpact(PATH_SHOOT_IMPACT),
        WeaponFireSmg(PATH_SHOOT_SMG),
        WeaponFireMagnum(PATH_SHOOT_MAGNUM),
        WeaponFireBuzz(PATH_SHOOT_BUZZ),
        WeaponFireLance(PATH_SHOOT_LANCE),
        WeaponChange(PATH_CHANGE),
        ItemAddAmmo(PATH_ADD_AMMO),
        ItemHealthSmall(PATH_ADD_HEALTH_SMALL),
        ItemHealthLarge(PATH_ADD_HEALTH_LARGE),
        ItemKey(PATH_ADD_KEY),
        LevelSecret(PATH_LEVEL_SECRET),
        LevelOpen(PATH_LEVEL_OPEN),
        LevelClose(PATH_LEVEL_CLOSE),
        LevelSwitch(PATH_LEVEL_SWITCH),
        LevelLocked(PATH_LEVEL_LOCK),
        HeroBringPain(PATH_HERO_BRING_PAIN),
        HeroGetSome1(PATH_HERO_GET_SOME_1),
        HeroGetSome2(PATH_HERO_GET_SOME_2),
        HeroGetSome3(PATH_HERO_GET_SOME_3),
        HeroGetSome4(PATH_HERO_GET_SOME_4),
        HeroTakeOut(PATH_HERO_TAKE_OUT),
        HeroTrash1(PATH_HERO_TRASH_1),
        HeroTrash2(PATH_HERO_TRASH_2),
        HeroTakeDown(PATH_HERO_TAKE_DOWN),
        HeroDead(PATH_HERO_DEAD),
        EnemyAlert1(PATH_ENEMY_ALERT_1),
        EnemyAlert2(PATH_ENEMY_ALERT_2),
        EnemyAlert3(PATH_ENEMY_ALERT_3),
        EnemyAlert4(PATH_ENEMY_ALERT_4),
        EnemyAlert5(PATH_ENEMY_ALERT_5),
        EnemyAlert6(PATH_ENEMY_ALERT_6),
        EnemyAlert7(PATH_ENEMY_ALERT_7),
        EnemyHurt1(PATH_ENEMY_HURT_1),
        EnemyHurt2(PATH_ENEMY_HURT_2),
        EnemyHurt3(PATH_ENEMY_HURT_3),
        EnemyHurt4(PATH_ENEMY_HURT_4),
        EnemyHurt5(PATH_ENEMY_HURT_5),
        EnemyHurt6(PATH_ENEMY_HURT_6),
        EnemyHurt7(PATH_ENEMY_HURT_7),
        EnemyHurt8(PATH_ENEMY_HURT_8),
        EnemyHurt9(PATH_ENEMY_HURT_9),
        EnemyHurt10(PATH_ENEMY_HURT_10),
        EnemyHurt11(PATH_ENEMY_HURT_11),
        EnemyHurt12(PATH_ENEMY_HURT_12),
        EnemyHurt13(PATH_ENEMY_HURT_13),
        EnemyDead1(PATH_ENEMY_DEAD_1),
        EnemyDead2(PATH_ENEMY_DEAD_2),
        EnemyDead3(PATH_ENEMY_DEAD_3),
        EnemyDead4(PATH_ENEMY_DEAD_4),
        EnemyDead5(PATH_ENEMY_DEAD_5),
        EnemyDead6(PATH_ENEMY_DEAD_6),
        EnemyDead7(PATH_ENEMY_DEAD_7),
        EnemyDead8(PATH_ENEMY_DEAD_8),
        EnemyDead9(PATH_ENEMY_DEAD_9),
        EnemyDead10(PATH_ENEMY_DEAD_10),
        EnemyWeaponShoot1(PATH_ENEMY_SHOOT_1),
        EnemyWeaponShoot2(PATH_ENEMY_SHOOT_2),
        EnemyWeaponShoot3(PATH_ENEMY_SHOOT_3),
        EnemyWeaponShoot4(PATH_ENEMY_SHOOT_4),
        EnemyWeaponShoot5(PATH_ENEMY_SHOOT_5),
        EnemyWeaponShoot6(PATH_ENEMY_SHOOT_6),
        EnemyWeaponShoot7(PATH_ENEMY_SHOOT_7);

        private final String path;

        Sfx(String path) {
            this.path = path;
        }

        public String getPath() {
            return this.path;
        }
    }

    public static void playSfx(AssetManager assetManager, Sfx sfx) {
        assetManager.get(sfx.getPath(), Sound.class).play();
    }

    public static void playDead(AssetManager assetManager) {

        Sfx sfx = null;

        switch (getRandom().nextInt(SFX_DEAD_COUNT)) {

            case 0:
                sfx = Sfx.EnemyDead1;
                break;

            case 1:
                sfx = Sfx.EnemyDead2;
                break;

            case 2:
                sfx = Sfx.EnemyDead3;
                break;

            case 3:
                sfx = Sfx.EnemyDead4;
                break;

            case 4:
                sfx = Sfx.EnemyDead5;
                break;

            case 5:
                sfx = Sfx.EnemyDead6;
                break;

            case 6:
                sfx = Sfx.EnemyDead7;
                break;

            case 7:
                sfx = Sfx.EnemyDead8;
                break;

            case 8:
                sfx = Sfx.EnemyDead9;
                break;

            case 9:
            default:
                sfx = Sfx.EnemyDead10;
                break;

        }

        if (sfx != null)
            playSfx(assetManager, sfx);
    }

    public static void playHurt(AssetManager assetManager) {

        Sfx sfx = null;

        switch (getRandom().nextInt(SFX_HURT_COUNT)) {
            case 0:
                sfx = Sfx.EnemyHurt1;
                break;

            case 1:
                sfx = Sfx.EnemyHurt2;
                break;

            case 2:
                sfx = Sfx.EnemyHurt3;
                break;

            case 3:
                sfx = Sfx.EnemyHurt4;
                break;

            case 4:
                sfx = Sfx.EnemyHurt5;
                break;

            case 5:
                sfx = Sfx.EnemyHurt6;
                break;

            case 6:
                sfx = Sfx.EnemyHurt7;
                break;

            case 7:
                sfx = Sfx.EnemyHurt8;
                break;

            case 8:
                sfx = Sfx.EnemyHurt9;
                break;

            case 9:
                sfx = Sfx.EnemyHurt10;
                break;

            case 10:
                sfx = Sfx.EnemyHurt11;
                break;

            case 11:
                sfx = Sfx.EnemyHurt12;
                break;

            case 12:
                sfx = Sfx.EnemyHurt13;
                break;
        }

        if (sfx != null)
            playSfx(assetManager, sfx);
    }

    public static void playHero(AssetManager assetManager) {

        Sfx sfx = null;

        switch (getRandom().nextInt(SFX_HERO_COUNT)) {

            case 0:
                sfx = Sfx.HeroBringPain;
                break;

            case 1:
                sfx = Sfx.HeroGetSome1;
                break;

            case 2:
                sfx = Sfx.HeroGetSome2;
                break;

            case 3:
                sfx = Sfx.HeroGetSome3;
                break;

            case 4:
                sfx = Sfx.HeroGetSome4;
                break;

            case 5:
                sfx = Sfx.HeroTakeDown;
                break;

            case 6:
                sfx = Sfx.HeroTakeOut;
                break;

            case 7:
                sfx = Sfx.HeroTrash1;
                break;

            case 8:
            default:
                sfx = Sfx.HeroTrash2;
                break;
        }

        if (sfx != null)
            playSfx(assetManager, sfx);
    }

    public static void playAlert(AssetManager assetManager) {

        Sfx sfx = null;

        switch (getRandom().nextInt(SFX_ALERT_COUNT)) {

            case 0:
                sfx = Sfx.EnemyAlert1;
                break;

            case 1:
                sfx = Sfx.EnemyAlert2;
                break;

            case 2:
                sfx = Sfx.EnemyAlert3;
                break;

            case 3:
                sfx = Sfx.EnemyAlert4;
                break;

            case 4:
                sfx = Sfx.EnemyAlert5;
                break;

            case 5:
                sfx = Sfx.EnemyAlert6;
                break;

            case 6:
                sfx = Sfx.EnemyAlert7;
                break;
        }

        if (sfx != null)
            playSfx(assetManager, sfx);
    }

    public static void playWeapon(AssetManager assetManager, Weapon weapon) {

        Sfx sfx = null;

        if (weapon.getBullets() == 0) {

            sfx = Sfx.WeaponFireEmpty;

        } else {

            switch (weapon.getType()) {

                case Glock:
                    sfx = Sfx.WeaponFireGlock;
                    break;

                case Shotgun:
                    sfx = Sfx.WeaponFireShotgun;
                    break;

                case Smg:
                    sfx = Sfx.WeaponFireSmg;
                    break;

                case Magnum:
                    sfx = Sfx.WeaponFireMagnum;
                    break;

                case Impact:
                    sfx = Sfx.WeaponFireImpact;
                    break;

                case Buzz:
                    sfx = Sfx.WeaponFireBuzz;
                    break;

                case Lance:
                    sfx = Sfx.WeaponFireLance;
                    break;
            }
        }

        //play sound
        if (sfx != null)
            playSfx(assetManager, sfx);
    }
}