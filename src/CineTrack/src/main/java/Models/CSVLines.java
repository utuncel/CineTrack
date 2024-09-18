package Models;

import java.util.List;

public class CSVLines {
    private List<String> columns;

    public CSVLines(List<String> columns) {
        this.columns = columns;
    }

    public List<String> getColumns() {
        return columns;
    }

    public int getSize() {
        return columns.size();
    }
}
