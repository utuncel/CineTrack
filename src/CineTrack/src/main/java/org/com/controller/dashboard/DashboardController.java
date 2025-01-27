package org.com.controller.dashboard;

import java.io.IOException;
import java.util.EnumMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import org.com.model.enums.State;
import org.com.model.enums.StatisticStrategy;
import org.com.model.enums.Type;
import org.com.model.models.DashboardModel;
import org.com.service.LogService;

/**
 * Controller class for managing the dashboard UI, handling user interactions with checkboxes for
 * various statistics and type filters, and updating the displayed charts.
 *
 * @author umut
 * @version 1.0
 */
public class DashboardController {

  private static final Map<StatisticStrategy, Consumer<DashboardController>> STRATEGY_CHART_HANDLERS = Map.of(
      StatisticStrategy.AVERAGE_RATING_STRATEGY, DashboardController::addAverageRatingChart,
      StatisticStrategy.GENRE_COUNT_STRATEGY, DashboardController::addGenreCountChart,
      StatisticStrategy.STATE_COUNT_STRATEGY, DashboardController::addStateCountChart,
      StatisticStrategy.TYPE_COUNT_STRATEGY, DashboardController::addTypeCountChart,
      StatisticStrategy.GENRE_RATING_STRATEGY, DashboardController::addGenreRatingChart,
      StatisticStrategy.ACTOR_RATING_STRATEGY, DashboardController::addActorRatingChart
  );

  private final LogService logger = LogService.getInstance();
  private final Map<Type, CheckBox> typeCheckBoxMap = new EnumMap<>(Type.class);
  private final Map<State, CheckBox> stateCheckBoxMap = new EnumMap<>(State.class);
  private final Map<StatisticStrategy, CheckBox> strategyCheckBoxMap = new EnumMap<>(
      StatisticStrategy.class);
  @FXML
  private CheckBox movieCheckBox;
  @FXML
  private CheckBox animeCheckBox;
  @FXML
  private CheckBox seriesCheckBox;
  @FXML
  private CheckBox averageRatingCheckBox;
  @FXML
  private CheckBox actorRatingCheckBox;
  @FXML
  private CheckBox genreRatingCheckBox;
  @FXML
  private CheckBox genreCountCheckBox;
  @FXML
  private CheckBox typeCountCheckBox;
  @FXML
  private CheckBox stateCountCheckBox;
  @FXML
  private CheckBox toWatchCheckBox;
  @FXML
  private CheckBox droppedCheckBox;
  @FXML
  private CheckBox finishedCheckBox;
  @FXML
  private CheckBox watchingCheckBox;
  @FXML
  private VBox chartContainer;
  @FXML
  private StackPane contentPane;

  private DashboardModel dashboardModel;

  /**
   * Sets the DashboardModel for this controller, initializes the checkbox maps, and sets up the
   * checkbox listeners.
   *
   * @param dashboardModel the DashboardModel to be set
   */
  public void setDashboardModel(DashboardModel dashboardModel) {
    try {
      this.dashboardModel = dashboardModel;
      dashboardModel.removeAllStatisticStrategies();
      initializeCheckBoxMaps();
      setupCheckBoxListeners();
    } catch (Exception e) {
      logger.logError("Error initializing Dashboard Controller: " + e.getMessage());
    }
  }

