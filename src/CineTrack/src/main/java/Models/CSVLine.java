package Models;

import java.util.List;

public class CSVLine {
    private List<String> columns;

    public CSVLine(List<String> columns) {
        this.columns = columns;
    }

    public List<String> getColumns() {
        return columns;
    }

    public int getSize() {
        return columns.size();
    }
}
