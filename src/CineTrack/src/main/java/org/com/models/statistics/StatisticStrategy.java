package org.com.models.statistics;

import java.util.List;
import org.com.models.Cinematic;
import org.com.models.enums.State;
import org.com.models.enums.Type;

public interface StatisticStrategy<T> {

  T calculate(List<Cinematic> cinematics, List<Type> types, List<State> states);
}
