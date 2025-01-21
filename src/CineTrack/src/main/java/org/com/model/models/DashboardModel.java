package org.com.model.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.com.model.domain.Cinematic;
import org.com.model.enums.State;
import org.com.model.enums.StatisticStrategy;
import org.com.model.enums.Type;

public class DashboardModel {

  private final List<Type> types = new ArrayList<>();
  private final List<StatisticStrategy> statisticStrategies = new ArrayList<>();
  private final List<State> states = new ArrayList<>();
  private final List<Listener> listeners = new ArrayList<>();
  private final List<Cinematic> cinematics = new ArrayList<>();

  // Default settings
  public DashboardModel() {
    Collections.addAll(types, Type.MOVIE, Type.ANIME, Type.SERIES);
    Collections.addAll(states, State.FINISHED, State.WATCHING, State.DROPPED);
  }

  public List<Cinematic> getCinematics() {
    return Collections.unmodifiableList(cinematics);
  }

  // Setter for cinematics with notification
  public void setCinematics(List<Cinematic> cinematics) {
    this.cinematics.clear();
    this.cinematics.addAll(cinematics);
    notifyObservers();
  }

  // Notify all registered listeners
  private void notifyObservers() {
    listeners.forEach(listener -> listener.onChange(this));
  }

  public void addType(Type type) {
    if (!types.contains(type)) {
      types.add(type);
      notifyObservers();
    }
  }

  public void removeType(Type type) {
    if (types.remove(type)) {
      notifyObservers();
    }
  }

  public List<Type> getTypes() {
    return Collections.unmodifiableList(types);
  }

  public void addState(State state) {
    if (!states.contains(state)) {
      states.add(state);
      notifyObservers();
    }
  }

  public void removeState(State state) {
    if (states.remove(state)) {
      notifyObservers();
    }
  }

  public List<State> getStates() {
    return Collections.unmodifiableList(states);
  }

  public void addStatisticStrategy(StatisticStrategy statisticStrategy) {
    if (!statisticStrategies.contains(statisticStrategy)) {
      statisticStrategies.add(statisticStrategy);
      notifyObservers();
    }
  }

  public void removeStatisticStrategy(StatisticStrategy statisticStrategy) {
    if (statisticStrategies.remove(statisticStrategy)) {
      notifyObservers();
    }
  }

  public void removeAllStatisticStrategies() {
    statisticStrategies.clear();
    notifyObservers();
  }

  public List<StatisticStrategy> getStatisticStrategies() {
    return Collections.unmodifiableList(statisticStrategies);
  }

  public void addCinematic(Cinematic cinematic) {
    if (!cinematics.contains(cinematic)) {
      cinematics.add(cinematic);
      notifyObservers();
    }
  }

  // Listener interface
  public interface Listener {

    void onChange(DashboardModel dashboardModel);
  }
}
