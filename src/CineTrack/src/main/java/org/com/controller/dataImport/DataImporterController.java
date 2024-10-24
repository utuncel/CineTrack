package org.com.controller.dataImport;

import java.io.File;
import java.io.IOException;
import java.util.List;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.com.controller.dashboard.DashboardModelSingleton;
import org.com.models.Cinematic;
import org.com.models.DashboardModel;
import org.com.service.ApiData;
import org.com.service.CineFactory;
import org.com.service.CsvImporter;

public class DataImporterController {

  @FXML
  private Button chooseFileButton;

  private DashboardModel dashboardModel;

  @FXML
  public void initialize() {
    dashboardModel = DashboardModelSingleton.getInstance();
    chooseFileButton.setOnAction(event -> {
      try {
        openFileChooser();
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    });
  }

  private void openFileChooser() throws IOException {
    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("chose file");

    fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV File", "*.csv"));

    Stage stage = (Stage) chooseFileButton.getScene().getWindow();

    File selectedFile = fileChooser.showOpenDialog(stage);
    if (selectedFile != null) {

      dashboardModel.setCinematics(getImportedData(selectedFile.getAbsolutePath()));
    }
  }

  public List<Cinematic> getImportedData(String csvFilePath) throws IOException {
    CsvImporter csvImporter = new CsvImporter(csvFilePath);
    ApiData apiData = new ApiData();
    CineFactory cineFactory = new CineFactory(csvImporter, apiData);
    return cineFactory.createCinematics();
  }
}