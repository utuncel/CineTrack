package org.com.model.models;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.com.model.domain.Log;
import org.com.model.repository.HibernateUtil;
import org.com.model.repository.LogRepository;
import org.com.service.SessionManagerService;

public class LogModel {

  private final ObservableList<Log> logs = FXCollections.observableArrayList();
  private final LogRepository logRepository;

  public LogModel() {
    this.logRepository = new LogRepository(HibernateUtil.getSessionFactory());
    initializeLogs();
  }

  public ObservableList<Log> getLogs() {
    return FXCollections.unmodifiableObservableList(logs);
  }

  public void addLog(String level, String message) {
    Log newLog = new Log(level, message);

    // Add the log to the UI thread
    Platform.runLater(() -> logs.add(newLog));

    // Save the log to the database asynchronously
    saveLogAsync(newLog);
  }

  private void initializeLogs() {
    logs.add(new Log("INFO", "LogModel initialized"));
  }

  private void saveLogAsync(Log log) {
    new Thread(() -> {
      try {
        logRepository.saveLogger(log, SessionManagerService.getInstance().getCurrentUser());
      } catch (RuntimeException e) {
        handlePersistenceError(e);
      }
    }).start();
  }

  private void handlePersistenceError(RuntimeException e) {
    String errorMessage = "Failed to persist log: " + e.getMessage();
    Platform.runLater(() -> logs.add(new Log("ERROR", errorMessage)));
  }
}
