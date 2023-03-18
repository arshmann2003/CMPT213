import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

/**
 * Get user input for menu selections
*/

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

public class TextGetUserInput {

// Enter the serial number (0 for list, -1 for cancel): 12306
   public static String getSerialNumberFromUser(ArrayList<String> allSerialNumbers){
        Scanner keyboard = new Scanner(System.in);
        String serialNumber = "";
        while(true){
            System.out.print("Enter the serial number (0 for list -1 for cancel):");
            serialNumber = keyboard.nextLine();

           if (Objects.equals(serialNumber, "0") || Objects.equals(serialNumber, "-1")) {
                return serialNumber;
           }
           else if(TextMenu.validSerialNumber(serialNumber)){
                if(allSerialNumbers.contains(serialNumber)){
                    return serialNumber;
                }else{
                    System.out.println("Serial number is not within units");
                }
           }else{
               System.out.println("Invalid Serial number");
           }
       }
   }

    public static JsonArray getJsonInput() throws IOException {
        Scanner key = new Scanner(System.in);
        String message = "sub";
        System.out.println("Enter the path to the input JSON file; blank to cancel.");
        System.out.println("WARNING: This will replace all current data with data from the file.");


        while(true){
            message = key.nextLine();
            if( (Files.exists(Paths.get(message))) ) {
                break;
            } else if(message.equals("")){
                break;
            }else{
                System.out.println("File not found re enter path to JSON file; blank to cancel: ");
            }
        }
        if(!message.equals("")){
            FileReader fileReader = new FileReader(message);
            JsonParser jsonParser = new JsonParser();
            JsonElement jsonElement = jsonParser.parse(fileReader);
            JsonArray x = jsonElement.getAsJsonArray();
            System.out.printf("Read " + x.size() + " products from JSON file '" + message + "'\n" );
            return x;
        }
        return null;
    }


    public static String getTestCasePass(){
       Scanner keyboard = new Scanner(System.in);
       String message = "-1";
        while(!(message.equals("y") || message.equals("n") || message.equals(""))){
            System.out.print("Pass? (y/n): ");
            message = keyboard.nextLine();
        }
       return message;
    }
    public static String getComment(){
        Scanner key = new Scanner(System.in);
        System.out.print("Comment: ");
        return key.nextLine();
    }
    public static String getSortOrderOption(){
        Scanner key = new Scanner(System.in);
        return key.nextLine();
    }

    public static String getSortOder(){
       Scanner key = new Scanner(System.in);
       String message = "";
       while(true){
           System.out.print("Enter value: ");
            message = key.nextLine();
            if(Objects.equals(message, "1") || Objects.equals(message, "2") || Objects.equals(message, "3") || Objects.equals(message, "4"))
                return message;
            else{
                System.out.println("Try again values[1..4]");
            }
        }
    }

}
