import java.time.LocalDate;

public class Data {
    private final String isoCode;
    private final String continent;
    private final String location;
    private final LocalDate date;
    private final int newCases;
    private final int newDeaths;
    private final int peopleVaccinated;
    private final long population;

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

    // Getters
    public String getIsoCode() { return isoCode; }
    public String getContinent() { return continent; }
    public String getLocation() { return location; }
    public LocalDate getDate() { return date; }
    public int getNewCases() { return newCases; }
    public int getNewDeaths() { return newDeaths; }
    public int getPeopleVaccinated() { return peopleVaccinated; }
    public long getPopulation() { return population; }

    @Override
    public String toString() {
        return String.format("%s,%s,%s,%s,%d,%d,%d,%d", 
            isoCode, continent, location, date, newCases, newDeaths, peopleVaccinated, population);
    }
}