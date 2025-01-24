package org.com.controller.sidebar;

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
import org.com.model.models.DashboardModel;
import org.com.service.LogService;
import org.com.service.ViewLoaderService;

public class SidebarController {

  private final LogService logger = LogService.getInstance();
  private final ViewLoaderService viewLoaderService = new ViewLoaderService(logger);

  @FXML
  private BorderPane mainContentPane;
  private DashboardModel dashboardModel;

  @FXML
  public void initialize() {
    dashboardModel = DashboardModelSingleton.getInstance();
  }

  @FXML
  public void loadMovieView() {
    loadCinematicView("/cinematics/MovieView.fxml");
  }

  @FXML
  public void loadSeriesView() {
    loadCinematicView("/cinematics/SeriesView.fxml");
  }

  @FXML
  public void loadAnimeView() {
    loadCinematicView("/cinematics/AnimeView.fxml");
  }

  @FXML
  public void loadDataImporterView() {
    viewLoaderService.loadView("/dataimport/DataImporterView.fxml", mainContentPane);
  }

  @FXML
  public void loadDataExportView() {
    viewLoaderService.loadView("/dataexport/DataExportView.fxml", mainContentPane);
  }

  @FXML
  public void loadAddCinematicView() {
    viewLoaderService.loadView("/addcinematic/AddCinematicView.fxml", mainContentPane);
  }

  @FXML
  public void loadLoggerView() {
    try {
      FXMLLoader loader = new FXMLLoader(getClass().getResource("/log/LogView.fxml"));
      Parent view = loader.load();
      updateMainContent(view);
      logger.logInfo("Log View successfully loaded");
    } catch (IOException e) {
      handleLoadError("Log View", e);
    }
  }

  @FXML
  public void loadDashboardView() {
    try {
      FXMLLoader loader = new FXMLLoader(getClass().getResource("/dashboard/DashboardView.fxml"));
      Parent view = loader.load();
      DashboardController controller = loader.getController();
      controller.setDashboardModel(dashboardModel);
      updateMainContent(view);
      logger.logInfo("Dashboard View successfully loaded");
    } catch (IOException e) {
      handleLoadError("Dashboard View", e);
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
      handleLoadError("Media View " + viewPath, e);
    }
  }

  private void updateMainContent(Parent content) {
    Stage stage = (Stage) mainContentPane.getScene().getWindow();
    Scene newScene = new Scene(content);
    stage.setScene(newScene);
  }

  private void handleLoadError(String viewName, Exception e) {
    logger.logError("Error loading " + viewName + ": " + e.getMessage());
  }
}