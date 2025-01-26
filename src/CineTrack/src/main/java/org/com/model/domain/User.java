package org.com.model.domain;

import java.util.List;

/**
 * Represents a user in the system.
 *
 * <p>The `User` class stores information about a user, including their unique identifier,
 * name, password, associated cinematics, and activity logs. It is designed to manage user-specific
 * data within the application, such as their watched movies and logs of actions.</p>
 *
 * <h3>Features:</h3>
 * <ul>
 *   <li>Tracks the user's ID, name, and password.</li>
 *   <li>Maintains a list of `Cinematic` objects associated with the user.</li>
 *   <li>Stores activity logs for the user, although currently not utilized.</li>
 * </ul>
 */
public class User {

  private Long id;

  private String name;

  private String password;

  private List<Cinematic> cinematics;

  private List<Log> logs;

  /**
   * Protected no-argument constructor.
   * <p>This constructor is required Hibernate.</p>
   */
  protected User() {
  }

  /**
   * Constructs a new `User` with the specified name and password.
   *
   * @param name     The username of the user.
   * @param password The password of the user.
   */
  public User(String name, String password) {
    this.name = name;
    this.password = password;
  }

  /**
   * Retrieves the user's unique identifier.
   *
   * @return The user's ID.
   */
  public Long getId() {
    return id;
  }

  /**
   * Sets the user's unique identifier.
   *
   * @param id The new ID to set.
   */
  public void setId(Long id) {
    this.id = id;
  }

  /**
   * Retrieves the user's name.
   *
   * @return The user's name.
   */
  public String getName() {
    return name;
  }

  /**
   * Sets the user's name.
   *
   * @param name The new name to set.
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Retrieves the user's password.
   *
   * @return The user's password.
   */
  public String getPassword() {
    return password;
  }

  /**
   * Sets the user's password.
   *
   * @param password The new password to set.
   */
  public void setPassword(String password) {
    this.password = password;
  }

  /**
   * Retrieves the list of cinematics associated with the user.
   *
   * @return A list of the user's cinematics.
   */
  public List<Cinematic> getCinematics() {
    return cinematics;
  }

  /**
   * Sets the list of cinematics associated with the user.
   *
   * @param cinematics The new list of cinematics to set.
   */
  public void setCinematics(List<Cinematic> cinematics) {
    this.cinematics = cinematics;
  }

  /**
   * Retrieves the list of logs associated with the user.
   *
   * @return A list of the user's logs.
   */
  public List<Log> getLogs() {
    return logs;
  }

  /**
   * Sets the list of logs associated with the user.
   * <p>This method is not used but needed.</p>
   *
   * @param logs The new list of logs to set.
   */
  public void setLogs(List<Log> logs) {
    this.logs = logs;
  }
}
