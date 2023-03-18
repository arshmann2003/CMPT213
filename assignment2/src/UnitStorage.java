import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.lang.reflect.Array;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Store, update, and keep track of a database of units
*/

public class UnitStorage {

    private ArrayList<Unit> units;

    UnitStorage(){
        this.units = new ArrayList<Unit>();
    }

    public void addUnit(Unit e){
        units.add(e);
        units.sort(Comparator.comparing(a -> a.serialNumber));
    }

    public void sortBySerialNumber(){
        units.sort(Comparator.comparing(a -> a.serialNumber));
    }

    public void sortByModelFirst(){
        units.sort(Comparator.comparing(a -> a.model));
    }

    public void sortByRecentTestDate(){

        units.sort(Comparator.comparing(a -> a.dateShipped));
    }

    public ArrayList<Unit> getUnits(){
        return units;
    }


    public ArrayList<String> getSerialNumbers(){
        ArrayList<String> models = new ArrayList<String>();
        for (Unit unit : units) {
            models.add(unit.serialNumber);
        }
        return models;
    }

    public boolean containsSerialNumber(String serialNumber){
        for (Unit unit : units) {
            if (Objects.equals(unit.serialNumber, serialNumber)) {
                return true;
            }
        }
        return false;
    }

    public boolean shipUnit(String serialNumber){
        for(Unit unit : units){
            if(Objects.equals(unit.serialNumber, serialNumber)){
                unit.dateShipped = getDate();
                return true;
            }
        }
        return false;
    }

    public void displayDefectiveUnits() throws ParseException {
        TextMenu.displayDefectiveUnitsHeader();
        for (Unit unit : units) {


            ArrayList<ArrayList<String>> test = unit.tests;
            String newestDate = "0000-00-00";
            boolean testPassed = true;

            for (ArrayList<String> testFields : test) {

                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

                Date parsedDate1 = formatter.parse(newestDate);
                Date parsedDate2 = formatter.parse(testFields.get(0));

                if (parsedDate2.after(parsedDate1)) {
                    newestDate = testFields.get(0);
                    if(Objects.equals(testFields.get(1), "false"))
                        testPassed = false;
                    else
                        testPassed = true;
                }
            }
            if(!testPassed){
                displayUnitInReport(unit);
            }
        }
    }

    public void displayUnits(){
        TextMenu.displayListOfUnitsHeader();

        for(Unit unit : units){
            displayUnitInReport(unit);
        }
    }

    public void displayUnitInReport(Unit unit){
        int modelWidth = 10;
        int serialWidth = 15;
        int tests = 10;
        int shipDateLength = 10;
        String model = String.format("%1$" + modelWidth + "s", unit.model);
        String serialNumber = String.format("%1$" + (serialWidth+2) + "s", unit.serialNumber);
        String testTotal = String.format("%1$" + (tests+2) + "s", unit.numOfTestsPassed);
        String shipDate = String.format("%1$" + (shipDateLength+2) + "s", unit.dateShipped);
        System.out.print(model);
        System.out.print(serialNumber);
        System.out.print(testTotal);
        System.out.print(shipDate);
        System.out.println();
    }


    public void displaySingleUnit(String serialNumber){
        Unit matchingUnit = null;
        for (Unit unit : units) {
            if (Objects.equals(unit.serialNumber, serialNumber)) {
                matchingUnit = unit;
            }
        }
        if(matchingUnit!=null){
            System.out.println("\nUnit details:");
            System.out.println("  Serial: " + matchingUnit.serialNumber);
            System.out.println("  Model: " + matchingUnit.model);
            System.out.println("Ship Date: " + matchingUnit.dateShipped);
            System.out.println("\nTests\n*********");
            System.out.print("        Date   Passed?  Test Comments\n");
            System.out.print("------------  --------  ");

            int dateWidth = 12;
            int passedWidth = 8;
            ArrayList<ArrayList<String>> arr = matchingUnit.tests;
            int commentsWidth = maxTestCommentWidth(arr);
            for(int i=0; i<commentsWidth; i++){System.out.print("-");}

            System.out.println();
            for (ArrayList<String> strings : arr) {
                String date = String.format("%1$" + (dateWidth) + "s", strings.get(0));
                String passed = String.format("%1$" + (passedWidth + 2) + "s", strings.get(1));
                String comments = String.format("%1$" + (commentsWidth + 2) + "s", strings.get(2));
                System.out.print(date);
                System.out.print(passed);
                System.out.print(comments);
                System.out.println();
            }
        }
    }

