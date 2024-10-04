import java.util.ArrayList;
import java.util.List;

public class Summary {
    public enum Metric { POSITIVE_CASES, DEATHS, PEOPLE_VACCINATED }
    public enum ResultType { NEW_TOTAL, UP_TO }

    private final List<List<Data>> groupedData;
    private final Metric metric;
    private final ResultType resultType;

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

    public List<SummaryResult> calculate() {
        List<SummaryResult> results = new ArrayList<>();
        int runningTotal = 0;

        for (List<Data> group : groupedData) {
            int groupTotal = calculateGroupTotal(group);
            runningTotal += groupTotal;

            DateRange dateRange = new DateRange(
                group.get(0).getDate(),
                group.get(group.size() - 1).getDate()
            );

            int value = (resultType == ResultType.NEW_TOTAL) ? groupTotal : runningTotal;
            results.add(new SummaryResult(dateRange, value));
        }

        return results;
    }

    private int calculateGroupTotal(List<Data> group) {
        return group.stream().mapToInt(data -> {
            switch (metric) {
                case POSITIVE_CASES:
                    return data.getNewCases();
                case DEATHS:
                    return data.getNewDeaths();
                case PEOPLE_VACCINATED:
                    return data.getPeopleVaccinated();
                default:
                    throw new IllegalStateException("Unexpected metric: " + metric);
            }
        }).sum();
    }

    public static class SummaryResult {
        private final DateRange dateRange;
        private final int value;

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
                groups.add(new ArrayList<>(data.subList(start, start + groupSize)));
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
            if (data.size() % daysPerGroup != 0) {
                throw new IllegalArgumentException("Data size is not divisible by the number of days per group");
            }

            List<List<Data>> groups = new ArrayList<>();
            for (int i = 0; i < data.size(); i += daysPerGroup) {
                groups.add(new ArrayList<>(data.subList(i, i + daysPerGroup)));
            }

            return groups;
        }
    }
}