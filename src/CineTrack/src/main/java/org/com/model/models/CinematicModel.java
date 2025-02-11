package org.com.model.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.com.model.domain.Cinematic;
import org.com.model.enums.State;
import org.com.model.enums.StatisticStrategy;
import org.com.model.enums.Type;

/**
 * The `CinematicModel` class represents the data model for the dashboard in the application.
 *
 * <p>This class manages the state of various filters, strategies, and cinematics that
 * are used in the dashboard. It provides methods to modify these settings while notifying
 * registered listeners about any changes, ensuring a reactive and dynamic UI.</p>
 *
 * <h2>Key Features:</h2>
 * <ul>
 *   <li>Maintains a list of types (e.g., MOVIE, ANIME, SERIES) for filtering dashboard data.</li>
 *   <li>Tracks selected states (e.g., FINISHED, WATCHING) for additional filtering options.</li>
 *   <li>Manages statistic strategies that determine which metrics to display.</li>
 *   <li>Supports observer pattern for real-time updates to the UI or other components.</li>
 *   <li>Handles a list of cinematics that are displayed or analyzed in the dashboard.</li>
 * </ul>
 *
 * @author umut
 * @version 1.0
 */
public class CinematicModel {

  private final List<Type> types = new ArrayList<>();
  private final List<StatisticStrategy> statisticStrategies = new ArrayList<>();
  private final List<State> states = new ArrayList<>();
  private final List<Listener> listeners = new ArrayList<>();
  private final List<Cinematic> cinematics = new ArrayList<>();

  /**
   * Constructs a new `CinematicModel` with default settings.
   * <p>Default settings include:</p>
   * <ul>
   *   <li>All types (MOVIE, ANIME, SERIES) are enabled.</li>
   *   <li>States FINISHED, WATCHING, and DROPPED are enabled.</li>
   * </ul>
   */
  public CinematicModel() {
    Collections.addAll(types, Type.MOVIE, Type.ANIME, Type.SERIES);
    Collections.addAll(states, State.FINISHED, State.WATCHING, State.DROPPED);
  }

  /**
   * Retrieves an unmodifiable list of all cinematics.
   *
   * @return An unmodifiable list of cinematics.
   */
  public List<Cinematic> getCinematics() {
    return Collections.unmodifiableList(cinematics);
  }

  /**
   * Sets the list of cinematics and notifies observers of the change.
   *
   * @param cinematics The new list of cinematics to set.
   */
  public void setCinematics(List<Cinematic> cinematics) {
    this.cinematics.clear();
    this.cinematics.addAll(cinematics);
    notifyObservers();
  }

  /**
   * Adds a new cinematic to the list and notifies observers.
   *
   * @param cinematic The cinematic to add.
   */
  public void addCinematic(Cinematic cinematic) {
    if (!cinematics.contains(cinematic)) {
      cinematics.add(cinematic);
      notifyObservers();
    }
  }


  /**
   * Retrieves an unmodifiable list of all types.
   *
   * @return An unmodifiable list of types.
   */
  public List<Type> getTypes() {
    return Collections.unmodifiableList(types);
  }


  /**
   * Adds a new type to the filter and notifies observers.
   *
   * @param type The type to add.
   */
  public void addType(Type type) {
    if (!types.contains(type)) {
      types.add(type);
      notifyObservers();
    }
  }

  /**
   * Removes a type from the filter and notifies observers.
   *
   * @param type The type to remove.
   */
  public void removeType(Type type) {
    if (types.remove(type)) {
      notifyObservers();
    }
  }

  /**
   * Retrieves an unmodifiable list of all states.
   *
   * @return An unmodifiable list of states.
   */
  public List<State> getStates() {
    return Collections.unmodifiableList(states);
  }

  /**
   * Adds a new state to the filter and notifies observers.
   *
   * @param state The state to add.
   */
  public void addState(State state) {
    if (!states.contains(state)) {
      states.add(state);
      notifyObservers();
    }
  }

  /**
   * Removes a state from the filter and notifies observers.
   *
   * @param state The state to remove.
   */
  public void removeState(State state) {
    if (states.remove(state)) {
      notifyObservers();
    }
  }

  /**
   * Retrieves an unmodifiable list of all statistic strategies.
   *
   * @return An unmodifiable list of statistic strategies.
   */
  public List<StatisticStrategy> getStatisticStrategies() {
    return Collections.unmodifiableList(statisticStrategies);
  }

  /**
   * Adds a new statistic strategy to the list and notifies observers.
   *
   * @param statisticStrategy The statistic strategy to add.
   */
  public void addStatisticStrategy(StatisticStrategy statisticStrategy) {
    if (!statisticStrategies.contains(statisticStrategy)) {
      statisticStrategies.add(statisticStrategy);
      notifyObservers();
    }
  }

  /**
   * Removes a statistic strategy from the list and notifies observers.
   *
   * @param statisticStrategy The statistic strategy to remove.
   */
  public void removeStatisticStrategy(StatisticStrategy statisticStrategy) {
    if (statisticStrategies.remove(statisticStrategy)) {
      notifyObservers();
    }
  }

  /**
   * Clears all statistic strategies and notifies observers.
   */
  public void removeAllStatisticStrategies() {
    statisticStrategies.clear();
    notifyObservers();
  }

  /**
   * Notifies all registered listeners about changes to the model.
   */
  private void notifyObservers() {
    listeners.forEach(listener -> listener.onChange(this));
  }


  /**
   * A listener interface for observing changes in the `CinematicModel`.
   */
  public interface Listener {

    /**
     * Called when the `CinematicModel` has changed.
     *
     * @param cinematicModel The updated `CinematicModel` instance.
     */
    void onChange(CinematicModel cinematicModel);
  }
}
