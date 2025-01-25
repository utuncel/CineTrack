package org.com.service;

import org.com.model.enums.State;
import org.com.model.enums.Type;

/**
 * Service for parsing and validating CSV data types, states, and ratings. Provides type conversion
 * and validation for cinematic entries.
 *
 * @author Umut
 * @version 1.0
 */
public class CsvParserService {

  private final LogService logger = LogService.getInstance();
  private int lineNumber;

  /**
   * Constructs a CsvParserService with specific line number for error tracking.
   *
   * @param lineNumber Current line number being processed
   */
  public CsvParserService(int lineNumber) {
    this.lineNumber = lineNumber;
    logger.logInfo("Initialized MediaEntryParser for line: " + lineNumber);
  }

  /**
   * Default constructor for CsvParserService.
   */
  public CsvParserService() {
  }

  /**
   * Parses the cinematic type from a string.
   *
   * @param typeString String representation of the cinematic type
   * @return Parsed Type enum
   * @throws IllegalArgumentException If type is invalid
   */
  public Type parseTypes(String typeString) {
    try {
      return Type.valueOf(typeString);
    } catch (IllegalArgumentException e) {
      handleParseError("Type", typeString, "MOVIE, ANIME, SERIES");
      throw createParseException("Type", typeString);
    }
  }

  /**
   * Parses the cinematic state from a string.
   *
   * @param stateString String representation of the cinematic state
   * @return Parsed State enum
   * @throws IllegalArgumentException If state is invalid
   */
  public State parseStates(String stateString) {
    try {
      return State.valueOf(stateString);
    } catch (IllegalArgumentException e) {
      handleParseError("State", stateString, "FINISHED, WATCHING, DROPPED, TOWATCH");
      throw createParseException("State", stateString);
    }
  }

  /**
   * Parses a rating from a string.
   *
   * @param input String representation of the rating
   * @return Parsed integer rating
   * @throws IllegalArgumentException If rating is invalid
   */
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

  /**
   * Handles logging of parsing errors for different fields.
   *
   * @param field       The field being parsed (e.g., "Type", "State", "Rating")
   * @param value       The invalid value that caused the parsing error
   * @param validValues A string listing valid input values
   */
  private void handleParseError(String field, String value, String validValues) {
    String errorMessage = String.format("Invalid %s: %s at index: %d - valid are %s",
        field, value, lineNumber, validValues);
    logger.logError(errorMessage);
  }

  /**
   * Creates a standardized IllegalArgumentException for parsing errors.
   *
   * @param field The field that failed parsing
   * @param value The invalid value that caused the parsing error
   * @return Configured IllegalArgumentException with detailed error message
   */
  private IllegalArgumentException createParseException(String field, String value) {
    return new IllegalArgumentException(
        String.format("Invalid %s: %s at index: %d", field, value, lineNumber)
    );
  }
}