package Controller;

import Models.CSVLines;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CSVReader {
    private final RecordValidator validator = new RecordValidator();
    private static final String CSV_DELIMITER = ",";

    public List<CSVLines> readCSV(String csvFile) {
        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            // Check the header first
            String headerLine = br.readLine();
            if (headerLine == null || !validator.isValidHeader(headerLine)) {
                throw new RuntimeException("Invalid CSV Header: Must contain exactly 4 columns.");
            }

            // Process the rest of the lines
            return br.lines()
                    .map(line -> new CSVLines(Arrays.asList(line.split(CSV_DELIMITER))))
                    .filter(validator::isValidRecord)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException("Error reading CSV file", e);
        }
    }

}

class RecordValidator {
    private static final int HEADER_COLUMNS = 4;
    private static final int MIN_COLUMNS = 3;
    private static final int MAX_COLUMNS = 4;

    public boolean isValidHeader(String header) {
        String[] columns = header.split(",");
        return columns.length == HEADER_COLUMNS;
    }

    public boolean isValidRecord(CSVLines record) {
        int size = record.getSize();

        if (size < MIN_COLUMNS || size > MAX_COLUMNS) {
            return false;
        }

        if (record.getColumns().get(0).isBlank() || record.getColumns().get(1).isBlank() || record.getColumns().get(2).isBlank()) {
            return false;
        }

        return true;
    }
}
