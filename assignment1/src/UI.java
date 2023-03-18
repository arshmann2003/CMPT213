import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class UI {
    public void welcomeMessage(){
        System.out.println(
                "***************************************\n" +
                        "Welcome to the Evil Minion Tracker (tm)\n" +
                        "by Arshdeep Mann (301461759).\n" +
                        "***************************************"
        );
    }
    public void displayMenu(){
        System.out.println();
        System.out.println("*************");
        System.out.println("* MAIN MENU *");
        System.out.println("*************");
        System.out.println("1. List minions");
        System.out.println("2. Add a new minion");
        System.out.println("3. Remove a minion");
        System.out.println("4. Attribute evil deed to a minion");
        System.out.println("5. DEBUG: Dump objects (toString)");
        System.out.println("6. Exit");
    }
    public void displayMinionsToString(StoreMinions dataBase){
        System.out.println("All minion objects:");
        for(int i=0; i<dataBase.getNumberOfMinions(); i++){
            System.out.println(i+1 + " " + dataBase.getMinionAtIndex(i).toString());
        }
    }
    public void displayEvilDeedMessage(Minion min){
        System.out.println(min.getName() + " has now done " + min.getNumberOfEvilDeeds() + " evil deed(s)!");
    }
    public int getMenuChoice(){
        Scanner myObj = new Scanner(System.in);
        int userName = myObj.nextInt();
        return userName;
    }
    public List<Object> getNewMinionInfo(){
        List<Object> sections = new ArrayList<Object>();
        Scanner myObj = new Scanner(System.in);

        System.out.print("Enter Minion's Name: ");
        String name = myObj.nextLine();
        name = name.strip();
        sections.add(name);
        System.out.print("Enter Minion's height: ");
        float height = myObj.nextFloat();
        sections.add(height);
        return sections;
    }
    public int getChosenIndex(int bound){
        int n = bound;
        if(n==0)
            return -1;

        while(true){
            System.out.println("(Enter 0 to cancel)");
            Scanner myObj = new Scanner(System.in);
            int i = myObj.nextInt();
            if(i==0)
                return -1;
            else if(i<=n && i>0)
                return i-1;

            System.out.println("Invalid index choose from 1..." + n);
        }
    }
}
