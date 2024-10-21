package org.com;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.com.controller.dashboard.DashboardController;
import org.com.controller.dashboard.DashboardModelSingleton;
import org.com.models.DashboardModel;
import org.com.service.ApiData;
import org.com.service.CineFactory;
import org.com.service.CsvImporter;


public class CineTrackApplication extends Application {

  private CineFactory cineFactory;
  private CsvImporter csvImporter;

  public static void main(String[] args) {
    launch();
  }

  @Override
  public void start(Stage stage) throws IOException {
    csvImporter = new CsvImporter(
        "C:\\Users\\umut2\\Desktop\\Programmieren\\Projekte\\CineTrack\\src\\CineTrack\\src\\main\\resources\\Test.csv");
    ApiData apiData = new ApiData();
    cineFactory = new CineFactory(csvImporter, apiData);
    var cinematics = cineFactory.createCinematics();

    DashboardModel dashboardModel = DashboardModelSingleton.getInstance();
    dashboardModel.setCinematics(cinematics);

    var loader = new FXMLLoader(getClass().getResource("/Dashboard/DashboardView.fxml"));
    Parent root = loader.load();
    DashboardController dashboardController = loader.getController();
    dashboardController.setDashboardModel(dashboardModel);

    stage.setScene(new Scene(root));
    stage.show();
  }
}
