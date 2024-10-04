import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class DateRange {
    private LocalDate startDate;
    private LocalDate endDate;

    public DateRange(LocalDate startDate, LocalDate endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public DateRange(LocalDate date, int numberOfDays, boolean isFromDate) {
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