package org.com.Models.Statistic;

import org.com.Models.Cinematic;
import org.com.Models.Enums.State;
import org.com.Models.Enums.Type;

import java.util.List;

public class ImdbRatingAverageStrategy implements StatisticStrategy<Double> {
    @Override
    public Double calculate(List<Cinematic> cinematics, List<Type> types) {
        double averageRating = 0.0;
        int count = 0;

        for (Cinematic cinematic : cinematics) {
            if (types.contains(cinematic.getType()) && cinematic.getState() != State.TOWATCH && cinematic.getImdbRating() != 0) {
                averageRating = averageRating + (cinematic.getImdbRating());
                count++;
            }
        }
        return Math.round((averageRating / count) * 100.0) / 100.0;
    }
}

