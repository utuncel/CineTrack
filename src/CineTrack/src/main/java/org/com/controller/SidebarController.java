package org.com.controller;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.com.controller.cinematics.helper.CinematicController;
import org.com.controller.dashboard.DashboardController;
import org.com.controller.dashboard.DashboardModelSingleton;
import org.com.models.DashboardModel;

public class SidebarController {

  @FXML
  private BorderPane mainContentPane;

  private DashboardModel dashboardModel;

  @FXML
  public void initialize() {
    dashboardModel = DashboardModelSingleton.getInstance();
  }

  @FXML
  public void loadMovieView(ActionEvent event) {
    loadCinematicView("/cinematics/MovieView.fxml");
  }

  @FXML
  public void loadSeriesView(ActionEvent event) {
    loadCinematicView("/cinematics/SeriesView.fxml");
  }

  @FXML
  public void loadAnimeView(ActionEvent event) {
    loadCinematicView("/cinematics/AnimeView.fxml");
  }

  @FXML
  public void loadDashboardView(ActionEvent event) {
    try {
      FXMLLoader loader = new FXMLLoader(getClass().getResource("/dashboard/dashboardView.fxml"));
      Parent view = loader.load();

      DashboardController controller = loader.getController();
      controller.setDashboardModel(dashboardModel);

      updateMainContent(view);
    } catch (IOException e) {
      handleLoadError(e);
    }
  }

  private void loadCinematicView(String viewPath) {
    try {
      FXMLLoader loader = new FXMLLoader(getClass().getResource(viewPath));
      Parent view = loader.load();

      CinematicController controller = loader.getController();
      controller.loadData(dashboardModel.getCinematics());

      updateMainContent(view);
    } catch (IOException e) {
      handleLoadError(e);
    }
  }

  private void updateMainContent(Parent content) {
    Stage stage = (Stage) mainContentPane.getScene().getWindow();
    Scene newScene = new Scene(content);
    stage.setScene(newScene);
  }


  private void handleLoadError(Exception e) {
    e.printStackTrace();
    System.err.println("Error loading view: " + e.getMessage());
  }
}