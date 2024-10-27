package org.com.service;

import org.com.models.logger.LoggerModel;

public class LoggerService {

  private static LoggerService instance;
  private final LoggerModel loggerModel;

  private LoggerService() {
    this.loggerModel = new LoggerModel();
  }

  public static synchronized LoggerService getInstance() {
    if (instance == null) {
      instance = new LoggerService();
    }
    return instance;
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
    if (loggerModel != null) {
      loggerModel.addLog(level, message);
    }
  }
}