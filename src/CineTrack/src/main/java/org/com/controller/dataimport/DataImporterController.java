package org.com.controller.dataimport;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.List;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.com.controller.dashboard.DashboardModelSingleton;
import org.com.model.domain.Cinematic;
import org.com.model.models.DashboardModel;
import org.com.repository.CinematicRepository;
import org.com.repository.HibernateUtil;
import org.com.service.ApiService;
import org.com.service.CineFactoryService;
import org.com.service.CsvImporterService;
import org.com.service.SessionManagerService;
import javafx.concurrent.Task;

public class DataImporterController {

  @FXML
  private Button chooseFileButton;

  private DashboardModel dashboardModel;
  private final CinematicRepository cinematicRepository;

  public DataImporterController(){
    cinematicRepository = new CinematicRepository(HibernateUtil.getSessionFactory());
  }

  @FXML
  public void initialize() {
    dashboardModel = DashboardModelSingleton.getInstance();
    chooseFileButton.setOnAction(event -> {
      try {
        openFileChooser();
      } catch (IOException e) {
        throw new UncheckedIOException(e);
      }
    });
  }

  private void openFileChooser() throws IOException {
    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("Choose file");
    fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV File", "*.csv"));

    Stage stage = (Stage) chooseFileButton.getScene().getWindow();
    File selectedFile = fileChooser.showOpenDialog(stage);

    if (selectedFile != null) {
      Task<List<Cinematic>> task = new Task<>() {
        @Override
        protected List<Cinematic> call() throws Exception {
          return getImportedData(selectedFile.getAbsolutePath());
        }
      };


      task.setOnSucceeded(event -> {
        List<Cinematic> cinematics = task.getValue();
        cinematicRepository.deleteAllCinematicsByUser(SessionManagerService.getInstance().getCurrentUser());
        cinematicRepository.saveCinematics(cinematics,
            SessionManagerService.getInstance().getCurrentUser());
        dashboardModel.setCinematics(cinematics);
        SessionManagerService.getInstance().getCurrentUser().setCinematics(cinematics);
      });

      // Fehlerbehandlung
      task.setOnFailed(event -> {
        Throwable exception = task.getException();
        exception.printStackTrace();
      });

      new Thread(task).start();
    }
  }


  public List<Cinematic> getImportedData(String csvFilePath) throws IOException {
    CsvImporterService csvImporterService = new CsvImporterService(csvFilePath);
    ApiService apiService = new ApiService();
    CineFactoryService cineFactoryService = new CineFactoryService(csvImporterService, apiService, SessionManagerService.getInstance().getCurrentUser());
    return cineFactoryService.createCinematics();
  }
}