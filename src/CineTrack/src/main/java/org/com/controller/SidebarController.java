package org.com.controller;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.com.controller.cinematics.helper.CinematicController;
import org.com.controller.dashboard.DashboardController;
import org.com.controller.dashboard.DashboardModelSingleton;
import org.com.controller.logger.LoggerController;
import org.com.models.DashboardModel;
import org.com.service.LogService;

public class SidebarController {

  private final LogService logger = LogService.getInstance();
  @FXML
  private BorderPane mainContentPane;
  private DashboardModel dashboardModel;

  @FXML
  public void initialize() {
    dashboardModel = DashboardModelSingleton.getInstance();
  }

  @FXML
  public void loadMovieView() {
    logger.logInfo("Loading Movie View");
    loadCinematicView("/cinematics/MovieView.fxml");
  }

  @FXML
  public void loadSeriesView() {
    logger.logInfo("Loading Series View");
    loadCinematicView("/cinematics/SeriesView.fxml");
  }

  @FXML
  public void loadAnimeView() {
    logger.logInfo("Loading Anime View");
    loadCinematicView("/cinematics/AnimeView.fxml");
  }

  @FXML
  public void loadDataImporterView() throws IOException {
    logger.logInfo("Loading Data Import View");
    try {
      var loader = new FXMLLoader(getClass().getResource("/dataimport/DataImporterView.fxml"));
      Parent view = loader.load();
      Stage stage = (Stage) mainContentPane.getScene().getWindow();
      Scene newScene = new Scene(view);
      stage.setScene(newScene);
      logger.logInfo("Data Import View successfully loaded");
    } catch (IOException e) {
      logger.logError("Error loading Data Import View: " + e.getMessage());
      throw e;
    }
  }

  @FXML
  public void loadDataExportView() throws IOException {
    logger.logInfo("Loading Data Export View");
    try {
      var loader = new FXMLLoader(getClass().getResource("/dataexport/DataExportView.fxml"));
      Parent view = loader.load();
      Stage stage = (Stage) mainContentPane.getScene().getWindow();
      Scene newScene = new Scene(view);
      stage.setScene(newScene);
      logger.logInfo("Data Export View successfully loaded");
    } catch (IOException e) {
      logger.logError("Error loading Data Export View: " + e.getMessage());
      throw e;
    }
  }

  @FXML
  public void loadAddCinematicView() throws IOException {
    logger.logInfo("Loading Add Media View");
    try {
      var loader = new FXMLLoader(getClass().getResource("/addcinematic/AddCinematicView.fxml"));
      Parent view = loader.load();
      Stage stage = (Stage) mainContentPane.getScene().getWindow();
      Scene newScene = new Scene(view);
      stage.setScene(newScene);
      logger.logInfo("Add Media View successfully loaded");
    } catch (IOException e) {
      logger.logError("Error loading Add Media View: " + e.getMessage());
      throw e;
    }
  }

  @FXML
  public void loadLoggerView() {
    logger.logInfo("Loading Logger View");
    try {
      FXMLLoader loader = new FXMLLoader(getClass().getResource("/logger/LoggerView.fxml"));
      Parent view = loader.load();
      LoggerController loggerController = loader.getController();
      loggerController.setLogs();
      updateMainContent(view);
      logger.logInfo("Logger View successfully loaded");
    } catch (IOException e) {
      logger.logError("Error loading Logger View: " + e.getMessage());
      handleLoadError(e);
    }
  }

  @FXML
  public void loadDashboardView() {
    logger.logInfo("Loading Dashboard View");
    try {
      FXMLLoader loader = new FXMLLoader(getClass().getResource("/dashboard/DashboardView.fxml"));
      Parent view = loader.load();
      DashboardController controller = loader.getController();
      controller.setDashboardModel(dashboardModel);
      updateMainContent(view);
      logger.logInfo("Dashboard View successfully loaded");
    } catch (IOException e) {
      logger.logError("Error loading Dashboard View: " + e.getMessage());
      handleLoadError(e);
    }
  }

  private void loadCinematicView(String viewPath) {
    try {
      FXMLLoader loader = new FXMLLoader(getClass().getResource(viewPath));
      Parent view = loader.load();
      CinematicController controller = loader.getController();
      controller.loadData(dashboardModel.getCinematics());
      updateMainContent(view);
      logger.logInfo("Media View successfully loaded: " + viewPath);
    } catch (IOException e) {
      logger.logError("Error loading Media View " + viewPath + ": " + e.getMessage());
      handleLoadError(e);
    }
  }

  private void updateMainContent(Parent content) {
    Stage stage = (Stage) mainContentPane.getScene().getWindow();
    Scene newScene = new Scene(content);
    stage.setScene(newScene);
  }

  private void handleLoadError(Exception e) {
    logger.logError("Critical error while loading view: " + e.getMessage());
  }
}