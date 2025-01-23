package org.com.controller.dataexport;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.com.controller.dashboard.DashboardModelSingleton;
import org.com.model.domain.Cinematic;
import org.com.model.models.DashboardModel;
import org.com.service.DialogService;
import org.com.service.LogService;

public class DataExportController {

  private final DashboardModel dashboardModel;
  private final DialogService dialogService;
  private final LogService logger = LogService.getInstance();
  @FXML
  private Button exportButton;

  public DataExportController() {
    this.dashboardModel = DashboardModelSingleton.getInstance();
    this.dialogService = new DialogService();
  }

  @FXML
  public void initialize() {
    exportButton.setOnAction(event -> openSaveFileChooser());
  }

  private void openSaveFileChooser() {
    FileChooser fileChooser = createFileChooser();
    Stage stage = (Stage) exportButton.getScene().getWindow();
    File selectedFile = fileChooser.showSaveDialog(stage);

    if (selectedFile != null) {
      exportCinematics(selectedFile);
    }
  }

  private FileChooser createFileChooser() {
    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("Save CSV File");
    fileChooser.getExtensionFilters().add(
        new FileChooser.ExtensionFilter("CSV Files", "*.csv")
    );
    return fileChooser;
  }

  private void exportCinematics(File file) {
    Task<Void> task = createExportTask(file);

    task.setOnFailed(event -> {
      dialogService.showErrorAlert("Export Failed",
          "An error occurred during cinematic export");
      Throwable exception = task.getException();
      if (exception != null) {
        logger.logError("Error during cinematic export");
      } else {
        logger.logError("Error during cinematic export: Unknown error occurred.");
      }
    });

    new Thread(task).start();
  }

  private Task<Void> createExportTask(File file) {
    return new Task<>() {
      @Override
      protected Void call() throws IOException {
        exportToCsv(file);
        return null;
      }
    };
  }

  private void exportToCsv(File file) throws IOException {
    try (FileWriter writer = new FileWriter(file)) {
      writer.write("Title,Type,State,Rating\n");

      for (Cinematic cinematic : dashboardModel.getCinematics()) {
        writer.write(formatCinematicLine(cinematic));
      }
    }
  }

  private String formatCinematicLine(Cinematic cinematic) {
    return String.format("%s,%s,%s,%s%n",
        escapeSpecialCharacters(cinematic.getTitle()),
        cinematic.getType(),
        cinematic.getState(),
        cinematic.getMyRating() != 0 ? cinematic.getMyRating() : ""
    );
  }

  private String escapeSpecialCharacters(String text) {
    if (text == null || text.isEmpty()) {
      return "";
    }

    if (text.contains(",") || text.contains("\"") || text.contains("\n")) {
      text = text.replace("\"", "\"\"");
      return "\"" + text + "\"";
    }
    return text;
  }
}