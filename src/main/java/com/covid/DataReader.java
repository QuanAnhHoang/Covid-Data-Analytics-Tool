import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class DataReader {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("M/d/yyyy");

    public static List<Data> readCSV(String fileName) throws IOException {
        List<Data> dataList = new ArrayList<>();

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
                    dataList.add(data);
                } catch (IllegalArgumentException e) {
                    System.err.println("Error parsing line: " + line + ". " + e.getMessage());
                }
            }
        }

        // Sort the data by country and date
        dataList.sort(Comparator
            .comparing(Data::getLocation)
            .thenComparing(Data::getDate));

        return dataList;
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