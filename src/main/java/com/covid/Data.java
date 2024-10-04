import java.time.LocalDate;

/**
 * Represents a single record of COVID-19 data, containing information about a specific location
 * on a particular date.  Includes data such as new cases, new deaths, and vaccination numbers.
 */
public class Data {
    private final String isoCode;         // ISO code of the location
    private final String continent;        // Continent of the location
    private final String location;         // Name of the location (country, region, etc.)
    private final LocalDate date;          // Date of the data record
    private final int newCases;           // Number of new cases on the given date
    private final int newDeaths;           // Number of new deaths on the given date
    private final int peopleVaccinated;    // Number of people vaccinated on the given date
    private final long population;        // Total population of the location

    /**
     * Constructs a `Data` object.
     *
     * @param isoCode         The ISO code of the location.
     * @param continent        The continent of the location.
     * @param location         The name of the location.
     * @param date          The date of the data record.
     * @param newCases           The number of new cases.
     * @param newDeaths           The number of new deaths.
     * @param peopleVaccinated    The number of people vaccinated.
     * @param population        The total population.
     * @throws IllegalArgumentException if any of the string inputs are null or if any numeric inputs are negative.
     */
    public Data(String isoCode, String continent, String location, LocalDate date,
                int newCases, int newDeaths, int peopleVaccinated, long population) {
        if (isoCode == null || continent == null || location == null || date == null) {
            throw new IllegalArgumentException("ISO code, continent, location, and date must not be null");
        }
        if (newCases < 0 || newDeaths < 0 || peopleVaccinated < 0 || population < 0) {
            throw new IllegalArgumentException("Numeric values must not be negative");
        }
        this.isoCode = isoCode;
        this.continent = continent;
        this.location = location;
        this.date = date;
        this.newCases = newCases;
        this.newDeaths = newDeaths;
        this.peopleVaccinated = peopleVaccinated;
        this.population = population;
    }

    // Getters - Javadoc added below for completeness

    /**
     * @return The ISO code of the location.
     */
    public String getIsoCode() { return isoCode; }

    /**
     * @return The continent of the location.
     */
    public String getContinent() { return continent; }

    /**
     * @return The name of the location.
     */
    public String getLocation() { return location; }

    /**
     * @return The date of the data record.
     */
    public LocalDate getDate() { return date; }

    /**
     * @return The number of new cases.
     */
    public int getNewCases() { return newCases; }

    /**
     * @return The number of new deaths.
     */
    public int getNewDeaths() { return newDeaths; }

    /**
     * @return The number of people vaccinated.
     */
    public int getPeopleVaccinated() { return peopleVaccinated; }

    /**
     * @return The total population.
     */
    public long getPopulation() { return population; }


    /**
     * Returns a comma-separated string representation of the data record.
     *
     * @return A comma-separated string of the data fields.
     */
    @Override
    public String toString() {
        return String.format("%s,%s,%s,%s,%d,%d,%d,%d",
            isoCode, continent, location, date, newCases, newDeaths, peopleVaccinated, population);
    }
}