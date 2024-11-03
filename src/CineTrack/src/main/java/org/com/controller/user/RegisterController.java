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

public class RegisterController {

  @FXML private TextField usernameField;
  @FXML private PasswordField passwordField;
  @FXML private Button registerButton;

  @FXML
  private void handleRegistration(ActionEvent event) {
    String username = usernameField.getText();
    String password = passwordField.getText();
    try {
      UserController.registerUser(username, password);
      showInfo("Registration Successful", "Please login with your new account");
      switchToLogin(event);
    } catch (IllegalArgumentException e) {
      showError("Registration Error", e.getMessage());
    }
  }

  @FXML
  private void switchToLogin(ActionEvent event) {
    try {
      FXMLLoader loader = new FXMLLoader(getClass().getResource("/user/LoginView.fxml"));
      Parent loginView = loader.load();

      Scene currentScene = usernameField.getScene();
      Stage primaryStage = (Stage) currentScene.getWindow();

      Scene scene = new Scene(loginView);
      primaryStage.setScene(scene);
      primaryStage.show();
    } catch (Exception e) {
      e.printStackTrace();
      showError("Navigation Error", "Could not switch to login view");
    }
  }

  private void showError(String title, String content) {
    Alert alert = new Alert(Alert.AlertType.ERROR);
    alert.setTitle(title);
    alert.setHeaderText(null);
    alert.setContentText(content);
    alert.showAndWait();
  }

  private void showInfo(String title, String content) {
    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    alert.setTitle(title);
    alert.setHeaderText(null);
    alert.setContentText(content);
    alert.showAndWait();
  }
}