package org.com.model.domain.statistics;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.com.model.domain.Cinematic;
import org.com.model.enums.State;
import org.com.model.enums.Type;

/**
 * A strategy for calculating the count of each genre in the list of cinematics.
 * <p>
 * This strategy counts how many times each genre appears across the cinematics that match the
 * specified types and states.
 *
 * @author umut
 * @version 1.0
 */
public class GenreCountStrategy implements StatisticStrategy<Map<String, Integer>> {

  /**
   * Calculates the count of each genre based on the specified types and states of the cinematics.
   *
   * @param cinematics The list of cinematics to analyze.
   * @param types      The types of cinematics to include in the calculation (e.g., Movie, Series,
   *                   Anime).
   * @param states     The states of cinematics to include in the calculation (e.g., Watched,
   *                   ToWatch, Dropped).
   * @return A map where the key is the genre name and the value is the count of cinematics in that
   * genre.
   */
  @Override
  public Map<String, Integer> calculate(List<Cinematic> cinematics, List<Type> types,
      List<State> states) {
    Map<String, Integer> genreCounts = new HashMap<>();
    for (Cinematic cinematic : cinematics) {
      if (types.contains(cinematic.getType()) && states.contains(cinematic.getState())) {
        for (String genre : cinematic.getGenres()) {
          genreCounts.merge(genre, 1, Integer::sum);
        }
      }
    }

    return genreCounts;
  }
}