package org.com.controller.dashboard;

import java.io.IOException;
import java.util.EnumMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import org.com.model.enums.State;
import org.com.model.enums.StatisticStrategy;
import org.com.model.enums.Type;
import org.com.model.models.DashboardModel;
import org.com.service.LogService;

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

  public CheckBox movieCheckBox;
  public CheckBox animeCheckBox;
  public CheckBox seriesCheckBox;
  public CheckBox averageRatingCheckBox;
  public CheckBox actorRatingCheckBox;
  public CheckBox genreRatingCheckBox;
  public CheckBox genreCountCheckBox;
  public CheckBox typeCountCheckBox;
  public CheckBox stateCountCheckBox;
  public CheckBox toWatchCheckBox;
  public CheckBox droppedCheckBox;
  public CheckBox finishedCheckBox;
  public CheckBox watchingCheckBox;
  public VBox chartContainer;
  public StackPane contentPane;

  private DashboardModel dashboardModel;

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

  private void setupCheckBoxListeners() {
    setupTypeCheckBoxListeners();
    setupStateCheckBoxListeners();
    setupStrategyCheckBoxListeners();
  }

  private void setupTypeCheckBoxListeners() {
    typeCheckBoxMap.forEach((type, checkBox) ->
        checkBox.setOnAction(event -> handleTypeCheckBoxAction(type, checkBox.isSelected())));
  }

  private void setupStateCheckBoxListeners() {
    stateCheckBoxMap.forEach((state, checkBox) ->
        checkBox.setOnAction(event -> handleStateCheckBoxAction(state, checkBox.isSelected())));
  }

  private void setupStrategyCheckBoxListeners() {
    strategyCheckBoxMap.forEach((strategy, checkBox) ->
        checkBox.setOnAction(
            event -> handleStrategyCheckBoxAction(strategy, checkBox.isSelected())));
  }

  private void handleTypeCheckBoxAction(Type type, boolean isSelected) {
    if (isSelected) {
      dashboardModel.addType(type);
    } else {
      dashboardModel.removeType(type);
    }
    updateDashboard();
  }

  private void handleStateCheckBoxAction(State state, boolean isSelected) {
    if (isSelected) {
      dashboardModel.addState(state);
    } else {
      dashboardModel.removeState(state);
    }
    updateDashboard();
  }

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

  private void removeChart(String chartId) {
    chartContainer.getChildren()
        .removeIf(node -> node.getId() != null && node.getId().equals(chartId));
  }

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

  private void updateDashboard() {
    chartContainer.getChildren().clear();
    dashboardModel.getStatisticStrategies()
        .forEach(strategy ->
            Optional.ofNullable(STRATEGY_CHART_HANDLERS.get(strategy))
                .ifPresent(handler -> handler.accept(this)));
  }
}