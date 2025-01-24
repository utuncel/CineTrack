package org.com.controller.dashboard;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.com.model.domain.Cinematic;
import org.com.model.domain.statistics.ActorRatingsStrategy;
import org.com.model.enums.State;
import org.com.model.enums.Type;
import org.com.service.LogService;

public class ActorRatingStatisticController {

  private final LogService logger = LogService.getInstance();
  @FXML
  private BarChart<String, Number> actorRatingBarChart;
  @FXML
  private TextField minRatingInput;
  @FXML
  private CategoryAxis xAxis;
  private VBox chartContainer;


  public ActorRatingStatisticController() {
  }

  public ActorRatingStatisticController(VBox chartContainer) {
    this.chartContainer = chartContainer;
  }

  public void setChartContainer(VBox chartContainer) {
    this.chartContainer = chartContainer;
  }

  public void addActorRatingStatistic(List<Cinematic> cinematics, List<Type> types,
      List<State> states) throws IOException {
    FXMLLoader loader = new FXMLLoader(
        getClass().getResource("/dashboard/ActorRatingStatisticView.fxml"));
    HBox genreRatingStatistic = loader.load();

    ActorRatingStatisticController controller = loader.getController();
    controller.setChartContainer(this.chartContainer);

    controller.initializeChart(cinematics, types, states);

    if (chartContainer != null) {
      chartContainer.getChildren().add(genreRatingStatistic);
    }
  }

  private void initializeChart(List<Cinematic> cinematics, List<Type> types, List<State> states) {

    updateChart(cinematics, types, states);

    minRatingInput.textProperty().addListener((observable, oldValue, newValue) -> {
      if (!newValue.matches("\\d*")) {
        minRatingInput.setText(newValue.replaceAll("\\D", ""));
      } else {
        updateChart(cinematics, types, states);
      }
    });
  }

  private void updateChart(List<Cinematic> cinematics, List<Type> types, List<State> states) {
    ActorRatingsStrategy actorRatingStrategy = new ActorRatingsStrategy();
    Map<String, List<Double>> actorRatingData = actorRatingStrategy.calculate(cinematics, types,
        states);

    double minRating = 0;
    try {
      minRating =
          minRatingInput.getText().isEmpty() ? 0.0 : Double.parseDouble(minRatingInput.getText());
    } catch (NumberFormatException e) {
      logger.logError("minRating got an error");
    }

    XYChart.Series<String, Number> myRatingSeries = new XYChart.Series<>();
    myRatingSeries.setName("My Rating");

    XYChart.Series<String, Number> imdbRatingSeries = new XYChart.Series<>();
    imdbRatingSeries.setName("IMDb Rating");

    List<String> categories = new ArrayList<>();

    double finalMinRating = minRating;
    actorRatingData.entrySet().stream()
        .filter(entry -> entry.getValue().getFirst() >= finalMinRating
            || entry.getValue().get(1) >= finalMinRating)
        .forEach(entry -> {
          String actor = entry.getKey();
          Double myRating = entry.getValue().get(0);
          Double otherRating = entry.getValue().get(1);

          myRatingSeries.getData().add(new XYChart.Data<>(actor, myRating));
          imdbRatingSeries.getData().add(new XYChart.Data<>(actor, otherRating));
          categories.add(actor);
        });

    xAxis.getCategories().clear();
    xAxis.setCategories(FXCollections.observableArrayList(categories));

    actorRatingBarChart.getData().clear();

    List<XYChart.Series<String, Number>> seriesList = new ArrayList<>();
    seriesList.add(myRatingSeries);
    seriesList.add(imdbRatingSeries);

    actorRatingBarChart.getData().addAll(seriesList);

    actorRatingBarChart.setCategoryGap(10);
    actorRatingBarChart.setBarGap(1);
  }

}
