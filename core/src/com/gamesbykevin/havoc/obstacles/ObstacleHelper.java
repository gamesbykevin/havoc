package com.gamesbykevin.havoc.obstacles;

import java.util.ArrayList;
import java.util.List;

import static com.gamesbykevin.havoc.dungeon.Dungeon.getRandom;

public class ObstacleHelper {

    private static List<Integer> OPTIONS_RANDOM_TYPE;

    private static List<Obstacles.Type> OPTIONS_OTHER;
    private static List<Obstacles.Type> OPTIONS_PLANT;
    private static List<Obstacles.Type> OPTIONS_SPECIMEN;
    private static List<Obstacles.Type> OPTIONS_WELL;
    private static List<Obstacles.Type> OPTIONS_CAGE;
    private static List<Obstacles.Type> OPTIONS_BARREL;
    private static List<Obstacles.Type> OPTIONS_FLAG;
    private static List<Obstacles.Type> OPTIONS_STATUE;
    private static List<Obstacles.Type> OPTIONS_LIGHT;
    private static List<Obstacles.Type> OPTIONS_PILLAR;
    private static List<Obstacles.Type> OPTIONS_GRASS;

    protected static void recycle() {

        if (OPTIONS_OTHER != null)
           OPTIONS_OTHER.clear();
        OPTIONS_OTHER = null;

        if (OPTIONS_PLANT != null)
            OPTIONS_PLANT.clear();
        OPTIONS_PLANT = null;

        if (OPTIONS_SPECIMEN != null)
            OPTIONS_SPECIMEN.clear();
        OPTIONS_SPECIMEN = null;

        if (OPTIONS_WELL != null)
            OPTIONS_WELL.clear();
        OPTIONS_WELL = null;

        if (OPTIONS_CAGE != null)
            OPTIONS_CAGE.clear();
        OPTIONS_CAGE = null;

        if (OPTIONS_BARREL != null)
            OPTIONS_BARREL.clear();
        OPTIONS_BARREL = null;

        if (OPTIONS_FLAG != null)
            OPTIONS_FLAG.clear();
        OPTIONS_FLAG = null;

        if (OPTIONS_STATUE != null)
            OPTIONS_STATUE.clear();
        OPTIONS_STATUE = null;

        if (OPTIONS_LIGHT != null)
            OPTIONS_LIGHT.clear();
        OPTIONS_LIGHT = null;

        if (OPTIONS_PILLAR != null)
            OPTIONS_PILLAR.clear();
        OPTIONS_PILLAR = null;

        if (OPTIONS_GRASS != null)
            OPTIONS_GRASS.clear();
        OPTIONS_GRASS = null;

        if (OPTIONS_RANDOM_TYPE != null)
            OPTIONS_RANDOM_TYPE.clear();
        OPTIONS_RANDOM_TYPE = null;
    }

    protected static Obstacles.Type getRandomGrass() {

        if (OPTIONS_GRASS == null)
            OPTIONS_GRASS = new ArrayList<>();

        if (OPTIONS_GRASS.isEmpty()) {
            OPTIONS_GRASS.add(Obstacles.Type.Grass1);
            OPTIONS_GRASS.add(Obstacles.Type.Grass2);
            OPTIONS_GRASS.add(Obstacles.Type.Grass3);
            OPTIONS_GRASS.add(Obstacles.Type.Grass4);
            OPTIONS_GRASS.add(Obstacles.Type.Grass5);
            OPTIONS_GRASS.add(Obstacles.Type.Grass6);
            OPTIONS_GRASS.add(Obstacles.Type.Grass7);
            OPTIONS_GRASS.add(Obstacles.Type.Grass8);
            OPTIONS_GRASS.add(Obstacles.Type.Grass9);
            OPTIONS_GRASS.add(Obstacles.Type.Grass10);
            OPTIONS_GRASS.add(Obstacles.Type.Grass11);
        }

        return getType(OPTIONS_GRASS);
    }

