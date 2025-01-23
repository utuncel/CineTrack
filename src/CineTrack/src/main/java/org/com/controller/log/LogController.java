package org.com.controller.log;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.com.model.domain.Log;
import org.com.model.models.LogModel;
import org.com.service.LogService;
import org.com.service.SessionManagerService;

public class LogController {

  @FXML
  private TableView<Log> logTableView;
  @FXML
  private TableColumn<Log, String> timestampColumn;
  @FXML
  private TableColumn<Log, String> levelColumn;
  @FXML
  private TableColumn<Log, String> messageColumn;

  @FXML
  public void initialize() {
    initializeColumns();
    loadUserLogs();
  }

  private void initializeColumns() {
    timestampColumn.setCellValueFactory(new PropertyValueFactory<>("timestamp"));
    levelColumn.setCellValueFactory(new PropertyValueFactory<>("level"));
    messageColumn.setCellValueFactory(new PropertyValueFactory<>("message"));
  }

  private void loadUserLogs() {
    LogModel logModel = LogService.getInstance().getLoggerModel();
    if (logModel != null) {
      var logs = SessionManagerService.getInstance().getCurrentUser().getLogs();
      logTableView.setItems(FXCollections.observableArrayList(logs));
    }
  }

}