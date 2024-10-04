import java.io.IOException;
import java.util.List;

/**
 * Main class for the COVID data analysis application.
 * This class reads COVID data from a CSV file, initializes the user interface, and handles exceptions.
 */
public class Main {

    /**
     * Main method - entry point of the application.
     * Reads data from the CSV file, initializes the user interface, and handles potential exceptions.
     *
     * @param args Command line arguments (not used in this application).
     */
    public static void main(String[] args) {
        try {
            // Read data from the CSV file using the DataReader class
            List<Data> allData = DataReader.readCSV("data/covid-data.csv");

            // Check if any data was read. Exit if the list is empty.
            if (allData.isEmpty()) {
                System.err.println("No data was read from the CSV file. Exiting.");
                return; // Exit the application if no data is found
            }

            // Create an instance of the UserInterface, passing the data read from the CSV
            UserInterface ui = new UserInterface(allData);

            // Start the user interface interaction
            ui.run();

        } catch (IOException e) {
            // Handle IOExceptions, which can occur during file reading
            System.err.println("Error reading CSV file: " + e.getMessage()); // Print error message to the console

        } catch (Exception e) {
            // Handle any other unexpected exceptions
            System.err.println("An unexpected error occurred: " + e.getMessage()); // Print error message to the console
            e.printStackTrace(); // Print the stack trace for debugging purposes
        }
    }
}