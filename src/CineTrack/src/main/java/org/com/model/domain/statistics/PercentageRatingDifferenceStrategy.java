package org.com.model.domain.statistics;

import java.util.List;
import org.com.model.domain.Cinematic;
import org.com.model.enums.State;
import org.com.model.enums.Type;

/**
 * A strategy for calculating the percentage difference between the user's average rating and IMDb's
 * average rating for a given list of cinematics.
 * <p>
 * The percentage is calculated using the formula: ((user's average rating - IMDb's average rating)
 * / IMDb's average rating) * 100. If IMDb's rating is 0, the result will be 0%.
 *
 * @author umut
 * @version 1.0
 */
public class PercentageRatingDifferenceStrategy implements StatisticStrategy<Double> {

  MyRatingAverageStrategy myRatingAverageStrategy = new MyRatingAverageStrategy();
  ImdbRatingAverageStrategy imdbRatingAverageStrategy = new ImdbRatingAverageStrategy();

  /**
   * Calculates the percentage difference between the user's average rating and IMDb's average
   * rating for the provided cinematics filtered by type and state.
   *
   * @param cinematics The list of cinematics to analyze.
   * @param types      The types of cinematics to include in the calculation (e.g., Movie, Series,
   *                   Anime).
   * @param states     The states of cinematics to include in the calculation (e.g., Watched,
   *                   ToWatch, Dropped).
   * @return The percentage difference between the user's average rating and IMDb's average rating.
   * Returns 0.0 if IMDb's rating is 0.
   */
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
