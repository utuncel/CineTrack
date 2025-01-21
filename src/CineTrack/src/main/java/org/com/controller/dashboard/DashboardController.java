package org.com.controller.dashboard;

import java.io.IOException;
import java.util.EnumMap;
import java.util.Map;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import org.com.model.models.DashboardModel;
import org.com.model.enums.State;
import org.com.model.enums.StatisticStrategy;
import org.com.model.enums.Type;
import org.com.service.LogService;


public class DashboardController {

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

  //For page switch
  public DashboardController () {
  }

  public void setDashboardModel(DashboardModel dashboardModel) {
    logger.logInfo("Initializing Dashboard Controller with model");
    try {
      this.dashboardModel = dashboardModel;
      dashboardModel.removeAllStatisticStrategies();
      initializeCheckBoxMaps();
      setupTypeCheckBoxListeners();
      setupStateCheckBoxListeners();
      setupStatisticStrategyCheckBoxListeners();
      logger.logInfo("Dashboard Controller successfully initialized");
    } catch (Exception e) {
      logger.logError("Error initializing Dashboard Controller: " + e.getMessage());
    }
  }

  private void initializeCheckBoxMaps() {
    logger.logInfo("Initializing checkbox mappings");
    try {
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

      logger.logInfo("Checkbox mappings successfully initialized");
    } catch (Exception e) {
      logger.logError("Error initializing checkbox mappings: " + e.getMessage());
    }
  }

  private void setupTypeCheckBoxListeners() {
    logger.logInfo("Setting up type checkbox listeners");
    typeCheckBoxMap.forEach((type, checkBox) ->
        checkBox.setOnAction(event -> handleTypeCheckBoxAction(type, checkBox.isSelected())));
  }

  private void setupStateCheckBoxListeners() {
    logger.logInfo("Setting up state checkbox listeners");
    stateCheckBoxMap.forEach((state, checkBox) ->
        checkBox.setOnAction(event -> handleStateCheckBoxAction(state, checkBox.isSelected())));
  }

  private void setupStatisticStrategyCheckBoxListeners() {
    logger.logInfo("Setting up statistic strategy checkbox listeners");
    for (Map.Entry<StatisticStrategy, CheckBox> entry : strategyCheckBoxMap.entrySet()) {
      StatisticStrategy strategy = entry.getKey();
      CheckBox checkBox = entry.getValue();

      if (checkBox != null) {
        checkBox.setOnAction(
            event -> handleStrategyCheckBoxAction(strategy, checkBox.isSelected()));
      } else {
        logger.logWarning("CheckBox for strategy " + strategy + " is null");
      }
    }
  }

  private void handleTypeCheckBoxAction(Type type, boolean isSelected) {
    logger.logInfo(
        "Type filter changed: " + type + " is now " + (isSelected ? "selected" : "deselected"));
    if (isSelected) {
      dashboardModel.addType(type);
    } else {
      dashboardModel.removeType(type);
    }
    updateDashboard();
  }

  private void handleStateCheckBoxAction(State state, boolean isSelected) {
    logger.logInfo(
        "State filter changed: " + state + " is now " + (isSelected ? "selected" : "deselected"));
    if (isSelected) {
      dashboardModel.addState(state);
    } else {
      dashboardModel.removeState(state);
    }
    updateDashboard();
  }

  private void handleStrategyCheckBoxAction(StatisticStrategy strategy, boolean isSelected) {
    logger.logInfo("Statistic strategy changed: " + strategy + " is now " + (isSelected ? "enabled"
        : "disabled"));
    try {
      if (isSelected) {
        dashboardModel.addStatisticStrategy(strategy);
        addStrategyChart(strategy);
        logger.logInfo("Successfully added chart for strategy: " + strategy);
      } else {
        dashboardModel.removeStatisticStrategy(strategy);
        removeChart(getChartIdForStrategy(strategy));
        logger.logInfo("Successfully removed chart for strategy: " + strategy);
      }
    } catch (Exception e) {
      logger.logError("Error handling strategy change for " + strategy + ": " + e.getMessage());
    }
  }

