package org.com.controller.authentication;

import java.util.List;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.com.model.domain.Cinematic;
import org.com.model.domain.User;
import org.com.model.repository.CinematicRepository;
import org.com.model.repository.HibernateUtil;
import org.com.service.DialogService;
import org.com.service.LogService;
import org.com.service.SessionManagerService;
import org.com.service.ViewLoaderService;

/**
 * Controller for handling the login functionality in the application. Provides methods for user
 * authentication and navigation between login and registration views. Handles loading the user's
 * cinematics data upon successful login and navigating to the dashboard view.
 * <p>
 * This class interacts with the {@link AuthenticationController}, {@link CinematicRepository}, and
 * several services such as {@link DialogService}, {@link LogService},
 * {@link SessionManagerService}, and {@link ViewLoaderService}.
 *
 * @author umut
 * @version 1.0
 * @see AuthenticationController
 * @see CinematicRepository
 * @see ViewLoaderService
 */
public class LoginController {

  private final AuthenticationController authenticationController;
  private final CinematicRepository cinematicRepository;
  private final ViewLoaderService viewLoaderService;
  private final DialogService dialogService;
  private final SessionManagerService sessionManager;
  @FXML
  private TextField usernameField;
  @FXML
  private PasswordField passwordField;
  @FXML
  private Button loginButton;

  /**
   * Constructs a {@code LoginController} and initializes required dependencies for authentication,
   * cinematic data access, view loading, and dialog handling.
   */
  public LoginController() {
    this.authenticationController = new AuthenticationController();
    this.cinematicRepository = new CinematicRepository(HibernateUtil.getSessionFactory());
    this.viewLoaderService = new ViewLoaderService(LogService.getInstance());
    this.dialogService = new DialogService();
    this.sessionManager = SessionManagerService.getInstance();
  }

  /**
   * Handles the login action. Authenticates the user and loads the dashboard view with the user's
   * cinematics if the login is successful. Displays an error dialog otherwise.
   */
  @FXML
  private void handleLogin() {
    String username = usernameField.getText();
    String password = passwordField.getText();

    User user = authenticationController.authenticateUser(username, password);
    if (user != null) {
      List<Cinematic> cinematics = cinematicRepository.getAllCinematicsByUser(user);
      prepareDashboardView(cinematics);
    } else {
      dialogService.showErrorAlert("Login failed", "Invalid username or password");
    }
  }

  /**
   * Navigates the user to the registration view. Displays an error dialog if navigation fails.
   */
  @FXML
  private void switchToRegister() {
    try {
      viewLoaderService.loadView("/org/com/view/authentication/RegisterView.fxml",
          (Stage) usernameField.getScene().getWindow());
    } catch (Exception e) {
      dialogService.showErrorAlert("Navigation Error", "Could not switch to registration view");
    }
  }

  /**
   * Prepares and navigates the user to the dashboard view with the provided cinematics. Displays an
   * error dialog if navigation fails.
   *
   * @param cinematics A list of {@link Cinematic} objects associated with the logged-in user.
   */
  private void prepareDashboardView(List<Cinematic> cinematics) {
    try {
      viewLoaderService.loadDashboardView(cinematics, usernameField);
    } catch (Exception e) {
      dialogService.showErrorAlert("Navigation Error", "Could not load DashboardView");
    }
  }
}