package org.com.service;

import org.com.models.enums.State;
import org.com.models.enums.Type;

public class ParserUtil {

  private int lineNumber;

  public ParserUtil(int lineNumber) {
    this.lineNumber = lineNumber;
  }

  public ParserUtil() {
  }

  public Type parseTypes(String typeString) {
    return switch (typeString) {
      case "MOVIE" -> Type.MOVIE;
      case "ANIME" -> Type.ANIME;
      case "SERIES" -> Type.SERIES;
      default -> throw new IllegalArgumentException(
          "Invalid Type: " + typeString + " at index: " + lineNumber
              + " - valid are MOVIE, ANIME, SERIES");
    };
  }

  public State parseStates(String stateString) {
    return switch (stateString) {
      case "FINISHED" -> State.FINISHED;
      case "WATCHING" -> State.WATCHING;
      case "DROPPED" -> State.DROPPED;
      case "TOWATCH" -> State.TOWATCH;
      default -> throw new IllegalArgumentException(
          "Invalid State: " + stateString + " at index: " + lineNumber
              + " - valid are FINISHED, WATCHING, DROPPED, TOWATCH");
    };
  }

  public int parseStringToInt(String input) {
    try {
      int number = Integer.parseInt(input);

      if (number < 1 || number > 10) {
        throw new IllegalArgumentException(
            "Invalid input: " + input + " at index: " + lineNumber + " is not between 1 and 10.");
      }

      return number;
    } catch (NumberFormatException e) {
      throw new IllegalArgumentException(
          "Invalid input: " + input + " at index: " + lineNumber + " is not a valid number.");
    }
  }
}