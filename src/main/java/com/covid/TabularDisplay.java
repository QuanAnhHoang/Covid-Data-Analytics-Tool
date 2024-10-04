import java.util.List;

public class TabularDisplay implements Display {
    @Override
    public void show(List<Summary.SummaryResult> results) {
        if (results == null || results.isEmpty()) {
            System.out.println("No results to display.");
            return;
        }

        System.out.println("Range                 | Value");
        System.out.println("----------------------|-------");
        
        for (Summary.SummaryResult result : results) {
            String range = String.format("%-20s", result.getDateRange().toString());
            System.out.printf("%s | %d%n", range, result.getValue());
        }
    }
}