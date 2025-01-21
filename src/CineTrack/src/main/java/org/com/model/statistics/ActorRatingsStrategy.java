package org.com.model.statistics;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.com.model.Cinematic;
import org.com.model.enums.State;
import org.com.model.enums.Type;

public class ActorRatingsStrategy implements StatisticStrategy<Map<String, List<Double>>> {

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