package org.com.controller.dashboard;

import java.io.IOException;
import java.util.List;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.com.model.domain.Cinematic;
import org.com.model.domain.statistics.StateCountStrategy;
import org.com.model.enums.State;
import org.com.model.enums.Type;
import org.com.model.records.StateCount;

/**
 * Controller for managing the display of watch state statistics in the dashboard. Handles the
 * visualization of counts for different watch states (finished, watching, dropped, to watch) using
 * a custom statistic view.
 *
 * @author umut
 * @version 1.0
 * @see org.com.model.domain.statistics.StateCountStrategy
 * @see org.com.model.records.StateCount
 */
public class StateCountStatisticController {

  @FXML
  private Label finished;
  @FXML
  private Label watching;
  @FXML
  private Label dropped;
  @FXML
  private Label toWatch;

  private VBox chartContainer;

  /**
   * Default constructor for FXML initialization.
   */
  public StateCountStatisticController() {
  }

  /**
   * Constructor with chart container initialization.
   *
   * @param chartContainer The VBox container for the statistics chart
   */
  public StateCountStatisticController(VBox chartContainer) {
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
   * Creates and adds a state count statistic view to the chart container. Calculates watch state
   * counts using the StateCountStrategy and updates the view accordingly.
   *
   * @param cinematics List of cinematic items to analyze
   * @param types      List of media types to filter the cinematics
   * @param states     List of states to consider in the count
   * @throws IOException If the FXML file cannot be loaded
   */
  public void addStateCountStatistic(List<Cinematic> cinematics, List<Type> types,
      List<State> states) throws IOException {
    FXMLLoader loader = new FXMLLoader(
        getClass().getResource("/dashboard/StateCountStatisticView.fxml"));
    HBox fourBlockStatistic = loader.load();

    StateCountStatisticController controller = loader.getController();
    controller.setChartContainer(this.chartContainer);

    StateCountStrategy stateCountStrategy = new StateCountStrategy();
    StateCount genreCountData = stateCountStrategy.calculate(cinematics, types, states);

    controller.setBlockValues(String.valueOf(genreCountData.finished()),
        String.valueOf(genreCountData.watching()), String.valueOf(genreCountData.dropped()),
        String.valueOf(genreCountData.towatch()));

    if (chartContainer != null) {
      chartContainer.getChildren().add(fourBlockStatistic);
    }
  }

  /**
   * Updates the display values for each watch state count.
   *
   * @param finishedCount The number of finished items
   * @param watchedCount  The number of items currently being watched
   * @param droppedCount  The number of dropped items
   * @param toWatchCount  The number of items to watch
   */
  public void setBlockValues(String finishedCount, String watchedCount, String droppedCount,
      String toWatchCount) {
    finished.setText(finishedCount);
    watching.setText(watchedCount);
    dropped.setText(droppedCount);
    toWatch.setText(toWatchCount);
  }
}
