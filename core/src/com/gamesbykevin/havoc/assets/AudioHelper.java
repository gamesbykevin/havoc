package com.gamesbykevin.havoc.assets;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.gamesbykevin.havoc.weapon.Weapon;

import static com.gamesbykevin.havoc.assets.AssetManagerHelper.*;
import static com.gamesbykevin.havoc.dungeon.Dungeon.getRandom;

public class AudioHelper {

    //how many different hero sound effects are there
    public static final int SFX_HERO_COUNT = 9;

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
        EnemyDead1(PATH_ENEMY_DEAD_1),
        EnemyDead2(PATH_ENEMY_DEAD_2),
        EnemyDead3(PATH_ENEMY_DEAD_3),
        EnemyDead4(PATH_ENEMY_DEAD_4),
        EnemyDead5(PATH_ENEMY_DEAD_5),
        EnemyDead6(PATH_ENEMY_DEAD_6),
        EnemyDead7(PATH_ENEMY_DEAD_7),
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
}