package org.com.model.statistics;

import java.util.List;
import org.com.model.Cinematic;
import org.com.model.enums.State;
import org.com.model.enums.Type;

public interface StatisticStrategy<T> {

  T calculate(List<Cinematic> cinematics, List<Type> types, List<State> states);
}
