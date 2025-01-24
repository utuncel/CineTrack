package org.com.service;

import java.io.IOException;
import java.util.List;
import javafx.scene.layout.VBox;
import org.com.controller.dashboard.ActorRatingStatisticController;
import org.com.controller.dashboard.AverageRatingStatisticController;
import org.com.controller.dashboard.GenreCountStatisticController;
import org.com.controller.dashboard.GenreRatingStatisticController;
import org.com.controller.dashboard.StateCountStatisticController;
import org.com.controller.dashboard.TypeCountStatisticController;
import org.com.model.domain.Cinematic;
import org.com.model.enums.State;
import org.com.model.enums.Type;

public class ChartService {

  private final LogService logger;
  private final VBox chartContainer;

  public ChartService(VBox chartContainer) {
    this.logger = LogService.getInstance();
    this.chartContainer = chartContainer;
  }

  public void addChart(ChartType chartType, List<Cinematic> cinematics, List<Type> types,
      List<State> states) {
    try {
      switch (chartType) {
        case AVERAGE_RATING -> addAverageRatingChart(cinematics, types, states);
        case GENRE_COUNT -> addGenreCountChart(cinematics, types, states);
        case STATE_COUNT -> addStateCountChart(cinematics, types, states);
        case TYPE_COUNT -> addTypeCountChart(cinematics, types, states);
        case GENRE_RATING -> addGenreRatingChart(cinematics, types, states);
        case ACTOR_RATING -> addActorRatingChart(cinematics, types, states);
      }
    } catch (IOException e) {
      logger.logError("Error adding " + chartType + " chart: " + e.getMessage());
    }
  }

  private void addAverageRatingChart(List<Cinematic> cinematics, List<Type> types,
      List<State> states) throws IOException {
    new AverageRatingStatisticController(chartContainer)
        .addAverageRatingStatistic(cinematics, types, states);
  }

  private void addGenreCountChart(List<Cinematic> cinematics, List<Type> types, List<State> states)
      throws IOException {
    new GenreCountStatisticController(chartContainer)
        .addGenreCountStatistic(cinematics, types, states);
  }

  private void addStateCountChart(List<Cinematic> cinematics, List<Type> types, List<State> states)
      throws IOException {
    new StateCountStatisticController(chartContainer)
        .addStateCountStatistic(cinematics, types, states);
  }

  private void addTypeCountChart(List<Cinematic> cinematics, List<Type> types, List<State> states)
      throws IOException {
    new TypeCountStatisticController(chartContainer)
        .addTypeCountStatistic(cinematics, types, states);
  }

  private void addGenreRatingChart(List<Cinematic> cinematics, List<Type> types, List<State> states)
      throws IOException {
    new GenreRatingStatisticController(chartContainer)
        .addGenreRatingStatistic(cinematics, types, states);
  }

  private void addActorRatingChart(List<Cinematic> cinematics, List<Type> types, List<State> states)
      throws IOException {
    new ActorRatingStatisticController(chartContainer)
        .addActorRatingStatistic(cinematics, types, states);
  }

  public enum ChartType {
    AVERAGE_RATING,
    GENRE_COUNT,
    STATE_COUNT,
    TYPE_COUNT,
    GENRE_RATING,
    ACTOR_RATING
  }
}