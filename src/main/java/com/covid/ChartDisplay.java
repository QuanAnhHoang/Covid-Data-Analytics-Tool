import java.util.List;

public class ChartDisplay implements Display {
    private static final int CHART_WIDTH = 80;
    private static final int CHART_HEIGHT = 24;

    @Override
    public void show(List<Summary.SummaryResult> results) {
        int maxValue = results.stream().mapToInt(Summary.SummaryResult::getValue).max().orElse(0);
        char[][] chart = new char[CHART_HEIGHT][CHART_WIDTH];
        initializeChart(chart);

        for (int i = 0; i < results.size(); i++) {
            Summary.SummaryResult result = results.get(i);
            int x = (int) ((double) i / (results.size() - 1) * (CHART_WIDTH - 2)) + 1;
            int y = (int) ((double) result.getValue() / maxValue * (CHART_HEIGHT - 2)) + 1;
            chart[CHART_HEIGHT - 1 - y][x] = '*';
        }

        printChart(chart);
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
}