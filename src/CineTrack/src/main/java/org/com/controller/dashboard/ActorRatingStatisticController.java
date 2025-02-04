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

/**
 * Controller for managing the display of watch state statistics in the dashboard. Handles the
 * visualization of the actor ratings.
 *
 * @author umut
 * @version 1.0
 * @see org.com.model.domain.statistics.ActorRatingsStrategy
 */
public class ActorRatingStatisticController {

  private final LogService logger = LogService.getInstance();
  @FXML
  private BarChart<String, Number> actorRatingBarChart;
  @FXML
  private TextField minRatingInput;
  @FXML
  private CategoryAxis xAxis;
  private VBox chartContainer;

  /**
   * Default constructor for FXML initialization.
   */
  public ActorRatingStatisticController() {
  }

  /**
   * Constructor with a container for the chart.
   *
   * @param chartContainer The VBox container for displaying the actors rating chart.
   */
  public ActorRatingStatisticController(VBox chartContainer) {
    this.chartContainer = chartContainer;
  }

  /**
   * Sets the container for the genre rating chart.
   *
   * @param chartContainer The VBox container to hold the chart.
   */
  public void setChartContainer(VBox chartContainer) {
    this.chartContainer = chartContainer;
  }

  /**
   * Creates and adds an actor rating statistic view to the chart container. Initializes the chart
   * and filters the data based on the minimum rating input.
   *
   * <p>This method loads the FXML for the actor rating statistic view, initializes the chart with
   * actor ratings, and adds the view to the chart container.</p>
   *
   * @param cinematics List of cinematic items to analyze
   * @param types      List of media types to filter the cinematics
   * @param states     List of states to consider in the count
   * @throws IOException If the FXML file cannot be loaded
   */
  public void addActorRatingStatistic(List<Cinematic> cinematics, List<Type> types,
      List<State> states) throws IOException {
    FXMLLoader loader = new FXMLLoader(
        getClass().getResource("/org/com/view/dashboard/ActorRatingStatisticView.fxml"));
    HBox genreRatingStatistic = loader.load();

    ActorRatingStatisticController controller = loader.getController();
    controller.setChartContainer(this.chartContainer);

    controller.initializeChart(cinematics, types, states);

    if (chartContainer != null) {
      chartContainer.getChildren().add(genreRatingStatistic);
    }
  }

  /**
   * Initializes the actor rating chart and sets up the listener for the minimum rating input. This
   * method ensures that the chart is updated whenever the minimum rating value changes.
   *
   * @param cinematics List of cinematic items to analyze
   * @param types      List of media types to filter the cinematics
   * @param states     List of states to consider in the count
   */
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

  /**
   * Updates the actor rating chart based on the provided cinematics, types, states, and the minimum
   * rating. This method filters actors based on the minimum rating input and updates the chart with
   * the relevant data.
   *
   * @param cinematics List of cinematic items to analyze
   * @param types      List of media types to filter the cinematics
   * @param states     List of states to consider in the count
   */
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
