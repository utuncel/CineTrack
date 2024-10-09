package org.com.Models.Statistic;

import org.com.Models.Cinematic;
import org.com.Models.Enums.Type;

import java.util.List;

public class PercentageRatingDifferenceStrategy implements StatisticStrategy<Double> {
    MyRatingAverageStrategy myRatingAverageStrategy = new MyRatingAverageStrategy();
    ImdbRatingAverageStrategy imdbRatingAverageStrategy = new ImdbRatingAverageStrategy();

    @Override
    public Double calculate(List<Cinematic> cinematics, List<Type> types) {
        double myRating = myRatingAverageStrategy.calculate(cinematics, types);
        double imdbRating = imdbRatingAverageStrategy.calculate(cinematics, types);

        if (imdbRating == 0) {
            return 0.0;
        }

        return ((myRating - imdbRating) / imdbRating) * 100;
    }
}
