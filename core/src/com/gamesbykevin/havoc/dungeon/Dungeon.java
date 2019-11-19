package com.gamesbykevin.havoc.dungeon;

import com.gamesbykevin.havoc.astar.AStar;
import com.gamesbykevin.havoc.dungeon.Cell.Type;
import com.gamesbykevin.havoc.level.Level;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.gamesbykevin.havoc.dungeon.DungeonHelper.*;

public class Dungeon {

    //the map of the dungeon
    private boolean[][] map;

    //easier to keep track of where the interact objects are
    private boolean[][] interact;

    //details of the map
    private Cell[][] cells;

    //object used to make random decisions
    private static Random RANDOM;

    //used to find shortest path
    private AStar aStar;

    //binary space partitioning
    private List<Leaf> leafs;

    //the start location for the player
    private int startCol, startRow;

    //the goal to beat the level
    private int goalCol, goalRow;

    //which leaf is the goal contained within
    private int goalLeafIndex = -1;

    //reference to our level
    private final Level level;

    public Dungeon(Level level, int cols, int rows) {

        //store the reference to our level
        this.level = level;

        //object used for path finding
        this.aStar = new AStar();

        //create the map for our dungeon
        this.map = new boolean[rows][cols];

        //create array of interact items
        this.interact = new boolean[rows][cols];

        //this array will contain the details of the map
        this.cells = new Cell[rows][cols];

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {

                //all edges of the room will be walls
                Type type = (col == 0 || row == 0 || col == cols - 1 || row == rows - 1) ? Type.Wall : Type.Unvisited;

                //create the cell
                getCells()[row][col] = new Cell(col, row, type);
            }
        }

