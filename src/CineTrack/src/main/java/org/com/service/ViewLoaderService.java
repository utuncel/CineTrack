package org.com.service;

import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

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
}