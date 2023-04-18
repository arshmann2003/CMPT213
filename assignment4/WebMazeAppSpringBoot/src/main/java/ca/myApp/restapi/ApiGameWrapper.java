package ca.myApp.restapi;

/**
 * Hold information about maze game and update during runtime
 */

public class ApiGameWrapper {
    public int gameNumber;      // Same as the ID
    public boolean isGameWon;
    public boolean isGameLost;
    public int numCheeseFound;
    public int numCheeseGoal;

    public int getGameNumber() {
        return gameNumber;
    }

    public void setGameNumber(int gameNumber) {
        this.gameNumber = gameNumber;
    }

    public boolean isGameWon() {
        return isGameWon;
    }

    public void setIsGameWon(boolean isGameWon) {
        this.isGameWon = isGameWon;
    }

    public boolean isGameLost() {
        return isGameLost;
    }

    public void setIsGameLost(boolean isGameLost) {
        this.isGameLost = isGameLost;
    }

    public int getNumCheeseFound() {
        return numCheeseFound;
    }

    public void setNumCheeseFound(int numCheeseFound) {
        this.numCheeseFound = numCheeseFound;
    }

    public int getNumCheeseGoal() {
        return numCheeseGoal;
    }

    public void setNumCheeseGoal(int numCheeseGoal) {
        this.numCheeseGoal = numCheeseGoal;
    }
}
