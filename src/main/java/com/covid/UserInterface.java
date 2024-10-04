import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

public class UserInterface {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("M/d/yyyy");
    private Scanner scanner;
    private List<Data> allData;

    public UserInterface(List<Data> allData) {
        this.scanner = new Scanner(System.in);
        this.allData = allData;
    }

    public void run() {
        while (true) {
            System.out.println("\nCOVID-19 Data Analysis");
            System.out.println("1. Select Data");
            System.out.println("2. Choose Summary Options");
            System.out.println("3. Display Results");
            System.out.println("4. Exit");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    selectData();
                    break;
                case 2:
                    chooseSummaryOptions();
                    break;
                case 3:
                    displayResults();
                    break;
                case 4:
                    System.out.println("Exiting program. Goodbye!");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void selectData() {
        System.out.print("Enter location (country or continent): ");
        String location = scanner.nextLine();

        System.out.print("Enter start date (M/d/yyyy): ");
        LocalDate startDate = LocalDate.parse(scanner.nextLine(), DATE_FORMATTER);

        System.out.print("Enter end date (M/d/yyyy): ");
        LocalDate endDate = LocalDate.parse(scanner.nextLine(), DATE_FORMATTER);

        DateRange dateRange = new DateRange(startDate, endDate);

        // Filter data based on location and date range
        List<Data> filteredData = allData.stream()
                .filter(d -> (d.getLocation().equalsIgnoreCase(location) || d.getContinent().equalsIgnoreCase(location))
                        && !d.getDate().isBefore(dateRange.getStartDate())
                        && !d.getDate().isAfter(dateRange.getEndDate()))
                .toList();

        System.out.println("Data selected: " + filteredData.size() + " records");
    }

    private void chooseSummaryOptions() {
        System.out.println("Choose grouping method:");
        System.out.println("1. No grouping");
        System.out.println("2. Number of groups");
        System.out.println("3. Number of days per group");
        int groupingChoice = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        GroupingStrategy groupingStrategy;
        switch (groupingChoice) {
            case 1:
                groupingStrategy = new Summary.NoGrouping();
                break;
            case 2:
                System.out.print("Enter number of groups: ");
                int numberOfGroups = scanner.nextInt();
                scanner.nextLine(); // Consume newline
                groupingStrategy = new Summary.NumberOfGroups(numberOfGroups);
                break;
            case 3:
                System.out.print("Enter number of days per group: ");
                int daysPerGroup = scanner.nextInt();
                scanner.nextLine(); // Consume newline
                groupingStrategy = new Summary.NumberOfDays(daysPerGroup);
                break;
            default:
                System.out.println("Invalid choice. Using no grouping.");
                groupingStrategy = new Summary.NoGrouping();
        }

        System.out.println("Choose metric:");
        System.out.println("1. Positive cases");
        System.out.println("2. Deaths");
        System.out.println("3. People vaccinated");
        int metricChoice = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        Summary.Metric metric;
        switch (metricChoice) {
            case 1:
                metric = Summary.Metric.POSITIVE_CASES;
                break;
            case 2:
                metric = Summary.Metric.DEATHS;
                break;
            case 3:
                metric = Summary.Metric.PEOPLE_VACCINATED;
                break;
            default:
                System.out.println("Invalid choice. Using positive cases.");
                metric = Summary.Metric.POSITIVE_CASES;
        }

        System.out.println("Choose result type:");
        System.out.println("1. New Total");
        System.out.println("2. Up To");
        int resultTypeChoice = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        Summary.ResultType resultType = (resultTypeChoice == 2) ? Summary.ResultType.UP_TO : Summary.ResultType.NEW_TOTAL;

        System.out.println("Summary options selected.");
    }

    private void displayResults() {
        System.out.println("Choose display method:");
        System.out.println("1. Tabular");
        System.out.println("2. Chart");
        int displayChoice = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        Display display = (displayChoice == 2) ? new ChartDisplay() : new TabularDisplay();

        System.out.println("Results would be displayed here using the selected method.");
    }
}