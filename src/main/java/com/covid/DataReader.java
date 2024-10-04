import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

/**
 * The `DataReader` class is responsible for reading and parsing COVID-19 data from a CSV file.
 * It handles file I/O, data validation, and filling in missing dates with default values.
 */
public class DataReader {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("M/d/yyyy"); // Date formatter for parsing dates

    /**
     * Reads COVID-19 data from a CSV file and returns a list of `Data` objects.
     *
     * @param fileName The path to the CSV file.
     * @return A list of `Data` objects representing the data from the CSV file.
     * @throws IOException If an error occurs during file reading or if no valid data is found.
     */
    public static List<Data> readCSV(String fileName) throws IOException {
        // Use a HashMap to store data grouped by location, then a TreeMap within each location to store data sorted by date.
        Map<String, TreeMap<LocalDate, Data>> dataMap = new HashMap<>();


        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) { // Try-with-resources for automatic resource closure
            String line;
            br.readLine(); // Skip the header line

            int lineNumber = 1; // Keep track of the current line number for error reporting
            while ((line = br.readLine()) != null) {
                lineNumber++;
                try {
                    String[] values = line.split(","); // Split the line into values based on commas
                    if (values.length != 8) { // Check for the correct number of fields
                        System.err.println("Skipping invalid line " + lineNumber + ": " + line);
                        continue; // Skip the line if it has an invalid number of fields
                    }
                    Data data = parseData(values); // Parse the values into a Data object
                    // Add the data to the map, creating a new TreeMap if the location is not already present
                    dataMap
                        .computeIfAbsent(data.getLocation(), k -> new TreeMap<>())
                        .put(data.getDate(), data);


                } catch (IllegalArgumentException e) {
                    System.err.println("Error parsing line " + lineNumber + ": " + line + ". " + e.getMessage());
                }
            }
        }


        List<Data> filledData = fillMissingDates(dataMap); // Fill in missing dates with default values

        if (filledData.isEmpty()) {
            throw new IOException("No valid data was read from the CSV file.");
        }

        return filledData;
    }


    /**
     * Fills in missing dates in the data map with default `Data` objects.  This ensures that each location has a continuous date range.
     * @param dataMap The map containing the data read from the CSV, grouped by location and sorted by date within each location.
     * @return A list of `Data` objects with filled-in missing dates.
     */
    private static List<Data> fillMissingDates(Map<String, TreeMap<LocalDate, Data>> dataMap) {
        List<Data> filledData = new ArrayList<>();

        for (Map.Entry<String, TreeMap<LocalDate, Data>> entry : dataMap.entrySet()) {
            String location = entry.getKey();
            TreeMap<LocalDate, Data> locationData = entry.getValue();

            if (!locationData.isEmpty()) {
                LocalDate startDate = locationData.firstKey(); // Get the first date for the location
                LocalDate endDate = locationData.lastKey();   // Get the last date for the location
                LocalDate currentDate = startDate;

                Data firstEntry = locationData.firstEntry().getValue(); // Get the first data entry for location information
                long lastPopulation = firstEntry.getPopulation(); // Track the last known population

                while (!currentDate.isAfter(endDate)) {
                    Data data = locationData.get(currentDate);
                    if (data == null) {
                        // Create a new Data object with zero values for missing dates, using the last known population
                        data = new Data(
                            firstEntry.getIsoCode(),
                            firstEntry.getContinent(),
                            location,
                            currentDate,
                            0, 0, 0,
                            lastPopulation
                        );

                    } else {
                        lastPopulation = data.getPopulation(); // Update last known population
                    }
                    filledData.add(data);
                    currentDate = currentDate.plusDays(1); // Move to the next date
                }
            }
        }

        // Sort the filled data by location and then by date
        filledData.sort(Comparator.comparing(Data::getLocation).thenComparing(Data::getDate));
        return filledData;
    }

    private static Data parseData(String[] values) {
        if (values.length != 8) {
            throw new IllegalArgumentException("Invalid number of fields. Expected 8, got " + values.length);
        }

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
        if (value == null || value.trim().isEmpty()) {
            return 0;
        }
        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private static long parseLongOrZero(String value) {
        if (value == null || value.trim().isEmpty()) {
            return 0;
        }
        try {
            return Long.parseLong(value.trim());
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}