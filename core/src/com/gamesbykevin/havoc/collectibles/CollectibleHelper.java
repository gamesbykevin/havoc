package com.gamesbykevin.havoc.collectibles;

import com.gamesbykevin.havoc.entities.Entities;
import com.gamesbykevin.havoc.entities.Entity;
import com.gamesbykevin.havoc.level.Level;
import com.gamesbykevin.havoc.player.Player;
import com.gamesbykevin.havoc.weapon.WeaponHelper;
import com.gamesbykevin.havoc.weapon.Weapons;

import static com.gamesbykevin.havoc.player.Player.HEALTH_MAX;
import static com.gamesbykevin.havoc.player.PlayerHelper.HEALTH_LARGE;
import static com.gamesbykevin.havoc.player.PlayerHelper.HEALTH_SMALL;

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

            //handle the collectible accordingly
            switch (((Collectible)entity).getType()) {

                case smg:
                    weapons.add(WeaponHelper.Type.Smg);
                    add = true;
                    collected = true;
                    break;

                case glock:
                    weapons.add(WeaponHelper.Type.Glock);
                    add = true;
                    collected = true;
                    break;

                case impact:
                    weapons.add(WeaponHelper.Type.Impact);
                    add = true;
                    collected = true;
                    break;

                case magnum:
                    weapons.add(WeaponHelper.Type.Magnum);
                    add = true;
                    collected = true;
                    break;

                case buzzsaw:
                    weapons.add(WeaponHelper.Type.Buzz);
                    add = true;
                    collected = true;
                    break;

                case shotgun:
                    weapons.add(WeaponHelper.Type.Shotgun);
                    add = true;
                    collected = true;
                    break;

                case ammo:

                    //only collect if we aren't at the max
                    if (weapons.getWeapon().getBullets() < weapons.getWeapon().getType().getBulletsMax()) {
                        weapons.getWeapon().addAmmoSmall();
                        collected = true;
                    }
                    break;

                case ammo_crate:

                    //only collect if we aren't at the max
                    if (weapons.getWeapon().getBullets() < weapons.getWeapon().getType().getBulletsMax()) {
                        weapons.getWeapon().addAmmoLarge();
                        collected = true;
                    }
                    break;

                case health_large:

                    //only collect if we aren't at the max
                    if (player.getHealth() < HEALTH_MAX) {
                        player.setHealth(player.getHealth() + HEALTH_LARGE);
                        collected = true;
                    }
                    break;

                case health_small:

                    //only collect if we aren't at the max
                    if (player.getHealth() < HEALTH_MAX) {
                        player.setHealth(player.getHealth() + HEALTH_SMALL);
                        collected = true;
                    }
                    break;

                case key:
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
            if (add)
                player.getController().setChange(true);
        }
    }
}