package model;

import java.util.ArrayList;

/**
 * Get and change positions of cat in maze
 */

public class Cat {
    ArrayList<Cell> previousPositions;
    private int x;
    private int y;

    public Cat(int x, int y) {
        this.x = x;
        this.y = y;
        this.previousPositions = new ArrayList<>();
    }

    public void addOldPosition(Cell cell){
        previousPositions.add(cell);
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public int getX() {
        return x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public ArrayList<Cell> getOldPositions(){
        return this.previousPositions;
    }

}

