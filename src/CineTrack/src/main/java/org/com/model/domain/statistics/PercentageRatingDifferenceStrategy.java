package org.com.model.domain.statistics;

import java.util.List;
import org.com.model.domain.Cinematic;
import org.com.model.enums.State;
import org.com.model.enums.Type;

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
