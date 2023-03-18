import java.io.IOException;
import java.text.ParseException;

/**
 * Main driver to begin inventory  application
 */

public class Main {
    public static void main(String[] args) throws IOException, ParseException {
        TextUI ui = new TextUI();
        ui.start();
    }
}
