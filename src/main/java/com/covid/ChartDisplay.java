import java.util.List;

public class ChartDisplay implements Display {
    private static final int CHART_WIDTH = 80;
    private static final int CHART_HEIGHT = 24;

    @Override
    public void show(List<Summary.SummaryResult> results) {
        if (results == null || results.isEmpty()) {
            System.out.println("No results to display.");
            return;
        }

        int maxValue = results.stream().mapToInt(Summary.SummaryResult::getValue).max().orElse(0);
        if (maxValue == 0) {
            System.out.println("All values are zero. Unable to display meaningful chart.");
            return;
        }

        char[][] chart = new char[CHART_HEIGHT][CHART_WIDTH];
        initializeChart(chart);

        for (int i = 0; i < results.size(); i++) {
            Summary.SummaryResult result = results.get(i);
            int x = (results.size() > 1) 
                ? (int) ((double) i / (results.size() - 1) * (CHART_WIDTH - 2)) + 1 
                : CHART_WIDTH / 2;
            int y = (int) ((double) result.getValue() / maxValue * (CHART_HEIGHT - 2)) + 1;
            y = Math.min(y, CHART_HEIGHT - 1); // Ensure y doesn't exceed chart height
            chart[CHART_HEIGHT - 1 - y][x] = '*';
        }

        printChart(chart);
        printLegend(results);
    }

    private void initializeChart(char[][] chart) {
        for (int i = 0; i < CHART_HEIGHT; i++) {
            for (int j = 0; j < CHART_WIDTH; j++) {
                if (i == CHART_HEIGHT - 1) {
                    chart[i][j] = '_';
                } else if (j == 0) {
                    chart[i][j] = '|';
                } else {
                    chart[i][j] = ' ';
                }
            }
        }
    }

    private void printChart(char[][] chart) {
        for (char[] row : chart) {
            System.out.println(new String(row));
        }
    }

    private void printLegend(List<Summary.SummaryResult> results) {
        System.out.println("\nLegend:");
        for (int i = 0; i < results.size(); i++) {
            Summary.SummaryResult result = results.get(i);
            System.out.printf("%d: %s (%d)%n", i + 1, result.getDateRange(), result.getValue());
        }
    }
}