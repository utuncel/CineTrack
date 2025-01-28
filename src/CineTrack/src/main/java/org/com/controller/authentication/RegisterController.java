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

/**
 * Controller class responsible for handling user registration in the application. This class
 * manages the registration process and navigation between the registration and login views.
 *
 * <p>Dependencies include:</p>
 * <ul>
 *   <li>{@link AuthenticationController} for handling authentication logic.</li>
 *   <li>{@link ViewLoaderService} for managing view transitions.</li>
 *   <li>{@link DialogService} for displaying alerts and error messages.</li>
 *   <li>{@link LogService} for logging application events.</li>
 * </ul>
 *
 * @author umut
 * @version 1.0
 */
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

  /**
   * Constructs a {@code RegisterController} and initializes required services for registration,
   * navigation, and logging.
   */
  public RegisterController() {
    this.authenticationController = new AuthenticationController();
    this.viewLoader = new ViewLoaderService(LogService.getInstance());
    this.dialogService = new DialogService();
    this.logger = LogService.getInstance();
  }

  /**
   * Handles the user registration process when the register button is clicked. Validates the input,
   * attempts to register the user, and displays appropriate success or error messages. If
   * registration is successful, navigates to the login view.
   *
   * @param event the {@link ActionEvent} triggered by the register button.
   */
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

  /**
   * Navigates the user to the login view when the login switch button is clicked. Displays an error
   * dialog if the navigation fails.
   *
   * @param event the {@link ActionEvent} triggered by the navigation request.
   */
  @FXML
  private void switchToLogin(ActionEvent event) {
    try {
      viewLoader.loadView("/authentication/LoginView.fxml",
          (Stage) usernameField.getScene().getWindow());
    } catch (Exception e) {
      logger.logError("Navigation error: " + e.getMessage());
      dialogService.showErrorAlert("Navigation Error", "Could not switch to login view");
    }
  }
}