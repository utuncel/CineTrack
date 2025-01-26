package org.com.model.domain;


import java.time.LocalDateTime;

/**
 * Represents a log entry for the application.
 *
 * <p>The `Log` class is designed to capture and manage log details such as timestamp,
 * log level, message content, and the associated user. Logs can be created using a builder pattern
 * for flexibility and clarity.</p>
 *
 * <h3>Features:</h3>
 * <ul>
 *   <li>Tracks log details: ID, timestamp, level, message, and user.</li>
 *   <li>Supports automatic timestamp creation upon initialization.</li>
 *   <li>Provides a builder pattern for convenient log entry construction.</li>
 * </ul>
 */
public class Log {

  private Long id;

  private LocalDateTime timestamp;

  private String level;

  private String message;

  private User user;

  /**
   * Protected no-argument constructor.
   * <p>This constructor is required Hibernate.</p>
   */
  protected Log() {
    this.timestamp = LocalDateTime.now();
  }

  /**
   * Constructs a new log entry with the specified level and message.
   *
   * @param level   The severity level of the log.
   * @param message The message describing the log event.
   */
  public Log(String level, String message) {
    this.timestamp = LocalDateTime.now();
    this.level = level;
    this.message = message;
  }

  /**
   * Returns a builder for creating `Log` instances.
   *
   * @return A `LoggerBuilder` instance.
   */
  public static LoggerBuilder builder() {
    return new LoggerBuilder();
  }

  /**
   * Retrieves the unique identifier of the log entry.
   *
   * @return The log entry's ID.
   */
  public Long getId() {
    return id;
  }

  /**
   * Sets the unique identifier of the log entry.
   * <p>This method is not used but needed.</p>
   *
   * @param id The new ID to set.
   */
  protected void setId(Long id) {
    this.id = id;
  }

  /**
   * Retrieves the timestamp of the log entry.
   * <p>This method is not used but needed.</p>
   *
   * @return The timestamp of the log entry.
   */
  public LocalDateTime getTimestamp() {
    return timestamp;
  }

  /**
   * Sets the timestamp of the log entry.
   * <p>This method is not used but needed.</p>
   *
   * @param timestamp The new timestamp to set.
   */
  protected void setTimestamp(LocalDateTime timestamp) {
    this.timestamp = timestamp;
  }

  /**
   * Retrieves the severity level of the log entry.
   *
   * @return The severity level of the log.
   */
  public String getLevel() {
    return level;
  }

  /**
   * Sets the severity level of the log entry.
   * <p>This method is not used but needed.</p>
   *
   * @param level The new level to set.
   */
  public void setLevel(String level) {
    this.level = level;
  }

  /**
   * Retrieves the message of the log entry.
   *
   * @return The message of the log entry.
   */
  public String getMessage() {
    return message;
  }

  /**
   * Sets the message of the log entry.
   *
   * @param message The new message to set.
   */
  public void setMessage(String message) {
    this.message = message;
  }

  /**
   * Retrieves the user associated with this log entry.
   *
   * @return The user associated with the log entry.
   */
  public User getUser() {
    return user;
  }

  /**
   * Sets the user associated with this log entry.
   *
   * @param user The new user to associate with the log entry.
   */
  public void setUser(User user) {
    this.user = user;
  }

  /**
   * A builder for creating instances of `Log`.
   */
  public static class LoggerBuilder {

    private String level;
    private String message;
    private User user;

    /**
     * Sets the severity level for the log entry.
     *
     * @param level The severity level of the log.
     * @return The current `LoggerBuilder` instance.
     */
    public LoggerBuilder level(String level) {
      this.level = level;
      return this;
    }

    /**
     * Sets the message for the log entry.
     *
     * @param message The message of the log.
     * @return The current `LoggerBuilder` instance.
     */
    public LoggerBuilder message(String message) {
      this.message = message;
      return this;
    }

    /**
     * Sets the user associated with the log entry.
     *
     * @param user The user to associate with the log.
     * @return The current `LoggerBuilder` instance.
     */
    public LoggerBuilder user(User user) {
      this.user = user;
      return this;
    }

    /**
     * Builds a new instance of `Log` using the set parameters.
     *
     * @return A new `Log` instance.
     */
    public Log build() {
      Log log = new Log(level, message);
      log.setUser(user);
      return log;
    }
  }
}