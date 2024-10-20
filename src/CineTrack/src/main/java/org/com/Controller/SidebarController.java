package org.com.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.com.Controller.Cinematics.Helper.CinematicController;
import org.com.Controller.Dashboard.DashboardController;
import org.com.Controller.Dashboard.DashboardModelSingleton;
import org.com.Models.DashboardModel;
import org.com.Models.Enums.Type;

import java.io.IOException;

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
        loadCinematicView("/Cinematics/MovieView.fxml", Type.MOVIE);
    }

    @FXML
    public void loadSeriesView(ActionEvent event) {
        loadCinematicView("/Cinematics/SeriesView.fxml", Type.SERIES);
    }

    @FXML
    public void loadAnimeView(ActionEvent event) {
        loadCinematicView("/Cinematics/AnimeView.fxml", Type.ANIME);
    }

    @FXML
    public void loadDashboardView(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Dashboard/dashboardView.fxml"));
            Parent view = loader.load();

            DashboardController controller = loader.getController();
            controller.setDashboardModel(dashboardModel);

            updateMainContent(view);
        } catch (IOException e) {
            handleLoadError(e);
        }
    }

    private void loadCinematicView(String ViewPath,Type type) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(ViewPath));
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