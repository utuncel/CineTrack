package Service;

import Models.CSVLine;

class CSVReaderValidator {
    private static final int HEADER_COLUMNS = 4;
    private static final int MIN_COLUMNS = 3;
    private static final int MAX_COLUMNS = 4;

    public boolean isValidHeader(String header) {
        String[] columns = header.split(",");
        return columns.length == HEADER_COLUMNS;
    }

    public boolean isValidLine(CSVLine line) {
        int size = line.getSize();

        if (size < MIN_COLUMNS || size > MAX_COLUMNS) {
            return false;
        }

        if (line.getColumns().get(0).isBlank() || line.getColumns().get(1).isBlank() || line.getColumns().get(2).isBlank()) {
            return false;
        }

        if(size == MAX_COLUMNS && line.getColumns().get(3).isBlank()){
            return false;
        }

        return true;
    }
}
