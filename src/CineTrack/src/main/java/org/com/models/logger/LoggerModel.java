package org.com.models.logger;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class LoggerModel {

  private final ObservableList<Logger> logs = FXCollections.observableArrayList();

  public LoggerModel() {
    logs.add(new Logger("INFO", "LoggerModel initialized"));
  }

  public ObservableList<Logger> getLogs() {
    return logs;
  }

  public void addLog(String level, String message) {
    Platform.runLater(() -> logs.add(new Logger(level, message)));
  }
}