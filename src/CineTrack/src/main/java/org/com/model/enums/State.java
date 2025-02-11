package org.com.model.enums;

/**
 * An enumeration representing the state of a cinematic item.
 *
 * <p>This enum is used to track the viewing status of a movie, anime, or series in the
 * application. Each state indicates the user's progress or intent regarding the item.</p>
 *
 * <ul>
 *   <li>{@link #FINISHED} - Indicates that the cinematic item has been fully watched.</li>
 *   <li>{@link #WATCHING} - Indicates that the cinematic item is currently being watched.</li>
 *   <li>{@link #DROPPED} - Indicates that the cinematic item was started but not completed.</li>
 *   <li>{@link #TOWATCH} - Indicates that the cinematic item is planned to be watched in the future.</li>
 * </ul>
 *
 * @author umut
 * @version 1.0
 */
public enum State {
  /**
   * The cinematic item has been fully watched.
   */
  FINISHED,

  /**
   * The cinematic item is currently being watched.
   */
  WATCHING,

  /**
   * The cinematic item was started but not completed.
   */
  DROPPED,

  /**
   * The cinematic item is planned to be watched in the future.
   */
  TOWATCH
}
