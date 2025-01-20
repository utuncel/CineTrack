package org.com.service;

import org.com.models.enums.State;
import org.com.models.enums.Type;

public class CsvParserService {

  private final LogService logger = LogService.getInstance();
  private int lineNumber;

  public CsvParserService(int lineNumber) {
    this.lineNumber = lineNumber;
    logger.logInfo("Initialized MediaEntryParser for line: " + lineNumber);
  }

  public CsvParserService() {
  }

  public Type parseTypes(String typeString) {
    try {
      return Type.valueOf(typeString);
    } catch (IllegalArgumentException e) {
      handleParseError("Type", typeString, "MOVIE, ANIME, SERIES");
      throw createParseException("Type", typeString);
    }
  }

  public State parseStates(String stateString) {
    try {
      return State.valueOf(stateString);
    } catch (IllegalArgumentException e) {
      handleParseError("State", stateString, "FINISHED, WATCHING, DROPPED, TOWATCH");
      throw createParseException("State", stateString);
    }
  }

  public int parseStringToInt(String input) {
    try {
      int rating = Integer.parseInt(input);
      if (rating >= 1 && rating <= 10) {
        return rating;
      }
      handleParseError("Rating", input, "number between 1 and 10");
      throw createParseException("Rating", input);
    } catch (NumberFormatException e) {
      handleParseError("Rating", input, "number between 1 and 10");
      throw createParseException("Rating", input);
    }
  }

  private void handleParseError(String field, String value, String validValues) {
    String errorMessage = String.format("Invalid %s: %s at index: %d - valid are %s",
        field, value, lineNumber, validValues);
    logger.logError(errorMessage);
  }

  private IllegalArgumentException createParseException(String field, String value) {
    return new IllegalArgumentException(
        String.format("Invalid %s: %s at index: %d", field, value, lineNumber)
    );
  }
}