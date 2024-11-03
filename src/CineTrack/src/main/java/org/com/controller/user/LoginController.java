package org.com.controller.user;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.com.models.user.User;

public class LoginController {


  @FXML
  private TextField usernameField;
  @FXML
  private PasswordField passwordField;
  @FXML
  private Button loginButton;

  @FXML
  private void handleLogin(ActionEvent event) {
    String username = usernameField.getText();
    String password = passwordField.getText();
    User user = UserController.authenticateUser(username, password);
    if (user != null) {
      // TODO: Implement navigation to main view
    } else {
      showError("Login failed", "Invalid username or password");
    }
  }

  @FXML
  private void switchToRegister(ActionEvent event) {
    try {
      FXMLLoader loader = new FXMLLoader(
          getClass().getResource("/user/RegisterView.fxml"));
      Parent registrationView = loader.load();

      Scene currentScene = usernameField.getScene();
      Stage primaryStage = (Stage) currentScene.getWindow();

      Scene scene = new Scene(registrationView);
      primaryStage.setScene(scene);
      primaryStage.show();
    } catch (Exception e) {
      e.printStackTrace();
      showError("Navigation Error", "Could not switch to registration view");
    }
  }

  private void showError(String title, String content) {
    Alert alert = new Alert(Alert.AlertType.ERROR);
    alert.setTitle(title);
    alert.setHeaderText(null);
    alert.setContentText(content);
    alert.showAndWait();
  }
}