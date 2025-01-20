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
import org.com.models.Cinematic;
import org.com.models.DashboardModel;
import org.com.repository.CinematicDAO;
import org.com.repository.HibernateUtil;
import org.com.service.ApiService;
import org.com.service.CineFactory;
import org.com.service.CsvImporter;
import org.com.service.SessionManager;
import javafx.concurrent.Task;

public class DataImporterController {

  @FXML
  private Button chooseFileButton;

  private DashboardModel dashboardModel;
  private final CinematicDAO cinematicDAO;

  public DataImporterController(){
    cinematicDAO = new CinematicDAO(HibernateUtil.getSessionFactory());
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
        cinematicDAO.deleteAllCinematicsByUser(SessionManager.getInstance().getCurrentUser().getId());
        cinematicDAO.createCinematics(cinematics);
        dashboardModel.setCinematics(cinematics);
        SessionManager.getInstance().getCurrentUser().setCinematics(cinematics);
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
    CsvImporter csvImporter = new CsvImporter(csvFilePath);
    ApiService apiService = new ApiService();
    CineFactory cineFactory = new CineFactory(csvImporter, apiService, SessionManager.getInstance().getCurrentUser());
    return cineFactory.createCinematics();
  }
}