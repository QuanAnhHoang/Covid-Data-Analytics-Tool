package com.covid.summary;

import com.covid.data.Data;

import java.util.List;

/**
 * The `GroupingStrategy` interface defines a strategy for grouping a list of `Data` objects
 * into sublists.  This allows for different grouping methods (e.g., no grouping, by number of groups,
 * by number of days) to be used interchangeably.
 */
public interface GroupingStrategy {

    /**
     * Groups the input data list according to the specific implementation of the strategy.
     *
     * @param data The list of `Data` objects to be grouped.
     * @return A list of lists, where each inner list represents a group of `Data` objects.
     */
    List<List<Data>> group(List<Data> data);
}