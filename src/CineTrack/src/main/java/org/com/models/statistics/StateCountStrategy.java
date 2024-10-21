package org.com.models.statistics;

import java.util.List;
import org.com.models.Cinematic;
import org.com.models.enums.State;
import org.com.models.enums.Type;
import org.com.models.records.StateCount;

public class StateCountStrategy implements StatisticStrategy<StateCount> {

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
