package org.com.Models.Statistic;

import org.com.Models.Cinematic;
import org.com.Models.Enums.Type;
import org.com.Models.Records.TypeCount;

import java.util.List;

public class TypeCountStrategy implements StatisticStrategy <TypeCount>{
    @Override
    public TypeCount calculate(List<Cinematic> cinematics, List<Type> types) {
        int movie = 0;
        int series = 0;
        int anime = 0;

        for (Cinematic cinematic : cinematics) {
            if (types.contains(cinematic.getType())) {
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
