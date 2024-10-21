package org.com.models.statistics;

import java.util.List;
import org.com.models.Cinematic;
import org.com.models.enums.State;
import org.com.models.enums.Type;
import org.com.models.records.TypeCount;

public class TypeCountStrategy implements StatisticStrategy<TypeCount> {

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
