import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;

/**
 * Display all text menus, and read menu options
*/

public class TextMenu {
    private static final int EXTRA_CHARACTERS_IN_TITLE = 4;
    private static final int MINIMUM_SELECTION_NUMBER = 1;

    private final String title;
    private final String[] options;

    public TextMenu(String title, String[] options) {
        this.title = title;
        this.options = options;
    }

    public static void displayShipUnitsHeader() {
        System.out.println("READ-TO-SHIP Water Purification Units:");
        System.out.println("******************************************");
        System.out.println("     Model           Serial      #Tests   Ship Date");
        System.out.println("----------  ---------------  ----------  ----------");

    }

    public static void displayDefectiveUnitsHeader() {
        System.out.println("DEFECTIVE Water Purification Units:");
        System.out.println("***************************************");
        System.out.println("     Model           Serial      #Tests   Ship Date");
        System.out.println("----------  ---------------  ----------  ----------");
    }

    public void display() {
        // Header
        System.out.println();
        displayRowOfStars(title.length());
        displayHeaderTextRow();
        displayRowOfStars(title.length());

        // Options
        for (int i = 0; i < options.length; i++) {
            int num = i + MINIMUM_SELECTION_NUMBER;
            System.out.println(num + ". " + options[i]);
        }
    }

    private void displayRowOfStars(int length) {
        for (int i = 0; i < length + EXTRA_CHARACTERS_IN_TITLE; i++) {
            System.out.print("*");
        }
        System.out.println();
    }

    private void displayHeaderTextRow() {
        System.out.println("* " + title + " *");
    }

    public int getSelection() {
        return getNumberBetween(MINIMUM_SELECTION_NUMBER, getNumOptions());
    }

    static public int getNumberBetween(int min, int max) {
        Scanner keyboard = new Scanner(System.in);
        boolean inputOk = false;
        int selection = 0;
        do {
            System.out.print("> ");
            selection = keyboard.nextInt();
            inputOk = (selection >= min && selection <= max);
            if (!inputOk) {
                System.out.println("Error: Please enter a selection between "
                        + min + " and " + max);
            }
        } while (!inputOk);
        return selection;
    }

    private int getNumOptions() {
        return options.length;
    }

    public boolean getUnitInfo(Unit unit, ArrayList<String> serialNumbers) {
        System.out.println("Enter product info; blank line to quit");
        String model = getModelInfo();
        if(model=="")
            return false;
        String serialNumber = getSerialNumber(serialNumbers);
        if(serialNumber=="")
            return false;

        unit.model = model;
        unit.serialNumber = serialNumber;
        unit.numOfTestsPassed = 0;
        unit.dateShipped = "-";
        return true;
    }

    private String getSerialNumber(ArrayList<String> serialNumbers){
        Scanner keyboard = new Scanner(System.in);
        String serialNumber = "";

        while(true){
            System.out.print("Serial number: ");
            serialNumber = keyboard.nextLine();
            if(Objects.equals(serialNumber, "")){return "";}

            if(serialNumbers.contains(serialNumber)){
                System.out.println("Serial number is already chosen please try again");
            }else{
                boolean isDigitsOnly = serialNumber.matches("[0-9]+");

                if(isDigitsOnly && (serialNumber.length()<=15)) {
                    if (checkSum(serialNumber))
                        return serialNumber;
                    else
                        displayCheckSumError();
                }else{
                    displayOnlyDigitsMessage();
                }
            }
        }
    }

    public static boolean validSerialNumber(String serialNumber){
        boolean validRange = serialNumber.length()<=15 && serialNumber.length()>=3;
        return serialNumber.matches("[0-9]+") && validRange && checkSum(serialNumber);
    }

    private String getModelInfo(){
        Scanner keyboard = new Scanner(System.in);
        String model = "";
        System.out.print("Model: ");
        model = keyboard.nextLine();
        return model;
    }

    private static boolean checkSum(String userInput){

        int n = userInput.length();
        String lastValue = String.valueOf(userInput.charAt(n-1));
        String secLastValue = String.valueOf(userInput.charAt(n-2));
        String lastTwoDigits = secLastValue+lastValue;

        int sum = 0;
        for(int i=0; i<n-2; i++){
            sum += Integer.parseInt(String.valueOf(userInput.charAt(i)));
        }
        sum %= 100;
        if(sum!=Integer.parseInt(lastTwoDigits)) {return false;}
        return true;
    }

    private void displayCheckSumError() {
        System.out.println("Unable to add the product.");
        System.out.println("    'Serial Number Error: Checksum does not match.'");
        System.out.println("Please try again.");
    }

    private void displayOnlyDigitsMessage() {
        System.out.println("Unable to add the product.");
        System.out.println("    Only 15 or less digits in serial number.");
    }

    public void displaySortOrderOptionMenu(){
        System.out.print("*************************************\n");
        System.out.print("* Select desired report sort order: *\n");
        System.out.print("*************************************\n");
        System.out.print("1. Sort by serial number\n");
        System.out.print("2. Sort by model, then serial number.\n");
        System.out.print("3. Sort by most recent test date.\n");
        System.out.print("4. Cancel\n");
    }

    public void displaySortOrderMenu() {
        System.out.print("\n******************\n");
        System.out.print("* Report Options *\n");
        System.out.print("******************\n");
        System.out.print("1. ALL:           All products.\n");
        System.out.print("2. DEFECTIVE:     Products that failed their last test.\n");
        System.out.print("3. READY-TO-SHIP: Products passed tests, not shipped.\n");
        System.out.println("4. Cancel report request.");
    }

    public static void displayListOfUnitsHeader() {
        System.out.println("\nList of Water Purification Units:");
        System.out.println("*************************************");
        System.out.println("     Model           Serial      #Tests   Ship Date");
        System.out.println("----------  ---------------  ----------  ----------");
    }
}
