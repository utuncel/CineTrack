package org.com.controller.logger;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.com.model.domain.Logger;
import org.com.model.models.LoggerModel;
import org.com.service.LogService;
import org.com.service.SessionManagerService;

public class LoggerController {

  @FXML
  private TableView<Logger> logTableView;
  @FXML
  private TableColumn<Logger, String> timestampColumn;
  @FXML
  private TableColumn<Logger, String> levelColumn;
  @FXML
  private TableColumn<Logger, String> messageColumn;

  private LoggerModel loggerModel;

  @FXML
  public void initialize() {
    this.loggerModel = LogService.getInstance().getLoggerModel();

    timestampColumn.setCellValueFactory(new PropertyValueFactory<>("timestamp"));
    levelColumn.setCellValueFactory(new PropertyValueFactory<>("level"));
    messageColumn.setCellValueFactory(new PropertyValueFactory<>("message"));

    if (loggerModel != null) {
      var logs = SessionManagerService.getInstance().getCurrentUser().getLogs();
      logTableView.setItems(
          FXCollections.observableArrayList(logs));
    }
  }
  public void setController(LoggerModel loggerModel){
    this.loggerModel = loggerModel;
  }

  public void setLogs() {
    if (logTableView != null && loggerModel != null) {
      logTableView.setItems(FXCollections.observableArrayList(
          SessionManagerService.getInstance().getCurrentUser().getLogs()));
    }
  }

  public void addLog(String level, String message) {
    if (loggerModel != null) {
      loggerModel.addLog(level, message);
    }
  }
}