  private void addStrategyChart(StatisticStrategy strategy) {
    logger.logInfo("Adding chart for strategy: " + strategy);
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
      }
      logger.logInfo("Successfully added chart for strategy: " + strategy);
    } catch (IOException e) {
      logger.logError("Failed to add chart for strategy " + strategy + ": " + e.getMessage());
      throw new RuntimeException("Failed to add chart for strategy: " + strategy, e);
    }
  }

  private void addAverageRatingChart() throws IOException {
    logger.logInfo("Adding Average Rating chart");
    try {
      AverageRatingStatisticController averageRatingStatisticController = new AverageRatingStatisticController(
          chartContainer);
      averageRatingStatisticController.addAverageRatingStatistic(dashboardModel.getCinematics(),
          dashboardModel.getTypes(), dashboardModel.getStates());
      logger.logInfo("Successfully added Average Rating chart");
    } catch (IOException e) {
      logger.logError("Error adding Average Rating chart: " + e.getMessage());
      throw e;
    }
  }

  private void addGenreCountChart() throws IOException {
    logger.logInfo("Adding Genre Count chart");
    try {
      GenreCountStatisticController genreCountController = new GenreCountStatisticController(
          chartContainer);
      genreCountController.addGenreCountStatistic(dashboardModel.getCinematics(),
          dashboardModel.getTypes(), dashboardModel.getStates());
      logger.logInfo("Successfully added Genre Count chart");
    } catch (IOException e) {
      logger.logError("Error adding Genre Count chart: " + e.getMessage());
      throw e;
    }
  }

  private void addStateCountChart() throws IOException {
    logger.logInfo("Adding State Count chart");
    try {
      StateCountStatisticController stateCountController = new StateCountStatisticController(
          chartContainer);
      stateCountController.addStateCountStatistic(dashboardModel.getCinematics(),
          dashboardModel.getTypes(), dashboardModel.getStates());
      logger.logInfo("Successfully added State Count chart");
    } catch (IOException e) {
      logger.logError("Error adding State Count chart: " + e.getMessage());
      throw e;
    }
  }

  private void addTypeCountChart() throws IOException {
    logger.logInfo("Adding Type Count chart");
    try {
      TypeCountStatisticController typeCountController = new TypeCountStatisticController(
          chartContainer);
      typeCountController.addTypeCountStatistic(dashboardModel.getCinematics(),
          dashboardModel.getTypes(), dashboardModel.getStates());
      logger.logInfo("Successfully added Type Count chart");
    } catch (IOException e) {
      logger.logError("Error adding Type Count chart: " + e.getMessage());
      throw e;
    }
  }

  private void addGenreRatingChart() throws IOException {
    logger.logInfo("Adding Genre Rating chart");
    try {
      GenreRatingStatisticController genreRatingController = new GenreRatingStatisticController(
          chartContainer);
      genreRatingController.addGenreRatingStatistic(dashboardModel.getCinematics(),
          dashboardModel.getTypes(), dashboardModel.getStates());
      logger.logInfo("Successfully added Genre Rating chart");
    } catch (IOException e) {
      logger.logError("Error adding Genre Rating chart: " + e.getMessage());
      throw e;
    }
  }

  private void addActorRatingChart() throws IOException {
    logger.logInfo("Adding Actor Rating chart");
    try {
      ActorRatingStatisticController actorRatingController = new ActorRatingStatisticController(
          chartContainer);
      actorRatingController.addActorRatingStatistic(dashboardModel.getCinematics(),
          dashboardModel.getTypes(), dashboardModel.getStates());
      logger.logInfo("Successfully added Actor Rating chart");
    } catch (IOException e) {
      logger.logError("Error adding Actor Rating chart: " + e.getMessage());
      throw e;
    }
  }

  private void removeChart(String chartId) {
    logger.logInfo("Removing chart with ID: " + chartId);
    try {
      chartContainer.getChildren()
          .removeIf(node -> node.getId() != null && node.getId().equals(chartId));
      logger.logInfo("Successfully removed chart: " + chartId);
    } catch (Exception e) {
      logger.logError("Error removing chart " + chartId + ": " + e.getMessage());
    }
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
        logger.logWarning("Unknown strategy type encountered: " + strategy);
        return "";
    }
  }

  private void updateDashboard() {
    logger.logInfo("Updating dashboard with current filters and strategies");
    try {
      chartContainer.getChildren().clear();
      dashboardModel.getStatisticStrategy().forEach(this::addStrategyChart);
      logger.logInfo("Dashboard successfully updated");
    } catch (Exception e) {
      logger.logError("Failed to update dashboard: " + e.getMessage());
      throw new RuntimeException("Failed to update dashboard", e);
    }
  }
}