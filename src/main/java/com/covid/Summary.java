import java.util.ArrayList;
import java.util.List;

/**
 * The `Summary` class calculates summary statistics for COVID-19 data based on different metrics,
 * result types, and grouping strategies.
 */
public class Summary {

    /**
     * Represents the available metrics for summarizing data (positive cases, deaths, people vaccinated).
     */
    public enum Metric { POSITIVE_CASES, DEATHS, PEOPLE_VACCINATED }

    /**
     * Represents the type of result to calculate (new total for the group or cumulative up to the group).
     */
    public enum ResultType { NEW_TOTAL, UP_TO }

    private final List<List<Data>> groupedData; // Data grouped according to the chosen strategy
    private final Metric metric; // The metric to calculate the summary for
    private final ResultType resultType; // The type of result to calculate

    /**
     * Constructs a `Summary` object.
     *
     * @param data             The list of `Data` objects to summarize.
     * @param groupingStrategy The strategy to group the data.
     * @param metric           The metric to calculate the summary for.
     * @param resultType       The type of result to calculate.
     * @throws IllegalArgumentException if any of the input parameters are invalid.
     */
    public Summary(List<Data> data, GroupingStrategy groupingStrategy, Metric metric, ResultType resultType) {
        if (data == null || data.isEmpty()) {
            throw new IllegalArgumentException("Data list must not be null or empty");
        }
        if (groupingStrategy == null || metric == null || resultType == null) {
            throw new IllegalArgumentException("Grouping strategy, metric, and result type must not be null");
        }
        this.groupedData = groupingStrategy.group(data);
        this.metric = metric;
        this.resultType = resultType;
    }

    /**
     * Calculates the summary results based on the chosen metric, result type, and grouping strategy.
     *
     * @return A list of `SummaryResult` objects, each representing the summary for a group.
     */
    public List<SummaryResult> calculate() {
        List<SummaryResult> results = new ArrayList<>();
        int runningTotal = 0; // Stores the running total for UP_TO result type

        for (List<Data> group : groupedData) {
            if (group.isEmpty()) {
                continue; // Skip empty groups
            }

            int groupTotal = calculateGroupTotal(group); // Calculate the total for the current group
            runningTotal += groupTotal; // Update the running total

            // Create a DateRange object representing the date range of the current group
            DateRange dateRange = new DateRange(
                group.get(0).getDate(),
                group.get(group.size() - 1).getDate()
            );

            // Determine the value to use based on the result type
            int value = (resultType == ResultType.NEW_TOTAL) ? groupTotal : runningTotal;

            // Add a new SummaryResult to the list
            results.add(new SummaryResult(dateRange, value));
        }

        return results;
    }


    /**
     * Calculates the total value for a given group based on the chosen metric.
     *
     * @param group The list of `Data` objects representing a group.
     * @return The total value for the group.
     * @throws IllegalStateException if an unexpected metric is encountered.
     */
    private int calculateGroupTotal(List<Data> group) {
        return group.stream().mapToInt(data -> {
            if (data == null) {
                return 0; // Handle null data points
            }
            switch (metric) {
                case POSITIVE_CASES:
                    return data.getNewCases();
                case DEATHS:
                    return data.getNewDeaths();
                case PEOPLE_VACCINATED:
                    // Calculate the difference in vaccination numbers to get the new vaccinations for the period
                    int currentVaccinated = data.getPeopleVaccinated();
                    int previousVaccinated = group.indexOf(data) > 0 ? group.get(group.indexOf(data) - 1).getPeopleVaccinated() : 0;
                    return currentVaccinated - previousVaccinated;
                default:
                    throw new IllegalStateException("Unexpected metric: " + metric);
            }
        }).sum();
    }


    /**
     * Inner class representing a summary result, containing a date range and a value.
     */
    public static class SummaryResult {
        private final DateRange dateRange; // The date range for the summary result
        private final int value;          // The calculated value for the summary result


        /**
         * Constructs a `SummaryResult` object.
         * @param dateRange The date range for the result.
         * @param value The calculated value.
         */
        public SummaryResult(DateRange dateRange, int value) {
            this.dateRange = dateRange;
            this.value = value;
        }

        public DateRange getDateRange() { return dateRange; }
        public int getValue() { return value; }
    }

    public static class NoGrouping implements GroupingStrategy {
        @Override
        public List<List<Data>> group(List<Data> data) {
            return data.stream().map(d -> List.of(d)).collect(java.util.stream.Collectors.toList());
        }
    }

    public static class NumberOfGroups implements GroupingStrategy {
        private final int numberOfGroups;

        public NumberOfGroups(int numberOfGroups) {
            if (numberOfGroups <= 0) {
                throw new IllegalArgumentException("Number of groups must be positive");
            }
            this.numberOfGroups = numberOfGroups;
        }

        @Override
        public List<List<Data>> group(List<Data> data) {
            List<List<Data>> groups = new ArrayList<>();
            int size = data.size();
            int baseSize = size / numberOfGroups;
            int remainder = size % numberOfGroups;

            int start = 0;
            for (int i = 0; i < numberOfGroups; i++) {
                int groupSize = baseSize + (i < remainder ? 1 : 0);
                groups.add(new ArrayList<>(data.subList(start, Math.min(start + groupSize, size))));
                start += groupSize;
            }

            return groups;
        }
    }

    public static class NumberOfDays implements GroupingStrategy {
        private final int daysPerGroup;

        public NumberOfDays(int daysPerGroup) {
            if (daysPerGroup <= 0) {
                throw new IllegalArgumentException("Days per group must be positive");
            }
            this.daysPerGroup = daysPerGroup;
        }

        @Override
        public List<List<Data>> group(List<Data> data) {
            List<List<Data>> groups = new ArrayList<>();
            for (int i = 0; i < data.size(); i += daysPerGroup) {
                groups.add(new ArrayList<>(data.subList(i, Math.min(i + daysPerGroup, data.size()))));
            }
            return groups;
        }
    }
}