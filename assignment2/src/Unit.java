import java.util.ArrayList;

/**
 * Unit which represents one item in inventory management application
*/

public class Unit {
    public String serialNumber;
    public String model;
    public int numOfTestsPassed;
    public String dateShipped;
    ArrayList<ArrayList<String>> tests;

    Unit(){
        this.tests = new ArrayList<ArrayList<String>>();
    }
}
