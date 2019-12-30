package com.gamesbykevin.havoc.collectibles;

import com.gamesbykevin.havoc.assets.AudioHelper;
import com.gamesbykevin.havoc.entities.Entities;
import com.gamesbykevin.havoc.entities.Entity;
import com.gamesbykevin.havoc.level.Level;
import com.gamesbykevin.havoc.player.Player;
import com.gamesbykevin.havoc.weapon.WeaponHelper;
import com.gamesbykevin.havoc.weapon.Weapons;

import static com.gamesbykevin.havoc.assets.AudioHelper.playSfx;
import static com.gamesbykevin.havoc.player.Player.HEALTH_MAX;
import static com.gamesbykevin.havoc.player.PlayerHelper.*;
import static com.gamesbykevin.havoc.util.Language.*;

public class CollectibleHelper {

    //check if we collected any collectibles
    public static void check(Level level) {

        //get our objects
        Entities entities = level.getCollectibles();
        Player player = level.getPlayer();
        Weapons weapons = player.getWeapons();

        //check the whole list
        for (int i = 0; i < entities.getEntityList().size(); i++) {

            //get the current entity
            Entity entity = entities.getEntityList().get(i);

            //update the entity
            entity.update(level);

            //skip if not solid
            if (!entity.isSolid())
                continue;

            //skip if we don't have collision
            if (!entity.hasCollision(player.getCamera3d().position.x, player.getCamera3d().position.y))
                continue;

            //did we collect this?
            boolean collected = false;

            //are we adding a new weapon
            boolean add = false;

            Collectible collectible = (Collectible)entity;

            //handle the collectible accordingly
            switch (collectible.getType()) {

                case smg:
                    weapons.add(WeaponHelper.Type.smg);
                    add = true;
                    collected = true;
                    player.setTextNotify(getTranslatedText(KEY_NOTIFICATION_SMG));
                    break;

                case impact:
                    weapons.add(WeaponHelper.Type.impact);
                    add = true;
                    collected = true;
                    player.setTextNotify(getTranslatedText(KEY_NOTIFICATION_IMPACT));
                    break;

                case magnum:
                    weapons.add(WeaponHelper.Type.magnum);
                    add = true;
                    collected = true;
                    player.setTextNotify(getTranslatedText(KEY_NOTIFICATION_MAGNUM));
                    break;

                case buzzsaw:
                    weapons.add(WeaponHelper.Type.buzz);
                    add = true;
                    collected = true;
                    player.setTextNotify(getTranslatedText(KEY_NOTIFICATION_BUZZ));
                    break;

                case shotgun:
                    weapons.add(WeaponHelper.Type.shotgun);
                    add = true;
                    collected = true;
                    player.setTextNotify(getTranslatedText(KEY_NOTIFICATION_SHOTGUN));
                    break;

                case ammo:

                    //only collect if we aren't at the max
                    if (weapons.getWeapon().getBullets() < weapons.getWeapon().getType().getBulletsMax()) {
                        playSfx(level.getAssetManager(), collectible.getSoundEffect());
                        weapons.getWeapon().addAmmoSmall();
                        player.setTextNotify(getTranslatedText(KEY_NOTIFICATION_AMMO));
                        collected = true;
                    }
                    break;

                case ammo_crate:

                    //only collect if we aren't at the max
                    if (weapons.getWeapon().getBullets() < weapons.getWeapon().getType().getBulletsMax()) {
                        playSfx(level.getAssetManager(), collectible.getSoundEffect());
                        weapons.getWeapon().addAmmoLarge();
                        player.setTextNotify(getTranslatedText(KEY_NOTIFICATION_AMMO));
                        collected = true;
                    }
                    break;

                case health_large:

                    //only collect if we aren't at the max
                    if (player.getHealth() < HEALTH_MAX) {
                        playSfx(level.getAssetManager(), collectible.getSoundEffect());
                        player.setHealth(player.getHealth() + HEALTH_LARGE);
                        player.setTextNotify(getTranslatedText(KEY_NOTIFICATION_HEALTH_LARGE));
                        collected = true;
                    }
                    break;

                case health_small:

                    //only collect if we aren't at the max
                    if (player.getHealth() < HEALTH_MAX) {
                        playSfx(level.getAssetManager(), collectible.getSoundEffect());
                        player.setHealth(player.getHealth() + HEALTH_SMALL);
                        player.setTextNotify(getTranslatedText(KEY_NOTIFICATION_HEALTH_SMALL));
                        collected = true;
                    }
                    break;

                case key:
                    playSfx(level.getAssetManager(), collectible.getSoundEffect());
                    player.setTextNotify(getTranslatedText(KEY_NOTIFICATION_KEY));
                    player.setKey(true);
                    collected = true;
                    break;
            }

            //flag false so we can't collect again
            if (collected) {
                entity.setSolid(false);
                player.setCollect(true);
            }

            //flag we are switching weapons
            if (add) {
                playSfx(level.getAssetManager(), AudioHelper.Sfx.WeaponChange);
                player.getController().setChange(true);
            }
        }
    }
}