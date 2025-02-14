package org.com.model.domain.statistics;

import java.util.List;
import org.com.model.domain.Cinematic;
import org.com.model.enums.State;
import org.com.model.enums.Type;
import org.com.model.records.TypeCount;

/**
 * A strategy for calculating the count of cinematics by their type (Movie, Series, Anime).
 * Implements the {@link StatisticStrategy} interface for the {@link TypeCount} result type.
 *
 * @author umut
 * @version 1.0
 */
public class TypeCountStrategy implements StatisticStrategy<TypeCount> {

  /**
   * Calculates the count of cinematics by their type (Movie, Series, Anime) based on the provided
   * filters for types and states.
   *
   * @param cinematics The list of all cinematics to evaluate.
   * @param types      The types to include in the calculation (e.g., Movie, Series, Anime).
   * @param states     The states to include in the calculation (e.g., Watched, ToWatch, Dropped).
   * @return A {@link TypeCount} object containing the counts of movies, series, and anime.
   */
  @Override
  public TypeCount calculate(List<Cinematic> cinematics, List<Type> types, List<State> states) {
    int movie = 0;
    int series = 0;
    int anime = 0;

    for (Cinematic cinematic : cinematics) {
      if (types.contains(cinematic.getType()) && states.contains(cinematic.getState())) {
        if (cinematic.getType() == Type.MOVIE) {
          movie++;
        } else if (cinematic.getType() == Type.SERIES) {
          series++;
        } else if (cinematic.getType() == Type.ANIME) {
          anime++;
        }
      }
    }
    return new TypeCount(movie, series, anime);
  }
}
