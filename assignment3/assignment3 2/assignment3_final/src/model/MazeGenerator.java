package model;

import java.util.Random;

/**
 * Generate maze using the depth-first search algorithm
 */

public class MazeGenerator {
    private final Cell[][] maze;

    public MazeGenerator(Cell[][] cells) {
        this.maze = cells;
    }

    public void generateMaze() {
        for (int i = 0; i < maze.length; i++) {
            for (int j = 0; j < maze[0].length; j++) {
                maze[i][j] = new Cell(i, j);
            }
        }
        Random random = new Random();
        int height = maze.length;
        int width = maze[0].length;

        int row = random.nextInt(height-1);
        int col = random.nextInt(width-1);
        maze[row][col].setVisited(true);

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                Cell cell = maze[i][j];
                if(validCell(i, j)) {
                    maze[i][j].setWall(true);
                }
            }
        }
        generateMazeDFS(row, col);
        removeSquares();
    }

    private boolean validCell(int i, int j) {
        if( (i==1 && j==1) )
            return false;
        else if(i==1 && j==maze[0].length-2)
            return false;
        else if(i==maze.length-2 && j==1)
            return false;
        else if(i==maze.length-2 && j==maze[0].length-2)
            return false;

        return true;
    }

    public void generateMazeDFS(int row, int col) {
        int[] directions = {0, 1, 2, 3};
        shuffleArray(directions);
        maze[row][col].setVisited(true);
        for (int i = 0; i < directions.length; i++) {
            int direction = directions[i];
            int neighborRow = row;
            int neighborCol = col;
            if (direction == 0 && row > 0)
                neighborRow--;
            else if (direction == 1 && col < maze[0].length - 1)
                neighborCol++;
            else if (direction == 2 && row < maze.length - 1)
                neighborRow++;
            else if (direction == 3 && col > 0)
                neighborCol--;

            if (!maze[neighborRow][neighborCol].isVisited()) {
                if (direction == 0) {
                    setAdjacentWall(neighborRow, neighborCol, false, Direction.UP);
                } else if (direction == 1) {
                    setAdjacentWall(neighborRow, neighborCol , false, Direction.RIGHT);
                } else if (direction == 2) {
                    setAdjacentWall(neighborRow, neighborCol, false, Direction.DOWN);
                } else {
                    setAdjacentWall(neighborRow, neighborCol, false, Direction.LEFT);
                }
                generateMazeDFS(neighborRow, neighborCol);
            }
        }
        removeWalls();
    }

    private void removeWalls() {
        for (int i = 0; i < maze.length; i++) {
            for (int j = 0; j < maze[0].length ; j++) {
                if (!maze[i][j].isWall())
                    setNeighborsWall(i, j);
            }
        }
    }

    private void setNeighborsWall(int i, int j) {
        for (Direction dir : Direction.values()) {
            int neighborRow = i + deltaRow(dir);
            int neighborCol = j + deltaCol(dir);
            if(neighborRow<maze.length && neighborRow>=0 && neighborCol<maze[0].length && neighborCol>=0)
                if (!maze[neighborRow][neighborCol].isWall()) {
                    setAdjacentWall(i, j, false, dir);
                }
        }
    }

    private void removeSquares() {
        Random random = new Random();
        for (int i = 1; i < maze.length-1 ; i++) {
            for (int j = 1; j < maze[0].length-1 ; j++) {
                if (maze[i][j].isWall() && maze[i][j+1].isWall() &&
                    maze[i+1][j].isWall() && maze[i+1][j+1].isWall()) {
                    int rand = random.nextInt(4);
                    removeWall(rand, i, j);
                }
            }
        }
    }

    private void removeWall(int rand, int i, int j) {
        if (rand == 0) {
            maze[i][j].setWall(false);
        } else if (rand == 1) {
            maze[i][j+1].setWall(false);
        } else if (rand == 2) {
            maze[i+1][j].setWall(false);
        } else {
            maze[i+1][j+1].setWall(false);
        }
    }


    public int deltaRow(Direction direction) {
        if (direction == Direction.UP) {
            return -1;
        } else if (direction == Direction.DOWN) {
            return 1;
        } else {
            return 0;
        }
    }

    public int deltaCol(Direction direction) {
        if (direction == Direction.LEFT) {
            return -1;
        } else if (direction == Direction.RIGHT) {
            return 1;
        } else {
            return 0;
        }
    }

    private void shuffleArray(int[] array) {
        Random rand = new Random();
        for (int i=array.length-1; i>0; i--) {
            int index = rand.nextInt(i + 1);
            int temp = array[index];
            array[index] = array[i];
            array[i] = temp;
        }
    }

    private void setAdjacentWall(int row, int col, boolean b, Direction direction) {
        Cell cell = null;
        if(row < maze.length && row >= 0){
            cell = maze[row][col];
        }
        if(cell==null)
            return;

        int newRow = row;
        int newCol = col;

        switch(direction){
            case UP ->{
                newRow = row-1;
            }
            case DOWN -> {
                newRow = row+1;
            }
            case RIGHT -> {
                newCol = col+1;
            }
            case LEFT -> {
                newCol = col-1;
            }
        }
        if(newRow>=0 && newRow<maze.length && newCol>=0 && newCol<maze[0].length){
            maze[newRow][newCol].setWall(false);
        }
    }
}
