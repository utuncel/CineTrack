package org.com.controller.logger;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.com.models.logger.Logger;
import org.com.models.logger.LoggerModel;
import org.com.service.LoggerService;

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
    this.loggerModel = LoggerService.getInstance().getLoggerModel();

    timestampColumn.setCellValueFactory(new PropertyValueFactory<>("timestamp"));
    levelColumn.setCellValueFactory(new PropertyValueFactory<>("level"));
    messageColumn.setCellValueFactory(new PropertyValueFactory<>("message"));

    if (loggerModel != null) {
      logTableView.setItems(loggerModel.getLogs());
    }
  }

  public void setLoggerModel(LoggerModel model) {
    this.loggerModel = model;
    if (logTableView != null && model != null) {
      logTableView.setItems(model.getLogs());
    }
  }

  public void addLog(String level, String message) {
    if (loggerModel != null) {
      loggerModel.addLog(level, message);
    }
  }
}