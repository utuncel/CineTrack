package org.com.models.statistics;

import java.util.List;
import org.com.models.Cinematic;
import org.com.models.enums.State;
import org.com.models.enums.Type;

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

