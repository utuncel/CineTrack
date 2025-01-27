package org.com.service;


import java.io.IOException;
import java.util.List;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.com.controller.dashboard.DashboardController;
import org.com.model.models.DashboardModelSingleton;
import org.com.model.domain.Cinematic;

/**
 * Service for loading and managing JavaFX views and scenes. Handles view transitions and scene
 * setup for different application stages.
 *
 * @author Umut
 * @version 1.0
 */
public class ViewLoaderService {

  private final LogService logger;

  /**
   * Constructs ViewLoaderService with a logging service.
   *
   * @param logger LogService for tracking view loading events
   */
  public ViewLoaderService(LogService logger) {
    this.logger = logger;
  }

  /**
   * Loads a view into a BorderPane and updates the current stage's scene.
   *
   * @param viewPath        Path to the FXML view resource
   * @param mainContentPane BorderPane to be replaced
   */
  public void loadView(String viewPath, BorderPane mainContentPane) {
    try {
      FXMLLoader loader = new FXMLLoader(getClass().getResource(viewPath));
      Parent view = loader.load();
      Stage stage = (Stage) mainContentPane.getScene().getWindow();
      Scene newScene = new Scene(view);
      stage.setScene(newScene);
      logger.logInfo("View successfully loaded: " + viewPath);
    } catch (IOException e) {
      logger.logError("Error loading view " + viewPath + ": " + e.getMessage());
    }
  }

  /**
   * Loads the dashboard view with cinematics and updates the current stage.
   *
   * @param cinematics    List of cinematics to be displayed
   * @param usernameField TextField used to access the current stage
   */
  public void loadDashboardView(List<Cinematic> cinematics, TextField usernameField) {
    try {
      FXMLLoader loader = new FXMLLoader(getClass().getResource("/dashboard/DashboardView.fxml"));
      Parent dashboardView = loader.load();

      DashboardModelSingleton.getInstance().setCinematics(cinematics);
      SessionManagerService.getInstance().getCurrentUser().setCinematics(cinematics);

      DashboardController controller = loader.getController();
      controller.setDashboardModel(DashboardModelSingleton.getInstance());

      Scene scene = new Scene(dashboardView);
      Stage primaryStage = (Stage) usernameField.getScene().getWindow();
      primaryStage.setScene(scene);
      primaryStage.show();
      logger.logInfo("View successfully loaded: " + "/dashboard/DashboardView.fxml");
    } catch (IOException e) {
      logger.logError(
          "Error loading view " + "/dashboard/DashboardView.fxml" + ": " + e.getMessage());
    }
  }

  /**
   * Loads a view into a specified stage.
   *
   * @param viewPath Path to the FXML view resource
   * @param stage    Stage to load the view into
   */
  public void loadView(String viewPath, Stage stage) {
    try {
      FXMLLoader loader = new FXMLLoader(getClass().getResource(viewPath));
      Parent view = loader.load();
      Scene scene = new Scene(view);
      stage.setScene(scene);
      stage.show();
    } catch (IOException e) {
      logger.logError("Error loading view " + viewPath + ": " + e.getMessage());
    }
  }
}