package org.com.model.domain.statistics;

import java.util.List;
import org.com.model.domain.Cinematic;
import org.com.model.enums.State;
import org.com.model.enums.Type;

/**
 * A generic interface for implementing strategies to calculate statistics based on a list of
 * cinematics and filtering criteria.
 *
 * @param <T> The type of the result produced by the strategy.
 *
 * @author umut
 * @version 1.0
 */
public interface StatisticStrategy<T> {

  /**
   * Calculates a statistic based on the provided list of cinematics and filtering criteria.
   *
   * @param cinematics The list of cinematics to be analyzed.
   * @param types      The types of cinematics to include in the calculation (e.g., Movie, Series,
   *                   Anime).
   * @param states     The states of cinematics to include (e.g., Watched, ToWatch, Dropped).
   * @return The result of the statistic calculation, represented as an object of type {@code T}.
   */
  T calculate(List<Cinematic> cinematics, List<Type> types, List<State> states);
}
