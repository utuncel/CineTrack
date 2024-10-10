package org.com.Models.Statistic;

import org.com.Models.Cinematic;
import org.com.Models.Enums.State;
import org.com.Models.Enums.Type;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//TODO filter genres with x mapsize out
public class GenreRatingsStrategy implements StatisticStrategy<Map<String, List<Double>>> {
    @Override
    public Map<String, List<Double>> calculate(List<Cinematic> cinematics, List<Type> types, List<State> states) {

        Map<String, List<Double>> genreRatings = new HashMap<>();

        for (Cinematic cinematic : cinematics) {
            if (types.contains(cinematic.getType()) && states.contains(cinematic.getState())) {
                for (String genre : cinematic.getGenres()) {
                    List<Double> ratingData = genreRatings.getOrDefault(genre, Arrays.asList(0.0, 0.0, 0.0, 0.0));

                    double myRatingSum = ratingData.get(0) + cinematic.getMyRating();
                    double myRatingCount = ratingData.get(1) + 1;
                    double imdbRatingSum = ratingData.get(2) + cinematic.getImdbRating();
                    double imdbRatingCount = ratingData.get(3) + 1;

                    genreRatings.put(genre, Arrays.asList(myRatingSum, myRatingCount, imdbRatingSum, imdbRatingCount));
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
