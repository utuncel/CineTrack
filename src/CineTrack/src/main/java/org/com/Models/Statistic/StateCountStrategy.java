package org.com.Models.Statistic;

import org.com.Models.Cinematic;
import org.com.Models.Enums.State;
import org.com.Models.Enums.Type;
import org.com.Models.Records.StateCount;

import java.util.List;

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
