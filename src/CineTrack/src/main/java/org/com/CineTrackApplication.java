package org.com;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class CineTrackApplication extends Application {

  public static void main(String[] args) {
    launch();
  }

  @Override
  public void start(Stage stage) {
    try {
      FXMLLoader loader = new FXMLLoader(getClass().getResource("/user/LoginView.fxml"));
      Parent root = loader.load();
      stage.setScene(new Scene(root));

      stage.show();
    } catch (IOException e) {
      e.printStackTrace();

    }
  }

}
