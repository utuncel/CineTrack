package org.com.model.enums;

/**
 * An enumeration representing different strategies for statistical analysis in the application.
 *
 * <p>Each strategy corresponds to a specific type of analysis that can be performed on the
 * cinematic data, such as counting genres or calculating average ratings.</p>
 *
 * <ul>
 *   <li>{@link #GENRE_COUNT_STRATEGY} - Counts the number of cinematics per genre.</li>
 *   <li>{@link #STATE_COUNT_STRATEGY} - Counts the number of cinematics by their state
 *       (e.g., finished, watching, dropped).</li>
 *   <li>{@link #TYPE_COUNT_STRATEGY} - Counts the number of cinematics by their type
 *       (e.g., movie, anime, series).</li>
 *   <li>{@link #ACTOR_RATING_STRATEGY} - Analyzes the average ratings of cinematics
 *       based on actors.</li>
 *   <li>{@link #GENRE_RATING_STRATEGY} - Analyzes the average ratings per genre.</li>
 *   <li>{@link #AVERAGE_RATING_STRATEGY} - Calculates the overall average rating of cinematics.</li>
 * </ul>
 *
 * @author umut
 * @version 1.0
 */
public enum StatisticStrategy {
  /**
   * Strategy for counting the number of cinematics per genre.
   */
  GENRE_COUNT_STRATEGY,

  /**
   * Strategy for counting the number of cinematics by their state (e.g., finished, watching,
   * dropped).
   */
  STATE_COUNT_STRATEGY,

  /**
   * Strategy for counting the number of cinematics by their type (e.g., movie, anime, series).
   */
  TYPE_COUNT_STRATEGY,

  /**
   * Strategy for analyzing the average ratings of cinematics based on actors.
   */
  ACTOR_RATING_STRATEGY,

  /**
   * Strategy for analyzing the average ratings per genre.
   */
  GENRE_RATING_STRATEGY,

  /**
   * Strategy for calculating the overall average rating of cinematics.
   */
  AVERAGE_RATING_STRATEGY,
}
