package org.com;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.com.service.LogService;

public class CineTrackApplication extends Application {

  private final LogService logger = LogService.getInstance();

  public static void main(String[] args) {
    launch();
  }

  @Override
  public void start(Stage stage) {
    try {
      stage.setScene(createScene());
      stage.show();
    } catch (IOException e) {
      logger.logError(e.getMessage());
    }
  }

  private Scene createScene() throws IOException {
    FXMLLoader loader = new FXMLLoader(getClass().getResource("/authentication/LoginView.fxml"));
    Parent root = loader.load();
    return new Scene(root);
  }
}