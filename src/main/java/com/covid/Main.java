import java.io.IOException;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        try {
            List<Data> allData = DataReader.readCSV("data/covid-data.csv");
            if (allData.isEmpty()) {
                System.err.println("No data was read from the CSV file. Exiting.");
                return;
            }
            UserInterface ui = new UserInterface(allData);
            ui.run();
        } catch (IOException e) {
            System.err.println("Error reading CSV file: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("An unexpected error occurred: " + e.getMessage());
            e.printStackTrace();
        }
    }
}