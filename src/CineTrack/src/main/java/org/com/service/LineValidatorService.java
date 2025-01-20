package org.com.service;

public class LineValidatorService {

  private static final int HEADER_COLUMNS = 4;
  private static final int MIN_COLUMNS = 3;
  private static final int MAX_COLUMNS = 4;
  private final LogService logger = LogService.getInstance();

  public boolean isValidHeaderLength(String header) {
    String[] columns = header.split(",");
    if (columns.length != HEADER_COLUMNS) {
      logger.logWarning("Invalid CSV header length. Expected " + HEADER_COLUMNS + " columns.");
      return false;
    }
    return true;
  }

  public boolean isValidHeader(String header) {
    String[] columns = header.split(",");
    boolean isValid = "Title".equalsIgnoreCase(columns[0]) && "Type".equalsIgnoreCase(columns[1])
        && "State".equalsIgnoreCase(columns[2]) && "Rating".equalsIgnoreCase(columns[3]);

    if (!isValid) {
      logger.logWarning("Invalid CSV header: " + header);
    }
    return isValid;
  }

  public boolean isValidRecord(String[] columns) {
    int size = columns.length;

    if (size < MIN_COLUMNS || size > MAX_COLUMNS) {
      logger.logWarning(
          "Invalid CSV record at line. Expected between " + MIN_COLUMNS + " and " + MAX_COLUMNS
              + " columns, found " + size + " columns.");
      return false;
    }

    if (columns[0].isBlank() || columns[1].isBlank() || columns[2].isBlank()) {
      logger.logWarning(
          "Invalid CSV record at line: Missing required fields. Title, Type, or State is blank.");
      return false;
    }

    if (size == MAX_COLUMNS && columns[3].isBlank()) {
      logger.logWarning("Invalid CSV record at line: Rating is blank.");
      return false;
    }

    return true;
  }
}
