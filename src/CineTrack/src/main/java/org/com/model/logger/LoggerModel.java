package org.com.model.logger;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.com.repository.HibernateUtil;
import org.com.repository.LogRepository;
import org.com.service.SessionManagerService;

public class LoggerModel {

  private ObservableList<Logger> logs = FXCollections.observableArrayList();
  private final LogRepository logRepository;

  public LoggerModel() {
    this.logRepository = new LogRepository(HibernateUtil.getSessionFactory());
    logs.add(new Logger("INFO", "LoggerModel initialized"));
  }

  public ObservableList<Logger> getLogs() {
    return logs;
  }

  public void addLog(String level, String message) {
    Logger newLog = new Logger(level, message);

    Platform.runLater(() -> logs.add(newLog));

    try {
      logRepository.saveLogger(newLog, SessionManagerService.getInstance().getCurrentUser());
    } catch (RuntimeException e) {
      e.printStackTrace();
      Platform.runLater(() -> logs.add(new Logger("ERROR", "Failed to persist log: " + e.getMessage())));
    }
  }
}
