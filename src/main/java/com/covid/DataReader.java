import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

public class DataReader {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("M/d/yyyy");

    public static List<Data> readCSV(String fileName) throws IOException {
        Map<String, TreeMap<LocalDate, Data>> dataMap = new HashMap<>();

        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            br.readLine(); // Skip header

            while ((line = br.readLine()) != null) {
                try {
                    String[] values = line.split(",");
                    if (values.length != 8) {
                        System.err.println("Skipping invalid line: " + line);
                        continue;
                    }
                    Data data = parseData(values);
                    dataMap
                        .computeIfAbsent(data.getLocation(), k -> new TreeMap<>())
                        .put(data.getDate(), data);
                } catch (IllegalArgumentException e) {
                    System.err.println("Error parsing line: " + line + ". " + e.getMessage());
                }
            }
        }

        return fillMissingDates(dataMap);
    }

    private static List<Data> fillMissingDates(Map<String, TreeMap<LocalDate, Data>> dataMap) {
        List<Data> filledData = new ArrayList<>();

        for (Map.Entry<String, TreeMap<LocalDate, Data>> entry : dataMap.entrySet()) {
            String location = entry.getKey();
            TreeMap<LocalDate, Data> locationData = entry.getValue();

            if (!locationData.isEmpty()) {
                LocalDate startDate = locationData.firstKey();
                LocalDate endDate = locationData.lastKey();
                LocalDate currentDate = startDate;

                while (!currentDate.isAfter(endDate)) {
                    Data data = locationData.get(currentDate);
                    if (data == null) {
                        // Create a new Data object with zero values for missing dates
                        data = new Data(
                            locationData.firstEntry().getValue().getIsoCode(),
                            locationData.firstEntry().getValue().getContinent(),
                            location,
                            currentDate,
                            0, 0, 0,
                            locationData.firstEntry().getValue().getPopulation()
                        );
                    }
                    filledData.add(data);
                    currentDate = currentDate.plusDays(1);
                }
            }
        }

        filledData.sort(Comparator.comparing(Data::getLocation).thenComparing(Data::getDate));
        return filledData;
    }

    private static Data parseData(String[] values) {
        String isoCode = values[0];
        String continent = values[1];
        String location = values[2];
        LocalDate date;
        try {
            date = LocalDate.parse(values[3], DATE_FORMATTER);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid date format: " + values[3]);
        }
        int newCases = parseIntOrZero(values[4]);
        int newDeaths = parseIntOrZero(values[5]);
        int peopleVaccinated = parseIntOrZero(values[6]);
        long population = parseLongOrZero(values[7]);

        return new Data(isoCode, continent, location, date, newCases, newDeaths, peopleVaccinated, population);
    }

    private static int parseIntOrZero(String value) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private static long parseLongOrZero(String value) {
        try {
            return Long.parseLong(value);
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}