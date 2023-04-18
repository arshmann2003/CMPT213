package ca.myApp.restapi;

/**
 * Store x and y positions of mae objects
 */

public class ApiLocationWrapper {
    public int x;
    public int y;

    public ApiLocationWrapper(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
}
