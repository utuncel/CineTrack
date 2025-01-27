package org.com.controller.log;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.com.model.domain.Log;
import org.com.model.models.LogModel;
import org.com.model.repository.HibernateUtil;
import org.com.model.repository.LogRepository;
import org.com.service.LogService;
import org.com.service.SessionManagerService;

/**
 * Controller for the log view that displays system logs in a table format. Manages the display and
 * organization of log entries specific to the current user.
 *
 * @author [Your Name]
 * @version 1.0
 * @see org.com.model.domain.Log
 * @see org.com.model.models.LogModel
 */
public class LogController {

  private final LogRepository logRepository = new LogRepository(
      HibernateUtil.getSessionFactory());
  @FXML
  private TableView<Log> logTableView;
  @FXML
  private TableColumn<Log, String> timestampColumn;
  @FXML
  private TableColumn<Log, String> levelColumn;
  @FXML
  private TableColumn<Log, String> messageColumn;

  /**
   * Initializes the controller and sets up the log table. This method is automatically called after
   * the FXML has been loaded. It initializes table columns and loads the user-specific logs.
   */
  @FXML
  public void initialize() {
    initializeColumns();
    loadUserLogs();
  }

  /**
   * Initializes the table columns by setting up their cell value factories. Maps the column cells
   * to corresponding properties of the Log class.
   */
  private void initializeColumns() {
    timestampColumn.setCellValueFactory(new PropertyValueFactory<>("timestamp"));
    levelColumn.setCellValueFactory(new PropertyValueFactory<>("level"));
    messageColumn.setCellValueFactory(new PropertyValueFactory<>("message"));
  }

  /**
   * Loads and displays log entries specific to the current user. Retrieves logs from the LogModel
   * through the LogService and populates the table view with the user's log entries.
   */
  private void loadUserLogs() {
    LogModel logModel = LogService.getInstance().getLoggerModel();
    if (logModel != null) {
      var logs = logRepository.getLogsByUser(SessionManagerService.getInstance().getCurrentUser());
      logTableView.setItems(FXCollections.observableArrayList(logs));
    }
  }

}