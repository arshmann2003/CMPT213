package view;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/**
 * Game Menu For User Interaction
 */

public class TextMenu {

    public void displayMenuOption(){
        System.out.print("Enter your move [WASD?]: ");
    }

    public String getMenuOption(){
        Scanner keyboard = new Scanner(System.in);
        String str = "";

        List<String> validOptions = Arrays.asList("W", "S", "A", "D", "?", "C", "M"); // where arr = int[] arr;

        while(!validOptions.contains(str)){
            displayMenuOption();
            str = keyboard.nextLine().toUpperCase();

            if(!validOptions.contains(str)){
                displayMenuErrorMessage();
            }else{
                break;
            }
        }
        return str.toUpperCase();
    }

    public void displayMenuErrorMessage(){
        System.out.println("Invalid move. Please enter just A (left), S (down), D (right), or W (up).");
    }

    public void displayDirectionsMenu(){
        System.out.print("""
        DIRECTIONS:
        Find 5 cheese before a cat eats you!
                LEGEND:
                    #: Wall
        @: You (a mouse)
        !: Cat
        $: Cheese
                .: Unexplored space
        MOVES:
        Use W (up), A (left), S (down) and D (right) to move.
        (You must press enter after each move).
        """);
    }

    public void displayInvalidMove() {
        System.out.println("Invalid move: you cannot move through walls!");
    }
}

