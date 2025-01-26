package org.com.model.models;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.com.model.domain.Log;
import org.com.model.repository.HibernateUtil;
import org.com.model.repository.LogRepository;
import org.com.service.SessionManagerService;

/**
 * The `LogModel` class manages the logging functionality for the application.
 *
 * <p>This class provides an observable list of logs for the UI, handles the
 * addition of new logs, and saves logs persistently using a repository. It ensures thread-safe
 * operations for the UI and asynchronous database persistence.</p>
 *
 * <h3>Key Responsibilities:</h3>
 * <ul>
 *   <li>Maintains an observable list of logs for UI binding.</li>
 *   <li>Adds logs both to the UI and the database asynchronously.</li>
 *   <li>Handles errors during log persistence and provides feedback in the logs.</li>
 * </ul>
 */
public class LogModel {

  private final ObservableList<Log> logs = FXCollections.observableArrayList();
  private final LogRepository logRepository;

  /**
   * Constructs a new `LogModel` instance.
   * <p>Initializes the log repository and preloads the observable log list
   * with an initial log entry.</p>
   */
  public LogModel() {
    this.logRepository = new LogRepository(HibernateUtil.getSessionFactory());
    initializeLogs();
  }

  /**
   * Retrieves an unmodifiable observable list of logs.
   *
   * @return An unmodifiable `ObservableList` containing all current logs.
   */
  public ObservableList<Log> getLogs() {
    return FXCollections.unmodifiableObservableList(logs);
  }

  /**
   * Adds a new log entry to the observable list and saves it to the database.
   *
   * @param level   The severity level of the log (e.g., INFO, ERROR).
   * @param message The message associated with the log entry.
   */
  public void addLog(String level, String message) {
    Log newLog = new Log(level, message);

    // Add the log to the UI thread
    Platform.runLater(() -> logs.add(newLog));

    // Save the log to the database asynchronously
    saveLogAsync(newLog);
  }

  /**
   * Initializes the observable log list with a default entry.
   */
  private void initializeLogs() {
    logs.add(new Log("INFO", "LogModel initialized"));
  }

  /**
   * Saves a log entry to the database asynchronously.
   *
   * @param log The `Log` instance to save.
   */
  private void saveLogAsync(Log log) {
    new Thread(() -> {
      try {
        logRepository.saveLogger(log, SessionManagerService.getInstance().getCurrentUser());
      } catch (RuntimeException e) {
        handlePersistenceError(e);
      }
    }).start();
  }

  /**
   * Handles errors encountered during log persistence.
   *
   * <p>Adds an error log entry to the observable list with details about the
   * persistence issue.</p>
   *
   * @param e The exception that occurred during log persistence.
   */
  private void handlePersistenceError(RuntimeException e) {
    String errorMessage = "Failed to persist log: " + e.getMessage();
    Platform.runLater(() -> logs.add(new Log("ERROR", errorMessage)));
  }
}
