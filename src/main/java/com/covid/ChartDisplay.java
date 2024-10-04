import java.util.List;

/**
 * The `ChartDisplay` class implements the `Display` interface and provides a chart-based visualization
 * of summary results.
 */
public class ChartDisplay implements Display {

    private static final int CHART_WIDTH = 80;  // Width of the chart
    private static final int CHART_HEIGHT = 24; // Height of the chart

    /**
     * Displays the provided summary results as a chart.
     *
     * @param results The list of `SummaryResult` objects to display.  Handles null or empty lists
     *                and cases where all values are zero.
     */
    @Override
    public void show(List<Summary.SummaryResult> results) {
        if (results == null || results.isEmpty()) {
            System.out.println("No results to display.");
            return;
        }

        // Find the maximum value for scaling the chart
        int maxValue = results.stream().mapToInt(Summary.SummaryResult::getValue).max().orElse(0);
        if (maxValue == 0) {
            System.out.println("All values are zero. Unable to display meaningful chart.");
            return;
        }

        // Create a 2D char array to represent the chart
        char[][] chart = new char[CHART_HEIGHT][CHART_WIDTH];
        initializeChart(chart); // Initialize the chart with borders and empty spaces

        // Plot the data points on the chart
        for (int i = 0; i < results.size(); i++) {
            Summary.SummaryResult result = results.get(i);

            // Calculate x-coordinate (distributed evenly across the chart width)
            int x = (results.size() > 1)
                ? (int) ((double) i / (results.size() - 1) * (CHART_WIDTH - 2)) + 1
                : CHART_WIDTH / 2;

            // Calculate y-coordinate (scaled based on the value and chart height)
            int y = (int) ((double) result.getValue() / maxValue * (CHART_HEIGHT - 2)) + 1;
            y = Math.min(y, CHART_HEIGHT - 1); // Ensure y doesn't exceed chart height

            chart[CHART_HEIGHT - 1 - y][x] = '*'; // Plot the data point
        }

        printChart(chart);     // Print the chart to the console
        printLegend(results); // Print the legend showing date ranges and values
    }


    /**
     * Initializes the chart array with borders and empty spaces.
     * @param chart The 2D char array representing the chart.
     */
    private void initializeChart(char[][] chart) {
        for (int i = 0; i < CHART_HEIGHT; i++) {
            for (int j = 0; j < CHART_WIDTH; j++) {
                if (i == CHART_HEIGHT - 1) {
                    chart[i][j] = '_'; // Bottom border
                } else if (j == 0) {
                    chart[i][j] = '|'; // Left border
                } else {
                    chart[i][j] = ' '; // Empty space
                }
            }
        }
    }


    /**
     * Prints the chart to the console.
     * @param chart The 2D char array representing the chart.
     */
    private void printChart(char[][] chart) {
        for (char[] row : chart) {
            System.out.println(new String(row));
        }
    }

    /**
     * Prints the legend, showing the date range and value for each data point.
     * @param results The list of `SummaryResult` objects.
     */
    private void printLegend(List<Summary.SummaryResult> results) {
        System.out.println("\nLegend:");
        for (int i = 0; i < results.size(); i++) {
            Summary.SummaryResult result = results.get(i);
            System.out.printf("%d: %s (%d)%n", i + 1, result.getDateRange(), result.getValue());
        }
    }
}