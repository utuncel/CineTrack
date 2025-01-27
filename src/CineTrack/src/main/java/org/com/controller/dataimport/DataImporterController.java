package org.com.controller.dataimport;

import java.io.File;
import java.io.IOException;
import java.util.List;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.com.model.models.DashboardModelSingleton;
import org.com.model.domain.Cinematic;
import org.com.model.models.DashboardModel;
import org.com.model.repository.CinematicRepository;
import org.com.model.repository.HibernateUtil;
import org.com.service.ApiService;
import org.com.service.CineFactoryService;
import org.com.service.CsvImporterService;
import org.com.service.DialogService;
import org.com.service.LogService;
import org.com.service.SessionManagerService;

/**
 * Controller for handling data import operations in the application. Manages the import of
 * cinematic data from CSV files, coordinates with various services for data processing, and updates
 * the application state accordingly.
 *
 * @author umut
 * @version 1.0
 * @see org.com.service.CsvImporterService
 * @see org.com.service.ApiService
 * @see org.com.service.CineFactoryService
 */
public class DataImporterController {

  private final DashboardModel dashboardModel;
  private final CinematicRepository cinematicRepository;
  private final SessionManagerService sessionManager;
  private final DialogService dialogService;
  private final LogService logger = LogService.getInstance();
  @FXML
  private Button chooseFileButton;

  /**
   * Constructs a new DataImporterController. Initializes required services and repositories for
   * data import operations.
   */

  public DataImporterController() {
    this.cinematicRepository = new CinematicRepository(HibernateUtil.getSessionFactory());
    this.dashboardModel = DashboardModelSingleton.getInstance();
    this.sessionManager = SessionManagerService.getInstance();
    this.dialogService = new DialogService();
  }

  /**
   * Initializes the controller after FXML loading. Sets up event handlers for the file chooser
   * button.
   */
  @FXML
  public void initialize() {
    chooseFileButton.setOnAction(event -> openFileChooser());
  }

  /**
   * Opens a file chooser dialog for CSV file selection. Triggers the import process if a file is
   * selected.
   */
  private void openFileChooser() {
    FileChooser fileChooser = createFileChooser();
    Stage stage = (Stage) chooseFileButton.getScene().getWindow();
    File selectedFile = fileChooser.showOpenDialog(stage);

    if (selectedFile != null) {
      importCinematics(selectedFile);
    }
  }

  /**
   * Creates and configures a FileChooser for CSV files.
   *
   * @return Configured FileChooser instance
   */
  private FileChooser createFileChooser() {
    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("Choose CSV File");
    fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
    return fileChooser;
  }

  /**
   * Initiates the asynchronous import process for cinematics. Creates and starts a background task
   * for importing data.
   *
   * @param selectedFile The CSV file to import
   */
  private void importCinematics(File selectedFile) {
    Task<List<Cinematic>> importTask = createImportTask(selectedFile);

    importTask.setOnSucceeded(event -> {
      List<Cinematic> cinematics = importTask.getValue();
      updateRepositoryAndModel(cinematics);
    });

    importTask.setOnFailed(event -> {
      dialogService.showErrorAlert("Import Failed", "An error occurred during cinematic import");
      Throwable exception = importTask.getException();
      if (exception != null) {
        logger.logError("Error during cinematic import");
      } else {
        logger.logError("Error during cinematic import: Unknown error occurred.");
      }
    });

    new Thread(importTask).start();
  }

  /**
   * Creates a background task for importing cinematic data.
   *
   * @param file The file to import
   * @return Task for asynchronous data import
   */
  private Task<List<Cinematic>> createImportTask(File file) {
    return new Task<>() {
      @Override
      protected List<Cinematic> call() throws IOException {
        return getImportedData(file.getAbsolutePath());
      }
    };
  }

  /**
   * Updates the repository and model with imported cinematic data. Deletes existing cinematics for
   * the current user and saves the new ones.
   *
   * @param cinematics List of imported cinematics
   */
  private void updateRepositoryAndModel(List<Cinematic> cinematics) {
    cinematicRepository.deleteAllCinematicsByUser(sessionManager.getCurrentUser());
    cinematicRepository.saveCinematics(cinematics, sessionManager.getCurrentUser());
    dashboardModel.setCinematics(cinematics);
    sessionManager.getCurrentUser().setCinematics(cinematics);
  }

  /**
   * Processes the imported CSV file and creates cinematic objects. Coordinates between CSV import,
   * API service, and cinematic factory service.
   *
   * @param csvFilePath Path to the CSV file
   * @return List of created Cinematic objects
   * @throws IOException If there's an error reading the CSV file
   */
  private List<Cinematic> getImportedData(String csvFilePath) throws IOException {
    CsvImporterService csvImporterService = new CsvImporterService(csvFilePath);
    ApiService apiService = new ApiService();
    CineFactoryService cineFactoryService = new CineFactoryService(csvImporterService, apiService,
        sessionManager.getCurrentUser());
    return cineFactoryService.createCinematics();
  }
}