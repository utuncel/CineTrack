package org.com.controller;

import java.io.IOException;
import java.util.function.Consumer;
import javafx.application.Platform;
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
import org.com.service.LoggerService;

public class SidebarController {

  private final LoggerService logger = LoggerService.getInstance();
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
  public void loadDataImporterView() {
    logger.logInfo("Loading Data Import View");
    loadViewInBackground("/dataimport/DataImporterView.fxml", this::updateMainContent, this::handleLoadError);
  }

  @FXML
  public void loadAddCinematicView() {
    logger.logInfo("Loading Add Media View");
    loadViewInBackground("/addcinematic/AddCinematicView.fxml", this::updateMainContent, this::handleLoadError);
  }

  @FXML
  public void loadLoggerView() {
    logger.logInfo("Loading Logger View");
    loadViewInBackground("/logger/LoggerView.fxml", this::setLoggerView, this::handleLoadError);
  }

  @FXML
  public void loadDashboardView() {
    logger.logInfo("Loading Dashboard View");
    loadViewInBackground("/dashboard/DashboardView.fxml", this::setDashboardView, this::handleLoadError);
  }

  private void loadCinematicView(String viewPath) {
    new Thread(() -> {
      try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(viewPath));
        Parent view = loader.load();

        // Lade die Daten im Hintergrund
        CinematicController controller = loader.getController();
        controller.loadData(dashboardModel.getCinematics());

        // UI im richtigen Thread aktualisieren
        Platform.runLater(() -> {
          updateMainContent(view);
          logger.logInfo("Media View successfully loaded: " + viewPath);
        });
      } catch (IOException e) {
        Platform.runLater(() -> {
          logger.logError("Error loading Media View " + viewPath + ": " + e.getMessage());
          handleLoadError(e);
        });
      }
    }).start();
  }

  private void loadViewInBackground(String viewPath, Consumer<Parent> onSuccess, Consumer<Exception> onError) {
    new Thread(() -> {
      try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(viewPath));
        Parent view = loader.load();

        Platform.runLater(() -> {
          onSuccess.accept(view);
        });
      } catch (IOException e) {
        Platform.runLater(() -> {
          onError.accept(e);
        });
      }
    }).start();
  }

  private void setLoggerView(Parent view) {
    LoggerController loggerController = new FXMLLoader(getClass().getResource("/logger/LoggerView.fxml")).getController();
    loggerController.setLogs();
    updateMainContent(view);
  }

  private void setDashboardView(Parent view) {
    DashboardController controller = new FXMLLoader(getClass().getResource("/dashboard/DashboardView.fxml")).getController();
    controller.setDashboardModel(dashboardModel);
    updateMainContent(view);
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
