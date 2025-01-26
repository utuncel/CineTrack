package org.com.model.domain;

import org.com.model.enums.State;
import org.com.model.enums.Type;

/**
 * Represents a cinematic entity parsed from a CSV file.
 *
 * <p>The `CsvCinematic` class encapsulates the basic details of a cinematic, such as title,
 * rating, state, and type, which are typically extracted from a CSV file.</p>
 *
 * <h3>Features:</h3>
 * <ul>
 *   <li>Immutable design: All fields are final and set through a builder.</li>
 *   <li>Supports a fluent builder pattern for convenient object creation.</li>
 *   <li>Integrates enums for `State` and `Type` to enforce valid values.</li>
 * </ul>
 */
public class CsvCinematic {

  private final String title;
  private final int rating;
  private final State state;
  private final Type type;

  /**
   * Private constructor to initialize `CsvCinematic` with values from the builder.
   *
   * @param builder The `Builder` instance containing the values for the cinematic.
   */
  private CsvCinematic(Builder builder) {
    this.title = builder.title;
    this.state = builder.state;
    this.type = builder.type;
    this.rating = builder.rating;
  }

  /**
   * Retrieves the title of the cinematic.
   *
   * @return The title of the cinematic.
   */
  public String getTitle() {
    return title;
  }

  /**
   * Retrieves the user's personal rating of the cinematic.
   *
   * @return The personal rating of the cinematic.
   */
  public int getRating() {
    return rating;
  }

  /**
   * Retrieves the current state of the cinematic.
   *
   * @return The state of the cinematic.
   */
  public State getState() {
    return state;
  }

  /**
   * Retrieves the type of the cinematic.
   *
   * @return The type of the cinematic.
   */
  public Type getType() {
    return type;
  }

  /**
   * A builder for creating instances of `CsvCinematic`.
   *
   * <p>The `Builder` provides a fluent interface for setting individual attributes
   * and ensures immutability of the resulting `CsvCinematic` instance.</p>
   */
  public static class Builder {

    private String title;
    private int rating;
    private State state;
    private Type type;

    /**
     * Sets the title of the cinematic.
     *
     * @param title The title of the cinematic.
     * @return The current `Builder` instance.
     */
    public Builder withTitle(String title) {
      this.title = title;
      return this;
    }

    /**
     * Sets the user's personal rating of the cinematic.
     *
     * @param rating The personal rating (0-10).
     * @return The current `Builder` instance.
     */
    public Builder withRating(int rating) {
      this.rating = rating;
      return this;
    }

    /**
     * Sets the state of the cinematic.
     *
     * @param state The state of the cinematic (e.g., FINISHED, TO_WATCH).
     * @return The current `Builder` instance.
     */
    public Builder withState(State state) {
      this.state = state;
      return this;
    }

    /**
     * Sets the type of the cinematic.
     *
     * @param type The type of the cinematic (e.g., MOVIE, SERIES).
     * @return The current `Builder` instance.
     */
    public Builder withType(Type type) {
      this.type = type;
      return this;
    }

    /**
     * Builds a new `CsvCinematic` instance with the set attributes.
     *
     * @return A new `CsvCinematic` instance.
     */
    public CsvCinematic build() {
      return new CsvCinematic(this);
    }
  }


}