    protected static Obstacles.Type getRandomPillar() {

        if (OPTIONS_PILLAR == null)
            OPTIONS_PILLAR = new ArrayList<>();

        if (OPTIONS_PILLAR.isEmpty()) {
            OPTIONS_PILLAR.add(Obstacles.Type.pillar1);
            OPTIONS_PILLAR.add(Obstacles.Type.pillar2);
            OPTIONS_PILLAR.add(Obstacles.Type.pillar3);
            OPTIONS_PILLAR.add(Obstacles.Type.pillar4);
            OPTIONS_PILLAR.add(Obstacles.Type.pillar5);
        }

        return getType(OPTIONS_PILLAR);
    }

    protected static Obstacles.Type getRandomLight() {

        if (OPTIONS_LIGHT == null)
            OPTIONS_LIGHT = new ArrayList<>();

        if (OPTIONS_LIGHT.isEmpty()) {
            OPTIONS_LIGHT.add(Obstacles.Type.Light1);
            OPTIONS_LIGHT.add(Obstacles.Type.Light2);
            OPTIONS_LIGHT.add(Obstacles.Type.Light3);
            OPTIONS_LIGHT.add(Obstacles.Type.Light4);
            OPTIONS_LIGHT.add(Obstacles.Type.Light5);
            OPTIONS_LIGHT.add(Obstacles.Type.Light6);
            OPTIONS_LIGHT.add(Obstacles.Type.Light7);
        }

        return getType(OPTIONS_LIGHT);
    }

    protected static Obstacles.Type getRandomStatue() {

        if (OPTIONS_STATUE == null)
            OPTIONS_STATUE = new ArrayList<>();

        if (OPTIONS_STATUE.isEmpty()) {
            OPTIONS_STATUE.add(Obstacles.Type.statue1);
            OPTIONS_STATUE.add(Obstacles.Type.statue2);
            OPTIONS_STATUE.add(Obstacles.Type.statue3);
            OPTIONS_STATUE.add(Obstacles.Type.statue4);
            OPTIONS_STATUE.add(Obstacles.Type.statue5);
        }

        return getType(OPTIONS_STATUE);
    }

    protected static Obstacles.Type getRandomFlag() {

        if (OPTIONS_FLAG == null)
            OPTIONS_FLAG = new ArrayList<>();

        if (OPTIONS_FLAG.isEmpty()) {
            OPTIONS_FLAG.add(Obstacles.Type.flag1);
            OPTIONS_FLAG.add(Obstacles.Type.flag2);
        }

        return getType(OPTIONS_FLAG);
    }

    protected static Obstacles.Type getRandomBarrel() {

        if (OPTIONS_BARREL == null)
            OPTIONS_BARREL = new ArrayList<>();

        if (OPTIONS_BARREL.isEmpty()) {
            OPTIONS_BARREL.add(Obstacles.Type.barrel1);
            OPTIONS_BARREL.add(Obstacles.Type.barrel2);
        }

        return getType(OPTIONS_BARREL);
    }

    protected static Obstacles.Type getRandomCage() {

        if (OPTIONS_CAGE == null)
            OPTIONS_CAGE = new ArrayList<>();

        if (OPTIONS_CAGE.isEmpty()) {
            OPTIONS_CAGE.add(Obstacles.Type.cage1);
            OPTIONS_CAGE.add(Obstacles.Type.cage2);
            OPTIONS_CAGE.add(Obstacles.Type.cage3);
            OPTIONS_CAGE.add(Obstacles.Type.cage4);
        }

        return getType(OPTIONS_CAGE);
    }

    protected static Obstacles.Type getRandomWell() {

        if (OPTIONS_WELL == null)
            OPTIONS_WELL = new ArrayList<>();

        if (OPTIONS_WELL.isEmpty()) {
            OPTIONS_WELL.add(Obstacles.Type.well1);
            OPTIONS_WELL.add(Obstacles.Type.well2);
            OPTIONS_WELL.add(Obstacles.Type.well3);
            OPTIONS_WELL.add(Obstacles.Type.well4);
            OPTIONS_WELL.add(Obstacles.Type.well5);
        }

        return getType(OPTIONS_WELL);
    }

