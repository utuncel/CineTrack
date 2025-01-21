package org.com.model.models;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.com.model.domain.Log;
import org.com.repository.HibernateUtil;
import org.com.repository.LogRepository;
import org.com.service.SessionManagerService;

public class LogModel {

  private ObservableList<Log> logs = FXCollections.observableArrayList();
  private final LogRepository logRepository;

  public LogModel() {
    this.logRepository = new LogRepository(HibernateUtil.getSessionFactory());
    logs.add(new Log("INFO", "LogModel initialized"));
  }

  public ObservableList<Log> getLogs() {
    return logs;
  }

  public void addLog(String level, String message) {
    Log newLog = new Log(level, message);

    Platform.runLater(() -> logs.add(newLog));

    try {
      logRepository.saveLogger(newLog, SessionManagerService.getInstance().getCurrentUser());
    } catch (RuntimeException e) {
      e.printStackTrace();
      Platform.runLater(() -> logs.add(new Log("ERROR", "Failed to persist log: " + e.getMessage())));
    }
  }
}
