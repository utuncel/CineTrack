package org.com.Controller.Dashboard;

import javafx.scene.control.CheckBox;
import javafx.scene.layout.VBox;
import org.com.Models.DashboardModel;
import org.com.Models.Enums.StatisticStrategy;
import org.com.Models.Enums.Type;

import java.io.IOException;
import java.util.EnumMap;
import java.util.Map;


public class DashboardController {
    public CheckBox movieCheckBox;
    public CheckBox animeCheckBox;
    public CheckBox seriesCheckBox;
    public CheckBox averageRatingCheckBox;
    public CheckBox actorRatingCheckBox;
    public CheckBox genreRatingCheckBox;
    public CheckBox genreCountCheckBox;
    public CheckBox typeCountCheckBox;
    public CheckBox stateCountCheckBox;
    public VBox chartContainer;

    private DashboardModel dashboardModel;

    private final Map<Type, CheckBox> typeCheckBoxMap = new EnumMap<>(Type.class);
    private final Map<StatisticStrategy, CheckBox> strategyCheckBoxMap = new EnumMap<>(StatisticStrategy.class);

    public void setDashboardModel(DashboardModel dashboardModel) {
        this.dashboardModel = dashboardModel;
        initializeCheckBoxMaps();
        setupTypeCheckBoxListeners();
        setupStatisticStrategyCheckBoxListeners();
    }

    private void initializeCheckBoxMaps() {
        typeCheckBoxMap.put(Type.MOVIE, movieCheckBox);
        typeCheckBoxMap.put(Type.ANIME, animeCheckBox);
        typeCheckBoxMap.put(Type.SERIES, seriesCheckBox);

        strategyCheckBoxMap.put(StatisticStrategy.AVERAGE_RATING_STRATEGY, averageRatingCheckBox);
        //strategyCheckBoxMap.put(StatisticStrategy.ACTOR_RATING_STRATEGY, actorRatingCheckBox);
        //strategyCheckBoxMap.put(StatisticStrategy.GENRE_RATING_STRATEGY, genreRatingCheckBox);
        strategyCheckBoxMap.put(StatisticStrategy.GENRE_COUNT_STRATEGY, genreCountCheckBox);
        //strategyCheckBoxMap.put(StatisticStrategy.TYPE_COUNT_STRATEGY, typeCountCheckBox);
        strategyCheckBoxMap.put(StatisticStrategy.STATE_COUNT_STRATEGY, stateCountCheckBox);
    }

    private void setupTypeCheckBoxListeners() {
        typeCheckBoxMap.forEach((type, checkBox) ->
                checkBox.setOnAction(event -> handleTypeCheckBoxAction(type, checkBox.isSelected())));
    }

    private void handleTypeCheckBoxAction(Type type, boolean isSelected) {
        if (isSelected) {
            dashboardModel.addType(type);
        } else {
            dashboardModel.removeType(type);
        }
        updateDashboard();
    }

    private void setupStatisticStrategyCheckBoxListeners() {
        for (Map.Entry<StatisticStrategy, CheckBox> entry : strategyCheckBoxMap.entrySet()) {
            StatisticStrategy strategy = entry.getKey();
            CheckBox checkBox = entry.getValue();

            if (checkBox != null) {
                checkBox.setOnAction(event -> handleStrategyCheckBoxAction(strategy, checkBox.isSelected()));
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
                // Add other cases as needed
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to add chart for strategy: " + strategy, e);
        }
    }

    private void addAverageRatingChart() throws IOException {
        AverageRatingStatisticController averageRatingStatisticController = new AverageRatingStatisticController(chartContainer);
        averageRatingStatisticController.addAverageRatingStatistic(dashboardModel.getCinematics(), dashboardModel.getTypes());
    }

    private void addGenreCountChart() throws IOException {
        GenreCountStatisticController genreCountController = new GenreCountStatisticController(chartContainer);
        genreCountController.addGenreCountStatistic(dashboardModel.getCinematics(), dashboardModel.getTypes());
    }

    private void addStateCountChart() throws IOException {
        StateCountStatisticController stateCountController = new StateCountStatisticController(chartContainer);
        stateCountController.addStateCountStatistic(dashboardModel.getCinematics(), dashboardModel.getTypes());
    }

    private void removeChart(String chartId) {
        chartContainer.getChildren().removeIf(node -> node.getId() != null && node.getId().equals(chartId));
    }

    private String getChartIdForStrategy(StatisticStrategy strategy) {
        switch (strategy) {
            case AVERAGE_RATING_STRATEGY:
                return "averageRating";
            case GENRE_COUNT_STRATEGY:
                return "genreCount";
            case STATE_COUNT_STRATEGY:
                return "stateCount";
            // Add other cases as needed
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