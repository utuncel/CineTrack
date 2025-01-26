package org.com.model.domain.statistics;

import java.util.List;
import org.com.model.domain.Cinematic;
import org.com.model.enums.State;
import org.com.model.enums.Type;

/**
 * A strategy for calculating the average of the user's ratings for a given list of cinematics.
 * <p>
 * The average is calculated by summing all the user's ratings for cinematics that match the
 * specified types and states, excluding cinematics with a rating of 0. The result is rounded to two
 * decimal places.
 */
public class MyRatingAverageStrategy implements StatisticStrategy<Double> {

  /**
   * Calculates the average of the user's ratings for the provided cinematics filtered by type and
   * state.
   *
   * @param cinematics The list of cinematics to analyze.
   * @param types      The types of cinematics to include in the calculation (e.g., Movie, Series,
   *                   Anime).
   * @param states     The states of cinematics to include in the calculation (e.g., Watched,
   *                   ToWatch, Dropped).
   * @return The average of the user's ratings, rounded to two decimal places. Returns 0.0 if no
   * ratings are found.
   */
  @Override
  public Double calculate(List<Cinematic> cinematics, List<Type> types, List<State> states) {
    double averageRating = 0.0;
    int count = 0;

    for (Cinematic cinematic : cinematics) {
      if (types.contains(cinematic.getType()) && states.contains(cinematic.getState())
          && cinematic.getMyRating() != 0) {
        averageRating = averageRating + (cinematic.getMyRating());
        count++;
      }
    }
    if (count == 0) {
      return 0.0;
    }
    return Math.round((averageRating / count) * 100.0) / 100.0;
  }
}
