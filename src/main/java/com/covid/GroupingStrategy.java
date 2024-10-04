import java.util.List;

public interface GroupingStrategy {
    List<List<Data>> group(List<Data> data);
}