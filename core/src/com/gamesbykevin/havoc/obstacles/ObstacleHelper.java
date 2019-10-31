package com.gamesbykevin.havoc.obstacles;

import com.gamesbykevin.havoc.maze.Maze;

public class ObstacleHelper {

    protected static void assignRandomType() {

        switch (Maze.getRandom().nextInt(9)) {

            case 0:
                assignRandomStatue();
                break;

            case 1:
                assignRandomFlag();
                break;

            case 2:
                assignRandomCage();
                break;

            case 3:
                assignRandomBarrel();
                break;

            case 4:
                assignRandomWell();
                break;

            case 5:
                assignRandomSpecimenPod();
                break;

            case 6:
                assignRandomPlantPot();
                break;

            case 7:
                assignRandomOven();
                break;

            case 8:
                assignRandomOther();
                break;
        }
    }

    protected static void assignRandomOther() {

        switch (Maze.getRandom().nextInt(14)) {

            case 0:
                Obstacle.TYPE = Obstacles.Type.Candle;
                break;

            case 1:
                Obstacle.TYPE = Obstacles.Type.Fountain;
                break;

            case 2:
                Obstacle.TYPE = Obstacles.Type.spear1;
                break;

            case 3:
                Obstacle.TYPE = Obstacles.Type.spear2;
                break;

            case 4:
                Obstacle.TYPE = Obstacles.Type.random1;
                break;

            case 5:
                Obstacle.TYPE = Obstacles.Type.random2;
                break;

            case 6:
                Obstacle.TYPE = Obstacles.Type.random3;
                break;

            case 7:
                Obstacle.TYPE = Obstacles.Type.random4;
                break;

            case 8:
                Obstacle.TYPE = Obstacles.Type.FloorLamp1;
                break;

            case 9:
                Obstacle.TYPE = Obstacles.Type.FloorLamp2;
                break;

            case 10:
                Obstacle.TYPE = Obstacles.Type.DogFood;
                break;

            case 11:
                Obstacle.TYPE = Obstacles.Type.pots1;
                break;

            case 12:
                Obstacle.TYPE = Obstacles.Type.pots2;
                break;

            case 13:
                Obstacle.TYPE = Obstacles.Type.table;
                break;
        }
    }

    protected static void assignRandomOven() {

        switch (Maze.getRandom().nextInt(2)) {

            case 0:
                Obstacle.TYPE = Obstacles.Type.oven1;
                break;

            case 1:
                Obstacle.TYPE = Obstacles.Type.oven2;
                break;
        }
    }

    protected static void assignRandomPlantPot() {

        switch (Maze.getRandom().nextInt(5)) {

            case 0:
                Obstacle.TYPE = Obstacles.Type.BluePotEmpty;
                break;

            case 1:
                Obstacle.TYPE = Obstacles.Type.BluePotLargeEmpty;
                break;

            case 2:
                Obstacle.TYPE = Obstacles.Type.BluePotPlant1;
                break;

            case 3:
                Obstacle.TYPE = Obstacles.Type.BluePotPlant2;
                break;

            case 4:
                Obstacle.TYPE = Obstacles.Type.YellowPotPlant;
                break;
        }
    }

    protected static void assignRandomSpecimenPod() {

        switch (Maze.getRandom().nextInt(3)) {

            case 0:
                Obstacle.TYPE = Obstacles.Type.SpecimenPod1;
                break;

            case 1:
                Obstacle.TYPE = Obstacles.Type.SpecimenPod2;
                break;

            case 2:
                Obstacle.TYPE = Obstacles.Type.SpecimenPod3;
                break;
        }
    }

    protected static void assignRandomWell() {

        switch (Maze.getRandom().nextInt(5)) {

            case 0:
                Obstacle.TYPE = Obstacles.Type.well1;
                break;

            case 1:
                Obstacle.TYPE = Obstacles.Type.well2;
                break;

            case 2:
                Obstacle.TYPE = Obstacles.Type.well3;
                break;

            case 3:
                Obstacle.TYPE = Obstacles.Type.well4;
                break;

            case 4:
                Obstacle.TYPE = Obstacles.Type.well5;
                break;
        }
    }

