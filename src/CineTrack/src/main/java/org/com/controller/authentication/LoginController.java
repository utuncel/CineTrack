package org.com.controller.authentication;

import java.util.List;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.com.controller.dashboard.DashboardModelSingleton;
import org.com.model.domain.Cinematic;
import org.com.model.domain.User;
import org.com.repository.CinematicRepository;
import org.com.repository.HibernateUtil;
import org.com.service.DialogService;
import org.com.service.LogService;
import org.com.service.SessionManagerService;
import org.com.service.ViewLoaderService;

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

  public LoginController() {
    this.authenticationController = new AuthenticationController();
    this.cinematicRepository = new CinematicRepository(HibernateUtil.getSessionFactory());
    this.viewLoaderService = new ViewLoaderService(LogService.getInstance());
    this.dialogService = new DialogService();
    this.sessionManager = SessionManagerService.getInstance();
  }

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

  @FXML
  private void switchToRegister() {
    try {
      viewLoaderService.loadView("/authentication/RegisterView.fxml",
          (Stage) usernameField.getScene().getWindow());
    } catch (Exception e) {
      dialogService.showErrorAlert("Navigation Error", "Could not switch to registration view");
    }
  }

  private void prepareDashboardView(List<Cinematic> cinematics) {
    try {
      User currentUser = sessionManager.getCurrentUser();
      currentUser.setCinematics(cinematics);
      DashboardModelSingleton.getInstance().setCinematics(cinematics);

      // setDashbordmodell is missing

      viewLoaderService.loadView("/dashboard/DashboardView.fxml",
          (Stage) usernameField.getScene().getWindow());
    } catch (Exception e) {
      dialogService.showErrorAlert("Navigation Error", "Could not load DashboardView");
    }
  }
}