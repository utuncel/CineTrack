package org.com.model;

import java.util.ArrayList;
import java.util.List;
import org.com.model.enums.State;
import org.com.model.enums.StatisticStrategy;
import org.com.model.enums.Type;

public class DashboardModel {

  private final List<Type> types = new ArrayList<>();
  private final List<StatisticStrategy> statisticStrategies = new ArrayList<>();
  private final List<State> states = new ArrayList<>();
  private final List<Listener> listeners = new ArrayList<Listener>();
  private List<Cinematic> cinematics = new ArrayList<>();

  //Default settings
  public DashboardModel() {
    types.add(Type.MOVIE);
    types.add(Type.ANIME);
    types.add(Type.SERIES);

    states.add(State.FINISHED);
    states.add(State.WATCHING);
    states.add(State.DROPPED);
  }

  public List<Cinematic> getCinematics() {
    return cinematics;
  }

  public void setCinematics(List<Cinematic> cinematics) {
    this.cinematics.clear();
    this.cinematics.addAll(cinematics);
    notifyObservers();
  }

  private void notifyObservers() {
    listeners.forEach(listener -> listener.onChange(this));
  }

  public void addType(Type type) {
    if (!types.contains(type)) {
      types.add(type);
    }
  }

  public void removeType(Type type) {
    types.remove(type);
  }

  public void addState(State state) {
    if (!states.contains(state)) {
      states.add(state);
    }
  }

  public void removeState(State state) {
    states.remove(state);
  }

  public List<Type> getTypes() {
    return types;
  }

  public List<State> getStates() {
    return states;
  }

  public void removeAllStatisticStrategies(){
    statisticStrategies.clear();
  }

  public void addStatisticStrategy(StatisticStrategy statisticStrategy) {
    if (!statisticStrategies.contains(statisticStrategy)) {
      statisticStrategies.add(statisticStrategy);
    }
  }

  public void removeStatisticStrategy(StatisticStrategy statisticStrategy) {
    statisticStrategies.remove(statisticStrategy);
  }

  public void addCinematic(Cinematic cinematic) {
    if (!cinematics.contains(cinematic)) {
      cinematics.add(cinematic);
    }
  }

  public List<StatisticStrategy> getStatisticStrategy() {
    return statisticStrategies;
  }

  public interface Listener {

    void onChange(DashboardModel dashboardModel);
  }
}
