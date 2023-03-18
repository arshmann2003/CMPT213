package model;

import java.util.*;

/**
 * Holds general structure of maze and perform moves of cats, mouses, cheese
 */

public class Maze {
    private final Cell[][] cells;
    private final MazeGenerator generator;
    private final int numOfRows;
    private final int numOfColumns;

    public Maze(int rows, int columns) {
        this.numOfRows = rows;
        this.numOfColumns = columns;
        this.cells = new Cell[rows][columns];
        this.generator = new MazeGenerator(cells);
        generateMaze();
        initializeCells();
        hideCellsInitially();
    }

    private void hideCellsInitially() {
        for(int i=1; i<cells.length-1; i++){
            for(int j=1; j<cells[0].length-1; j++){
                cells[i][j].setVisited(false);
            }
        }
    }

    private void generateMaze() {
        generator.generateMaze();
    }

    private void initializeCells() {
        Cell mouseCell = cells[1][1];
        mouseCell.setMouse(true);
        mouseCell.setWall(false);

        Cell cat1 = cells[1][cells[0].length-2];
        cat1.setCat(true);
        cat1.setWall(false);
        cat1.setCatVisited(true);

        Cell cat2 =  cells[cells.length-2][cells[0].length-2];
        cat2.setCat(true);
        cat2.setWall(false);
        cat2.setCatVisited(true);

        Cell cat3 = cells[cells.length-2][1];
        cat3.setCat(true);
        cat3.setWall(false);

        addNewCheese();
        createMazeBorder();
        removeAdjacentWalls();
    }

    private void removeAdjacentWalls() {
        cells[1][2].setWall(false);
        cells[2][1].setWall(false);
        cells[1][cells[0].length-3].setWall(false);
        cells[2][cells[0].length-2].setWall(false);
        cells[cells.length-2][cells[0].length-3].setWall(false);
        cells[cells.length-3][cells[0].length-2].setWall(false);
        cells[cells.length-2][2].setWall(false);
        cells[cells.length-3][1].setWall(false);
    }

    public void addNewCheese(){
        Random random = new Random();
        int minNumber = 1;
        int maxNumber = cells.length-2;
        int minNumber2 = 1;
        int maxNumber2 = cells[0].length-2;

        int rand1 = random.nextInt((maxNumber - minNumber) + 1) + minNumber;
        int rand2 = random.nextInt((maxNumber2 - minNumber2) + 1) + minNumber2;

        while(true){
            if(!cells[rand1][rand2].isWall() && !cells[rand1][rand2].isMouse() && !cells[rand1][rand2].isCat()){
                cells[rand1][rand2].setCheese(true);
                break;
            }else{
                rand1 = random.nextInt((maxNumber - minNumber) + 1) + minNumber;
                rand2 = random.nextInt((maxNumber2 - minNumber2) + 1) + minNumber2;
            }
        }
    }

    private void createMazeBorder(){
        for(int i=0; i< cells[0].length; i++){
            cells[0][i].setWall(true);
            cells[0][i].setVisited(true);
            cells[cells.length-1][i].setWall(true);
            cells[cells.length-1][i].setVisited(true);

        }
        for(int i=1; i<cells.length; i++){
            cells[i][0].setWall(true);
            cells[i][0].setVisited(true);
            cells[i][cells[0].length-1].setWall(true);
            cells[i][cells[0].length-1].setVisited(true);
        }
    }

    public boolean LooseCondition(){
        for(int i=0; i<cells.length; i++){
            for(int j=0; j<cells[0].length; j++){
                if(cells[i][j].isMouse() && cells[i][j].isCat())
                    return true;
            }
        }
        return false;
    }

    public void moveCatInMaze(Cat cat) {
        int row = cat.getX();
        int col = cat.getY();
        Cell currentCell = cells[row][col];
        cat.addOldPosition(currentCell);
        currentCell.setCat(false);

        boolean moved = false;
        boolean foundNewPosition = true;
        Set<Cell> visitedCells = new HashSet<>();
        List<int[]> directions = Arrays.asList(new int[] { 1, 0 }, new int[] { 0, -1 }, new int[] { -1, 0 }, new int[] { 0, 1 });
        int[] lastDirection = null;
        Cell previousCell = null;

        while (!moved) {
            Collections.shuffle(directions);
            for (int[] direction : directions) {
                if (Arrays.equals(direction, lastDirection))
                    continue;
                int newRow = row + direction[0];
                int newCol = col + direction[1];

                if (newRow < 0 || newRow >= cells.length || newCol < 0 || newCol >= cells[0].length)
                    continue;

                Cell newCell = cells[newRow][newCol];
                if (newCell.isWall() || newCell.isCat() || newCell.hasCatVisited())
                    continue;
                newCell.setCat(true);
                cat.setX(newRow);
                cat.setY(newCol);
                newCell.setCatVisited(true);
                currentCell = newCell;
                cat.addOldPosition(currentCell);
                moved = true;
                visitedCells.add(currentCell);
                lastDirection = direction;
                previousCell = cells[row][col];
                break;
            }
            if (!moved) {
                foundNewPosition = false;
                for (int i = directions.size() - 1; i >= 0; i--) {
                    int[] direction = directions.get(i);

                    if (Arrays.equals(direction, lastDirection)) {
                        continue;
                    }

                    int newRow = row + direction[0];
                    int newCol = col + direction[1];

                    if (newRow < 0 || newRow >= cells.length || newCol < 0 || newCol >= cells[0].length) {
                        continue;
                    }

                    Cell newCell = cells[newRow][newCol];
                    if (newCell.isWall() || newCell.isCat())
                        continue;
                    newCell.setCat(true);
                    cat.setX(newRow);
                    cat.setY(newCol);
                    newCell.setCatVisited(true);
                    currentCell = newCell;
                    cat.addOldPosition(currentCell);
                    moved = true;
                    visitedCells.add(currentCell);
                    lastDirection = direction;
                    previousCell = cells[row][col];
                    break;
                }
            }
            if (!moved)
                break;
            row = cat.getX();
            col = cat.getY();
        }
    }

    public void visitAdjacentCells(int row, int col){
        // visit vertically adjacent cells above (three positions)
        for(int i=col-1; i<=col+1; i++){
            if(row-1 < cells.length && row-1 >= 0 && i >= 0 && i < cells[0].length){
                Cell cell = cells[row-1][i];
                if(!cells[row-1][i].isVisited()){
                    cell.setVisited(true);
                }
            }
            if(row+1 < cells.length && row+1 >=0 && i >= 0 && i < cells[0].length){
                Cell cell = cells[row+1][i];
                if(!cell.isVisited()){
                    cell.setVisited(true);
                }
            }
        }
        // visit horizontally adjacent cells
        if(col-1 >= 0 && col-1 < cells[0].length){
            cells[row][col-1].setVisited(true);
        }
        if(col+1 >= 0 && col+1 < cells[0].length){
            cells[row][col+1].setVisited(true);
        }
    }

    public Cell[][] getCells() {
        return cells;
    }

}


