package org.com.controller.dataexport;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.com.model.domain.Cinematic;
import org.com.model.models.CinematicModel;
import org.com.model.models.CinematicModelSingleton;
import org.com.service.DialogService;
import org.com.service.LogService;

/**
 * Controller for handling data export operations in the application. Manages the export of
 * cinematic data to CSV files, handles file selection, and performs asynchronous export operations
 * with proper CSV formatting.
 *
 * @author umut
 * @version 1.0
 * @see org.com.model.domain.Cinematic
 * @see CinematicModel
 */
public class DataExportController {

  private final CinematicModel cinematicModel;
  private final DialogService dialogService;
  private final LogService logger = LogService.getInstance();
  @FXML
  private Button exportButton;

  /**
   * Constructs a new DataExportController. Initializes the dashboard model and dialog service.
   */
  public DataExportController() {
    this.cinematicModel = CinematicModelSingleton.getInstance();
    this.dialogService = new DialogService();
  }

  /**
   * Initializes the controller after FXML loading. Sets up event handler for the export button.
   */
  @FXML
  public void initialize() {
    exportButton.setOnAction(event -> openSaveFileChooser());
  }

  /**
   * Opens a file chooser dialog for selecting the export file location. Triggers the export process
   * if a file location is selected.
   */
  private void openSaveFileChooser() {
    FileChooser fileChooser = createFileChooser();
    Stage stage = (Stage) exportButton.getScene().getWindow();
    File selectedFile = fileChooser.showSaveDialog(stage);

    if (selectedFile != null) {
      exportCinematics(selectedFile);
    }
  }

  /**
   * Creates and configures a FileChooser for CSV files.
   *
   * @return Configured FileChooser instance
   */
  private FileChooser createFileChooser() {
    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("Save CSV File");
    fileChooser.getExtensionFilters().add(
        new FileChooser.ExtensionFilter("CSV Files", "*.csv")
    );
    return fileChooser;
  }

  /**
   * Initiates the asynchronous export process for cinematics. Creates and starts a background task
   * for exporting data.
   *
   * @param file The file to export to
   */
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

  /**
   * Creates a background task for exporting cinematic data.
   *
   * @param file The target file for export
   * @return Task for asynchronous data export
   */
  private Task<Void> createExportTask(File file) {
    return new Task<>() {
      @Override
      protected Void call() throws IOException {
        exportToCsv(file);
        return null;
      }
    };
  }

  /**
   * Exports cinematic data to a CSV file. Writes header and formatted cinematic data lines to the
   * file.
   *
   * @param file The target file for export
   * @throws IOException If there's an error writing to the file
   */
  private void exportToCsv(File file) throws IOException {
    try (FileWriter writer = new FileWriter(file)) {
      writer.write("Title,Type,State,Rating\n");

      for (Cinematic cinematic : cinematicModel.getCinematics()) {
        writer.write(formatCinematicLine(cinematic));
      }
    }
  }

  /**
   * Formats a single cinematic entry as a CSV line. Handles proper formatting of fields including
   * escaping special characters.
   *
   * @param cinematic The cinematic object to format
   * @return Formatted CSV line for the cinematic
   */
  private String formatCinematicLine(Cinematic cinematic) {
    return String.format("%s,%s,%s,%s%n",
        escapeSpecialCharacters(cinematic.getTitle()),
        cinematic.getType(),
        cinematic.getState(),
        cinematic.getMyRating() != 0 ? cinematic.getMyRating() : ""
    );
  }

  /**
   * Escapes special characters in text for CSV format. Handles commas, quotes, and newlines
   * according to CSV specification.
   *
   * @param text The text to escape
   * @return Escaped text safe for CSV format
   */
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