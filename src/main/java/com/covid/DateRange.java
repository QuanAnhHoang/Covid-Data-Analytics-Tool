import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class DateRange {
    private final LocalDate startDate;
    private final LocalDate endDate;

    public DateRange(LocalDate startDate, LocalDate endDate) {
        if (startDate == null || endDate == null) {
            throw new IllegalArgumentException("Start date and end date must not be null");
        }
        if (endDate.isBefore(startDate)) {
            throw new IllegalArgumentException("End date cannot be before start date");
        }
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public DateRange(LocalDate date, int numberOfDays, boolean isFromDate) {
        if (date == null || numberOfDays <= 0) {
            throw new IllegalArgumentException("Date must not be null and number of days must be positive");
        }
        if (isFromDate) {
            this.startDate = date;
            this.endDate = date.plusDays(numberOfDays - 1);
        } else {
            this.endDate = date;
            this.startDate = date.minusDays(numberOfDays - 1);
        }
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public long getDays() {
        return ChronoUnit.DAYS.between(startDate, endDate) + 1;
    }

    @Override
    public String toString() {
        return startDate.equals(endDate) 
               ? startDate.toString() 
               : startDate + " - " + endDate;
    }
}