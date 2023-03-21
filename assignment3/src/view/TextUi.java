package view;

import model.*;
import java.util.ArrayList;
import java.util.Objects;

/**
 * Manage user interaction and display aspects of the maze
 */

public class TextUi {
    private Maze board;
    private TextMenu textMenu;
    private Mouse mouse;
    private ArrayList<Cat> cats;
    private int numOfCheeseFound;
    private int requiredCheese;

    public void start() {
        this.numOfCheeseFound = 0;
        this.requiredCheese = 5;
        board = new Maze(15, 20);

        updatePosAfterGeneration();
        displayWelcomeMessage();
        textMenu = new TextMenu();
        textMenu.displayDirectionsMenu();

        displayMaze(true);
        beginGame();
    }

    private void beginGame() {
        boolean looseCond = false;
        boolean hidden = true;
        String moveOption = "";
        while (numOfCheeseFound!=requiredCheese) {
            moveOption = getMoveOption();

            if(Objects.equals(moveOption, "C")){
                requiredCheese = 1;
                moveOption = getMoveOption();
            }
            if(Objects.equals(moveOption, "M")){
                hidden = false;
            }
            if (Objects.equals(moveOption, "?"))
                textMenu.displayDirectionsMenu();
            else {
                if(!Objects.equals(moveOption, "M")){
                    moveMouse(moveOption);
                    moveCats();
                }
                displayMaze(hidden);
                displayNumOfCollectedCheese();
                if(board.LooseCondition()){
                    looseCond = true;
                    displayMouseEatenMessage();
                    break;
                }
            }
        }
        if(looseCond){
            displayMaze(hidden);
            displayNumOfCollectedCheese();
            displayGameLooseMessage();
        }
    }

    public void moveCats(){
        Cat cat1 = cats.get(0);
        Cat cat2 = cats.get(1);
        Cat cat3 = cats.get(2);
        board.moveCatInMaze(cat3);
        board.moveCatInMaze(cat1);
        board.moveCatInMaze(cat2);
    }

    public void moveMouse(String option){
        Cell[][] cells = board.getCells();
        Cell oldMouseCell = cells[mouse.getHorizontalPosition()][mouse.getVerticalPosition()];
        oldMouseCell.setVisited(true);
        oldMouseCell.setMouse(false);

        switch (option) {
            case "W" -> mouse.move(Direction.UP);
            case "S" -> mouse.move(Direction.DOWN);
            case "A" -> mouse.move(Direction.LEFT);
            case "D" -> mouse.move(Direction.RIGHT);
        }

        Cell newMousePosition = cells[mouse.getHorizontalPosition()][mouse.getVerticalPosition()];
        newMousePosition.setMouse(true);
        if(newMousePosition.isCheese()){
            this.numOfCheeseFound++;
            newMousePosition.setCheese(false);
            if(requiredCheese!=numOfCheeseFound)
                board.addNewCheese();
        }
        board.visitAdjacentCells(mouse.getHorizontalPosition(), mouse.getVerticalPosition());
    }

    public void updatePosAfterGeneration(){
        Cell[][] cells = board.getCells();
        this.cats = new ArrayList<>();

        for(int i=0; i<cells.length; i++){
            for(int j=0; j<cells[0].length; j++){
                if(cells[i][j].isMouse()){
                    mouse = new Mouse(i, j);
                    board.visitAdjacentCells(i, j);
                }
                else if(cells[i][j].isCat()){
                    cats.add(new Cat(i, j));
                }
                else if(cells[i][j].isCheese()){
                    Cheese cheese = new Cheese(i, j);
                }
            }
        }
    }

    public String getMoveOption() {

        String userMoveChoice = "";

        while (true) {

            userMoveChoice = textMenu.getMenuOption();

            if (Objects.equals(userMoveChoice, "W")) {
                if (mouse.validMove(Direction.UP, board.getCells()))
                    return userMoveChoice;
            }

            if (Objects.equals(userMoveChoice, "S") ) {
                if(mouse.validMove(Direction.DOWN, board.getCells()))
                    return userMoveChoice;
            }
            if (Objects.equals(userMoveChoice, "D") ) {
                if(mouse.validMove(Direction.RIGHT, board.getCells()))
                    return userMoveChoice;
            }
            if (Objects.equals(userMoveChoice, "A") ) {
                if(mouse.validMove(Direction.LEFT, board.getCells()))
                    return userMoveChoice;
            }
            if (Objects.equals(userMoveChoice, "C") || Objects.equals(userMoveChoice, "?") || Objects.equals(userMoveChoice, "M")) {
                return userMoveChoice;
            }
            textMenu.displayInvalidMove();
        }
    }

    private void displayMouseEatenMessage() {
        System.out.println("I'm sorry, you have been eaten!");
    }

    public void displayGameLooseMessage(){
        System.out.println("GAME OVER; please try again.");
    }

    public void displayMaze(boolean hidden) {
        System.out.println("\nMaze:");
        Cell[][] copy = board.getCells(); // changing this has no effect on underlying data structure

        for (int i=0; i<copy.length; i++) {
            for (int j = 0; j < copy[0].length; j++) {
                Cell cell = copy[i][j];
                displayItemGraphic(cell, hidden);
            }
            System.out.println();
        }
    }

    private void displayItemGraphic(Cell cell, Boolean hidden) {
        if(cell.isCat())
            System.out.print("!");
        else if(cell.isMouse())
            System.out.print("@");
        else if(cell.isCheese())
            System.out.print("$");
        else{
            if(hidden) {
                if(cell.isVisited() && cell.isWall())
                    System.out.print("#");
                else if(cell.isVisited() && !cell.isWall())
                    System.out.print(" ");
                else
                    System.out.print(".");
            }
            else{
                if(cell.isWall())
                    System.out.print("#");
                else
                    System.out.print(" ");

            }
        }
    }

    public void displayNumOfCollectedCheese(){
       System.out.println("Cheese collected: "+ numOfCheeseFound + " of " + requiredCheese);
    }

    public void displayWelcomeMessage() {
        System.out.println
                ("""
        ----------------------------------------
        Welcome to Cat and Mouse Maze Adventure!
        by Arshdeep Mann
        ----------------------------------------
        """);
    }
}