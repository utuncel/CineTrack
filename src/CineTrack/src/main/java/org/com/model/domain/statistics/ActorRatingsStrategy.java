package org.com.model.domain.statistics;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.com.model.domain.Cinematic;
import org.com.model.enums.State;
import org.com.model.enums.Type;

/**
 * A strategy for calculating the average ratings for each actor based on the cinematics.
 * <p>
 * This strategy calculates the average of both the user's rating and IMDb rating for each actor in
 * the list of cinematics, considering only the cinematics that match the specified types and
 * states.
 */
public class ActorRatingsStrategy implements StatisticStrategy<Map<String, List<Double>>> {

  /**
   * Calculates the average ratings (user's rating and IMDb rating) for each actor based on the
   * specified types and states of the cinematics.
   *
   * @param cinematics The list of cinematics to analyze.
   * @param types      The types of cinematics to include in the calculation (e.g., Movie, Series,
   *                   Anime).
   * @param states     The states of cinematics to include in the calculation (e.g., Watched,
   *                   ToWatch, Dropped).
   * @return A map where the key is the actor's name and the value is a list containing the average
   * user rating and IMDb rating.
   */
  @Override
  public Map<String, List<Double>> calculate(List<Cinematic> cinematics, List<Type> types,
      List<State> states) {

    Map<String, List<Double>> actorsRatings = new HashMap<>();

    for (Cinematic cinematic : cinematics) {
      if (types.contains(cinematic.getType()) && states.contains(cinematic.getState())) {
        for (String actors : cinematic.getActors()) {
          List<Double> ratingData = actorsRatings.getOrDefault(actors,
              Arrays.asList(0.0, 0.0, 0.0, 0.0));

          double myRatingSum = ratingData.get(0) + cinematic.getMyRating();
          double myRatingCount = ratingData.get(1) + 1;
          double imdbRatingSum = ratingData.get(2) + cinematic.getImdbRating();
          double imdbRatingCount = ratingData.get(3) + 1;

          actorsRatings.put(actors,
              Arrays.asList(myRatingSum, myRatingCount, imdbRatingSum, imdbRatingCount));
        }
      }
    }

    Map<String, List<Double>> averageRatings = new HashMap<>();
    for (Map.Entry<String, List<Double>> entry : actorsRatings.entrySet()) {
      String actor = entry.getKey();
      List<Double> ratingData = entry.getValue();

      double myRatingAverage = ratingData.get(0) / ratingData.get(1);
      double imdbRatingAverage = ratingData.get(2) / ratingData.get(3);

      averageRatings.put(actor, Arrays.asList(myRatingAverage, imdbRatingAverage));
    }

    return averageRatings;
  }

}