    private int maxTestCommentWidth(ArrayList<ArrayList<String>> arr){
        int max = 0;
        for(ArrayList<String> specificTest : arr){
            int n = specificTest.get(2).length();
            if(n > max){
                max = n;
            }
        }
        return max;
    }


    public void displaySortOrderOption(){
        System.out.print("*************************************\n");
        System.out.print("* Select desired report sort order: *\n");
        System.out.print("*************************************\n");
        System.out.print("1. Sort by serial number\n");
        System.out.print("2. Sort by model, then serial number.\n");
        System.out.print("3. Sort by most recent test date.\n");
        System.out.print("4. Cancel\n");
    }

    public String getDate(){
        return LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE);
    }

    public void updateTestCase(String serialNumber, String pass, String message){
        ArrayList<String> testInfo = new ArrayList<String>();
        String date = getDate();
        testInfo.add(date);
        if(!Objects.equals(pass, "n"))
            testInfo.add("PASSED");
        testInfo.add(message);

        for(Unit unit : units){
            if(Objects.equals(unit.serialNumber, serialNumber)){
                unit.tests.add(testInfo);
                unit.numOfTestsPassed+=1;
            }
        }
    }

    public void insertTestInfo(Unit unit, JsonArray x){
        ArrayList<ArrayList<String>> subTests = new ArrayList<ArrayList<String>>();
        int passedTestCount=0;
        for(int j=0; j<x.size(); j++){
            JsonObject test = x.get(j).getAsJsonObject();
            ArrayList<String> tests = new ArrayList<>();
            String date = test.get("date").getAsString().trim().replaceAll("[\\s\"]+", "");
            String testPassed = test.get("isTestPassed").getAsString().trim().replaceAll("[\\s\"]+", "");
            if(testPassed.equals("true")){
                passedTestCount++;
            };
            String testResultComment = test.get("testResultComment").getAsString().trim().replaceAll("[\\s\"]+", " ");
            tests.add(date);
            tests.add(testPassed);
            tests.add(testResultComment);
            subTests.add(tests);
        }
        unit.numOfTestsPassed = subTests.size();
        unit.tests = subTests;
        units.sort(Comparator.comparing(a -> a.serialNumber));
    }

    public void convertJsonToUnits(JsonArray jsonInput) {

        clearUnits();
        int n = jsonInput.size();
        for(int i=0; i<n; i++){
            JsonObject unitAsJson = jsonInput.get(i).getAsJsonObject();
            Unit unit = new Unit();
            unit.model = String.valueOf((unitAsJson.get("model"))).trim().replaceAll("[\\s\"]+", "");
            unit.serialNumber = String.valueOf(unitAsJson.get("serialNumber")).trim().replaceAll("[\\s\"]+", "");
            unit.dateShipped = String.valueOf(unitAsJson.get("dateShipped")).trim().replaceAll("[\\s\"]+", "");
            if(unit.dateShipped.equals("null")){
                unit.dateShipped="-";
            }
            JsonArray x = unitAsJson.get("tests").getAsJsonArray();
            insertTestInfo(unit, x);
            units.add(unit);
            sortBySerialNumber();
        }
    }
    public void clearUnits(){
        units.clear();
    }

    public void displayUnitsReadyToShip() throws ParseException {
        TextMenu.displayShipUnitsHeader();
        for (Unit unit : units) {

            ArrayList<ArrayList<String>> test = unit.tests;
            String newestDate = "0000-00-00";
            boolean testPassed = true;

            for (ArrayList<String> testFields : test) {
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                Date parsedDate1 = formatter.parse(newestDate);
                Date parsedDate2 = formatter.parse(testFields.get(0));
                if (parsedDate2.after(parsedDate1)) {
                    newestDate = testFields.get(0);
                    if(Objects.equals(testFields.get(1), "false"))
                        testPassed = false;
                    else
                        testPassed = true;
                }
            }
            if(testPassed && Objects.equals(unit.dateShipped, "-")){
                displayUnitInReport(unit);
            }
        }
    }
}
