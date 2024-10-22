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
import org.com.models.Cinematic;
import org.com.models.enums.State;
import org.com.models.enums.Type;
import org.com.models.statistics.GenreCountStrategy;

public class GenreCountStatisticController {

  @FXML
  private BarChart<String, Number> genreBarChart;

  @FXML
  private TextField minGenreCountInput;

  @FXML
  private CategoryAxis xAxis;

  private VBox chartContainer;

  public GenreCountStatisticController() {
  }

  public GenreCountStatisticController(VBox chartContainer) {
    this.chartContainer = chartContainer;
  }

  public void setChartContainer(VBox chartContainer) {
    this.chartContainer = chartContainer;
  }

  public void addGenreCountStatistic(List<Cinematic> cinematics, List<Type> types,
      List<State> states) throws IOException {
    FXMLLoader loader = new FXMLLoader(
        getClass().getResource("/dashboard/GenreCountStatisticView.fxml"));
    HBox genreCountStatistic = loader.load();

    GenreCountStatisticController controller = loader.getController();
    controller.setChartContainer(this.chartContainer);

    controller.initializeChart(cinematics, types, states);

    if (chartContainer != null) {
      chartContainer.getChildren().add(genreCountStatistic);
    }
  }

  private void initializeChart(List<Cinematic> cinematics, List<Type> types, List<State> states) {
    updateChart(cinematics, types, states);

    minGenreCountInput.textProperty().addListener((observable, oldValue, newValue) -> {
      if (!newValue.matches("\\d*")) {
        minGenreCountInput.setText(newValue.replaceAll("\\D", ""));
      } else {
        updateChart(cinematics, types, states);
      }
    });
  }

  private void updateChart(List<Cinematic> cinematics, List<Type> types, List<State> states) {
    GenreCountStrategy genreCountStrategy = new GenreCountStrategy();
    Map<String, Integer> genreCountData = genreCountStrategy.calculate(cinematics, types, states);

    int minCount = 0;
    try {
      minCount = Integer.parseInt(minGenreCountInput.getText());
    } catch (NumberFormatException e) {
    }

    XYChart.Series<String, Number> series = new XYChart.Series<>();
    List<String> categories = new ArrayList<>();

    int finalMinCount = minCount;
    genreCountData.entrySet().stream()
        .filter(entry -> entry.getValue() >= finalMinCount)
        .forEach(entry -> {
          series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
          categories.add(entry.getKey());
        });

    xAxis.getCategories().clear();
    xAxis.setCategories(FXCollections.observableArrayList(categories));

    genreBarChart.getData().clear();
    genreBarChart.getData().add(series);

    genreBarChart.setCategoryGap(0);
    genreBarChart.setBarGap(0.1);
  }
}