        updateMap();
    }

    //generate a dungeon
    public void generate() {

        //binary space partitioning, to create our separate rooms
        divide(this);

        //now we can connect the rooms together
        connect(this);

        //fill the remaining unvisited spaces with walls
        fill(Type.Wall);

        //where does the player start
        assignStart();

        //update the map accordingly
        updateMap();

        //calculate the cost to figure out where the goal is
        calculateCost();

        //identify where the finish is
        locateGoal();

        //update the map
        updateMap();

        //print the dungeon so we can see it
        print();
    }

    private void calculateCost() {

        //set cost of start to be 0
        getCells()[getStartRow()][getStartCol()].setCost(0);

        List<Cell> cells = new ArrayList<>();
        cells.add(getCells()[getStartRow()][getStartCol()]);

        //continue as long as we have options
        while (!cells.isEmpty()) {

            Cell cell = cells.get(0);

            for (int x = -1; x <= 1; x++) {
                for (int y = -1; y <= 1; y++) {
                    if (x != 0 && y != 0 || x == 0 && y == 0)
                        continue;

                    if (getMap()[cell.getRow() + y][cell.getCol() + x] && getCells()[cell.getRow() + y][cell.getCol() + x].getCost() < 0) {
                        getCells()[cell.getRow() + y][cell.getCol() + x].setCost(cell.getCost() + 1);
                        cells.add(getCells()[cell.getRow() + y][cell.getCol() + x]);
                    }
                }
            }

            //remove the cell from the list
            cells.remove(0);
        }

        cells = null;
    }

    private void locateGoal() {

        int cost = -1;

        //locate the goal
        for (int row = 0; row < getRows(); row++) {
            for (int col = 0; col < getCols(); col++) {

                //if the cost is higher then this is the current goal
                if (getCells()[row][col].getCost() > cost) {

                    //the new cost to beat
                    cost = getCells()[row][col].getCost();

                    //this is the current goal
                    setGoalCol(col);
                    setGoalRow(row);
                }
            }
        }

        //flag the goal
        getCells()[getGoalRow()][getGoalCol()].setType(Type.Goal);

        //locate the child leaf containing the goal
        for (int i = 0; i < getLeafs().size(); i++) {

            Leaf leaf = getLeafs().get(i);

            if (leaf.hasChildren() || leaf.getRoom() == null)
                continue;

            if (getGoalCol() < leaf.getX())
                continue;
            if (getGoalCol() > leaf.getX() + leaf.getW() - 1)
                continue;
            if (getGoalRow() < leaf.getY())
                continue;
            if (getGoalRow() > leaf.getY() + leaf.getH() - 1)
                continue;

            //assign the index
            setGoalLeafIndex(i);

            //exit loop
            break;
        }
    }

    //mark where the player can go and where the obstacles are
    public void updateMap() {

        for (int row = 0; row < getRows(); row++) {
            for (int col = 0; col < getCols(); col++) {
                switch (getCells()[row][col].getType()) {
                    case DoorLocked:
                    case Door:
                    case Secret:
                        getMap()[row][col] = true;
                        getInteract()[row][col] = true;
                        break;

                    case Open:
                    case Unvisited:
                        getMap()[row][col] = true;
                        getInteract()[row][col] = false;
                        break;

                    case Goal:
                        getMap()[row][col] = false;
                        getInteract()[row][col] = true;
                        break;

                    case Wall:
                    default:
                        getMap()[row][col] = false;
                        getInteract()[row][col] = false;
                        break;
                }

                //if obstacles exist
                if (getLevel().getObstacles() != null && getLevel().getObstacles().hasCollision(col, row))
                    getMap()[row][col] = false;
            }
        }

        //update the map for our path finding
        getAStar().setMap(getMap());
    }

    public AStar getAStar() {
        return this.aStar;
    }

    public List<Leaf> getLeafs() {

        if (this.leafs == null)
            this.leafs = new ArrayList<>();

        return this.leafs;
    }

    private void fill(Type type) {

        //update all unvisited locations
        for (int row = 0; row < getRows(); row++) {

            for (int col = 0; col < getCols(); col++) {

                switch (getCells()[row][col].getType()) {

                    case Unvisited:
                        getCells()[row][col].setType(type);
                        break;
                }
            }
        }
    }

    public Cell[][] getCells() {
        return this.cells;
    }

    private boolean[][] getMap() {
        return this.map;
    }

    public boolean hasMap(int col, int row) {
        return getMap()[row][col];
    }

    private boolean[][] getInteract() {
        return this.interact;
    }

    public boolean hasInteract(int col, int row) {

        if (col < 0 || row < 0)
            return false;
        if (col >= getInteract()[0].length || row >= getInteract().length)
            return false;

        return getInteract()[row][col];
    }

    public int getCols() {
        return getMap()[0].length;
    }

    public int getRows() {
        return getMap().length;
    }

    public static Random getRandom() {
        if (RANDOM == null)
            RANDOM = new Random();

        return RANDOM;
    }

    private void assignStart() {

        for (int i = 0; i < getLeafs().size(); i++) {
            Leaf leaf = getLeafs().get(i);

            if (leaf.hasChildren())
                continue;

            if (leaf.getRoom() == null)
                continue;

            if (leaf.getX() != 0 || leaf.getY() != 0)
                continue;

            //set the start location
            setStartCol(leaf.getRoom().getX() + (leaf.getRoom().getW() / 2));
            setStartRow(leaf.getRoom().getY() + (leaf.getRoom().getH() / 2));
            break;
        }
    }

    public int getGoalLeafIndex() {
        return this.goalLeafIndex;
    }

    public void setGoalLeafIndex(int goalLeafIndex) {
        this.goalLeafIndex = goalLeafIndex;
    }

    public int getGoalCol() {
        return this.goalCol;
    }

    public void setGoalCol(int goalCol) {
        this.goalCol = goalCol;
    }

    public int getGoalRow() {
        return this.goalRow;
    }

    public void setGoalRow(int goalRow) {
        this.goalRow = goalRow;
    }

    public int getStartCol() {
        return this.startCol;
    }

    public void setStartCol(int startCol) {
        this.startCol = startCol;
    }

    public int getStartRow() {
        return this.startRow;
    }

    public void setStartRow(int startRow) {
        this.startRow = startRow;
    }

    private void print() {

        for (int row = 0; row < getRows(); row++) {

            String line = "";
            for (int col = 0; col < getCols(); col++) {

                /*
                int cost = getCells()[row][col].getCost();

                if (cost < 0) {
                    line += "    ";
                } else if (cost < 10) {
                    line += "000" + cost;
                } else if (cost < 100) {
                    line += "00" + cost;
                } else if (cost < 1000) {
                    line += "0" + cost;
                } else {
                    line += cost;
                }
                line += ",";
                */

                switch (getCells()[row][col].getType()) {

                    case Secret:
                        System.out.print("r");
                        break;

                    case Door:
                    case DoorLocked:
                        System.out.print("Z");
                        break;

                    case Goal:
                        System.out.print("g");
                        break;

                    case Wall:
                        System.out.print(" ");
                        break;

                    case Open:
                        System.out.print("o");
                        break;

                    case Unvisited:
                        System.out.print("_");
                        break;
                }
            }
            //System.out.println(line);
            System.out.println();
        }
    }

    public Level getLevel() {
        return this.level;
    }
}