  /**
   * Initializes the maps that associate types, states, and statistic strategies with their
   * corresponding checkboxes.
   */
  private void initializeCheckBoxMaps() {
    typeCheckBoxMap.put(Type.MOVIE, movieCheckBox);
    typeCheckBoxMap.put(Type.ANIME, animeCheckBox);
    typeCheckBoxMap.put(Type.SERIES, seriesCheckBox);

    stateCheckBoxMap.put(State.FINISHED, finishedCheckBox);
    stateCheckBoxMap.put(State.WATCHING, watchingCheckBox);
    stateCheckBoxMap.put(State.DROPPED, droppedCheckBox);
    stateCheckBoxMap.put(State.TOWATCH, toWatchCheckBox);

    strategyCheckBoxMap.put(StatisticStrategy.AVERAGE_RATING_STRATEGY, averageRatingCheckBox);
    strategyCheckBoxMap.put(StatisticStrategy.ACTOR_RATING_STRATEGY, actorRatingCheckBox);
    strategyCheckBoxMap.put(StatisticStrategy.GENRE_RATING_STRATEGY, genreRatingCheckBox);
    strategyCheckBoxMap.put(StatisticStrategy.GENRE_COUNT_STRATEGY, genreCountCheckBox);
    strategyCheckBoxMap.put(StatisticStrategy.TYPE_COUNT_STRATEGY, typeCountCheckBox);
    strategyCheckBoxMap.put(StatisticStrategy.STATE_COUNT_STRATEGY, stateCountCheckBox);
  }

  /**
   * Sets up listeners for all checkboxes in the UI (types, states, and statistic strategies).
   */
  private void setupCheckBoxListeners() {
    setupTypeCheckBoxListeners();
    setupStateCheckBoxListeners();
    setupStrategyCheckBoxListeners();
  }

  /**
   * Sets up listeners for the type-related checkboxes (Movie, Anime, Series).
   */
  private void setupTypeCheckBoxListeners() {
    typeCheckBoxMap.forEach((type, checkBox) ->
        checkBox.setOnAction(event -> handleTypeCheckBoxAction(type, checkBox.isSelected())));
  }

  /**
   * Sets up listeners for the state-related checkboxes (Finished, Watching, Dropped, To Watch).
   */
  private void setupStateCheckBoxListeners() {
    stateCheckBoxMap.forEach((state, checkBox) ->
        checkBox.setOnAction(event -> handleStateCheckBoxAction(state, checkBox.isSelected())));
  }

  /**
   * Sets up listeners for the statistic strategy checkboxes (Average Rating, Genre Count, etc.).
   */
  private void setupStrategyCheckBoxListeners() {
    strategyCheckBoxMap.forEach((strategy, checkBox) ->
        checkBox.setOnAction(
            event -> handleStrategyCheckBoxAction(strategy, checkBox.isSelected())));
  }

  /**
   * Handles the action of a type checkbox being selected or deselected.
   *
   * @param type       the type associated with the checkbox
   * @param isSelected whether the checkbox is selected or not
   */
  private void handleTypeCheckBoxAction(Type type, boolean isSelected) {
    if (isSelected) {
      dashboardModel.addType(type);
    } else {
      dashboardModel.removeType(type);
    }
    updateDashboard();
  }

  /**
   * Handles the action of a state checkbox being selected or deselected.
   *
   * @param state      the state associated with the checkbox
   * @param isSelected whether the checkbox is selected or not
   */
  private void handleStateCheckBoxAction(State state, boolean isSelected) {
    if (isSelected) {
      dashboardModel.addState(state);
    } else {
      dashboardModel.removeState(state);
    }
    updateDashboard();
  }

  /**
   * Handles the action of a statistic strategy checkbox being selected or deselected.
   *
   * @param strategy   the strategy associated with the checkbox
   * @param isSelected whether the checkbox is selected or not
   */
  private void handleStrategyCheckBoxAction(StatisticStrategy strategy, boolean isSelected) {
    try {
      if (isSelected) {
        dashboardModel.addStatisticStrategy(strategy);
        Optional.ofNullable(STRATEGY_CHART_HANDLERS.get(strategy))
            .ifPresent(handler -> handler.accept(this));
      } else {
        dashboardModel.removeStatisticStrategy(strategy);
        removeChart(getChartIdForStrategy(strategy));
      }
    } catch (Exception e) {
      logger.logError("Error handling strategy change: " + e.getMessage());
    }
  }

