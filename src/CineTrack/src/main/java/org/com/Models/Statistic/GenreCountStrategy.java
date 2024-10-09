package org.com.Models.Statistic;

import org.com.Models.Cinematic;
import org.com.Models.Enums.State;
import org.com.Models.Enums.Type;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GenreCountStrategy implements StatisticStrategy<Map<String, Integer>> {
    @Override
    public Map<String, Integer> calculate(List<Cinematic> cinematics, List<Type> types) {
        Map<String, Integer> genreCounts = new HashMap<>();
        for (Cinematic cinematic : cinematics) {
            if (types.contains(cinematic.getType()) && cinematic.getState() != State.TOWATCH) {
                for (String genre : cinematic.getGenres()) {
                    genreCounts.merge(genre, 1, Integer::sum);
                }
            }
        }

        return genreCounts;
    }
}