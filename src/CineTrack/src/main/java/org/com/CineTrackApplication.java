package org.com;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.com.service.LogService;

/**
 * Main application class for CineTrack, extending JavaFX Application. Initializes and launches the
 * application's primary stage with login view.
 *
 * @author Umut
 * @version 1.0
 */
public class CineTrackApplication extends Application {

  private final LogService logger = LogService.getInstance();

  /**
   * Entry point for launching the JavaFX application.
   *
   * @param args Command-line arguments
   */
  public static void main(String[] args) {
    launch();
  }

  /**
   * Initializes and displays the primary application stage.
   *
   * @param stage Primary application stage
   */
  @Override
  public void start(Stage stage) {
    try {
      stage.setScene(createScene());
      stage.show();
    } catch (IOException e) {
      logger.logError(e.getMessage());
    }
  }

  /**
   * Creates the initial scene with the login view.
   *
   * @return Configured Scene with login view
   * @throws IOException If FXML loading fails
   */
  private Scene createScene() throws IOException {
    FXMLLoader loader = new FXMLLoader(getClass().getResource(
        "/org/com/view/authentication/LoginView.fxml"));
    Parent root = loader.load();
    return new Scene(root);
  }
}