  /**
   * Adds the average rating chart to the dashboard.
   */
  private void addAverageRatingChart() {
    try {
      new AverageRatingStatisticController(chartContainer)
          .addAverageRatingStatistic(dashboardModel.getCinematics(),
              dashboardModel.getTypes(),
              dashboardModel.getStates());
    } catch (IOException e) {
      logger.logError("Error adding Average Rating chart: " + e.getMessage());
    }
  }

  /**
   * Adds the genre count chart to the dashboard.
   */
  private void addGenreCountChart() {
    try {
      new GenreCountStatisticController(chartContainer)
          .addGenreCountStatistic(dashboardModel.getCinematics(),
              dashboardModel.getTypes(),
              dashboardModel.getStates());
    } catch (IOException e) {
      logger.logError("Error adding Genre Count chart: " + e.getMessage());
    }
  }

  /**
   * Adds the state count chart to the dashboard.
   */
  private void addStateCountChart() {
    try {
      new StateCountStatisticController(chartContainer)
          .addStateCountStatistic(dashboardModel.getCinematics(),
              dashboardModel.getTypes(),
              dashboardModel.getStates());
    } catch (IOException e) {
      logger.logError("Error adding State Count chart: " + e.getMessage());
    }
  }

  /**
   * Adds the type count chart to the dashboard.
   */
  private void addTypeCountChart() {
    try {
      new TypeCountStatisticController(chartContainer)
          .addTypeCountStatistic(dashboardModel.getCinematics(),
              dashboardModel.getTypes(),
              dashboardModel.getStates());
    } catch (IOException e) {
      logger.logError("Error adding Type Count chart: " + e.getMessage());
    }
  }

  /**
   * Adds the genre rating chart to the dashboard.
   */
  private void addGenreRatingChart() {
    try {
      new GenreRatingStatisticController(chartContainer)
          .addGenreRatingStatistic(dashboardModel.getCinematics(),
              dashboardModel.getTypes(),
              dashboardModel.getStates());
    } catch (IOException e) {
      logger.logError("Error adding Genre Rating chart: " + e.getMessage());
    }
  }

  /**
   * Adds the actor rating chart to the dashboard.
   */
  private void addActorRatingChart() {
    try {
      new ActorRatingStatisticController(chartContainer)
          .addActorRatingStatistic(dashboardModel.getCinematics(),
              dashboardModel.getTypes(),
              dashboardModel.getStates());
    } catch (IOException e) {
      logger.logError("Error adding Actor Rating chart: " + e.getMessage());
    }
  }

  /**
   * Removes a chart from the dashboard based on its ID.
   *
   * @param chartId the ID of the chart to be removed
   */
  private void removeChart(String chartId) {
    chartContainer.getChildren()
        .removeIf(node -> node.getId() != null && node.getId().equals(chartId));
  }

  /**
   * Returns the chart ID associated with a given statistic strategy.
   *
   * @param strategy the statistic strategy
   * @return the chart ID
   */
  private String getChartIdForStrategy(StatisticStrategy strategy) {
    return switch (strategy) {
      case AVERAGE_RATING_STRATEGY -> "averageRating";
      case GENRE_COUNT_STRATEGY -> "genreCount";
      case STATE_COUNT_STRATEGY -> "stateCount";
      case TYPE_COUNT_STRATEGY -> "typeCount";
      case GENRE_RATING_STRATEGY -> "genreRating";
      case ACTOR_RATING_STRATEGY -> "actorRating";
    };
  }

  /**
   * Updates the dashboard by clearing the current charts and adding new ones based on the selected
   * strategies.
   */
  private void updateDashboard() {
    chartContainer.getChildren().clear();
    dashboardModel.getStatisticStrategies()
        .forEach(strategy ->
            Optional.ofNullable(STRATEGY_CHART_HANDLERS.get(strategy))
                .ifPresent(handler -> handler.accept(this)));
  }
}