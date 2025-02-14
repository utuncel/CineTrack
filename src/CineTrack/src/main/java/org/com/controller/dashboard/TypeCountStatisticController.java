package org.com.controller.dashboard;

import java.io.IOException;
import java.util.List;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.com.model.domain.Cinematic;
import org.com.model.domain.statistics.TypeCountStrategy;
import org.com.model.enums.State;
import org.com.model.enums.Type;
import org.com.model.records.TypeCount;

/**
 * Controller for managing the display of media type count statistics in the dashboard. Handles the
 * visualization of counts for movies, series, and anime using a custom statistic view.
 *
 * @author umut
 * @version 1.0
 * @see org.com.model.domain.statistics.TypeCountStrategy
 * @see org.com.model.records.TypeCount
 */
public class TypeCountStatisticController {

  @FXML
  public Label movie;
  @FXML
  public Label series;
  @FXML
  public Label anime;

  private VBox chartContainer;

  /**
   * Default constructor for FXML initialization.
   */
  public TypeCountStatisticController() {
  }

  /**
   * Constructor with chart container initialization.
   *
   * @param chartContainer The VBox container for the statistics chart
   */
  public TypeCountStatisticController(VBox chartContainer) {
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
   * Creates and adds a type count statistic view to the chart container. Calculates media type
   * counts using the TypeCountStrategy and updates the view accordingly.
   *
   * @param cinematics List of cinematic items to analyze
   * @param types      List of media types to consider in the count
   * @param states     List of states to filter the cinematics
   * @throws IOException If the FXML file cannot be loaded
   */
  public void addTypeCountStatistic(List<Cinematic> cinematics, List<Type> types,
      List<State> states) throws IOException {
    FXMLLoader loader = new FXMLLoader(
        getClass().getResource("/org/com/view/dashboard/TypeCountStatisticView.fxml"));
    HBox fourBlockStatistic = loader.load();

    TypeCountStatisticController controller = loader.getController();
    controller.setChartContainer(this.chartContainer);

    TypeCountStrategy typeCountStrategy = new TypeCountStrategy();
    TypeCount typeCountData = typeCountStrategy.calculate(cinematics, types, states);

    controller.setBlockValues(String.valueOf(typeCountData.movie()),
        String.valueOf(typeCountData.series()), String.valueOf(typeCountData.anime()));

    if (chartContainer != null) {
      chartContainer.getChildren().add(fourBlockStatistic);
    }
  }

  /**
   * Updates the display values for each media type count.
   *
   * @param movieCount  The number of movies to display
   * @param seriesCount The number of series to display
   * @param animeCount  The number of anime to display
   */
  public void setBlockValues(String movieCount, String seriesCount, String animeCount) {
    movie.setText(movieCount);
    series.setText(seriesCount);
    anime.setText(animeCount);
  }
}
