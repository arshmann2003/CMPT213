import com.google.gson.JsonArray;
import org.w3c.dom.Text;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * User interface for inventory management application
 */

public class TextUI {
    private static final String[] mainMenuOptions = {
            "Read JSON input file.",
            "Display info on a unit.",
            "Create new unit.",
            "Test a unit.",
            "Ship a unit.",
            "Print report.",
            "Set report sort order.",
            "EXIT."
    };
    private static final int OPTION_READ_JSON = 1;
    private static final int OPTION_DISPLAY_UINFO = OPTION_READ_JSON + 1;
    private static final int OPTION_CREATE_UNIT = OPTION_DISPLAY_UINFO + 1;
    private static final int OPTION_TEST_UNIT = OPTION_CREATE_UNIT + 1;
    private static final int OPTION_SHIP_UNIT = OPTION_TEST_UNIT + 1;
    private static final int OPTION_PRINT_REPORT = OPTION_SHIP_UNIT + 1;
    private static final int OPTION_SET_SORT_ORDER = OPTION_PRINT_REPORT + 1;
    private static final int OPTION_END = OPTION_SET_SORT_ORDER + 1;

    private UnitStorage units;
    private TextMenu mainMenu;

    public void start() throws IOException, ParseException {
        displayWelcomeMessage();
        this.units = new UnitStorage();

        TextMenu mainMenu = new TextMenu("Main Menu", mainMenuOptions);
        this.mainMenu = mainMenu;
        int option = OPTION_PRINT_REPORT;
        do {
            mainMenu.display();
            option = mainMenu.getSelection();
            handleMenuOption(option);
        } while (option != OPTION_END);
    }

    private void displayWelcomeMessage() {
        System.out.println("***************************************");
        System.out.println("Water Purification Inventory Management");
        System.out.println("by Arshdeep Mann.");
        System.out.println("***************************************");
    }

    private void handleMenuOption(int option) throws IOException, ParseException {
        switch (option) {
            case OPTION_READ_JSON:
                readJSON();
                break;
            case OPTION_DISPLAY_UINFO:
                displayUnitInfo();
                break;
            case OPTION_CREATE_UNIT:
                createUnit();
                break;
            case OPTION_TEST_UNIT:
                testUnit();
                break;
            case OPTION_SHIP_UNIT:
                shipUnit();
                break;
            case OPTION_PRINT_REPORT:
                printReport();
                break;
            case OPTION_SET_SORT_ORDER:
                setSortOrder();
                break;
        }
    }

    private void readJSON() throws IOException {
        JsonArray inputFileDate = TextGetUserInput.getJsonInput();
        if(inputFileDate!=null){
            units.convertJsonToUnits(inputFileDate);
        }
    }

    private void displayUnitInfo() {
        if(units.getUnits().size() < 1){
            System.out.println("No units defined");
            System.out.println("Please create a unit and then re-try this option");
        }else{
            while(true){
                String serialNumber  = TextGetUserInput.getSerialNumberFromUser(units.getSerialNumbers());

                if(units.containsSerialNumber(serialNumber)){
                    units.displaySingleUnit(serialNumber);
                    return;
                }
                else if(Objects.equals(serialNumber, "0")){
                    units.displayUnits();
                }
                else if(Objects.equals(serialNumber, "-1")){
                    return;
                }
            }
        }
    }

    private void shipUnit() {
        if(units.getUnits().size() < 1){
            System.out.println("No units defined");
            System.out.println("Please create a unit and then re-try this option");
        }else{
            while(true){
                String serialNumber  = TextGetUserInput.getSerialNumberFromUser(units.getSerialNumbers());

                if(units.shipUnit(serialNumber)){
                    System.out.println("Unit successfully shipped.");
                    return;
                }
                else if(Objects.equals(serialNumber, "0")){
                    units.displayUnits();
                }
                else if(Objects.equals(serialNumber, "-1")){
                    return;
                }
            }
        }
    }

    private void testUnit() {
        if(units.getUnits().size() < 1){
            System.out.println("No units defined");
            System.out.println("Please create a unit and then re-try this option");
        }else{
            while(true){
                String serialNumber  = TextGetUserInput.getSerialNumberFromUser(units.getSerialNumbers());

                if(units.containsSerialNumber(serialNumber)){
                    String passed = TextGetUserInput.getTestCasePass();
                    String message = TextGetUserInput.getComment();
                    units.updateTestCase(serialNumber, passed, message);
                    System.out.println("Test Recorded.");
                    return;
                }
                else if(Objects.equals(serialNumber, "0")){
                    units.displayUnits();
                }
                else if(Objects.equals(serialNumber, "-1")){
                    return;
                }
            }
        }
    }

    private void createUnit() {
        Unit unit = new Unit();
        boolean serialNumberNotInStorage = mainMenu.getUnitInfo(unit, units.getSerialNumbers());
        if(serialNumberNotInStorage)
            units.addUnit(unit);
    }

    private void printReport() throws ParseException {
        mainMenu.displaySortOrderMenu();
        String x = TextGetUserInput.getSortOder();

        if(x.equals("1")){
            units.displayUnits();
        }else if(x.equals("2")){
            units.displayDefectiveUnits(); // defective units
        }else if(x.equals("3")){
            units.displayUnitsReadyToShip(); // units ready to ship
        }
    }

    private void setSortOrder() {
        ArrayList<String> validOptions = new ArrayList<>(Arrays.asList("1", "2", "3", "4"));
        String getSortOrderOption = "";

        while(!(validOptions.contains(getSortOrderOption))){
            mainMenu.displaySortOrderOptionMenu();
            getSortOrderOption = TextGetUserInput.getSortOrderOption();
        }
        if(Objects.equals(getSortOrderOption, "1")){   //Sort by serial number
            units.sortBySerialNumber();
        }else if(Objects.equals(getSortOrderOption, "2")){ //Sort by model, then serial number.
            units.sortByModelFirst();
        }else if(Objects.equals(getSortOrderOption, "3")){ //Sort by most recent test date.
            units.sortByRecentTestDate();
        }   // end reached if option 4
    }
}

