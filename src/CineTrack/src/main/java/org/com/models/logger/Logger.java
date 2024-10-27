package org.com.models.logger;

import java.time.LocalDateTime;

public class Logger {

  private final LocalDateTime timestamp;
  private final String level;
  private final String message;

  public Logger(String level, String message) {
    this.timestamp = LocalDateTime.now();
    this.level = level;
    this.message = message;
  }

  public LocalDateTime getTimestamp() {
    return timestamp;
  }

  public String getLevel() {
    return level;
  }

  public String getMessage() {
    return message;
  }
}