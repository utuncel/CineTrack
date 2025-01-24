package org.com.service;

import java.util.EnumMap;
import java.util.Map;
import javafx.scene.control.CheckBox;
import org.com.model.enums.State;
import org.com.model.enums.StatisticStrategy;
import org.com.model.enums.Type;

public class CheckboxConfigurationService {

  public static Map<Type, CheckBox> createTypeCheckBoxMap(
      CheckBox movieCheckBox,
      CheckBox animeCheckBox,
      CheckBox seriesCheckBox
  ) {
    Map<Type, CheckBox> typeCheckBoxMap = new EnumMap<>(Type.class);
    typeCheckBoxMap.put(Type.MOVIE, movieCheckBox);
    typeCheckBoxMap.put(Type.ANIME, animeCheckBox);
    typeCheckBoxMap.put(Type.SERIES, seriesCheckBox);
    return typeCheckBoxMap;
  }

  public static Map<State, CheckBox> createStateCheckBoxMap(
      CheckBox finishedCheckBox,
      CheckBox watchingCheckBox,
      CheckBox droppedCheckBox,
      CheckBox toWatchCheckBox
  ) {
    Map<State, CheckBox> stateCheckBoxMap = new EnumMap<>(State.class);
    stateCheckBoxMap.put(State.FINISHED, finishedCheckBox);
    stateCheckBoxMap.put(State.WATCHING, watchingCheckBox);
    stateCheckBoxMap.put(State.DROPPED, droppedCheckBox);
    stateCheckBoxMap.put(State.TOWATCH, toWatchCheckBox);
    return stateCheckBoxMap;
  }

  public static Map<StatisticStrategy, CheckBox> createStrategyCheckBoxMap(
      CheckBox averageRatingCheckBox,
      CheckBox actorRatingCheckBox,
      CheckBox genreRatingCheckBox,
      CheckBox genreCountCheckBox,
      CheckBox typeCountCheckBox,
      CheckBox stateCountCheckBox
  ) {
    Map<StatisticStrategy, CheckBox> strategyCheckBoxMap = new EnumMap<>(StatisticStrategy.class);
    strategyCheckBoxMap.put(StatisticStrategy.AVERAGE_RATING_STRATEGY, averageRatingCheckBox);
    strategyCheckBoxMap.put(StatisticStrategy.ACTOR_RATING_STRATEGY, actorRatingCheckBox);
    strategyCheckBoxMap.put(StatisticStrategy.GENRE_RATING_STRATEGY, genreRatingCheckBox);
    strategyCheckBoxMap.put(StatisticStrategy.GENRE_COUNT_STRATEGY, genreCountCheckBox);
    strategyCheckBoxMap.put(StatisticStrategy.TYPE_COUNT_STRATEGY, typeCountCheckBox);
    strategyCheckBoxMap.put(StatisticStrategy.STATE_COUNT_STRATEGY, stateCountCheckBox);
    return strategyCheckBoxMap;
  }
}