    protected static Obstacles.Type getRandomSpecimen() {

        if (OPTIONS_SPECIMEN == null)
            OPTIONS_SPECIMEN = new ArrayList<>();

        if (OPTIONS_SPECIMEN.isEmpty()) {
            OPTIONS_SPECIMEN.add(Obstacles.Type.SpecimenPod1);
            OPTIONS_SPECIMEN.add(Obstacles.Type.SpecimenPod2);
        }

        return getType(OPTIONS_SPECIMEN);
    }

    protected static Obstacles.Type getRandomPlant() {

        if (OPTIONS_PLANT == null)
            OPTIONS_PLANT = new ArrayList<>();

        if (OPTIONS_PLANT.isEmpty()) {
            OPTIONS_PLANT.add(Obstacles.Type.BluePotEmpty);
            OPTIONS_PLANT.add(Obstacles.Type.BluePotLargeEmpty);
            OPTIONS_PLANT.add(Obstacles.Type.BluePotPlant1);
            OPTIONS_PLANT.add(Obstacles.Type.BluePotPlant2);
            OPTIONS_PLANT.add(Obstacles.Type.YellowPotPlant);
        }

        return getType(OPTIONS_PLANT);
    }

    private static Obstacles.Type getRandomOther() {

        if (OPTIONS_OTHER == null)
            OPTIONS_OTHER = new ArrayList<>();

        if (OPTIONS_OTHER.isEmpty()) {
            OPTIONS_OTHER.add(Obstacles.Type.Candle);
            OPTIONS_OTHER.add(Obstacles.Type.table);
            OPTIONS_OTHER.add(Obstacles.Type.spear1);
            OPTIONS_OTHER.add(Obstacles.Type.spear2);
            OPTIONS_OTHER.add(Obstacles.Type.random1);
            OPTIONS_OTHER.add(Obstacles.Type.random2);
            OPTIONS_OTHER.add(Obstacles.Type.random3);
            OPTIONS_OTHER.add(Obstacles.Type.random4);
            OPTIONS_OTHER.add(Obstacles.Type.FloorLamp1);
            OPTIONS_OTHER.add(Obstacles.Type.FloorLamp2);
            OPTIONS_OTHER.add(Obstacles.Type.DogFood);
            OPTIONS_OTHER.add(Obstacles.Type.pots1);
            OPTIONS_OTHER.add(Obstacles.Type.pots2);
        }

        return getType(OPTIONS_OTHER);
    }

    private static Obstacles.Type getType(List<Obstacles.Type> list) {
        int index = getRandom().nextInt(list.size());
        Obstacles.Type type = list.get(index);
        list.remove(index);
        return type;
    }

    protected static void assignRandomType() {

        if (OPTIONS_RANDOM_TYPE == null)
            OPTIONS_RANDOM_TYPE = new ArrayList<>();

        if (OPTIONS_RANDOM_TYPE.isEmpty()) {
            for (int i = 0; i < 9; i++) {
                OPTIONS_RANDOM_TYPE.add(i);
            }
        }

        int index = getRandom().nextInt(OPTIONS_RANDOM_TYPE.size());

        switch (OPTIONS_RANDOM_TYPE.get(index)) {

            case 0:
                Obstacle.TYPE = getRandomStatue();
                break;

            case 1:
                Obstacle.TYPE = getRandomFlag();
                break;

            case 2:
                Obstacle.TYPE = getRandomCage();
                break;

            case 3:
                Obstacle.TYPE = getRandomBarrel();
                break;

            case 4:
                Obstacle.TYPE = getRandomWell();
                break;

            case 5:
                Obstacle.TYPE = getRandomSpecimen();
                break;

            case 6:
                Obstacle.TYPE = getRandomPlant();
                break;

            case 7:
                Obstacle.TYPE = getRandomOther();
                break;

            case 8:
                Obstacle.TYPE = getRandomGrass();
                break;
        }

        //remove from the list
        OPTIONS_RANDOM_TYPE.remove(index);
    }
}