package org.com.controller.dashboard;

import java.io.IOException;
import java.util.List;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.com.model.domain.Cinematic;
import org.com.model.domain.statistics.ImdbRatingAverageStrategy;
import org.com.model.domain.statistics.MyRatingAverageStrategy;
import org.com.model.enums.State;
import org.com.model.enums.Type;

/**
 * Controller for managing the display of watch state statistics in the dashboard. Handles the
 * visualization of counts for different watch states (finished, watching, dropped, to watch) using
 * a custom statistic view.
 *
 * <p>This controller works in conjunction with the StateCountStatisticView FXML file to display
 * the statistics for various states of the cinematics (e.g., finished, watching, dropped, to
 * watch). The states are calculated using a strategy pattern and displayed in the dashboard.</p>
 *
 * @author umut
 * @version 1.0
 * @see org.com.model.domain.statistics.StateCountStrategy
 * @see org.com.model.records.StateCount
 */
public class AverageRatingStatisticController {

  @FXML
  public Label myRatingValue;
  @FXML
  public Label imdbRatingValue;

  private VBox chartContainer;

  /**
   * Default constructor for FXML initialization.
   */
  public AverageRatingStatisticController() {
  }

  /**
   * Constructor with chart container initialization.
   *
   * @param chartContainer The VBox container for the statistics chart
   */
  public AverageRatingStatisticController(VBox chartContainer) {
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
   * Sets the data for personal and IMDb rating values.
   *
   * @param myRatingValue   The user's average rating as a string.
   * @param imdbRatingValue The IMDb average rating as a string.
   */
  public void setData(String myRatingValue, String imdbRatingValue) {
    this.myRatingValue.setText(myRatingValue);
    this.imdbRatingValue.setText(imdbRatingValue);
  }

  /**
   * Adds the average rating statistic to the dashboard.
   *
   * @param cinematics The list of cinematics to analyze.
   * @param types      The list of types to filter (e.g., Movie, Anime, Series).
   * @param states     The list of states to filter (e.g., Watched, Dropped).
   * @throws IOException If the FXML file cannot be loaded.
   */
  public void addAverageRatingStatistic(List<Cinematic> cinematics, List<Type> types,
      List<State> states) throws IOException {
    FXMLLoader loader = new FXMLLoader(
        getClass().getResource("/org/com/view/dashboard/AverageRatingStatisticView.fxml"));
    HBox personalRatingBlock = loader.load();

    AverageRatingStatisticController controller = loader.getController();
    controller.setChartContainer(this.chartContainer);

    MyRatingAverageStrategy myAverageRatingStrategy = new MyRatingAverageStrategy();
    ImdbRatingAverageStrategy imdbAverageRatingStrategy = new ImdbRatingAverageStrategy();

    controller.setData(
        String.valueOf(myAverageRatingStrategy.calculate(cinematics, types, states)),
        String.valueOf(imdbAverageRatingStrategy.calculate(cinematics, types, states))
    );

    if (chartContainer != null) {
      chartContainer.getChildren().add(personalRatingBlock);
    }
  }
}
