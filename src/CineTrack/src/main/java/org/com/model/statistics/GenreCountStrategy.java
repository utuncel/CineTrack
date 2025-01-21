package org.com.model.statistics;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.com.model.Cinematic;
import org.com.model.enums.State;
import org.com.model.enums.Type;

public class GenreCountStrategy implements StatisticStrategy<Map<String, Integer>> {

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