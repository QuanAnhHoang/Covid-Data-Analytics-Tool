import java.util.List;

/**
 * The `TabularDisplay` class implements the `Display` interface and provides a tabular representation
 * of summary results.
 */
public class TabularDisplay implements Display {

    /**
     * Displays the provided summary results in a tabular format.
     *
     * @param results The list of `SummaryResult` objects to display. If null or empty, a "No results"
     *                message is printed.
     */
    @Override
    public void show(List<Summary.SummaryResult> results) {
        if (results == null || results.isEmpty()) {
            System.out.println("No results to display.");
            return;
        }

        // Print the header of the table
        System.out.println("Range                 | Value");
        System.out.println("----------------------|-------");

        // Iterate through the results and print each row of the table
        for (Summary.SummaryResult result : results) {
            // Format the date range string to occupy 20 characters, left-aligned
            String range = String.format("%-20s", result.getDateRange().toString());
            // Print the formatted date range and the corresponding value
            System.out.printf("%s | %d%n", range, result.getValue());
        }
    }
}