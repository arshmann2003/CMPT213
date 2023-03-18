package model;

/**
 * Get and change positions the mouse in maze
 */

public class Mouse {
    private int horizontalPosition;
    private int verticalPosition;

    public Mouse(int x, int y) {
        this.horizontalPosition = x;
        this.verticalPosition = y;
    }

    public int getHorizontalPosition() {
        return horizontalPosition;
    }

    public int getVerticalPosition() {
        return verticalPosition;
    }

    public boolean validMove(Direction direction, Cell[][] cells) {
        int updatedHorizontalPos = horizontalPosition + direction.getX();
        int updatedVerticalPos = verticalPosition + direction.getY();
        int i = updatedHorizontalPos;
        int j = updatedVerticalPos;

        if(i < 0 || i >= cells.length)
            return false;
        if(j < 0 || j >= cells[0].length)
            return false;

        return !cells[i][j].isWall() && !cells[i][j].isCat();
    }

    public void move(Direction direction) {
        horizontalPosition += direction.getX();
        verticalPosition += direction.getY();
    }
}
