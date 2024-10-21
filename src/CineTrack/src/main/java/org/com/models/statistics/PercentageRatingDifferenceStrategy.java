package org.com.models.statistics;

import java.util.List;
import org.com.models.Cinematic;
import org.com.models.enums.State;
import org.com.models.enums.Type;

public class PercentageRatingDifferenceStrategy implements StatisticStrategy<Double> {

  MyRatingAverageStrategy myRatingAverageStrategy = new MyRatingAverageStrategy();
  ImdbRatingAverageStrategy imdbRatingAverageStrategy = new ImdbRatingAverageStrategy();

  @Override
  public Double calculate(List<Cinematic> cinematics, List<Type> types, List<State> states) {
    double myRating = myRatingAverageStrategy.calculate(cinematics, types, states);
    double imdbRating = imdbRatingAverageStrategy.calculate(cinematics, types, states);

    if (imdbRating == 0) {
      return 0.0;
    }

    return ((myRating - imdbRating) / imdbRating) * 100;
  }
}
