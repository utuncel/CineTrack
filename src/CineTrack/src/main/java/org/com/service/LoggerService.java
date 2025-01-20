package org.com.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.com.models.logger.LoggerModel;

public class LoggerService {

  private final LoggerModel loggerModel;

  private LoggerService() {
    this.loggerModel = new LoggerModel();
  }

  public static LoggerService getInstance() {
    return InstanceHolder.instance;
  }

  public LoggerModel getLoggerModel() {
    return loggerModel;
  }

  public void logInfo(String message) {
    log("INFO", message);
  }

  public void logError(String message) {
    log("ERROR", message);
  }

  public void logWarning(String message) {
    log("WARN", message);
  }

  private void log(String level, String message) {
    String timestamp = getTimestamp();
    String formattedMessage = String.format("[%s] %s: %s", timestamp, level, message);

    loggerModel.addLog(level, formattedMessage);
  }

  private String getTimestamp() {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    return LocalDateTime.now().format(formatter);
  }

  private static final class InstanceHolder {

    private static final LoggerService instance = new LoggerService();
  }
}
