package model;

/**
 * Cell to Hold fields of each index of the maze
 */

public class Cell {
    private final int row;
    private final int column;
    private boolean visited;
    private boolean wall;
    private boolean cheese;
    private boolean cat;
    private boolean mouse;
    private boolean catHasVisited;


    public Cell(int row, int column) {
        this.row = row;
        this.column = column;
        this.visited = false;
        this.wall = false;
        this.cheese = false;
        this.cat = false;
        this.mouse = false;
        this.catHasVisited = false;
    }

    public boolean hasCatVisited(){
        return catHasVisited;
    }

    public void setCatVisited(boolean x){
        this.catHasVisited = x;
    }

    public boolean isVisited() {
        return visited;
    }

    public void setVisited(boolean visited) {
        this.visited = visited;
    }

    public boolean isWall() {
        return wall;
    }

    public void setWall(boolean wall) {
        this.wall = wall;
    }

    public boolean isCheese() {
        return cheese;
    }

    public void setCheese(boolean cheese) {
        this.cheese = cheese;
    }

    public boolean isCat() {
        return cat;
    }

    public void setCat(boolean cat) {
        this.cat = cat;
    }

    public boolean isMouse(){
        return mouse;
    }

    public void setMouse(boolean mouse){
        this.mouse = mouse;
    }
}
