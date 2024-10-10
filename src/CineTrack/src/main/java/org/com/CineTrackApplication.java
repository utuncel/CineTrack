package org.com;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.com.Controller.Dashboard.DashboardController;
import org.com.Models.DashboardModel;
import org.com.Service.ApiData;
import org.com.Service.CineFactory;
import org.com.Service.CsvImporter;


public class CineTrackApplication extends Application {
    private CineFactory cineFactory;
    private CsvImporter csvImporter;

    @Override
    public void start(Stage stage) throws IOException {
        csvImporter = new CsvImporter("C:\\Users\\umut2\\Desktop\\Programmieren\\Projekte\\CineTrack\\src\\CineTrack\\src\\main\\resources\\Test.csv");
        ApiData apiData = new ApiData();
        cineFactory = new CineFactory(csvImporter, apiData);
        var cinematics = cineFactory.createCinematics();

        var loader = new FXMLLoader(getClass().getResource("/DashboardView.fxml"));
        Parent root = loader.load();
        DashboardController dashboardController = loader.getController();

        var dashboardModel = new DashboardModel();
        dashboardController.setDashboardModel(dashboardModel);

        dashboardModel.setCinematics(cinematics);

        stage.setScene(new Scene(root));
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
