package org.com.model.domain.statistics;

import java.util.List;
import org.com.model.domain.Cinematic;
import org.com.model.enums.State;
import org.com.model.enums.Type;

public class ImdbRatingAverageStrategy implements StatisticStrategy<Double> {

  @Override
  public Double calculate(List<Cinematic> cinematics, List<Type> types, List<State> states) {
    double averageRating = 0.0;
    int count = 0;

    for (Cinematic cinematic : cinematics) {
      if (types.contains(cinematic.getType()) && states.contains(cinematic.getState())
          && cinematic.getImdbRating() != 0) {
        averageRating = averageRating + (cinematic.getImdbRating());
        count++;
      }
    }
    if (count == 0) {
      return 0.0;
    }
    return Math.round((averageRating / count) * 100.0) / 100.0;
  }
}

