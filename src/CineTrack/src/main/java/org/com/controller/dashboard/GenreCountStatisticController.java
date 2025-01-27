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
import org.com.model.domain.statistics.GenreCountStrategy;
import org.com.model.enums.State;
import org.com.model.enums.Type;
import org.com.service.LogService;

/**
 * Controller for managing and displaying genre count statistics in the dashboard. This class
 * initializes and updates a bar chart visualization for genre-based data, allowing filtering by
 * type, state, and minimum count thresholds.
 *
 * @author umut
 * @version 1.0
 * @see org.com.model.domain.statistics.GenreCountStrategy
 */
public class GenreCountStatisticController {

  private final LogService logger = LogService.getInstance();
  @FXML
  private BarChart<String, Number> genreBarChart;
  @FXML
  private TextField minGenreCountInput;
  @FXML
  private CategoryAxis xAxis;
  private VBox chartContainer;

  /**
   * Default constructor for FXML initialization.
   */
  public GenreCountStatisticController() {
  }

  /**
   * Constructor with chart container initialization.
   *
   * @param chartContainer The VBox container for the statistics chart
   */
  public GenreCountStatisticController(VBox chartContainer) {
    this.chartContainer = chartContainer;
  }

  /**
   * Sets the chart container for the statistics view.
   *
   * @param chartContainer The VBox container to hold the statistics chart
   */
  public void setChartContainer(VBox chartContainer) {
    this.chartContainer = chartContainer;
  }

  /**
   * Creates and adds a genre count statistic view to the chart container. The method initializes
   * the view, calculates the genre data, and updates the chart accordingly.
   *
   * @param cinematics List of cinematic items to analyze
   * @param types      List of media types to consider in the analysis
   * @param states     List of states to filter the cinematics
   * @throws IOException If the FXML file cannot be loaded
   */
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

  /**
   * Initializes the genre count bar chart with provided data and sets up a listener for filtering
   * by minimum genre count.
   *
   * @param cinematics List of cinematic items to analyze
   * @param types      List of media types to consider in the analysis
   * @param states     List of states to filter the cinematics
   */
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

  /**
   * Updates the genre count bar chart with filtered data based on the provided criteria. Filters by
   * media type, state, and minimum count threshold.
   *
   * @param cinematics List of cinematic items to analyze
   * @param types      List of media types to consider in the analysis
   * @param states     List of states to filter the cinematics
   */
  private void updateChart(List<Cinematic> cinematics, List<Type> types, List<State> states) {
    GenreCountStrategy genreCountStrategy = new GenreCountStrategy();
    Map<String, Integer> genreCountData = genreCountStrategy.calculate(cinematics, types, states);

    int minCount = 0;
    try {
      minCount = minGenreCountInput.getText().isEmpty() ? 0
          : Integer.parseInt(minGenreCountInput.getText());
    } catch (NumberFormatException e) {
      logger.logError("minRating got an error");
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