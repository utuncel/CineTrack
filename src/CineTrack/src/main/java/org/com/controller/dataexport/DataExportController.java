package org.com.controller.dataexport;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UncheckedIOException;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.com.controller.dashboard.DashboardModelSingleton;
import org.com.model.domain.Cinematic;
import org.com.model.models.DashboardModel;
import javafx.concurrent.Task;

public class DataExportController {

  @FXML
  private Button exportButton;

  private DashboardModel dashboardModel;

  @FXML
  public void initialize() {
    dashboardModel = DashboardModelSingleton.getInstance();
    exportButton.setOnAction(event -> {
      try {
        openSaveFileChooser();
      } catch (IOException e) {
        throw new UncheckedIOException(e);
      }
    });
  }

  private void openSaveFileChooser() throws IOException {
    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("Save CSV File");
    fileChooser.getExtensionFilters().add(
        new FileChooser.ExtensionFilter("CSV File", "*.csv")
    );

    Stage stage = (Stage) exportButton.getScene().getWindow();
    File selectedFile = fileChooser.showSaveDialog(stage);

    if (selectedFile != null) {
      Task<Void> task = new Task<>() {
        @Override
        protected Void call() throws Exception {
          exportToCsv(selectedFile);
          return null;
        }
      };

      task.setOnFailed(event -> {
        Throwable exception = task.getException();
        exception.printStackTrace();
      });

      new Thread(task).start();
    }
  }

  private void exportToCsv(File file) throws IOException {
    try (FileWriter writer = new FileWriter(file)) {
      writer.write("Title,Type,State,Rating\n");

      for (Cinematic cinematic : dashboardModel.getCinematics()) {
        StringBuilder line = new StringBuilder();
        line.append(escapeSpecialCharacters(cinematic.getTitle())).append(",");
        line.append(cinematic.getType()).append(",");
        line.append(cinematic.getState()).append(",");
        line.append(cinematic.getMyRating() != 0 ? cinematic.getMyRating() : "");
        line.append("\n");

        writer.write(line.toString());
      }
    }
  }

  private String escapeSpecialCharacters(String text) {
    if (text == null) {
      return "";
    }

    if (text.contains(",") || text.contains("\"") || text.contains("\n")) {
      text = text.replace("\"", "\"\"");
      return "\"" + text + "\"";
    }
    return text;
  }
}