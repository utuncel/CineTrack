package org.com.service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.com.models.helper.CsvCinematic;

public class CsvImporter {

  private static final String CSV_DELIMITER = ",";
  private final LineValidator validator = new LineValidator();
  private final String filePath;
  private int lineNumber = 2; // 2 because we read the header and first time in the while loop
  private final ParserUtil parser;

  public CsvImporter(String filePath) {
    this.filePath = filePath;
    parser = new ParserUtil(lineNumber);
  }

  public List<CsvCinematic> importData() throws IOException {
    List<CsvCinematic> cinematics = new ArrayList<>();

    try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
      String line;
      String header = br.readLine();

      if (header == null || !validator.isValidHeaderLength(header)) {
        throw new RuntimeException("Invalid CSV Header: Must contain exactly 4 columns.");
      }

      if (!validator.isValidHeader(header)) {
        throw new RuntimeException(
            "Invalid CSV Header: Must has right Header names for his columns.");
      }

      while ((line = br.readLine()) != null) {
        String[] columns = line.split(CSV_DELIMITER);
        cinematics.add(createPersonFromCsvLine(columns));
        lineNumber++;
      }
    }

    return cinematics;
  }

  private CsvCinematic createPersonFromCsvLine(String[] columns) {
    if (!validator.isValidRecord(columns)) {
      throw new RuntimeException("CSV line is Invalid at line:" + lineNumber);
    }

    if (columns.length == 3) {
      return new CsvCinematic.Builder()
          .withTitle(columns[0])
          .withType(parser.parseTypes(columns[1]))
          .withState(parser.parseStates(columns[2]))
          .build();
    }

    return new CsvCinematic.Builder()
        .withTitle(columns[0])
        .withType(parser.parseTypes(columns[1]))
        .withState(parser.parseStates(columns[2]))
        .withRating(parser.parseStringToInt(columns[3]))
        .build();
  }
}