    protected static void assignRandomCage() {

        switch (Maze.getRandom().nextInt(4)) {

            case 0:
                Obstacle.TYPE = Obstacles.Type.cage1;
                break;

            case 1:
                Obstacle.TYPE = Obstacles.Type.cage2;
                break;

            case 2:
                Obstacle.TYPE = Obstacles.Type.cage3;
                break;

            case 3:
                Obstacle.TYPE = Obstacles.Type.cage4;
                break;
        }
    }

    protected static void assignRandomBarrel() {

        switch (Maze.getRandom().nextInt(2)) {

            case 0:
                Obstacle.TYPE = Obstacles.Type.barrel1;
                break;

            case 1:
                Obstacle.TYPE = Obstacles.Type.barrel2;
                break;
        }
    }

    protected static void assignRandomFlag() {

        switch (Maze.getRandom().nextInt(2)) {

            case 0:
                Obstacle.TYPE = Obstacles.Type.flag1;
                break;

            case 1:
                Obstacle.TYPE = Obstacles.Type.flag2;
                break;
        }
    }

    protected static void assignRandomStatue() {

        switch (Maze.getRandom().nextInt(5)) {

            case 0:
                Obstacle.TYPE = Obstacles.Type.statue1;
                break;

            case 1:
                Obstacle.TYPE = Obstacles.Type.statue2;
                break;

            case 2:
                Obstacle.TYPE = Obstacles.Type.statue3;
                break;

            case 3:
                Obstacle.TYPE = Obstacles.Type.statue4;
                break;

            case 4:
                Obstacle.TYPE = Obstacles.Type.statue5;
                break;
        }
    }

    protected static void assignRandomLight() {

        //pick random light
        switch (Maze.getRandom().nextInt(7)) {

            case 0:
                Obstacle.TYPE = Obstacles.Type.Light1;
                break;

            case 1:
                Obstacle.TYPE = Obstacles.Type.Light2;
                break;

            case 2:
                Obstacle.TYPE = Obstacles.Type.Light3;
                break;

            case 3:
                Obstacle.TYPE = Obstacles.Type.Light4;
                break;

            case 4:
                Obstacle.TYPE = Obstacles.Type.Light5;
                break;

            case 5:
                Obstacle.TYPE = Obstacles.Type.Light6;
                break;

            case 6:
                Obstacle.TYPE = Obstacles.Type.Light7;
                break;
        }
    }

    protected static void assignRandomPillar() {

        switch (Maze.getRandom().nextInt(5)) {

            case 0:
                Obstacle.TYPE = Obstacles.Type.pillar1;
                break;

            case 1:
                Obstacle.TYPE = Obstacles.Type.pillar2;
                break;

            case 2:
                Obstacle.TYPE = Obstacles.Type.pillar3;
                break;

            case 3:
                Obstacle.TYPE = Obstacles.Type.pillar4;
                break;

            case 4:
                Obstacle.TYPE = Obstacles.Type.pillar5;
                break;
        }
    }

    protected static void assignRandomGrass() {

        switch (Maze.getRandom().nextInt(11)) {

            case 0:
                Obstacle.TYPE = Obstacles.Type.Grass1;
                break;

            case 1:
                Obstacle.TYPE = Obstacles.Type.Grass2;
                break;

            case 2:
                Obstacle.TYPE = Obstacles.Type.Grass3;
                break;

            case 3:
                Obstacle.TYPE = Obstacles.Type.Grass4;
                break;

            case 4:
                Obstacle.TYPE = Obstacles.Type.Grass5;
                break;

            case 5:
                Obstacle.TYPE = Obstacles.Type.Grass6;
                break;

            case 6:
                Obstacle.TYPE = Obstacles.Type.Grass7;
                break;

            case 7:
                Obstacle.TYPE = Obstacles.Type.Grass8;
                break;

            case 8:
                Obstacle.TYPE = Obstacles.Type.Grass9;
                break;

            case 9:
                Obstacle.TYPE = Obstacles.Type.Grass10;
                break;

            case 10:
                Obstacle.TYPE = Obstacles.Type.Grass11;
                break;
        }
    }
}