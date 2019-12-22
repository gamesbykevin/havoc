package com.gamesbykevin.havoc.obstacles;

import static com.gamesbykevin.havoc.dungeon.Dungeon.getRandom;

public class ObstacleHelper {

    public static Obstacle.Type getRandomTypeGrass() {

        switch (getRandom().nextInt(11)) {
            case 0:
            default:
                return Obstacle.Type.Grass1;
            case 1:
                return Obstacle.Type.Grass2;
            case 2:
                return Obstacle.Type.Grass3;
            case 3:
                return Obstacle.Type.Grass4;
            case 4:
                return Obstacle.Type.Grass5;
            case 5:
                return Obstacle.Type.Grass6;
            case 6:
                return Obstacle.Type.Grass7;
            case 7:
                return Obstacle.Type.Grass8;
            case 8:
                return Obstacle.Type.Grass9;
            case 9:
                return Obstacle.Type.Grass10;
            case 10:
                return Obstacle.Type.Grass11;
        }
    }

    public static Obstacle.Type getRandomTypePillar() {

        switch (getRandom().nextInt(5)) {
            case 0:
            default:
                return Obstacle.Type.pillar1;
            case 1:
                return Obstacle.Type.pillar2;
            case 2:
                return Obstacle.Type.pillar3;
            case 3:
                return Obstacle.Type.pillar4;
            case 4:
                return Obstacle.Type.pillar5;
        }
    }

    public static Obstacle.Type getRandomTypeLight() {

        switch (getRandom().nextInt(7)) {
            case 0:
            default:
                return Obstacle.Type.Light1;
            case 1:
                return Obstacle.Type.Light2;
            case 2:
                return Obstacle.Type.Light3;
            case 3:
                return Obstacle.Type.Light4;
            case 4:
                return Obstacle.Type.Light5;
            case 5:
                return Obstacle.Type.Light6;
            case 6:
                return Obstacle.Type.Light7;
        }
    }

    public static Obstacle.Type getRandomTypeStatue() {

        switch (getRandom().nextInt(5)) {
            case 0:
            default:
                return Obstacle.Type.statue1;
            case 1:
                return Obstacle.Type.statue2;
            case 2:
                return Obstacle.Type.statue3;
            case 3:
                return Obstacle.Type.statue4;
            case 4:
                return Obstacle.Type.statue5;
        }
    }

    public static Obstacle.Type getRandomTypeFlag() {

        switch (getRandom().nextInt(2)) {
            case 0:
            default:
                return Obstacle.Type.flag1;
            case 1:
                return Obstacle.Type.flag2;
        }
    }

    public static Obstacle.Type getRandomTypeCage() {

        switch (getRandom().nextInt(4)) {
            case 0:
            default:
                return Obstacle.Type.cage1;
            case 1:
                return Obstacle.Type.cage2;
            case 2:
                return Obstacle.Type.cage3;
            case 3:
                return Obstacle.Type.cage4;
        }
    }

    public static Obstacle.Type getRandomTypePlant() {

        switch (getRandom().nextInt(4)) {
            case 0:
            default:
                return Obstacle.Type.BluePotLargeEmpty;
            case 1:
                return Obstacle.Type.BluePotPlant1;
            case 2:
                return Obstacle.Type.BluePotPlant2;
            case 3:
                return Obstacle.Type.YellowPotPlant;
        }
    }

    public static Obstacle.Type getRandomTypeOther() {

        switch (getRandom().nextInt(12)) {
            case 0:
            default:
                return Obstacle.Type.Candle;
            case 1:
                return Obstacle.Type.table;
            case 2:
                return Obstacle.Type.spear1;
            case 3:
                return Obstacle.Type.spear2;
            case 4:
                return Obstacle.Type.random1;
            case 5:
                return Obstacle.Type.random2;
            case 6:
                return Obstacle.Type.random3;
            case 7:
                return Obstacle.Type.random4;
            case 8:
                return Obstacle.Type.FloorLamp1;
            case 9:
                return Obstacle.Type.FloorLamp2;
            case 10:
                return Obstacle.Type.pots1;
            case 11:
                return  Obstacle.Type.pots2;
        }
    }
}