package org.com.Models.Statistic;

import org.com.Models.Cinematic;
import org.com.Models.Enums.State;
import org.com.Models.Enums.Type;

import java.util.List;

public interface StatisticStrategy<T> {
    T calculate(List<Cinematic> cinematics, List<Type> types, List<State> states);
}
