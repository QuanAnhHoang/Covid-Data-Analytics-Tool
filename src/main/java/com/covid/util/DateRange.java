package com.covid.util;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * Represents a date range with a start and end date.  Provides methods for accessing the start and end dates,
 * calculating the number of days in the range, and generating a string representation of the range.
 */
public class DateRange {
    private final LocalDate startDate; // The start date of the range (inclusive)
    private final LocalDate endDate;   // The end date of the range (inclusive)

    /**
     * Constructs a `DateRange` object with the specified start and end dates.
     *
     * @param startDate The start date of the range.
     * @param endDate   The end date of the range.
     * @throws IllegalArgumentException if either date is null or if the end date is before the start date.
     */
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

    /**
     * Constructs a `DateRange` object with a specified date and a number of days, indicating whether the
     * provided date is the start or end date of the range.
     *
     * @param date         The reference date.
     * @param numberOfDays The number of days in the range.
     * @param isFromDate   True if the provided date is the start date, false if it's the end date.
     * @throws IllegalArgumentException if the date is null, the number of days is not positive, or if the calculated end date is before the start date.
     */
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

    /**
     * Returns the start date of the range.
     *
     * @return The start date.
     */
    public LocalDate getStartDate() {
        return startDate;
    }

    /**
     * Returns the end date of the range.
     *
     * @return The end date.
     */
    public LocalDate getEndDate() {
        return endDate;
    }

    /**
     * Calculates the number of days in the date range (inclusive).
     *
     * @return The number of days in the range.
     */
    public long getDays() {
        return ChronoUnit.DAYS.between(startDate, endDate) + 1;
    }

    /**
     * Returns a string representation of the date range.  If the start and end dates are the same,
     * only the start date is returned. Otherwise, the start and end dates are separated by " - ".
     *
     * @return A string representation of the date range.
     */
    @Override
    public String toString() {
        return startDate.equals(endDate)
               ? startDate.toString()
               : startDate + " - " + endDate;
    }
}