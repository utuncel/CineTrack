package org.com.controller.user;

import java.io.IOException;
import java.util.List;
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
import org.com.controller.dashboard.DashboardController;
import org.com.controller.dashboard.DashboardModelSingleton;
import org.com.model.Cinematic;
import org.com.model.user.User;
import org.com.repository.CinematicRepository;
import org.com.repository.HibernateUtil;
import org.com.service.SessionManagerService;

public class LoginController {

  @FXML
  private TextField usernameField;
  @FXML
  private PasswordField passwordField;
  @FXML
  private Button loginButton;
  private final UserController userController;
  private final CinematicRepository cinematicRepository;

  public LoginController() {
    this.userController = new UserController();
    this.cinematicRepository = new CinematicRepository(HibernateUtil.getSessionFactory());
  }

  @FXML
  private void handleLogin(ActionEvent event) throws IOException {
    String username = usernameField.getText();
    String password = passwordField.getText();
    User user = userController.authenticateUser(username, password);
    if (user != null) {

      switchToDashboardView(cinematicRepository.getAllCinematicsByUser(
          SessionManagerService.getInstance().getCurrentUser()));
    } else {
      showError("Login failed", "Invalid username or password");
    }
  }

  @FXML
  private void switchToRegister(ActionEvent event) {
    try {
      FXMLLoader loader = new FXMLLoader(
          getClass().getResource("/user/RegisterView.fxml"));
      loader.setControllerFactory(param -> new RegisterController());
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

  private void switchToDashboardView(List<Cinematic> cinematics) {
    try {
      FXMLLoader loader = new FXMLLoader(getClass().getResource("/dashboard/DashboardView.fxml"));
      Parent dashboardView = loader.load();

      DashboardModelSingleton.getInstance().setCinematics(cinematics);
      SessionManagerService.getInstance().getCurrentUser().setCinematics(cinematics);

      DashboardController controller = loader.getController();
      controller.setDashboardModel(DashboardModelSingleton.getInstance());


      Scene scene = new Scene(dashboardView);
      Stage primaryStage = (Stage) usernameField.getScene().getWindow();
      primaryStage.setScene(scene);
      primaryStage.show();
    } catch (IOException e) {
      e.printStackTrace();
      showError("Navigation Error", "Could not load DashboardView.fxml.");
    }
  }

}
