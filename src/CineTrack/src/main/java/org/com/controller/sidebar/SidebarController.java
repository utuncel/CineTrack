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
import org.com.model.models.CinematicModelSingleton;
import org.com.model.models.CinematicModel;
import org.com.service.LogService;
import org.com.service.ViewLoaderService;

/**
 * Controller for the application's sidebar navigation. Manages navigation between different views
 * (Dashboard, Movies, Series, Anime, etc.) and controls the display in the main content area.
 *
 * @author umut
 * @version 1.0
 * @see org.com.controller.dashboard.DashboardController
 * @see org.com.controller.cinematics.helper.CinematicController
 */
public class SidebarController {

  private final LogService logger = LogService.getInstance();
  private final ViewLoaderService viewLoaderService = new ViewLoaderService(logger);

  @FXML
  private BorderPane mainContentPane;
  private CinematicModel cinematicModel;

  /**
   * Initializes the controller. Automatically called after FXML loading.
   */
  @FXML
  public void initialize() {
    cinematicModel = CinematicModelSingleton.getInstance();
  }

  /**
   * Loads the movie view into the main content area.
   */
  @FXML
  public void loadMovieView() {
    loadCinematicView("/org/com/view/cinematics/MovieView.fxml");
  }

  /**
   * Loads the series view into the main content area.
   */
  @FXML
  public void loadSeriesView() {
    loadCinematicView("/org/com/view/cinematics/SeriesView.fxml");
  }

  /**
   * Loads the anime view into the main content area.
   */
  @FXML
  public void loadAnimeView() {
    loadCinematicView("/org/com/view/cinematics/AnimeView.fxml");
  }

  /**
   * Loads the data importer view into the main content area.
   */
  @FXML
  public void loadDataImporterView() {
    viewLoaderService.loadView("/org/com/view/dataimport/DataImporterView.fxml", mainContentPane);
  }

  /**
   * Loads the data export view into the main content area.
   */
  @FXML
  public void loadDataExportView() {
    viewLoaderService.loadView("/org/com/view/dataexport/DataExportView.fxml", mainContentPane);
  }

  /**
   * Loads the view for adding new media content.
   */
  @FXML
  public void loadAddCinematicView() {
    viewLoaderService.loadView("/org/com/view/addcinematic/AddCinematicView.fxml", mainContentPane);
  }

  /**
   * Loads the logger view to display system logs. If loading fails, an appropriate error message is
   * logged.
   */
  @FXML
  public void loadLoggerView() {
    try {
      FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/com/view/log/LogView.fxml"));
      Parent view = loader.load();
      updateMainContent(view);
      logger.logInfo("Log View successfully loaded");
    } catch (IOException e) {
      handleLoadError("Log View", e);
    }
  }

  /**
   * Loads the dashboard view and initializes it with the current dashboard model. If loading fails,
   * an appropriate error message is logged.
   */
  @FXML
  public void loadDashboardView() {
    try {
      FXMLLoader loader = new FXMLLoader(getClass().getResource(
          "/org/com/view/dashboard/DashboardView.fxml"));
      Parent view = loader.load();
      DashboardController controller = loader.getController();
      controller.setDashboardModel(cinematicModel);
      updateMainContent(view);
      logger.logInfo("Dashboard View successfully loaded");
    } catch (IOException e) {
      handleLoadError("Dashboard View", e);
    }
  }

  /**
   * Helper method for loading movie, series, or anime views. Loads the corresponding view and
   * initializes its controller with media data.
   *
   * @param viewPath The path to the FXML file of the view to be loaded
   */
  private void loadCinematicView(String viewPath) {
    try {
      FXMLLoader loader = new FXMLLoader(getClass().getResource(viewPath));
      Parent view = loader.load();
      CinematicController controller = loader.getController();
      controller.loadData(cinematicModel.getCinematics());
      updateMainContent(view);
      logger.logInfo("Media View successfully loaded: " + viewPath);
    } catch (IOException e) {
      handleLoadError("Media View " + viewPath, e);
    }
  }

  /**
   * Updates the main content of the application with a new view.
   *
   * @param content The new view to be displayed
   */
  private void updateMainContent(Parent content) {
    Stage stage = (Stage) mainContentPane.getScene().getWindow();
    Scene newScene = new Scene(content);
    stage.setScene(newScene);
  }

  /**
   * Central error handling for view loading failures.
   *
   * @param viewName Name of the view that failed to load
   * @param e        The exception that occurred
   */
  private void handleLoadError(String viewName, Exception e) {
    logger.logError("Error loading " + viewName + ": " + e.getMessage());
  }
}