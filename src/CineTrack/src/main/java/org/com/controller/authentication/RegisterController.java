package org.com.controller.authentication;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.com.service.DialogService;
import org.com.service.LogService;
import org.com.service.ViewLoaderService;

public class RegisterController {

  private final AuthenticationController authenticationController;
  private final ViewLoaderService viewLoader;
  private final DialogService dialogService;
  private final LogService logger;
  @FXML
  private TextField usernameField;
  @FXML
  private PasswordField passwordField;
  @FXML
  private Button registerButton;

  public RegisterController() {
    this.authenticationController = new AuthenticationController();
    this.viewLoader = new ViewLoaderService(LogService.getInstance());
    this.dialogService = new DialogService();
    this.logger = LogService.getInstance();
  }

  @FXML
  private void handleRegistration(ActionEvent event) {
    String username = usernameField.getText();
    String password = passwordField.getText();

    try {
      if (authenticationController.registerUser(username, password)) {
        dialogService.showInfoAlert("Registration Successful",
            "Please login with your new account");
        switchToLogin(event);
      } else {
        dialogService.showErrorAlert("Registration Failed", "Username already exists");
      }
    } catch (Exception e) {
      logger.logError("Registration error: " + e.getMessage());
      dialogService.showErrorAlert("Registration Error", "An error occurred during registration");
    }
  }

  @FXML
  private void switchToLogin(ActionEvent event) {
    try {
      viewLoader.loadView("/authentication/LoginView.fxml", (Stage) usernameField.getScene().getWindow());
    } catch (Exception e) {
      logger.logError("Navigation error: " + e.getMessage());
      dialogService.showErrorAlert("Navigation Error", "Could not switch to login view");
    }
  }
}