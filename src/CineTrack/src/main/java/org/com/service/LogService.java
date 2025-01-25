package org.com.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.com.model.models.LogModel;

/**
 * Singleton service for logging application events with different severity levels. Manages log
 * creation, storage, and timestamp generation.
 *
 * @author Umut
 * @version 1.0
 */
public class LogService {

  private final LogModel logModel;

  private LogService() {
    this.logModel = new LogModel();
  }

  /**
   * Retrieves the singleton instance of LogService.
   *
   * @return Singleton LogService instance
   */
  public static LogService getInstance() {
    return InstanceHolder.instance;
  }

  /**
   * Retrieves the underlying log model.
   *
   * @return Current LogModel containing log entries
   */
  public LogModel getLoggerModel() {
    return logModel;
  }

  /**
   * Logs an informational message.
   *
   * @param message Information to be logged
   */
  public void logInfo(String message) {
    log("INFO", message);
  }

  /**
   * Logs an error message.
   *
   * @param message Error details to be logged
   */
  public void logError(String message) {
    log("ERROR", message);
  }

  /**
   * Logs a warning message.
   *
   * @param message Warning details to be logged
   */
  public void logWarning(String message) {
    log("WARN", message);
  }

  /**
   * Internal method to log messages with timestamp and severity level.
   *
   * @param level   Log severity level
   * @param message Log message content
   */
  private void log(String level, String message) {
    String timestamp = getTimestamp();
    String formattedMessage = String.format("[%s] %s: %s", timestamp, level, message);

    logModel.addLog(level, formattedMessage);
  }

  /**
   * Generates a formatted timestamp for log entries.
   *
   * @return Current timestamp as a formatted string
   */
  private String getTimestamp() {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    return LocalDateTime.now().format(formatter);
  }

  private static final class InstanceHolder {

    private static final LogService instance = new LogService();
  }
}
