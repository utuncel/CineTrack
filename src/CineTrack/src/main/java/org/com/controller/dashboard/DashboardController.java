package org.com.controller.dashboard;

import java.io.IOException;
import java.util.EnumMap;
import java.util.Map;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import org.com.models.DashboardModel;
import org.com.models.enums.State;
import org.com.models.enums.StatisticStrategy;
import org.com.models.enums.Type;


public class DashboardController {

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
    this.dashboardModel = dashboardModel;
    initializeCheckBoxMaps();
    setupTypeCheckBoxListeners();
    setupStateCheckBoxListeners();
    setupStatisticStrategyCheckBoxListeners();
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

  private void setupTypeCheckBoxListeners() {
    typeCheckBoxMap.forEach((type, checkBox) ->
        checkBox.setOnAction(event -> handleTypeCheckBoxAction(type, checkBox.isSelected())));
  }

  private void setupStateCheckBoxListeners() {
    stateCheckBoxMap.forEach((state, checkBox) ->
        checkBox.setOnAction(event -> handleStateCheckBoxAction(state, checkBox.isSelected())));
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

  private void setupStatisticStrategyCheckBoxListeners() {
    for (Map.Entry<StatisticStrategy, CheckBox> entry : strategyCheckBoxMap.entrySet()) {
      StatisticStrategy strategy = entry.getKey();
      CheckBox checkBox = entry.getValue();

      if (checkBox != null) {
        checkBox.setOnAction(
            event -> handleStrategyCheckBoxAction(strategy, checkBox.isSelected()));
      } else {
        System.err.println("Warning: CheckBox for strategy " + strategy + " is null");
      }
    }
  }

  private void handleStrategyCheckBoxAction(StatisticStrategy strategy, boolean isSelected) {
    if (isSelected) {
      dashboardModel.addStatisticStrategy(strategy);
      addStrategyChart(strategy);
    } else {
      dashboardModel.removeStatisticStrategy(strategy);
      removeChart(getChartIdForStrategy(strategy));
    }
  }

  private void addStrategyChart(StatisticStrategy strategy) {
    try {
      switch (strategy) {
        case AVERAGE_RATING_STRATEGY:
          addAverageRatingChart();
          break;
        case GENRE_COUNT_STRATEGY:
          addGenreCountChart();
          break;
        case STATE_COUNT_STRATEGY:
          addStateCountChart();
          break;
        case TYPE_COUNT_STRATEGY:
          addTypeCountChart();
          break;
        case GENRE_RATING_STRATEGY:
          addGenreRatingChart();
          break;
        case ACTOR_RATING_STRATEGY:
          addActorRatingChart();
          break;
        // Add other cases as needed
      }
    } catch (IOException e) {
      throw new RuntimeException("Failed to add chart for strategy: " + strategy, e);
    }
  }

  private void addAverageRatingChart() throws IOException {
    AverageRatingStatisticController averageRatingStatisticController = new AverageRatingStatisticController(
        chartContainer);
    averageRatingStatisticController.addAverageRatingStatistic(dashboardModel.getCinematics(),
        dashboardModel.getTypes(), dashboardModel.getStates());
  }

  private void addGenreCountChart() throws IOException {
    GenreCountStatisticController genreCountController = new GenreCountStatisticController(
        chartContainer);
    genreCountController.addGenreCountStatistic(dashboardModel.getCinematics(),
        dashboardModel.getTypes(), dashboardModel.getStates());
  }

  private void addStateCountChart() throws IOException {
    StateCountStatisticController stateCountController = new StateCountStatisticController(
        chartContainer);
    stateCountController.addStateCountStatistic(dashboardModel.getCinematics(),
        dashboardModel.getTypes(), dashboardModel.getStates());
  }

  private void addTypeCountChart() throws IOException {
    TypeCountStatisticController typeCountController = new TypeCountStatisticController(
        chartContainer);
    typeCountController.addTypeCountStatistic(dashboardModel.getCinematics(),
        dashboardModel.getTypes(), dashboardModel.getStates());
  }

  private void addGenreRatingChart() throws IOException {
    GenreRatingStatisticController genreRatingController = new GenreRatingStatisticController(
        chartContainer);
    genreRatingController.addGenreRatingStatistic(dashboardModel.getCinematics(),
        dashboardModel.getTypes(), dashboardModel.getStates());
  }

  private void addActorRatingChart() throws IOException {
    ActorRatingStatisticController actorRatingController = new ActorRatingStatisticController(
        chartContainer);
    actorRatingController.addActorRatingStatistic(dashboardModel.getCinematics(),
        dashboardModel.getTypes(), dashboardModel.getStates());
  }

  private void removeChart(String chartId) {
    chartContainer.getChildren()
        .removeIf(node -> node.getId() != null && node.getId().equals(chartId));
  }

  private String getChartIdForStrategy(StatisticStrategy strategy) {
    switch (strategy) {
      case AVERAGE_RATING_STRATEGY:
        return "averageRating";
      case GENRE_COUNT_STRATEGY:
        return "genreCount";
      case STATE_COUNT_STRATEGY:
        return "stateCount";
      case TYPE_COUNT_STRATEGY:
        return "typeCount";
      case GENRE_RATING_STRATEGY:
        return "genreRating";
      case ACTOR_RATING_STRATEGY:
        return "actorRating";
      default:
        return "";
    }
  }

  private void updateDashboard() {
    try {
      chartContainer.getChildren().clear();
      dashboardModel.getStatisticStrategy().forEach(this::addStrategyChart);
    } catch (Exception e) {
      throw new RuntimeException("Failed to update dashboard", e);
    }
  }
}