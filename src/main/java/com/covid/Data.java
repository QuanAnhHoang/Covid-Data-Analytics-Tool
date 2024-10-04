import java.time.LocalDate;

public class Data {
    private String isoCode;
    private String continent;
    private String location;
    private LocalDate date;
    private int newCases;
    private int newDeaths;
    private int peopleVaccinated;
    private long population;

    public Data(String isoCode, String continent, String location, LocalDate date,
                int newCases, int newDeaths, int peopleVaccinated, long population) {
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