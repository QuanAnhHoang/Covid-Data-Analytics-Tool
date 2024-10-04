import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

/**
 * The `UserInterface` class handles user interaction for the COVID-19 data analysis application.
 * It allows users to select data, choose summary options, and display results.
 */
public class UserInterface {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("M/d/yyyy"); // Date formatter for user input
    private Scanner scanner; // Scanner for user input
    private List<Data> allData; // All data read from the CSV file
    private List<Data> selectedData; // Data selected by the user based on location and date range
    private Summary summary; // Summary object for calculating summary statistics
    private Display display; // Display object for displaying the results


    /**
     * Constructor for the UserInterface.
     * @param allData The complete list of COVID-19 data.
     */
    public UserInterface(List<Data> allData) {
        this.scanner = new Scanner(System.in);
        this.allData = allData;
    }

    /**
     * Runs the main user interface loop.  Presents the menu, gets user input, and performs actions.
     */
    public void run() {
        while (true) {
            displayMenu();
            int choice = getValidIntInput(1, 4);

            switch (choice) {
                case 1:
                    selectData(); // Select data based on location and date range
                    break;
                case 2:
                    chooseSummaryOptions(); // Choose summary options (grouping, metric, result type)
                    break;
                case 3:
                    displayResults(); // Display the calculated summary results
                    break;
                case 4:
                    System.out.println("Exiting program. Goodbye!");
                    return; // Exit the program
            }
        }
    }

    /**
     * Displays the main menu options to the user.
     */
    private void displayMenu() {
        System.out.println("\nCOVID-19 Data Analysis");
        System.out.println("1. Select Data");
        System.out.println("2. Choose Summary Options");
        System.out.println("3. Display Results");
        System.out.println("4. Exit");
    }


    /**
     * Gets valid integer input from the user within a specified range.
     * @param min The minimum acceptable value.
     * @param max The maximum acceptable value.
     * @return The valid integer input from the user.
     */
    private int getValidIntInput(int min, int max) {
        while (true) {
            System.out.print("Enter your choice (" + min + "-" + max + "): ");
            try {
                int choice = Integer.parseInt(scanner.nextLine().trim());
                if (choice >= min && choice <= max) {
                    return choice;
                } else {
                    System.out.println("Invalid input. Please enter a number between " + min + " and " + max + ".");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
            }
        }
    }

    private void selectData() {
        System.out.print("Enter location (country or continent): ");
        String location = scanner.nextLine().trim();

        LocalDate startDate = getValidDate("Enter start date (M/d/yyyy): ");
        LocalDate endDate = getValidDate("Enter end date (M/d/yyyy): ");

        if (endDate.isBefore(startDate)) {
            System.out.println("End date cannot be before start date. Please try again.");
            return;
        }

        DateRange dateRange = new DateRange(startDate, endDate);

        selectedData = allData.stream()
                .filter(d -> (d.getLocation().equalsIgnoreCase(location) || d.getContinent().equalsIgnoreCase(location))
                        && !d.getDate().isBefore(dateRange.getStartDate())
                        && !d.getDate().isAfter(dateRange.getEndDate()))
                .toList();

        if (selectedData.isEmpty()) {
            System.out.println("No data found for the specified location and date range.");
        } else {
            System.out.println("Data selected: " + selectedData.size() + " records");
        }
    }

    private LocalDate getValidDate(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            try {
                return LocalDate.parse(input, DATE_FORMATTER);
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format. Please use M/d/yyyy format.");
            }
        }
    }

    private void chooseSummaryOptions() {
        if (selectedData == null || selectedData.isEmpty()) {
            System.out.println("Please select data first.");
            return;
        }

        GroupingStrategy groupingStrategy = chooseGroupingStrategy();
        Summary.Metric metric = chooseMetric();
        Summary.ResultType resultType = chooseResultType();

        try {
            summary = new Summary(selectedData, groupingStrategy, metric, resultType);
            System.out.println("Summary options selected and applied.");
        } catch (IllegalArgumentException e) {
            System.out.println("Error creating summary: " + e.getMessage());
        }
    }

    private GroupingStrategy chooseGroupingStrategy() {
        System.out.println("Choose grouping method:");
        System.out.println("1. No grouping");
        System.out.println("2. Number of groups");
        System.out.println("3. Number of days per group");
        int groupingChoice = getValidIntInput(1, 3);

        switch (groupingChoice) {
            case 1:
                return new Summary.NoGrouping();
            case 2:
                System.out.print("Enter number of groups: ");
                int numberOfGroups = getValidIntInput(1, selectedData.size());
                return new Summary.NumberOfGroups(numberOfGroups);
            case 3:
                System.out.print("Enter number of days per group: ");
                int daysPerGroup = getValidIntInput(1, selectedData.size());
                return new Summary.NumberOfDays(daysPerGroup);
            default:
                throw new IllegalStateException("Unexpected value: " + groupingChoice);
        }
    }

    private Summary.Metric chooseMetric() {
        System.out.println("Choose metric:");
        System.out.println("1. Positive cases");
        System.out.println("2. Deaths");
        System.out.println("3. People vaccinated");
        int metricChoice = getValidIntInput(1, 3);

        switch (metricChoice) {
            case 1:
                return Summary.Metric.POSITIVE_CASES;
            case 2:
                return Summary.Metric.DEATHS;
            case 3:
                return Summary.Metric.PEOPLE_VACCINATED;
            default:
                throw new IllegalStateException("Unexpected value: " + metricChoice);
        }
    }

    private Summary.ResultType chooseResultType() {
        System.out.println("Choose result type:");
        System.out.println("1. New Total");
        System.out.println("2. Up To");
        int resultTypeChoice = getValidIntInput(1, 2);

        return (resultTypeChoice == 2) ? Summary.ResultType.UP_TO : Summary.ResultType.NEW_TOTAL;
    }

    private void displayResults() {
        if (summary == null) {
            System.out.println("Please select data and choose summary options first.");
            return;
        }

        System.out.println("Choose display method:");
        System.out.println("1. Tabular");
        System.out.println("2. Chart");
        int displayChoice = getValidIntInput(1, 2);

        display = (displayChoice == 2) ? new ChartDisplay() : new TabularDisplay();

        List<Summary.SummaryResult> results = summary.calculate();
        if (results.isEmpty()) {
            System.out.println("No results to display.");
        } else {
            display.show(results);
        }
    }
}