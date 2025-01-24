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
import org.com.controller.dashboard.DashboardModelSingleton;
import org.com.model.domain.Cinematic;

public class ViewLoaderService {

  private final LogService logger;

  public ViewLoaderService(LogService logger) {
    this.logger = logger;
  }

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