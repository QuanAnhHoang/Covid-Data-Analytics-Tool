import java.util.List;

/**
 * The `Display` interface defines a method for displaying summary results.  This allows for
 * different display methods (e.g., tabular, chart) to be used interchangeably.
 */
public interface Display {

    /**
     * Displays the given summary results.  The specific display format depends on the implementation
     * of this interface.
     *
     * @param results The list of `Summary.SummaryResult` objects to be displayed.
     */
    void show(List<Summary.SummaryResult> results);
}