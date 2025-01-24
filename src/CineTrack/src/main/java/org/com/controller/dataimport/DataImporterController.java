package org.com.controller.dataimport;

import java.io.File;
import java.io.IOException;
import java.util.List;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.com.controller.dashboard.DashboardModelSingleton;
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

public class DataImporterController {

  private final DashboardModel dashboardModel;
  private final CinematicRepository cinematicRepository;
  private final SessionManagerService sessionManager;
  private final DialogService dialogService;
  private final LogService logger = LogService.getInstance();
  @FXML
  private Button chooseFileButton;

  public DataImporterController() {
    this.cinematicRepository = new CinematicRepository(HibernateUtil.getSessionFactory());
    this.dashboardModel = DashboardModelSingleton.getInstance();
    this.sessionManager = SessionManagerService.getInstance();
    this.dialogService = new DialogService();
  }

  @FXML
  public void initialize() {
    chooseFileButton.setOnAction(event -> openFileChooser());
  }

  private void openFileChooser() {
    FileChooser fileChooser = createFileChooser();
    Stage stage = (Stage) chooseFileButton.getScene().getWindow();
    File selectedFile = fileChooser.showOpenDialog(stage);

    if (selectedFile != null) {
      importCinematics(selectedFile);
    }
  }

  private FileChooser createFileChooser() {
    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("Choose CSV File");
    fileChooser.getExtensionFilters().add(
        new FileChooser.ExtensionFilter("CSV Files", "*.csv")
    );
    return fileChooser;
  }

  private void importCinematics(File selectedFile) {
    Task<List<Cinematic>> importTask = createImportTask(selectedFile);

    importTask.setOnSucceeded(event -> {
      List<Cinematic> cinematics = importTask.getValue();
      updateRepositoryAndModel(cinematics);
    });

    importTask.setOnFailed(event -> {
      dialogService.showErrorAlert("Import Failed",
          "An error occurred during cinematic import");
      Throwable exception = importTask.getException();
      if (exception != null) {
        logger.logError("Error during cinematic import");
      } else {
        logger.logError("Error during cinematic import: Unknown error occurred.");
      }
    });

    new Thread(importTask).start();
  }

  private Task<List<Cinematic>> createImportTask(File file) {
    return new Task<>() {
      @Override
      protected List<Cinematic> call() throws IOException {
        return getImportedData(file.getAbsolutePath());
      }
    };
  }

  private void updateRepositoryAndModel(List<Cinematic> cinematics) {
    cinematicRepository.deleteAllCinematicsByUser(sessionManager.getCurrentUser());
    cinematicRepository.saveCinematics(cinematics, sessionManager.getCurrentUser());
    dashboardModel.setCinematics(cinematics);
    sessionManager.getCurrentUser().setCinematics(cinematics);
  }

  private List<Cinematic> getImportedData(String csvFilePath) throws IOException {
    CsvImporterService csvImporterService = new CsvImporterService(csvFilePath);
    ApiService apiService = new ApiService();
    CineFactoryService cineFactoryService = new CineFactoryService(
        csvImporterService,
        apiService,
        sessionManager.getCurrentUser()
    );
    return cineFactoryService.createCinematics();
  }
}