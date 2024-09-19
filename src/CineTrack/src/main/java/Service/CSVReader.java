package Service;

import Models.CSVLine;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CSVReader {
    private final CSVReaderValidator validator = new CSVReaderValidator();
    private static final String CSV_DELIMITER = ",";

    public List<CSVLine> readCSV(String csvFile) {
        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            // Check the header first
            String headerLine = br.readLine();
            if (headerLine == null || !validator.isValidHeader(headerLine)) {
                throw new RuntimeException("Invalid CSV Header: Must contain exactly 4 columns.");
            }

            // Process the rest of the lines
            return br.lines()
                    .map(line -> new CSVLine(Arrays.asList(line.split(CSV_DELIMITER))))
                    .filter(validator::isValidLine)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException("Error reading CSV file", e);
        }
    }

}

