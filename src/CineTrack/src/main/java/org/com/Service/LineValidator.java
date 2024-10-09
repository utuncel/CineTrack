package org.com.Service;

class LineValidator {
    private static final int HEADER_COLUMNS = 4;
    private static final int MIN_COLUMNS = 3;
    private static final int MAX_COLUMNS = 4;

    public boolean isValidHeaderLength(String header) {
        String[] columns = header.split(",");
        return columns.length == HEADER_COLUMNS;
    }

    public boolean isValidHeader(String header) {
        String[] columns = header.split(",");
        return "Title".equalsIgnoreCase(columns[0]) && "Type".equalsIgnoreCase(columns[1]) && "State".equalsIgnoreCase(columns[2]) && "Rating".equalsIgnoreCase(columns[3]);
    }

    public boolean isValidRecord(String[] columns) {
        int size = columns.length;

        if (size < MIN_COLUMNS || size > MAX_COLUMNS) {
            return false;
        }

        if (columns[0].isBlank() || columns[1].isBlank() || columns[2].isBlank()) {
            return false;
        }

        return size != MAX_COLUMNS || !columns[3].isBlank();
    }
}
