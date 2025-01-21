package org.com.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.com.model.models.LogModel;

public class LogService {

  private final LogModel logModel;

  private LogService() {
    this.logModel = new LogModel();
  }

  public static LogService getInstance() {
    return InstanceHolder.instance;
  }

  public LogModel getLoggerModel() {
    return logModel;
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

    logModel.addLog(level, formattedMessage);
  }

  private String getTimestamp() {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    return LocalDateTime.now().format(formatter);
  }

  private static final class InstanceHolder {

    private static final LogService instance = new LogService();
  }
}
