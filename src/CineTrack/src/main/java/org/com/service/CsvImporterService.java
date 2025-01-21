package org.com.service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.com.model.helper.CsvCinematic;

public class CsvImporterService {

  private static final String CSV_DELIMITER = ",";
  private final LineValidatorService validator = new LineValidatorService();
  private final String filePath;
  private final CsvParserService parser;
  private final LogService logger = LogService.getInstance();
  private int lineNumber = 2; // 2 because we read the header and first time in the while loop

  public CsvImporterService(String filePath) {
    this.filePath = filePath;
    this.parser = new CsvParserService(lineNumber);
  }

  public List<CsvCinematic> importData() throws IOException {
    List<CsvCinematic> cinematics = new ArrayList<>();

    try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
      String line;
      String header = br.readLine();

      validateHeader(header);

      while ((line = br.readLine()) != null) {
        String[] columns = line.split(CSV_DELIMITER);
        cinematics.add(createCinematicFromCsvLine(columns));
        lineNumber++;
      }
    } catch (IOException e) {
      logger.logError("Error reading CSV file: " + e.getMessage());
      throw new IOException("Error reading CSV file: " + e.getMessage(), e);
    }

    return cinematics;
  }

  private void validateHeader(String header) {
    if (header == null || !validator.isValidHeaderLength(header)) {
      logger.logError("Invalid CSV Header: Must contain exactly 4 columns.");
    }

    if (header != null && !validator.isValidHeader(header)) {
      logger.logError("Invalid CSV Header: Column names are incorrect.");
    }
  }

  private CsvCinematic createCinematicFromCsvLine(String[] columns) {
    if (!validator.isValidRecord(columns)) {
      logger.logError("CSV line is invalid at line: " + lineNumber);
    }

    if (columns.length == 3) {
      return new CsvCinematic.Builder().withTitle(columns[0])
          .withType(parser.parseTypes(columns[1])).withState(parser.parseStates(columns[2]))
          .build();
    }

    return new CsvCinematic.Builder().withTitle(columns[0]).withType(parser.parseTypes(columns[1]))
        .withState(parser.parseStates(columns[2])).withRating(parser.parseStringToInt(columns[3]))
        .build();
  }
}
