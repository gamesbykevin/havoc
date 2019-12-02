package com.gamesbykevin.havoc.weapon;

import com.gamesbykevin.havoc.entities.Entities;
import com.gamesbykevin.havoc.input.MyController;
import com.gamesbykevin.havoc.level.Level;
import com.gamesbykevin.havoc.util.Disposable;
import com.gamesbykevin.havoc.util.Hud;
import com.gamesbykevin.havoc.util.Restart;

import static com.gamesbykevin.havoc.weapon.WeaponHelper.*;
import static com.gamesbykevin.havoc.util.Hud.*;

public final class Weapons extends Entities implements Disposable, Restart {

    //current selected weapon
    private int index = 0;

    public Weapons(Level level) {

        //call parent
        super(level);

        //add default weapons
        spawn();

        //start with glock
        setIndex(1);
    }

    @Override
    public void spawn() {

        //add weapon to our list
        add(Type.Lance);
        add(Type.Glock);

        for (int i = 0; i < getEntityList().size(); i++) {
            getEntityList().get(i).reset();
        }
    }

    public Weapon getWeapon() {
        return (Weapon)getEntityList().get(getIndex());
    }

    public void add(Type type) {

        //the weapon to add
        Weapon weapon = null;

        for (int i = 0; i < getEntityList().size(); i++) {
            Weapon tmp = (Weapon)getEntityList().get(i);

            if (tmp.getType() == type) {
                weapon = tmp;
                break;
            }
        }

        //if we don't have the weapon we will create and add to the list
        if (weapon == null) {

            //add the new weapon
            add(new Weapon(type));

            //assign as the current weapon
            setIndex(getEntityList().size() - 1);

            //flag switch to the new weapon
            getLevel().getPlayer().getController().setChange(true);

        } else {

            //if weapon exists we will add the bullets
            weapon.addAmmoLarge();
        }
    }

    public void setIndex(int index) {
        this.index = index;

        if (getIndex() >= getEntityList().size() || getIndex() < 0)
            this.index = 0;

        for (int i = 0; i < getEntityList().size(); i++) {
            getEntityList().get(i).setSolid(getIndex() == i);
        }
    }

    public int getIndex() {
        return this.index;
    }

    @Override
    public void update() {

        //get our current weapon
        Weapon weapon = getWeapon();

        //get the controller
        MyController controller = getLevel().getPlayer().getController();

        //if we want to switch weapons we can't be doing anything else
        if (controller.isChange()) {

            //we have to be resting to switch weapons
            if (!weapon.isResting())
                controller.setChange(false);
            if (weapon.isSwitchingOff() || weapon.isSwitchingOn())
                controller.setChange(false);
        }

        if (weapon.isSwitchingOff()) {

            //update the weapon
            switchWeaponOff(weapon);

            //if we are no longer switching off
            if (!weapon.isSwitchingOff()) {

                //reset the current weapon
                weapon.reset();

                //move to the next weapon
                setIndex(getIndex() + 1);

                //reset the new weapon
                getEntityList().get(getIndex()).reset();
            }

        } else if (weapon.isSwitchingOn()) {

            //update the weapon
            switchWeaponOn(weapon);

            //if we are done switching on
            if (!weapon.isSwitchingOn())
                controller.setChange(false);

        } else {

            if (controller.isChange())
                weapon.setSwitchingOff(true);

            //update the current weapon if we aren't switching
            weapon.update(getLevel());
        }
    }

    @Override
    public void reset() {

        //call parent
        super.reset();

        //remove all weapons
        super.getEntityList().clear();

        //spawn default weapons
        spawn();
    }

    @Override
    public boolean hasCollision(float x, float y) {

        //no need to check for collision
        return false;
    }

    @Override
    public int render() {

        //render entity
        super.render(true, -1);

        //render weapon bullet count
        Hud.renderNumber(
            getLevel().getPlayer().getController().getStage().getBatch(),
            getWeapon().getBullets(),
            HUD_BULLET_X, HUD_BULLET_Y,
            HUD_NUMBER_WIDTH, HUD_NUMBER_HEIGHT,
            HUD_NUMBER_PAD
        );

        //we rendered 1 item
        return 1;
    }

    @Override
    public void dispose() {
        super.dispose();
    }
}