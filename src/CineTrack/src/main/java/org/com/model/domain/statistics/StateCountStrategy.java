package org.com.model.domain.statistics;

import java.util.List;
import org.com.model.domain.Cinematic;
import org.com.model.enums.State;
import org.com.model.enums.Type;
import org.com.model.records.StateCount;

/**
 * A strategy for calculating the count of cinematics in different states (e.g., Finished, Dropped,
 * Watching, ToWatch) based on filtering criteria.
 *
 * @author umut
 * @version 1.0
 */
public class StateCountStrategy implements StatisticStrategy<StateCount> {

  /**
   * Calculates the count of cinematics in each state (Finished, Dropped, Watching, ToWatch),
   * filtered by the provided types and states.
   *
   * @param cinematics The list of cinematics to analyze.
   * @param types      The types of cinematics to include in the calculation (e.g., Movie, Series,
   *                   Anime).
   * @param states     The states of cinematics to include in the calculation (e.g., Watched,
   *                   ToWatch, Dropped).
   * @return A {@link StateCount} object containing the counts of each state.
   */
  @Override
  public StateCount calculate(List<Cinematic> cinematics, List<Type> types, List<State> states) {
    int towatch = 0;
    int finished = 0;
    int watching = 0;
    int dropped = 0;

    for (Cinematic cinematic : cinematics) {
      if (types.contains(cinematic.getType()) && states.contains(cinematic.getState())) {
        if (cinematic.getState() == State.DROPPED) {
          dropped++;
        } else if (cinematic.getState() == State.FINISHED) {
          finished++;
        } else if (cinematic.getState() == State.WATCHING) {
          watching++;
        } else if (cinematic.getState() == State.TOWATCH) {
          towatch++;
        }
      }
    }
    return new StateCount(finished, dropped, watching, towatch);
  }
}
