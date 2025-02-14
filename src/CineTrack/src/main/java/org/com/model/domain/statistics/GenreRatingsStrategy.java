package org.com.model.domain.statistics;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.com.model.domain.Cinematic;
import org.com.model.enums.State;
import org.com.model.enums.Type;

/**
 * A strategy for calculating the average ratings (both personal and IMDb) for each genre.
 * <p>
 * This strategy calculates the average ratings for each genre based on the cinematics that match
 * the specified types and states. It considers both the user's rating and IMDb rating for each
 * genre.
 *
 * @author umut
 * @version 1.0
 */
public class GenreRatingsStrategy implements StatisticStrategy<Map<String, List<Double>>> {

  /**
   * Calculates the average personal and IMDb ratings for each genre.
   *
   * @param cinematics The list of cinematics to analyze.
   * @param types      The types of cinematics to include in the calculation (e.g., Movie, Series,
   *                   Anime).
   * @param states     The states of cinematics to include in the calculation (e.g., Watched,
   *                   ToWatch, Dropped).
   * @return A map where the key is the genre name and the value is a list containing the average
   * personal rating at index 0 and the average IMDb rating at index 1.
   */
  @Override
  public Map<String, List<Double>> calculate(List<Cinematic> cinematics, List<Type> types,
      List<State> states) {

    Map<String, List<Double>> genreRatings = new HashMap<>();

    for (Cinematic cinematic : cinematics) {
      if (types.contains(cinematic.getType()) && states.contains(cinematic.getState())) {
        for (String genre : cinematic.getGenres()) {
          List<Double> ratingData = genreRatings.getOrDefault(genre,
              Arrays.asList(0.0, 0.0, 0.0, 0.0));

          double myRatingSum = ratingData.get(0) + cinematic.getMyRating();
          double myRatingCount = ratingData.get(1) + 1;
          double imdbRatingSum = ratingData.get(2) + cinematic.getImdbRating();
          double imdbRatingCount = ratingData.get(3) + 1;

          genreRatings.put(genre,
              Arrays.asList(myRatingSum, myRatingCount, imdbRatingSum, imdbRatingCount));
        }
      }
    }

    Map<String, List<Double>> averageRatings = new HashMap<>();
    for (Map.Entry<String, List<Double>> entry : genreRatings.entrySet()) {
      String genre = entry.getKey();
      List<Double> ratingData = entry.getValue();

      double myRatingAverage = ratingData.get(0) / ratingData.get(1);
      double imdbRatingAverage = ratingData.get(2) / ratingData.get(3);

      averageRatings.put(genre, Arrays.asList(myRatingAverage, imdbRatingAverage));
    }

    return averageRatings;
  }

}
